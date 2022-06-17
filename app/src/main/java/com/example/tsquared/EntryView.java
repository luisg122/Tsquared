package com.example.tsquared;
  
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Observable;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.yanus171.feedexfork.Constants;
import ru.yanus171.feedexfork.MainApplication;
import ru.yanus171.feedexfork.R;
import ru.yanus171.feedexfork.activity.EntryActivity;
import ru.yanus171.feedexfork.activity.LoadLinkLaterActivity;
import ru.yanus171.feedexfork.parser.FeedFilters;
import ru.yanus171.feedexfork.provider.FeedData;
import ru.yanus171.feedexfork.provider.FeedDataContentProvider;
import com.example.tsquared.FetcherService;
import com.example.tsquared.ArticleTextExtractor;
import ru.yanus171.feedexfork.utils.Dog;
import ru.yanus171.feedexfork.utils.FileUtils;
import ru.yanus171.feedexfork.utils.HtmlUtils;
import ru.yanus171.feedexfork.utils.LabelVoc;
import ru.yanus171.feedexfork.utils.NetworkUtils;
import com.example.tsquared.PrefUtils;
import ru.yanus171.feedexfork.utils.Theme;
import ru.yanus171.feedexfork.utils.Timer;
import ru.yanus171.feedexfork.utils.UiUtils;

import static androidx.core.content.FileProvider.getUriForFile;
import static ru.yanus171.feedexfork.activity.BaseActivity.PAGE_SCROLL_DURATION_MSEC;
import static ru.yanus171.feedexfork.adapter.EntriesCursorAdapter.CategoriesToOutput;
import static ru.yanus171.feedexfork.provider.FeedData.FilterColumns.DB_APPLIED_TO_TITLE;
import static ru.yanus171.feedexfork.provider.FeedDataContentProvider.SetNotifyEnabled;
import static com.example.tsquared.FetcherService.EXTRA_LABEL_ID_LIST;
import static com.example.tsquared.FetcherService.GetExtrenalLinkFeedID;
import static com.example.tsquared.FetcherService.IS_RSS;
import static com.example.tsquared.FetcherService.isLinkToLoad;
import static com.example.tsquared.FetcherService.mMaxImageDownloadCount;
import static com.example.tsquared.ArticleTextExtractor.AddTagButtons;
import static com.example.tsquared.ArticleTextExtractor.FindBestElement;
import static com.example.tsquared.ArticleTextExtractor.TAG_BUTTON_CLASS;
import static com.example.tsquared.ArticleTextExtractor.TAG_BUTTON_CLASS_DATE;
import static com.example.tsquared.ArticleTextExtractor.TAG_BUTTON_CLASS_HIDDEN;
import static com.example.tsquared.ArticleTextExtractor.TAG_BUTTON_CLASS_CATEGORY;
import static com.example.tsquared.ArticleTextExtractor.TAG_BUTTON_FULL_TEXT_ROOT_CLASS;
import static com.example.tsquared.PrefUtils.ARTICLE_TEXT_BUTTON_LAYOUT_HORIZONTAL;
import static com.example.tsquared.PrefUtils.BASE_TEXT_FONT_SIZE;
import static ru.yanus171.feedexfork.utils.Theme.LINK_COLOR;
import static ru.yanus171.feedexfork.utils.Theme.LINK_COLOR_BACKGROUND;
import static ru.yanus171.feedexfork.utils.Theme.QUOTE_BACKGROUND_COLOR;
import static ru.yanus171.feedexfork.utils.Theme.QUOTE_LEFT_COLOR;
import static ru.yanus171.feedexfork.utils.Theme.SUBTITLE_BORDER_COLOR;
import static ru.yanus171.feedexfork.utils.Theme.SUBTITLE_COLOR;
import static ru.yanus171.feedexfork.utils.UiUtils.SetupSmallTextView;
import static ru.yanus171.feedexfork.utils.UiUtils.SetupTextView;
import static ru.yanus171.feedexfork.widget.FontSelectPreference.DefaultFontFamily;
import static ru.yanus171.feedexfork.widget.FontSelectPreference.GetTypeFaceLocalUrl;

public class EntryView extends WebView implements Handler.Callback {

    private static final String TEXT_HTML = "text/html";
    private static final String HTML_IMG_REGEX = "(?i)<[/]?[ ]?img(.|\n)*?>";
    public static final String TAG = "EntryView";
    private static final String NO_MENU = "NO_MENU_";
    public static final String BASE_URL = "";
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;
    public static final int TOUCH_PRESS_POS_DELTA = 5;
    public boolean mWasAutoUnStar = false;

    public long mEntryId = -1;
    private String mEntryLink = "";
    public Runnable mScrollChangeListener = null;
    private int mLastContentLength = 0;
    private Stack<Integer> mHistoryAchorScrollY = new Stack<>();
    private final Handler mHandler = new Handler(this);
    private int mScrollY = 0;
    private int mStatus = 0;
    public boolean mLoadTitleOnly = false;
    public boolean mContentWasLoaded = false;
    private double mLastContentHeight = 0;
    private long mLastTimeScrolled = 0;
    private String mDataWithWebLinks = "";
    public boolean mIsEditingMode = false;
    public long mLastSetHTMLTime = 0;
    private ArrayList<String> mImagesToDl = new ArrayList<>();

