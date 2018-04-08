package MapEditor;

/**
 * Created by Armaghan on 5/15/2017.
 */
public class Cursor  {
    int item;
    int x,y;


    public Cursor(int s){
        item = s;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getItem() {
        return item;
    }

}