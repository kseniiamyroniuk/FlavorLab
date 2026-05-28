package model;

public class Ingredient {

    private String key;
    private String displayName;
    private String category;
    private FlavorProfile profile;
    private double spiciness;

    private double defaultVolumeMl;    // початковий обʼєм при додаванні
    private double caloriesPer100ml;   // ккал на 100 мл/г
    private double costPer100ml;       // ₴ на 100 мл/г


    public Ingredient(String key, String displayName, String category,
                      FlavorProfile profile,
                      double spiciness,
                      double defaultVolumeMl, double caloriesPer100ml, double costPer100ml) {
        this.key = key;
        this.displayName = displayName;
        this.category = category;
        this.profile = profile;
        this.spiciness = spiciness;
        this.defaultVolumeMl = defaultVolumeMl;
        this.caloriesPer100ml = caloriesPer100ml;
        this.costPer100ml = costPer100ml;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public FlavorProfile getProfile() {
        return profile;
    }

    public double getSpiciness() {
        return spiciness;
    }

    public double getDefaultVolumeMl() {
        return defaultVolumeMl;
    }


    // скільки ккал і ₴ для конкретного обʼєму
    public double calcCalories(double volumeMl) {
        return caloriesPer100ml * volumeMl / 100.0;
    }

    public double calcCost(double volumeMl) {
        return costPer100ml * volumeMl / 100.0;
    }
}