    private static String GetCSS(final String text, final String url, boolean isEditingMode) {
        String mainFontLocalUrl = GetTypeFaceLocalUrl(PrefUtils.getString("fontFamily", DefaultFontFamily), isEditingMode);
        final CustomClassFontInfo customFontInfo = GetCustomClassAndFontName("font_rules", url);
        if ( !customFontInfo.mKeyword.isEmpty() && customFontInfo.mClassName.isEmpty() )
            mainFontLocalUrl = GetTypeFaceLocalUrl( customFontInfo.mFontName, isEditingMode );
        return "<head><style type='text/css'> "
                + "@font-face { font-family:\"MainFont\"; src: url(\"" + mainFontLocalUrl + "\");" + "} "
                + "@font-face { font-family:\"CustomFont\"; src: url(\"" + GetTypeFaceLocalUrl(customFontInfo.mFontName, isEditingMode) + "\");}"
                + "body {max-width: 100%; margin: " + getMargins() + "; text-align:" + getAlign(text) + "; font-weight: " + getFontBold() + "; "
                + "font-family: \"MainFont\"; color: " + Theme.GetTextColor() + "; background-color:" + Theme.GetBackgroundColor() + "; " +  PrefUtils.getString( "main_font_css_text", "" ) + "; line-height: 120%} "
                + "* {max-width: 100%; word-break: break-word}"
                + "h1, h2 {font-weight: normal; line-height: 120%} "
                + "h1 {font-size: 140%; text-align:center; margin-top: 1.0cm; margin-bottom: 0.1em} "
                + "h2 {font-size: 120%} "
                + "}body{color: #000; text-align: justify; background-color: #fff;}"
                + "a.no_draw_link {color: " + Theme.GetTextColor() + "; background: " + Theme.GetBackgroundColor() + "; text-decoration: none" + "}"
                + "a {color: " + Theme.GetColor(LINK_COLOR, R.string.default_link_color) + "; background: " + Theme.GetColor(LINK_COLOR_BACKGROUND, R.string.default_text_color_background) +
                (PrefUtils.getBoolean("underline_links", true) ? "" : "; text-decoration: none") + "}"
                + "h1 {color: inherit; text-decoration: none}"
                + "img {display: inline;max-width: 100%;height: auto; " + (PrefUtils.isImageWhiteBackground() ? "background: white" : "") + "} "
                + "iframe {allowfullscreen; position:relative;top:0;left:0;width:100%;height:100%;}"
                + "pre {white-space: pre-wrap;} "
                + "blockquote {border-left: thick solid " + Theme.GetColor(QUOTE_LEFT_COLOR, android.R.color.black) + "; background-color:" + Theme.GetColor(QUOTE_BACKGROUND_COLOR, android.R.color.black) + "; margin: 0.5em 0 0.5em 0em; padding: 0.5em} "
                + "td {font-weight: " + getFontBold() + "; text-align:" + getAlign(text) + "} "
                + "hr {width: 100%; color: #777777; align: center; size: 1} "
                + "p.button {text-align: center} "
                + "p {font-family: \"MainFont\"; margin: 0.8em 0 0.8em 0; text-align:" + getAlign(text) + "} "
                + getCustomFontClassStyle("p", url, customFontInfo)
                + getCustomFontClassStyle("span", url, customFontInfo)
                + getCustomFontClassStyle("div", url, customFontInfo)
                + "p.subtitle {color: " + Theme.GetColor(SUBTITLE_COLOR, android.R.color.black) + "; border-top:1px " + Theme.GetColor(SUBTITLE_BORDER_COLOR, android.R.color.black) + "; border-bottom:1px " + Theme.GetColor(SUBTITLE_BORDER_COLOR, android.R.color.black) + "; padding-top:2px; padding-bottom:2px; font-weight:800 } "
                + "ul, ol {margin: 0 0 0.8em 0.6em; padding: 0 0 0 1em} "
                + "ul li, ol li {margin: 0 0 0.8em 0; padding: 0} "
                + "div.bottom-page {display: block; min-height: 80vh} "
                + "div.button-section {padding: 0.8cm 0; margin: 0; text-align: center} "
                + "div {text-align:" + getAlign(text) + "} "
                //+ "* { -webkit-tap-highlight-color: rgba(" + Theme.GetToolBarColorRGBA() + "); } "
                + ".categories {font-style: italic; color: " + Theme.GetColor(SUBTITLE_COLOR, android.R.color.black) + "} "
                + ".button-section p {font-family: \"MainFont\"; margin: 0.1cm 0 0.2cm 0} "
                + ".button-section p.marginfix {margin: 0.2cm 0 0.2cm 0}"
                + ".button-section input, .button-section a {font-family: \"MainFont\"; font-size: 100%; color: #FFFFFF; background-color: " + Theme.GetToolBarColor() + "; text-decoration: none; border: none; border-radius:0.2cm; margin: 0.2cm} "
                + "." + TAG_BUTTON_CLASS + " i { font-size: 100%; color: #FFFFFF; background-color: " + Theme.GetToolBarColor() + "; text-decoration: none; border: none; border-radius:0.2cm;  margin-right: 0.2cm; padding-top: 0.0cm; padding-bottom: 0.0cm; padding-left: 0.2cm; padding-right: 0.2cm} "
                + "." + TAG_BUTTON_CLASS_CATEGORY + " i {font-size: 100%; color: #FFFFFF; background-color: #00AAAA; text-decoration: none; border: none; border-radius:0.2cm;  margin-right: 0.2cm; padding-top: 0.0cm; padding-bottom: 0.0cm; padding-left: 0.2cm; padding-right: 0.2cm} "
                + "." + TAG_BUTTON_CLASS_DATE + " i {font-size: 100%; color: #FFFFFF; background-color: #0000AA; text-decoration: none; border: none; border-radius:0.2cm;  margin-right: 0.2cm; padding-top: 0.0cm; padding-bottom: 0.0cm; padding-left: 0.2cm; padding-right: 0.2cm} "
                + "." + TAG_BUTTON_FULL_TEXT_ROOT_CLASS + " i {font-size: 100%; color: #FFFFFF; background-color: #00AA00; text-decoration: none; border: none; border-radius:0.2cm;  margin-right: 0.2cm; padding-top: 0.0cm; padding-bottom: 0.0cm; padding-left: 0.2cm; padding-right: 0.2cm} "
                + "." + TAG_BUTTON_CLASS_HIDDEN + " i {font-size: 100%; color: #FFFFFF; background-color: #888888; text-decoration: none; border: none; border-radius:0.2cm;  margin-right: 0.2cm; padding-top: 0.0cm; padding-bottom: 0.0cm; padding-left: 0.2cm; padding-right: 0.2cm} "
                + "</style><meta name='viewport' content='width=device-width'/></head>";
    }

