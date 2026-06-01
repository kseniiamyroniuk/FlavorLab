package modifier;

import java.util.Map;

public class OriginModifier {

    // acidity | sweetness | bitterness | fruitiness | creaminess
    private static final Map<String, double[]> MODIFIERS = Map.of(
            // Яскрава кислотність, цитрус, квіти, ягоди
            "ethiopia", new double[]{1.45, 1.10, 0.85, 1.55, 0.95},

            // Баланс карамелі, яблука, червоні ягоди
            "colombia", new double[]{1.20, 1.20, 0.95, 1.20, 1.05},

            // Горіхи, шоколад, низька кислотність, щільне тіло
            "brazil", new double[]{0.80, 1.30, 1.05, 0.75, 1.30},

            // Какао, спеції, більш виражена гірчинка
            "guatemala", new double[]{1.05, 1.00, 1.30, 0.95, 1.00},

            // Дуже яскрава кислотність, чорна смородина, соковитість
            "kenya", new double[]{1.55, 1.00, 0.95, 1.45, 0.85},

            // Землистість, спеції, важке тіло, кремовість
            "indonesia", new double[]{0.70, 1.00, 1.35, 0.70, 1.45}
    );

    public static double[] get(String origin) {
        return MODIFIERS.getOrDefault(origin, new double[]{1, 1, 1, 1, 1});
    }
}