package Game.graphics;

import Game.entities.*;
import Game.mapworks.Map;
import Game.pathfinding.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static Game.entities.Entities.totalEntities;

/**
 * Created by Armaghan on 7/3/2017.
 */

public class Toolbar extends JPanel implements MouseListener, ActionListener{
    private int width, height;
    private Object currObject;
    private int foodNum, woodNum, goldNum, barrackI, barrackJ, h, portI, portJ;
    private Map map;
    private int mouseX, mouseY;
    private boolean waitingForB, waitingForP, waitingForQ;
    JButton bbutton, pbutton, qbutton;
    private Coordinates shipCoordinates;

    Timer t = new Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            foodNum = map.getPlayer().getFishes();
            woodNum = map.getPlayer().getWoods();
            goldNum = map.getPlayer().getGolds();
            repaint();
        }
    });

    public Toolbar(int w, int h, Map map){
        setLayout(null);
        this.map = map;
        this.h =h;
        width  = w - (int )(h*(1.5));
        height = h - width;
        setSize(width, height);
        setBackground(Color.pink);
        bbutton= new JButton("MAKE BARRACK");
        pbutton= new JButton("MAKE PORT");
        qbutton= new JButton("MAKE QUARRY");
        bbutton.setSize(130, 50);
        bbutton.setLocation(10 , 470);
        pbutton.setSize(120, 50);
        pbutton.setLocation(150, 470);
        qbutton.setSize(120, 50);
        qbutton.setLocation(280,470);
        setLocation((int )(h*(1.5)), 0);
        qbutton.setForeground(new Color(179, 94, 37));
        bbutton.setForeground(new Color(179, 94, 37));
        pbutton.setForeground(new Color(179, 94, 37));
        bbutton.addActionListener(this);
        pbutton.addActionListener(this);
        qbutton.addActionListener(this);
        this.add(bbutton);
        this.add(pbutton);
        this.add(qbutton);
        this.addMouseListener(this);
        t.start();
        foodNum = map.getPlayer().getFishes();
        woodNum = map.getPlayer().getWoods();
        goldNum = map.getPlayer().getGolds();
        JLabel background=new JLabel(new ImageIcon("R/back.jpg"));
        background.setSize(700,  height);
        this.add(background);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //g.drawImage(new ImageIcon("R/back.jpg").getImage() ,0, 0, width, height , null);
        g.drawImage(new ImageIcon("R/fish.png").getImage(), 60, 49, 50, 60, null);
        g.drawImage(new ImageIcon("R/wood.png").getImage(), 205, 50, 50, 46, null);
        g.drawImage(new ImageIcon("R/gold.png").getImage(), 155, 100, 50, 46, null);

        Font myFont = new Font("Serif", Font.ITALIC | Font.BOLD, 26);
        g.setFont(myFont);
        g.drawString("×"+ foodNum , 120, 90);
        g.drawString("×"+ woodNum , 270, 90);
        g.drawString("×"+ goldNum , 210, 143);

        if (currObject instanceof Castle ) {
            g.drawImage(new ImageIcon("R/toolbarcastle.png").getImage(), 125, 200, 170, 150, null);
            g.drawString("Castle" , 130, 185);
            g.drawString("you can make Human" , 145, 415);
            g.drawImage(new ImageIcon("R/h01.png").getImage(), 40, 335, 100, 95, null);
        }
        else if(currObject instanceof Port){
            g.drawImage(new ImageIcon("R/port.png").getImage(), 135, 200, 120, 120, null);
            g.drawString("Port" , 150, 185);
            g.drawString("you can make FishingShip" , 130, 400);
            g.drawImage(new ImageIcon("R/fishingship.png").getImage(), 15, 310, 120, 120, null);
        }

        else if(currObject instanceof Barrack){
            g.drawImage(new ImageIcon("R/barracks.png").getImage(), 125, 200, 170, 150, null);
            g.drawString("Barrack" , 130, 185);
            g.drawString("you can make Soldier" , 145, 415);
            g.drawImage(new ImageIcon("R/s01.png").getImage(), 40, 335, 100, 95, null);
        }
        else if(currObject instanceof Quarry){
            g.drawImage(new ImageIcon("R/quarry.png").getImage(), 125, 200, 170, 170, null);
            g.drawString("Quarry", 140, 185);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mouseX != e.getX() && mouseY != e.getY()) {

            if (e.getX() > 40 && e.getX() < 160 && e.getY() > 310 && e.getY() < 430 && currObject instanceof Port) {
                shipCoordinates = map.getPlayer().findShipCoordinates();
                if (shipCoordinates != null) {
                    totalEntities.add(new FishingShip(shipCoordinates.i,
                            shipCoordinates.j, map.getPlayer()));
                    JOptionPane.showMessageDialog(null, "Ship is Made");
                }
            }

            if (e.getX() > 40 && e.getX() < 140 && e.getY() > 335 && e.getY() < 430 && currObject instanceof Castle) {
                if (foodNum >= 1000) {
                    map.getPlayer().setAHuman();
                    map.getPlayer().setFishes(foodNum -1000);
                } else
                    JOptionPane.showMessageDialog(null, "not enough food");
            }

            if (e.getX() > 40 && e.getX() < 140 && e.getY() > 335 && e.getY() < 430 && currObject instanceof Barrack) {
                if (foodNum >= 2000 && goldNum >= 250 && woodNum >= 600) {
                    // JOptionPane.showMessageDialog(null, "can make a soldier");
                    map.getPlayer().setASoldier();
                    map.getPlayer().setFishes(foodNum - 2000);
                    map.getPlayer().setGolds(goldNum - 250);
                    map.getPlayer().setWoods(woodNum - 600);

                }
                else
                    JOptionPane.showMessageDialog(null, "not enough storage");
            }

            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setCurrObject(Object currObject) {
        this.currObject = currObject;
        //JOptionPane.showMessageDialog(null, currObject.toString());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("MAKE BARRACK")) {
            JOptionPane.showMessageDialog(null, "choose a place for your Barrack");
            waitingForB= true;
           //map.getPlayer().setBarrack(barrackI, barrackJ);
           bbutton.setVisible(false);
        }
        else if(e.getActionCommand().equals("MAKE PORT")){
            JOptionPane.showMessageDialog(null, "choose a place for your Port");
            waitingForP = true;

//            map.getPlayer().setAPort();
            pbutton.setVisible(false);
        }
        else if(e.getActionCommand().equals("MAKE QUARRY")){
            JOptionPane.showMessageDialog(null, "choose a place for your Quarry");
            waitingForQ = true;

            qbutton.setVisible(false);
        }
    }

    public Object getCurrObject() {
        return currObject;
    }

    public boolean isWaitingForB() {
        return waitingForB;
    }

    public void setBarrackI(int barrackI) {
        this.barrackI = barrackI;
    }

    public void setBarrackJ(int barrackJ) {
        this.barrackJ = barrackJ;
    }

    public void setWaitingForB(boolean waitingForB) {
        this.waitingForB = waitingForB;
    }

    public boolean isWaitingForP() {
        return waitingForP;
    }

    public boolean isWaitingForQ() {
        return waitingForQ;
    }

    public void setPortI(int portI) {
        this.portI = portI;
    }

    public void setPortJ(int portJ) {
        this.portJ = portJ;
    }

    public void setWaitingForP(boolean waitingForP) {
        this.waitingForP = waitingForP;
    }

    public void setWaitingForQ(boolean waitingForQ) {
        this.waitingForQ = waitingForQ;
    }
}