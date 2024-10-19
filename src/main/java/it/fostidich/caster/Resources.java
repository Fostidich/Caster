package it.fostidich.caster;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.Objects;

import static it.fostidich.caster.Errors.ResourceNotFound;
import static it.fostidich.caster.Errors.UndefinedVariable;

public class Resources {

    public static InputStream getResource(String resourcePath) {
        UndefinedVariable.abort(resourcePath.isEmpty(), "resourcePath is empty");
        InputStream result = null;
        try {
            result = Objects.requireNonNull(Main.class.getResourceAsStream(resourcePath));
        } catch (Exception ignored) {
            ResourceNotFound.abort(resourcePath);
        }
        return result;
    }

    public static String getResourceURL(String resourcePath) {
        UndefinedVariable.abort(resourcePath.isEmpty(), "resourcePath is empty");
        String result = null;
        try {
            result = Objects.requireNonNull(Main.class.getResource(resourcePath)).toExternalForm();
        } catch (Exception ignored) {
            ResourceNotFound.abort(resourcePath);
        }
        return result;
    }

    public static String getCssResource(String filename) {
        return getResourceURL("/css/" + filename + ".css");
    }

    public static ImageView getTileResource(String filename) {
        Image image = new Image(getResource("/tiles/" + filename + ".png"));
        return new ImageView(image);
    }

    public static ImageView getPlayerResource(String filename) {
        Image image = new Image(getResource("/players/" + filename + ".png"));
        return new ImageView(image);
    }

}
