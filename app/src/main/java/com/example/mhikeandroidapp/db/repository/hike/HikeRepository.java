package com.example.mhikeandroidapp.db.repository.hike;

import com.example.mhikeandroidapp.db.entity.hike.Hike;
import com.example.mhikeandroidapp.db.entity.hike.HikeDTO;

import java.util.List;

public interface HikeRepository {
    void prepopulateHike();
    List<Hike> getHikeList();
    Hike getSingleHike(int hikeId);
    int createHike(HikeDTO hikeDTO);
    int updateHike(int hikeId, HikeDTO hikeDTO);
    void deleteHike(int hikeId);
}
