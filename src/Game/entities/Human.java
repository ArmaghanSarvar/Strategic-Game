package Game.entities;

import Game.Players.Enemy;
import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;
import Game.entities.states.HumanStates;
import Game.pathfinding.Coordinates;

import javax.swing.*;

import java.util.Random;

import static Game.entities.Entities.entityOfDest;

/**
 * Created by Armaghan on 7/6/2017.
 */
public class Human extends Character {
    private int capacity, wood, gold;
    private boolean fullcapacity, teamHasBarracks, teamHasPort, teamHasQuarry;
    private int treeI, treeJ;
    private Random woodRand;

    public HumanStates hState;

    public Human(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.HUMAN_ID;
        if (team instanceof Player && map.multiPlayer) {
//            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        health = 500;
        this.setImage(new ImageIcon("R/h01.png"));
        hState = HumanStates.IDLE;
        (new Thread(this)).start();
        woodRand = new Random();
    }

    @Override
    public void run() {

        while (isAlive()) {
            try {
                Thread.sleep(9);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkStates();
        }
    }

    private void checkStates() {
        switch (hState) {
            case IDLE: {

                if (teamHasBarracks && teamHasPort && teamHasQuarry) {
                    if (checkForEnemies() != null) {
//                        System.out.println("enemy found");
                        //move(getPointsAround(checkForEnemies()));
//                        System.out.println("x is :  " + getPointsAround(checkForEnemies()).i);
                        hState = HumanStates.FIGHTING;
                    }
                }
                break;
            }

            case FIGHTING: {

                move(destination);
                this.setImage(new ImageIcon("R/fightingh.gif"));
                if (map.multiPlayer)
                    map.gameClient.sendImage(i, j, "R/fightingh.gif", team.clientName);

                Entity c = entityOfDest(checkForEnemies());
                //JOptionPane.showMessageDialog(null, c.toString());
                if (c instanceof Soldier) {
                    reduceHealthBySoldier(this);
                    this.alive = false;
                    omitCharacter(this.i, this.j);
                    this.setImage(new ImageIcon("R/h01.png"));
                    if (map.multiPlayer)
                        map.gameClient.sendImage(i, j, "R/h01.png", team.clientName);
                    hState = HumanStates.IDLE;
                } else {
                    reduceHealthByHuman((Character) c);
                    omitCharacter(c.i, c.j);
                    this.setImage(new ImageIcon("R/h01.png"));
                    if (map.multiPlayer)
                        map.gameClient.sendImage(i, j, "R/h01.png", team.clientName);
                    hState = HumanStates.IDLE;
                }
                break;
            }

            case BUILDING_BARRACKS: {
                //TODO check enemy
//                System.out.println("building barrack");
                this.move(getPointsAround(new Coordinates(team.getBarracksI(), team.getBarracksJ())));

                Entities.totalEntities.add(new Barrack(team.getBarracksI(), team.getBarracksJ(), this.getTeam()));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hState = HumanStates.DISCHARGING;
                teamHasBarracks = true;
                break;
            }

            case BUILDING_QUARRY: {
                //TODO check enemy
//                System.out.println("building quarry");
                this.move(getPointsAround(new Coordinates(team.getQuarryI(), team.getQuarryJ())));
                Entities.totalEntities.add(new Quarry(team.getQuarryI(), team.getQuarryJ(), this.getTeam()));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hState = HumanStates.COLLECTING_WOOD;
                teamHasQuarry = true;
                break;
            }

            case BUILDING_PORT: {
//                System.out.println("building port");
                this.move(getPointsAround(new Coordinates(team.getPortI(), team.getPortJ())));
                Entities.totalEntities.add(new Port(team.getPortI(), team.getPortJ(), this.getTeam()));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hState = HumanStates.COLLECTING_GOLD;
                teamHasPort = true;
                break;
            }

            case COLLECTING_GOLD: {
                if (this.team instanceof Enemy && !map.multiPlayer) {
                    Coordinates gmCo = findGoldMine();
                    if (gmCo != null) {
                        move(getPointsAround(gmCo));
                        (this).setImage(new ImageIcon("R/collecting.gif"));
                        if (map.multiPlayer)
                            map.gameClient.sendImage(i, j, "R/collecting.gif", team.clientName);
                        while (!fullcapacity)
                            collectGold();
                        hState = HumanStates.DISCHARGING;
                    }
                } else if (this.team instanceof Player) {
                    move(destination);
                    (this).setImage(new ImageIcon("R/collecting.gif"));
                    if (map.multiPlayer)
                        map.gameClient.sendImage(i, j, "R/collecting.gif", team.clientName);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gold = 200;
                    hState = HumanStates.DISCHARGING;
                }
                (this).setImage(new ImageIcon("R/h01.png"));
                if (map.multiPlayer)
                    map.gameClient.sendImage(i, j, "R/h01.png", team.clientName);
                break;
            }

            case COLLECTING_WOOD: {
                if (this.team instanceof Enemy && !map.multiPlayer) {

                    if (woodRand.nextBoolean()) {
                        if (Entities.getTeamQuarry(team) != null) {
                            move(getPointsAround(new Coordinates(Entities.getTeamQuarry(team).getI(), Entities.getTeamQuarry(team).getJ())));
                            this.setImage(new ImageIcon("R/collecting.gif"));
                            if (map.multiPlayer)
                                map.gameClient.sendImage(i, j, "R/collecting.gif", team.clientName);

                            while (!fullcapacity)
                                collectWood();
                            this.setImage(new ImageIcon("R/h01.png"));
                            if (map.multiPlayer)
                                map.gameClient.sendImage(i, j,"R/h01.png", team.clientName);

                            hState = HumanStates.DISCHARGING;
                        }
                    } else {
                        Coordinates tCo = findTree();
                        if (tCo != null) {
                            move(getPointsAround(tCo));
                            this.setImage(new ImageIcon("R/collecting.gif"));
                            if (map.multiPlayer)
                                map.gameClient.sendImage(i, j,"R/collecting.gif", team.clientName);

                            while (!fullcapacity)
                                collectWood();

                            map.getTiles()[treeI][treeJ].setElementPic("transparent.png");
                            map.getTiles()[treeI][treeJ].setElement(7);
                            map.getTiles()[treeI][treeJ].setWalkableOn(true);
                            this.setImage(new ImageIcon("R/h01.png"));
                            if (map.multiPlayer)
                                map.gameClient.sendImage(i, j, "R/h01.png", team.clientName);
                            hState = HumanStates.DISCHARGING;

                        }
                    }
                } else if (this.team instanceof Player) {
                    move(destination);
                    (this).setImage(new ImageIcon("R/collecting.gif"));
                    if (map.multiPlayer)
                        map.gameClient.sendImage(i, j, "R/collecting.gif", team.clientName);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wood = 200;
                    if (map.multiPlayer)
                        map.gameClient.sendEndElement(treeI, treeJ, team.clientName);
                    map.getTiles()[treeI][treeJ].setElementPic("R/spring/transparent.png");
                    map.getTiles()[treeI][treeJ].setElement(7);
                    map.getTiles()[treeI][treeJ].setWalkableOn(true);
                    hState = HumanStates.DISCHARGING;
                }
                (this).setImage(new ImageIcon("R/h01.png"));
                if (map.multiPlayer)
                    map.gameClient.sendImage(i, j,"R/h01.png" , team.clientName);

                break;
            }

            case DISCHARGING: {
                move(getPointsAround(new Coordinates(Entities.getTeamCastle(team).getI(), Entities.getTeamCastle(team).getJ())));
                discharge();
                hState = HumanStates.IDLE;
                break;
            }

            case MOVING_BY_ORDERED: {
                move(destination);
                hState = HumanStates.IDLE;
                break;
            }
        }
    }

    private void discharge() {
        this.team.addGold(gold);
        this.team.addWood(wood);
        wood = 0;
        gold = 0;
        capacity = 0;
        fullcapacity = false;
    }

    private void collectWood() {
        if (capacity + Constants.WOOD_CAPACITY <= Constants.HUMAN_MAX_CAPACTY) {
            capacity += Constants.WOOD_CAPACITY;
            wood++;
        } else fullcapacity = true;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void collectGold() {
        if (capacity + Constants.GOLD_CAPACITY <= Constants.HUMAN_MAX_CAPACTY) {
            gold++;
            capacity += Constants.GOLD_CAPACITY;
        } else fullcapacity = true;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean fullCapacity() {
        return fullcapacity;
    }

    private Coordinates findGoldMine() {
        int m = 1;

        while (m < 30) {
            for (int i = this.i - m; i < this.i + m; i++)
                if (Constants.isValid(i, this.j - m) && isGoldMine(i, this.j - m)) {
                    return new Coordinates(i, this.j - m);
                }

            for (int j = this.j - m; j < this.j + m; j++)
                if (Constants.isValid(this.i + m, j) && isGoldMine(this.i + m, j)) {
                    return new Coordinates(this.i + m, j);
                }

            for (int i = this.i + m + 1; i > this.i - m; i--)
                if (Constants.isValid(i, this.j + m) && isGoldMine(i, this.j + m)) {
                    return new Coordinates(i, this.j + m);
                }

            for (int j = this.j + m; j > this.j - m; j--)
                if (Constants.isValid(this.i - m, j) && isGoldMine(this.i - m, j)) {
                    return new Coordinates(this.i - m, j);
                }
            m++;
        }
        return null;
    }

    private Coordinates findTree() {
        int m = 1;

        while (m < 30) {
            for (int i = this.i - m; i < this.i + m; i++)
                if (Constants.isValid(i, this.j - m) && isTree(i, this.j - m)) {
                    treeI = i;
                    treeJ = this.j - m;
                    return new Coordinates(i, this.j - m);
                }

            for (int j = this.j - m; j < this.j + m; j++)
                if (Constants.isValid(this.i + m, j) && isTree(this.i + m, j)) {
                    treeI = this.i + m;
                    treeJ = j;
                    return new Coordinates(this.i + m, j);
                }

            for (int i = this.i + m + 1; i > this.i - m; i--)
                if (Constants.isValid(i, this.j + m) && isTree(i, this.j + m)) {
                    treeI = i;
                    treeJ = this.j + m;
                    return new Coordinates(i, this.j + m);
                }

            for (int j = this.j + m; j > this.j - m; j--)
                if (Constants.isValid(this.i - m, j) && isTree(this.i - m, j)) {
                    treeI = this.i - m;
                    treeJ = j;
                    return new Coordinates(this.i - m, j);
                }
            m++;
        }
        return null;
    }

    public void setState(HumanStates state) {
        this.hState = state;
    }

    public int getWood() {
        return wood;
    }

    public int getGold() {
        return gold;
    }

    public HumanStates gethState() {
        return hState;
    }

    public void setTreeI(int treeI) {
        this.treeI = treeI;
    }

    public void setTreeJ(int treeJ) {
        this.treeJ = treeJ;
    }
}