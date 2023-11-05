package com.example.mhikeandroidapp.db.entity.hike;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class Hike implements Parcelable {
    private int id;
    private String title;
    private String location;
    private LocalDate date;
    private boolean parking;
    private float length;
    private byte difficulty;
    private String description;


    public Hike() { }

    public Hike(int id, String title, String location, LocalDate date, boolean parking, float length, byte difficulty, String description) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
    }

    protected Hike(Parcel in) {
        id = in.readInt();
        title = in.readString();
        location = in.readString();
        parking = in.readByte() != 0;
        length = in.readFloat();
        difficulty = in.readByte();
        description = in.readString();
    }

    public static final Creator<Hike> CREATOR = new Creator<Hike>() {
        @Override
        public Hike createFromParcel(Parcel in) {
            return new Hike(in);
        }

        @Override
        public Hike[] newArray(int size) {
            return new Hike[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public byte getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public final static String TBL_NAME = "tbl_hike";
    public final static String COL_ID = "id";
    public final static String COL_TITLE = "title";
    public final static String COL_LOCATION = "location";
    public final static String COL_DATE = "date";
    public final static String COL_PARKING = "parking";
    public final static String COL_LENGTH = "length";
    public final static String COL_DIFFICULTY = "difficulty";
    public final static String COL_DESCRIPTION = "description";

    public static final String CREATE_TABLE = "CREATE TABLE " + TBL_NAME + "("
            + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + COL_TITLE + " TEXT NOT NULL,"
            + COL_LOCATION + " TEXT NOT NULL,"
            + COL_DATE + " TEXT NOT NULL,"
            + COL_PARKING + " BOOLEAN NOT NULL,"
            + COL_LENGTH + " REAL NOT NULL,"
            + COL_DIFFICULTY + " INTEGER NOT NULL,"
            + COL_DESCRIPTION + " TEXT"
            + ")";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(location);
        dest.writeByte((byte) (parking ? 1 : 0));
        dest.writeFloat(length);
        dest.writeByte(difficulty);
        dest.writeString(description);
    }
}
