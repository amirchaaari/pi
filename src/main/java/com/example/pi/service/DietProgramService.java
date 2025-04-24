package com.example.pi.service;

import com.example.pi.entity.DietProgram;
import com.example.pi.repository.DietProgramRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            if (Diet.getIdDiet() == null) {
                throw new IllegalArgumentException("DietProgram ID is required for update.");
            }

            Optional<DietProgram> existingDiet = dietProgramRepo.findById(Diet.getIdDiet());
            if (existingDiet.isEmpty()) {
                throw new EntityNotFoundException("DietProgram with ID " + Diet.getIdDiet() + " not found.");
            }

            return dietProgramRepo.save(Diet);
        }

        @Override
        public DietProgram retrieveDietProgram(Long idDietProgram) {
            return dietProgramRepo.findById(idDietProgram).orElse(null);  // Retourne null si le programme n'est pas trouvé
        }

        @Override
        public void removeDietProgram(Long idDietProgram) {
            dietProgramRepo.deleteById(idDietProgram);
        }
         @Override
         public List<DietProgram> findByUserId(Long userId) {
            return dietProgramRepo.findByUserId(userId);
        }
        @Override
        public List<DietProgram> findByMultipleUserIds(List<Long> userIds) {
        return dietProgramRepo.findByUserIdIn(userIds);
    }
    @Override
    public String compareNutritionPrograms(Long programId1, Long programId2) {
        DietProgram program1 = retrieveDietProgram(programId1);
        DietProgram program2 = retrieveDietProgram(programId2);

        if (program1 == null || program2 == null) {
            return "Oops! One or both nutrition programs couldn't be found.";
        }

        int calories1 = program1.getCalories();
        int calories2 = program2.getCalories();

        if (calories1 == calories2) {
            return String.format("Both programs — %s and %s — pack the same punch with %d calories each!",
                    program1.getName(), program2.getName(), calories1);
        } else if (calories1 > calories2) {
            return String.format("Looking for more fuel? %s has %d calories — that's more than %s with its %d calories.",
                    program1.getName(), calories1, program2.getName(), calories2);
        } else {
            return String.format("Need a lighter option? %s comes in at %d calories, lower than %s with %d calories.",
                    program1.getName(), calories1, program2.getName(), calories2);
        }
    }


    @Override
        public List<DietProgram> addDietPrograms(List<DietProgram> DietPrograms) {
            return (List<DietProgram>) dietProgramRepo.saveAll(DietPrograms);
        }

    }

