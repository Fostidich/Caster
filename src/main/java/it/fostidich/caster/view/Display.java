package it.fostidich.caster.view;

import it.fostidich.caster.controller.Main;
import it.fostidich.caster.model.Player;
import it.fostidich.caster.model.WorldMap;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.function.Consumer;

public class Display {

    private Actions actions;

    private Consumer<Event> consumer;

    private WorldMap worldMap;

    private Player player;

    public Display() {
        consumer = null;
        worldMap = null;
        player = null;
    }

    public void render() {
        if (consumer == null) {
            System.err.println("Consumer was not set before rendering");
            System.exit(1);
        }
        if (worldMap == null) {
            System.err.println("World map was not set before rendering");
            System.exit(1);
        }
        if (player == null) {
            System.err.println("Player was not set before rendering");
            System.exit(1);
        }
    }

    public Actions getActions() {
        return actions;
    }

    public void setConsumer(Consumer<Event> consumer) {
        this.consumer = consumer;
    }

    public void plotWorldMap(String worldMapPath) {
        try (
            InputStream inputStream = Main.class.getResourceAsStream(worldMapPath);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            worldMap = (WorldMap) objectInputStream.readObject();
        } catch (Exception ignored) {
            System.err.println("Unable to load world map: " + worldMapPath);
            System.exit(1);
        }
    }

    public void plotPlayer(String playerPath) {
        try (
            InputStream inputStream = Main.class.getResourceAsStream(playerPath);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            player = (Player) objectInputStream.readObject();
        } catch (Exception ignored) {
            System.err.println("Unable to load player: " + playerPath);
            System.exit(1);
        }
    }
}
