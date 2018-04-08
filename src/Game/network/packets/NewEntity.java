package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/11/2017.
 */
public class NewEntity extends Packet
{
    public int i,j,entityID;
    public String clientName;
    public NewEntity(byte[]data){
        String[] dataArray = readData(data).split(",");
        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);
        entityID = Integer.parseInt(dataArray[2]);
        clientName = dataArray[3];
    }
}
