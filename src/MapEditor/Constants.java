package MapEditor;

/**
 * Created by Armaghan on 5/14/2017.
 */
public class Constants {
    public static int TOOLBAR_WIDTH ;
    public static final int MATRIX_SIZE = 84;
    public static final int MATRIX_MAX_ZOOM =1;
    public static void setToolbarWidth(int toolbarWidth) {
        TOOLBAR_WIDTH = toolbarWidth;
    }
    public static int SHALLOW_WATER = 0;
    public static int DEEP_WATER = 1;
    public static int LAND = 2;
    public static int TREE = 3;
    public static int FISH = 4;
    public static int GOLD_MINE =5;

    public static int MOUNTAIN = 6;
    public static int Castle= 9;
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    private static String season;

    public static boolean isNight() {
        return night;
    }

    private static boolean night;

    private static String[] seasons= { "spring", "summer", "autumn", "winter"};


    public static void setMapWidth(int mapWidth) {
        MAP_WIDTH = mapWidth;
        season = seasons[0];
        Images.UpdateImages();
    }
    public static String getSeason() {
        return season;
    }

    public static void reverseNight(){
        night = !night;
    }
    public static String getDayOrNight(){

        if(night)
            return "night";
        return "day";
    }

    public static void changeSeason(int i) {
        Constants.season = seasons[i];
    }

    public static void setMapHeight(int mapHeight) {
        MAP_HEIGHT = mapHeight;
    }
}