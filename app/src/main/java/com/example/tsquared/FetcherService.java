package com.example.tsquared;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Xml;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.yanus171.feedexfork.Constants;
import ru.yanus171.feedexfork.MainApplication;
import ru.yanus171.feedexfork.R;
import ru.yanus171.feedexfork.activity.EntryActivity;
import ru.yanus171.feedexfork.activity.HomeActivity;
import ru.yanus171.feedexfork.fragment.EntriesListFragment;
import ru.yanus171.feedexfork.parser.FeedFilters;
import ru.yanus171.feedexfork.parser.HTMLParser;
import ru.yanus171.feedexfork.parser.OPML;
import ru.yanus171.feedexfork.parser.OneWebPageParser;
import ru.yanus171.feedexfork.parser.RssAtomParser;
import ru.yanus171.feedexfork.provider.FeedData;
import ru.yanus171.feedexfork.provider.FeedData.EntryColumns;
import ru.yanus171.feedexfork.provider.FeedData.FeedColumns;
import ru.yanus171.feedexfork.provider.FeedData.TaskColumns;
import ru.yanus171.feedexfork.utils.ArticleTextExtractor;
import ru.yanus171.feedexfork.utils.Connection;
import ru.yanus171.feedexfork.utils.DebugApp;
import ru.yanus171.feedexfork.utils.Dog;
import ru.yanus171.feedexfork.utils.EntryUrlVoc;
import ru.yanus171.feedexfork.utils.FileUtils;
import ru.yanus171.feedexfork.utils.HtmlUtils;
import ru.yanus171.feedexfork.utils.LabelVoc;
import ru.yanus171.feedexfork.utils.NetworkUtils;
import com.example.tsquared.PrefUtils;
import ru.yanus171.feedexfork.utils.UiUtils;
import com.example.tsquared.EntryView;
import ru.yanus171.feedexfork.view.StatusText;

import static android.content.Intent.EXTRA_TEXT;
import static android.provider.BaseColumns._ID;
import static java.lang.Thread.MIN_PRIORITY;
import static ru.yanus171.feedexfork.Constants.DB_AND;
import static ru.yanus171.feedexfork.Constants.DB_IS_NOT_NULL;
import static ru.yanus171.feedexfork.Constants.DB_IS_NULL;
import static ru.yanus171.feedexfork.Constants.DB_OR;
import static ru.yanus171.feedexfork.Constants.EXTRA_FILENAME;
import static ru.yanus171.feedexfork.Constants.EXTRA_ID;
import static ru.yanus171.feedexfork.Constants.EXTRA_URI;
import static ru.yanus171.feedexfork.Constants.GROUP_ID;
import static ru.yanus171.feedexfork.Constants.URL_LIST;
import static ru.yanus171.feedexfork.MainApplication.OPERATION_NOTIFICATION_CHANNEL_ID;
import static ru.yanus171.feedexfork.MainApplication.getContext;
import static ru.yanus171.feedexfork.MainApplication.mImageFileVoc;
import static ru.yanus171.feedexfork.fragment.EntriesListFragment.GetWhereSQL;
import static ru.yanus171.feedexfork.fragment.EntriesListFragment.mCurrentUri;
import static ru.yanus171.feedexfork.parser.OPML.AUTO_BACKUP_OPML_FILENAME;
import static ru.yanus171.feedexfork.parser.OPML.EXTRA_REMOVE_EXISTING_FEEDS_BEFORE_IMPORT;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.CATEGORY_LIST_SEP;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.FEED_ID;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.IMAGES_SIZE;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.IS_FAVORITE;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.LINK;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.MOBILIZED_HTML;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.WHERE_FAVORITE;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.WHERE_NOT_FAVORITE;
import static ru.yanus171.feedexfork.provider.FeedData.EntryColumns.WHERE_READ;
import static ru.yanus171.feedexfork.provider.FeedDataContentProvider.SetNotifyEnabled;
import static ru.yanus171.feedexfork.provider.FeedDataContentProvider.URI_ENTRIES_FOR_FEED;
import static ru.yanus171.feedexfork.provider.FeedDataContentProvider.notifyChangeOnAllUris;
import static ru.yanus171.feedexfork.service.BroadcastActionReciever.Action;
import static com.example.tsquared.PrefUtils.MAX_IMAGE_DOWNLOAD_COUNT;
import static ru.yanus171.feedexfork.view.StatusText.GetPendingIntentRequestCode;

public class FetcherService extends IntentService {

    public static final String ACTION_REFRESH_FEEDS = FeedData.PACKAGE_NAME + ".REFRESH";
    public static final String ACTION_MOBILIZE_FEEDS = FeedData.PACKAGE_NAME + ".MOBILIZE_FEEDS";
    private static final String ACTION_LOAD_LINK = FeedData.PACKAGE_NAME + ".LOAD_LINK";
    public static final String EXTRA_STAR = "STAR";
    public static final String EXTRA_LABEL_ID_LIST = "LABEL_ID_LIST";

    private static final int THREAD_NUMBER = 10;
    private static final int MAX_TASK_ATTEMPT = 3;

    private static final int FETCHMODE_DIRECT = 1;
    private static final int FETCHMODE_REENCODE = 2;
    public static final int FETCHMODE_EXERNAL_LINK = 3;

    private static final String CHARSET = "charset=";
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    private static final String HREF = "href=\"";

    private static final String HTML_BODY = "<body";
    private static final String ENCODING = "encoding=\"";
    public static final String CUSTOM_KEEP_TIME = "customKeepTime";
    public static final String IS_ONE_WEB_PAGE = "isOneWebPage";
    public static final String IS_RSS = "isRss";
    public static final String NEXT_PAGE_URL_CLASS_NAME = "UrlNextPageClassName";
    public static final String NEXT_PAGE_MAX_COUNT = "NextPageMaxCount";

    public static Boolean mCancelRefresh = false;
    private static final ArrayList<Long> mActiveEntryIDList = new ArrayList<>();
    private static Boolean mIsDownloadImageCursorNeedsRequery = false;

    //private static volatile Boolean mIsDeletingOld = false;

    public static final ArrayList<MarkItem> mMarkAsStarredFoundList = new ArrayList<>();

    /* Allow different positions of the "rel" attribute w.r.t. the "href" attribute */
    public static final Pattern FEED_LINK_PATTERN = Pattern.compile(
            "[.]*<link[^>]* ((rel=alternate|rel=\"alternate\")[^>]* href=\"[^\"]*\"|href=\"[^\"]*\"[^>]* (rel=alternate|rel=\"alternate\"))[^>]*>",
            Pattern.CASE_INSENSITIVE);
    public static int mMaxImageDownloadCount = PrefUtils.getImageDownloadCount();

    public static StatusText.FetcherObservable Status() {
        if ( mStatusText == null ) {
            mStatusText = new StatusText.FetcherObservable();
        }
        return mStatusText;
    }

    private static StatusText.FetcherObservable mStatusText = null;

    public FetcherService() {
        super(FetcherService.class.getSimpleName());
        HttpURLConnection.setFollowRedirects(true);
    }

    public static boolean hasMobilizationTask(long entryId) {
        Cursor cursor = getContext().getContentResolver().query(TaskColumns.CONTENT_URI, TaskColumns.PROJECTION_ID,
                TaskColumns.ENTRY_ID + '=' + entryId + DB_AND + TaskColumns.IMG_URL_TO_DL + Constants.DB_IS_NULL, null, null);

        boolean result = cursor.getCount() > 0;
        cursor.close();

        return result;
    }

    public static void addImagesToDownload(String entryId, ArrayList<String> images) {
        if (images != null && !images.isEmpty()) {
            ContentValues[] values = new ContentValues[images.size()];
            for (int i = 0; i < images.size(); i++) {
                values[i] = new ContentValues();
                values[i].put(TaskColumns.ENTRY_ID, entryId);
                values[i].put(TaskColumns.IMG_URL_TO_DL, images.get(i));
            }

            getContext().getContentResolver().bulkInsert(TaskColumns.CONTENT_URI, values);
        }
    }

    public static void addEntriesToMobilize( Uri uri ) {
        ContentResolver cr = MainApplication.getContext().getContentResolver();

        ArrayList<Long> entriesId = new ArrayList<>();
        Cursor c = cr.query(uri, new String[]{EntryColumns._ID, EntryColumns.LINK, MOBILIZED_HTML},
                MOBILIZED_HTML + DB_IS_NULL + DB_OR + MOBILIZED_HTML + " = '" + FileUtils.EMPTY_MOBILIZED_VALUE + "'", null, EntryColumns.DATE + Constants.DB_DESC);
        while (c.moveToNext()) {
            if (!FileUtils.INSTANCE.isMobilized( c.getString(1), c, 2, 0 ))
                entriesId.add(c.getLong(0));
        }
        c.close();

        ContentValues[] values = new ContentValues[entriesId.size()];
        for (int i = 0; i < entriesId.size(); i++) {
            values[i] = new ContentValues();
            values[i].put(TaskColumns.ENTRY_ID, entriesId.get(i));
        }

        getContext().getContentResolver().bulkInsert(TaskColumns.CONTENT_URI, values);
    }

    static boolean isBatteryLow() {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = getContext().registerReceiver(null, ifilter);
        int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale * 100;

        long lowLevelPct = 20;
        try {
            lowLevelPct = Math.max(50, Long.parseLong(PrefUtils.getString("refresh.min_update_battery_level", 20)) );
        } catch (Exception ignored) {
        }
        return batteryPct < lowLevelPct;
    }

