package com.example.pi.service;

import com.example.pi.entity.Dossier;
import com.example.pi.repository.DossierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> getGenderStats() {
        List<Object[]> results = dossierRepository.countByGender();
        Map<String, Object> stats = new HashMap<>();

        // Compter le nombre total de dossiers
        long total = results.stream().mapToLong(row -> (Long) row[1]).sum();

        // Transformation des résultats en Map<String, Object> avec les pourcentages
        for (Object[] row : results) {
            String gender = (String) row[0];
            Long count = (Long) row[1];
            double percentage = (total == 0) ? 0 : (count * 100.0) / total; // Calcul du pourcentage
            stats.put(gender.toLowerCase(), Map.of("count", count, "percentage", percentage));
        }

        return stats;
    }
    public long getTotalMedicalFolders() {
        return dossierRepository.count();
    }


}
