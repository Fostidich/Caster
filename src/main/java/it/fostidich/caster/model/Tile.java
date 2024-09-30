package it.fostidich.caster.model;

public class Tile {

    private String texture;
    private String icon;
    private ActionType actionType;
    private ActionTag[] actionTags;

    public Tile() {}

    public String getTexture() {
        return texture;
    }

    public String getIcon() {
        return icon;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public ActionTag[] getActionTags() {
        return actionTags;
    }
}
