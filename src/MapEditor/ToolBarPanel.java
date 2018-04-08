package MapEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Armaghan on 5/13/2017.
 */
public class ToolBarPanel extends JPanel implements ActionListener {
    int w = Constants.TOOLBAR_WIDTH, h;
    private MyComboBox comboBox;
    private Cursor cursor;
    private JLabel label;
    private MyFrame frame;

    public MapMatrix getMapMatrix() {
        return mapMatrix;
    }

    private MapMatrix mapMatrix;
    private MyButtons saveBut, undoBut, exitBut, redoBut, playBut, loadBut;

    public ToolBarPanel(int h, Cursor cursor, MapMatrix m, MyFrame f) {
        this.h = h;
        mapMatrix = m;
        setSize(w, h - Constants.TOOLBAR_WIDTH);
        this.cursor = cursor;
        comboBox = new MyComboBox();
        frame = f;
        saveBut = new MyButtons("SAVE");
        undoBut = new MyButtons("UNDO");
        exitBut = new MyButtons("EXIT");
        playBut = new MyButtons("PLAY");
        redoBut = new MyButtons("REDO");
        loadBut = new MyButtons("LOAD");

        redoBut.setLocation(130, 210);
        undoBut.setLocation(26, 210);
        saveBut.setLocation(26, 290);
        loadBut.setLocation(130, 290);
        playBut.setLocation(26, 380);
        exitBut.setLocation(78, 380);
        JLabel background=new JLabel(new ImageIcon("R/woodtool.jpg"));
        background.setSize(700, h - Constants.TOOLBAR_WIDTH );

        label = new JLabel("Choose your Item:");
        label.setFont(new Font("Serif", Font.BOLD, 20));

        label.setSize(200, 40);
        label.setLocation(34, 3);

        this.add(label);
        this.add(comboBox);
        this.add(saveBut);
//        this.add(undoBut);
        this.add(exitBut);
//        this.add(redoBut);
//        this.add(playBut);
        this.add(loadBut);
        comboBox.setLocation(34, 50);

        saveBut.addActionListener(this);
        undoBut.addActionListener(this);
        exitBut.addActionListener(this);
        redoBut.addActionListener(this);
        playBut.addActionListener(this);
        loadBut.addActionListener(this);
        comboBox.addActionListener(this);
        this.add(background);

        setLayout(null);
        setBackground(Color.GREEN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        File savedFile = null;
        cursor.setItem(comboBox.getSelectedIndex());
        if (e.getActionCommand().equals("SAVE")) {
            fileChooser.setDialogTitle("Choose a folder");
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                savedFile= fileChooser.getSelectedFile();
            }
            mapMatrix.saveMap(savedFile.getAbsolutePath());
        } else if (e.getActionCommand().equals("LOAD")) {
            fileChooser.setDialogTitle("Open file to load");
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal==JFileChooser.APPROVE_OPTION){
                File file=fileChooser.getSelectedFile();
                String a = file.getAbsolutePath();
                mapMatrix.loadMap(a);
            }
        }
        else if(e.getActionCommand().equals("EXIT"))
            frame.dispose();
//        if (e.getActionCommand().equals("UNDO"))
//            mapMatrix.undo();
//        else if (e.getActionCommand().equals("REDO"))
//            mapMatrix.reDo();
    }

}