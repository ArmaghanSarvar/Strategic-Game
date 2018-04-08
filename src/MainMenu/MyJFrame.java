package MainMenu;

import Game.network.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ASUS UX360CA on 7/17/2017.
 */
public class MyJFrame extends JFrame implements ActionListener {


    JLabel jLabel = new JLabel("enter your name please");
    JTextField textField = new JTextField();
    JButton jButton = new JButton("ok");


    public MyJFrame() {
        int w = 500;
        int h = 500;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int) (d.getWidth() / 2 - w / 2)), ((int) (d.getHeight() / 2 - h / 2)));
        setSize(w, h);
        setLayout(null);

        add(textField);
        add(jLabel);
        add(jButton);

        //todo
        jLabel.setLocation(w / 2 - w / 8, h / 4);
        textField.setLocation(w / 2 - w / 4, h / 2 - h / 16);
        jButton.setLocation(w / 2 - w / 4, h / 2 + 70);

        textField.setSize(w / 2, h / 8);
        jLabel.setSize(w / 2, h / 8);
        jButton.setSize(w / 8, h / 8);

        jButton.addActionListener(this);

        getContentPane().setBackground(Color.GREEN);
        setVisible(true);
        System.out.println("loading...");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = textField.getText();

        if ("ok".equals(jButton.getText()))
            new GameClient(s);
    }
}
