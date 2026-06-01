package data;

import model.*;

import java.util.List;

public class SampleData {

    public static void load() {
        User kseniia = new User("Kseniia", null);
        User barista = new User("Barista Pro", null);
        UserRepository.add(kseniia);
        UserRepository.add(barista);
        UserRepository.setCurrentUser(kseniia);

        Recipe flatWhite = new Recipe(
                "Flat White",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("espresso"), 50),
                        new RecipeIngredient(IngredientRepository.get("вівсяне_молоко"), 150)
                ),
                "ethiopia", "espresso", "hot"
        );
        flatWhite.setAuthor(barista);
        flatWhite.addReview(new Review(kseniia, 5, "Дуже збалансований!"));
        flatWhite.addReview(new Review(barista, 4, "Класика"));
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
        mangoColdBrew.setAuthor(barista);
        mangoColdBrew.addReview(new Review(barista, 5, "Тропічний вибух!"));
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
        lavenderLatte.setAuthor(barista);
        lavenderLatte.addReview(new Review(kseniia, 4, "Дуже ароматний"));
        RecipeUtils.finalize(lavenderLatte);
        RecipeRepository.add(lavenderLatte);
        barista.saveRecipe(lavenderLatte);
        kseniia.saveRecipe(lavenderLatte);

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
        raspberryCaramel.setAuthor(barista);
        raspberryCaramel.addReview(new Review(barista, 5, "Неочікувано круто!"));
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
        citrusTonic.setAuthor(barista);
        citrusTonic.addReview(new Review(kseniia, 5, "Супер освіжає!"));
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
        pistachioLatte.setAuthor(kseniia);
        pistachioLatte.addReview(new Review(barista, 5, "Дуже creamy"));
        RecipeUtils.finalize(pistachioLatte);
        RecipeRepository.add(pistachioLatte);
        kseniia.addRecipe(pistachioLatte);

        Recipe honeyCardamom = new Recipe(
                "Медовий кардамоновий V60",
                List.of(
                        new RecipeIngredient(IngredientRepository.get("v60"), 250),
                        new RecipeIngredient(IngredientRepository.get("мед"), 15),
                        new RecipeIngredient(IngredientRepository.get("кардамон"), 5)
                ),
                "kenya", "filter", "hot"
        );
        honeyCardamom.setAuthor(barista);
        honeyCardamom.addReview(new Review(kseniia, 4, "Дуже пряний aftertaste"));
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
        coconutNitro.setAuthor(barista);
        coconutNitro.addReview(new Review(barista, 5, "Літній вайб"));
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
        blackForest.setAuthor(barista);
        blackForest.addReview(new Review(kseniia, 5, "Смакує як десерт"));
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
        seaSaltCaramel.setAuthor(barista);
        seaSaltCaramel.addReview(new Review(barista, 5, "Balanced sweet & salty"));
        RecipeUtils.finalize(seaSaltCaramel);
        RecipeRepository.add(seaSaltCaramel);
    }
}