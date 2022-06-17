package com.example.tsquared;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;

import static ru.yanus171.feedexfork.utils.Theme.DARK;
import static ru.yanus171.feedexfork.utils.Theme.LIGHT;
import static ru.yanus171.feedexfork.utils.Theme.BLACK;


public class PrefUtils {

    public static final String FIRST_OPEN = "FIRST_OPEN";
    public static final String DISPLAY_TIP = "DISPLAY_TIP";

    public static final String IS_REFRESHING = "IS_REFRESHING";

    public static final String REFRESH_INTERVAL = "refresh.interval";
    public static final String REFRESH_ENABLED = "refresh.enabled";
    public static final String REFRESH_ONLY_SELECTED = "refresh.only_selected";
    public static final String REFRESH_ON_OPEN_ENABLED = "refreshonopen.enabled";
    public static final String REFRESH_WIFI_ONLY = "refreshwifionly.enabled";
    public static final String DELETE_OLD_INTERVAL = "delete_old_interval";

    public static final String AUTO_BACKUP_INTERVAL = "autobackup.interval";
    public static final String AUTO_BACKUP_ENABLED = "autobackup.enabled";

    public static final String NOTIFICATIONS_ENABLED = "notifications.enabled";
    public static final String NOTIFICATIONS_RINGTONE = "notifications.ringtone";
    public static final String NOTIFICATIONS_VIBRATE = "notifications.vibrate";
    public static final String NOTIFICATIONS_LIGHT = "notifications.light";

    //    public static final String LIGHT_THEME = "lighttheme";
    public static final String THEME = "theme";
    public static final String DISPLAY_IMAGES = "display_images";
    //public static final String FULL_SCREEN_STATUSBAR_VISIBLE = "full_screen_statusbar_visible";
    static final String PRELOAD_IMAGE_MODE = "preload_image_mode";
    public static final String DISPLAY_OLDEST_FIRST = "display_oldest_first";
    public static final String DISPLAY_ENTRIES_FULLSCREEN = "display_entries_fullscreen";
    public static final String ENTRY_FONT_BOLD = "entry_font_bold";
    public static final String SHOW_ARTICLE_URL = "settings_show_article_url";
    public static final String SHOW_ARTICLE_CATEGORY = "settings_show_article_category";
    //public static final String TEXT_COLOR_BRIGHTNESS = "text_color_brightness";
    public static final String MAX_IMAGE_DOWNLOAD_COUNT = "max_image_download_count";
    private static final String MAX_IMAGE_DOWNLOAD_SIZE = "settings_max_image_download_size_kb";
    private static final String MAX_SINGLE_REFRESH_TRAFFIC = "settings_max_single_refresh_traffic_mb";
    public static final String ENTRY_MAGRINS = "entry_margins";
    public static final String ENTRY_TEXT_ALIGN_JUSTIFY = "entry_text_align_justify";
    public static final String LANGUAGE = "language";
    public static final String DATA_FOLDER = "data_folder";
    public static final String READING_NOTIFICATION = "reading_notification";

    public static final String CONTENT_TEXT_ROOT_EXTRACT_RULES = "content_extract_rules";
    public static final String CATEGORY_EXTRACT_RULES = "category_extract_rules";
    public static final String MAIN_IMAGE_EXTRACT_RULES = "main_image_extract_rules";
    public static final String COMMENTS_EXTRACT_RULES = "comments_extract_rules";
    public static final String DATE_EXTRACT_RULES = "date_extract_rules";
    public static final String LOAD_COMMENTS = "load_comments";
    public static final String REMEBER_LAST_ENTRY = "remember_last_entry";
    public static final String TAP_ZONES_VISIBLE = "settings_tap_zones_visible";
    public static final String SHOW_PROGRESS_INFO = "settings_show_progress_info";
    public static final String VIBRATE_ON_ARTICLE_LIST_ENTRY_SWYPE = "vibrate_on_article_list_entry_swype";
    public static final String BRIGHTNESS_GESTURE_ENABLED = "brightness_gesture_enabled";

    public static final String GLOBAL_CLASS_LIST_TO_REMOVE_FROM_ARTICLE_TEXT = "global_class_list_to_remove_from_article_text";

    public static boolean CALCULATE_IMAGES_SIZE() {return getBoolean("calculate_images_size", false );}

    public static final String LAST_ENTRY_URI = "last_entry_uri";
    public static final String LAST_ENTRY_SCROLL_Y = "last_entry_scroll_y";
    public static final String LAST_ENTRY_ID = "last_entry_id";
    public static final String LAST_BRIGHTNESS_FLOAT = "last_brightness_float";
    public static final String LAST_BRIGHTNESS_ONPAUSE_TIME = "last_brightness_onpause_time";


    public static final String VOLUME_BUTTONS_ACTION_DEFAULT = "Default";
    public static final String VOLUME_BUTTONS_ACTION_SWITCH_ENTRY = "SwithEntry";
    public static final String VOLUME_BUTTONS_ACTION_PAGE_UP_DOWN = "PageUpDown";

    public static final String ARTICLE_TEXT_BUTTON_LAYOUT_HORIZONTAL = "Horizontal";

    public static final String KEEP_TIME = "keeptime";

    public static final String FIRST_LAUNCH_TIME = "first_launch_time";


    public static final String SHOW_READ_ARTICLE_COUNT = "show_read_article_count";

    public static final int BASE_TEXT_FONT_SIZE = 18;

    public static int getFontSize() {
        return Integer.parseInt(PrefUtils.getString("fontsize", "0"));
    }
    public static int getFontSizeEntryList() {
        return Integer.parseInt(PrefUtils.getString("fontsize_entrylist", "0"));
    }

