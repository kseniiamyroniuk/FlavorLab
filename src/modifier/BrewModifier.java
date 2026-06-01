package modifier;

import java.util.Map;

public class BrewModifier {

    // acidity | sweetness | bitterness | fruitiness | creaminess
    private static final Map<String, double[]> MODIFIERS = Map.ofEntries(
            Map.entry("espresso", new double[]{1.0, 1.0, 1.3, 0.9, 1.4}),
            Map.entry("filter", new double[]{1.3, 1.0, 0.8, 1.3, 0.7}),
            Map.entry("cold_brew", new double[]{0.7, 1.1, 0.6, 1.1, 1.2}),
            Map.entry("aeropress", new double[]{1.1, 1.0, 1.0, 1.1, 1.1}),
            Map.entry("moka_pot", new double[]{0.9, 1.0, 1.4, 0.8, 1.3}),
            Map.entry("chemex", new double[]{1.2, 1.0, 0.7, 1.4, 0.6}),
            Map.entry("v60", new double[]{1.3, 1.0, 0.7, 1.4, 0.6}),
            Map.entry("french_press", new double[]{0.9, 1.0, 1.2, 0.9, 1.3}),
            Map.entry("turkish_coffee", new double[]{0.8, 1.1, 1.5, 0.7, 1.2}),
            Map.entry("siphon", new double[]{1.2, 1.0, 0.9, 1.3, 0.8}),
            Map.entry("nitro_cold_brew", new double[]{0.6, 1.2, 0.5, 1.0, 1.4})
    );

    public static double[] get(String brewMethod) {
        return MODIFIERS.getOrDefault(brewMethod, new double[]{1, 1, 1, 1, 1});
    }
}