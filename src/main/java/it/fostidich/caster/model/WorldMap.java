package it.fostidich.caster.model;

import java.io.Serializable;

public class WorldMap implements Serializable {

    private int width;

    private int height;

    private Tile[][] world;

    public WorldMap() {}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int x, int y) {
        return world[x][y];
    }

    private void setTile(int x, int y, Tile tile) {
        world[x][y] = tile;
    }
}
