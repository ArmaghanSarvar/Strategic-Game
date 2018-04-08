package Game.consts;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Armaghan on 7/6/2017.
 */
public class TimeHandler {
    private static String[] seasons= {"spring/", "summer/", "winter/", "autumn/"};
    private static String[] dayTime= {"day/" , "night/"};
    static int day , s;

    public Timer changeSeason= new Timer(10000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           s++;
           s%=4;
           Constants.setCharacterSpeed(s);
           Images.modifyPath(seasons[s], dayTime[day]);
        }
    });

    public Timer changeDay= new Timer(5000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            day++ ;
            day %= 2;
            Images.modifyPath(seasons[s], dayTime[day]);
        }
    });

    public TimeHandler(){
        changeDay.start();
        changeSeason.start();
        Constants.setCharacterSpeed(s);
    }

    public static String getSeason() {
        return seasons[s];
    }

    public static String getDayTime() {
        return dayTime[day];
    }
}