    @NotNull
    private static String getCustomFontClassStyle(String tag, String url, CustomClassFontInfo info) {
        if ( info.mKeyword.isEmpty() )
            return "";
        else
            return tag + ( info.mClassName.isEmpty() ? "" : "." + info.mClassName)
                    +   "{font-family: \"CustomFont\"; " + info.mStyleText + "} ";
    }

    static class CustomClassFontInfo {
        float mLetterSpacing = 0.01F;
        String mClassName = "";
        String mKeyword = "";
        String mFontName = "";
        String mStyleText = "";
        CustomClassFontInfo( String line ) {
            String[] list1 = line.split(":");
            if ( list1.length >= 1 ) {
                mKeyword = list1[0];
                if (list1.length >= 2) {
                    String[] list2 = list1[1].split("=");
                    if (list2.length >= 2) {
                        mClassName = list2[0].toLowerCase();
                        mFontName = list2[1];
                    }
                    if (list2.length >= 3)
                        mStyleText = list2[2];
                }
            }
            for ( int i = 0; i < list1.length; i++ )
                if (i >= 2)
                    mStyleText += ":" + list1[i];
        }

    }
    private static CustomClassFontInfo GetCustomClassAndFontName(final String key, final String url ) {
        final String pref = PrefUtils.getString( key, "" );
        for( String line: pref.split( "\\n" ) ) {
            if ((line == null) || line.isEmpty())
                continue;
            try {
                CustomClassFontInfo info = new CustomClassFontInfo( line );
                if (url.contains(info.mKeyword)) {
                    return info;
                }
            } catch (Exception e) {
                Dog.e(e.getMessage());
            }
        }
        return new CustomClassFontInfo("");
    }


    private static String getFontBold() {
        if (PrefUtils.getBoolean(PrefUtils.ENTRY_FONT_BOLD, false))
            return "bold;";
        else
            return "normal;";
    }

    private static String getMargins() {
        if (PrefUtils.getBoolean(PrefUtils.ENTRY_MAGRINS, true))
            return "4%";
        else
            return "0.1cm";
    }

    public static String getAlign(String text) {
        if (isTextRTL(text))
            return "right";
        else if (PrefUtils.getBoolean(PrefUtils.ENTRY_TEXT_ALIGN_JUSTIFY, false))
            return "justify";
        else
            return "left";
    }

    private static boolean isWordRTL(String s) {
        if (s.isEmpty()) {
            return false;
        }
        char c = s.charAt(0);
        return c >= 0x590 && c <= 0x6ff;
    }

    public static boolean isTextRTL(String text) {
        String[] list = TextUtils.split(text, " ");
        for (String item : list)
            if (isWordRTL(item))
                return true;
        return false;
    }

    public static boolean isRTL(String text) {
        final int directionality = Character.getDirectionality(Locale.getDefault().getDisplayName().charAt(0));
        return (directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC) && isTextRTL(text);
    }

    private static final String BODY_START = "<body dir=\"%s\">";
    private static final String BODY_END = "</body>";
    private static final String TITLE_START_WITH_LINK = "<h1><a class='no_draw_link' href=\"%s\">";
    private static final String TITLE_START = "<h1>";
    //private static final String TITLE_MIDDLE = "'>";
    private static final String TITLE_END = "</h1>";
    private static final String TITLE_END_WITH_LINK = "</a></h1>";
    private static final String SUBTITLE_START = "<p class='subtitle'>";
    private static final String SUBTITLE_END = "</p>";
    private static final String CATEGORIES_START = "<p class='categories'>";
    private static final String CATEGORIES_END = "</p>";
    private static final String BUTTON_SECTION_START = "<div class='button-section'>";
    private static final String BOTTOM_PAGE = "<div class='bottom-page'/>";
    private static final String BUTTON_SECTION_END = "</div>";
    private static String BUTTON_START( String layout ) {
        return ( layout.equals( ARTICLE_TEXT_BUTTON_LAYOUT_HORIZONTAL ) ? "" : "<p class='button'>" ) + "<input type='button' value='";
    }
    private static final String BUTTON_MIDDLE = "' onclick='";
    private static String BUTTON_END( String layout ) {
        return "'/>" + (layout.equals(ARTICLE_TEXT_BUTTON_LAYOUT_HORIZONTAL) ? "" : "</p>");
    }
    // the separate 'marginfix' selector in the following is only needed because the CSS box model treats <input> and <a> elements differently
    private static final String LINK_BUTTON_START = "<p class='marginfix'><a href='";
    private static final String LINK_BUTTON_MIDDLE = "'>";
    private static final String LINK_BUTTON_END = "</a></p>";
    private static final String IMAGE_ENCLOSURE = "[@]image/";
    private static final long TAP_TIMEOUT = 300;
    private static final long MOVE_TIMEOUT = 800;

    private final JavaScriptObject mInjectedJSObject = new JavaScriptObject();
    private final ImageDownloadJavaScriptObject mImageDownloadObject = new ImageDownloadJavaScriptObject();
    public static final ImageDownloadObservable mImageDownloadObservable = new ImageDownloadObservable();
    private EntryViewManager mEntryViewMgr;
    String mData = "";
    public double mScrollPartY = 0;

