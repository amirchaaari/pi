package com.example.pi.service;

import com.example.pi.entity.Nutritionist;
import com.example.pi.repository.NutritionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NutritionnistService implements INutritionnistService{
    @Autowired
    NutritionistRepository nutritionistRepository ;
    @Override
    public List<Nutritionist> retrieveAllNutritionist() {
        return (List<Nutritionist>) nutritionistRepository.findAll();
    }

    @Override
    public Nutritionist addNutritionist(Nutritionist e) {
        return nutritionistRepository.save(e) ;
    }

    @Override
    public Nutritionist updateNutritionist(Nutritionist e) {
        return nutritionistRepository.save(e) ;
    }

    @Override
    public Nutritionist retrieveNutritionist(Long idNutritionist) {
        return nutritionistRepository.findById(idNutritionist).orElse(null);
    }


    @Override
    public void removeNutritionist(Long idNutritionist) {
        nutritionistRepository.deleteById(idNutritionist);

    }

    @Override
    public List<Nutritionist> Nutritionists(List<Nutritionist> Nutritionists) {
        return (List<Nutritionist>) nutritionistRepository.saveAll(Nutritionists);
    }
}
