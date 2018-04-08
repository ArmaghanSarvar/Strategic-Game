package Game.entities;

import Game.Players.Team;
import Game.consts.Constants;
import Game.mapworks.Map;
import Game.pathfinding.A_star;
import Game.pathfinding.Coordinates;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Armaghan on 7/4/2017.
 */
public abstract class Entity {
    protected int x, y, i, j, health;
    protected ImageIcon imageIcon;
    protected Team team;
    protected int takenTiles;
    protected boolean moving;
    protected Map map;
    protected int xInTile = 0, yInTile = 0;
    public int entityID;

    public Entity(int i, int j, Team team) {
        this.i = i;
        this.j = j;
        this.team = team;
        takenTiles = 1;
        map = team.getMap();
        map.getTiles()[i][j].setWalkableOn(false);
    }

    public void updateXY(int startX, int startY, int sizeX, int sizeY, int tileSize) {
        if (isInSight(startX, startY, sizeX, sizeY)) {
            x = tileSize * (i - startX) + xInTile;
            y = tileSize * (j - startY) + yInTile;
        }
    }

    protected Coordinates getPointsAround(Coordinates destiation) {
        if (map.getTiles()[destiation.i][destiation.j].getElement() == Constants.CASTLE)
            return getPointsAroundCastle(destiation);

        if (Constants.isValid(destiation.i - 1, destiation.j - 1))
            if (map.getTiles()[destiation.i - 1][destiation.j - 1].isWalkableOn() &&
                    !map.getTiles()[destiation.i - 1][destiation.j - 1].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i - 1, destiation.j - 1);

        if (Constants.isValid(destiation.i - 1, destiation.j))
            if (map.getTiles()[destiation.i - 1][destiation.j].isWalkableOn() &&
                    !map.getTiles()[destiation.i - 1][destiation.j].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j).isEmpty()))
                return new Coordinates(destiation.i - 1, destiation.j);

        if (Constants.isValid(destiation.i, destiation.j - 1))
            if (map.getTiles()[destiation.i][destiation.j - 1].isWalkableOn() &&
                    !map.getTiles()[destiation.i][destiation.j - 1].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i, destiation.j - 1);

        if (Constants.isValid(destiation.i + 1, destiation.j))
            if (map.getTiles()[destiation.i + 1][destiation.j].isWalkableOn() &&
                    map.getTiles()[destiation.i + 1][destiation.j].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 1, destiation.j).isEmpty()))
                return new Coordinates(destiation.i + 1, destiation.j);

        if (Constants.isValid(destiation.i + 1, destiation.j + 1))
            if (map.getTiles()[destiation.i + 1][destiation.j + 1].isWalkableOn() &&
                    !map.getTiles()[destiation.i + 1][destiation.j + 1].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 1, destiation.j + 1).isEmpty()))
                return new Coordinates(destiation.i + 1, destiation.j + 1);

        if (Constants.isValid(destiation.i, destiation.j + 1))
            if (map.getTiles()[destiation.i][destiation.j + 1].isWalkableOn() &&
                    !map.getTiles()[destiation.i][destiation.j + 1].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i, destiation.j + 1).isEmpty()))
                return new Coordinates(destiation.i, destiation.j + 1);

        if (Constants.isValid(destiation.i - 1, destiation.j + 1))
            if (map.getTiles()[destiation.i - 1][destiation.j + 1].isWalkableOn() &&
                    (map.getTiles()[destiation.i - 1][destiation.j + 1].isBeingMovedTo() &&
                            !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j + 1).isEmpty())))
                return new Coordinates(destiation.i - 1, destiation.j + 1);

        if (Constants.isValid(destiation.i + 1, destiation.j - 1))
            if (map.getTiles()[destiation.i + 1][destiation.j - 1].isWalkableOn() &&
                    !map.getTiles()[destiation.i + 1][destiation.j - 1].isBeingMovedTo() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 1, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i + 1, destiation.j - 1);

        return null;

    }

    public Coordinates getPointsAroundCastle(Coordinates destiation) {
        if (Constants.isValid(destiation.i - 1, destiation.j - 1))
            if (map.getTiles()[destiation.i - 1][destiation.j - 1].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i - 1, destiation.j - 1);

        if (Constants.isValid(destiation.i - 1, destiation.j))
            if (map.getTiles()[destiation.i - 1][destiation.j].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j).isEmpty()))
                return new Coordinates(destiation.i - 1, destiation.j);

        if (Constants.isValid(destiation.i, destiation.j - 1))
            if (map.getTiles()[destiation.i][destiation.j - 1].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i, destiation.j - 1);

        if (Constants.isValid(destiation.i + 1, destiation.j - 1))
            if (map.getTiles()[destiation.i + 1][destiation.j - 1].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 1, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i + 1, destiation.j - 1);

        if (Constants.isValid(destiation.i - 1, destiation.j + 1))
            if (map.getTiles()[destiation.i - 1][destiation.j + 1].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j + 1).isEmpty()))
                return new Coordinates(destiation.i - 1, destiation.j + 1);

        if (Constants.isValid(destiation.i + 2, destiation.j))
            if (map.getTiles()[destiation.i + 2][destiation.j].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 2, destiation.j).isEmpty()))
                return new Coordinates(destiation.i + 2, destiation.j);

        if (Constants.isValid(destiation.i + 2, destiation.j + 2))
            if (map.getTiles()[destiation.i + 2][destiation.j + 2].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 2, destiation.j + 2).isEmpty()))
                return new Coordinates(destiation.i + 2, destiation.j + 2);

        if (Constants.isValid(destiation.i + 2, destiation.j + 1))
            if (map.getTiles()[destiation.i + 2][destiation.j + 1].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 2, destiation.j + 1).isEmpty()))
                return new Coordinates(destiation.i + 2, destiation.j + 1);

        if (Constants.isValid(destiation.i, destiation.j + 2))
            if (map.getTiles()[destiation.i][destiation.j + 2].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i, destiation.j + 2).isEmpty()))
                return new Coordinates(destiation.i, destiation.j + 2);

        if (Constants.isValid(destiation.i + 1, destiation.j + 2))
            if (map.getTiles()[destiation.i + 1][destiation.j + 2].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 1, destiation.j + 2).isEmpty()))
                return new Coordinates(destiation.i + 1, destiation.j + 2);

        if (Constants.isValid(destiation.i + 2, destiation.j - 1))
            if (map.getTiles()[destiation.i + 2][destiation.j - 1].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i + 2, destiation.j - 1).isEmpty()))
                return new Coordinates(destiation.i + 2, destiation.j - 1);

        if (Constants.isValid(destiation.i - 1, destiation.j + 2))
            if (map.getTiles()[destiation.i - 1][destiation.j + 2].isWalkableOn() &&
                    !(A_star.findPath(map, this.i, this.j, destiation.i - 1, destiation.j + 2).isEmpty()))
                return new Coordinates(destiation.i - 1, destiation.j + 2);

        return null;

    }


    public Coordinates getSailablePointsAround(Coordinates destination) {
        int m = 1;

        while (m < 3) {
            for (int i = destination.i - m; i < destination.i + m; i++)
                if (Constants.isValid(i, destination.j - m) && map.getTiles()[i][destination.j - m].isSailableOn()) {
                    return new Coordinates(i, destination.j - m);
                }

            for (int j = destination.j - m; j < destination.j + m; j++)
                if (Constants.isValid(destination.i + m, j) && map.getTiles()[destination.i + m][j].isSailableOn()) {
                    return new Coordinates(destination.i + m, j);
                }

            for (int i = destination.i + m + 1; i > destination.i - m; i--)
                if (Constants.isValid(i, destination.j + m) && map.getTiles()[i][destination.j + m].isSailableOn()) {
                    return new Coordinates(i, destination.j + m);
                }

            for (int j = destination.j + m; j > destination.j - m; j--)
                if (Constants.isValid(destination.i - m, j) && map.getTiles()[destination.i - m][j].isSailableOn()) {
                    return new Coordinates(destination.i - m, j);
                }
            m++;
        }
        return null;
    }

    public boolean isInSight(int startX, int startY, int sizeX, int sizeY) {
        return i + takenTiles - 1 >= startX && i < startX + sizeX && j + takenTiles - 1 >= startY && j < startY + sizeY;
    }

    public int getTakenTiles() {
        return takenTiles;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getHealth() {
        return health;
    }

    public Image getImage() {
        return imageIcon.getImage();
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setImage(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getxInTile() {
        return xInTile;
    }

    public int getyInTile() {
        return yInTile;
    }

    public Team getTeam() {
        return team;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected Map getMap() {
        return map;
    }
}