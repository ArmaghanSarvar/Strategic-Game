package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/10/2017.
 */
public class TilePacket extends Packet
{
    public int i, j, type, element;
    public String picName, elementName;
    public TilePacket(byte[]data)
    {
        String [] dataArray = readData(data).trim().split(",");

        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);
        type = Integer.parseInt(dataArray[2]);
        element = Integer.parseInt(dataArray[3]);
        picName = dataArray[4];
        elementName = dataArray[5];
    }
}
