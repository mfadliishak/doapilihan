package com.mfadli.doapilihan.data.repo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mfadli.doapilihan.data.DBManager;
import com.mfadli.doapilihan.data.model.DoaData;
import com.mfadli.doapilihan.model.DoaDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mfad on 27/03/2016.
 */
public class DoaDataRepo {

    private static final String LOG_TAG = DoaDataRepo.class.getSimpleName();

    public DoaDataRepo() {

    }

    public static String createTable() {
        return "CREATE TABLE " + DoaData.TABLE + " ( " +
                DoaData.KEY_ID + " PRIMARY KEY ," +
                DoaData.KEY_TYPE + " TEXT ," +
                DoaData.KEY_TITLE + " TEXT 	," +
                DoaData.KEY_REFERENCE + " TEXT 	," +
                DoaData.KEY_URL + " TEXT 	," +
                DoaData.KEY_TRANSLATION + " TEXT 	," +
                DoaData.KEY_TRANSLATION_EN + " TEXT 	," +
                DoaData.KEY_DOA + " TEXT )";
    }

    public List<DoaDetail> getAllDoa() {
        List<DoaDetail> doaDetails = new ArrayList<>();

        SQLiteDatabase db = DBManager.getInstance().openDatabase();
        String selectQuery = String.format("SELECT * FROM %s", DoaData.TABLE);

        Log.d(LOG_TAG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DoaDetail doa = new DoaDetail();
                doa.setId((cursor.getInt(cursor.getColumnIndex(DoaData.KEY_ID))));
                doa.setTitle(cursor.getString(cursor.getColumnIndex(DoaData.KEY_TITLE)));
                doa.setType((cursor.getInt(cursor.getColumnIndex(DoaData.KEY_TYPE))));
                doa.setReference(cursor.getString(cursor.getColumnIndex(DoaData.KEY_REFERENCE)));
                doa.setUrl(cursor.getString(cursor.getColumnIndex(DoaData.KEY_URL)));
                doa.setTranslation(cursor.getString(cursor.getColumnIndex(DoaData.KEY_TRANSLATION)));
                doa.setTranslationEn(cursor.getString(cursor.getColumnIndex(DoaData.KEY_TRANSLATION_EN)));
                doa.setDoa(cursor.getString(cursor.getColumnIndex(DoaData.KEY_DOA)));

                doaDetails.add(doa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DBManager.getInstance().closeDatabase();

        return doaDetails;
    }
}
