package ui.common;

import data.CoffeeConstants;
import engine.FlavorEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.FlavorProfile;
import model.Recipe;
import model.RecipeIngredient;
import ui.tab1.RecipeListView;

public class RecipeDetailView extends VBox {
    private final FlavorEngine engine = new FlavorEngine();

    // конструктор для використання у RecipeListView (головне меню)
    public RecipeDetailView(Recipe recipe, RecipeListView parent) {
        Button back = new Button("← Назад");
        back.getStyleClass().add("secondary-button");
        back.setOnAction(e -> parent.showList());
        init(recipe, back);
    }

    private void init(Recipe recipe, Button back) {
        setSpacing(16);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #fefdeb;");

        Label title = new Label(recipe.getName()); // назва рецепту
        title.getStyleClass().add("title-label");

        double rating = recipe.getRating();
        Label ratingLabel = new Label(rating == 0
                ? "Оцінок немає"
                : String.format("★ %.1f", rating));
        //якщо оцінка 0, то пишемо що оцінок немає, інакше пишемо середню оцінку у кількість відгуків
        ratingLabel.getStyleClass().add("subtitle-label");


        HBox topRow = new HBox(12, title);
        topRow.setAlignment(Pos.CENTER_LEFT);

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

        VBox content = new VBox(16,
                back, topRow, ratingLabel,
                new Separator(),
                infoRow, allergenRow,
                new Separator(),
                ingredientsTitle, ingredientsList,
                new Separator(),
                radarTitle, radar, spicyLabel,
                new Separator()
        );
        content.setPadding(new Insets(4));

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Label addReviewTitle = new Label("Залишити оцінку");
        addReviewTitle.getStyleClass().add("subtitle-label");

        ToggleGroup starGroup = new ToggleGroup();
        HBox stars = new HBox(4);

        for (int i = 1; i <= 5; i++) {
            ToggleButton star = new ToggleButton("★");
            star.setToggleGroup(starGroup);
            star.setUserData(i);
            stars.getChildren().add(star);

            star.setStyle("-fx-background-color: transparent; -fx-text-fill: #C5D86D; -fx-font-size: 22px; -fx-cursor: hand; -fx-padding: 0 2;");
        }
        starGroup.selectedToggleProperty().addListener((obs, old, newToggle) -> {
            int createdRating = newToggle != null ? (int) newToggle.getUserData() : 0;
            stars.getChildren().forEach(node -> {
                ToggleButton btn = (ToggleButton) node;
                String color = (int) btn.getUserData() <= createdRating ? "#FFD700" : "#C5D86D";
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + "; -fx-font-size: 22px; -fx-cursor: hand; -fx-padding: 0 2;");
            });
        });

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

            int userRating = (int) selected.getUserData();
            int newCount = recipe.getRatingCount() + 1;
            double newRating = (recipe.getRating() * recipe.getRatingCount() + userRating) / newCount;
            recipe.setRating(newRating);
            recipe.setRatingCount(newCount);
            ratingLabel.setText(String.format("★ %.1f", newRating));

            starGroup.selectToggle(null);
            errorLabel.setText("");
        });

        VBox reviewForm = new VBox(10,
                addReviewTitle,
                stars, errorLabel, submitBtn
        );

        content.getChildren().add(reviewForm);

        getChildren().add(scroll);
    }


    private Label infoChip(String text) {
        Label chip = new Label(text);
        chip.getStyleClass().add("info-chip");
        return chip;
    }
}