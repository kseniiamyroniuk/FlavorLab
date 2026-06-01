package ui.common;

import data.CoffeeConstants;
import data.UserRepository;
import engine.FlavorEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;
import ui.tab1.RecipeListView;
import ui.tab3.ProfileView;


public class RecipeDetailView extends VBox {
    private final FlavorEngine engine = new FlavorEngine();

    // конструктор для використання у RecipeListView (головне меню)
    public RecipeDetailView(Recipe recipe, RecipeListView parent) {
        Button back = new Button("← Назад");
        back.getStyleClass().add("secondary-button");
        back.setOnAction(e -> parent.showList());
        init(recipe, back);
    }

    // конструктор для використання у ProfileView
    public RecipeDetailView(Recipe recipe, ProfileView parent) {
        Button back = new Button("← Назад");
        back.getStyleClass().add("secondary-button");
        back.setOnAction(e -> parent.showProfile());
        init(recipe, back);
    }

    private void init(Recipe recipe, Button back) {
        setSpacing(16);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #fefdeb;");

        Label title = new Label(recipe.getName()); // назва рецепту
        title.getStyleClass().add("title-label");

        String authorName = recipe.getAuthor() != null
                ? recipe.getAuthor().getName() : "Невідомо";
        Label author = new Label("Автор: " + authorName);
        author.getStyleClass().add("small-label");

        double rating = recipe.averageRating();
        Label ratingLabel = new Label(rating == 0
                ? "Оцінок немає"
                : String.format("★ %.1f  (%d відгуків)", rating, recipe.getReviews().size()));
        //якщо оцінка 0, то пишемо що оцінок немає, інакше пишемо середню оцінку у кількість відгуків
        ratingLabel.getStyleClass().add("subtitle-label");

        // логіка відображення кнопки збережено та зберігання/видалення з улюблених рецепту
        User currentUser = UserRepository.getCurrentUser();
        boolean isSaved = currentUser != null && currentUser.hasSaved(recipe);
        Button saveBtn = new Button(isSaved ? "♥ Збережено" : "♡ Зберегти");
        saveBtn.getStyleClass().add("secondary-button");
        saveBtn.setOnAction(e -> {
            if (currentUser == null) return;
            if (currentUser.hasSaved(recipe)) {
                currentUser.unsaveRecipe(recipe);
                saveBtn.setText("♡ Зберегти");
                saveBtn.getStyleClass().remove("saved-button");
            } else {
                currentUser.saveRecipe(recipe);
                saveBtn.setText("♥ Збережено");
                saveBtn.getStyleClass().add("saved-button");
            }
        });

        HBox topRow = new HBox(12, title, saveBtn);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // рахуємо кофеїн
        FlavorProfile profile = engine.calculate(recipe);

        // інформація загальна про напій
        HBox infoRow = new HBox(16,
                infoChip(String.format("%.0f ккал", recipe.getCalories())),
                infoChip(String.format("%.0f ₴", recipe.getCostUah())),
                infoChip(CoffeeConstants.tempName(recipe.getTemperature())),
                infoChip(CoffeeConstants.brewName(recipe.getBrewMethod()))
        );

        // алергени. для кожного алергена робимо chip
        HBox allergenRow = new HBox(8);
        for (String a : recipe.getAllergens()) {
            Label chip = new Label("⚠ " + a);
            chip.getStyleClass().add("allergy-chip");
            allergenRow.getChildren().add(chip);
        }

        Label ingredientsTitle = new Label("Інгредієнти");
        ingredientsTitle.getStyleClass().add("subtitle-label");

        // список інгредієнтів у напої та їх міліметраж
        VBox ingredientsList = new VBox(6);
        for (RecipeIngredient ri : recipe.getIngredients()) {
            Label item = new Label(String.format("• %s  —  %.0f мл",
                    ri.getIngredient().getName(), ri.getVolumeMl()));
            item.getStyleClass().add("small-label");
            ingredientsList.getChildren().add(item);
        }

        Label radarTitle = new Label("Смаковий профіль");
        radarTitle.getStyleClass().add("subtitle-label");

        RadarChart radar = new RadarChart(300);
        radar.draw(profile);

        Label spicyLabel = new Label("Пряний акцент");
        spicyLabel.getStyleClass().add("small-label");
        spicyLabel.setStyle("-fx-text-fill: #e07b54;");
        spicyLabel.setVisible(profile.isSpicyAccent()); // не показувати елемент
        spicyLabel.setManaged(profile.isSpicyAccent()); // прибрати місце в layout

        Label reviewsTitle = new Label("Відгуки");
        reviewsTitle.getStyleClass().add("subtitle-label");

        // існуючі відгуки
        VBox reviewsList = new VBox(8);
        if (recipe.getReviews().isEmpty()) {
            Label noReviews = new Label("Відгуків ще немає");
            noReviews.getStyleClass().add("small-label");
            reviewsList.getChildren().add(noReviews);
        } else {
            for (Review r : recipe.getReviews()) {
                VBox reviewCard = new VBox(4);
                Label reviewAuthor = new Label(r.getAuthor().getName()
                        + "  " + "★".repeat(r.getRating()));
                Label reviewText = new Label(r.getComment());
                reviewCard.getStyleClass().add("review-card");
                reviewAuthor.getStyleClass().add("review-author");
                reviewText.getStyleClass().add("review-text");
                reviewCard.getChildren().addAll(reviewAuthor, reviewText);
                reviewsList.getChildren().add(reviewCard);
            }
        }

        VBox content = new VBox(16,
                back, topRow, author, ratingLabel,
                new Separator(),
                infoRow, allergenRow,
                new Separator(),
                ingredientsTitle, ingredientsList,
                new Separator(),
                radarTitle, radar, spicyLabel,
                new Separator(),
                reviewsTitle, reviewsList
        );
        content.setPadding(new Insets(4));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");


        if (currentUser != null) {
            Label addReviewTitle = new Label("Залишити відгук");
            addReviewTitle.getStyleClass().add("subtitle-label");

            // зірки
            ToggleGroup starGroup = new ToggleGroup();
            HBox stars = new HBox(4);
            for (int i = 1; i <= 5; i++) {
                ToggleButton star = new ToggleButton("★");
                star.setToggleGroup(starGroup);
                star.setUserData(i);
                star.setStyle(
                        "-fx-background-color: transparent; -fx-text-fill: #C5D86D;" +
                                "-fx-font-size: 22px; -fx-cursor: hand; -fx-padding: 0 2;"
                );
                star.selectedProperty().addListener((obs, old, sel) ->
                        star.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: " + (sel ? "#FFD700" : "#C5D86D") + ";" +
                                        "-fx-font-size: 22px; -fx-cursor: hand; -fx-padding: 0 2;"
                        )
                );
                stars.getChildren().add(star);
            }

            // текст
            TextArea commentField = new TextArea();
            commentField.setPromptText("Твій коментар...");
            commentField.setPrefRowCount(2);
            commentField.setWrapText(true);
            commentField.setStyle(
                    "-fx-background-color: #F8FBEF; -fx-text-fill: #4F5D2F;" +
                            "-fx-border-color: #C5D86D; -fx-border-radius: 10;" +
                            "-fx-background-radius: 10; -fx-padding: 8 12;"
            );

            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: #e07b54; -fx-font-size: 12px;");

            Button submitBtn = new Button("Опублікувати");
            submitBtn.getStyleClass().add("primary-button");
            submitBtn.setOnAction(e -> {
                Toggle selected = starGroup.getSelectedToggle();
                if (selected == null) {
                    errorLabel.setText("Обери рейтинг");
                    return;
                }
                String comment = commentField.getText().trim();

                int rating2 = (int) selected.getUserData();
                Review newReview = new Review(currentUser, rating2, comment);
                recipe.addReview(newReview);

                // оновлюємо список відгуків
                reviewsList.getChildren().clear();
                for (Review r : recipe.getReviews()) {
                    VBox reviewCard = new VBox(4);
                    Label reviewAuthor = new Label(r.getAuthor().getName()
                            + "  " + "★".repeat(r.getRating()));
                    Label reviewText = new Label(r.getComment());
                    reviewCard.getStyleClass().add("review-card");
                    reviewAuthor.getStyleClass().add("review-author");
                    reviewText.getStyleClass().add("review-text");
                    reviewCard.getChildren().addAll(reviewAuthor, reviewText);
                    reviewsList.getChildren().add(reviewCard);
                }

                // оновлюємо рейтинг
                double newRating = recipe.averageRating();
                ratingLabel.setText(String.format("★ %.1f  (%d відгуків)",
                        newRating, recipe.getReviews().size()));

                // скидаємо форму
                starGroup.selectToggle(null);
                commentField.clear();
                errorLabel.setText("");
            });

            VBox reviewForm = new VBox(10,
                    new Separator(),
                    addReviewTitle,
                    stars, commentField, errorLabel, submitBtn
            );

            content.getChildren().add(reviewForm);
        }

        getChildren().add(scroll);
    }


    private Label infoChip(String text) {
        Label chip = new Label(text);
        chip.getStyleClass().add("info-chip");
        return chip;
    }
}