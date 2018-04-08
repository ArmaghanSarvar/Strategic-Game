package MapEditor;

import javax.swing.*;

/**
 * Created by Armaghan on 5/14/2017.
 */
public class MyComboBox extends JComboBox{
    private String[] str= {"Shallow Water","Deep Water", "land", "Tree", "Fish", "Gold Mine", "Mountain", "Castle"};

    public MyComboBox(){
        for (int i=0; i<str.length; i++){
            this.addItem(str[i]);
        }
        setSize(120, 40);
        setLocation(40,35);
    }

    @Override
    public int getSelectedIndex() {
        if(super.getSelectedIndex()== 7)
            return 9;
       else
           return super.getSelectedIndex();
    }
}