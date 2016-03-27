package com.mfadli.doapilihan.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.mfadli.doapilihan.DoaPilihanApplication;
import com.mfadli.doapilihan.data.repo.DoaDataRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mfad on 27/03/2016.
 * <p>
 * Helper class to manage database creation, migrations and version management.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String LOG_TAG = DBHelper.class.getSimpleName();
    // Version number to upgrade database version
    // each time if Add/Edit table, need
    // to change the version number.
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "doa.db";
    // Migration Folder
    private static final String MIGRATION_FOLDER = "migration/";

    public DBHelper() {
        super(DoaPilihanApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // All necessary tables will be created here
        db.execSQL(DoaDataRepo.createTable());

        // Call onUpgrade manually for first time install app when
        // the version is bigger than 1.
        if (DATABASE_VERSION > 1) {
            onUpgrade(db, 1, DATABASE_VERSION);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                Log.d(LOG_TAG, "Looking for migration file: " + migrationName);
                readAndExecuteSQLScript(db, DoaPilihanApplication.getContext(), migrationName);
            }
        } catch (Exception exception) {
            Log.e(LOG_TAG, "Exception running upgrade script:", exception);
        }

    }

    /**
     * Read sql file from assets folder.
     * Specify filename e.g. from_1_to_2.sql (from_<oldVersion>_to_<newVersion>.sql).
     *
     * @param db       {@link SQLiteDatabase}
     * @param context  {@link Context}
     * @param fileName String
     */
    private void readAndExecuteSQLScript(SQLiteDatabase db, Context context, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(LOG_TAG, "SQL script file name is empty");
            return;
        }

        Log.d(LOG_TAG, "Script found. Executing...");
        AssetManager assetManager = context.getAssets();
        BufferedReader reader = null;

        try {
            InputStream inputStream = assetManager.open(MIGRATION_FOLDER + fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "IOException:", e);
                }
            }
        }

    }

    /**
     * Execute SQL per line. SQL statement must be terminated by semicolon each line.
     *
     * @param db     {@link SQLiteDatabase}
     * @param reader {@link BufferedReader}
     * @throws IOException
     */
    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        db.beginTransaction();
        try {
            while ((line = reader.readLine()) != null) {
                statement.append(line);
                statement.append("\n");
                if (line.endsWith(";")) {
                    db.execSQL(statement.toString());
                    statement = new StringBuilder();
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            db.endTransaction();
            throw e;
        }

        db.endTransaction();
    }
}
