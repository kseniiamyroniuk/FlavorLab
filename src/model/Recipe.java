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
    private List<Review> reviews;
    private User author;

    public Recipe(String name, List<RecipeIngredient> ingredients,
                  String origin, String brewMethod, String temperature) {
        this.name = name;
        this.avatarPath = null;
        this.ingredients = ingredients;
        this.origin = origin;
        this.brewMethod = brewMethod;
        this.temperature = temperature;
        this.allergens = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.calories = 0;
        this.costUah = 0;
        this.author = null;
    }

    // відгуки
    public void addReview(Review review) {
        reviews.add(review);
    }

    // we calculate average rating for a Recipe and if there's no reviews we make it 0
    public double averageRating() {
        if (reviews.isEmpty()) return 0;
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    // алергени
    public void addAllergen(String allergen) {
        if (!allergens.contains(allergen)) {
            allergens.add(allergen);
        }
    }

    // getters
    public String getName()                        { return name; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public String getOrigin()                      { return origin; }
    public String getBrewMethod()                  { return brewMethod; }
    public String getTemperature()                 { return temperature; }
    public double getCalories()                    { return calories; }
    public double getCostUah()                     { return costUah; }
    public List<String> getAllergens()             { return allergens; }
    public List<Review> getReviews()               { return reviews; }
    public User getAuthor()                        { return author; }

    // setters
    public void setCalories(double calories)       { this.calories = calories; }
    public void setCostUah(double costUah)         { this.costUah = costUah; }
    public void setAuthor(User author)             { this.author = author; }
}