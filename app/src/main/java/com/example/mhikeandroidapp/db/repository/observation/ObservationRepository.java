package com.example.mhikeandroidapp.db.repository.observation;

import com.example.mhikeandroidapp.db.entity.observation.Observation;
import com.example.mhikeandroidapp.db.entity.observation.ObservationDTO;

import java.util.List;

public interface ObservationRepository {
    List<Observation> getHikeObservations(int hikeId);
    Observation getSingleObservation(int observationId);
    int createObservation(ObservationDTO observationDTO);
    int updateObservation(int observationId, ObservationDTO observationDTO);
    void deleteObservation(int observationId);
}
