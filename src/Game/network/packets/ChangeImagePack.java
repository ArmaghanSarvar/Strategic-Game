package Game.network.packets;

/**
 * Created by ASUS UX360CA on 7/12/2017.
 */
public class ChangeImagePack extends Packet
{
    public int i , j;
    public String image, clientName;

    public ChangeImagePack(byte data [])
    {
        String[] dataArray = readData(data).split(",");

        i = Integer.parseInt(dataArray[0]);
        j = Integer.parseInt(dataArray[1]);

        image = dataArray[2];
        clientName = dataArray[3];

    }

}
