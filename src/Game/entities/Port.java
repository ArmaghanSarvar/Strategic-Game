package Game.entities;

import Game.Players.Enemy;
import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;
import Game.pathfinding.Coordinates;

import javax.swing.*;

import static Game.entities.Entities.totalEntities;

/**
 * Created by Armaghan on 7/8/2017.
 */
public class Port extends Entity implements Runnable {
    private int numOfFishingShip =0;

    public Port(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.PORT_ID;
        if (team instanceof Player&& map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        if (team instanceof Player && map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        map.getTiles()[i][j].setWalkableOn(false);
        this.setImage(new ImageIcon("R/loadingport.png"));
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setImage(new ImageIcon("R/port.png"));

        if (team instanceof Enemy && !map.multiPlayer) {
            Coordinates fishingShipCo;


            while (true) {
                fishingShipCo = findShipCoordinates();
                if (team.getWoods() >= 2000 && team.getGolds() >= 250) {
                    if (fishingShipCo != null && numOfFishingShip < 6) {
                        totalEntities.add(new FishingShip(fishingShipCo.i, fishingShipCo.j, getTeam()));
                        team.reduceGolds(250);
                        team.reduceWoods(2000);
                        numOfFishingShip++;
                    }
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Coordinates findShipCoordinates() {
        int m = 1;

        while (m < 8) {
            for (int i = this.i - m; i < this.i + m; i++)
                if (Constants.isValid(i, this.j - m) && map.getTiles()[i][this.j - m].isSailableOn()) {
                    map.getTiles()[i][this.j - m].setSailableOn(false);
                    return new Coordinates(i, this.j - m);
                }

            for (int j = this.j - m; j < this.j + m; j++)
                if (Constants.isValid(this.i + m, j) && map.getTiles()[this.i + m][j].isSailableOn()) {
                    map.getTiles()[this.i + m][j].setSailableOn(false);
                    return new Coordinates(this.i + m, j);

                }

            for (int i = this.i + m; i > this.i - m; i--)
                if (Constants.isValid(i, this.j + m) && map.getTiles()[i][this.j + m].isSailableOn()) {
                    map.getTiles()[i][this.j + m].setSailableOn(false);
                    return new Coordinates(i, this.j + m);
                }

            for (int j = this.j + m; j > this.j - m; j--)
                if (Constants.isValid(this.i - m, j) && map.getTiles()[this.i - m][j].isSailableOn()) {
                    map.getTiles()[this.i - m][j].setSailableOn(false);
                    return new Coordinates(this.i - m, j);
                }

            m++;
        }
        return null;
    }
}
