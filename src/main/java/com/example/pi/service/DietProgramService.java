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
            return List.of();
        }

        @Override
        public DietProgram addDietProgram(DietProgram Diet) {
            return null;
        }

        @Override
        public DietProgram updateDietProgram(DietProgram Diet) {
            return null;
        }

        @Override
        public DietProgram retrieveDietProgram(Long idDietProgram) {
            return null;
        }

        @Override
        public void removeDietProgram(Long idDietProgram) {

        }

        public double compareNutritionPrograms(Long programId1, Long programId2) {
            return 0;
        }

        @Override
        public List<DietProgram> addDietPrograms(List<DietProgram> DietPrograms) {
            return List.of();
        }
    }

