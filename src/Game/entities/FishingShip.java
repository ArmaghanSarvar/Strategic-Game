package Game.entities;

import Game.Players.Enemy;
import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;
import Game.entities.states.FishingShipStates;
import Game.pathfinding.Coordinates;

import javax.swing.*;

/**
 * Created by Armaghan on 7/10/2017.
 */
public class FishingShip extends Ship{
    private Coordinates fCo;
    FishingShipStates currS= FishingShipStates.IDLE;

    public FishingShip(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.FISHING_SHIP_ID;
        if (team instanceof Player&& map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        fCo= new Coordinates(0,0);

        this.setImage(new ImageIcon("R/fs0-1.png"));
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true){
            checkStates();
            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkStates(){
        switch (currS){
            case IDLE:{
                if (this.team instanceof Enemy && !map.multiPlayer)
                    currS = FishingShipStates.MOVE_TO_FISH;
                break;
            }

            case MOVE_TO_FISH:{
                if (this.team instanceof Enemy && !map.multiPlayer) {
                    fCo = findFish();
                    if (fCo != null) {
                        this.move(fCo);
                        currS = FishingShipStates.CHARGE;
                    }
                }

                break;
            }
            case CHARGE:{
                collectFish();
                if (map.multiPlayer)
                    map.gameClient.sendEndElement(fCo.i,fCo.j,team.clientName);
                map.getTiles()[fCo.i][fCo.j].setElementPic("R/spring/transparent.png");
                map.getTiles()[fCo.i][fCo.j].setElement(7);
                currS = FishingShipStates.MOVE_TO_PORT;
                break;
            }
            case DISCHARGE:{

                discharge();
                currS = FishingShipStates.IDLE;
                break;
            }
            case MOVE_TO_PORT:{


                move(getSailablePointsAround(new Coordinates(Entities.getTeamPort(team).getI(), Entities.getTeamPort(team).getJ())));
                currS = FishingShipStates.DISCHARGE;
                break;
            }
            case MOVE_BY_ORDER:{
                move(destination);
                if(isFish(destination.i, destination.j)) {
                    fCo.i = destination.i;
                    fCo.j = destination.j;
                    this.currS = FishingShipStates.CHARGE;
                }
            }
        }
    }

    private void collectFish(){


        while (capacity < Constants.FISHING_SHIP_MAX_CAP){
            capacity += 50;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void discharge(){
        team.setFish(team.getFishes() + capacity);
        capacity = 0;
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Coordinates findFish(){
        int  m = 2;
        while (m < 7 ){
            for (int i = this.i - m ; i < this.i + m ; i++){
                if(Constants.isValid(i,this.j - m )  && map.getTiles()[i][this.j -m].isSailableOn()
                      && isFish(i, this.j -m )){
                    return new Coordinates(i, this.j -m);
                }
            }
            for (int j=this.j - m; j < this.j + m ; j++) {
                if (Constants.isValid(this.i + m , j) && map.getTiles()[this.i + m + 1][j].isSailableOn()  &&
                       isFish(this.i + m, j) ) {

                    return new Coordinates(this.i + m, j);
                }
            }
            for (int i= this.i + m ; i > this.i -m ; i--) {
                if (Constants.isValid(i, this.j + m)&& map.getTiles()[i][this.j + m ].isSailableOn() &&
                       isFish(i, this.j +m)) {
                    return new Coordinates(i, this.j + m);
                }
            }
            for (int j = this.j + m  ; j> this.j-m; j--){
                if(Constants.isValid(this.i -m , j)&& map.getTiles()[this.i -m][j].isSailableOn()
                     && isFish(this.i - m, j)){
                    return new Coordinates(this.i - m, j);
                }
            }
            m++;
        }
        return null;
    }

    public void setCurrS(FishingShipStates currS) {
        this.currS = currS;
    }

    private boolean isFish(int i, int j){
        return getMap().getTiles()[i][j].getElement() == Constants.FISH;
    }
}