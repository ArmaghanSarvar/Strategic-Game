package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/13/2017.
 */
public class EndElementPacket extends  Packet
{
    public int i, j;
    public String clientName;
    public EndElementPacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);
        clientName = dataArray[2];
    }
}
