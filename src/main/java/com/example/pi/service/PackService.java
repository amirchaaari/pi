package com.example.pi.service;

import com.example.pi.entity.Pack;
import com.example.pi.interfaces.IPackService;
import com.example.pi.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PackService implements IPackService {

    @Autowired
    private PackRepository packRepository;

    @Override
    public Pack createPack(Pack pack) {
        return packRepository.save(pack);
    }

    @Override
    public Pack updatePack(Long id, Pack pack) {
        Optional<Pack> existingPack = packRepository.findById(id);
        if (existingPack.isPresent()) {
            Pack updatedPack = existingPack.get();
            updatedPack.setName(pack.getName());
            updatedPack.setPrice(pack.getPrice());
            updatedPack.setDuration(pack.getDuration());
            return packRepository.save(updatedPack);
        }
        return null;
    }

    @Override
    public void deletePack(Long id) {
        packRepository.deleteById(id);
    }

    @Override
    public Pack getPackById(Long id) {
        return packRepository.findById(id).orElse(null);
    }

    @Override
    public List<Pack> getAllPacks() {
        return packRepository.findAll();
    }
}
