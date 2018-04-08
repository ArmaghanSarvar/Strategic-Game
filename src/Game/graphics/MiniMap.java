package Game.graphics;

import Game.consts.Constants;
import Game.mapworks.Map;
import Game.mapworks.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Armaghan on 7/3/2017.
 */
public class MiniMap extends JPanel implements MouseListener, MouseMotionListener{
    private Tile[][] mapTiles;
    private Map map;
    private int width;
    private double size;
    Timer t = new Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    });

    public MiniMap(int w, int h, Map map){
        setLayout(null);
        mapTiles = map.getTiles();
        this.map = map;
        width = w - (int )(h*(1.5));
        setSize(width, width);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        size = (double) width / Constants.MATRIX_SIZE;
        setLocation((int )(h*(1.5)), h -( w - (int )(h*(1.5))));
        setBackground(Color.PINK);
        t.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i=0; i< Constants.MATRIX_SIZE; i++)
            for (int j=0 ; j <Constants.MATRIX_SIZE; j++)
                g.drawImage(new ImageIcon("R/spring/day/" + mapTiles[i][j].getMapPic()).getImage(),
                        i * (int)size, j * ((int) size), (int) size, ((int) size), null);

        g.drawImage(MapEditor.Images.getImage(8), map.getStartX() *(int) size, map.getStartY() *(int) size,
                (int) size * map.getSizeX(), ((int)size * map.getSizeY()),null );
    }

    private int xToi(int x){
        return ((int) (x / size));
    }

    private int yToj(int y){
        return ((int) (y / size));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int newStartX, newStartY;
        newStartX = checkXvalidity(xToi(e.getX())- map.getSizeX()/2);
        newStartY = checkYValidity(yToj(e.getY())- map.getSizeY()/2);
        map.setStartX(newStartX);
        map.setStartY(newStartY);
    }

    private int checkXvalidity(int sx){
        while (sx + map.getSizeX()> Constants.MATRIX_SIZE)
            sx--;
        if(sx < 0)
             sx = 0;
        return sx;
    }

    private int checkYValidity(int sy){
        while (sy + map.getSizeY()> Constants.MATRIX_SIZE)
            sy--;
        if(sy < 0)
            sy = 0;
        return sy;
    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseClicked(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
