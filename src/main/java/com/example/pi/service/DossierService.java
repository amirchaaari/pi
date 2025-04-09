package com.example.pi.service;

import com.example.pi.entity.Dossier;
import com.example.pi.entity.Meeting;
import com.example.pi.repository.DossierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class DossierService implements IDossierService{

    DossierRepository dossierRepository ;
    @Override
    public List<Dossier> retrieveAllDossiers() {
        return (List<Dossier>) dossierRepository.findAll();
    }

    @Override
    public Dossier addDossier(Dossier e) {
        return dossierRepository.save(e) ;
    }

    @Override
    public Dossier updateDossier(Dossier e) {
        return dossierRepository.save(e) ;
    }

    @Override
    public Dossier retrieveDossier(Long idDossier) {
        return dossierRepository.findById(idDossier).get() ;
    }

    @Override
    public void removeDossier(Long idDossier) {
        dossierRepository.deleteById(idDossier);
    }

    @Override
    public List<Dossier> addDossiers(List<Dossier> Dossiers) {
        return (List<Dossier>) dossierRepository.saveAll(Dossiers);
    }

    @Override
    public Dossier findDossierByPatientId(Long patientId) {
        return null;
    }
}
