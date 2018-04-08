package Game.mapworks;

import Game.consts.Constants;

/**
 * Created by Armaghan on 7/3/2017.
 */

public class Tile {
    private int type, element;
    private String mapPic = "";
    private String elementPic= "";
    private boolean walkableOn, sailableOn, beach, beingMovedTo;

    public void setType(int type) {
        this.type = type;
        if(this.type == Constants.LAND )
        {
            sailableOn= false;
            walkableOn= true;
        }
        else if(this.type == Constants.DEEP_WATER || this.type == Constants.SHALLOW_WATER){
            sailableOn= true;
            walkableOn= false;
        }
    }

    public void setElement(int element) {
        this.element = element;
        if(this.element == Constants.GOLD_MINE || this.element== Constants.TREE || this.element == Constants.CASTLE){
            walkableOn = false;
        }
    }

    public void setMapPic(String picName) {
        this.mapPic = picName;
        if (picName.contains("0"))
            beach = true;
    }

    public void setElementPic(String elementPic) {
        this.elementPic = elementPic;
    }

    public String getElementPic() {
        return elementPic;
    }

    public String getMapPic() {
        return mapPic;
    }
    public int getType() {
        return type;
    }
    public int getElement() {
        return element;
    }

    public boolean isWalkableOn() {
        return walkableOn;
    }

    public boolean isSailableOn() {
        return sailableOn;
    }

    public void setWalkableOn(boolean walkableOn) {
        this.walkableOn = walkableOn;
    }

    public boolean isBeach() {
        return beach;
    }

    public void setSailableOn(boolean sailableOn) {
        this.sailableOn = sailableOn;
    }

    public boolean isBeingMovedTo() {
        return beingMovedTo;
    }

    public void setBeingMovedTo(boolean beingMovedTo) {
        this.beingMovedTo = beingMovedTo;
    }
}
