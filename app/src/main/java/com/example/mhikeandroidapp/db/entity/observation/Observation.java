package com.example.mhikeandroidapp.db.entity.observation;

import com.example.mhikeandroidapp.db.entity.comment.Comment;
import com.example.mhikeandroidapp.db.entity.hike.Hike;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Observation {
    private int id;
    private String observation;
    private LocalDateTime time;
    private ArrayList<Comment> comments;
    private Hike hike;

    public Observation() {
    }

    public Observation(int id, String observation, LocalDateTime time, ArrayList<Comment> comments, Hike hike) {
        this.id = id;
        this.observation = observation;
        this.time = time;
        this.comments = comments;
        this.hike = hike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public Hike getHike() {
        return hike;
    }

    public void setHike(Hike hike) {
        this.hike = hike;
    }

    public final static String TBL_NAME = "tbl_observation";
    public final static String COL_ID = "id";
    public final static String COL_OBSERVATION = "observation";
    public final static String COL_TIME = "time";
    public final static String COL_HIKE_ID = "hike_id";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TBL_NAME + "(" + COL_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + COL_OBSERVATION + " TEXT NOT NULL,"
            + COL_TIME + " TEXT NOT NULL,"
            + COL_HIKE_ID + " INTEGER,"
            + " FOREIGN KEY(" + COL_HIKE_ID + ") REFERENCES " + Hike.TBL_NAME + "(" + Hike.COL_ID + ")"
            + ")";
}
