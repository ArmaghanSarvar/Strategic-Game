package Game.Players;

import Game.consts.Constants;
import Game.entities.Barrack;
import Game.entities.Port;
import Game.entities.Quarry;
import Game.entities.Soldier;
import Game.mapworks.Map;
import Game.pathfinding.Coordinates;

import javax.swing.*;

import static Game.entities.Entities.totalEntities;

/**
 * Created by Armaghan on 7/9/2017.
 */
public class Player extends Team {
    public Player(Map map) {
        super(map);
        woods = 3000;
        golds = 4000;
        fishes = 52000;
    }

    public Coordinates findShipCoordinates() {
        int m = 1;
        while (m < 3) {
            for (int i = this.portI - m; i < this.portI + m; i++)
                if (Constants.isValid(i, this.portJ - m) && map.getTiles()[i][this.portJ - m].isSailableOn()) {
                    map.getTiles()[i][this.portJ - m].setSailableOn(false);
                    return new Coordinates(i, this.portJ - m);
                }

            for (int j = this.portI - m; j < this.portJ + m; j++)
                if (Constants.isValid(this.portI + m, j) && map.getTiles()[this.portI + m][j].isSailableOn()) {
                    map.getTiles()[this.portI + m][j].setSailableOn(false);
                    return new Coordinates(this.portI + m, j);

                }

            for (int i = this.portI + m; i > this.portI - m; i--)
                if (Constants.isValid(i, this.portJ + m) && map.getTiles()[i][this.portJ + m].isSailableOn()) {
                    map.getTiles()[i][this.portJ + m].setSailableOn(false);
                    return new Coordinates(i, this.portJ + m);
                }

            for (int j = this.portJ + m; j > this.portJ - m; j--)
                if (Constants.isValid(this.portI - m, j) && map.getTiles()[this.portI - m][j].isSailableOn()) {
                    map.getTiles()[this.portI - m][j].setSailableOn(false);
                    return new Coordinates(this.portI - m, j);
                }

            m++;
        }
        return null;
    }

    public void setBarrack() {
        int m = 2;
        boolean set = false;
        while (m < 5) {
            for (int i = castleI - m; i < castleI + m; i++) {
                if (Constants.isValid(i, castleJ - m) && map.getTiles()[i][castleJ - m].isWalkableOn() && !set && !map.getTiles()[i][castleJ - m].isBeach()) {
                    map.getTiles()[i][castleJ - m].setWalkableOn(false);
                    totalEntities.add(new Barrack(i, castleJ - m, this));
                    this.barracksI = i;
                    this.barracksJ = castleJ - m;
                    set = true;
                    break;
                }
            }
            for (int j = castleJ - m; j < castleJ + m; j++) {
                if (Constants.isValid(castleI + m, j) && map.getTiles()[castleI + m][j].isWalkableOn() && !set&& !map.getTiles()[castleI + m][j].isBeach()) {
                    map.getTiles()[castleI + m][j].setWalkableOn(false);
                    totalEntities.add(new Barrack(castleI + m, j, this));
                    this.barracksI = castleI + m;
                    this.barracksJ = j;
                    set = true;
                    break;
                }
            }
            for (int i = castleI + m; i > castleI - m; i--) {
                if (Constants.isValid(i, castleJ + m) && map.getTiles()[i][castleJ + m].isWalkableOn() && !set && !map.getTiles()[i][castleJ + m].isBeach()) {
                    map.getTiles()[i][castleJ + m].setWalkableOn(false);
                    totalEntities.add(new Barrack(i, castleJ + m, this));
                    this.barracksI = i;
                    this.barracksJ = castleJ + m;
                    set = true;
                    break;
                }
            }
            for (int j = castleJ + m; j > castleJ - m; j--) {
                if (Constants.isValid(castleI - m, j) && map.getTiles()[castleI - m][j].isWalkableOn() && !set && !map.getTiles()[castleI - m][j].isBeach()) {
                    map.getTiles()[castleI - m][j].setWalkableOn(false);
                    totalEntities.add(new Barrack(castleI - m, j, this));
                    this.barracksI = castleI - m;
                    this.barracksJ = j;
                    set = true;
                    break;
                }
            }
            m++;
        }
//        JOptionPane.showMessageDialog(null, "human is set");
    }

