package model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String name;
    private String avatarPath;
    private List<RecipeIngredient> ingredients;
    private String origin;
    private String brewMethod;
    private String temperature;

    private double calories;
    private double costUah;
    private List<String> allergens;
    private double rating;
    private int ratingCount; // щоб можна були вираховувати середнє значення

    public Recipe(String name, List<RecipeIngredient> ingredients,
                  String origin, String brewMethod, String temperature) {
        this.name = name;
        this.avatarPath = null;
        this.ingredients = ingredients;
        this.origin = origin;
        this.brewMethod = brewMethod;
        this.temperature = temperature;
        this.allergens = new ArrayList<>();
        this.rating = 0.0;
        this.ratingCount = 0;
        this.calories = 0;
        this.costUah = 0;
    }


    // алергени
    public void addAllergen(String allergen) {
        if (!allergens.contains(allergen)) {
            allergens.add(allergen);
        }
    }

    // гетери
    public String getName() {
        return name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public String getOrigin() {
        return origin;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public String getTemperature() {
        return temperature;
    }

    public double getCalories() {
        return calories;
    }

    public double getCostUah() {
        return costUah;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public double getRating() {
        if (rating < 0 || rating > 5) {
            return 0.0;
        }
        return rating;
    }

    public  int getRatingCount() {
        return ratingCount;
    }

    // сетери
    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setCostUah(double costUah) {
        this.costUah = costUah;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}