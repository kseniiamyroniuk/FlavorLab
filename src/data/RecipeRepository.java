package data;

import model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeRepository {
    private static final List<Recipe> RECIPES = new ArrayList<>();

    public static void add(Recipe recipe) {
        RECIPES.add(recipe);
    }

    public static void remove(Recipe recipe) {
        RECIPES.remove(recipe);
    }

    public static List<Recipe> getAll() {
        return new ArrayList<>(RECIPES);
    }

    public static List<Recipe> search(String query) {
        String q = query.toLowerCase();
        // беремо лист усіх рецептів, фільтруємо і повертаємо лист лише з тими, що відповідають пошуку
        return RECIPES.stream()
                .filter(r -> r.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

}