package com.mfadli.doapilihan.data.model;

/**
 * Created by mfad on 27/03/2016.
 */

public class DoaData {
    public static final String TAG = DoaData.class.getSimpleName();
    public static final String TABLE = "DoaData";

    public static final String KEY_ID = "id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_TITLE = "title";
    public static final String KEY_REFERENCE = "reference";
    public static final String KEY_URL = "url";
    public static final String KEY_TRANSLATION = "translation";
    public static final String KEY_TRANSLATION_EN = "translation_en";
    public static final String KEY_DOA = "doa";

    private int mId;
    private int mType;
    private String mTitle;
    private String mDoa;
    private String mReference;
    private String mTranslation;
    private String mTranslationEn;
    private String mUrl;

    public void setId(int id) {
        mId = id;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDoa(String doa) {
        mDoa = doa;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
    }

    public void setTranslationEn(String translationEn) {
        mTranslationEn = translationEn;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public int getId() {
        return mId;
    }

    public int getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDoa() {
        return mDoa;
    }

    public String getReference() {
        return mReference;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public String getTranslationEn() {
        return mTranslationEn;
    }

    public String getUrl() {
        return mUrl;
    }
}
