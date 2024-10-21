package it.fostidich.caster;

import java.util.EnumSet;

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
import static javafx.scene.input.KeyCode.*;

public class Region extends Application {

    public static int sideFraction = 8;
    public static int minWindowSide = 400;
    public static String firstRegion;
    public static String firstPlayer;

    private ImageView[][] regionView;
    private ImageView playerView;
    private int windowHeight = 1080;
    private int windowWidth = 1920;
    private int step = 32;
    private int playerPositionX = 0;
    private int playerPositionY = 0;

    public static void run() {
        launch();
    }

    @Override
    public void init() {
        // Initial check assertions
        NonNegativeValue.abort(sideFraction < 0, "side fraction set to " + sideFraction);
        NonNegativeValue.abort(minWindowSide < 0, "minimum window side set to " + minWindowSide);
        UndefinedVariable.abort(firstRegion.isEmpty(), "first region is empty");
        UndefinedVariable.abort(firstPlayer.isEmpty(), "first player is empty");

        // Retrieve the tiles string descriptors from json file
        String[][] tileDescriptors = Json.fromJsonFile(String[][].class, "/regions/" + firstRegion + ".json");

        System.out.println(tileDescriptors.length);

        for (int y = 0; y < tileDescriptors.length; y++) {
            for (int x = 0; x < tileDescriptors[0].length; x++)
                System.out.print(tileDescriptors[y][x] + "\t");
            System.out.println();
        }

        // Get an image view for each tile
        regionView = new ImageView[tileDescriptors[0].length][tileDescriptors.length];
        for (int y = 0; y < tileDescriptors.length; y++)
            for (int x = 0; x < tileDescriptors[0].length; x++)
                regionView[x][y] = getTileResource(tileDescriptors[y][x]);

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
                gridPane.add(regionView[x][y], x, y);

        // Player initial location
        playerView.setTranslateX(playerPositionX * step);
        playerView.setTranslateY(-playerPositionY * step);

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

    private void updatePosition() {
        // Compute cell offsets
        int offsetX = regionView[0].length % 2 == 0 ? step / 2 : 0;
        int offsetY = regionView.length % 2 == 0 ? step / 2 : 0;

        // Compute pixel positions
        playerView.setTranslateX(playerPositionX * step + offsetX);
        playerView.setTranslateY(-playerPositionY * step - offsetY);

        // Print coordinates on console
        System.out.println("(" + playerPositionX + ", " + playerPositionY + ")");
    }

    private void resizeTiles() {
        // Update step size
        step = resizedTileSide();

        // Resize each tile image
        for (ImageView[] line : regionView)
            for (ImageView tileView : line) {
                tileView.setFitWidth(step);
                tileView.setPreserveRatio(true);
            }

        // Resize player icon
        playerView.setFitWidth(step);
        playerView.setPreserveRatio(true);

        // Update player position
        updatePosition();
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
        return tileSide % 2 == 0 ? tileSide : tileSide + 1;
    }

    private void movePlayer(int x, int y) {
        // Region side lengths
        int xLen = regionView[0].length;
        int yLen = regionView.length;

        // Move player coordinates to positive matrix indices
        int newRelPosX = playerPositionX + x + xLen / 2;
        int newRelPosY = playerPositionY + y + yLen / 2;

        // Check that new position is inside limits
        if (newRelPosX < 0 || newRelPosY < 0 ||
                newRelPosX >= xLen || newRelPosY >= yLen) return;

        // Update positions
        playerPositionX += x;
        playerPositionY += y;
    }

    private void keyPressHandler(KeyEvent event) {
        KeyCode code = event.getCode();

        // Handle Ctrl+Q to quit the application
        if (event.isControlDown() && code == Q) {
            Platform.exit();
            return;
        }

        // Handle player movement due to WASD keys pressed
        if (EnumSet.of(W, A, S, D).contains(code)) {
            switch (code) {
                case W:
                    movePlayer(0, 1);
                    break;
                case S:
                    movePlayer(0, -1);
                    break;
                case A:
                    movePlayer(-1, 0);
                    break;
                case D:
                    movePlayer(1, 0);
                    break;
            }
            updatePosition();
        }

        // Print pressed key on console
        System.out.println("[" + code + "]");
    }
}
