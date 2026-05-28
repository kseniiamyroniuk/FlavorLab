package modifier;

import java.util.Map;

public class TempModifier {

    // acidity | sweetness | bitterness | fruitiness | creaminess
    private static final Map<String, double[]> MODIFIERS = Map.of(
            "hot",  new double[]{1.1, 0.9, 1.1, 1.0, 1.0},
            "cold", new double[]{0.8, 1.2, 0.7, 1.0, 1.1}
    );

    public static double[] get(String temperature) {
        return MODIFIERS.getOrDefault(temperature, new double[]{1, 1, 1, 1, 1});
    }
}