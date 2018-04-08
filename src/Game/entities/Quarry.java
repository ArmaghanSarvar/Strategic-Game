package Game.entities;

import Game.Players.Player;
import Game.Players.Team;
import Game.consts.Constants;

import javax.swing.*;

/**
 * Created by Armaghan on 7/9/2017.
 */
public class Quarry extends Entity implements Runnable{
    public Quarry(int i, int j, Team team) {
        super(i, j, team);
        entityID = Constants.QUARRY_ID;
        if (team instanceof Player && map.multiPlayer) {
            System.out.println("an entity sent to server");
            map.gameClient.sendNewEntity(i, j, entityID, team.clientName);
        }
        map.getTiles()[i][j].setWalkableOn(false);
        this.setImage(new ImageIcon("R/loadingquarry.png"));
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setImage(new ImageIcon("R/quarry.png"));
    }
}
