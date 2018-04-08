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
public class Barrack extends Entity implements Runnable{
    private Coordinates soldierCoordinates;

    public Barrack(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.BARRACK_ID;
        if (team instanceof Player&& map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        map.getTiles()[i][j].setWalkableOn(false);
        this.setImage(new ImageIcon("R/loadingbarrack.png"));
        new Thread(this).start();
    }

    @Override
    public void run() {

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setImage(new ImageIcon("R/barracks.png"));

        if( team instanceof Enemy && !map.multiPlayer){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true){
                if(team.getGolds() >= 250 && team.getWoods() >= 2000 && team.getFishes() >= 2000){
                    soldierCoordinates = findSoldierCoordinates();
                    if( soldierCoordinates!= null){
                        totalEntities.add(new Soldier(soldierCoordinates.i, soldierCoordinates.j, team));
//                        JOptionPane.showMessageDialog(null, "new Soldier added");
                        team.reduceGolds(250);
                        team.reduceWoods(2000);
                        team.reduceFish(2000);
                    }
                }

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Coordinates findSoldierCoordinates() {
        int m = 2;

        while (m < 8) {
            for (int i = this.i - m; i < this.i + m; i++)
                if (Constants.isValid(i, this.j - m) && map.getTiles()[i][this.j - m].isWalkableOn()) {
                    map.getTiles()[i][this.j - m].setWalkableOn(false);
                    return new Coordinates(i, this.j - m);
                }

            for (int j = this.j - m; j < this.j + m; j++)
                if (Constants.isValid(this.i + m, j) && map.getTiles()[this.i + m][j].isWalkableOn() ) {
                    map.getTiles()[this.i + m][j].setWalkableOn(false);
                    return new Coordinates(this.i + m, j);

                }

            for (int i = this.i + m; i > this.i - m; i--)
                if (Constants.isValid(i, this.j + m) && map.getTiles()[i][this.j + m].isWalkableOn() ) {
                    map.getTiles()[i][this.j + m].setWalkableOn(false);
                    return new Coordinates(i, this.j + m);
                }

            for (int j = this.j + m; j > this.j - m; j--)
                if (Constants.isValid(this.i - m, j) && map.getTiles()[this.i - m][j].isWalkableOn()) {
                    map.getTiles()[this.i - m][j].setWalkableOn(false);
                    return new Coordinates(this.i - m, j);
                }

            m++;
        }
        return null;
    }
}
