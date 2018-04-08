package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/10/2017.
 */
public class NewElementPacket extends Packet
{
    int i, j, elementID;
    public NewElementPacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);
        this.elementID = Integer.parseInt(dataArray[2]);
    }
}
