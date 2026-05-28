package model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private String avatarPath;
    private List<Recipe> myRecipes;
    private List<Recipe> savedRecipes;

    public User(String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
        this.myRecipes = new ArrayList<>();
        this.savedRecipes = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe) {
        myRecipes.add(recipe);
    }

    public void saveRecipe(Recipe recipe) {
        if (!savedRecipes.contains(recipe)) {
            savedRecipes.add(recipe);
        }
    }

    public void unsaveRecipe(Recipe recipe) {
        savedRecipes.remove(recipe);
    }

    public boolean hasSaved(Recipe recipe) {
        return savedRecipes.contains(recipe);
    }

    public String getName()              { return name; }
    public String getAvatarPath()        { return avatarPath; }
    public List<Recipe> getMyRecipes()   { return myRecipes; }
    public List<Recipe> getSavedRecipes(){ return savedRecipes; }

    public void setName(String name)           { this.name = name; }
    public void setAvatarPath(String path)     { this.avatarPath = path; }
}
