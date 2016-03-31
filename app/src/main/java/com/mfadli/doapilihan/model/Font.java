package com.mfadli.doapilihan.model;

/**
 * Created by mfad on 31/03/2016.
 */
public class Font {
    private String mName;
    private String mPath;

    public Font(String name, String path) {
        mName = name;
        mPath = path;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }
}
