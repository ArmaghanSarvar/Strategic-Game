package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/12/2017.
 */
public class EnemyPacket extends Packet
{
    public int castleI, castleJ;
    public String clientName;
    public EnemyPacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        castleI = Integer.parseInt(dataArray[0]);
        castleJ = Integer.parseInt(dataArray[1]);
        this.clientName = dataArray[2];
    }
}