    private EntryActivity mActivity;
    private long mPressedTime = 0;
    private long mMovedTime = 0;



    public EntryView(Context context) {
        super(context);
        init();
    }

    public EntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EntryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListener(EntryViewManager manager) {
        mEntryViewMgr = manager;
    }

    public boolean setHtml(final long entryId,
                           Cursor newCursor,
                           FeedFilters filters,
                           boolean isFullTextShown,
                           boolean forceUpdate,
                           EntryActivity activity) {
        Timer timer = new Timer("EntryView.setHtml");
        mLastSetHTMLTime = new Date().getTime();

        mEntryId = entryId;
        mEntryLink = newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.LINK));

        final String feedID = newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.FEED_ID));
        final String author = newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.AUTHOR));
        final String categories = newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.CATEGORIES));
        final long timestamp = newCursor.getLong(newCursor.getColumnIndex(FeedData.EntryColumns.DATE));
        //final String feedTitle = filters.removeTextFromTitle( newCursor.getString(newCursor.getColumnIndex(FeedData.FeedColumns.NAME)) );
        String title =
                newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.TITLE));
        if ( filters != null )
            title = filters.removeText(title, DB_APPLIED_TO_TITLE );
        final String enclosure = newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.ENCLOSURE));
        mWasAutoUnStar = newCursor.getInt(newCursor.getColumnIndex(FeedData.EntryColumns.IS_WAS_AUTO_UNSTAR)) == 1;
        mScrollPartY = !newCursor.isNull(newCursor.getColumnIndex(FeedData.EntryColumns.SCROLL_POS)) ?
                newCursor.getDouble(newCursor.getColumnIndex(FeedData.EntryColumns.SCROLL_POS)) : 0;
        boolean hasOriginal = !feedID.equals(GetExtrenalLinkFeedID());
        try {
            JSONObject options = new JSONObject( newCursor.getString(newCursor.getColumnIndex(FeedData.FeedColumns.OPTIONS)) );
            hasOriginal = hasOriginal && options.has( IS_RSS ) && options.getBoolean( IS_RSS );
        } catch (Exception ignored) {

        }
        String contentText;
        if (mLoadTitleOnly)
            contentText = getContext().getString(R.string.loading);
        else {
            try {
                if (!feedID.equals(GetExtrenalLinkFeedID()) &&
                        (!FileUtils.INSTANCE.isMobilized(mEntryLink, newCursor) || (forceUpdate && !isFullTextShown))) {
                    isFullTextShown = false;
                    contentText = newCursor.getString(newCursor.getColumnIndex(FeedData.EntryColumns.ABSTRACT));
                } else {
                    isFullTextShown = true;
                    contentText = FileUtils.INSTANCE.loadMobilizedHTML(mEntryLink, newCursor);
                }
                if (contentText == null) {
                    contentText = "";
                }

            } catch (IllegalStateException e) {
                e.printStackTrace();
                contentText = "Context too large";
            }
        }

        mActivity = activity;
        if (!mLoadTitleOnly && contentText.length() == mLastContentLength) {
            EndStatus();
            return isFullTextShown;
        }
        mLastContentLength = contentText.length();
        //getSettings().setBlockNetworkLoads(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setSupportZoom(false);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setBackgroundColor(Color.parseColor(Theme.GetBackgroundColor()));
        // Text zoom level from preferences
        int fontSize = PrefUtils.getFontSize();
        if (fontSize != 0) {
            getSettings().setTextZoom(100 + (fontSize * 15));
        }


        final String finalContentText = contentText;
        final boolean finalIsFullTextShown = isFullTextShown;
        final boolean finalHasOriginal = hasOriginal;
        final String finalTitle = title;
        new Thread() {
            @Override
            public void run() {
                final String dataWithLinks = generateHtmlContent(feedID, finalTitle, mEntryLink, finalContentText, categories, enclosure, author, timestamp, finalIsFullTextShown, finalHasOriginal);
                final ArrayList<String> imagesToDl = new ArrayList<>();
                final String data = HtmlUtils.replaceImageURLs( dataWithLinks, "", mEntryId, mEntryLink, false, imagesToDl, null, mMaxImageDownloadCount );
                synchronized (EntryView.this) {
                    mImagesToDl = imagesToDl;
                    mData = data;
                    mDataWithWebLinks = dataWithLinks;
                }
                UiUtils.RunOnGuiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadData();
                    }
                });
            }
        }.start();
        timer.End();
        return isFullTextShown;
    }

    public void InvalidateContentCache() {
        mLastContentLength = 0;
    }

    private String generateHtmlContent(String feedID, String title, String link, String contentText, String categories,
                                       String enclosure, String author,
                                       long timestamp, boolean canSwitchToFullText, boolean hasOriginalText) {
        Timer timer = new Timer("EntryView.generateHtmlContent");

        StringBuilder content = new StringBuilder(GetCSS(title, link, mIsEditingMode)).append(String.format(BODY_START, isTextRTL(title) ? "rtl" : "inherit"));

        if (link == null) {
            link = "";
        }

        if (PrefUtils.getBoolean("entry_text_title_link", true))
            content.append(String.format(TITLE_START_WITH_LINK, link + NO_MENU)).append(title).append(TITLE_END_WITH_LINK);
        else
            content.append(TITLE_START).append(title).append(TITLE_END);

        content.append(SUBTITLE_START);

        Date date = new Date(timestamp);
        Context context = getContext();
        StringBuilder dateStringBuilder = new StringBuilder(DateFormat.getLongDateFormat(context).format(date)).append(' ').append(
                DateFormat.getTimeFormat(context).format(date));

        if (author != null && !author.isEmpty()) {
            dateStringBuilder.append(" &mdash; ").append(author);
        }

        content.append(dateStringBuilder).append(SUBTITLE_END);
        if (categories != null && !categories.isEmpty())
            content.append( CATEGORIES_START ).append( CategoriesToOutput( categories ) ).append( CATEGORIES_END );

        content.append(contentText);


        final String layout = PrefUtils.getString( "setting_article_text_buttons_layout", ARTICLE_TEXT_BUTTON_LAYOUT_HORIZONTAL );
        if ( !layout.equals( "Hidden" ) ) {
            content.append(BUTTON_SECTION_START);
            if (!feedID.equals(FetcherService.GetExtrenalLinkFeedID())) {
                content.append(BUTTON_START(layout));

                if (!canSwitchToFullText) {
                    content.append(context.getString(R.string.get_full_text)).append(BUTTON_MIDDLE).append("injectedJSObject.onClickFullText();");
                } else if (hasOriginalText) {
                    content.append(context.getString(R.string.original_text)).append(BUTTON_MIDDLE).append("injectedJSObject.onClickOriginalText();");
                }
                content.append(BUTTON_END(layout));
            }

            if (canSwitchToFullText)
                content.append(BUTTON_START(layout)).append(context.getString(R.string.btn_reload_full_text)).append(BUTTON_MIDDLE)
                        .append("injectedJSObject.onReloadFullText();").append(BUTTON_END(layout));
            if (enclosure != null && enclosure.length() > 6 && !enclosure.contains(IMAGE_ENCLOSURE)) {
                content.append(BUTTON_START(layout)).append(context.getString(R.string.see_enclosure)).append(BUTTON_MIDDLE)
                        .append("injectedJSObject.onClickEnclosure();").append(BUTTON_END(layout));
            }
            content.append(BUTTON_START(layout)).append(context.getString(R.string.menu_go_back)).append(BUTTON_MIDDLE)
                    .append("injectedJSObject.onClose();").append(BUTTON_END(layout));

            content.append(BUTTON_SECTION_END).append(BOTTOM_PAGE).append(BODY_END);
        }

        timer.End();
        return content.toString();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init() {

        StatusStartPageLoading();
        setBackgroundColor(Color.parseColor(Theme.GetBackgroundColor()));

        Timer timer = new Timer("EntryView.init");

        getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            setTextDirection(TEXT_DIRECTION_LOCALE);
        // For scrolling
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(true);
        getSettings().setUseWideViewPort(true);
        // For color

        // For javascript
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(mInjectedJSObject, mInjectedJSObject.toString());
        addJavascriptInterface(mImageDownloadObject, mImageDownloadObject.toString());
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //    setNestedScrollingEnabled(true);
        // For HTML5 video
        setWebChromeClient(new WebChromeClient() {
            private View mCustomView;
            private CustomViewCallback mCustomViewCallback;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // if a view already exists then immediately terminate the new one
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                FrameLayout videoLayout = mEntryViewMgr.getVideoLayout();
                if (videoLayout != null) {
                    mCustomView = view;

                    setVisibility(View.GONE);
                    videoLayout.setVisibility(View.VISIBLE);
                    videoLayout.addView(view);
                    mCustomViewCallback = callback;

                    mEntryViewMgr.onStartVideoFullScreen();
                }
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                if (mCustomView == null) {
                    return;
                }

                FrameLayout videoLayout = mEntryViewMgr.getVideoLayout();
                if (videoLayout != null) {
                    setVisibility(View.VISIBLE);
                    videoLayout.setVisibility(View.GONE);

                    // HideByScroll the custom view.
                    mCustomView.setVisibility(View.GONE);

                    // Remove the custom view from its container.
                    videoLayout.removeView(mCustomView);
                    mCustomViewCallback.onCustomViewHidden();

                    mCustomView = null;

                    mEntryViewMgr.onEndVideoFullScreen();
                }
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                Bitmap result = super.getDefaultVideoPoster();
                if (result == null)
                    result = BitmapFactory.decodeResource(MainApplication.getContext().getResources(), android.R.drawable.presence_video_online);
                return result;
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
            }


        });

        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                DoNotShowMenu();
                if ( System.currentTimeMillis() - mMovedTime < MOVE_TIMEOUT  )
                    return true;
                final Context context = getContext();
                try {
                    if (url.startsWith(Constants.FILE_SCHEME)) {
                        OpenImage(url, context);
                    } else if (url.contains("#")) {
                        String hash = url.substring(url.indexOf('#') + 1);
                        hash = URLDecoder.decode(hash);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            view.evaluateJavascript("javascript:window.location.hash = '" + hash + "';", null);
                            mHistoryAchorScrollY.push(getScrollY());
                        }
                    } else if (url.contains(NO_MENU)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getContext().startActivity(intent.setData(Uri.parse(url.replace(NO_MENU, ""))));
                    } else {

                        class Item {
                            public final String text;
                            public final int icon;

                            private Item(int textID, Integer icon) {
                                this.text = getContext().getString(textID);
                                this.icon = icon;
                            }
                            private Item(String text, Integer icon) {
                                this.text = text;
                                this.icon = icon;
                            }

                            @Override
                            public String toString() {
                                return text;
                            }
                        }


                        final Item[] items = {
                                new Item(url, 0),
                                new Item(R.string.loadLink, R.drawable.cup_new_load_now),
                                new Item(R.string.loadLinkLater, R.drawable.cup_new_load_later),
                                new Item(R.string.loadLinkLaterStarred, R.drawable.cup_new_load_later_star),
                                new Item(R.string.open_link, android.R.drawable.ic_menu_send),
                                new Item(R.string.menu_share, android.R.drawable.ic_menu_share)
                        };

                        final Item[] itemsNoRead = {
                                new Item(url, 0),
                                new Item(R.string.open_link, android.R.drawable.ic_menu_send),
                                new Item(R.string.menu_share, android.R.drawable.ic_menu_share)
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setAdapter(new ArrayAdapter<Item>(
                                getContext(),
                                android.R.layout.select_dialog_item,
                                android.R.id.text1,
                                !isLinkToLoad( url ) ? itemsNoRead : items) {
                            @NonNull
                            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                                //Use super class to create the View
                                View v = super.getView(position, convertView, parent);
                                TextView tv = SetupTextView( v, android.R.id.text1);
                                if ( items[position].icon > 0 ) {
                                    //Put the image on the TextView
                                    int dp50 = (int) (50 * getResources().getDisplayMetrics().density + 0.5f);
                                    Drawable dr = getResources().getDrawable(items[position].icon);
                                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, dp50, dp50, true));
                                    d.setBounds(0, 0, dp50, dp50);
                                    tv.setCompoundDrawables(d, null, null, null);
                                    //Add margin between image and text (support various screen densities)
                                    int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                                    tv.setCompoundDrawablePadding(dp5);
                                } else {
                                    tv = SetupSmallTextView(v, android.R.id.text1);
                                    tv.setCompoundDrawables( null, null, null, null );
                                }
                                return v;
                            }
                        }, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item > 0) {
                                    Intent intent = null;
                                    if (item == 1)
                                        intent = new Intent(getContext(), EntryActivity.class).setData(Uri.parse(url));
                                    else if (item == 2)
                                        intent = new Intent(getContext(), LoadLinkLaterActivity.class).setData(Uri.parse(url));
                                    else if (item == 3) {
                                        LabelVoc.INSTANCE.showDialog(getContext(), R.string.article_labels_setup_title, false, new HashSet<Long>(), null, (checkedLabels) -> {
                                            Intent intent_ = new Intent(getContext(), LoadLinkLaterActivity.class).setData(Uri.parse(url)).putExtra(FetcherService.EXTRA_STAR, true);
                                            ArrayList<String> list = new ArrayList<>();
                                            for( long labelID: checkedLabels )
                                                list.add( String.valueOf( labelID ) );
                                            intent_.putStringArrayListExtra( EXTRA_LABEL_ID_LIST, list );
                                            getContext().startActivity(intent_);
                                            return null;
                                        });
                                    } else if (item == 4)
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    else
                                        intent = Intent.createChooser(
                                                new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, url)
                                                        .setType(Constants.MIMETYPE_TEXT_PLAIN), getContext().getString(R.string.menu_share));
                                    if ( intent != null )
                                        getContext().startActivity(intent);
                                }
                            }
                        });

                        final String urlWithoutRegexSymbols =
                                url.replace( "/", "." ).
                                        replace( ":", "." ).
                                        replace( "?", "." ).
                                        replace( "+", "." ).
                                        replace( "&", "&amp;" ).
                                        replace( "*", "." ).
                                        replace( ",", "." );
                        final Pattern REGEX = java.util.regex.Pattern.compile("<a[^>]+?href=.url.+?>(.+?)</a>".replace( "url", urlWithoutRegexSymbols ), java.util.regex.Pattern.CASE_INSENSITIVE);
                        Matcher matcher = REGEX.matcher(mData);
                        final String title = matcher.find() ? Jsoup.parse( matcher.group( 1 ) ).text() : url;
                        builder.setTitle( url.equals( title )  ? "" : title );
                        builder.show();
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, R.string.cant_open_link, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                StatusStartPageLoading();
                ScheduleScrollTo(view);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!mLoadTitleOnly)
                    mContentWasLoaded = true;

                StatusStartPageLoading();
                ScheduleScrollTo(view);
            }

            private void ScheduleScrollTo(final WebView view) {
                Dog.v(TAG, "EntryView.ScheduleScrollTo() mEntryID = " + mEntryId + ", mScrollPartY=" + mScrollPartY + ", GetScrollY() = " + GetScrollY() + ", GetContentHeight()=" + GetContentHeight() );
                double newContentHeight = GetContentHeight();
                final String searchText = mActivity.getIntent().getStringExtra( "SCROLL_TEXT" );
                final boolean isSearch = searchText != null && !searchText.isEmpty();
                if (newContentHeight > 0 && newContentHeight == mLastContentHeight) {
                    if ( isSearch ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            findAllAsync(searchText);
                        else
                            findAll(searchText);
                        UiUtils.RunOnGuiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.clearMatches();
                            }
                        }, 5000 );
                    } else
                        ScrollToY();
                    DownLoadImages();
                    EndStatus();
                } else {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ScheduleScrollTo(view);
                        }
                    }, 150);
                    if ( !isSearch )
                        ScrollToY();
                }
                mLastContentHeight = newContentHeight;
            }
        });

        setOnTouchListener(new View.OnTouchListener() {
            private float mPressedY;
            private float mPressedX;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mPressedX = event.getX();
                    mPressedY = event.getY();
                    mPressedTime = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (Math.abs(event.getX() - mPressedX) > TOUCH_PRESS_POS_DELTA ||
                            Math.abs(event.getY() - mPressedY) > TOUCH_PRESS_POS_DELTA) {
                        mPressedTime = 0;
                        mMovedTime = System.currentTimeMillis();
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ( System.currentTimeMillis() - mPressedTime < TAP_TIMEOUT &&
                            Math.abs(event.getX() - mPressedX) < TOUCH_PRESS_POS_DELTA &&
                            Math.abs(event.getY() - mPressedY) < TOUCH_PRESS_POS_DELTA &&
                            System.currentTimeMillis() - mLastTimeScrolled > 500 &&
                            PrefUtils.isArticleTapEnabled() &&
                            EntryActivity.GetIsActionBarHidden() &&
                            !mActivity.mHasSelection) {
                        mHandler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 300);
                    }
                }
                return false;
            }
        });

        //setNestedScrollingEnabled( true );
        timer.End();
    }

    private void DownLoadImages() {
        final ArrayList<String> imagesToDl = GetImageListCopy();
        if ( !imagesToDl.isEmpty() )
            new Thread(() -> {
                FetcherService.downloadEntryImages("", mEntryId, mEntryLink, imagesToDl);
                ClearImageList();
            }).start();
    }

    private void ClearImageList() {
        synchronized ( EntryView.this ) {
            mImagesToDl.clear();
        }
    }

    @NotNull
    private ArrayList<String> GetImageListCopy() {
        final ArrayList<String> imagesToDl;
        synchronized ( EntryView.this ) {
            imagesToDl = (ArrayList<String>) mImagesToDl.clone();
        }
        return imagesToDl;
    }

    public static void OpenImage( String url, Context context ) throws IOException {
        File file = new File(url.replace(Constants.FILE_SCHEME, ""));
        File extTmpFile = new File(context.getCacheDir(), file.getName());
        FileUtils.INSTANCE.copy(file, extTmpFile);
        Uri contentUri = getUriForFile(context, FeedData.PACKAGE_NAME + ".fileprovider", extTmpFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(contentUri, "image/*");
        context.startActivity(intent);
    }

    private void EndStatus() {
        synchronized (EntryView.this) {
            if (mStatus != 0)
                FetcherService.Status().End(mStatus);
            mStatus = 0;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL) {
            mHandler.removeMessages(CLICK_ON_WEBVIEW);
            mActivity.closeContextMenu();
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW) {
            mActivity.openOptionsMenu();
            return true;
        }
        return false;
    }

    private void ScrollToY() {
        Dog.v(TAG, "EntryView.ScrollToY() mEntryID = " + mEntryId + ", mScrollPartY=" + mScrollPartY + ", GetScrollY() = " + GetScrollY());
        if (GetScrollY() > 0)
            EntryView.this.scrollTo(0, GetScrollY());
    }

    public String GetData() {
        synchronized (EntryView.this) {
            return mData;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        FetcherService.Status().HideByScroll();
        //int contentHeight = (int) Math.floor(GetContentHeight());
        //int webViewHeight = getMeasuredHeight();
        if ( mActivity.mEntryFragment != null )
            mActivity.mEntryFragment.UpdateFooter();

        mLastTimeScrolled = System.currentTimeMillis();
        if (mScrollChangeListener != null)
            mScrollChangeListener.run();
    }

    public boolean IsScrollAtBottom() {
        return getScrollY() + getMeasuredHeight() >= (int) Math.floor(GetContentHeight()) - getMeasuredHeight() * 0.4;
    }

    public void UpdateImages( final boolean downloadImages ) {
        StatusStartPageLoading();
        new Thread() {
            @Override
            public void run() {
                final String data = HtmlUtils.replaceImageURLs( mDataWithWebLinks, mEntryId, mEntryLink, downloadImages);
                synchronized (EntryView.this) {
                    mData = data;
                }
                UiUtils.RunOnGuiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ( !IsStatusStartPageLoading() )
                            mScrollY = getScrollY();
                        LoadData();
                    }
                });
            }
        }.start();
    }

    public void UpdateTags() {
        final int status = FetcherService.Status().Start(getContext().getString(R.string.last_update), true);
        Document doc = Jsoup.parse(ArticleTextExtractor.mLastLoadedAllDoc, NetworkUtils.getUrlDomain(mEntryLink));
        Element root = FindBestElement(doc, mEntryLink, "", true);
        AddTagButtons(doc, mEntryLink, root);
        final String data = generateHtmlContent("-1", "", mEntryLink, doc.toString(), "", "", "", 0, true, false);
        synchronized (EntryView.this) {
            mData = data;
        }
        mScrollY = getScrollY();
        LoadData();
        FetcherService.Status().End(status);
    }

    private void LoadData() {
        if (mContentWasLoaded && GetViewScrollPartY() > 0)
            mScrollPartY = GetViewScrollPartY();
        //StatusStartPageLoading();
        final String data;
        synchronized (EntryView.this) {
            data = mData;
        }
        mLastContentHeight = 0;
        loadDataWithBaseURL(BASE_URL, data, TEXT_HTML, Constants.UTF8, null);
    }

    public void StatusStartPageLoading() {
        synchronized (EntryView.this) {
            if (mStatus == 0)
                mStatus = FetcherService.Status().Start(R.string.web_page_loading, true);
        }
    }
    private boolean IsStatusStartPageLoading() {
        synchronized (EntryView.this) {
            return mStatus == 0;
        }
    }

    static int NOTIFY_OBSERVERS_DELAY_MS = 1000;

    public static void NotifyToUpdate(final long entryId, final String entryLink) {

        UiUtils.RunOnGuiThread(new Runnable() {
            @Override
            public void run() {
                Dog.d( String.format( "NotifyToUpdate( %d )", entryId ) );
                EntryView.mImageDownloadObservable.notifyObservers(new Entry(entryId, entryLink) );//ScheduledNotifyObservers(entryId, entryLink);
            }
        }, 0 );//NOTIFY_OBSERVERS_DELAY_MS);
    }

    static HashMap<Long, Long> mLastNotifyObserversTime = new HashMap<>();
    static HashMap<Long, Boolean> mLastNotifyObserversScheduled = new HashMap<>();

    static void ScheduledNotifyObservers(final long entryId, final String entryLink) {
        mLastNotifyObserversTime.put(entryId, new Date().getTime());

        if (!mLastNotifyObserversScheduled.containsKey(entryId)) {
            mLastNotifyObserversScheduled.put(entryId, true);
            UiUtils.RunOnGuiThread(new ScheduledEnrtyNotifyObservers(entryId, entryLink), NOTIFY_OBSERVERS_DELAY_MS);
        }
    }

    public boolean CanGoBack() {
        return canGoBack() && !mHistoryAchorScrollY.isEmpty();
    }

    public void GoBack() {
        if (CanGoBack())
            scrollTo(0, mHistoryAchorScrollY.pop());
    }

    public void GoTop() {
        mHistoryAchorScrollY.push(getScrollY());
        scrollTo(0, 0);
    }


    public interface EntryViewManager {
        void onClickOriginalText();

        void onClickFullText();

        void onClickEnclosure();

        void onReloadFullText();

        void onClose();

        void onStartVideoFullScreen();

        void onEndVideoFullScreen();

        FrameLayout getVideoLayout();

        void downloadImage(String url);

        void openTagMenu(String className, String baseUrl, String paramValue);

        void downloadNextImages();

        void downloadAllImages();
    }

    private class JavaScriptObject {
        @Override
        @JavascriptInterface
        public String toString() {
            return "injectedJSObject";
        }

        @JavascriptInterface
        public void onClickOriginalText() {
            DoNotShowMenu();
            mEntryViewMgr.onClickOriginalText();
        }

        @JavascriptInterface
        public void onClickFullText() {
            DoNotShowMenu();
            mEntryViewMgr.onClickFullText();
        }

        @JavascriptInterface
        public void onClickEnclosure() {
            DoNotShowMenu();
            mEntryViewMgr.onClickEnclosure();
        }

        @JavascriptInterface
        public void onReloadFullText() {
            DoNotShowMenu();
            mEntryViewMgr.onReloadFullText();
        }

        @JavascriptInterface
        public void onClose() {
            DoNotShowMenu();
            mEntryViewMgr.onClose();
        }
    }

    private void DoNotShowMenu() {
        mHandler.sendEmptyMessage(CLICK_ON_URL);
        mActivity.closeOptionsMenu();
    }

    private class ImageDownloadJavaScriptObject {
        @Override
        @JavascriptInterface
        public String toString() {
            return "ImageDownloadJavaScriptObject";
        }

        @JavascriptInterface
        public void downloadImage(String url) {
            DoNotShowMenu();
            mEntryViewMgr.downloadImage(url);
        }

        @JavascriptInterface
        public void downloadNextImages() {
            DoNotShowMenu();
            mEntryViewMgr.downloadNextImages();

        }

        @JavascriptInterface
        public void downloadAllImages() {
            DoNotShowMenu();
            mEntryViewMgr.downloadAllImages();

        }

        @JavascriptInterface
        public void openTagMenu(String className, String baseUrl, String paramValue) {
            DoNotShowMenu();
            mEntryViewMgr.openTagMenu(className, baseUrl, paramValue);
        }
    }

    public static class ImageDownloadObservable extends Observable {
        @Override
        public boolean hasChanged() {
            return true;
        }
    }


    private int GetScrollY() {
        if (mScrollY != 0)
            return mScrollY;
        return GetContentHeight() * mScrollPartY != 0 ? (int) (GetContentHeight() * mScrollPartY) : 0;
    }

    public double GetContentHeight() {
        return getContentHeight() * getScale();
    }

    public double GetViewScrollPartY() {
        return getContentHeight() != 0 ? getScrollY() / GetContentHeight() : 0;
    }

    public void ScrollTo(int y) {
        ObjectAnimator anim = ObjectAnimator.ofInt(this, "scrollY", getScrollY(), y);
        anim.setDuration(PAGE_SCROLL_DURATION_MSEC);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }
    public void PageChange(int delta, StatusText statusText) {
        ScrollTo((int) (getScrollY() + delta * (getHeight() - statusText.GetHeight()) *
                (PrefUtils.getBoolean("page_up_down_90_pct", false) ? 0.9 : 0.98)));
    }

    @Override
    public void onPause() {
        super.onPause();
        SaveScrollPos();
        EndStatus();
    }

    public void SaveScrollPos() {
        if ( !mContentWasLoaded )
            return;
        mScrollPartY = GetViewScrollPartY();
        if ( mScrollPartY > 0.0001 ) {
            ContentValues values = new ContentValues();
            values.put(FeedData.EntryColumns.SCROLL_POS, mScrollPartY);
            ContentResolver cr = MainApplication.getContext().getContentResolver();
            SetNotifyEnabled(false ); try {
                //String where = FeedData.EntryColumns.SCROLL_POS + " < " + scrollPart + Constants.DB_OR + FeedData.EntryColumns.SCROLL_POS + Constants.DB_IS_NULL;
                cr.update(FeedData.EntryColumns.CONTENT_URI(mEntryId), values, null, null);
            } finally {
                SetNotifyEnabled( true );
            }
        }
    }

}

class ScheduledEnrtyNotifyObservers implements Runnable {
    private final String mLink;
    private long mId = 0;

    public ScheduledEnrtyNotifyObservers( long id, String link ) {
        mId = id;
        mLink = link;
    }

    @Override
    public void run() {
        EntryView.mLastNotifyObserversScheduled.remove( mId );
        Dog.v( EntryView.TAG,"EntryView.ScheduledNotifyObservers() run");
        if (new Date().getTime() - EntryView.mLastNotifyObserversTime.get( mId ) > EntryView.NOTIFY_OBSERVERS_DELAY_MS)
            EntryView.mImageDownloadObservable.notifyObservers(new Entry(mId, mLink) );
        else
            EntryView.ScheduledNotifyObservers( mId, mLink );
    }
}
