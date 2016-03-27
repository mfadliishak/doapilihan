package com.mfadli.doapilihan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mfadli.doapilihan.data.DBHelper;
import com.mfadli.doapilihan.data.DBManager;
import com.mfadli.doapilihan.event.RxBus;

/**
 * Created by mfad on 24/03/2016.
 */
public class DoaPilihanApplication extends Application {
    private static final String LOG_TAG = DoaPilihanApplication.class.getSimpleName();
    private static final String DOA_FONT_SIZE_PREF = "DoaFontSize";
    private static final String TRANSLATION_EN_PREF = "TranslationEn";

    private static Context sContext;
    private static DBHelper sDBHelper;
    private RxBus mRxBus = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sDBHelper = new DBHelper();
        DBManager.initializeInstance(sDBHelper);
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
     * Convenient function to get system default SharedPreference.
     *
     * @return {@link SharedPreferences}
     */
    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
     * Read Doa Font Size from local storage.
     *
     * @return int Font Size
     */
    public int getDoaFontSize() {
        return getPreferences().getInt(DOA_FONT_SIZE_PREF, 0);
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
}
