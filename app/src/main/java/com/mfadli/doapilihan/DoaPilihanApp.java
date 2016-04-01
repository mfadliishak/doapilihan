package com.mfadli.doapilihan;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mfadli.doapilihan.data.DBHelper;
import com.mfadli.doapilihan.data.DBManager;
import com.mfadli.doapilihan.event.RxBus;
import com.mfadli.doapilihan.model.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mfad on 24/03/2016.
 */
public class DoaPilihanApp extends Application {
    private static final String LOG_TAG = DoaPilihanApp.class.getSimpleName();
    private static final String DOA_FONT_SIZE_PREF = "DoaFontSize";
    private static final String TRANSLATION_EN_PREF = "TranslationEn";
    private static final String DOA_LINE_SPACING_SIZE_PREF = "LineSpacingSize";
    private static final String DOA_FONT_TYPE_PREF = "DoaFontType";

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
        List<Font> list = new ArrayList<>();
        list.add(new Font("Normal", ""));
        list.add(new Font("A_Nefel_Botan", "fonts/A_Nefel_Botan.ttf"));
        list.add(new Font("PakType_Naqsh_2.2_volt", "fonts/PakType_Naqsh_2.2_volt.ttf"));
        list.add(new Font("Thabit", "fonts/Thabit.ttf"));
        list.add(new Font("Diwanltr", "fonts/Diwanltr.ttf"));
        list.add(new Font("Frsspbl", "fonts/Frsspbl.ttf"));

        return list;
    }
}
