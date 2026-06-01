package ui.tab2;

import data.*;
import engine.FlavorEngine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import ui.common.RadarChart;

import java.util.*;

public class RecipeBuilderView extends BorderPane {
    private final Label caloriesLabel = new Label();
    private final Label costLabel = new Label();

    private final FlavorEngine engine = new FlavorEngine();
    private final Map<String, Double> selected = new LinkedHashMap<>();
    private final RadarChart radar = new RadarChart(260);

    private final TextField nameField = new TextField();
    private final ComboBox<String> originBox = new ComboBox<>();
    private final ToggleGroup brewGroup = new ToggleGroup();
    private final ComboBox<String> tempBox = new ComboBox<>();
    private final VBox selectedList = new VBox(8);
    private final Label balanceLabel = new Label();
    private final Label dominantLabel = new Label();
    private final Label spicyLabel = new Label("⚠ Пряний акцент");

    private final Label coffeeInfoLabel = new Label();

    private final String[] CATEGORIES = {"milk", "syrup", "puree", "tonic", "spice", "nuts", "citrus", "foam", "seasonal"};
    private final String[] CATEGORY_NAMES = {"Молоко", "Сиропи", "Пюре", "Тоніки", "Спеції", "Горіхи", "Цитруси", "Піна", "Сезонні"};
    private Map<String, List<Map.Entry<String, Ingredient>>> groupedRef;

    public RecipeBuilderView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #fefdeb;");

