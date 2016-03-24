package com.mfadli.doapilihan;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mfadli.doapilihan.event.RxBus;

/**
 * Created by mfad on 24/03/2016.
 */
public class DoaPilihanApplication extends Application {
    private static final String LOG_TAG = DoaPilihanApplication.class.getSimpleName();
    private static final String DOA_FONT_SIZE_PREF = "DoaFontSize";
    private RxBus mRxBus = null;

    @Override
    public void onCreate() {
        super.onCreate();
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
}