    public boolean setBarrack(int i, int j){
        barracksI = i;
        barracksJ = j;
        if(map.getTiles()[barracksI][barracksJ].isWalkableOn() && !map.getTiles()[barracksI][barracksJ].isBeach()) {
            totalEntities.add(new Barrack(barracksI, barracksJ, this));
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "this ain't a valid place for barrack");
            return false;
        }
    }

    public boolean setPort(int i, int j){
        portI = i;
        portJ= j;
        if(map.getTiles()[portI][portJ].isBeach() && map.getTiles()[portI][portJ].isWalkableOn()) {
            totalEntities.add(new Port(portI, portJ, this));
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "this ain't a valid place for port");
            return false;
        }
    }

    public boolean setQuarry(int i, int j){
        quarryI = i;
        quarryJ = j;
        if(!map.getTiles()[quarryI][quarryJ].isBeach() && map.getTiles()[quarryI][quarryJ].isWalkableOn()) {
            totalEntities.add(new Quarry(quarryI, quarryJ, this));
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "this ain't a valid place for quarry");
            return false;
        }
    }

    public void setAPort() {
        int m = 3;
        boolean found = false;
        while (m < 9 && !found) {
            for (int i = castleI - m; i < castleI + m; i++) {
                if (Constants.isValid(i, castleJ - m) && map.getTiles()[i][castleJ - m].isWalkableOn() &&
                        map.getTiles()[i][castleJ - m].isBeach()) {
                    map.getTiles()[i][castleJ - m].setWalkableOn(false);
                    totalEntities.add(new Port(i, castleJ - m, this));
                    portI = i;
                    portJ = castleJ - m;
                    found = true;
                    break;
                }
            }
            for (int j = castleJ - m; j < castleJ + m; j++) {
                if (Constants.isValid(castleI + m, j) && map.getTiles()[castleI + m][j].isWalkableOn() &&
                        map.getTiles()[castleI + m][j].isBeach() && !found) {
                    map.getTiles()[castleI + m][j].setWalkableOn(false);
                    totalEntities.add(new Port(castleI + m, j, this));
                    portI = castleI + m;
                    portJ = j;
                    found = true;
                    break;
                }
            }
            for (int i = castleI + m; i > castleI - m; i--) {
                if (Constants.isValid(i, castleJ + m) && map.getTiles()[i][castleJ + m].isWalkableOn() &&
                        map.getTiles()[i][castleJ + m].isBeach() && !found) {
                    map.getTiles()[i][castleJ + m].setWalkableOn(false);
                    totalEntities.add(new Port(i, castleJ + m, this));
                    portI = i;
                    portJ = castleJ + m;
                    found = true;
                    break;
                }
            }
            for (int j = castleJ + m; j > castleJ - m; j--) {
                if (Constants.isValid(castleI - m, j) && map.getTiles()[castleI - m][j].isWalkableOn()
                        && map.getTiles()[castleI - m][j].isBeach() && !found) {
                    map.getTiles()[castleI - m][j].setWalkableOn(false);
                    totalEntities.add(new Port(castleI - m, j, this));
                    portI = castleI - m;
                    portJ = j;
                    found = true;
                    break;
                }
            }
            m++;
        }
    }

    public void setASoldier() {
        int m = 1 ;
        boolean set = false;
        while (m < 7) {
            for (int i = this.barracksI - m; i < this.barracksI + m; i++)
                if (Constants.isValid(i, this.barracksJ - m) && map.getTiles()[i][this.barracksJ - m].isWalkableOn() &&
                        !set) {
                    totalEntities.add(new Soldier(i, barracksJ - m, this));
                    map.getTiles()[i][this.barracksJ - m].setWalkableOn(false);
                    set = true;
                    break;
                }

            for (int j = this.barracksJ - m; j < this.barracksJ + m; j++)
                if (Constants.isValid(this.barracksI + m, j) && map.getTiles()[this.barracksI + m][j].isWalkableOn() && !set) {
                    totalEntities.add(new Soldier(barracksI + m, j, this));
                    map.getTiles()[this.barracksI + m][j].setWalkableOn(false);
                    set= true;
                    break;
                }

            for (int i = this.barracksI + m; i > this.barracksI - m; i--)
                if (Constants.isValid(i, this.barracksJ + m) && map.getTiles()[i][this.barracksJ + m].isWalkableOn() && !set) {
                    totalEntities.add(new Soldier(i, barracksJ + m, this));
                    map.getTiles()[i][this.barracksJ + m].setWalkableOn(false);

                    set= true;
                    break;
                }

            for (int j = this.barracksJ + m; j > this.barracksJ - m; j--)
                if (Constants.isValid(this.barracksI - m, j) && map.getTiles()[this.barracksI - m][j].isWalkableOn() && !set) {
                    totalEntities.add(new Soldier(barracksI - m, j, this));
                    map.getTiles()[this.barracksI - m][j].setWalkableOn(false);
                    set= true;
                    break;
                }
            m++;
        }
        JOptionPane.showMessageDialog(null, "Soldier is Made");
    }
}