package Game.graphics;

import Game.mapworks.Map;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Armaghan on 7/3/2017.
 */

public class Frame extends JFrame{

    public Frame(Map gm){
        int width, height;
        Panel panel;
        Toolbar toolbar;
        MiniMap miniMap;
        Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
        width = d.width;
        height = d.height - d.height%100;
        setLayout(null);
        setSize(width, height);
        toolbar= new Toolbar(width, height, gm);
        panel = new Panel(gm, height, toolbar);
        miniMap = new MiniMap(width, height, gm);
        this.add(panel);
        this.add(toolbar);
        this.add(miniMap);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("R/cursor.png").getImage(),
                new Point(0, 0), "Custom Cursor"));
        setVisible(true);

    }
}