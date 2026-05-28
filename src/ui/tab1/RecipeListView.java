package ui.tab1;

import data.RecipeRepository;
import data.UserRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Recipe;
import model.User;
import ui.common.RecipeDetailView;

import java.util.List;

public class RecipeListView extends BorderPane {

    private final FlowPane cardsPane = new FlowPane();
    public RecipeListView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #fefdeb;");

        Label title = new Label("Меню рецептів");
        title.getStyleClass().add("title-label");

        // пошук
        TextField search = new TextField();
        search.setPromptText("Пошук рецепту...");
        search.setPrefWidth(300);
        search.textProperty().addListener((obs, old, val) -> loadCards(val));

        HBox header = new HBox(16, title, search);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 16, 0));

        // картки рецептів
        cardsPane.setHgap(16);
        cardsPane.setVgap(16);
        cardsPane.setPadding(new Insets(4));

        ScrollPane scroll = new ScrollPane(cardsPane);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        setTop(header);
        setCenter(scroll);

        loadCards("");
    }

    private void loadCards(String query) {
        cardsPane.getChildren().clear();

        User currentUser = UserRepository.getCurrentUser();

        List<Recipe> all = query.isBlank()
                ? RecipeRepository.getAll()
                : RecipeRepository.search(query);

        // фільтруємо - прибираємо мої та збережені
        List<Recipe> filtered = all.stream()
                .filter(r -> {
                    if (currentUser == null) return true;
                    boolean isMine   = currentUser.getMyRecipes().contains(r);
                    boolean isSaved  = currentUser.hasSaved(r);
                    return !isMine && !isSaved;
                })
                .collect(java.util.stream.Collectors.toList());

        // рандомний порядок щоразу
        java.util.Collections.shuffle(filtered);

        for (Recipe recipe : filtered) {
            cardsPane.getChildren().add(makeCard(recipe));
        }

        if (filtered.isEmpty()) {
            Label empty = new Label("Немає нових рецептів для перегляду");
            empty.getStyleClass().add("small-label");
            cardsPane.getChildren().add(empty);
        }
    }
    private VBox makeCard(Recipe recipe) {
        Label name = new Label(recipe.getName());
        name.getStyleClass().add("subtitle-label");
        name.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        // рейтинг
        double rating = recipe.averageRating();
        String stars = rating == 0 ? "Немає оцінок" : String.format("★ %.1f", rating);
        Label ratingLabel = new Label(stars);
        ratingLabel.getStyleClass().add("small-label");
        ratingLabel.setStyle("-fx-text-fill: #c8a882;");

        // калорії та ціна
        Label info = new Label(String.format("%.0f ккал  •  %.0f ₴",
                recipe.getCalories(), recipe.getCostUah()));
        info.getStyleClass().add("small-label");

        // алергени
        String allergenText = recipe.getAllergens().isEmpty()
                ? ""
                : "⚠ " + String.join(", ", recipe.getAllergens());
        Label allergens = new Label(allergenText);
        allergens.getStyleClass().add("small-label");
        allergens.setStyle("-fx-text-fill: #e07b54;");
        allergens.setWrapText(true);

        VBox card = new VBox(8, name, ratingLabel, info, allergens);
        card.getStyleClass().add("card");
        card.setPrefWidth(200);
        card.setMaxWidth(200);

        name.setWrapText(true);
        allergens.setWrapText(true);

        // клік -> детальний перегляд
        card.setOnMouseClicked(e -> openDetail(recipe));

        return card;
    }

    private void openDetail(Recipe recipe) {
        RecipeDetailView detail = new RecipeDetailView(recipe, this);
        setCenter(detail);
        setTop(null);
    }

    public void showList() {
        // повернення до списку
        setPadding(new Insets(20));

        Label title = new Label("Меню рецептів");
        title.getStyleClass().add("title-label");

        TextField search = new TextField();
        search.setPromptText("Пошук рецепту...");
        search.setPrefWidth(300);
        search.textProperty().addListener((obs, old, val) -> loadCards(val));

        HBox header = new HBox(16, title, search);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 16, 0));

        ScrollPane scroll = new ScrollPane(cardsPane);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        setTop(header);
        setCenter(scroll);

        loadCards("");
    }

    public void refresh() {
        loadCards("");
    }
}