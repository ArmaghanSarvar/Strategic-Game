package Game.mapworks;

import Game.consts.TimeHandler;
import Game.graphics.Frame;

/**
 * Created by Armaghan on 7/3/2017.
 */
public class Main {
    public Main(){
        new Frame(new Map());
        new TimeHandler();
    }
}
