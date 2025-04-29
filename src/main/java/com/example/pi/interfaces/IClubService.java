package com.example.pi.interfaces;
import com.example.pi.entity.Club;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IClubService {
    Club createClub(Club club);
    void deleteClub(Long id);
    List<Club> getAllClubs();
    Club getClubById(Long id);
    Club updateClub(Long id, Club updatedClub, MultipartFile imageFile);
    byte[] getClubImage(Long id);


    Club affecterSportToClub(Long clubId, Long sportId);
}

