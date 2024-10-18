package it.fostidich.caster;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Region extends Application {

    private String[][] region;
    public static String firstRegion;

    public static void run() {
        launch();
    }

    @Override
    public void init() {
        // Check that the first region has been previously defined
        if (firstRegion.isEmpty()) {
            System.err.println("First region has not been defined");
            System.exit(1);
        }

        // Populate region matrix from json file of the first region
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(
                        Main.class.getResourceAsStream(
                                "/regions/" + firstRegion + ".json")))) {
            Gson gson = new Gson();
            region = gson.fromJson(reader, String[][].class);
        } catch (Exception ignored) {
            System.err.println("Error while opening region file");
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Populate a grid pane with the tile textures provided in the region matrix
        GridPane gridPane = new GridPane();

        for (int y = 0; y < region.length; y++)
            for (int x = 0; x < region[0].length; x++)
                gridPane.add(
                        getTileImage(region[x][y]),
                        y,
                        x
                );

        // Center the region in the window
        gridPane.setAlignment(Pos.CENTER);

        // Define window size
        Scene scene = new Scene(gridPane, 800, 800);

        // Spawn window
        primaryStage.setTitle("Caster");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView getTileImage(String tile) {
        Image image = new Image(
                Objects.requireNonNull(
                        Main.class.getResourceAsStream(
                                "/tiles/" + tile + ".png")));
        return new ImageView(image);
    }
}
