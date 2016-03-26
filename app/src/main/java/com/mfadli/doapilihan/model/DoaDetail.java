package com.mfadli.doapilihan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mfad on 26/03/2016.
 */
public class DoaDetail implements Parcelable {
    private String title;
    private String doa;
    private String reference;
    private String translation;
    private String translationEn;
    private String url;

    public DoaDetail(String title, String doa, String reference, String translation, String translationEn, String url) {
        this.title = title;
        this.doa = doa;
        this.reference = reference;
        this.translation = translation;
        this.translationEn = translationEn;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDoa() {
        return doa;
    }

    public String getReference() {
        return reference;
    }

    public String getTranslation() {
        return translation;
    }

    public String getTranslationEn() {
        return translationEn;
    }

    public String getUrl() {
        return url;
    }

    protected DoaDetail(Parcel in) {
        title = in.readString();
        doa = in.readString();
        reference = in.readString();
        translation = in.readString();
        translationEn = in.readString();
        url = in.readString();
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
        dest.writeString(title);
        dest.writeString(doa);
        dest.writeString(reference);
        dest.writeString(translation);
        dest.writeString(translationEn);
        dest.writeString(url);
    }
}
