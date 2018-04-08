package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/11/2017.
 */
public class NumberPack extends Packet
{
    public int i;

    public NumberPack(byte [] data) {
        String[] dataArray = readData(data).split(",");
        i = Integer.parseInt(dataArray[0]);
    }
}
