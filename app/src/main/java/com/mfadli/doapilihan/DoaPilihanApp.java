package com.mfadli.doapilihan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mfadli.doapilihan.data.DBHelper;
import com.mfadli.doapilihan.data.DBManager;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.BGPattern;
import com.mfadli.doapilihan.model.Font;
import com.securepreferences.SecurePreferences;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mfad on 24/03/2016.
 */
public class DoaPilihanApp extends Application {
    private static final String LOG_TAG = DoaPilihanApp.class.getSimpleName();
    private static final String PROPERTY_ID = "UA-75852856-1";
    private static final String DOA_FONT_SIZE_PREF = "DoaFontSize";
    private static final String TRANSLATION_EN_PREF = "TranslationEn";
    private static final String DOA_LINE_SPACING_SIZE_PREF = "LineSpacingSize";
    private static final String DOA_FONT_TYPE_PREF = "DoaFontType";
    private static final String SHOULD_SHOW_ADS_PREF = "ShouldShowAds";
    private static final String PREMIUM_PREF = "PremiumPref";
    private static final String BG_PATTERN_PREF = "BGPatternPref";
    private static List<Font> sFontList = new ArrayList<>();
    private static List<BGPattern> sBgPatternList = new ArrayList<>();

    private static Context sContext;
    private static DBHelper sDBHelper;
    private static SecurePreferences mSecurePreferences;
    private RxBus mRxBus = null;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sContext = getApplicationContext();
        sDBHelper = new DBHelper();
        DBManager.initializeInstance(sDBHelper);

        AesCbcWithIntegrity.SecretKeys myKeys = null;
        try {
            myKeys = AesCbcWithIntegrity
                    .generateKeyFromPassword(getString(R.string.passwd), getString(R.string.salt), 500);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        mSecurePreferences = new SecurePreferences(getContext(), myKeys, "com.mfadli.doapilihan.pref");

        checkAds();
    }

    /**
     * First run will check if ads is turned off. Turn in back on.
     */
    private void checkAds() {
        boolean showAds = shouldShowAds();
        boolean isPremium = isPremium();

        if (!isPremium && !showAds) {
            saveShouldShowAds(true);
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public RxBus getRxBusSingleton() {
        if (mRxBus == null) {
            mRxBus = new RxBus();
        }

        return mRxBus;
    }

    /**
     * Get Default {@link Tracker}
     *
     * @return Tracker
     */
    synchronized public Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);// Enable Display Features.
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    /**
     * Convenient function to get system default SharedPreference.
     *
     * @return {@link SharedPreferences}
     */
    public SharedPreferences getPreferences() {
//        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return mSecurePreferences;
    }

    /**
     * Save Doa Font Size selection into local storage.
     *
     * @param size int Font Size
     */
    public void saveDoaFontSize(int size) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(DOA_FONT_SIZE_PREF, size);
        editor.commit();
    }

    /**
     * Read Doa Line Spacing Size from local storage.
     *
     * @return int Line Spacing Size
     */
    public int getDoaLineSpacingSize() {
        return getPreferences().getInt(DOA_LINE_SPACING_SIZE_PREF, 0);
    }

