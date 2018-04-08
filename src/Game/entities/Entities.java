package Game.entities;

import Game.Players.Player;
import Game.Players.Team;
import Game.pathfinding.Coordinates;

import java.util.LinkedList;

/**
 * Created by Armaghan on 7/8/2017.
 */
public class Entities {

    public static LinkedList<Entity> totalEntities = new LinkedList<>();
    public static Entity selected;
    public static LinkedList<Entity> getTotalEntities() {
        return totalEntities;
    }

    public static LinkedList<Entity> getTeamEntities(Team team){
        LinkedList<Entity> result = new LinkedList<>();
        for (int i=0; i< totalEntities.size(); i++){
            if( totalEntities.get(i).getTeam() == team)
                result.add(totalEntities.get(i));
        }
        return result;
    }

    public synchronized static Entity entityOfDest(Coordinates enemy){
      try {
          for (int k = 0; k < totalEntities.size(); k++) {
              if(totalEntities.get(k).getI() == enemy.i && totalEntities.get(k).getJ() == enemy.j ){
                  return totalEntities.get(k);
              }
          }
      }
      catch (NullPointerException e){
          System.out.printf("");
      }
      //  System.out.println("entity is null in entityOfDest method");
        return null;
    }

    public static LinkedList<Entity> getTeamHumans(Team team){
        LinkedList<Entity> res= new LinkedList<>();
        for (int i=0; i<totalEntities.size(); i++){
            if(totalEntities.get(i).getTeam() == team && totalEntities.get(i) instanceof Human){
                res.add(totalEntities.get(i));
            }
        }
        return res;
    }

    public static LinkedList<Entity> getTeamSoldiers(Team team){
        LinkedList<Entity> res= new LinkedList<>();
        for (int i=0; i<totalEntities.size(); i++){
            if(totalEntities.get(i).getTeam() == team && totalEntities.get(i) instanceof Soldier){
                res.add(totalEntities.get(i));
            }
        }
        return res;
    }

    public static Entity getTeamBarrack(Team team) {
        for (int k = 0; k < totalEntities.size(); k++) {
            if (totalEntities.get(k) instanceof Barrack && totalEntities.get(k).team == team)
                return totalEntities.get(k);
        }

        return null;
    }

    public static Entity getTeamPort(Team team) {
        for (int k = 0; k < totalEntities.size(); k++) {
            if (totalEntities.get(k) instanceof Port && totalEntities.get(k).team == team)
                return totalEntities.get(k);
        }

        return null;
    }
    public static Entity getTeamQuarry(Team team) {
        for (int k = 0; k < totalEntities.size(); k++) {
            if (totalEntities.get(k) instanceof Quarry && totalEntities.get(k).team == team)
                return totalEntities.get(k);
        }

        return null;
    }

    public static Entity getTeamCastle(Team team) {
        for (int k = 0; k < totalEntities.size(); k++) {
            if (totalEntities.get(k) instanceof Castle && totalEntities.get(k).team == team)
                return totalEntities.get(k);
        }
        return null;
    }

    public static void setSelected(Entity selected) {
        if (selected.team instanceof Player)
            Entities.selected = selected;
    }
}