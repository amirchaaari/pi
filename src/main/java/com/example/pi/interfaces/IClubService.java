package com.example.pi.interfaces;
import com.example.pi.entity.Club;
import java.util.List;

public interface IClubService {
    Club createClub(Club club);
    Club updateClub(Long id, Club club);
    void deleteClub(Long id);
    List<Club> getAllClubs();
    Club getClubById(Long id);

    Club affecterSportToClub(Long clubId, Long sportId);
}

