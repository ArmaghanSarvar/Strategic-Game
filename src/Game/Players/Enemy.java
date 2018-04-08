package Game.Players;

import Game.consts.Constants;
import Game.entities.Entities;
import Game.entities.Entity;
import Game.entities.Human;
import Game.entities.states.HumanStates;
import Game.mapworks.Map;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Armaghan on 7/4/2017.
 */
public class Enemy extends Team implements Runnable {
    Random random;
    public Enemy(Map map) {
        super(map);
        this.fishes = 10000;
        this.golds= 20000;
        this.woods = 20000;
        random = new Random();
    }
    public Enemy(Map map, String clientName)
    {
        super(map);
        this.fishes = 3000;
        this.golds= 4000;
        this.woods = 5000;
        this.clientName = clientName;
    }

    @Override
    public void run() {

        if (!hasBarrack){
            setBarracksIJ();
            setBarracksBuilder();
            hasBarrack = true;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!hasPort){
//            System.out.println("let's make port ");
            setPortIJ();
            setPortBuilder();
            hasPort = true;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!hasQuarry){
//            System.out.println("let's make quarry");
            setQuarryIJ();
            setQuarryBuilder();
            hasQuarry = true;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            setRandomHumanStates();
            try {
                Thread.sleep(4000 + random.nextInt(4000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void setBarracksIJ(){
        int  m = 3;
        boolean found= false;
        while (m < 6 && !found){
            for (int i = castleI - m ; i < castleI + m +1; i++){
                if(Constants.isValid(i, castleJ - m )  && map.getTiles()[i][castleJ -m].isWalkableOn() &&
                        !map.getTiles()[i][castleJ -m].isBeach() ){
                    map.getTiles()[i][castleJ -m].setWalkableOn(false);
                    barracksI= i;
                    barracksJ= castleJ-m;
                    found = true;
                    break;
                }
            }
            for (int j= castleJ - m; j < castleJ + m ; j++) {
                if (Constants.isValid(castleI + m , j) && map.getTiles()[castleI + m ][j].isWalkableOn() &&
                        !map.getTiles()[castleI + m ][j].isBeach() && !found) {
                    map.getTiles()[castleI + m ][j].setWalkableOn(false);
                    barracksI= castleI + m;
                    barracksJ= j;
                    found = true;
                    break;
                }
            }
            for (int i= castleI + m ; i > castleI -m ; i--) {
                if (Constants.isValid(i, castleJ + m)&& map.getTiles()[i][castleJ + m ].isWalkableOn() &&
                        !map.getTiles()[i][castleJ + m ].isBeach() && !found) {
                    map.getTiles()[i][castleJ + m ].setWalkableOn(false);
                    barracksI= i;
                    barracksJ= castleJ + m ;
                    found = true;
                    break;
                }
            }
            for (int j = castleJ + m ; j>castleJ -m; j--){
                if(Constants.isValid(castleI -m , j)&& map.getTiles()[castleI-m][j].isWalkableOn()
                        && !map.getTiles()[castleI-m][j].isBeach() && !found){
                    map.getTiles()[castleI -m][j].setWalkableOn(false);
                    barracksI= castleI - m;
                    barracksJ = j;
                    found = true;
                     break;
                }
            }
            m++;
        }
    }

    private void setQuarryIJ(){
        int  m = 3;
        boolean foundQ= false;
        while (m < 7 && !foundQ){
            for (int i = castleI - m ; i < castleI + m ; i++){
                if(Constants.isValid(i, castleJ - m )  && map.getTiles()[i][castleJ -m].isWalkableOn() &&
                        !map.getTiles()[i][castleJ -m].isBeach()){
                    map.getTiles()[i][castleJ -m].setWalkableOn(false);
                    quarryI= i;
                    quarryJ= castleJ-m;
                    foundQ = true;
                    break;
                }
            }
            for (int j= castleJ - m; j < castleJ + m ; j++) {
                if (Constants.isValid(castleI + m , j) && map.getTiles()[castleI + m ][j].isWalkableOn() &&
                        !map.getTiles()[castleI + m ][j].isBeach() && !foundQ) {
                    map.getTiles()[castleI + m ][j].setWalkableOn(false);
                    quarryI= castleI + m;
                    quarryJ= j;
                    foundQ = true;
                    break;
                }
            }
            for (int i= castleI + m ; i > castleI -m ; i--) {
                if (Constants.isValid(i, castleJ + m)&& map.getTiles()[i][castleJ + m ].isWalkableOn() &&
                        !map.getTiles()[i][castleJ + m ].isBeach() && !foundQ) {
                    map.getTiles()[i][castleJ + m ].setWalkableOn(false);
                    quarryI= i;
                    quarryJ= castleJ + m +1;
                    foundQ = true;
                    break;
                }
            }
            for (int j = castleJ + m ; j>castleJ -m; j--){
                if(Constants.isValid(castleI -m , j)&& map.getTiles()[castleI-m][j].isWalkableOn()
                        && !map.getTiles()[castleI-m][j].isBeach() && !foundQ){
                    map.getTiles()[castleI -m][j].setWalkableOn(false);
                    quarryI= castleI - m;
                    quarryJ = j;
                    foundQ = true;
                    break;
                }
            }
            m++;
        }
    }

    private void setPortIJ(){
        int  m = 3;
        boolean found = false;
        while (m < 9 && !found){
            for (int i = castleI - m ; i < castleI + m ; i++){
                if(Constants.isValid(i, castleJ - m )  && map.getTiles()[i][castleJ -m].isWalkableOn() &&
                        map.getTiles()[i][castleJ -m].isBeach() ){
                    map.getTiles()[i][castleJ -m].setWalkableOn(false);
                    portI= i;
                    portJ= castleJ-m;
                    found = true;
                    break;
                }
            }
            for (int j= castleJ - m; j < castleJ + m ; j++) {
                if (Constants.isValid(castleI + m , j) && map.getTiles()[castleI + m ][j].isWalkableOn() &&
                        map.getTiles()[castleI + m ][j].isBeach() && !found) {
                    map.getTiles()[castleI + m ][j].setWalkableOn(false);
                    portI= castleI + m;
                    portJ= j;
                    found = true;
                    break;
                }
            }
            for (int i= castleI + m ; i > castleI -m ; i--) {
                if (Constants.isValid(i, castleJ + m)&& map.getTiles()[i][castleJ + m ].isWalkableOn() &&
                        map.getTiles()[i][castleJ + m ].isBeach() && !found) {
                    map.getTiles()[i][castleJ + m ].setWalkableOn(false);
                    portI= i;
                    portJ= castleJ + m +1;
                    found = true;
                    break;
                }
            }
            for (int j = castleJ + m ; j>castleJ -m; j--){
                if(Constants.isValid(castleI -m , j)&& map.getTiles()[castleI-m][j].isWalkableOn()
                        && map.getTiles()[castleI-m][j].isBeach() && !found){
                    map.getTiles()[castleI -m][j].setWalkableOn(false);
                    portI= castleI - m;
                    portJ = j;
                    found = true;
                    break;
                }
            }
            m++;
        }
    }

    private void setBarracksBuilder(){
//        System.out.println("baracks builder");
        LinkedList<Entity> humans= Entities.getTeamHumans(this);
        for (int i = 0; i< humans.size(); i++){
            if(((Human)humans.get(i)).gethState() == HumanStates.IDLE) {
                ((Human) humans.get(i)).setState(HumanStates.BUILDING_BARRACKS);
                break;
            }
        }
    }

    private void setQuarryBuilder(){
        LinkedList<Entity> humans= Entities.getTeamHumans(this);
        for (int i = 0; i < humans.size(); i++) {
            if(((Human)humans.get(i)).gethState() == HumanStates.IDLE) {
                ((Human) humans.get(i)).setState(HumanStates.BUILDING_QUARRY);
                break;
            }
        }
    }

    private void setPortBuilder(){
        LinkedList<Entity> humans= Entities.getTeamHumans(this);
        for (int i = 0; i < humans.size(); i++) {
            if(((Human)humans.get(i)).gethState() == HumanStates.IDLE) {
                ((Human) humans.get(i)).setState(HumanStates.BUILDING_PORT);
                break;
            }
        }
    }

    private void setRandomHumanStates(){
        int c = random.nextInt(3);

        for (int i = 0; i < Entities.getTeamHumans(this).size() ; i++) {
            if (((Human) Entities.getTeamHumans(this).get(i)).gethState() == HumanStates.IDLE){
                if (c == 0){
                    ((Human) Entities.getTeamHumans(this).get(i)).setState(HumanStates.COLLECTING_WOOD);
                }
                else if(c == 1){
                    ((Human) Entities.getTeamHumans(this).get(i)).setState(HumanStates.COLLECTING_GOLD);
                }
                break;
            }

        }
    }
}