package Game.entities;

import Game.Movable;
import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;
import Game.consts.Images;
import Game.entities.states.HumanStates;
import Game.entities.states.SoldierStates;
import Game.mapworks.Tile;
import Game.pathfinding.A_star;
import Game.pathfinding.Coordinates;

import javax.swing.*;

import static Game.entities.Entities.getTeamHumans;
import static Game.entities.Entities.totalEntities;

/**
 * Created by Armaghan on 7/4/2017.
 */

public abstract class Character extends Entity implements Runnable, Movable{
    protected int dirX, dirY;
    protected boolean alive;
    protected Coordinates destination;

    public Character(int i, int j, Team team) {
        super(i, j, team);
    }

    public void move(Coordinates coordinates) {
        this.moving = true;

//        System.out.println("coor"+coordinates.i + "+" + coordinates.j);
        try {
            Tile tile = getMap().getTiles()[coordinates.i][coordinates.j];
            if (tile != null)
                tile.setBeingMovedTo(true);
        }catch (NullPointerException e){
            System.out.printf("");
        }

        Coordinates next;
        //  if(coordinates != null)
        while ((this.i != coordinates.i || this.j != coordinates.j) && this.moving) {
            try {
                if (A_star.findPath(getMap(), this.i, this.j, coordinates.i, coordinates.j).isEmpty()) {
                    break;
                }
                next = A_star.findNode(getMap(), this.i, this.j, coordinates.i, coordinates.j);
                if (team.getMap().getTiles()[next.i][next.j].isWalkableOn())
                    getMap().getTiles()[next.i][next.j].setWalkableOn(false);
                this.step(next);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("error");
                break;
            }
        }
        getMap().getTiles()[this.i][this.j].setWalkableOn(false);
        getMap().getTiles()[coordinates.i][coordinates.j].setBeingMovedTo(false);
        this.moving = false;
    }

    private void step(Coordinates nextDes) {
        dirX = nextDes.i - this.i;
        dirY = nextDes.j - this.j;
        System.out.println(dirX);
        if (team instanceof Player && map.multiPlayer) {
            map.gameClient.sendMove(team.clientName, i, j, nextDes.i, nextDes.j);
        }
        if (this instanceof Human)
            this.setImage(Images.humanImages[(dirX + 1) * 3 + (dirY + 1)]);
        else if (this instanceof Soldier)
            this.setImage(Images.soldierImages[(dirX + 1) * 3 + (dirY + 1)]);

        while (xInTile != dirX * Constants.TILE_SIZE || yInTile != dirY * Constants.TILE_SIZE) {
            xInTile += dirX;
            yInTile += dirY;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        team.getMap().getTiles()[i][j].setWalkableOn(true);
        this.i = nextDes.i;
        this.j = nextDes.j;

        xInTile = 0;
        yInTile = 0;
    }

    private boolean isThereEnemy(int i, int j) {
        Entity entity;
        for (int m = 0; m < Entities.getTotalEntities().size(); m++) {
            entity = Entities.getTotalEntities().get(m);
            if (entity != null && entity instanceof Character && entity.getI() == i && entity.getJ() == j &&
                    this.team != entity.getTeam())
                return true;
        }
        return false;
    }

    protected Coordinates checkForEnemies() {
        int m = 1;
        while (m <= 5) {
            for (int i = this.i - m; i < this.i + m; i++) {
                if (Constants.isValid(i, this.j - m) && isThereEnemy(i, this.j - m)) {
                    return new Coordinates(i, this.j - m);
                }
            }

            for (int j = this.j - m; j < this.j + m; j++) {
                if (Constants.isValid(this.i + m, j) && isThereEnemy(this.i + m, j)) {
                    return new Coordinates(this.i + m, j);
                }
            }

            for (int i = this.i + m; i > this.i - m; i--) {
                if (Constants.isValid(i, this.j + m) && isThereEnemy(i, this.j + m)) {
                    return new Coordinates(i, this.j + m);
                }
            }
            for (int j = this.j + m; j > this.j - m; j--) {
                if (Constants.isValid(this.i - m, j) && isThereEnemy(this.i - m, j)) {
                    return new Coordinates(this.i - m, j);

                }
            }
            if (this instanceof Human && m == 2)
                break;
            m++;
        }
        return null;
    }

    protected void reduceHealthByHuman(Character ch) {
        while (ch.health > 0) {
            ch.health -= 20;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void reduceHealthBySoldier(Character ch) {
        while (ch.health > 0) {
            ch.health -= 70;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void omitCharacter(int i, int j) {
        Entity entity;
        for (int k = 0; k < totalEntities.size(); k++) {
            entity = totalEntities.get(k);
            if (entity.getI() == i && entity.getJ() == j && entity.team != this.team) {
                if (entity instanceof Human) {
                    entity.setImage(new ImageIcon("R/dyingh.gif"));
                    if (entity == Entities.selected) {
                        Entities.selected = null;
                    }
                    if (map.multiPlayer)
                        map.gameClient.sendImage(i, j, "R/dyingh.gif", team.clientName);
                }
                else {
                    entity.setImage(new ImageIcon("R/dyings.png"));
                    if (entity == Entities.selected) {
                        Entities.selected = null;
                    }
                    if (map.multiPlayer)
                        map.gameClient.sendImage(i, j,"R/dyings.png" , team.clientName);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                totalEntities.remove(k);
                if (team instanceof Player && map.multiPlayer)
                {
                    team.getMap().gameClient.sendOmitCharacter(i,j);
                }
                if (getTeamHumans(map.getPlayer()).isEmpty()) {
                    new JOptionPane("you lost");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        }
    }

    public boolean isGoldMine(int i, int j) {
        return getMap().getTiles()[i][j].getElement() == Constants.GOLD_MINE;
    }

    public boolean isTree(int i, int j) {
        return getMap().getTiles()[i][j].getElement() == Constants.TREE;
    }

    protected boolean isAlive() {
        return health > 0;
    }

    public void setDestination(Coordinates destination) {
        this.destination = destination;
        Entity entity = Entities.entityOfDest(destination);
        if (entity == null) {
            if (isGoldMine(destination.i, destination.j)) {
                if (this instanceof Human) {
                    this.destination = getPointsAround(destination);
                    ((Human) this).setState(HumanStates.COLLECTING_GOLD);
                }
            } else if (isTree(destination.i, destination.j)) {
                ((Human) this).setTreeI(destination.i);
                ((Human) this).setTreeJ(destination.j);
                if (this instanceof Human) {
                    this.destination = getPointsAround(destination);
                    ((Human) this).setState(HumanStates.COLLECTING_WOOD);
                }
            } else {
                if (this instanceof Human) {
                    ((Human) this).setState(HumanStates.MOVING_BY_ORDERED);
                } else if (this instanceof Soldier) {
                    ((Soldier) this).setsState(SoldierStates.MOVING_BY_ORDERED);
                }
            }
        } else {

            if (entity.team != this.team && (entity instanceof Character)) {
                if (this instanceof Human) {
                    this.destination = getPointsAround(destination);
                    ((Human) this).setState(HumanStates.FIGHTING);
                } else if (this instanceof Soldier) {
                    this.destination = getPointsAround(destination);
                    ((Soldier) this).setsState(SoldierStates.FIGHTING);
                }
            } else if (entity instanceof Quarry) {
                if (this instanceof Human) {
                    this.destination = getPointsAround(destination);
                    ((Human) this).setState(HumanStates.COLLECTING_WOOD);
                }
            }
        }
    }
}