    @Override
    public void onHandleIntent(final Intent intent) {
        if (intent == null) // No intent, we quit
            return;
        if ( MainApplication.getContext() == null )
            return;
        Status().ClearError();

        FileUtils.INSTANCE.reloadPrefs();

        if (intent.hasExtra(Constants.FROM_AUTO_BACKUP)) {
            LongOper(R.string.exportingToFile, new Runnable() {
                @Override
                public void run() {
                    try {
                        final String sourceFileName = OPML.GetAutoBackupOPMLFileName();
                        OPML.exportToFile( sourceFileName, true );
                        //final ArrayList<String> resultList = new ArrayList<>();
                        //resultList.add( sourceFileName );
                        String lastFile = "";
                        for (String destDir: FileUtils.INSTANCE.getStorageListWithPublic() ) {
                            File destFile = new File(destDir, AUTO_BACKUP_OPML_FILENAME);
                            if ( !destFile.getAbsolutePath().equals(sourceFileName) ) {
                                try {
                                    lastFile = destFile.getPath();
                                    FileUtils.INSTANCE.copy(new File(sourceFileName), destFile);
                                    //resultList.add( destFile.getAbsolutePath() );
                                } catch ( Exception e ) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        PrefUtils.putLong( AutoJobService.LAST_JOB_OCCURED + PrefUtils.AUTO_BACKUP_INTERVAL, System.currentTimeMillis() );
                        final String finalLastFile = lastFile;
                        UiUtils.RunOnGuiThread(() -> {
                            Toast.makeText(getContext(), getString(R.string.auto_backup_opml_file_created) + "\n" + finalLastFile, Toast.LENGTH_LONG ).show();
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        DebugApp.SendException( e, FetcherService.this );
                    }
                }
            });
            return;
        } else if (intent.hasExtra(Constants.FROM_IMPORT)) {
            LongOper(R.string.importingFromFile, new Runnable() {
                @Override
                public void run() {
                    try {
                        final boolean isRemoveExistingFeeds = intent.getBooleanExtra( EXTRA_REMOVE_EXISTING_FEEDS_BEFORE_IMPORT, false );
                        if ( intent.hasExtra( EXTRA_FILENAME ) )
                            OPML.importFromFile( intent.getStringExtra( EXTRA_FILENAME ), isRemoveExistingFeeds );
                        else if ( intent.hasExtra( EXTRA_URI ) )
                            OPML.importFromFile( Uri.parse( intent.getStringExtra( EXTRA_URI ) ), isRemoveExistingFeeds );
                    } catch (Exception e) {
                        DebugApp.SendException(e, FetcherService.this);
                    }
                }
            });
            return;
        } else if (intent.hasExtra( Constants.SET_VISIBLE_ITEMS_AS_OLD )) {
            startForeground(Constants.NOTIFICATION_ID_REFRESH_SERVICE, StatusText.GetNotification("", "", R.drawable.refresh, OPERATION_NOTIFICATION_CHANNEL_ID, null));
            EntriesListFragment.SetVisibleItemsAsOld(intent.getStringArrayListExtra(URL_LIST ) );
            stopForeground(true);
            return;
        } else if (intent.hasExtra( Constants.FROM_DELETE_OLD )) {
            LongOper(R.string.menu_delete_old, new Runnable() {
                @Override
                public void run() {
                    SetNotifyEnabled( false );
                    try {
                        long keepTime = (long) (GetDefaultKeepTime() * 86400000L);
                        long keepDateBorderTime = keepTime > 0 ? System.currentTimeMillis() - keepTime : 0;
                        deleteOldEntries(keepDateBorderTime);
                        deleteGhost();
                        if (PrefUtils.CALCULATE_IMAGES_SIZE())
                            CalculateImageSizes();
                        if (Build.VERSION.SDK_INT >= 21)
                            PrefUtils.putLong(AutoJobService.LAST_JOB_OCCURED + PrefUtils.DELETE_OLD_INTERVAL, System.currentTimeMillis());
                    } finally {
                        SetNotifyEnabled( true );
                        notifyChangeOnAllUris( URI_ENTRIES_FOR_FEED, null );
                    }
                }
            });
            return;
        } else if (intent.hasExtra( Constants.FROM_RELOAD_ALL_TEXT )) {
            LongOper(R.string.reloading_all_texts, new Runnable() {
                @Override
                public void run() {
                    SetNotifyEnabled(false);
                    try {
                        Cursor cursor = getContext().getContentResolver().query(intent.getData(), new String[]{_ID, LINK}, GetWhereSQL(), null, null);
                        ContentValues[] values = new ContentValues[cursor.getCount()];
                        while (cursor.moveToNext()) {
                            final long entryId = cursor.getLong(0);
                            final String link = cursor.getString(1);
                            FileUtils.INSTANCE.deleteMobilized(link, EntryColumns.CONTENT_URI(entryId));
                            values[cursor.getPosition()] = new ContentValues();
                            values[cursor.getPosition()].put(TaskColumns.ENTRY_ID, entryId);
                        }
                        cursor.close();
                        getContext().getContentResolver().bulkInsert(TaskColumns.CONTENT_URI, values);
                    } finally {
                        SetNotifyEnabled(true);
                        notifyChangeOnAllUris( URI_ENTRIES_FOR_FEED, null );
                    }

                    ExecutorService executor = CreateExecutorService(GetThreadCount());
                    try {
                        mobilizeAllEntries(executor);
                        downloadAllImages(executor);
                    } finally {
                        executor.shutdown();
                    }
                }
            });
            return;
        }

        mIsWiFi = GetIsWifi();

        final boolean isFromAutoRefresh = intent.getBooleanExtra(Constants.FROM_AUTO_REFRESH, false);

        if (ACTION_MOBILIZE_FEEDS.equals(intent.getAction())) {
            ExecutorService executor = CreateExecutorService( GetThreadCount() ); try {
                mobilizeAllEntries(executor);
                downloadAllImages(executor);
            } finally {
                executor.shutdown();
            }
        } else if (ACTION_LOAD_LINK.equals(intent.getAction())) {
            LongOper(R.string.loadingLink, new Runnable() {
                @Override
                public void run() {
                    ExecutorService executor = CreateExecutorService(GetLoadImageThreadCount()); try {
                        Pair<Uri,Boolean> result = LoadLink(GetExtrenalLinkFeedID(),
                                intent.getStringExtra(Constants.URL_TO_LOAD),
                                intent.getStringExtra(Constants.TITLE_TO_LOAD),
                                null,
                                FetcherService.ForceReload.No,
                                true,
                                true,
                                intent.getBooleanExtra(EXTRA_STAR, false),
                                AutoDownloadEntryImages.Yes,
                                false,
                                true);
                        if ( intent.hasExtra( EXTRA_LABEL_ID_LIST ) && intent.getStringArrayListExtra( EXTRA_LABEL_ID_LIST ) != null && result.first != null ) {
                            HashSet<Long> labels = new HashSet<>();
                            for ( String item: intent.getStringArrayListExtra( EXTRA_LABEL_ID_LIST ) )
                                labels.add( Long.parseLong( item ) );
                            LabelVoc.INSTANCE.setEntry(Long.parseLong(result.first.getLastPathSegment() ), labels );
                        }
                        downloadAllImages(executor);
                    } finally { executor.shutdown(); }
                }
            } );
        } else { // == Constants.ACTION_REFRESH_FEEDS
            LongOper(R.string.RefreshFeeds, new Runnable() {
                @Override
                public void run() {
                    long keepTime = (long) (GetDefaultKeepTime() * 86400000L);
                    long keepDateBorderTime = keepTime > 0 ? System.currentTimeMillis() - keepTime : 0;

                    String feedId = intent.getStringExtra(Constants.FEED_ID);
                    String groupId = intent.getStringExtra(Constants.GROUP_ID);

                    mMarkAsStarredFoundList.clear();
                    int newCount = 0;
                    ExecutorService executor = CreateExecutorService(GetThreadCount()); try {
                        if ( isFromAutoRefresh )
                            SetNotifyEnabled( false );
                        try {
                            newCount = (feedId == null ?
                                    refreshFeeds( executor, keepDateBorderTime, groupId, isFromAutoRefresh) :
                                    refreshFeed( executor, feedId, keepDateBorderTime));
                            EntryUrlVoc.INSTANCE.reinit( false );
                        } finally {
                            if (mMarkAsStarredFoundList.size() > 5) {
                                ArrayList<String> list = new ArrayList<>();
                                for (MarkItem item : mMarkAsStarredFoundList)
                                    list.add(item.mCaption);
                                ShowEventNotification(TextUtils.join(", ", list),
                                        R.string.markedAsStarred,
                                        new Intent(FetcherService.this, HomeActivity.class),
                                        Constants.NOTIFICATION_ID_MANY_ITEMS_MARKED_STARRED, null);
                            } else if (mMarkAsStarredFoundList.size() > 0)
                                for (MarkItem item : mMarkAsStarredFoundList) {
                                    Uri entryUri = GetEntryUri(item.mLink);

                                    int ID = -1;
                                    try {
                                        if (entryUri != null)
                                            ID = Integer.parseInt(entryUri.getLastPathSegment());
                                    } catch (Throwable ignored) {

                                    }

                                    ShowEventNotification(item.mCaption,
                                            R.string.markedAsStarred,
                                            GetActionIntent( Intent.ACTION_VIEW, entryUri),
                                            ID, createCancelStarPI( item.mLink, ID ));
                                }
                            if ( isFromAutoRefresh ) {
                                SetNotifyEnabled( true );
                                notifyChangeOnAllUris( URI_ENTRIES_FOR_FEED, mCurrentUri );
                            }
                        }

                        if (PrefUtils.getBoolean(PrefUtils.NOTIFICATIONS_ENABLED, true) && newCount > 0)
                            ShowEventNotification(getResources().getQuantityString(R.plurals.number_of_new_entries, newCount, newCount),
                                    R.string.flym_feeds,
                                    new Intent(FetcherService.this, HomeActivity.class),
                                    Constants.NOTIFICATION_ID_NEW_ITEMS_COUNT,
                                    null);
                        else if (Constants.NOTIF_MGR != null)
                            Constants.NOTIF_MGR.cancel(Constants.NOTIFICATION_ID_NEW_ITEMS_COUNT);

                        mobilizeAllEntries( executor );

                        downloadAllImages( executor );
                    } finally {
                        executor.shutdown();
                    }
                    if ( isFromAutoRefresh && Build.VERSION.SDK_INT >= 21 )
                        PrefUtils.putLong( AutoJobService.LAST_JOB_OCCURED + PrefUtils.REFRESH_INTERVAL, System.currentTimeMillis() );
                }
            } );
        }
    }

    private void deleteGhost() {
        final int status = Status().Start( R.string.deleting_ghost_entries, false );
        final Cursor cursor = getContentResolver().query(EntryColumns.CONTENT_URI, new String[] {LINK}, null, null, null );
        final HashSet<String> mapEntryLinkHash = new HashSet<>();
        while  ( cursor.moveToNext() )
            mapEntryLinkHash.add( FileUtils.INSTANCE.getLinkHash( cursor.getString( 0 ) ) );
        cursor.close();
        deleteGhostHtmlFiles( mapEntryLinkHash );
        deleteGhostImages( mapEntryLinkHash );
        EntryUrlVoc.INSTANCE.reinit( true );
        Status().End( status );
    }


    private void deleteGhostHtmlFiles( final HashSet<String> mapEntryLink ) {
        if ( isCancelRefresh() )
            return;
        int deletedCount = 0;
        final File folder = FileUtils.INSTANCE.GetHTMLFolder();
        String[] fileNames = folder.list();
        if (fileNames != null  )
            for (String fileName : fileNames) {
                if ( !mapEntryLink.contains( fileName ) ) {
                    if ( new File( folder, fileName ).delete() )
                        deletedCount++;
                    Status().ChangeProgress(getString(R.string.deleteFullTexts) + String.format( " %d", deletedCount ) );
                    if (FetcherService.isCancelRefresh())
                        break;

                }
            }
        Status().ChangeProgress( "" );
        //Status().End( status );
    }

    private void deleteGhostImages(  final HashSet<String> setEntryLinkHash ) {
        if ( isCancelRefresh() )
            return;
        HashSet<String> setEntryLinkHashFavorities = new HashSet<>();
        Cursor cursor = getContentResolver().query( EntryColumns.FAVORITES_CONTENT_URI, new String[] {LINK}, null, null, null );
        while ( cursor.moveToNext() )
            setEntryLinkHashFavorities.add( cursor.getString( 0 ) );
        cursor.close();
        int deletedCount = 0;
        final File folder = FileUtils.INSTANCE.GetImagesFolder();
        File[] files = FileUtils.INSTANCE.GetImagesFolder().listFiles();
        final int status = Status().Start( getString(R.string.image_count) + String.format(": %d", files.length), false ); try {
            final int FIRST_COUNT_TO_DELETE = files.length - 8000;
            if (FIRST_COUNT_TO_DELETE > 500)
                Arrays.sort(files, new Comparator<File>() {

                    @Override
                    public int compare(File f1, File f2) {
                        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                    }
                });
            if (isCancelRefresh())
                return;
            for (File file : files) {
                final String fileName = file.getName();
                final String[] list = TextUtils.split(fileName, "_");
                if (fileName.equals(".nomedia"))
                    continue;
                String linkHash = list[0];
                if ( deletedCount < FIRST_COUNT_TO_DELETE && !setEntryLinkHashFavorities.contains(linkHash) ||
                        list.length != 3 ||
                        list.length >= 2 && !setEntryLinkHash.contains(linkHash) ){
                    if (mImageFileVoc.removeFile(fileName))
                        deletedCount++;
                    Status().ChangeProgress(getString(R.string.deleteImages) + String.format(" %d", deletedCount));
                    if (FetcherService.isCancelRefresh())
                        break;
                }
            }
        } finally {
            Status().ChangeProgress( "" );
            Status().End( status );
        }
    }

    private void LongOper( int textID, Runnable oper ) {
        LongOper( getString( textID ), oper );
    }

    private void LongOper( String title, Runnable oper ) {
        startForeground(Constants.NOTIFICATION_ID_REFRESH_SERVICE, StatusText.GetNotification("", title, R.drawable.refresh, OPERATION_NOTIFICATION_CHANNEL_ID, createCancelPI()));
        Status().SetNotificationTitle( title, createCancelPI() );
        PrefUtils.putBoolean(PrefUtils.IS_REFRESHING, true);
        synchronized (mCancelRefresh) {
            mCancelRefresh = false;
        }
        try {
            oper.run();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText( this, getString( R.string.error ) + ": " + e.getMessage(), Toast.LENGTH_LONG ).show();
            DebugApp.SendException( e, this );
        } finally {
            Status().SetNotificationTitle( "", null );
            PrefUtils.putBoolean(PrefUtils.IS_REFRESHING, false);
            stopForeground(true);
            synchronized (mCancelRefresh) {
                mCancelRefresh = false;
            }
        }
    }

    public static float GetDefaultKeepTime() {
        return Float.parseFloat(PrefUtils.getString(PrefUtils.KEEP_TIME, "4"));
    }

    private static boolean mIsWiFi = false;
    private boolean GetIsWifi() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI );
    }
    public static boolean isNotCancelRefresh() {
        return !isCancelRefresh();
    }
    public static boolean isCancelRefresh() {
        synchronized (mCancelRefresh) {
            if ( !mIsWiFi && Status().mBytesRecievedLast > PrefUtils.getMaxSingleRefreshTraffic() * 1024 * 1024 )
                return true;
            //if (mCancelRefresh) {
            //    MainApplication.getContext().getContentResolver().delete( TaskColumns.CONTENT_URI, null, null );
            //}
            return mCancelRefresh;
        }
    }

    public static boolean isEntryIDActive(long id) {
        synchronized (mActiveEntryIDList) {
            return mActiveEntryIDList.contains( id );
        }
    }
    public static void setEntryIDActiveList(ArrayList<Long> list) {
        synchronized (mActiveEntryIDList) {
            mActiveEntryIDList.clear();
            mActiveEntryIDList.addAll( list );
        }
    }
    public static void addActiveEntryID( long value ) {
        synchronized (mActiveEntryIDList) {
            if ( !mActiveEntryIDList.contains( value ) )
                mActiveEntryIDList.add( value );
        }
    }
    public static void removeActiveEntryID( long value ) {
        synchronized (mActiveEntryIDList) {
            if ( mActiveEntryIDList.contains( value ) )
                mActiveEntryIDList.remove( value );
        }
    }
    public static void clearActiveEntryID() {
        synchronized (mActiveEntryIDList) {
            mActiveEntryIDList.clear();
        }
    }
    private static boolean isDownloadImageCursorNeedsRequery() {
        synchronized (mIsDownloadImageCursorNeedsRequery) {
            return mIsDownloadImageCursorNeedsRequery;
        }
    }
    public static void setDownloadImageCursorNeedsRequery( boolean value ) {
        synchronized (mIsDownloadImageCursorNeedsRequery) {
            mIsDownloadImageCursorNeedsRequery = value;
        }
    }

    public static void CancelStarNotification( long entryID ) {
        if ( Constants.NOTIF_MGR != null ) {
            Constants.NOTIF_MGR.cancel((int) entryID);
            Constants.NOTIF_MGR.cancel(Constants.NOTIFICATION_ID_MANY_ITEMS_MARKED_STARRED);
            Constants.NOTIF_MGR.cancel(Constants.NOTIFICATION_ID_NEW_ITEMS_COUNT);
        }
    }

    private void mobilizeAllEntries( ExecutorService executor) {
        final String statusText = getString(R.string.mobilizeAll);
        int status = Status().Start(statusText, false);
        SetNotifyEnabled( false ); try {
            final ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(TaskColumns.CONTENT_URI, new String[]{_ID, TaskColumns.ENTRY_ID, TaskColumns.NUMBER_ATTEMPT},
                    TaskColumns.IMG_URL_TO_DL + Constants.DB_IS_NULL, null, null);
            Status().ChangeProgress("");
            ArrayList<Future<FetcherService.DownloadResult>> futures = new ArrayList<>();
            while (cursor.moveToNext() && !isCancelRefresh()) {
                final long taskId = cursor.getLong(0);
                final long entryId = cursor.getLong(1);
                int attemptCount = 0;
                if (!cursor.isNull(2)) {
                    attemptCount = cursor.getInt(2);
                }

                final int finalAttemptCount = attemptCount;
                futures.add( executor.submit( new Callable<DownloadResult>() {
                    @Override
                    public DownloadResult call() {
                        DownloadResult result = new DownloadResult();
                        result.mAttemptNumber = finalAttemptCount;
                        result.mTaskID = taskId;
                        result.mResultCount = 0;
                        try {
                            Cursor curEntry = cr.query(EntryColumns.CONTENT_URI(entryId), new String[]{EntryColumns.FEED_ID}, null, null, null);
                            if ( curEntry != null ) {
                                if (curEntry.moveToFirst()) {
                                    final String feedID = curEntry.getString(0);
                                    FeedFilters filters = new FeedFilters(feedID);
                                    if (mobilizeEntry(entryId, filters, ArticleTextExtractor.MobilizeType.Yes, IsAutoDownloadImages(feedID), true, false, false, false)) {
                                        ContentResolver cr = getContext().getContentResolver();
                                        cr.delete(TaskColumns.CONTENT_URI(taskId), null, null);//operations.add(ContentProviderOperation.newDelete(TaskColumns.CONTENT_URI(taskId)).build());
                                        result.mResultCount = 1;
                                    }
                                }
                                curEntry.close();
                            }
                        } catch ( Exception e ) {
                            Status().SetError( "", "", "", e );
                        }
                        return result;
                    }
                }) );
            }

            FinishExecutionService( statusText, status, futures );

            cursor.close();
        } finally {
            SetNotifyEnabled( true );
            Status().End( status );
        }


    }

    public static AutoDownloadEntryImages IsAutoDownloadImages(String feedId) {
        final ContentResolver cr = getContext().getContentResolver();
        AutoDownloadEntryImages result = AutoDownloadEntryImages.Yes;
        Cursor curFeed = cr.query( FeedColumns.CONTENT_URI( feedId ), new String[] { FeedColumns.IS_IMAGE_AUTO_LOAD }, null, null, null );
        if ( curFeed.moveToFirst() )
            result = curFeed.isNull( 0 ) || curFeed.getInt( 0 ) == 1 ? AutoDownloadEntryImages.Yes : AutoDownloadEntryImages.No;
        curFeed.close();
        return result;
    }

    public enum AutoDownloadEntryImages {Yes, No}

    public static boolean mobilizeEntry(final long entryId,
                                        FeedFilters filters,
                                        final ArticleTextExtractor.MobilizeType mobilize,
                                        final AutoDownloadEntryImages autoDownloadEntryImages,
                                        final boolean isCorrectTitle,
                                        final boolean isShowError,
                                        final boolean isForceReload,
                                        boolean isParseDateFromHTML) {
        boolean success = false;
        ContentResolver cr = getContext().getContentResolver();
        Uri entryUri = EntryColumns.CONTENT_URI(entryId);
        Cursor entryCursor = cr.query(entryUri, null, null, null, null);

        if (entryCursor.moveToFirst()) {
            int linkPos = entryCursor.getColumnIndex(LINK);
            String link = entryCursor.getString(linkPos);
            String linkToLoad = HTMLParser.INSTANCE.replaceTomorrow(link).trim();
            {
                Pattern rx = Pattern.compile("\\{(.+)\\}");
                final Matcher matcher = rx.matcher(linkToLoad);

                if (matcher.find()) {
                    Calendar date = Calendar.getInstance();
                    linkToLoad = linkToLoad.replace(matcher.group(0), new SimpleDateFormat(matcher.group(1)).format(new Date(date.getTimeInMillis())));
                }
            }

            if ( isLinkToLoad(linkToLoad.toLowerCase()) && (isForceReload || !FileUtils.INSTANCE.isMobilized(link, entryCursor )) ) { // If we didn't already mobilized it
                int abstractHtmlPos = entryCursor.getColumnIndex(EntryColumns.ABSTRACT);
                final long feedId = entryCursor.getLong(entryCursor.getColumnIndex(EntryColumns.FEED_ID));
                Connection connection = null;

                try {

                    // Try to find a text indicator for better content extraction
                    String contentIndicator = null;
                    String text = entryCursor.getString(abstractHtmlPos);
                    if (!TextUtils.isEmpty(text)) {
                        text = Html.fromHtml(text).toString();
                        if (text.length() > 60) {
                            contentIndicator = text.substring(20, 40);
                        }
                    }

                    connection = new Connection( linkToLoad );

                    String mobilizedHtml;
                    Status().ChangeProgress(R.string.extractContent);

                    if (FetcherService.isCancelRefresh())
                        return false;
                    Document doc = Jsoup.parse(connection.getInputStream(), null, "");
                    {
                        for( Element el: doc.getElementsByTag("meta")) {
                            if (el.hasAttr("content") &&
                                    el.hasAttr("http-equiv") &&
                                    el.attr("http-equiv").equals("refresh")) {
                                String s = el.attr("content");
                                link = s.replaceFirst("\\d+;URL=", "");
                                connection.disconnect();
                                connection = new Connection(link);
                                doc = Jsoup.parse(connection.getInputStream(), null, "");
                                break;
                            }
                        }
                    }
                    final int titleCol = entryCursor.getColumnIndex(EntryColumns.TITLE);
                    String title = entryCursor.isNull(titleCol) ? null : entryCursor.getString(titleCol);
                    //if ( entryCursor.isNull( titlePos ) || title == null || title.isEmpty() || title.startsWith("http")  ) {
                    if ( isCorrectTitle ) {
                        Elements titleEls = doc.getElementsByTag("title");
                        if (!titleEls.isEmpty())
                            title = titleEls.first().text();
                    }

                    String dateText = "";
                    if (isParseDateFromHTML) {
                        Element element = ArticleTextExtractor.getDateElementFromPref(doc, link);
                        if (element != null) {
                            for (Element el : element.getAllElements())
                                if (el.hasText())
                                    dateText += el.ownText() + " ";
                            dateText = dateText.trim();
                        } else {
                            try {
                                dateText = doc.getElementsByTag("time").first().attr( "datetime" );
                            } catch ( Exception ignored) {

                            }
                        }
                    }
                    Dog.v( "date = " + dateText );

                    ArrayList<String> categoryList = new ArrayList<>();
                    mobilizedHtml = ArticleTextExtractor.extractContent(doc,
                            link,
                            contentIndicator,
                            filters,
                            mobilize,
                            categoryList,
                            !String.valueOf( feedId ).equals( GetExtrenalLinkFeedID() ),
                            entryCursor.getInt(entryCursor.getColumnIndex(EntryColumns.IS_WITH_TABLES) ) == 1);

                    Status().ChangeProgress("");

                    if (mobilizedHtml != null) {
                        ContentValues values = new ContentValues();
                        if ( !categoryList.isEmpty() )
                            values.put(EntryColumns.CATEGORIES, TextUtils.join(CATEGORY_LIST_SEP, categoryList ) );
                        else
                            values.putNull( EntryColumns.CATEGORIES );
                        if ( !dateText.isEmpty() ){
                            final String format = ArticleTextExtractor.getDataForUrlFromPref(link, PrefUtils.getString(PrefUtils.DATE_EXTRACT_RULES, "") );
                            Date date = null;
                            if ( !format.isEmpty()  ) {
                                try {
                                    date = new SimpleDateFormat(format, Locale.getDefault()).parse(dateText);
                                } catch ( ParseException e ) {
                                    Status().SetError( format, String.valueOf( feedId ), String.valueOf( entryId ), e );
                                }
                            }// else
                            //    date = parseDate( dateText, 0 );
                            if ( date != null )
                                values.put(EntryColumns.DATE, date.getTime());
                        }
                        FileUtils.INSTANCE.saveMobilizedHTML(link, mobilizedHtml, values);
                        final int favCol = entryCursor.getColumnIndex(IS_FAVORITE);
                        if ( entryCursor.isNull(titleCol) || entryCursor.isNull( favCol ) || entryCursor.getInt(favCol) == 0 )
                            values.put(EntryColumns.TITLE, title);

                        ArrayList<String> imgUrlsToDownload = new ArrayList<>();
                        if (autoDownloadEntryImages == AutoDownloadEntryImages.Yes && NetworkUtils.needDownloadPictures())
                            HtmlUtils.replaceImageURLs( mobilizedHtml, "", entryId, link, true, imgUrlsToDownload, null, mMaxImageDownloadCount );

                        String mainImgUrl;
                        if (!imgUrlsToDownload.isEmpty() )
                            mainImgUrl = HtmlUtils.getMainImageURL(imgUrlsToDownload);
                        else
                            mainImgUrl = HtmlUtils.getMainImageURL(mobilizedHtml);

                        if (mainImgUrl != null)
                            values.put(EntryColumns.IMAGE_URL, mainImgUrl);

                        cr.update( entryUri, values, null, null );

                        success = true;
                        if ( !imgUrlsToDownload.isEmpty() )
                            addImagesToDownload(String.valueOf(entryId), imgUrlsToDownload);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if ( isShowError ) {
                        String title = "";
                        Cursor cursor = cr.query( FeedColumns.CONTENT_URI( feedId ), new String[]{ FeedColumns.NAME }, null, null, null);
                        if ( cursor.moveToFirst() && cursor.isNull( 0 ) )
                            title = cursor.getString( 0 );
                        cursor.close();
                        Status().SetError(title + ": ", String.valueOf( feedId ), String.valueOf( entryId ), e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } else { // We already mobilized it
                success = true;
                //operations.add(ContentProviderOperation.newDelete(TaskColumns.CONTENT_URI(taskId)).build());
            }
        }
        entryCursor.close();
        return success;
    }

    public static boolean isLinkToLoad( String link ) {
        return  !link.endsWith( "mp3" ) &&
                !link.endsWith( "pdf" ) &&
                !link.endsWith( "avi" ) &&
                !link.endsWith( "mpeg" ) &&
                !link.endsWith( "doc" ) &&
                !link.endsWith( "docx" ) &&
                !link.endsWith( "jpeg" ) &&
                !link.endsWith( "png" );
    }


    public static Intent GetActionIntent( String action, Uri uri, Class<?> class1 ) {
        return new Intent(action, uri).setPackage( getContext().getPackageName() ).setClass( getContext(), class1 );
    }
    public static Intent GetActionIntent( String action, Uri uri ) {
        return GetActionIntent( action, uri, EntryActivity.class );
    }
    public static Intent GetIntent( String extra ) {
        return new Intent(getContext(), FetcherService.class).putExtra(extra, true );
    }
    public static void StartServiceLoadExternalLink(String url, String title, boolean star, ArrayList<String> labelIDs) {
        FetcherService.StartService( new Intent(getContext(), FetcherService.class )
                .setAction( ACTION_LOAD_LINK )
                .putExtra(Constants.URL_TO_LOAD, url)
                .putExtra(Constants.TITLE_TO_LOAD, title)
                .putExtra(EXTRA_LABEL_ID_LIST, labelIDs)
                .putExtra( EXTRA_STAR, star ), false );
    }
    public PendingIntent createCancelStarPI( String link, int notificationID ) {
        Intent intent = new Intent(this, BroadcastActionReciever.class);
        intent.setAction( Action );
        intent.putExtra("UnstarArticle", true );
        intent.putExtra( EXTRA_TEXT, link );
        intent.putExtra( EXTRA_ID, notificationID );
        return PendingIntent.getBroadcast(this, GetPendingIntentRequestCode(), intent, PendingIntent.FLAG_IMMUTABLE);
    }

    public enum ForceReload {Yes, No}
//    public static void OpenLink( Uri entryUri ) {
//        PrefUtils.putString(PrefUtils.LAST_ENTRY_URI, entryUri.toString());
//        Intent intent = new Intent(MainApplication.getContext(), HomeActivity.class);
//        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//        MainApplication.getContext().startActivity( intent );
//    }

    public static Uri GetEntryUri( final String url ) {
        Long id = EntryUrlVoc.INSTANCE.get( url );
        return id == null ? null : EntryColumns.CONTENT_URI(id);

//        Timer timer = new Timer( "GetEnryUri" );
//        Uri entryUri = null;
//        String url1 = url.replace("https:", "http:");
//        String url2 = url.replace("http:", "https:");
//        ContentResolver cr = MainApplication.getContext().getContentResolver();
//        Cursor cursor = cr.query(EntryColumns.CONTENT_URI,
//                new String[]{_ID, EntryColumns.FEED_ID},
//                LINK + "='" + url1 + "'" + DB_OR + LINK + "='" + url2 + "'",
//                null,
//                null);
//        try {
//            if (cursor.moveToFirst())
//                entryUri = Uri.withAppendedPath( EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI( cursor.getString(1) ), cursor.getString(0) );
//        } finally {
//            cursor.close();
//        }
//        timer.End();
//        return entryUri;
    }
    public static Pair<Uri,Boolean> LoadLink(final String feedID,
                                             final String url,
                                             final String title,
                                             FeedFilters filters,
                                             final ForceReload forceReload,
                                             final boolean isCorrectTitle,
                                             final boolean isShowError,
                                             final boolean isStarred,
                                             AutoDownloadEntryImages autoDownloadEntryImages,
                                             boolean isParseDateFromHTML,
                                             boolean isLoadingLinkStatus) {
        boolean load;
        Dog.v( "LoadLink " + url );

        final ContentResolver cr = getContext().getContentResolver();
        final int status = isLoadingLinkStatus ? FetcherService.Status().Start(getContext().getString(R.string.loadingLink), false) : -1;
        try {
            Uri entryUri = GetEntryUri( url );
            if ( entryUri != null ) {
                load = (forceReload == ForceReload.Yes);
                if (load) {
                    ContentValues values = new ContentValues();
                    values.put(EntryColumns.DATE, (new Date()).getTime());
                    cr.update(entryUri, values, null, null);//operations.add(ContentProviderOperation.newUpdate(entryUri).withValues(values).build());
                }
            } else {

                ContentValues values = new ContentValues();
                values.put(EntryColumns.TITLE, title);
                values.put(EntryColumns.SCROLL_POS, 0);
                //values.put(EntryColumns.ABSTRACT, NULL);
                //values.put(EntryColumns.IMAGE_URL, NULL);
                //values.put(EntryColumns.AUTHOR, NULL);
                //values.put(EntryColumns.ENCLOSURE, NULL);
                values.put(EntryColumns.DATE, (new Date()).getTime());
                values.put(LINK, url);
                values.put(EntryColumns.IS_WITH_TABLES, 0);
                values.put(EntryColumns.IMAGES_SIZE, 0);
                if ( isStarred )
                    values.put(EntryColumns.IS_FAVORITE, 1);

                //values.put(EntryColumns.MOBILIZED_HTML, enclosureString);
                //values.put(EntryColumns.ENCLOSURE, enclosureString);
                entryUri = cr.insert(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI(feedID), values);
                EntryUrlVoc.INSTANCE.set( url, entryUri );
                load = true;
            }

            if ( forceReload == ForceReload.Yes )
                FileUtils.INSTANCE.deleteMobilized( entryUri );

            if ( load && !FetcherService.isCancelRefresh() ) {
                final long entryId = Long.parseLong(entryUri.getLastPathSegment());
                mobilizeEntry( entryId, filters, ArticleTextExtractor.MobilizeType.Yes, autoDownloadEntryImages, isCorrectTitle, isShowError, false, isParseDateFromHTML);
            }
            return new Pair<>(entryUri, load);
        } finally {
            FetcherService.Status().End(status);
        }
        //stopForeground( true );
    }

    private static void downloadAllImages() {
        ExecutorService executor = CreateExecutorService(GetLoadImageThreadCount());
        try {
            downloadAllImages(executor);
        } finally {
            executor.shutdown();
        }
    }

    private static String mExtrenalLinkFeedID = "";
    public static String GetExtrenalLinkFeedID() {
        //Timer timer = new Timer( "GetExtrenalLinkFeedID()" );
        synchronized ( mExtrenalLinkFeedID ) {
            if (mExtrenalLinkFeedID.isEmpty()) {

                ContentResolver cr = getContext().getContentResolver();
                Cursor cursor = cr.query(FeedColumns.CONTENT_URI,
                        FeedColumns.PROJECTION_ID,
                        FeedColumns.FETCH_MODE + "=" + FetcherService.FETCHMODE_EXERNAL_LINK, null, null);
                if (cursor.moveToFirst())
                    mExtrenalLinkFeedID = cursor.getString(0);
                cursor.close();

                if (mExtrenalLinkFeedID.isEmpty()) {
                    ContentValues values = new ContentValues();
                    values.put(FeedColumns.FETCH_MODE, FetcherService.FETCHMODE_EXERNAL_LINK);
                    values.put(FeedColumns.NAME, getContext().getString(R.string.externalLinks));
                    mExtrenalLinkFeedID = cr.insert(FeedColumns.CONTENT_URI, values).getLastPathSegment();
                }
            }
        }
        //timer.End();
        return mExtrenalLinkFeedID;
    }

    public static class DownloadResult{
        public Long mTaskID;
        public Integer mAttemptNumber;
        public Integer mResultCount;

    }
    private static void downloadAllImages( ExecutorService executor ) {
        StatusText.FetcherObservable obs = Status();
        final String statusText = getContext().getString(R.string.AllImages);
        int status = obs.Start(statusText, false); try {

            ContentResolver cr = getContext().getContentResolver();
            Cursor cursor = cr.query(TaskColumns.CONTENT_URI, new String[]{_ID, TaskColumns.ENTRY_ID, TaskColumns.IMG_URL_TO_DL,
                    TaskColumns.NUMBER_ATTEMPT, LINK}, TaskColumns.IMG_URL_TO_DL + Constants.DB_IS_NOT_NULL, null, null);
            ArrayList<Future<DownloadResult>> futures = new ArrayList<>();
            while (cursor != null && cursor.moveToNext() && !isCancelRefresh() && !isDownloadImageCursorNeedsRequery()) {
                final long taskId = cursor.getLong(0);
                final long entryId = cursor.getLong(1);
                final String entryLink = cursor.getString(4);
                final String imgPath = cursor.getString(2);
                int attemptNum = 0;
                if (!cursor.isNull(3)) {
                    attemptNum = cursor.getInt(3);
                }
                final int finalNbAttempt = attemptNum;
                futures.add( executor.submit(new Callable<DownloadResult>() {
                    @Override
                    public DownloadResult call() {
                        DownloadResult result = new DownloadResult();
                        result.mAttemptNumber = finalNbAttempt;
                        result.mTaskID = taskId;
                        result.mResultCount = 0;
                        try {
                            if ( NetworkUtils.downloadImage(entryId, entryLink, imgPath, true, false) )
                                result.mResultCount = 1;
                        } catch ( Exception e ) {
                            Status().SetError( "", "", "", e );
                        }
                        return result;
                    }
                }) );
            }
            FinishExecutionService( statusText, status, futures);

            cursor.close();


        } finally { obs.End( status ); }

        if ( isDownloadImageCursorNeedsRequery() ) {
            setDownloadImageCursorNeedsRequery( false );
            downloadAllImages( executor );
        }
    }
    public static int FinishExecutionService( String statusText,
                                              int status,
                                              ArrayList<Future<DownloadResult>> futures) {
        final ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        int countOK = 0;
        Status().Change(status, statusText + String.format(" %d/%d", 0, futures.size()));
        for ( Future<DownloadResult> item: futures ) {
            try {
                if ( isCancelRefresh() ) {
                    item.cancel(false );
                    continue;
                }
                final DownloadResult result = item.get();
                Status().Change(status, statusText + String.format(" %d/%d", futures.indexOf( item ) + 1, futures.size()));
                if (result.mResultCount > 0 ) {
                    countOK += result.mResultCount;// If we are here, everything WAS OK
                    if ( operations != null && result.mTaskID != null )
                        operations.add(ContentProviderOperation.newDelete(TaskColumns.CONTENT_URI(result.mTaskID)).build());
                } else if ( operations != null ) {
                    if (result.mAttemptNumber + 1 > MAX_TASK_ATTEMPT) {
                        operations.add(ContentProviderOperation.newDelete(TaskColumns.CONTENT_URI(result.mTaskID)).build());
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(TaskColumns.NUMBER_ATTEMPT, result.mAttemptNumber + 1);
                        operations.add(ContentProviderOperation.newUpdate(TaskColumns.CONTENT_URI(result.mTaskID)).withValues(values).build());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();//DebugApp.AddErrorToLog( null ,e );
            }
        }
        if (!operations.isEmpty()) {
            new Thread() {
                @Override public void run() {
                    Status().ChangeProgress(R.string.applyOperations);
                    try {
                        MainApplication.getContext().getContentResolver().applyBatch(FeedData.AUTHORITY, operations);
                    } catch (Throwable ignored) {

                    }
                }
            }.start();

        }
        return countOK;
    }
    public static void downloadEntryImages( final String feedId, final long entryId, final String entryLink, final ArrayList<String> imageList ) {
        final StatusText.FetcherObservable obs = Status();
        final String statusText = getContext().getString(R.string.article_images_downloading);
        int status = obs.Start( statusText, true); try {
            int downloadedCount = 0;
            ExecutorService executor = CreateExecutorService(GetLoadImageThreadCount()); try {
                ArrayList<Future<DownloadResult>> futures = new ArrayList<>();
                for( final String imgPath: imageList ) {
                    futures.add( executor.submit( new Callable<DownloadResult>() {
                        @Override
                        public DownloadResult call() {
                            DownloadResult result = new DownloadResult();
                            result.mResultCount = 0;
                            if ( !isCancelRefresh() && isEntryIDActive( entryId ) ) {
                                try {
                                    if ( NetworkUtils.downloadImage(entryId, entryLink, imgPath, true, false) )
                                        result.mResultCount = 1;
                                } catch (Exception e) {
                                    obs.SetError(entryLink, feedId, String.valueOf(entryId), e);
                                }
                            }
                            return result;
                        }}));
                }
                downloadedCount = FinishExecutionService(statusText, status, futures );
                //Dog.v( "downloadedCount = " + downloadedCount );
            } finally {
                executor.shutdown();
            }
            if ( downloadedCount > 0 )
                EntryView.NotifyToUpdate( entryId, entryLink );
        } catch ( Exception e ) {
            obs.SetError(null, "", String.valueOf(entryId), e);
        } finally {
            obs.ResetBytes();
            obs.End(status);
        }
    }

    public static ExecutorService CreateExecutorService( int threadCount ) {
        return Executors.newFixedThreadPool( threadCount, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread( runnable );
                thread.setPriority( MIN_PRIORITY );
                return thread;
            }
        } );
    }

    private static int GetThreadCount() {
        int threadCount = PrefUtils.getIntFromText("thread_count", 5  );
        if ( threadCount < 1 )
            threadCount = 1;
        else if ( threadCount > 100 )
            threadCount = 100;
        return threadCount;
    }
    private static int GetLoadImageThreadCount() {
        int threadCount = PrefUtils.getIntFromText( MAX_IMAGE_DOWNLOAD_COUNT, GetThreadCount() );
        if ( threadCount < 1 )
            threadCount = GetThreadCount();
        else if ( threadCount > 100 )
            threadCount = 100;
        return threadCount;
    }

    private void deleteOldEntries(final long defaultKeepDateBorderTime) {
        if ( isCancelRefresh() )
            return;
        {
            //int status = Status().Start(MainApplication.getContext().getString(R.string.clearingWebViewChache), false);
            UiUtils.RunOnGuiThread(new Runnable() {
                @Override
                public void run() {
                    new WebView(getContext()).clearCache(true );
                }
            });
            //Status().End( status );
        }
        int status = Status().Start(getContext().getString(R.string.deleteOldEntries), false);
        ContentResolver cr = getContext().getContentResolver();
        final Cursor cursor = cr.query(FeedColumns.CONTENT_URI,
                new String[]{_ID, FeedColumns.OPTIONS},
                FeedColumns.LAST_UPDATE + Constants.DB_IS_NOT_NULL + DB_OR + FeedColumns._ID + "=" + GetExtrenalLinkFeedID(), null, null);
        try {
            //mIsDeletingOld = true;
            int index = 1;
            while (cursor.moveToNext() && !isCancelRefresh()) {
                index++;
                Status().ChangeProgress( String.format( "%d / %d", index, cursor.getCount() ) );
                long keepDateBorderTime = defaultKeepDateBorderTime;
                final String jsonText = cursor.isNull( 1 ) ? "" : cursor.getString(1);
                if ( !jsonText.isEmpty() )
                    try {
                        JSONObject jsonOptions = new JSONObject(jsonText);
                        if (jsonOptions.has(CUSTOM_KEEP_TIME))
                            keepDateBorderTime = jsonOptions.getDouble(CUSTOM_KEEP_TIME) == 0 ? 0 : System.currentTimeMillis() - (long) (jsonOptions.getDouble(CUSTOM_KEEP_TIME) * 86400000l);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                final long feedID = cursor.getLong(0);
                final boolean isDeleteRead = PrefUtils.getBoolean( "delete_read_articles", false );
                if ( keepDateBorderTime > 0 || isDeleteRead ) {
                    final String deleteRead = isDeleteRead ? DB_OR + WHERE_READ : "";
                    String where = "(" + EntryColumns.DATE + '<' + keepDateBorderTime + deleteRead + ")" + DB_AND + WHERE_NOT_FAVORITE;
                    // Delete the entries, the cache files will be deleted by the content provider
                    cr.delete(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI(feedID), where, null);
                }
            }
            EntryUrlVoc.INSTANCE.reinit( true );
        } finally {
            Status().ChangeProgress( "" );
            Status().End(status);
            cursor.close();
        }
    }

    private int refreshFeeds(final ExecutorService executor, final long keepDateBorderTime, String groupID, final boolean isFromAutoRefresh) {
        String statusText = "";
        int status = Status().Start( statusText, false ); try {
            final ExecutorService executorInner = CreateExecutorService(GetThreadCount()); try {
                ContentResolver cr = getContentResolver();
                final Cursor cursor;
                String where = PrefUtils.getBoolean(PrefUtils.REFRESH_ONLY_SELECTED, false) && isFromAutoRefresh ? FeedColumns.IS_AUTO_REFRESH + Constants.DB_IS_TRUE : null;
                if (groupID != null)
                    cursor = cr.query(FeedColumns.FEEDS_FOR_GROUPS_CONTENT_URI(groupID), FeedColumns.PROJECTION_ID, null, null, null);
                else
                    cursor = cr.query(FeedColumns.CONTENT_URI, FeedColumns.PROJECTION_ID, where, null, null);
                ArrayList<Future<DownloadResult>> futures = new ArrayList<>();
                while (cursor.moveToNext()) {
                    //Status().Start(String.format("%d from %d", cursor.getPosition(), cursor.getCount()));
                    final String feedId = cursor.getString(0);
                    futures.add(executor.submit(new Callable<DownloadResult>() {
                        @Override
                        public DownloadResult call() {
                            DownloadResult result = new DownloadResult();
                            result.mResultCount = 0;
                            try {
                                if (!isCancelRefresh())
                                    result.mResultCount = refreshFeed(executorInner, feedId, keepDateBorderTime);
                            } catch (Exception e) {
                                Status().SetError( "", "", "", e );
                            }
                            return result;
                        }
                    }));
                    //Status().End();
                }
                cursor.close();
                return FinishExecutionService( statusText, status,  futures );
            } finally {
                executorInner.shutdown();
            }
        } finally { Status().End( status ); }
    }

    private int refreshFeed( ExecutorService executor, String feedId, long keepDateBorderTime) {


        int newCount = 0;

        if ( GetExtrenalLinkFeedID().equals( feedId ) )
            return 0;

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(FeedColumns.CONTENT_URI(feedId), null, null, null, null);

        if (cursor.moveToFirst()) {
            int urlPosition = cursor.getColumnIndex(FeedColumns.URL);
            int idPosition = cursor.getColumnIndex(_ID);
            int titlePosition = cursor.getColumnIndex(FeedColumns.NAME);
            if ( cursor.isNull( cursor.getColumnIndex(FeedColumns.REAL_LAST_UPDATE) ) ) {
                keepDateBorderTime = 0;
            }
            boolean isRss = true;
            boolean isOneWebPage = false;
            try {

                JSONObject jsonOptions = new JSONObject();
                try {
                    jsonOptions = new JSONObject(cursor.getString(cursor.getColumnIndex(FeedColumns.OPTIONS)));
                } catch ( Exception e) {
                    e.printStackTrace();
                }
                isRss = !jsonOptions.has(IS_RSS) || jsonOptions.getBoolean(IS_RSS);
                isOneWebPage = jsonOptions.has(IS_ONE_WEB_PAGE) && jsonOptions.getBoolean(IS_ONE_WEB_PAGE);

                final String feedID = cursor.getString(idPosition);
                final String feedUrl = cursor.getString(urlPosition);
                final boolean isLoadImages  = NetworkUtils.needDownloadPictures() && ( IsAutoDownloadImages( feedID ) == AutoDownloadEntryImages.Yes );
                final int status = Status().Start(cursor.getString(titlePosition), false); try {
                    if ( isRss )
                        newCount = ParseRSSAndAddEntries(feedUrl, cursor, keepDateBorderTime, feedID);
                    else if ( isOneWebPage )
                        newCount = OneWebPageParser.INSTANCE.parse(keepDateBorderTime, feedID, feedUrl, jsonOptions, isLoadImages, 0 );
                    else {
                        newCount = HTMLParser.Parse(executor, feedID, feedUrl, jsonOptions, 0);
                        FetcherService.addEntriesToMobilize(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI(feedId));
                    }
                } finally {
                    Status().End(status);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        cursor.close();
        return newCount;
    }


    private void ShowEventNotification(String text, int captionID, Intent intent, int ID, PendingIntent cancelPI){
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext()) //
                .setContentIntent(contentIntent) //
                .setSmallIcon(R.mipmap.ic_launcher) //
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) //
                //.setTicker(text) //
                .setWhen(System.currentTimeMillis()) //
                .setAutoCancel(true) //
                .setContentTitle(getString(captionID)) //
                .setLights(0xffffffff, 0, 0);
        if (Build.VERSION.SDK_INT >= 26 ) {
            builder.setChannelId(OPERATION_NOTIFICATION_CHANNEL_ID);
            if ( cancelPI != null )
                builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, getContext().getString(android.R.string.cancel), cancelPI);
        }
        if (PrefUtils.getBoolean(PrefUtils.NOTIFICATIONS_VIBRATE, false))
            builder.setVibrate(new long[]{0, 1000});

        String ringtone = PrefUtils.getString(PrefUtils.NOTIFICATIONS_RINGTONE, null);
        if (ringtone != null && ringtone.length() > 0)
            builder.setSound(Uri.parse(ringtone));

        if (PrefUtils.getBoolean(PrefUtils.NOTIFICATIONS_LIGHT, false))
            builder.setLights(0xffffffff, 300, 1000);

        Notification nf;
        if (Build.VERSION.SDK_INT < 16)
            nf = builder.setContentText(text).build();
        else
            nf = new NotificationCompat.BigTextStyle(builder.setContentText(text)).bigText(text).build();

        if (Constants.NOTIF_MGR != null)
            Constants.NOTIF_MGR.notify(ID, nf);
    }

    public static String ToString (InputStream inputStream, Xml.Encoding encoding ) throws
            IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //InputStream inputStream = connection.getInputStream();

        byte[] byteBuffer = new byte[4096];

        int n;
        while ((n = inputStream.read(byteBuffer)) > 0) {
            Status().AddBytes(n);
            outputStream.write(byteBuffer, 0, n);
        }
        String content = outputStream.toString(encoding.name());
        content = CleanRSS(content);

        return content;
    }

    private static String ToString (Reader reader ) throws
            IOException {

        Scanner scanner = new Scanner(reader).useDelimiter("\\A");
        String content = scanner.hasNext() ? scanner.next() : "";
        Status().AddBytes(content.length());
        content = CleanRSS(content);
        return content;
    }

    @NotNull
    private static String CleanRSS(String content) {
        content = content.replace(" & ", " &amp; ");
        content = content.replaceAll( "<[a-z]+?:", "<" );
        content = content.replaceAll( "</[a-z]+?:", "</" );
        content = content.replace( "&mdash;", "-" );
        content = content.replace( "&ndash;", "-" );
        content = content.replace((char) 0x1F, ' ');
        content = content.replace((char) 0x02, ' ');
        content = content.replace(String.valueOf((char)0x00), "");
        return content;
    }


    private int ParseRSSAndAddEntries(String feedUrl, Cursor cursor, long keepDateBorderTime, String feedId) {
        RssAtomParser handler = null;

        int fetchModePosition = cursor.getColumnIndex(FeedColumns.FETCH_MODE);
        int realLastUpdatePosition = cursor.getColumnIndex(FeedColumns.REAL_LAST_UPDATE);
        int retrieveFullscreenPosition = cursor.getColumnIndex(FeedColumns.RETRIEVE_FULLTEXT);
        int autoImageDownloadPosition = cursor.getColumnIndex(FeedColumns.IS_IMAGE_AUTO_LOAD);
        int titlePosition = cursor.getColumnIndex(FeedColumns.NAME);
        int urlPosition = cursor.getColumnIndex(FeedColumns.URL);
        int iconUrlPosition = cursor.getColumnIndex(FeedColumns.ICON_URL);

        Connection connection = null;
        ContentResolver cr = getContext().getContentResolver();
        try {

            connection = new Connection( feedUrl);
            String contentType = connection.getContentType();
            int fetchMode = cursor.getInt(fetchModePosition);

            boolean autoDownloadImages = cursor.isNull(autoImageDownloadPosition) || cursor.getInt(autoImageDownloadPosition) == 1;

            if (fetchMode == 0) {
                if (contentType != null) {
                    int index = contentType.indexOf(CHARSET);

                    if (index > -1) {
                        int index2 = contentType.indexOf(';', index);

                        try {
                            Xml.findEncodingByName(index2 > -1 ? contentType.substring(index + 8, index2) : contentType.substring(index + 8));
                            fetchMode = FETCHMODE_DIRECT;
                        } catch (UnsupportedEncodingException ignored) {
                            fetchMode = FETCHMODE_REENCODE;
                        }
                    } else {
                        fetchMode = FETCHMODE_REENCODE;
                    }

                } else {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    char[] chars = new char[20];

                    int length = bufferedReader.read(chars);

                    FetcherService.Status().AddBytes(length);

                    String xmlDescription = new String(chars, 0, length);

                    connection.disconnect();
                    connection = new Connection(feedUrl);

                    int start = xmlDescription.indexOf(ENCODING);

                    if (start > -1) {
                        try {
                            Xml.findEncodingByName(xmlDescription.substring(start + 10, xmlDescription.indexOf('"', start + 11)));
                            fetchMode = FETCHMODE_DIRECT;
                        } catch (UnsupportedEncodingException ignored) {
                            fetchMode = FETCHMODE_REENCODE;
                        }
                    } else {
                        // absolutely no encoding information found
                        fetchMode = FETCHMODE_DIRECT;
                    }
                }

                ContentValues values = new ContentValues();
                values.put(FeedColumns.FETCH_MODE, fetchMode);
                cr.update(FeedColumns.CONTENT_URI(feedId), values, null, null);
            }

            handler = new RssAtomParser(new Date(cursor.getLong(realLastUpdatePosition)),
                    keepDateBorderTime,
                    feedId,
                    cursor.getString(titlePosition),
                    feedUrl,
                    cursor.getInt(retrieveFullscreenPosition) == 1);
            handler.setFetchImages(NetworkUtils.needDownloadPictures() && autoDownloadImages);

            InputStream inputStream = connection.getInputStream();

            switch (fetchMode) {
                default:
                case FETCHMODE_DIRECT: {
                    if (contentType != null) {
                        int index = contentType.indexOf(CHARSET);

                        int index2 = contentType.indexOf(';', index);

                        parseXml(//cursor.getString(urlPosition),
                                inputStream,
                                Xml.findEncodingByName(index2 > -1 ? contentType.substring(index + 8, index2) : contentType.substring(index + 8)),
                                handler);

                    } else {
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                        parseXml(reader, handler);

                    }
                    break;
                }
                case FETCHMODE_REENCODE: {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    byte[] byteBuffer = new byte[4096];

                    int n;
                    while ((n = inputStream.read(byteBuffer)) > 0) {
                        FetcherService.Status().AddBytes(n);
                        outputStream.write(byteBuffer, 0, n);
                    }

                    String xmlText = outputStream.toString().trim();

                    int start = xmlText != null ? xmlText.indexOf(ENCODING) : -1;

                    if (start > -1) {
                        parseXml( new StringReader(new String(outputStream.toByteArray(),
                                        xmlText.substring(start + 10,
                                                xmlText.indexOf('"', start + 11))).trim()),
                                handler );
                    } else {
                        // use content type
                        if (contentType != null) {
                            int index = contentType.indexOf(CHARSET);

                            if (index > -1) {
                                int index2 = contentType.indexOf(';', index);

                                try {
                                    StringReader reader = new StringReader(new String(outputStream.toByteArray(), index2 > -1 ? contentType.substring(
                                            index + 8, index2) : contentType.substring(index + 8)));
                                    parseXml(reader, handler);
                                } catch (Exception ignored) {
                                }
                            } else {
                                StringReader reader = new StringReader(new String(outputStream.toByteArray()));
                                parseXml(reader, handler);
                            }
                        }
                    }
                    break;
                }
            }


            connection.disconnect();
        } catch(FileNotFoundException e){
            if (handler == null || (!handler.isDone() && !handler.isCancelled())) {
                ContentValues values = new ContentValues();

                // resets the fetch mode to determine it again later
                values.put(FeedColumns.FETCH_MODE, 0);

                values.put(FeedColumns.ERROR, getString(R.string.error_feed_error));
                cr.update(FeedColumns.CONTENT_URI(feedId), values, null, null);
                FetcherService.Status().SetError( cursor.getString(titlePosition) + ": " + getString(R.string.error_feed_error), feedId, "", e);
            }
        } catch(Exception e){
            if (handler == null || (!handler.isDone() && !handler.isCancelled())) {
                ContentValues values = new ContentValues();

                // resets the fetch mode to determine it again later
                values.put(FeedColumns.FETCH_MODE, 0);

                values.put(FeedColumns.ERROR, e.getMessage() != null ? e.getMessage() : getString(R.string.error_feed_process));
                cr.update(FeedColumns.CONTENT_URI(feedId), values, null, null);

                FetcherService.Status().SetError(cursor.getString(titlePosition) + ": " + e.toString(),
                        feedId, "", e);
            }
        } finally{
            /* check and optionally find favicon */
            try {
                if (handler != null ) {
                    if (handler.getFeedLink() != null)
                        NetworkUtils.retrieveFavicon(this, new URL(handler.getFeedLink()), feedId);
                    else
                        NetworkUtils.retrieveFavicon(this, new URL( feedUrl ), feedId);
                }
            } catch (Throwable ignored) {
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return handler != null ? handler.getNewCount() : 0;
    }

    private static void parseXml ( InputStream in, Xml.Encoding
            encoding,
                                   ContentHandler contentHandler) throws IOException, SAXException {
        Status().ChangeProgress(R.string.parseXml);
        Xml.parse(ToString(in, encoding).trim(), contentHandler);
        Status().ChangeProgress("");
        Status().AddBytes(contentHandler.toString().length());
    }

    private static void parseXml (Reader reader,
                                  ContentHandler contentHandler) throws IOException, SAXException {
        Status().ChangeProgress(R.string.parseXml);
        Xml.parse(ToString( reader ), contentHandler);
        Status().ChangeProgress("");
        Status().AddBytes(contentHandler.toString().length());
    }

    public static void cancelRefresh () {
        synchronized (mCancelRefresh) {
            getContext().getContentResolver().delete( TaskColumns.CONTENT_URI, null, null );
            mCancelRefresh = true;
        }
    }

    public static void deleteAllFeedEntries( Uri entriesUri, String condition ){
        int status = Status().Start("deleteAllFeedEntries", true);
        try {
            final ContentResolver cr = getContext().getContentResolver();
            final Cursor cursor = cr.query( entriesUri, new String[] {EntryColumns._ID, EntryColumns.LINK}, condition, null, null );
            if ( cursor != null  ){
                while ( cursor.moveToNext() ) {
                    Status().ChangeProgress(String.format("%d/%d", cursor.getPosition(), cursor.getCount()));
                    FileUtils.INSTANCE.deleteMobilizedFile(cursor.getString(1));
                    EntryUrlVoc.INSTANCE.remove(  cursor.getString(1) );
                }
                cursor.close();
            }
            Status().ChangeProgress( "" );
            cr.delete(entriesUri, condition, null);
            EntryUrlVoc.INSTANCE.reinit( true );
        } finally {
            Status().End(status);
        }

    }

    public static void unstarAllFeedEntries( Uri entriesUri ){
        int status = Status().Start("unstarAllFeedEntries", true);
        try {

            final ContentResolver cr = getContext().getContentResolver();
            final Cursor cursor = cr.query( entriesUri, new String[] {EntryColumns._ID}, WHERE_FAVORITE, null, null );
            if ( cursor != null  ){
                SetNotifyEnabled( false ); try {
                    while (cursor.moveToNext()) {

                        Status().ChangeProgress(String.format("%d/%d", cursor.getPosition(), cursor.getCount()));
                        ContentValues values = new ContentValues();
                        values.putNull(EntryColumns.IS_FAVORITE);
                        final long entryID = cursor.getLong(0);
                        cr.update(EntryColumns.CONTENT_URI(entryID), values, null, null);
                        LabelVoc.INSTANCE.removeLabels(entryID);
                    }
                } finally {
                    SetNotifyEnabled( true );
                    notifyChangeOnAllUris( URI_ENTRIES_FOR_FEED, entriesUri );
                }
                cursor.close();
            }
            Status().ChangeProgress( "" );
        } finally {
            Status().End(status);
        }

    }



//        public static void createTestData () {
//            int status = Status().Start("createTestData", true);
//            try {
//                {
//                    final String testFeedID = "10000";
//                    final String testAbstract1 = "safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd ";
//                    String testAbstract = "";
//                    for (int i = 0; i < 10; i++)
//                        testAbstract += testAbstract1;
//                    //final String testAbstract2 = "sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff";
//
//                    deleteAllFeedEntries(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI( testFeedID) );
//
//                    ContentResolver cr = MainApplication.getContext().getContentResolver();
//                    ContentValues values = new ContentValues();
//                    values.put(_ID, testFeedID);
//                    values.put(FeedColumns.NAME, "testFeed");
//                    values.putNull(FeedColumns.IS_GROUP);
//                    //values.putNull(FeedColumns.GROUP_ID);
//                    values.putNull(FeedColumns.LAST_UPDATE);
//                    values.put(FeedColumns.FETCH_MODE, 0);
//                    cr.insert(FeedColumns.CONTENT_URI, values);
//
//                    for (int i = 0; i < 30; i++) {
//                        values.clear();
//                        values.put(_ID, i);
//                        values.put(EntryColumns.ABSTRACT, testAbstract);
//                        values.put(EntryColumns.TITLE, "testTitle" + i);
//                        cr.insert(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI(testFeedID), values);
//                    }
//                }
//
//                {
//                    // small
//                    final String testFeedID = "10001";
//                    final String testAbstract1 = "safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd safdkhfgsadjkhgfsakdhgfasdhkgf sadfdasfdsafasdfasd ";
//                    String testAbstract = "";
//                    for (int i = 0; i < 1; i++)
//                        testAbstract += testAbstract1;
//                    //final String testAbstract2 = "sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs sfdsdafsdafs fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff fffffffffffffff";
//
//                    deleteAllFeedEntries(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI( testFeedID) );
//
//                    ContentResolver cr = MainApplication.getContext().getContentResolver();
//                    ContentValues values = new ContentValues();
//                    values.put(_ID, testFeedID);
//                    values.put(FeedColumns.NAME, "testFeedSmall");
//                    values.putNull(FeedColumns.IS_GROUP);
//                    //values.putNull(FeedColumns.GROUP_ID);
//                    values.putNull(FeedColumns.LAST_UPDATE);
//                    values.put(FeedColumns.FETCH_MODE, 0);
//                    cr.insert(FeedColumns.CONTENT_URI, values);
//
//                    for (int i = 0; i < 30; i++) {
//                        values.clear();
//                        values.put(_ID, 100 + i);
//                        values.put(EntryColumns.ABSTRACT, testAbstract);
//                        values.put(EntryColumns.TITLE, "testTitleSmall" + i);
//                        cr.insert(EntryColumns.ENTRIES_FOR_FEED_CONTENT_URI(testFeedID), values);
//                    }
//                }
//            } finally {
//                Status().End(status);
//            }
//
//        }

    public static void StartService(Intent intent, boolean requiresNetwork) {
        final Context context = getContext();

        final boolean isFromAutoRefresh = intent.getBooleanExtra(Constants.FROM_AUTO_REFRESH, false);
        //boolean isOpenActivity = intent.getBooleanExtra(Constants.OPEN_ACTIVITY, false);

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // Connectivity issue, we quit
        if (requiresNetwork && (networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) ) {
            if (ACTION_REFRESH_FEEDS.equals(intent.getAction()) && !isFromAutoRefresh) {
                // Display a toast in that case
                UiUtils.RunOnGuiThread( new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return;
        }

        boolean skipFetch = requiresNetwork && isFromAutoRefresh && PrefUtils.getBoolean(PrefUtils.REFRESH_WIFI_ONLY, false)
                && networkInfo.getType() != ConnectivityManager.TYPE_WIFI;
        // We need to skip the fetching process, so we quit
        if (skipFetch)
            return;

        if (isFromAutoRefresh && Build.VERSION.SDK_INT < 26 && isBatteryLow())
            return;

        final boolean foreground = !ACTION_MOBILIZE_FEEDS.equals(intent.getAction());
        if (Build.VERSION.SDK_INT >= 26 && foreground)
            context.startForegroundService(intent);
        else
            context.startService( intent );
    }

    static Intent GetStartIntent() {
        return new Intent(getContext(), FetcherService.class)
                .setAction( FetcherService.ACTION_REFRESH_FEEDS );
    }

    void CalculateImageSizes() {
        final int status = Status().Start(R.string.setting_calculate_image_sizes, false ); try {
            {
                ContentValues values = new ContentValues();
                values.put( IMAGES_SIZE, 0 );
                getContentResolver().update( FeedColumns.CONTENT_URI, values, null, null );
            }

            final HashMap<Long, Long> mapEntryIDToSize = new HashMap<>();
            final HashMap<Long, Long> mapFeedIDToSize = new HashMap<>();

            final HashMap<String, Long> mapEntryLinkHashToID = new HashMap<>();
            final HashMap<String, Long> mapEntryLinkHashToFeedID = new HashMap<>();
            Cursor cursor = getContentResolver().query(EntryColumns.CONTENT_URI, new String[]{_ID, LINK, FEED_ID}, null, null, null);
            while (cursor.moveToNext()) {
                final String linkHash = FileUtils.INSTANCE.getLinkHash(cursor.getString(1));
                mapEntryLinkHashToID.put(linkHash, cursor.getLong(0));
                mapEntryLinkHashToFeedID.put(linkHash, cursor.getLong(2));
            }
            cursor.close();

            final HashMap<Long, Long> mapFeedIDToGroupID = new HashMap<>();
            cursor = getContentResolver().query(FeedColumns.CONTENT_URI, new String[]{_ID, GROUP_ID}, GROUP_ID + DB_IS_NOT_NULL, null, null);
            while (cursor.moveToNext())
                if (!cursor.isNull(1))
                    mapFeedIDToGroupID.put(cursor.getLong(0), cursor.getLong(1));
            cursor.close();

            File[] files = FileUtils.INSTANCE.GetImagesFolder().listFiles();
            if (isCancelRefresh())
                return;
            int index = 0;
            for (File file : files) {
                index++;
                if ( index % 71 == 0 ) {
                    Status().ChangeProgress(String.format("%d/%d", index, files.length));
                    if (FetcherService.isCancelRefresh())
                        break;
                }
                final String fileName = file.getName();
                final String[] list = TextUtils.split(fileName, "_");
                if (fileName.equals(".nomedia"))
                    continue;
                if (list.length >= 2) {
                    final String entryLinkHash = list[0];
                    if (!mapEntryLinkHashToID.containsKey(entryLinkHash))
                        continue;
                    final long entryID = mapEntryLinkHashToID.get(entryLinkHash);
                    final long feedID = mapEntryLinkHashToFeedID.get(entryLinkHash);
                    final long groupID = mapFeedIDToGroupID.containsKey(feedID) ? mapFeedIDToGroupID.get(feedID) : -1L;
                    final long size = file.length();

                    if (!mapEntryIDToSize.containsKey(entryID))
                        mapEntryIDToSize.put(entryID, size);
                    else
                        mapEntryIDToSize.put(entryID, mapEntryIDToSize.get(entryID) + size);

                    if (!mapFeedIDToSize.containsKey(feedID))
                        mapFeedIDToSize.put(feedID, size);
                    else
                        mapFeedIDToSize.put(feedID, mapFeedIDToSize.get(feedID) + size);

                    if (groupID != -1) {
                        if (!mapFeedIDToSize.containsKey(groupID))
                            mapFeedIDToSize.put(groupID, size);
                        else
                            mapFeedIDToSize.put(groupID, mapFeedIDToSize.get(groupID) + size);
                    }
                }
            }

            Status().ChangeProgress(R.string.applyOperations);
            if (FetcherService.isCancelRefresh())
                return;
            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            for (Map.Entry<Long, Long> item : mapEntryIDToSize.entrySet())
                operations.add(ContentProviderOperation.newUpdate(EntryColumns.CONTENT_URI(item.getKey()))
                        .withValue(EntryColumns.IMAGES_SIZE, item.getValue()).build());
            if (FetcherService.isCancelRefresh())
                return;
            for (Map.Entry<Long, Long> item : mapFeedIDToSize.entrySet())
                operations.add(ContentProviderOperation.newUpdate(FeedColumns.CONTENT_URI(item.getKey()))
                        .withValue(FeedColumns.IMAGES_SIZE, item.getValue()).build());
            if (FetcherService.isCancelRefresh())
                return;

            if (!operations.isEmpty())
                try {
                    //SetNotifyEnabled( false );
                    getContentResolver().applyBatch(FeedData.AUTHORITY, operations);
                    //SetNotifyEnabled( true );
                    //getContentResolver().notifyChange(FeedColumns.GROUPED_FEEDS_CONTENT_URI, null);
                } catch (Exception e) {
                    DebugApp.AddErrorToLog(null, e);
                }
        } finally {
            Status().ChangeProgress( "" );
            Status().End( status );
        }

    }
    private PendingIntent createCancelPI() {
        Intent intent = new Intent(this, BroadcastActionReciever.class);
        intent.setAction( Action );
        intent.putExtra("FetchingServiceStart", true );
        return PendingIntent.getBroadcast(this, GetPendingIntentRequestCode(), intent, PendingIntent.FLAG_IMMUTABLE);
    }
}