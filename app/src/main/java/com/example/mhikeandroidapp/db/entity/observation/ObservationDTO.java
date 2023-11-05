package com.example.mhikeandroidapp.db.entity.observation;


import java.time.LocalDateTime;

public class ObservationDTO {
    private String observation;
    private LocalDateTime time;
    private int hikeId;

    public ObservationDTO() {
    }

    public ObservationDTO(String observation, int hikeId) {
        this.observation = observation;
        this.hikeId = hikeId;
    }

    public ObservationDTO(String observation, LocalDateTime time, int hikeId) {
        this.observation = observation;
        this.time = time;
        this.hikeId = hikeId;
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

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }
}
