package MapEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Armaghan on 5/14/2017.
 */

public class MiniMapPanel extends JPanel{

    private int width, height;
    private MapMatrix mapMatrix;
    private int[][] minimatrix;

    Timer t = new Timer(300, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    });

    public MiniMapPanel(int wholeH, MapMatrix mapMatrix){
      width = Constants.TOOLBAR_WIDTH;
      height = Constants.TOOLBAR_WIDTH;
      this.mapMatrix = mapMatrix;
      setLocation(0, wholeH - height);
      setSize(width, height);
      minimatrix = mapMatrix.getMatrix();
      setBackground(Color.BLUE);
      t.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int w = width / Constants.MATRIX_SIZE;
        minimatrix = mapMatrix.getMatrix();
       for( int i=0; i< Constants.MATRIX_SIZE ; i++)
           for (int j=0; j< Constants.MATRIX_SIZE ; j++) {
               g.drawImage(Images.getImage(minimatrix[i][j]), i * w , j *w
                       , w, w, null);
               g.drawImage(Images.getImage(8), mapMatrix.getStartingPointX() * w, mapMatrix.getStartingPointY() * w, w*mapMatrix.getSize(), w*mapMatrix.getSize(), null);
           }
    }
}