    public static final String STATE_IMAGE_WHITE_BACKGROUND = "STATE_IMAGE_WHITE_BACKGROUND";
    public static Boolean isImageWhiteBackground() {
        return PrefUtils.getBoolean( STATE_IMAGE_WHITE_BACKGROUND, false );
    }

    public static final String PREF_ARTICLE_TAP_ENABLED = "article_tap_enabled";
    public static final String PREF_ARTICLE_TAP_ENABLED_TEMP = "article_tap_enabled_temp";
    public static Boolean isArticleTapEnabled() {
        return PrefUtils.getBoolean(PREF_ARTICLE_TAP_ENABLED, true ) && PrefUtils.getBoolean(PREF_ARTICLE_TAP_ENABLED_TEMP, true );
    }

    public static int getFontSizeFooterClock() {
        return Integer.parseInt(PrefUtils.getString("article_text_footer_show_clock_fontsize", "0"));
    }

    public static int getImageDownloadCount() {
        try {
            return Integer.parseInt(PrefUtils.getString(PrefUtils.MAX_IMAGE_DOWNLOAD_COUNT, "10"));
        } catch ( NumberFormatException e ) {
            return 10;
        }
    }

    public static int getMaxSingleRefreshTraffic() {
        try {
            return Integer.parseInt(PrefUtils.getString(PrefUtils.MAX_SINGLE_REFRESH_TRAFFIC, "50"));
        } catch ( NumberFormatException e ) {
            return 50;
        }
    }

    static int getImageMaxDownloadSizeInKb() {
        try {
            return Integer.parseInt(PrefUtils.getString(PrefUtils.MAX_IMAGE_DOWNLOAD_SIZE, "2048"));
        } catch ( NumberFormatException e ) {
            return 2048;
        }
    }

    public static boolean IsShowReadCheckbox() {
        return getBoolean( "show_read_checkbox", false );
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return settings.getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void toggleBoolean(String key, boolean defaultValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, !pref.getBoolean(key, defaultValue ));
        editor.apply();
    }
    public static int getInt(String key, int defValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return settings.getInt(key, defValue);
    }
    public static Float getFloat(String key, Float defValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return settings.getFloat( key, defValue);
    }
    public static int getIntFromText(String key, int defValue) {
        int result = defValue;
        try {
            result = Integer.parseInt( getString( key, String.valueOf( defValue ) ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return result;
    }
    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static long getLong(String key, long defValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return settings.getLong(key, defValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putFloat(String key, Float value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.putFloat(key, value);
        editor.apply();
    }
    public static boolean contains(String key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return settings.contains( key );
    }
    public static String getString(String key, String defValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        String result  = defValue;
        try {
            result = settings.getString(key, defValue);
        } catch (  ClassCastException e ){
            e.printStackTrace();
        }
        return result;
    }

    public static String getString(String key, int defValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return settings.getString(key, MainApplication.getContext().getString( defValue ));
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.putString(key, value);
        editor.apply();
    }

    @SuppressLint("ApplySharedPref")
    public static void putStringCommit(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).edit();
        editor.remove(key);
        editor.apply();
    }

    public static void registerOnPrefChangeListener(OnSharedPreferenceChangeListener listener) {
        try {
            PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).registerOnSharedPreferenceChangeListener(listener);
        } catch (Exception ignored) { // Seems to be possible to have a NPE here... Why??
        }
    }

    public static void unregisterOnPrefChangeListener(OnSharedPreferenceChangeListener listener) {
        try {
            PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext()).unregisterOnSharedPreferenceChangeListener(listener);
        } catch (Exception ignored) { // Seems to be possible to have a NPE here... Why??
        }
    }

    @SuppressLint("ApplySharedPref")
    public static void ToogleTheme(Intent intent ) {
        final String theme = PrefUtils.getString( PrefUtils.THEME, DARK );
//        PrefUtils.putString( PrefUtils.THEME, theme.equals(Theme.LIGHT) ? DARK : Theme.LIGHT);
        if (theme.equals(LIGHT)) PrefUtils.putString( PrefUtils.THEME, DARK);
        if (theme.equals(DARK)) PrefUtils.putString( PrefUtils.THEME, BLACK);
        if (theme.equals(BLACK)) PrefUtils.putString( PrefUtils.THEME, LIGHT);
        Context context = MainApplication.getContext();
        PreferenceManager.getDefaultSharedPreferences(context).edit().commit(); // to be sure all prefs are written
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        //System.exit(0);
        android.os.Process.killProcess( android.os.Process.myPid() );
    }

    public static int GetTapZoneSize() {
        //if ( !isArticleTapEnabled() )
        //    return 0;
        return UiUtils.mmToPixel(Integer.parseInt( PrefUtils.getString( "tap_zone_size", "7" ) ));
    }
    // ------------------------------------------------------------------------------------
    public static int GetPrefColorDefID(String key, int defaultValueID) {
        if (!Theme.IsCustom() )
            return Theme.GetColorInt( key, defaultValueID );
        else {
            int result = Color.parseColor(MainApplication.getContext().getString(defaultValueID));
            return getInt(key, result);
        }
    }

    @NonNull
    public static ArrayList<String> GetRemoveClassList() {
        final ArrayList<String> removeClassList = new ArrayList<>();
        for (String item : TextUtils.split(PrefUtils.getString(GLOBAL_CLASS_LIST_TO_REMOVE_FROM_ARTICLE_TEXT, ""), "\n"))
            if (!item.isEmpty()) {
                for (String item2 : TextUtils.split(item, " "))
                    removeClassList.add(item2);
            }
        return removeClassList;
    }
}