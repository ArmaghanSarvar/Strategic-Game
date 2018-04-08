package Game.entities;

import Game.Players.Enemy;
import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;
import Game.entities.states.SoldierStates;
import Game.pathfinding.Coordinates;

import javax.swing.*;

import static Game.entities.Entities.entityOfDest;

/**
 * Created by Armaghan on 7/6/2017.
 */
public class Soldier extends Character {

    private SoldierStates sState;

    public Soldier(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.SOLDIER_ID;
        if (team instanceof Player && map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        health = 1000;
        sState = SoldierStates.IDLE;
        this.setImage(new ImageIcon("R/s01.png"));
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        while(isAlive()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkStates();
        }
    }

    private void checkStates() {
        Character e;
        switch (sState) {
            case IDLE: {
                if (this.team instanceof Enemy && !map.multiPlayer){
                    Coordinates eCo = checkForEnemies();
                    e = ((Character) entityOfDest(eCo));
                    if(eCo != null && e != null && e.moving) {
                        destination =getPointsAround(eCo);
                        sState = SoldierStates.MOVE;
                    }
                }
                break;
            }

            case MOVING_BY_ORDERED: {
                move(destination);
                sState = SoldierStates.IDLE;
                break;
            }

            case FIGHTING:{
                move(destination);
                this.setImage(new ImageIcon("R/fightings.gif"));
                if (map.multiPlayer)
                    map.gameClient.sendImage(i, j, "R/fightings.gif", team.clientName);

                Entity c = entityOfDest(checkForEnemies()) ;
                reduceHealthBySoldier((Character) c);
                omitCharacter(c.i, c.j);
                this.setImage(new ImageIcon("R/s01.png"));
                if (map.multiPlayer)
                    map.gameClient.sendImage(i, j, "R/s01.png", team.clientName);

                sState= SoldierStates.IDLE;
                break;
            }

            case MOVE:{
                move(destination);
                sState= SoldierStates.FIGHTING;
                break;
            }
        }
    }

    public void setsState(SoldierStates sState) {
        this.sState = sState;
    }
}