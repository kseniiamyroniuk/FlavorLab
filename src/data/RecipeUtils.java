package data;

import model.Ingredient;
import model.Recipe;
import model.RecipeIngredient;

public class RecipeUtils {

    private static final double MARKUP = 3.5;

    // Головний метод — викликати після створення рецепту
    public static void finalize(Recipe recipe) {
        calcNutrition(recipe);
        detectAllergens(recipe);
    }

    // Калорії та ціна
    public static void calcNutrition(Recipe recipe) {
        double totalCal  = 0;
        double totalCost = 0;

        for (RecipeIngredient ri : recipe.getIngredients()) {
            Ingredient ing = ri.getIngredient();
            if (ing == null) continue;
            totalCal  += ing.calcCalories(ri.getVolumeMl());
            totalCost += ing.calcCost(ri.getVolumeMl());
        }

        recipe.setCalories(Math.round(totalCal));
        recipe.setCostUah(Math.round(totalCost * MARKUP));
    }

    // Алергени

    public static void detectAllergens(Recipe recipe) {
        for (RecipeIngredient ri : recipe.getIngredients()) {
            Ingredient ing = ri.getIngredient();
            if (ing == null) continue;
            addAllergens(recipe, ing);
        }
    }

    private static void addAllergens(Recipe recipe, Ingredient ing) {
        String key      = ing.getKey();
        String category = ing.getCategory();

        switch (category) {
            case "milk" -> {
                if (key.equals("соєве_молоко")) {
                    recipe.addAllergen("соя");
                } else if (key.equals("мигдальне_молоко")  ||
                        key.equals("фундукове_молоко")   ||
                        key.equals("макадамієве_молоко") ||
                        key.equals("фісташкове_молоко")) {
                    recipe.addAllergen("горіхи");
                } else if (!key.equals("кокосове_молоко") &&
                        !key.equals("кокосові_вершки")  &&
                        !key.equals("рисове_молоко")) {
                    recipe.addAllergen("молоко");
                }
            }
            case "nuts" -> {
                if (key.equals("кунжут") || key.equals("насіння_чіа")) {
                    recipe.addAllergen("кунжут");
                } else {
                    recipe.addAllergen("горіхи");
                }
            }
            case "syrup" -> {
                if (key.equals("фундук")  ||
                        key.equals("мигдаль") ||
                        key.equals("фісташка")||
                        key.equals("арахіс")) {
                    recipe.addAllergen("горіхи");
                }
                if (key.equals("тахіні")) {
                    recipe.addAllergen("кунжут");
                }
            }
            case "foam" -> recipe.addAllergen("молоко");
        }

        if (key.equals("імбирне_печиво") || key.equals("медовий_пряник")) {
            recipe.addAllergen("глютен");
        }
    }

    public static double marketPrice(Recipe recipe) {
        double totalCost = 0;
        for (RecipeIngredient ri : recipe.getIngredients()) {
            Ingredient ing = ri.getIngredient();
            if (ing == null) continue;
            totalCost += ing.calcCost(ri.getVolumeMl());
        }
        return totalCost * MARKUP;
    }

    public static double totalCalories(Recipe recipe) {
        double totalCal = 0;
        for (RecipeIngredient ri : recipe.getIngredients()) {
            Ingredient ing = ri.getIngredient();
            if (ing == null) continue;
            totalCal += ing.calcCalories(ri.getVolumeMl());
        }
        return totalCal;
    }
}