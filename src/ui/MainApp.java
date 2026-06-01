package ui;

import data.SampleData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.tab1.RecipeListView;
import ui.tab2.RecipeBuilderView;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Font.loadFont(
                getClass().getResourceAsStream("/TenorSans-Regular.ttf"), 14
        );

        SampleData.load();

        RecipeListView recipeListView = new RecipeListView();
        RecipeBuilderView builderView = new RecipeBuilderView();

        Tab tab1 = new Tab("Меню", recipeListView);
        Tab tab2 = new Tab("Рецепт", builderView);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(tab1, tab2);

        // оновлення при перемиканні вкладок
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tab1) recipeListView.refresh();
        });

        Scene scene = new Scene(tabPane, 900, 700);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("FlavorLab");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}