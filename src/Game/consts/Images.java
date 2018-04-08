package Game.consts;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Armaghan on 7/3/2017.
 */

public class Images {
    private static String path = "R/spring/day/" ;
    public static String defPath= "R/";

    public static ImageIcon[] humanImages = {new ImageIcon(defPath + "h-1-1.png"),
            new ImageIcon(defPath + "h-10.png"),
            new ImageIcon(defPath + "h-11.png"),
            new ImageIcon(defPath + "h0-1.png"),
            new ImageIcon(defPath + "h00.png"),
            new ImageIcon(defPath + "h01.png"),
            new ImageIcon(defPath + "h1-1.png"),
            new ImageIcon(defPath+ "h10.png"),
            new ImageIcon(defPath + "h11.png")};

    public static ImageIcon[] soldierImages = {new ImageIcon(defPath + "s-1-1.png"),
            new ImageIcon(defPath + "s-10.png"),
            new ImageIcon(defPath + "s-11.png"),
            new ImageIcon(defPath + "s0-1.png"),
            new ImageIcon(defPath + "s00.png"),
            new ImageIcon(defPath + "s01.png"),
            new ImageIcon(defPath + "s1-1.png"),
            new ImageIcon(defPath+ "s10.png"),
            new ImageIcon(defPath + "s11.png")};

    public static ImageIcon[] fishingShipImages = {new ImageIcon(defPath + "fs-1-1.png"),
            new ImageIcon(defPath + "fs-10.png"),
            new ImageIcon(defPath + "fs-11.png"),
            new ImageIcon(defPath + "fs0-1.png"),
            new ImageIcon(defPath + "fs00.png"),
            new ImageIcon(defPath + "fs01.png"),
            new ImageIcon(defPath + "fs1-1.png"),
            new ImageIcon(defPath+ "fs10.png"),
            new ImageIcon(defPath + "fs11.png")};

    public static void modifyPath(String season, String dayOrNight){
        path = defPath + season + dayOrNight;

    }

    public static Image getImage(String mapPic){
        return new ImageIcon(path + mapPic).getImage();
    }

}