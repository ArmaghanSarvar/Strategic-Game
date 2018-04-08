package MainMenu;

import Game.consts.MusicHandler;
import Game.network.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Armaghan on 6/8/2017.
 */
public class MenuFrame extends JFrame implements ActionListener{

    public MenuFrame(){
        MusicHandler.music("music.wav").start();
        setTitle("Final Project");
        JButton b1, b2, b3, b4, b5;
        setSize(700 , 700);
        setLocation(600, 200);
        b1 = new JButton("SINGLE PLAYER");
        b2 = new JButton("MULTI PLAYER");
        b3 = new JButton("MAP EDITOR");
        b4 = new JButton("EXIT");
        b5 = new JButton("STOP MUSIC");
        b1.setLocation(50, 50);
        b2.setLocation(50, 150);
        b3.setLocation(50,250);
        b4.setLocation(50, 450);
        b5.setLocation(50, 350);
        setBackground(Color.BLUE);
        b1.setSize(150, 60);
        b2.setSize(150, 60);
        b3.setSize(150, 60);
        b4.setSize(150, 60);
        b5.setSize(150, 60);
        b1.setBackground(Color.ORANGE);
        b2.setBackground(Color.ORANGE);
        b3.setBackground(Color.ORANGE);
        b4.setBackground(Color.ORANGE);
        b5.setBackground(Color.ORANGE);
        setLayout(null);
        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(b5);
        JLabel background=new JLabel(new ImageIcon("R/menu.jpg"));
        background.setSize(700, 700);
        getContentPane().add(background);
        setResizable(false);
        setFocusable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
    }

    public static void main(String[] args) {
        new MenuFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("SINGLE PLAYER")) {
            new Game.mapworks.Main();
        }
        else if(e.getActionCommand().equals("MULTI PLAYER")) {
            new MyJFrame();
        }
        else if(e.getActionCommand().equals("MAP EDITOR"))
            new MapEditor.MyFrame();
        else if(e.getActionCommand().equals("EXIT"))
            this.dispose();
        else if(e.getActionCommand().equals("STOP MUSIC"))
            MusicHandler.clip.stop();
    }
}