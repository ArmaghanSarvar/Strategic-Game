package Game.network;

import java.net.InetAddress;

/**
 * Created by ASUS UX360CA on 7/10/2017.
 */
public class ClientInfo
{
    int port;
    InetAddress address;
    String clientName;
    public int castleI, castleJ;

    public ClientInfo(int port, InetAddress address, String clientName) {
        this.port = port;
        this.address = address;
        this.clientName = clientName;
    }
}
