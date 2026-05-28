package model;

public class RecipeIngredient {
    private Ingredient ingredient;
    private double volumeMl;

    public RecipeIngredient(Ingredient ingredient, double volumeMl) {
        this.ingredient = ingredient;
        this.volumeMl = volumeMl;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getVolumeMl() {
        return volumeMl;
    }
}