package com.example.pi.service;

import com.example.pi.entity.Nutritionist;

import java.util.List;

public interface INutritionnistService {
    List<Nutritionist> retrieveAllNutritionist();

    Nutritionist addNutritionist(Nutritionist e);

    Nutritionist updateNutritionist(Nutritionist e);

    Nutritionist retrieveNutritionist(Long idNutritionist);

    void removeNutritionist(Long idNutritionist);

    List<Nutritionist> Nutritionists(List<Nutritionist> Nutritionists);
}
