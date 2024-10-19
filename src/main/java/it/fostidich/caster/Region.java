package it.fostidich.caster;

import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static it.fostidich.caster.Errors.NonNegativeValue;
import static it.fostidich.caster.Errors.UndefinedVariable;
import static it.fostidich.caster.Resources.*;

public class Region extends Application {

    public static int sideFraction = 6;
    public static int minWindowSide = 400;
    public static String firstRegion;
    public static String firstPlayer;

    private ImageView[][] regionView;
    private ImageView playerView;
    private int windowHeight = 1080;
    private int windowWidth = 1920;
    private double imageX = 0;
    private double imageY = 0;
    private int step = 32;

    public static void run() {
        launch();
    }

    @Override
    public void init() {
        // Initial check assertions
        NonNegativeValue.abort(sideFraction < 0, "sideFraction set to " + sideFraction);
        NonNegativeValue.abort(minWindowSide < 0, "minWindowSide set to " + minWindowSide);
        UndefinedVariable.abort(firstRegion.isEmpty(), "firstRegion is empty");
        UndefinedVariable.abort(firstPlayer.isEmpty(), "firstPlayer is empty");

        // Retrieve the tiles string descriptors from json file
        String[][] tileDescriptors = Json.fromJsonFile(String[][].class, "/regions/" + firstRegion + ".json");

        // Get an image view for each tile
        regionView = new ImageView[tileDescriptors.length][tileDescriptors[0].length];
        for (int y = 0; y < tileDescriptors.length; y++)
            for (int x = 0; x < tileDescriptors[0].length; x++)
                regionView[x][y] = getTileResource(tileDescriptors[x][y]);

        // Get image view for the player
        playerView = getPlayerResource(firstPlayer);
    }

    @Override
    public void start(Stage stage) {
        // Create and center the region in the window
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // Populate a grid pane with the tile textures
        for (int y = 0; y < regionView.length; y++)
            for (int x = 0; x < regionView[0].length; x++)
                gridPane.add(regionView[x][y], y, x);

        // Player initial location
        playerView.setTranslateX(imageX);
        playerView.setTranslateY(imageY);

        // Create a stack pane to layer the player over the region
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(gridPane, playerView);
        stackPane.requestFocus();

        // Create scene and set window size and set cell size
        Scene scene = new Scene(stackPane, windowWidth, windowHeight);
        resizeTiles();

        // Set up listeners for tile resizing
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            windowWidth = newVal.intValue();
            resizeTiles();
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            windowHeight = newVal.intValue();
            resizeTiles();
        });

        // Manage key pressing actions
        scene.setOnKeyPressed(this::keyPressHandler);

        // Provide css to scene
        scene.getStylesheets().add(getCssResource("style"));

        // Spawn window
        stage.setTitle("Caster");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    private void resizeTiles() {
        // Update step size
        step = resizedTileSide();

        // Resize each tile image
        for (ImageView[] line : regionView) {
            for (ImageView tileView : line) {
                tileView.setFitWidth(step);
                tileView.setPreserveRatio(true);
            }
        }

        // Resize player icon
        playerView.setFitWidth(step);
        playerView.setPreserveRatio(true);
    }

    private int resizedTileSide() {
        // Measure new size based on limits
        int tileSide;
        if (windowHeight < minWindowSide || windowWidth < minWindowSide)
            tileSide = minWindowSide / sideFraction;
        else if (windowWidth < windowHeight)
            tileSide = windowWidth / sideFraction;
        else
            tileSide = windowHeight / sideFraction;
        return tileSide;
    }

    private void keyPressHandler(KeyEvent event) {
        KeyCode code = event.getCode();

        // Handle Ctrl+Q to quit the application
        if (event.isControlDown() && event.getCode() == KeyCode.Q) {
            System.out.println("Pressed keys: Ctrl+Q - Closing application window");
            Platform.exit();
            return;
        }

        // Update player position
        Consumer<KeyCode> updatePosition = (key) -> {
            System.out.println("Pressed key: " + key + " - " + "Pixel coordinates: (" + imageX + ", " + imageY + ")");
            playerView.setTranslateX(imageX);
            playerView.setTranslateY(imageY);
        };

        // Handle player movement due to WASD keys pressed
        switch (code) {
            case W:
                imageY -= step;
                updatePosition.accept(code);
                break;
            case S:
                imageY += step;
                updatePosition.accept(code);
                break;
            case A:
                imageX -= step;
                updatePosition.accept(code);
                break;
            case D:
                imageX += step;
                updatePosition.accept(code);
                break;
        }
    }
}
