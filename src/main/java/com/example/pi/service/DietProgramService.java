package com.example.pi.service;

import com.example.pi.entity.DietProgram;
import com.example.pi.repository.DietProgramRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DietProgramService implements IDietProgramService {
    @Autowired
        DietProgramRepo dietProgramRepo;

        @Override
        public List<DietProgram> retrieveAllDietPrograms() {
            return (List<DietProgram>) dietProgramRepo.findAll();
        }

        @Override
        public DietProgram addDietProgram(DietProgram Diet) {
            return dietProgramRepo.save(Diet);
        }

        @Override
        public DietProgram updateDietProgram(DietProgram Diet) {
            return dietProgramRepo.save(Diet);
        }

        @Override
        public DietProgram retrieveDietProgram(Long idDietProgram) {
            return dietProgramRepo.findById(idDietProgram).orElse(null);  // Retourne null si le programme n'est pas trouvé
        }

        @Override
        public void removeDietProgram(Long idDietProgram) {

        }


    public double compareNutritionPrograms(Long programId1, Long programId2) {
        DietProgram program1 = retrieveDietProgram(programId1);
        DietProgram program2 = retrieveDietProgram(programId2);
        // Compare les calories pour l'exemple, mais tu peux comparer d'autres attributs
        return program1.getCalories() - program2.getCalories();
    }

        @Override
        public List<DietProgram> addDietPrograms(List<DietProgram> DietPrograms) {
            return (List<DietProgram>) dietProgramRepo.saveAll(DietPrograms);
        }
    }

