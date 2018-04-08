package MapEditor;

import javax.swing.*;
import java.awt.*;

public class Images{

    private static String[] imageName=  new String[]{"shallow_water.png", "deep_water.png", "", "tree.png", "fish.png", "gold_mine.png", "",
            "transparent.png",""};
    private static Image SHALLOW_WATER ;
    private static Image DEEP_WATER ;
    private static Image LAND ;
    private static Image TREE ;
    private static Image FISH;
    private static Image GOLD_MINE ;
    private static Image MOUNTAIN ;
    private static Image CASTLE ;
    private static Image  TRANSPARENT;
    private static Image MINI_MAP_FRAME = new ImageIcon("Resources/mini_map_frame.png").getImage();
    private static Image[] images = {SHALLOW_WATER, DEEP_WATER, LAND, TREE, FISH , GOLD_MINE, MOUNTAIN, TRANSPARENT, MINI_MAP_FRAME};


    public static void UpdateImages()
    {
        SHALLOW_WATER = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight() +"/shallow_water.png").getImage();
        DEEP_WATER = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight() +"/deep_water.png").getImage();
        LAND = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight() +"/land.png").getImage();
        TREE = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight() +"/tree.png").getImage();
        FISH = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight() +"/fish.png").getImage();
        GOLD_MINE = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight() + "/gold_mine.png").getImage();
        MOUNTAIN = new ImageIcon("Resources/"+Constants.getSeason()+ "/"+ Constants.getDayOrNight()+"/mountain.png").getImage();
        TRANSPARENT = new ImageIcon("Resources/"+Constants.getSeason() + "/"+ Constants.getDayOrNight() +"/transparent.png").getImage();
        CASTLE= new ImageIcon("Resources/castle.png").getImage();
        images = new Image[]{SHALLOW_WATER, DEEP_WATER, LAND, TREE, FISH , GOLD_MINE, MOUNTAIN, TRANSPARENT, MINI_MAP_FRAME,CASTLE};
    }

    public static String getImageName(int i) {
        return imageName[i];
    }

    public static Image getImage(int i)
    {
        return images[i];
    }

    public static Image getImage(String str){
        return new ImageIcon("Resources/"+ Constants.getSeason() + "/"+ Constants.getDayOrNight() +"/" + str).getImage();
    }
}