        setLeft(buildIngredientPanel());
        setCenter(buildBuilderPanel());
        setRight(buildPreviewPanel());
    }

    // ЛІВА ПАНЕЛЬ — інгредієнти
    private VBox buildIngredientPanel() {
        Label title = new Label("Інгредієнти");
        title.getStyleClass().add("subtitle-label");

        TextField search = new TextField();
        search.setPromptText("Пошук...");
        search.getStyleClass().add("text-field");
        search.setPrefWidth(185);

        VBox categoryList = new VBox(12);
        categoryListRef = categoryList;
        categoryList.setPadding(new Insets(8, 0, 0, 0));

        Map<String, List<Map.Entry<String, Ingredient>>> grouped = new LinkedHashMap<>();
        for (String cat : CATEGORIES) grouped.put(cat, new ArrayList<>());

        for (Map.Entry<String, Ingredient> entry : IngredientRepository.getAll().entrySet()) {
            String cat = entry.getValue().getCategory();
            if (grouped.containsKey(cat)) grouped.get(cat).add(entry);
        }
        // сортуємо за назвою у кожній категорії
        for (List<Map.Entry<String, Ingredient>> list : grouped.values()) {
            list.sort(Comparator.comparing(e -> e.getValue().getName()));
        }

        buildCategoryList(categoryList, CATEGORIES, CATEGORY_NAMES, grouped, "");

        groupedRef = grouped;

        search.textProperty().addListener((obs, old, val) ->
                buildCategoryList(categoryList, CATEGORIES, CATEGORY_NAMES, grouped, val));

        ScrollPane scroll = new ScrollPane(categoryList);
        scroll.setFitToWidth(true);
        scroll.setPrefWidth(200);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox panel = new VBox(10, title, search, scroll);
        panel.setPadding(new Insets(0, 16, 0, 0));
        panel.setPrefWidth(215);
        return panel;
    }

    private void buildCategoryList(VBox container, String[] categories, String[] categoryNames, Map<String, List<Map.Entry<String, Ingredient>>> grouped, String filter) {
        container.getChildren().clear();
        for (int c = 0; c < categories.length; c++) {
            String cat = categories[c];
            List<Map.Entry<String, Ingredient>> items = grouped.get(cat).stream().filter(e -> e.getValue().getName().toLowerCase().contains(filter.toLowerCase())).toList();
            if (items.isEmpty()) continue;

            Label catLabel = new Label(categoryNames[c]);
            catLabel.getStyleClass().add("small-label");
            catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4F5D2F;");

            VBox itemsBox = new VBox(2);
            for (Map.Entry<String, Ingredient> entry : items) {
                boolean isSelected = selected.containsKey(entry.getKey());
                Button btn = new Button((isSelected ? "✓ " : "+ ") + entry.getValue().getName());
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setAlignment(Pos.CENTER_LEFT);
                btn.setStyle("-fx-background-color: " + (isSelected ? "#EAF4C8" : "transparent") + ";" + "-fx-text-fill: " + (isSelected ? "#4F5D2F" : "#8D9470") + ";" + "-fx-cursor: hand; -fx-font-size: 12px;" + "-fx-padding: 5 8; -fx-background-radius: 8;");
                btn.setOnAction(e -> {
                    String key = entry.getKey();
                    if (selected.containsKey(key)) {
                        selected.remove(key);
                    } else {
                        // використовуємо defaultVolumeMl з інгредієнта
                        selected.put(key, entry.getValue().getDefaultVolumeMl());
                    }
                    buildCategoryList(container, categories, categoryNames, grouped, filter);
                    refreshSelectedList();
                    updatePreview();
                });
                itemsBox.getChildren().add(btn);
            }
            container.getChildren().addAll(catLabel, itemsBox);
        }
    }

    // ПРАВА ПАНЕЛЬ — превʼю

    private VBox buildPreviewPanel() {
        Label title = new Label("Профіль");
        title.getStyleClass().add("subtitle-label");

        balanceLabel.getStyleClass().add("small-label");

        dominantLabel.getStyleClass().add("small-label");
        dominantLabel.setStyle("-fx-text-fill: #4F5D2F; -fx-font-weight: bold;");

        spicyLabel.setStyle("-fx-text-fill: #e07b54; -fx-font-size: 13px;");
        spicyLabel.setVisible(false);
        spicyLabel.setManaged(false);

        radar.drawEmpty();

        caloriesLabel.getStyleClass().add("small-label");
        costLabel.setStyle("-fx-text-fill: #4F5D2F; -fx-font-size: 15px; -fx-font-weight: bold;");

        VBox panel = new VBox(12, title, radar, dominantLabel, balanceLabel, spicyLabel, new Separator(), costLabel, caloriesLabel);

        panel.setPadding(new Insets(0, 0, 0, 8));
        panel.setPrefWidth(280);
        return panel;
    }

    // ЛОГІКА
    private void refreshSelectedList() {
        selectedList.getChildren().clear();

        if (selected.isEmpty()) {
            Label empty = new Label("Нічого не вибрано");
            empty.getStyleClass().add("small-label");
            selectedList.getChildren().add(empty);
            return;
        }

        for (Map.Entry<String, Double> entry : selected.entrySet()) {
            Ingredient ing = IngredientRepository.get(entry.getKey());
            if (ing == null) continue;

            Label name = new Label(ing.getName());
            name.setStyle("-fx-text-fill: #4F5D2F; -fx-font-size: 13px; -fx-font-weight: bold;");

            double base = ing.getDefaultVolumeMl();
            boolean isMilk = ing.getCategory().equals("milk");

            Label volLabel = new Label(String.format("%.0f мл", entry.getValue()));
            volLabel.setStyle("-fx-text-fill: #8D9470; -fx-font-size: 12px; -fx-min-width: 40;");

            HBox controls;
            ToggleGroup portionGroup = new ToggleGroup();

            if (isMilk) {
                ToggleButton s = portionBtn("S", 100.0, portionGroup);
                ToggleButton m = portionBtn("M", 150.0, portionGroup);
                ToggleButton l = portionBtn("L", 200.0, portionGroup);
                ToggleButton xl = portionBtn("XL", 250.0, portionGroup);

                double cur = entry.getValue();
                if (cur <= 100) s.setSelected(true);
                else if (cur <= 150) m.setSelected(true);
                else if (cur <= 200) l.setSelected(true);
                else xl.setSelected(true);

                for (ToggleButton tb : List.of(s, m, l, xl)) {
                    double vol = tb.getText().equals("S") ? 100.0 : tb.getText().equals("M") ? 150.0 : tb.getText().equals("L") ? 200.0 : 250.0;
                    tb.setOnAction(e -> {
                        if (tb.isSelected()) {
                            selected.put(entry.getKey(), vol);
                            volLabel.setText(String.format("%.0f мл", vol));
                            updatePreview();
                        }
                    });
                }
                controls = new HBox(6, s, m, l, xl, volLabel);
            } else {
                ToggleButton x1 = portionBtn("x1", base, portionGroup);
                ToggleButton x2 = portionBtn("x2", base * 2, portionGroup);
                ToggleButton x3 = portionBtn("x3", base * 3, portionGroup);

                double cur = entry.getValue();
                if (cur <= base) x1.setSelected(true);
                else if (cur <= base * 2) x2.setSelected(true);
                else x3.setSelected(true);

                for (ToggleButton tb : List.of(x1, x2, x3)) {
                    tb.setOnAction(e -> {
                        if (tb.isSelected()) {
                            double val = base * (tb.getText().equals("x1") ? 1 : tb.getText().equals("x2") ? 2 : 3);
                            selected.put(entry.getKey(), val);
                            volLabel.setText(String.format("%.0f мл", val));
                            updatePreview();
                        }
                    });
                }
                controls = new HBox(6, x1, x2, x3, volLabel);
            }

            controls.setAlignment(Pos.CENTER_LEFT);

            Button remove = new Button("✕");
            remove.setStyle("-fx-background-color: transparent; -fx-text-fill: #8D9470;" + "-fx-cursor: hand; -fx-font-size: 11px; -fx-padding: 0 4;");
            remove.setOnAction(e -> {
                selected.remove(entry.getKey());
                refreshSelectedList();
                updatePreview();
            });

            HBox row = new HBox(10, name, controls, remove);
            row.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(name, Priority.ALWAYS);
            row.setStyle("-fx-background-color: #F8FBEF; -fx-background-radius: 10;" + "-fx-border-color: #E4ECC3; -fx-border-radius: 10; -fx-padding: 8 12;");
            selectedList.getChildren().add(row);
        }
    }

    private ToggleButton portionBtn(String label, double value, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(label);
        btn.setToggleGroup(group);
        btn.setStyle("-fx-background-color: #F0F5D8; -fx-text-fill: #4F5D2F;" + "-fx-background-radius: 8; -fx-padding: 4 10; -fx-cursor: hand;" + "-fx-font-size: 12px;");
        btn.selectedProperty().addListener((obs, old, sel) -> {
            if (sel)
                btn.setStyle("-fx-background-color: #DDEB9D; -fx-text-fill: #4F5D2F;" + "-fx-background-radius: 8; -fx-padding: 4 10; -fx-cursor: hand;" + "-fx-font-size: 12px; -fx-font-weight: bold;");
            else
                btn.setStyle("-fx-background-color: #F0F5D8; -fx-text-fill: #4F5D2F;" + "-fx-background-radius: 8; -fx-padding: 4 10; -fx-cursor: hand;" + "-fx-font-size: 12px;");
        });
        return btn;
    }

    // ЦЕНТРАЛЬНА ПАНЕЛЬ — конструктор

    private VBox buildBuilderPanel() {
        Label title = new Label("Новий рецепт");
        title.getStyleClass().add("title-label");

        nameField.setPromptText("Назва рецепту...");
        nameField.getStyleClass().add("name-field");
        nameField.setPrefWidth(280);

        originBox.getItems().addAll(CoffeeConstants.ORIGIN_MAP.keySet());
        originBox.setValue("Ефіопія");
        originBox.getStyleClass().add("combo-box");
        originBox.setPrefWidth(200);
        originBox.setOnAction(e -> updatePreview());

        tempBox.getItems().addAll(CoffeeConstants.TEMP_MAP.keySet());
        tempBox.setValue("Гаряча");
        tempBox.getStyleClass().add("combo-box");
        tempBox.setPrefWidth(200);
        tempBox.setOnAction(e -> updatePreview());

        Label brewLabel = new Label("Спосіб приготування");
        brewLabel.getStyleClass().add("small-label");

        coffeeInfoLabel.getStyleClass().add("small-label");
        coffeeInfoLabel.setStyle("-fx-text-fill: #708240;");
        updateCoffeeInfo();

        VBox brewBox = new VBox(6);
        for (String brewName : CoffeeConstants.BREW_MAP.keySet()) {
            RadioButton rb = new RadioButton(brewName);
            rb.setToggleGroup(brewGroup);
            rb.setStyle("-fx-text-fill: #4F5D2F; -fx-font-size: 13px; -fx-cursor: hand;");
            if (brewName.equals("Еспресо")) rb.setSelected(true);
            rb.setOnAction(e -> {
                updateCoffeeInfo();
                updatePreview();
            });
            brewBox.getChildren().add(rb);
        }

        GridPane settings = new GridPane();
        settings.setHgap(12);
        settings.setVgap(10);
        settings.addRow(0, styledLabel("Зерно:"), originBox);
        settings.addRow(1, styledLabel("Температура:"), tempBox);

        Label selectedTitle = new Label("Вибрані інгредієнти");
        selectedTitle.getStyleClass().add("subtitle-label");

        ScrollPane selectedScroll = new ScrollPane(selectedList);
        selectedScroll.setFitToWidth(true);
        selectedScroll.setPrefHeight(200);
        selectedScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Button saveBtn = new Button("Зберегти рецепт");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setOnAction(e -> saveRecipe());

        VBox panel = new VBox(14, title, nameField, settings, brewLabel, brewBox, coffeeInfoLabel, selectedTitle, selectedScroll, saveBtn);
        panel.setPadding(new Insets(0, 16, 0, 0));
        panel.setPrefWidth(320);
        return panel;
    }

    private void updateCoffeeInfo() {
        String brewKey = getSelectedBrew();
        double volume = CoffeeConstants.BREW_VOLUME.getOrDefault(brewKey, 50.0);
        coffeeInfoLabel.setText(String.format("Кава додається автоматично — %.0f мл", volume));
    }

    private Recipe buildRecipe(String name) {
        List<RecipeIngredient> items = new ArrayList<>();

        // кава — автоматично
        String brewKey = getSelectedBrew();
        String coffeeIngKey = CoffeeConstants.BREW_INGREDIENT.getOrDefault(brewKey, "espresso");
        double coffeeVolume = CoffeeConstants.BREW_VOLUME.getOrDefault(brewKey, 50.0);
        Ingredient coffeeIng = IngredientRepository.get(coffeeIngKey);
        if (coffeeIng != null) {
            items.add(new RecipeIngredient(coffeeIng, coffeeVolume));
        }

        // решта інгредієнтів
        for (Map.Entry<String, Double> entry : selected.entrySet()) {
            Ingredient ing = IngredientRepository.get(entry.getKey());
            if (ing != null) items.add(new RecipeIngredient(ing, entry.getValue()));
        }

        String originKey = CoffeeConstants.ORIGIN_MAP.getOrDefault(originBox.getValue(), "ethiopia");
        String tempKey = CoffeeConstants.TEMP_MAP.getOrDefault(tempBox.getValue(), "warm");

        return new Recipe(name, items, originKey, brewKey, tempKey);
    }

    private void updatePreview() {
        Recipe temp = buildRecipe("preview");
        if (temp == null) {
            radar.drawEmpty();
            balanceLabel.setText("");
            dominantLabel.setText("");
            spicyLabel.setVisible(false);
            spicyLabel.setManaged(false);
            return;
        }

        FlavorProfile profile = engine.calculate(temp);
        radar.draw(profile);

        dominantLabel.setText("Домінує: " + engine.dominantFlavor(profile));
        balanceLabel.setText(String.format("Баланс: %.2f", engine.balanceScore(profile)));

        spicyLabel.setVisible(profile.isSpicyAccent());
        spicyLabel.setManaged(profile.isSpicyAccent());

        double marketPrice = RecipeUtils.marketPrice(temp);
        double totalCal    = RecipeUtils.totalCalories(temp);
        costLabel.setText(String.format("~ %.0f ₴", marketPrice));
        caloriesLabel.setText(String.format("%.0f ккал", totalCal));
    }

    private VBox categoryListRef; // посилання на контейнер категорій

    private void saveRecipe() {
        String name = nameField.getText().trim();
        if (name.isBlank()) {
            nameField.setPromptText("⚠ Введіть назву!");
            nameField.setStyle("-fx-border-color: #e07b54; -fx-background-color: #FFF8F6;" +
                    "-fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 8 12;");
            return;
        }

        Recipe recipe = buildRecipe(name);
        if (recipe == null) return;

        RecipeUtils.finalize(recipe);

        User currentUser = UserRepository.getCurrentUser();
        recipe.setAuthor(currentUser);
        RecipeRepository.add(recipe);
        if (currentUser != null) currentUser.addRecipe(recipe);

        // скидаємо форму
        nameField.clear();
        nameField.setStyle("");
        selected.clear();
        refreshSelectedList();
        if (categoryListRef != null && groupedRef != null) {
            buildCategoryList(categoryListRef, CATEGORIES, CATEGORY_NAMES, groupedRef, "");
        }
        radar.drawEmpty();
        balanceLabel.setText("");
        dominantLabel.setText("");
        spicyLabel.setVisible(false);
        spicyLabel.setManaged(false);
        costLabel.setText("");
        caloriesLabel.setText("");
    }
    private String getSelectedBrew() {
        Toggle selected = brewGroup.getSelectedToggle();
        if (selected == null) return "espresso";
        String label = ((RadioButton) selected).getText();
        return CoffeeConstants.BREW_MAP.getOrDefault(label, "espresso");
    }

    private Label styledLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("small-label");
        return l;
    }

}