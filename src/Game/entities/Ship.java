package Game.entities;

import Game.Movable;
import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;
import Game.consts.Images;
import Game.entities.states.FishingShipStates;
import Game.pathfinding.A_star;
import Game.pathfinding.Coordinates;

/**
 * Created by Armaghan on 7/4/2017.
 */
public abstract class Ship extends Entity implements Runnable, Movable {
    protected int capacity = 0;
    protected Coordinates destination;

    public Ship(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.SHIP_ID;
        if (team instanceof Player && map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
    }

    public synchronized void move(Coordinates coordinates) {
        this.moving = true;
        Coordinates next;
        //  if(coordinates != null)
        while ((this.i != coordinates.i || this.j != coordinates.j) && this.moving) {
            try {
                if (A_star.findWaterPath(getMap(), this.i, this.j, coordinates.i, coordinates.j).isEmpty())
                    break;
                next = A_star.findWaterNode(getMap(), this.i, this.j, coordinates.i, coordinates.j);
//                if (team.getMap().getTiles()[next.i][next.j].isSailableOn())
//                    getMap().getTiles()[next.i][next.j].setSailableOn(false);
                map.getTiles()[i][j].setSailableOn(true);
                map.getTiles()[next.i][next.j].setSailableOn(false);
                this.step(next);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("error");
                break;
            }
        }
        this.moving = false;
    }

    private void step(Coordinates nextDes) {
        if (team instanceof Player && map.multiPlayer) {
            map.gameClient.sendMove(team.clientName, i, j, nextDes.i, nextDes.j);
        }

        int dirX = nextDes.i - this.i;
        int dirY = nextDes.j - this.j;
        System.out.println(dirX);


        if (this instanceof FishingShip) {
            this.setImage(Images.fishingShipImages[(dirX + 1) * 3 + (dirY + 1)]);
        }
        while (xInTile != dirX * Constants.TILE_SIZE || yInTile != dirY * Constants.TILE_SIZE) {
            xInTile += dirX;
            yInTile += dirY;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        team.getMap().getTiles()[i][j].setSailableOn(false);
        this.i = nextDes.i;
        this.j = nextDes.j;
//        team.getMap().getTiles()[i][j].setSailableOn(true);

        xInTile = 0;
        yInTile = 0;
    }


    public void setDestination(Coordinates coordinates) {
        this.destination = coordinates;
        if (this instanceof FishingShip) {
            ((FishingShip) this).setCurrS(FishingShipStates.MOVE_BY_ORDER);
        }
    }
}