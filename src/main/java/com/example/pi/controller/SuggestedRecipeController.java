package com.example.pi.controller;

import com.example.pi.entity.SuggestedRecipe;
import com.example.pi.repository.SuggestedRecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe/suggestedrecipes")
public class SuggestedRecipeController {

    @Autowired
     SuggestedRecipeRepo suggestedRecipeRepo;

    @GetMapping
    public List<SuggestedRecipe> getSuggestedRecipes() {
        return suggestedRecipeRepo.findAll();
}}
