package Game.consts;

import Game.entities.Human;

/**
 * Created by Armaghan on 7/3/2017.
 */
public class Constants {
    public static int CASTLE_ID = 0;
    public static int HUMAN_ID = 1;
    public static int BARRACK_ID = 2;
    public static int FISHING_SHIP_ID = 3;
    public static int PORT_ID = 4;
    public static int QUARRY_ID = 5;
    public static int SOLDIER_ID = 6;
    public static int SHIP_ID = 7;

    public static int MATRIX_SIZE  = 84;
    public static int CASTLE  = 9;
    public static int DEEP_WATER = 1;
    public static int LAND = 2;
    public static int TREE = 3;
    public static int FISH = 4;
    public static int GOLD_MINE =5;
    public static int SHALLOW_WATER = 0;
    public static int CHARACTER_SPEED;
    public static int[] CHARACTER_SPEEDS= {8,7,5,3};
    public static int TILE_SIZE;
    public static int HUMAN_MAX_CAPACTY = 300;
    public static int WOOD_CAPACITY = 3;
    public static int GOLD_CAPACITY = 5;
    public static int FISHING_SHIP_MAX_CAP = 1200;
    public static Human HUMAN;

    public static boolean isValid(int i, int j){
        if (i < 0 || j < 0)
            return false;
        if (i >= MATRIX_SIZE || j >= MATRIX_SIZE)
            return false;
        return true;
    }

    public static void setCharacterSpeed(int characterSpeed) {
        CHARACTER_SPEED = CHARACTER_SPEEDS[characterSpeed]  ;
    }

    public static void setTileSize(int tileSize) {
        TILE_SIZE = tileSize;
    }

//    public static void setHUMAN() {
//        for (int i = 0; i < totalEntities.size(); i++) {
//            if (totalEntities.get(i) instanceof Human) {
//                HUMAN = (Human ) totalEntities.get(i);
//                (new Thread(Constants.HUMAN)).start();
//                break;
//            }
//
//
//        }
//    }
}
