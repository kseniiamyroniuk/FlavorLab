package engine;

import model.FlavorProfile;
import model.Ingredient;
import model.Recipe;
import model.RecipeIngredient;
import modifier.BrewModifier;
import modifier.OriginModifier;
import modifier.TempModifier;

import java.util.List;

public class FlavorEngine {
    public FlavorProfile calculate(Recipe recipe) {
        // список інгредієнтів, що будуть і рецепті
        List<RecipeIngredient> items = recipe.getIngredients();

        // рахуємо обʼєм
        // кожний інгредієнт беремо обʼєм та сумуємо
        double effectiveVolume = items.stream()
                .mapToDouble(RecipeIngredient::getVolumeMl)
                .sum();

        // якщо обʼєм 0 -> повертаємо порожній профіль -- ніякого смаку
        if (effectiveVolume == 0) {
            return new FlavorProfile(0,0,0,0,0,false);
        }

        double[] result = new double[5]; // масив для результатів осей
        boolean spicy = false;

        // цикл по кожному інгредієнту (RecipeIngredient) рецепту
        // RecipeIngredient має 2 поля -- Ingredient + Volume
        for (RecipeIngredient item : items) {
            // Дістаємо з RecipeIngredient сам Ingredient
            // Далі з Ingredient отримуємо FlavorProfile, що є осями смаків
            // записуємо у масив values, щоб могти пройтись циклом
            Ingredient ingredient = item.getIngredient();
            FlavorProfile profile = ingredient.getProfile();
            double[] values = toArray(profile);

            // дивимось скільки місця займає кожен інгредієнт у напої
            // чим більше -> тим більше цей інгредієнт впливає на кінцевий смак
            double weight = item.getVolumeMl() / effectiveVolume;

            if (ingredient.getCategory().equals("coffee")) { // для кави окремо рахуємо з модифікаторами
                double[] origin = OriginModifier.get(recipe.getOrigin());
                double[] brew = BrewModifier.get(recipe.getBrewMethod());
                // отримуємо модифікатори смаку через походження зерен на метод заварювання
                // змінюємо осі кави помноживши на модифікатори
                for (int i = 0; i < 5; i++) {
                    values[i] *= origin[i] * brew[i];
                }
            }

            // записуємо осі смаків * на відсоток інгредієнта у напої у масив
            for (int i = 0; i < 5; i++) {
                result[i] += values[i] * weight;
            }
            // рахуємо чи буде напій з спеціями
            if (ingredient.getSpiciness() > 4) spicy = true;
        }

        // вираховуємо зміну від модифікаторів температури
        double[] temp = TempModifier.get(recipe.getTemperature());
        for (int i = 0; i < 5; i++) {
            result[i] *= temp[i];
            result[i] = Math.max(0, Math.min(10, result[i])); // не дає вийти за межі 0...10
        }

        // повертаємо новий вирахуваний FlavorProfile напою
        return new FlavorProfile(
                result[0], result[1], result[2],
                result[3], result[4], spicy
        );
    }


    public double balanceScore(FlavorProfile p) {
        // перетворюємо у масив профіль смаків
        double[] axes = toArray(p);

        double mean = 0; // середнє значення
        // axes - осі
        // v - value - значення
        // отрмуємо середнє серед усіх осей смаків, щоб зрозуміти чи домінує якийсь один смаковий профіль
        for (double v : axes) mean += v;
        mean /= axes.length;

        // наскільки кожне число далеко від середнього
        double variance = 0;
        // сума відхилень кожного
        for (double v : axes) variance += Math.pow(v - mean, 2);
        double stddev = Math.sqrt(variance / axes.length);
        // Standard deviation -- стандартне відхилення
        // stddev великий -- домінує один смак, stddev мелий -- збалансовано

        // ми ділимо відхилення на 5, бо 5 осей
        // віднімаємо від 1 цей результат
        // отримуємо balance score. чим ближче до 1, тим збалансованіші осі
        // Math.max є лише щоб значення не було менше 0
        return Math.max(0, 1 - (stddev / 5));

        // приклад
        // [6, 5, 4, 5, 5] - осі напою
        // mean = 25/5 = 5
        // variance[5] = (5-5)^2 = 0^2 = 0
        // variance[4] = variance[6] = 1
        // variance = 0*3 + 1*2 = 2
        // stddev = sqrt(2/5) = sqrt(0.4) = 0.63 -- малий
        // 1 - 0.63/5 = 0.874
    }

    public String dominantFlavor(FlavorProfile p) {
        String[] names = {"Кислинка", "Солодкість", "Гіркота",
                "Фруктовість", "Кремовість"};
        // проходимось по масиву і знаходимо найбільшу ось і повертаємо її назву. нічого цікавого
        double[] values = toArray(p);

        int maxIndex = 0;
        for (int i = 1; i < 5; i++) {
            if (values[i] > values[maxIndex]) maxIndex = i;
        }
        return names[maxIndex];
    }

    public String flavorLabel(double value) {
        if (value < 2) return "ледь відчутно";
        if (value < 5) return "помітно";
        if (value < 8) return "яскраво";
        return "домінує";
    }


    // приватний метод переводу FlavorProfile у масив зі значеннями усіх осей смакових
    private double[] toArray(FlavorProfile p) {
        return new double[]{
                p.getAcidity(),
                p.getSweetness(),
                p.getBitterness(),
                p.getFruitiness(),
                p.getCreaminess()
        };
    }
}