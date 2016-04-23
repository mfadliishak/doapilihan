package com.mfadli.doapilihan.model;

import android.support.annotation.DrawableRes;

/**
 * Created by mfad on 24/04/2016.
 */
public class BGPattern {
    private String mName;
    private int mDrawable;
    private int mDrawableDialog;

    public BGPattern(String name, @DrawableRes int drawable, @DrawableRes int drawableDialog) {
        mName = name;
        mDrawable = drawable;
        mDrawableDialog = drawableDialog;
    }

    public String getName() {
        return mName;
    }

    @DrawableRes
    public int getDrawable() {
        return mDrawable;
    }

    @DrawableRes
    public int getDrawableDialog() {
        return mDrawableDialog;
    }
}
