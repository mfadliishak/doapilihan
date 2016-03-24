package com.mfadli.doapilihan.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mfadli.doapilihan.R;

/**
 * Created by mfad on 24/03/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar_FullScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.zoom_enter_in, R.anim.zoom_exit_out);

        Handler handler = new Handler();
        handler.postDelayed(
                () -> {
                    MainActivity.start(this);
                    overridePendingTransition(R.anim.zoom_enter_in, R.anim.zoom_exit_out);
                    finish();
                }, 2000);
    }
}
