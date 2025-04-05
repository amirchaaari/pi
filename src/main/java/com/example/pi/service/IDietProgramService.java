package com.example.pi.service;

import com.example.pi.entity.DietProgram;

import java.util.List;

public interface IDietProgramService {
    List<DietProgram> retrieveAllDietPrograms();

    DietProgram addDietProgram(DietProgram Diet);

    DietProgram updateDietProgram(DietProgram Diet);

    DietProgram retrieveDietProgram(Long idDietProgram);

    void removeDietProgram(Long idDietProgram);

    List<DietProgram> addDietPrograms(List<DietProgram> DietPrograms);
}
