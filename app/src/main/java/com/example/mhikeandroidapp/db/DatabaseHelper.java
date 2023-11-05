package com.example.mhikeandroidapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mhikeandroidapp.db.entity.hike.Hike;
import com.example.mhikeandroidapp.db.entity.observation.Observation;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mhike_db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Hike.CREATE_TABLE);
        db.execSQL(Observation.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB UPGRADE", "RUN");
        db.execSQL("DROP TABLE IF EXISTS " + Hike.TBL_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Observation.TBL_NAME);
        onCreate(db);
    }
}
