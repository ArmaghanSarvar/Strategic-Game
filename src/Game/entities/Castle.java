package Game.entities;

import Game.Players.Team;

import javax.swing.*;

/**
 * Created by Armaghan on 7/5/2017.
 */
public class Castle extends Entity{

    public Castle(int i , int j, Team team){
        super(i,j,team);
        entityID = 0;
        this.setImage(new ImageIcon("R/castle.png"));
        this.takenTiles=2;
    }

}
