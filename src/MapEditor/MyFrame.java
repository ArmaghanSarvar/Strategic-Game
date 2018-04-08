package MapEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Armaghan on 5/13/2017.
 */
public class MyFrame extends JFrame {

    private int width, height;
    private Dimension screenDimension;
    private MapPanel panel;
    private ToolBarPanel toolBar;
    private MiniMapPanel miniMapPanel;
    private Cursor cursor;

    public MyFrame() {
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(100, 100);
        height =  (int) (screenDimension.height * (11/12f));

        height -= height % 100;
        cursor = new Cursor(0);
        Constants.setToolbarWidth(height/4);
        width = height + Constants.TOOLBAR_WIDTH;
        Constants.setMapHeight(height);
        Constants.setMapWidth(height);
        setSize(width, height);
        panel = new MapPanel(height, height , cursor);
        toolBar = new ToolBarPanel( height, cursor, panel.mapMatrix, this);
        miniMapPanel = new MiniMapPanel(height, toolBar.getMapMatrix());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(toolBar);
        getContentPane().add(panel);
        getContentPane().add(miniMapPanel);
        setLayout(null);
        this.setResizable(false);
        setVisible(true);
    }
}