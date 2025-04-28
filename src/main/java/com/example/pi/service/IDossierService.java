package com.example.pi.service;

import com.example.pi.entity.Dossier;

import java.util.List;
import java.util.Map;

public interface IDossierService {
    List<Dossier> retrieveAllDossiers();

    Dossier addDossier(Dossier e);

    Dossier updateDossier(Dossier e);

    Dossier retrieveDossier (Long idDossier);

    void removeDossier(Long idDossier);

    List<Dossier> addDossiers (List<Dossier> Dossiers);
    //une fonc avan
    Dossier findDossierByPatientId(Long patientId); // recherche du dossier d'un patient spécifique

    Map<String, Object> getGenderStats();
    long getTotalMedicalFolders();

}
