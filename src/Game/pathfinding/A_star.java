package Game.pathfinding;

import Game.consts.Constants;
import Game.mapworks.Map;

import java.util.List;
import java.util.Vector;

/**
 * Created by Armaghan on 7/6/2017.
 */
public class A_star {

    public static Vector<Coordinates> findPath(Map map, int i1, int j1, int i2, int j2) {
        PathFindingMap<ExampleNode> myMap = new PathFindingMap<ExampleNode>(map,Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, new ExampleFactory());
        List<ExampleNode> path = myMap.findPath(i1, j1, i2, j2);
        Vector<Coordinates> newpath = new Vector<>();
        if(path == null)
            return new Vector<>();
        for (int m = 0; m < path.size(); m++)
            newpath.add(new Coordinates(path.get(m).getxPosition(), path.get(m).getyPosition()));
        return newpath;
    }

    public static Coordinates findNode(Map map, int i1, int j1, int i2, int j2) throws ArrayIndexOutOfBoundsException{

        try {

            return findPath(map, i1, j1, i2, j2).get(0);
        }catch (Exception e){
            return new Coordinates(i1, j1);
        }

    }

    public static Coordinates findWaterNode(Map map, int i1, int j1, int i2, int j2){
        return findWaterPath(map, i1 , j1, i2, j2).get(0);
    }

    public static Vector<Coordinates> findWaterPath(Map map, int i1, int j1, int i2, int j2) {
        PathFindingMap<ExampleNode> myMap = new PathFindingMap<ExampleNode>(map,Constants.MATRIX_SIZE, Constants.MATRIX_SIZE, new ExampleFactory(), 1);
        List<ExampleNode> path = myMap.findPath(i1, j1, i2, j2);
        Vector<Coordinates> newpath = new Vector<>();

        for (int m = 0; m < path.size(); m++)
            newpath.add(new Coordinates(path.get(m).getxPosition(), path.get(m).getyPosition()));
        return newpath;
    }

}