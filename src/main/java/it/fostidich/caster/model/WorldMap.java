package it.fostidich.caster.model;

import java.util.Map;

public class WorldMap {

    private int width;
    private int height;
    private Map<Coordinates, Tile> world;

    public WorldMap(String path) {}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Coordinates, Tile> getWorld() {
        return world;
    }
}
