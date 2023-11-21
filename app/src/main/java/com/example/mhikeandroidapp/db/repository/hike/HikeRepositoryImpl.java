package com.example.mhikeandroidapp.db.repository.hike;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mhikeandroidapp.db.DatabaseHelper;
import com.example.mhikeandroidapp.db.entity.hike.Hike;
import com.example.mhikeandroidapp.db.entity.hike.HikeDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HikeRepositoryImpl  implements HikeRepository {
    private DatabaseHelper databaseHelper;

    public HikeRepositoryImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void prepopulateHike() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (int i = 0; i<=10; i++) {
            ContentValues values = new ContentValues();
            values.put(Hike.COL_TITLE, "New Hike");
            values.put(Hike.COL_LOCATION, "Hanoi");
            values.put(Hike.COL_DATE, LocalDate.of(2023, 9,23).toString());
            values.put(Hike.COL_PARKING, true);
            values.put(Hike.COL_LENGTH, 20.0);
            values.put(Hike.COL_DIFFICULTY, 1);
            values.put(Hike.COL_DESCRIPTION, "Lorem Ipsum");
            db.insertOrThrow(Hike.TBL_NAME, Hike.COL_DESCRIPTION, values);
        }

    }

    @Override
    public List<Hike> getHikeList() {
        ArrayList<Hike> hikes = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] requestedCols = {
                Hike.COL_ID,
                Hike.COL_TITLE,
                Hike.COL_LOCATION,
                Hike.COL_PARKING,
                Hike.COL_DATE,
                Hike.COL_LENGTH,
                Hike.COL_DIFFICULTY,
                Hike.COL_DESCRIPTION
        };
        Cursor cursor = db.query(
                Hike.TBL_NAME,
                requestedCols,
                null,
                null,
                null,
                null,
                Hike.COL_ID + " DESC"
        );

        if (cursor.moveToFirst()){
            do{
                Hike newHike = new Hike();
                newHike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COL_ID)));
                newHike.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_TITLE)));
                newHike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_LOCATION)));
                // convert string to date
                newHike.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_DATE)), DateTimeFormatter.ofPattern("uuuu-MM-dd")));
                // return true/false value based on returned integer
                newHike.setParking(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COL_PARKING)) != 0);
                // Set precision
                newHike.setLength(cursor.getFloat(cursor.getColumnIndexOrThrow(Hike.COL_LENGTH)));
                // cast to byte
                newHike.setDifficulty((byte) cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COL_DIFFICULTY)));
                newHike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_DESCRIPTION)));
                hikes.add(newHike);
            }while(cursor.moveToNext());
        }
        return hikes;
    }

    @Override
    public Hike getSingleHike(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String[] requestedCols = {
                Hike.COL_ID,
                Hike.COL_TITLE,
                Hike.COL_LOCATION,
                Hike.COL_PARKING,
                Hike.COL_DATE,
                Hike.COL_LENGTH,
                Hike.COL_DIFFICULTY,
                Hike.COL_DESCRIPTION
        };

        Cursor cursor = db.query(
                Hike.TBL_NAME,
                requestedCols,
                Hike.COL_ID + "=?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null
        );

        Hike foundHike = new Hike();

        if (cursor.moveToFirst()) {
            foundHike.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COL_ID)));
            foundHike.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_TITLE)));
            foundHike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_LOCATION)));

            // convert string to date
            foundHike.setDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_DATE))));

            // return true/false value based on returned integer
            foundHike.setParking(cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COL_PARKING)) != 0);

            // Set precision
            foundHike.setLength(cursor.getFloat(cursor.getColumnIndexOrThrow(Hike.COL_LENGTH)));

            // cast to byte
            foundHike.setDifficulty((byte) cursor.getInt(cursor.getColumnIndexOrThrow(Hike.COL_DIFFICULTY)));

            foundHike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Hike.COL_DESCRIPTION)));
        }

        return foundHike;
    }

    @Override
    public int createHike(HikeDTO hikeDTO) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Hike.COL_TITLE, hikeDTO.getTitle());
        values.put(Hike.COL_LOCATION, hikeDTO.getLocation());
        values.put(Hike.COL_DATE, hikeDTO.getDate().toString());
        values.put(Hike.COL_PARKING, hikeDTO.isParking());
        values.put(Hike.COL_LENGTH, hikeDTO.getLength());
        values.put(Hike.COL_DIFFICULTY, hikeDTO.getDifficulty());
        values.put(Hike.COL_DESCRIPTION, hikeDTO.getDescription());

        return (int) db.insertOrThrow(Hike.TBL_NAME, Hike.COL_DESCRIPTION, values);
    }

    @Override
    public int updateHike(int hikeId,HikeDTO hikeDTO) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Hike.COL_TITLE, hikeDTO.getTitle());
        values.put(Hike.COL_LOCATION, hikeDTO.getLocation());
        values.put(Hike.COL_DATE, hikeDTO.getDate().toString());
        values.put(Hike.COL_PARKING, hikeDTO.isParking());
        values.put(Hike.COL_LENGTH, hikeDTO.getLength());
        values.put(Hike.COL_DIFFICULTY, hikeDTO.getDifficulty());
        values.put(Hike.COL_DESCRIPTION, hikeDTO.getDescription());

        return db.update(
                Hike.TBL_NAME,
                values,
                Hike.COL_ID + "=?",
                new String[] {String.valueOf(hikeId)}
        );
    }

    @Override
    public void deleteHike(int hikeId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(
                Hike.TBL_NAME,
                Hike.COL_ID + "=?",
                 new String[] {String.valueOf(hikeId)});
    }
}
