package Game.Players;

import Game.consts.Constants;
import Game.entities.Castle;
import Game.entities.Human;
import Game.mapworks.Map;

import javax.swing.*;

import static Game.entities.Entities.totalEntities;

/**
 * Created by Armaghan on 7/4/2017.
 */
public abstract class Team {

    protected int humans, woods, golds, fishes;
    protected boolean hasPort, hasBarrack, hasQuarry;
    public int castleI, castleJ, barracksI, barracksJ, quarryI, quarryJ, portI, portJ;
    protected Map map;
    public String clientName;

    public Team(Map map) {
        this.map = map;
        try {
            clientName = map.gameClient.clientName;
        } catch (NullPointerException e) {

        }
    }

    public void setCastleIJ(int castleI, int castleJ) {
        this.castleI = castleI;
        this.castleJ = castleJ;
        totalEntities.add(new Castle(castleI, castleJ, this));
        this.setHumans();
    }

    private void setHumans() {
        int c = 0, m = 1;
        while (c < 10) {
            for (int i = castleI - m; i < castleI + m + 1; i++) {
                if (Constants.isValid(i, castleJ - m) && c < 10 && map.getTiles()[i][castleJ - m].isWalkableOn()) {
                    map.getTiles()[i][castleJ - m].setWalkableOn(false);
                    totalEntities.add(new Human(i, castleJ - m, this));
                    c++;
                }
            }
            for (int j = castleJ - m; j < castleJ + m + 1; j++) {
                if (Constants.isValid(castleI + m + 1, j) && c < 10 && map.getTiles()[castleI + m + 1][j].isWalkableOn()) {
                    map.getTiles()[castleI + m + 1][j].setWalkableOn(false);
                    totalEntities.add(new Human(castleI + m + 1, j, this));
                    c++;
                }
            }
            for (int i = castleI + m + 1; i > castleI - m; i--) {
                if (Constants.isValid(i, castleJ + m + 1) && c < 10 && map.getTiles()[i][castleJ + m + 1].isWalkableOn()) {
                    map.getTiles()[i][castleJ + m + 1].setWalkableOn(false);
                    totalEntities.add(new Human(i, castleJ + m + 1, this));
                    c++;
                }
            }
            for (int j = castleJ + m + 1; j > castleJ - m; j--) {
                if (Constants.isValid(castleI - m, j) && c < 10 && map.getTiles()[castleI - m][j].isWalkableOn()) {
                    map.getTiles()[castleI - m][j].setWalkableOn(false);
                    totalEntities.add(new Human(castleI - m, j, this));
                    c++;
                }
            }
            m++;
        }

        if (this instanceof Enemy) {
            new Thread((Enemy) this).start();
        }
    }

    public void setAHuman() {
        int m = 2;
        boolean set = false;
        while (m < 5) {
            for (int i = castleI - m; i < castleI + m + 1; i++) {
                if (Constants.isValid(i, castleJ - m) && map.getTiles()[i][castleJ - m].isWalkableOn() && !set) {
                    map.getTiles()[i][castleJ - m].setWalkableOn(false);
                    totalEntities.add(new Human(i, castleJ - m, this));
                    set = true;
                    break;
                }
            }
            for (int j = castleJ - m; j < castleJ + m + 1; j++) {
                if (Constants.isValid(castleI + m + 1, j) && map.getTiles()[castleI + m + 1][j].isWalkableOn() && !set) {
                    map.getTiles()[castleI + m + 1][j].setWalkableOn(false);
                    totalEntities.add(new Human(castleI + m + 1, j, this));
                    set = true;
                    break;
                }
            }
            for (int i = castleI + m + 1; i > castleI - m; i--) {
                if (Constants.isValid(i, castleJ + m + 1) && map.getTiles()[i][castleJ + m + 1].isWalkableOn() && !set) {
                    map.getTiles()[i][castleJ + m + 1].setWalkableOn(false);
                    totalEntities.add(new Human(i, castleJ + m + 1, this));
                    set = true;
                    break;
                }
            }
            for (int j = castleJ + m + 1; j > castleJ - m; j--) {
                if (Constants.isValid(castleI - m, j) && map.getTiles()[castleI - m][j].isWalkableOn() && !set) {
                    map.getTiles()[castleI - m][j].setWalkableOn(false);
                    totalEntities.add(new Human(castleI - m, j, this));
                    set = true;
                    break;
                }
            }
            m++;
        }
       JOptionPane.showMessageDialog(null, "Human is Made");
    }

    public void reduceWoods(int woods) {
        if (this.woods >= woods)
            this.woods -= woods;
    }


    public void reduceGolds(int golds) {
        if (this.golds >= golds)
            this.golds -= golds;
    }

    public void reduceFish(int fishes) {
        if (this.fishes >= fishes)
            this.fishes -= fishes;
    }

    public void addGold(int gold) {
        golds += gold;
    }

    public void addWood(int wood) {
        woods += wood;
    }

    public void setFish(int fish) {
        fishes += fish;
    }

    public void setHumans(int humans) {
        this.humans = humans;
    }

    public void setWoods(int woods) {
        this.woods = woods;
    }

    public void setGolds(int golds) {
        this.golds = golds;
    }

    public void setFishes(int fishes) {
        this.fishes = fishes;
    }

    public void setPort(boolean hasPort) {
        this.hasPort = hasPort;
    }

    public void setHasBarrack(boolean hasBarrack) {
        this.hasBarrack = hasBarrack;
    }

    public int getHumans() {
        return humans;
    }

    public int getWoods() {
        return woods;
    }

    public int getGolds() {
        return golds;
    }

    public int getFishes() {
        return fishes;
    }

    public boolean hasPort() {
        return hasPort;
    }

    public boolean isHasBarrack() {
        return hasBarrack;
    }

    public int getCastleI() {
        return castleI;
    }

    public int getCastleJ() {
        return castleJ;
    }

    public Map getMap() {
        return map;
    }

    public int getBarracksI() {
        return barracksI;
    }

    public int getBarracksJ() {
        return barracksJ;
    }

    public int getQuarryI() {
        return quarryI;
    }

    public int getQuarryJ() {
        return quarryJ;
    }

    public int getPortI() {
        return portI;
    }

    public int getPortJ() {
        return portJ;
    }


}
