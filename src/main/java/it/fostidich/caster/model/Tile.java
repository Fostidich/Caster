package it.fostidich.caster.model;

import java.io.Serializable;

public class Tile implements Serializable {

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
