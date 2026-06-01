package data;

import model.*;

import java.util.List;

public class SampleData {

    public static void load() {
        Recipe flatWhite = new Recipe(
                "Flat White",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("espresso"), 50),
                        new RecipeIngredient(IngredientRepository.get("вівсяне_молоко"), 150)
                ),
                "ethiopia", "espresso", "hot"
        );
        flatWhite.setRating(4.3);
        flatWhite.setRatingCount(5);
        RecipeUtils.finalize(flatWhite);
        RecipeRepository.add(flatWhite);

        Recipe mangoColdBrew = new Recipe(
                "Манго Cold Brew",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("cold_brew"), 150),
                        new RecipeIngredient(IngredientRepository.get("манго"), 50),
                        new RecipeIngredient(IngredientRepository.get("кокосове_молоко"), 30)
                ),
                "colombia", "cold_brew", "cold"
        );
        mangoColdBrew.setRating(3.9);
        mangoColdBrew.setRatingCount(2);
        RecipeUtils.finalize(mangoColdBrew);
        RecipeRepository.add(mangoColdBrew);

        Recipe lavenderLatte = new Recipe(
                "Лавандове Лате",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("espresso"), 50),
                        new RecipeIngredient(IngredientRepository.get("цільне_молоко"), 180),
                        new RecipeIngredient(IngredientRepository.get("лаванда"), 20)
                ),
                "kenya", "espresso", "hot"
        );
        lavenderLatte.setRating(4.8);
        lavenderLatte.setRatingCount(2);
        RecipeUtils.finalize(lavenderLatte);
        RecipeRepository.add(lavenderLatte);

        Recipe raspberryCaramel = new Recipe(
                "Малинова карамель",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("filter_coffee"), 200),
                        new RecipeIngredient(IngredientRepository.get("вівсяне_молоко"), 150),
                        new RecipeIngredient(IngredientRepository.get("карамель"), 30),
                        new RecipeIngredient(IngredientRepository.get("малина"), 40)
                ),
                "ethiopia", "filter", "cold"
        );
        raspberryCaramel.setRating(4.4);
        raspberryCaramel.setRatingCount(7);
        RecipeUtils.finalize(raspberryCaramel);
        RecipeRepository.add(raspberryCaramel);


        Recipe citrusTonic = new Recipe(
                "Цитрусове Tonic Espresso",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("espresso"), 50),
                        new RecipeIngredient(IngredientRepository.get("тонік_цитрус"), 150),
                        new RecipeIngredient(IngredientRepository.get("цедра_апельсина"), 10)
                ),
                "ethiopia", "espresso", "cold"
        );
        citrusTonic.setRating(2.3);
        citrusTonic.setRatingCount(3);
        RecipeUtils.finalize(citrusTonic);
        RecipeRepository.add(citrusTonic);

        Recipe pistachioLatte = new Recipe(
                "Фісташкове крем лате",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("espresso"), 50),
                        new RecipeIngredient(IngredientRepository.get("фісташкове_молоко"), 180),
                        new RecipeIngredient(IngredientRepository.get("фісташка"), 20)
                ),
                "brazil", "espresso", "hot"
        );
        pistachioLatte.setRating(3.3);
        pistachioLatte.setRatingCount(5);
        RecipeUtils.finalize(pistachioLatte);
        RecipeRepository.add(pistachioLatte);

        Recipe honeyCardamom = new Recipe(
                "Медовий кардамоновий V60",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("v60"), 250),
                        new RecipeIngredient(IngredientRepository.get("мед"), 15),
                        new RecipeIngredient(IngredientRepository.get("кардамон"), 5)
                ),
                "kenya", "filter", "hot"
        );
        honeyCardamom.setRating(2.9);
        honeyCardamom.setRatingCount(9);
        RecipeUtils.finalize(honeyCardamom);
        RecipeRepository.add(honeyCardamom);

        Recipe coconutNitro = new Recipe(
                "Кокосове нітро",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("nitro_cold_brew"), 200),
                        new RecipeIngredient(IngredientRepository.get("кокосова_вода"), 100),
                        new RecipeIngredient(IngredientRepository.get("кокосовий_сироп"), 20)
                ),
                "colombia", "cold_brew", "cold"
        );
        coconutNitro.setRating(4.6);
        coconutNitro.setRatingCount(7);
        RecipeUtils.finalize(coconutNitro);
        RecipeRepository.add(coconutNitro);

        Recipe blackForest = new Recipe(
                "Black Forest Mocha",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("espresso"), 50),
                        new RecipeIngredient(IngredientRepository.get("цільне_молоко"), 170),
                        new RecipeIngredient(IngredientRepository.get("темний_шоколад"), 20),
                        new RecipeIngredient(IngredientRepository.get("вишня"), 30)
                ),
                "brazil", "espresso", "hot"
        );
        blackForest.setRating(4.3);
        blackForest.setRatingCount(5);
        RecipeUtils.finalize(blackForest);
        RecipeRepository.add(blackForest);

        Recipe seaSaltCaramel = new Recipe(
                "Морська солона карамель",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("cold_brew"), 180),
                        new RecipeIngredient(IngredientRepository.get("солона_карамель"), 25),
                        new RecipeIngredient(IngredientRepository.get("вівсяне_молоко"), 80),
                        new RecipeIngredient(IngredientRepository.get("морська_сіль"), 2)
                ),
                "colombia", "cold_brew", "cold"
        );
        seaSaltCaramel.setRating(4.4);
        seaSaltCaramel.setRatingCount(6);
        RecipeUtils.finalize(seaSaltCaramel);
        RecipeRepository.add(seaSaltCaramel);
    }
}