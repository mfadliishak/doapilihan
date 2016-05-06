package com.mfadli.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.LruCache;

import com.mfadli.doapilihan.DoaPilihanApp;

/**
 * Created by mfad on 06/05/2016.
 * <p>
 * Helper to Cache Bitmap using LruCache to manage the memory used for displaying the Bitmap.
 * <p>
 * (http://stackoverflow.com/a/26595206)
 */
public class BitmapCacher {
    private static BitmapCacher instance;
    private LruCache<Object, Object> mLruCache;

    private BitmapCacher() {
        mLruCache = new LruCache<>(1024);
    }

    public static BitmapCacher getInstance() {
        if (instance == null) {
            instance = new BitmapCacher();
        }
        return instance;
    }

    public LruCache<Object, Object> getLruCache() {
        return mLruCache;
    }

    public static void cacheBitmap(@DrawableRes int drawable, String key) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeResource(DoaPilihanApp.getContext().getResources(), drawable, options);
        BitmapCacher.getInstance().getLruCache().put(key, bitmap);
    }

    public static Bitmap getCacheBitmap(String key) {
        return (Bitmap) BitmapCacher.getInstance().getLruCache().get(key);
    }
}
