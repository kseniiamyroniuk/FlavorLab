package model;

public class FlavorProfile {

    private double acidity;
    private double sweetness;
    private double bitterness;
    private double fruitiness;
    private double creaminess;

    private boolean spicyAccent;

    public FlavorProfile(double acidity, double sweetness, double bitterness, double fruitiness, double creaminess, boolean spicyAccent) {
        this.acidity = acidity;
        this.sweetness = sweetness;
        this.bitterness = bitterness;
        this.fruitiness = fruitiness;
        this.creaminess = creaminess;
        this.spicyAccent = spicyAccent;
    }

    public double getAcidity() {
        return acidity;
    }

    public double getSweetness() {
        return sweetness;
    }

    public double getBitterness() {
        return bitterness;
    }

    public double getFruitiness() {
        return fruitiness;
    }

    public double getCreaminess() {
        return creaminess;
    }

    public boolean isSpicyAccent() {
        return spicyAccent;
    }
}