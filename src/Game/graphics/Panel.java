package Game.graphics;

import Game.Players.Enemy;
import Game.Players.Player;
import Game.consts.Constants;
import Game.consts.Images;
import Game.entities.Character;
import Game.entities.Entities;
import Game.entities.Entity;
import Game.entities.FishingShip;
import Game.mapworks.Map;
import Game.mapworks.Tile;
import Game.pathfinding.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Vector;

import static Game.entities.Entities.getTeamEntities;
import static Game.entities.Entities.totalEntities;

/**
 * Created by Armaghan on 7/3/2017.
 */
public class Panel extends JPanel implements MouseMotionListener, MouseListener{
    private Map map;
    private int sizeX, sizeY;
    private Tile[][] mapTiles;
    private double tileSize;
    private int width, height;
    private Toolbar toolbar;
    Vector<Coordinates> coordinates;
    private int mouseX, mouseY;
    Timer t = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    });
    private Object activeObj;


    Timer panning = new Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(mouseX> width - 55)
                map.panRight();
            else if( mouseX < 55)
                map.panLeft();
            else if(mouseY > height - 55)
                map.panDown();
            else if(mouseY <  55)
                map.panUp();
        }
    });

    public Panel(Map map, int h, Toolbar toolbar){
        setLayout(null);
        this.map = map;
        setLocation(0,0);
        mapTiles = map.getTiles();
        width = (int) (h * 1.5);
        height = h;
        tileSize = (double) width / (double) map.getSizeX();
        Constants.setTileSize((int) tileSize);
        setSize(width, height);
        mouseX= width/2;
        mouseY = height/2;
        panning.start();
        this.toolbar= toolbar;
        sizeX = map.getSizeX();
        sizeY= map.getSizeY();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        t.start();
        setBackground(Color.green);
    }

    private void paintType(Graphics g) {
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++)
                g.drawImage(Images.getImage(mapTiles[i+ map.getStartX()][j+map.getStartY()].getMapPic()),((i * (int)tileSize)),
                        ((j * (int)tileSize)),((int) tileSize), ((int) tileSize), null);
    }

    private void paintElements(Graphics g){
        for (int i = 0; i < sizeX; i++)
            for (int j = 0; j < sizeY; j++) {
                g.drawImage(Images.getImage(mapTiles[i+ map.getStartX()][j+map.getStartY()].getElementPic()),((i * (int)tileSize)),
                        ((j * (int)tileSize)) , ((int) tileSize), ((int) tileSize), null);
            }
    }

    private void paintEntities(Graphics g){
        for (int i=0; i< Entities.getTotalEntities().size(); i++) {
            if(totalEntities.get(i).isInSight(map.getStartX(), map.getStartY(), map.getSizeX(), map.getSizeY()))
            g.drawImage(totalEntities.get(i).getImage(), totalEntities.get(i).getX(), totalEntities.get(i).getY(),
                    totalEntities.get(i).getTakenTiles() * (int)tileSize, totalEntities.get(i).getTakenTiles() *(int)tileSize,null);
        }
    }

    private int xToi(int x){
        return (x / ((int) tileSize)) + map.getStartX();
    }
    private int yToj(int y){
        return ( y / ((int) tileSize)) + map.getStartY();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        updateTotalEntities();
        paintType(g);
        paintElements(g);
        paintEntities(g);
        if (Entities.selected != null)
        paintIndicator(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       mouseX = e.getX();
       mouseY= e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Entity entity;
        if (e.getButton() == 1) {
            if(toolbar.isWaitingForB()){

                if (map.getPlayer().setBarrack(xToi(e.getX()),yToj(e.getY()) )) {
                    toolbar.setBarrackI(xToi(e.getX()));
                    toolbar.setBarrackJ(yToj(e.getY()));
                    toolbar.setWaitingForB(false);
                }

            }
            else if(toolbar.isWaitingForP()){

                if(map.getPlayer().setPort(xToi(e.getX()),yToj(e.getY()))){
                    toolbar.setPortI(xToi(e.getX()));
                    toolbar.setPortJ(yToj(e.getY()));
                    toolbar.setWaitingForP(false);
                }
            }
            else if(toolbar.isWaitingForQ()){

                if(map.getPlayer().setQuarry(xToi(e.getX()),yToj(e.getY()))){
                    toolbar.setWaitingForQ(false);
                }
            }

            else {
                Entity en = Entities.entityOfDest(new Coordinates(xToi(e.getX()), yToj(e.getY()))) ;
                if(en != null && en.getTeam() instanceof Player ) {
                    toolbar.setCurrObject(Entities.entityOfDest(new Coordinates(xToi(e.getX()), yToj(e.getY()))));
                    Entities.setSelected((Entity) toolbar.getCurrObject());
                }

            }
        }
        else if (e.getButton() == 3) {
            entity = ((Entity) toolbar.getCurrObject());
            if ( entity.getTeam() instanceof Player) {
                if ((entity instanceof Character))
                    ((Character) toolbar.getCurrObject()).setDestination(new Coordinates(xToi(e.getX()), yToj(e.getY())));
                else if (toolbar.getCurrObject() instanceof FishingShip) {
                    ((FishingShip) toolbar.getCurrObject()).setDestination(new Coordinates(xToi(e.getX()), yToj(e.getY())));
                }
            }
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
        mouseX = width/2;
        mouseY = height/2;
    }

    private void updateTotalEntities()
    {
        Player player= map.getPlayer();
        LinkedList<Enemy> enemies= map.getEnemies();

        for (int i = 0; i < getTeamEntities(player).size(); i++) {
            getTeamEntities(player).get(i).updateXY(map.getStartX(), map.getStartY(), map.getSizeX(), map.getSizeY(), (int)tileSize);

        }
        for (int i = 0; i < enemies.size(); i++)
            for (int j = 0; j < getTeamEntities(enemies.get(i)).size(); j++) {
                getTeamEntities(enemies.get(i)).get(j).updateXY(map.getStartX(), map.getStartY(), map.getSizeX(), map.getSizeY(), (int)tileSize);
          //      totalEntities.add(getTeamEntities(enemies.get(i)).get(j));
            }
    }

    private void paintIndicator(Graphics g) {
        if (Entities.selected.isInSight(map.getStartX(), map.getStartY(), map.getSizeX(), map.getSizeY()))
        g.drawImage(new ImageIcon("R/indicator.png").getImage(),
                Entities.selected.getX() + 15, Entities.selected.getY() - 20, 20, 20,  null);
    }
}
