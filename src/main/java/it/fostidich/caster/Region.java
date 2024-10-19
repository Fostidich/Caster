package it.fostidich.caster;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Region extends Application {

    public static String firstRegion;
    public static String firstPlayer;
    private ImageView[][] regionView;
    private ImageView playerView;

    private double imageX = 0;
    private double imageY = 0;
    private double step = 32;

    public static void run() {
        launch();
    }

    @Override
    public void init() {
        // TODO resources folder are to be made editable constant
        // TODO clean up variable presence checks with a new function
        // Check that the first region has been previously defined
        if (firstRegion.isEmpty()) {
            System.err.println("First region has not been defined: " + firstRegion);
            System.exit(1);
        }

        // Check that the first player has been previously defined
        if (firstPlayer.isEmpty()) {
            System.err.println("First player has not been defined: " + firstPlayer);
            System.exit(1);
        }

        // TODO clean up json reading with a new function
        // Retrieve the tiles string descriptors from json file
        String[][] tileDescriptors = null;
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(
                        Main.class.getResourceAsStream("/regions/" + firstRegion + ".json")))) {
            Gson gson = new Gson();
            tileDescriptors = gson.fromJson(reader, String[][].class);
        } catch (Exception ignored) {
            System.err.println("Error while opening region file: " + firstRegion);
            System.exit(1);
        }

        // Get image view for the tiles
        regionView = new ImageView[tileDescriptors.length][tileDescriptors[0].length];
        for (int y = 0; y < tileDescriptors.length; y++)
            for (int x = 0; x < tileDescriptors[0].length; x++)
                regionView[x][y] = getImageView("/tiles/" + tileDescriptors[x][y] + ".png");

        // Get image view for the player
        playerView = getImageView("/players/" + firstPlayer + ".png");
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
        Scene scene = new Scene(stackPane, 1920, 1080);
        resizeImages(scene);

        // Set up listeners for tile resizing
        scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeImages(scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeImages(scene));

        // Move with WASD keys
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            // Handle Ctrl+Q to quit the application
            if (event.isControlDown() && event.getCode() == KeyCode.Q) {
                System.out.println("Closing application window");
                Platform.exit();
                return;
            }

            // Update player position
            Consumer<String> updatePosition = (key) -> {
                System.out.println("Pressed key: " + key + " - " + "Pixel coordinates: (" + imageX + ", " + imageY + ")");
                playerView.setTranslateX(imageX);
                playerView.setTranslateY(imageY);
            };

            // Handle player movement
            switch (code) {
                case W:
                    imageY -= step;
                    updatePosition.accept("W");
                    break;
                case S:
                    imageY += step;
                    updatePosition.accept("S");
                    break;
                case A:
                    imageX -= step;
                    updatePosition.accept("A");
                    break;
                case D:
                    imageX += step;
                    updatePosition.accept("D");
                    break;
            }
        });

        // TODO we need a way to access resources more efficiently
        // TODO resources folder are to be made editable constant
        // Provide css to scene
        scene.getStylesheets().add(
                Objects.requireNonNull(
                                Main.class.getResource("/css/style.css"))
                        .toExternalForm());

        // Spawn window
        stage.setTitle("Caster");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    private void resizeImages(Scene scene) {
        // TODO make this value constants and customizable
        double widthFraction = 4;
        double heightFraction = 4;
        double minWidth = 32;
        double minHeight = 32;
        double maxWidth = 512;
        double maxHeight = 512;

        double windowWidth = scene.getWidth();
        double windowHeight = scene.getHeight();
        double imageWidth = windowWidth / widthFraction;
        double imageHeight = windowHeight / heightFraction;

        // Don't resize if limits are reached
        if (imageWidth < minWidth || imageHeight < minHeight ||
                imageWidth > maxWidth || imageHeight > maxHeight) return;

        // Update step size
        step = imageWidth * 2;

        // Resize each tile image
        for (ImageView[] line : regionView) {
            for (ImageView tileView : line) {
                tileView.setFitWidth(imageWidth);
                tileView.setFitHeight(imageHeight);
                tileView.setPreserveRatio(true);
            }
        }

        // Resize player icon
        playerView.setFitWidth(imageWidth);
        playerView.setFitHeight(imageHeight);
        playerView.setPreserveRatio(true);
    }

    /**
     * Build the ImageView object from the path of the image resource.
     *
     * @param path It is the full path of the image from the resources root folder
     * @return The ImageView object
     */
    private ImageView getImageView(String path) {
        // TODO manage file absence
        Image image = new Image(
                Objects.requireNonNull(
                        Main.class.getResourceAsStream(path)));
        return new ImageView(image);
    }
}
