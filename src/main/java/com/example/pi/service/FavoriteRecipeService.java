package com.example.pi.service;

import com.example.pi.entity.FavoriteRecipe;
import com.example.pi.entity.Recipe;
import com.example.pi.entity.UserInfo;
import com.example.pi.repository.FavoriteRecipeRepo;
import com.example.pi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteRecipeService {
    @Autowired
    private FavoriteRecipeRepo favoriteRecipeRepo;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public List<FavoriteRecipe> getFavoritesByUser(int id) {
        return favoriteRecipeRepo.findByUserId((long) id);
    }

    public FavoriteRecipe addFavorite(String email, Long recipeId) {
        if (favoriteRecipeRepo.existsByUserEmailAndRecipeIdRecipe(email, recipeId)) {
            throw new RuntimeException("Already in favorites");
        }
        Optional<UserInfo> userInfo = this.userInfoRepository.findByEmail(email);
        FavoriteRecipe favorite = new FavoriteRecipe();
        Recipe recipe = recipeService.findRecipeById(recipeId).orElse(null);
        favorite.setUser(userInfo.orElse(null));

        favorite.setRecipe(recipe);

        return favoriteRecipeRepo.save(favorite);
    }

    public void removeFavorite(Long id) {
        favoriteRecipeRepo.deleteById(id);
    }
}
