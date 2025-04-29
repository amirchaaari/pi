package com.example.pi.service;

import com.example.pi.dto.DietProgramRequest;
import com.example.pi.entity.DietProgram;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.DietProgramRepo;
import com.example.pi.repository.UserInfoRepository;
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
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Override
    public List<DietProgram> retrieveAllDietPrograms() {
        return (List<DietProgram>) dietProgramRepo.findAll();
    }


    @Override
    public DietProgram addDietProgram(DietProgramRequest dietProgramRequest) {

        DietProgram dietProgram = convertToEntity(dietProgramRequest);
        return dietProgramRepo.save(dietProgram);
    }



    @Override
    public DietProgram updateDietProgram(DietProgramRequest dietProgramRequest) {
        if (dietProgramRequest.getIdDiet() == null) {
            throw new IllegalArgumentException("DietProgram ID is required for update.");
        }

        Optional<DietProgram> existingDiet = dietProgramRepo.findById(dietProgramRequest.getIdDiet());
        if (existingDiet.isEmpty()) {
            throw new EntityNotFoundException("DietProgram with ID " + dietProgramRequest.getIdDiet() + " not found.");
        }

        DietProgram dietProgram = convertToEntity(dietProgramRequest);
        return dietProgramRepo.save(dietProgram);
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
    public List<DietProgram> addDietPrograms(List<DietProgramRequest> dietProgramRequests) {
        List<DietProgram> dietPrograms = dietProgramRequests.stream()
                .map(this::convertToEntity)
                .toList();
        return (List<DietProgram>) dietProgramRepo.saveAll(dietPrograms);
    }

    // Méthode de conversion de DietProgramRequest vers DietProgram
    private DietProgram convertToEntity(DietProgramRequest dietProgramRequest) {
        DietProgram dietProgram = new DietProgram();
        dietProgram.setIdDiet(dietProgramRequest.getIdDiet());
        dietProgram.setName(dietProgramRequest.getName());
        dietProgram.setDescription(dietProgramRequest.getDescription());
        dietProgram.setCalories(dietProgramRequest.getCalories());
        dietProgram.setDuration(dietProgramRequest.getDuration());
        dietProgram.setTargetGoal(dietProgramRequest.getTargetGoal());

        if (dietProgramRequest.getUserEmail() != null || dietProgramRequest.getUserUsername() != null) {
            Optional<UserInfo> userOptional = Optional.empty();

            if (dietProgramRequest.getUserEmail() != null) {
                userOptional = userInfoRepository.findByEmail(dietProgramRequest.getUserEmail());
            }
            if (userOptional.isEmpty() && dietProgramRequest.getUserUsername() != null) {
                userOptional = userInfoRepository.findByName(dietProgramRequest.getUserUsername());
            }

            UserInfo user = userOptional.orElseThrow(() ->
                    new RuntimeException("User not found with provided email or name")
            );

            dietProgram.setUser(user);
        }

        return dietProgram;
    }



    // Méthode de conversion de DietProgram vers DietProgramRequest
    private DietProgramRequest convertToDTO(DietProgram dietProgram) {
        DietProgramRequest dietProgramRequest = new DietProgramRequest();
        dietProgramRequest.setIdDiet(dietProgram.getIdDiet());
        dietProgramRequest.setName(dietProgram.getName());
        dietProgramRequest.setDescription(dietProgram.getDescription());
        dietProgramRequest.setCalories(dietProgram.getCalories());
        dietProgramRequest.setDuration(dietProgram.getDuration());
        dietProgramRequest.setTargetGoal(dietProgram.getTargetGoal());
        dietProgramRequest.setCreationDate(dietProgram.getCreationDate());
        if (dietProgram.getUser() != null) {
            dietProgramRequest.setUserUsername(dietProgram.getUser().getName());
            dietProgramRequest.setUserEmail(dietProgram.getUser().getEmail());
        }
        return dietProgramRequest;
    }}

