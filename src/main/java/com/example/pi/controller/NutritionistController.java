package com.example.pi.controller;


import com.example.pi.entity.Meeting;
import com.example.pi.entity.Nutritionist;
import com.example.pi.service.IMeetingService;
import com.example.pi.service.INutritionnistService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/nutritionist")

public class NutritionistController {
    INutritionnistService nutritionistService;

    @GetMapping("/retrieve-all-nutritionist")
    public List<Nutritionist> getNutritionist() {
        return nutritionistService.retrieveAllNutritionist();
    }



    @GetMapping("/retrieve-nutritionist/{nutritionist-id}")
    public Nutritionist retrieveNutritionist(@PathVariable("nutritionist-id") Long nutritionistId) {
        return nutritionistService.retrieveNutritionist(nutritionistId);
    }



    @PostMapping("/add-nutritionist")
    public Nutritionist addNutritionist(@RequestBody Nutritionist c) {
        Nutritionist nutritionist = nutritionistService.addNutritionist(c);
        return nutritionist;
    }

    @DeleteMapping("/remove-nutritionist/{nutritionist-id}")
    public void removeNutritionist(@PathVariable("nutritionist-id") Long nutritionistId) {
        nutritionistService.removeNutritionist(nutritionistId);
    }

    @PutMapping("/update-nutritionist")
    public Nutritionist updateNutritionist(@RequestBody Nutritionist c) {
        Nutritionist nutritionist= nutritionistService.updateNutritionist(c);
        return nutritionist;
    }


}
