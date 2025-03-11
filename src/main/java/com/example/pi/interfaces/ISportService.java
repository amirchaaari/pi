package com.example.pi.interfaces;

import com.example.pi.entity.Sport;
import java.util.List;

public interface ISportService {
    Sport createSport(Sport sport);
    Sport updateSport(Long id, Sport sport);
    void deleteSport(Long id);
    List<Sport> getAllSports();
    Sport getSportById(Long id);
}


