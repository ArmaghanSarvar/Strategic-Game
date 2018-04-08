package MapEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Armaghan on 5/13/2017.
 */
public class MapPanel extends JPanel implements MouseListener , MouseWheelListener, MouseMotionListener, KeyListener{
    private Cursor cursor;
    public  MapMatrix mapMatrix;
    private int[][] matrix, elements;
    private String[][] landPics;
    private int seasonCounter=0;
    private int mouseX, mouseY;

    Timer t = new Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    });

    Timer panning = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(mouseX> Constants.MAP_WIDTH - 55)
            mapMatrix.panRight();
        else if( mouseX < 55)
            mapMatrix.panLeft();
        else if(mouseY > Constants.MAP_HEIGHT - 55)
            mapMatrix.panDown();
        else if(mouseY <  55)
            mapMatrix.panUp();
        }
    });

    public Timer changeSeason = new Timer(10000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
             seasonCounter= seasonCounter % 4;
             Constants.changeSeason(seasonCounter);
             Images.UpdateImages();
             repaint();
             seasonCounter++;
        }
    });

    public Timer changeDay= new Timer(4000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Constants.reverseNight();
            Images.UpdateImages();
            repaint();
        }
    });

    public MapPanel(int w, int h, Cursor cursor){
        setSize( w,h);

        mouseX = w/2;
        mouseY = h/2;
        setLocation(Constants.TOOLBAR_WIDTH , 0);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        mapMatrix = new MapMatrix(cursor);
        changeSeason.start();
        changeDay.start();
        t.start();
        panning.start();
        this.cursor = cursor;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int size = mapMatrix.getSize();
        int picS = Constants.MAP_WIDTH;
        matrix = mapMatrix.getMatrix();
        elements = mapMatrix.getElements();
        landPics = mapMatrix.getLandPics();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (matrix[i + mapMatrix.getStartingPointX()][j + mapMatrix.getStartingPointY()] != Constants.LAND)
                    g.drawImage(Images.getImage(matrix[i + mapMatrix.getStartingPointX()][j + mapMatrix.getStartingPointY()]),
                            i * picS / size, j * picS / size, picS / size, picS / size, null);
                else
                    g.drawImage(Images.getImage(landPics[i + mapMatrix.getStartingPointX()][j + mapMatrix.getStartingPointY()]),
                            i * picS / size, j * picS / size, picS / size, picS / size, null);
                if (elements[i + mapMatrix.getStartingPointX()][j + mapMatrix.getStartingPointY()] != Constants.Castle)
                    g.drawImage(Images.getImage(elements[i + mapMatrix.getStartingPointX()][j + mapMatrix.getStartingPointY()]),
                            i * picS / size, j * picS / size, picS / size, picS / size, null);

            }
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
            if(elements[i+ mapMatrix.getStartingPointX()][j+mapMatrix.getStartingPointY()] == Constants.Castle)
                g.drawImage(Images.getImage(elements[i + mapMatrix.getStartingPointX()][j + mapMatrix.getStartingPointY()]),
                        i * picS / size, j * picS / size, picS / size * 2, picS / size * 2, null);

            }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        cursor.x = e.getX();
        cursor.y = e.getY();
        mapMatrix.updateMatrix(cursor.x, cursor.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        cursor.x = e.getX();
        cursor.y = e.getY();
        mapMatrix.updateMatrix(cursor.x, cursor.y);}

    @Override
    public void mouseReleased(MouseEvent e) {
     //   mapMatrix.pushToPrimary(new StackElements(matrix, elements));

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseX = Constants.MAP_WIDTH/2;
        mouseY = Constants.MAP_HEIGHT/2;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getPreciseWheelRotation() > 0)
            mapMatrix.zoomOut();
        else
            mapMatrix.zoomIn();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        cursor.x = e.getX();
        cursor.y = e.getY();
        mapMatrix.updateMatrix(cursor.x, cursor.y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

       if( e.getKeyCode() == KeyEvent.VK_RIGHT) {
           mapMatrix.panRight();
           System.out.println("here");
       }
       else if( e.getKeyCode() == KeyEvent.VK_LEFT)
           mapMatrix.panLeft();
       else if( e.getKeyCode() == KeyEvent.VK_UP)
           mapMatrix.panUp();
       else if( e.getKeyCode() == KeyEvent.VK_DOWN)
           mapMatrix.panDown();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}