package Game.network.packets;

public class LogInPacket extends Packet
{
    public String clientName;

    public LogInPacket(byte[]data)
    {
        String[] dataArray = readData(data).split(",");
        this.clientName = dataArray[0];
    }
}
