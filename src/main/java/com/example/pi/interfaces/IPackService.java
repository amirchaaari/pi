package com.example.pi.interfaces;

import com.example.pi.entity.Club;
import com.example.pi.entity.Pack;
import java.util.List;
import java.util.Map;

public interface IPackService {
    Pack createPack(Pack pack);
    Pack updatePack(Long id, Pack pack);
    void deletePack(Long id);
    List<Pack> getAllPacks();
    Pack getPackById(Long id);

    List<Pack> getPacksByPopularity() ;


    Club affecterPackToClub(Long clubId, Long packId);

    Map<String, Object> getPacksPopularityStatistics();


}

