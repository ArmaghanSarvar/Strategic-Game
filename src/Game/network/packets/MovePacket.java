package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/9/2017.
 */
public class MovePacket extends Packet
{
    public int destI, destJ, firstI, firstJ;
    public String clientName;
    public MovePacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        this.clientName = dataArray[0];
        firstI = Integer.parseInt(dataArray[1]);
        firstJ = Integer.parseInt(dataArray[2]);
        destI = Integer.parseInt(dataArray[3]);
        destJ = Integer.parseInt(dataArray[4]);
    }
}