    /**
     * Save Doa Line Spacing Size selection into local storage.
     *
     * @param size int Line Spacing Size
     */
    public void saveDoaLineSpacingSize(int size) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(DOA_LINE_SPACING_SIZE_PREF, size);
        editor.commit();
    }

    /**
     * Read Doa Font Size from local storage.
     *
     * @return int Font Size
     */
    public int getDoaFontSize() {
        return getPreferences().getInt(DOA_FONT_SIZE_PREF, 0);
    }

    /**
     * Read Doa Font Type from local storage.
     *
     * @return int Font Type
     */
    public int getDoaFontType() {
        return getPreferences().getInt(DOA_FONT_TYPE_PREF, 0);
    }

    /**
     * Save Doa Font Type selection into local storage.
     *
     * @param type int Font Type
     */
    public void saveDoaFontType(int type) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(DOA_FONT_TYPE_PREF, type);
        editor.commit();
    }

    /**
     * Read BG Pattern index from local storage and return the BGPattern object.
     *
     * @return BGPattern
     */
    public BGPattern getBgPattern() {
        int index = getPreferences().getInt(BG_PATTERN_PREF, 2);
        return getBgPatterns().get(index);
    }

    /**
     * Read BG Pattern index from local storage.
     *
     * @return int Pattern Index
     */
    public int getPatternIndex() {
        return getPreferences().getInt(BG_PATTERN_PREF, 2);
    }

    /**
     * Save BGPattern selection into local storage.
     *
     * @param pattern int BGPattern index selected.
     */
    public void saveBgPattern(int pattern) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(BG_PATTERN_PREF, pattern);
        editor.commit();
    }

    /**
     * Read if english translation button is clicked.
     *
     * @return boolean
     */
    public boolean isEnglishTranslation() {
        return getPreferences().getBoolean(TRANSLATION_EN_PREF, false);
    }

    /**
     * Save English translation selection into local storage.
     *
     * @param isEnglishTranslation boolean Is Translation selected
     */
    public void saveEnglishTranslation(boolean isEnglishTranslation) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(TRANSLATION_EN_PREF, isEnglishTranslation);
        editor.commit();
    }

    /**
     * Hack to get Font lists
     *
     * @return List<Font>
     */
    public List<Font> getFonts() {
        sFontList.clear();
        sFontList.add(new Font("Normal", ""));
        sFontList.add(new Font("A_Nefel_Botan", "fonts/A_Nefel_Botan.ttf"));
        sFontList.add(new Font("PakType_Naqsh_2.2_volt", "fonts/PakType_Naqsh_2.2_volt.ttf"));

        if (isPremium()) {
            sFontList.add(new Font("Thabit", "fonts/Thabit.ttf"));
            sFontList.add(new Font("Frsspbl", "fonts/Frsspbl.ttf"));
        }

        return sFontList;
    }

    /**
     * Hack to get Pattern Lists
     *
     * @return list<BGPattern>
     */
    public List<BGPattern> getBgPatterns() {
        sBgPatternList.clear();
        sBgPatternList.add(new BGPattern("Normal", R.drawable.bg_0, R.drawable.d_bg_0));
        sBgPatternList.add(new BGPattern("BG 1", R.drawable.bg_1, R.drawable.d_bg_1));
        sBgPatternList.add(new BGPattern("BG 2", R.drawable.bg_2, R.drawable.d_bg_2));

        if (isPremium()) {
            sBgPatternList.add(new BGPattern("BG 3", R.drawable.bg_3, R.drawable.d_bg_3));
            sBgPatternList.add(new BGPattern("BG 4", R.drawable.bg_4, R.drawable.d_bg_4));
            sBgPatternList.add(new BGPattern("BG 5", R.drawable.bg_5, R.drawable.d_bg_5));
        }

        return sBgPatternList;
    }

    /**
     * Premium Font for thanks
     *
     * @return Font
     */
    public Font getPremiumThanksFont() {
        return new Font("DIWANBNT", "fonts/DIWANBNT.ttf");
    }

    /**
     * Save Premium to local storage.
     *
     * @param isPremium boolean
     */
    public void savePremium(boolean isPremium) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(PREMIUM_PREF, isPremium);
        editor.commit();
    }

    /**
     * Read if premium user.
     *
     * @return boolean
     */
    public boolean isPremium() {
        return getPreferences().getBoolean(PREMIUM_PREF, false);
    }

    /**
     * Save ads to show selection into local storage.
     *
     * @param display boolean Is To show ads selected
     */
    public void saveShouldShowAds(boolean display) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(SHOULD_SHOW_ADS_PREF, display);
        editor.commit();
    }

    /**
     * Read is showing ads from local storage.
     * If premium user, return false.
     *
     * @return boolean
     */
    public boolean shouldShowAds() {
        return isPremium() ? false : getPreferences().getBoolean(SHOULD_SHOW_ADS_PREF, true);
    }

    /**
     * Check if BGPattern given is a normal Pattern.
     *
     * @param bgPattern BGPattern
     * @return boolean true if normal
     */
    public boolean isBgPatternNormal(BGPattern bgPattern) {
        return bgPattern.getName().equalsIgnoreCase("Normal");
    }
}
