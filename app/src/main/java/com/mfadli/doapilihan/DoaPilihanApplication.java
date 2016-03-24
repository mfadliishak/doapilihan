package com.mfadli.doapilihan;

import android.app.Application;

import com.mfadli.doapilihan.event.RxBus;

/**
 * Created by mfad on 24/03/2016.
 */
public class DoaPilihanApplication extends Application {
    private static final String LOG_TAG = DoaPilihanApplication.class.getSimpleName();
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
}
