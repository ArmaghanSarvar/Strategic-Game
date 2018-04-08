package Game.network.packets;

public class CastlePacket extends Packet
{
    public int i, j;
    public String clientName;

    public CastlePacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);
        this.clientName = dataArray[2];
    }
}
