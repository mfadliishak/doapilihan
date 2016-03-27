package com.mfadli.doapilihan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mfad on 26/03/2016.
 */
public class DoaDetail implements Parcelable {
    private String mTitle;
    private String mDoa;
    private String mReference;
    private String mTranslation;
    private String mTranslationEn;
    private String mUrl;

    public DoaDetail() {

    }

    public DoaDetail(String title, String doa, String reference, String translation, String translationEn, String url) {
        this.mTitle = title;
        this.mDoa = doa;
        this.mReference = reference;
        this.mTranslation = translation;
        this.mTranslationEn = translationEn;
        this.mUrl = url;
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
        mUrl = url;
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

    protected DoaDetail(Parcel in) {
        mTitle = in.readString();
        mDoa = in.readString();
        mReference = in.readString();
        mTranslation = in.readString();
        mTranslationEn = in.readString();
        mUrl = in.readString();
    }

    public static final Creator<DoaDetail> CREATOR = new Creator<DoaDetail>() {
        @Override
        public DoaDetail createFromParcel(Parcel in) {
            return new DoaDetail(in);
        }

        @Override
        public DoaDetail[] newArray(int size) {
            return new DoaDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDoa);
        dest.writeString(mReference);
        dest.writeString(mTranslation);
        dest.writeString(mTranslationEn);
        dest.writeString(mUrl);
    }
}
