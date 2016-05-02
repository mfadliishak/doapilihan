package com.mfadli.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mfadli.doapilihan.DoaPilihanApp;

/**
 * Created by mfad on 01/04/2016.
 * Class with static sending Screen and Event to Google Analytics.
 */
public class Analytic {
    static Tracker mTracker = ((DoaPilihanApp) DoaPilihanApp.getContext())
            .getTracker(DoaPilihanApp.TrackerName.APP_TRACKER);

    public static final String EVENT_BUTTON = "Button";
    public static final String EVENT_IAP = "IAP";
    public static final String EVENT_DOA = "Doa";

    public static void sendScreen(String name) {
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendEvent(String category, String action, String label) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public static void sendEvent(String category, String action, String label, long value) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

}
