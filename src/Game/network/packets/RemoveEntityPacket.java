package Game.network.packets;

public class RemoveEntityPacket extends Packet
{
    public int i, j;
    public String clientName;
    public RemoveEntityPacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);
        clientName = dataArray[2];
    }
}
