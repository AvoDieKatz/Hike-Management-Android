package com.example.mhikeandroidapp.db.repository.observation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mhikeandroidapp.db.DatabaseHelper;
import com.example.mhikeandroidapp.db.entity.observation.Observation;
import com.example.mhikeandroidapp.db.entity.observation.ObservationDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ObservationRepositoryImpl implements ObservationRepository {
    private DatabaseHelper databaseHelper;

    public ObservationRepositoryImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<Observation> getHikeObservations(int hikeId) {
        ArrayList<Observation> observations = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String[] requestedCols = {
                Observation.COL_ID,
                Observation.COL_OBSERVATION,
                Observation.COL_TIME
        };

        Cursor cursor = db.query(
                Observation.TBL_NAME,
                requestedCols,
                Observation.COL_HIKE_ID + "=?",
                new String[] {String.valueOf(hikeId)},
                null,
                null,
                Observation.COL_TIME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Observation newObservation = new Observation();
                newObservation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COL_ID)));
                newObservation.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COL_OBSERVATION)));
                newObservation.setTime(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COL_TIME))));
                observations.add(newObservation);
            } while (cursor.moveToNext());
        }

        return observations;
    }

    @Override
    public Observation getSingleObservation(int observationId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] requestedCols = {
                Observation.COL_ID,
                Observation.COL_OBSERVATION,
                Observation.COL_TIME
        };

        Cursor cursor = db.query(
                Observation.TBL_NAME,
                requestedCols,
                Observation.COL_ID + "=?",
                new String[] {String.valueOf(observationId)},
                null,
                null,
                null
        );

        Observation observation = new Observation();
        if (cursor.moveToFirst()) {
            observation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Observation.COL_ID)));
            observation.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COL_OBSERVATION)));
            observation.setTime(LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(Observation.COL_TIME))));
        }
        return observation;
    }

    @Override
    public int createObservation(ObservationDTO observationDTO) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC+7:00"));

        ContentValues values = new ContentValues();
        values.put(Observation.COL_OBSERVATION, observationDTO.getObservation());
        values.put(Observation.COL_TIME, df.format(new Date()));
        values.put(Observation.COL_HIKE_ID, observationDTO.getHikeId());

        return (int) db.insertOrThrow(Observation.TBL_NAME, null, values);
    }

    @Override
    public int updateObservation(int observationId, ObservationDTO observationDTO) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Observation.COL_OBSERVATION, observationDTO.getObservation());
        values.put(Observation.COL_HIKE_ID, observationDTO.getHikeId());

        return db.update(Observation.TBL_NAME, values, Observation.COL_ID + "=?", new String[] {String.valueOf(observationId)});
    }

    @Override
    public void deleteObservation(int observationId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(Observation.TBL_NAME,
                Observation.COL_ID + "=?",
                new String[] {String.valueOf(observationId)});
    }
}
