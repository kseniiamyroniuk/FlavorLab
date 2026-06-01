package ui.tab3;

import data.CoffeeConstants;
import data.RecipeRepository;
import data.UserRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Recipe;
import model.User;
import ui.common.RecipeDetailView;

public class ProfileView extends BorderPane {
    private final Label myCountLabel = new Label();
    private final Label savedCountLabel = new Label();
    private final Label totalCountLabel = new Label();

    private final User user;
    private final VBox myRecipesList = new VBox(10);
    private final VBox savedRecipesList = new VBox(10);

    private TabPane tabPane;


    private TabPane buildTabs() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: #fefdeb;");

        Tab myTab    = new Tab("Мої рецепти",  buildMyRecipes());
        Tab savedTab = new Tab("♥ Збережені", buildSavedRecipes());

        tabPane.getTabs().addAll(myTab, savedTab);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, old, now) -> {
            if (now == myTab)    refreshMyRecipes();
            if (now == savedTab) refreshSavedRecipes();
        });

        return tabPane;
    }

    public void showProfile() {
        setCenter(tabPane);
        refreshMyRecipes();
        refreshSavedRecipes();
        refreshStats();
    }

    public ProfileView() {
        this.user = UserRepository.getCurrentUser();
        setPadding(new Insets(24));
        setStyle("-fx-background-color: #fefdeb;");

        setTop(buildHeader());
        setCenter(buildTabs());
    }

    // ХЕДЕР - аватарка + ім'я + статистика

    private VBox buildHeader() {
        String initials = user != null && !user.getName().isBlank()
                ? String.valueOf(user.getName().charAt(0)).toUpperCase()
                : "?";

        Label avatar = new Label(initials);
        avatar.setStyle(
                "-fx-background-color: #DDEB9D; -fx-text-fill: #4F5D2F;" +
                        "-fx-font-size: 28px; -fx-font-weight: bold;" +
                        "-fx-min-width: 72; -fx-min-height: 72;" +
                        "-fx-max-width: 72; -fx-max-height: 72;" +
                        "-fx-background-radius: 36; -fx-alignment: center;"
        );

        Label name = new Label(user != null ? user.getName() : "Гість");
        name.getStyleClass().add("title-label");

        refreshStats();

        HBox stats = new HBox(24,
                statChip("Моїх рецептів", myCountLabel),
                statChip("♥ Збережених", savedCountLabel),
                statChip("Всього в базі", totalCountLabel)
        );
        stats.setAlignment(Pos.CENTER_LEFT);
        stats.setPadding(new Insets(8, 0, 0, 0));

        HBox avatarRow = new HBox(16, avatar, new VBox(6, name, stats));
        avatarRow.setAlignment(Pos.CENTER_LEFT);

        Separator sep = new Separator();
        sep.setPadding(new Insets(16, 0, 0, 0));

        VBox header = new VBox(0, avatarRow, sep);
        header.setPadding(new Insets(0, 0, 16, 0));
        return header;
    }

    private void refreshStats() {
        int myCount = user != null ? user.getMyRecipes().size() : 0;
        int savedCount = user != null ? user.getSavedRecipes().size() : 0;
        int totalCount = RecipeRepository.getAll().size();
        myCountLabel.setText(String.valueOf(myCount));
        savedCountLabel.setText(String.valueOf(savedCount));
        totalCountLabel.setText(String.valueOf(totalCount));
    }

    // МОЇ РЕЦЕПТИ

    private ScrollPane buildMyRecipes() {
        refreshMyRecipes();

        ScrollPane scroll = new ScrollPane(myRecipesList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPadding(new Insets(12, 0, 0, 0));
        return scroll;
    }

    private void refreshMyRecipes() {
        myRecipesList.getChildren().clear();

        if (user == null || user.getMyRecipes().isEmpty()) {
            myRecipesList.getChildren().add(emptyLabel("Ти ще не створив/ла жодного рецепту"));
            return;
        }

        for (Recipe recipe : user.getMyRecipes()) {
            myRecipesList.getChildren().add(recipeCard(recipe, false));
        }
    }

    // ЗБЕРЕЖЕНІ РЕЦЕПТИ


    private ScrollPane buildSavedRecipes() {
        refreshSavedRecipes();

        ScrollPane scroll = new ScrollPane(savedRecipesList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPadding(new Insets(12, 0, 0, 0));
        return scroll;
    }

    private void refreshSavedRecipes() {
        savedRecipesList.getChildren().clear();

        if (user == null || user.getSavedRecipes().isEmpty()) {
            savedRecipesList.getChildren().add(emptyLabel("Немає збережених рецептів"));
            return;
        }

        for (Recipe recipe : user.getSavedRecipes()) {
            savedRecipesList.getChildren().add(recipeCard(recipe, true));
        }
    }

    private HBox recipeCard(Recipe recipe, boolean canUnsave) {
        Label name = new Label(recipe.getName());
        name.setStyle("-fx-text-fill: #4F5D2F; -fx-font-size: 15px; -fx-font-weight: bold;");

        String authorText = recipe.getAuthor() != null
                ? "Автор: " + recipe.getAuthor().getName() : "";
        Label author = new Label(authorText);
        author.getStyleClass().add("small-label");

        double rating = recipe.averageRating();
        Label ratingLabel = new Label(rating == 0
                ? "Немає оцінок" : String.format("★ %.1f", rating));
        ratingLabel.setStyle("-fx-text-fill: #A7C957; -fx-font-size: 13px;");

        Label info = new Label(String.format("%.0f ккал  •  %.0f ₴  •  %s  •  %s",
                recipe.getCalories(), recipe.getCostUah(),
                CoffeeConstants.BREW_UA.getOrDefault(recipe.getBrewMethod(), recipe.getBrewMethod()),
                CoffeeConstants.TEMP_UA.getOrDefault(recipe.getTemperature(), recipe.getTemperature())));
        info.getStyleClass().add("small-label");

        VBox textCol = new VBox(4, name, author, ratingLabel, info);
        textCol.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textCol, Priority.ALWAYS);

        Button actionBtn;
        if (canUnsave) {
            actionBtn = new Button("♥ Видалити");
            actionBtn.getStyleClass().add("secondary-button");
            actionBtn.setOnAction(e -> {
                user.unsaveRecipe(recipe);
                refreshSavedRecipes();
                refreshStats();
            });
        } else {
            actionBtn = new Button("Видалити");
            actionBtn.setStyle(
                    "-fx-border-color: #e07b54; -fx-text-fill: #e07b54;" +
                            "-fx-background-color: transparent; -fx-border-radius: 24;" +
                            "-fx-background-radius: 24; -fx-padding: 8 20; -fx-cursor: hand;"
            );
            actionBtn.setOnAction(e -> {
                user.getMyRecipes().remove(recipe);
                RecipeRepository.remove(recipe);
                refreshMyRecipes();
                refreshStats();
            });
        }

        HBox card = new HBox(16, textCol, actionBtn);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: #FFFFFF; -fx-background-radius: 14;" +
                        "-fx-border-color: #E4ECC3; -fx-border-radius: 14; -fx-padding: 14 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(112,130,64,0.10), 8, 0, 0, 2);" +
                        "-fx-cursor: hand;"
        );

        // клік на картку відкриває детальний перегляд
        card.setOnMouseClicked(e -> {
            // ігноруємо якщо клікнули на кнопку
            if (e.getTarget() instanceof Button) return;
            openDetail(recipe);
        });

        return card;
    }

    private void openDetail(Recipe recipe) {
        RecipeDetailView detail = new RecipeDetailView(recipe, this);
        setCenter(detail);
    }


    private VBox statChip(String label, Label countLabel) {
        countLabel.setStyle(
                "-fx-text-fill: #4F5D2F; -fx-font-size: 20px; -fx-font-weight: bold;"
        );
        Label textLabel = new Label(label);
        textLabel.getStyleClass().add("small-label");

        VBox chip = new VBox(2, countLabel, textLabel);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setStyle(
                "-fx-background-color: #F8FBEF; -fx-background-radius: 10;" +
                        "-fx-border-color: #E4ECC3; -fx-border-radius: 10; -fx-padding: 8 16;"
        );
        return chip;
    }

    private Label emptyLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("small-label");
        l.setPadding(new Insets(20, 0, 0, 4));
        return l;
    }

    public void refresh() {
        refreshMyRecipes();
        refreshSavedRecipes();
        refreshStats();
    }
}