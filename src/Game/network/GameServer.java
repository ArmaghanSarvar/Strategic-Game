package Game.network;

import Game.consts.Constants;
import Game.mapworks.Map;
import Game.network.packets.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

/**
 * Created by ASUS UX360CA on 7/9/2017.
 */
public class GameServer implements Runnable {
    private final int PORT = 1234;
    private DatagramSocket socket;

    Vector<ClientInfo> gameClients;
    Vector<EntityInfo> entities;

    boolean running;

    Map map;

    public GameServer(Map map) {
        this.map = map;
        running = true;
        entities = new Vector<EntityInfo>();
        gameClients = new Vector<>();
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            byte[] data = new byte[512];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handlePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    void handlePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet;

        switch (type) {
            default:
            case INVALID: {
                System.err.println("invalid input");
                break;
            }
            case LOGIN: {
                packet = new LogInPacket(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((LogInPacket) packet).clientName + " has connected to the server...");
                gameClients.add(new ClientInfo(port, address, ((LogInPacket) packet).clientName));
                sendMapToClient(address, port);
                break;
            }
            case NEWENTITY: {
                packet = new NewEntity(data);
                int i = ((NewEntity) packet).i;
                int j = ((NewEntity) packet).j;
                int entityID = ((NewEntity) packet).entityID;
                String clientName = ((NewEntity) packet).clientName;

                entities.add(new EntityInfo(i, j, entityID, clientName));
                sendToAllExceptOne(new String(data), ((NewEntity) packet).clientName);
                break;
            }
            case PUTCASTLE: {
                packet = new CastlePacket(data);
                int i = ((CastlePacket) packet).i;
                int j = ((CastlePacket) packet).j;
                for (int k = 0; k < gameClients.size(); k++) {
                    if (gameClients.get(k).clientName.equals(((CastlePacket) packet).clientName))

                        gameClients.get(k).castleI = i;
                        gameClients.get(k).castleJ = j;
                }
                entities.add(new EntityInfo(i, j, 0, ((CastlePacket) packet).clientName));

                map.getTiles()[i][j].setElement(Constants.CASTLE);
                map.getTiles()[i][j].setWalkableOn(false);
                map.getTiles()[i + 1][j].setWalkableOn(false);
                map.getTiles()[i][j + 1].setWalkableOn(false);
                map.getTiles()[i + 1][j + 1].setWalkableOn(false);
                String s = "03";
                s += ((CastlePacket) packet).i + "," + ((CastlePacket) packet).j + "," + ((CastlePacket) packet).clientName;
                sendToAllExceptOne(s, ((CastlePacket) packet).clientName);
                break;
            }
            case ENEMY_REQUEST: {
                sendAllEnemies(address, port);
                break;
            }
            case TOTAL_ENTITY_REQUEST: {
                sendTotalEntitiesToClient(address, port);
                //todo think...
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendData("23", address, port);//send finish
                break;
            }
            case MOVE: {
                packet = new MovePacket(data);


                int firstI = ((MovePacket) packet).firstI;
                int fistJ = ((MovePacket) packet).firstJ;
                int destI = ((MovePacket) packet).destI;
                int destJ = ((MovePacket) packet).destJ;
                String clientName = ((MovePacket) packet).clientName;

                EntityInfo e = findEntity(firstI, fistJ);
                e.i = destI;
                e.j = destJ;

                sendToAllExceptOne(new String(data), clientName);
                break;
            }
            case REMOVE_ENTITY: {

                packet = new RemoveEntityPacket(data);
                int i = ((RemoveEntityPacket) packet).i;
                int j = ((RemoveEntityPacket) packet).j;
                String clientName = ((RemoveEntityPacket) packet).clientName;

                EntityInfo e = findEntity(i, j);
                entities.remove(e);

                sendToAllExceptOne(new String(data), clientName);
                break;
            }
            case END_ELEMENT: {
                packet = new EndElementPacket(data);
                int i = ((EndElementPacket) packet).i;
                int j = ((EndElementPacket) packet).j;
                String clientName = ((EndElementPacket) packet).clientName;

                map.getTiles()[i][j].setElementPic("R/spring/transparent.png");
                map.getTiles()[i][j].setElement(7);
                map.getTiles()[i][j].setWalkableOn(true);

                sendToAllExceptOne(new String(data), clientName);
                break;
            }
            case PIC_CHANGE: {
                packet = new ChangeImagePack(data);
                String clientName = ((ChangeImagePack) packet).clientName;

                sendToAllExceptOne(new String(data), clientName);

                break;
            }
        }

    }

    private EntityInfo findEntity(int i, int j) {
        for (int k = 0; k < entities.size(); k++) {
            EntityInfo e = entities.get(k);
            if (e.i == i && e.j == j)
                return e;
        }
        return null;
    }

    public void sendMapToClient(InetAddress address, int clientPORT) {
        String tileString;
        for (int i = 0; i < Constants.MATRIX_SIZE; i++) {
            for (int j = 0; j < Constants.MATRIX_SIZE; j++) {
                tileString = "01";
                tileString += i + "," + j + "," + map.getTiles()[i][j].getType() + "," + map.getTiles()[i][j].getElement() + "," + map.getTiles()[i][j].getMapPic() + "," + map.getTiles()[i][j].getElementPic();
                sendData(tileString, address, clientPORT);
            }
        }
    }

    public void sendAllEnemies(InetAddress address, int port) {
        sendData("12" + (gameClients.size() - 1), address, port);//number of enemies which are going to be sent

        String enemyString;
        for (int i = 0; i < gameClients.size() ; i++) {
            System.out.println(gameClients.get(i).clientName);
            System.out.println(gameClients.get(i).address);
            System.out.println(gameClients.get(i).port);

            System.out.println(address);
            System.out.println(port);

            if (!gameClients.get(i).address.equals(address) || gameClients.get(i).port != port) {
                System.out.println("in if");
                enemyString = "13";
                ClientInfo c = gameClients.get(i);
                enemyString += c.castleI + "," + c.castleJ + "," + c.clientName;
                sendData(enemyString, address, port);
                break;
            }
        }
    }

    private void sendTotalEntitiesToClient(InetAddress address, int clientPort) {
        String entityString;
        for (int i = 0; i < entities.size(); i++) {
            entityString = "04";
            EntityInfo e = entities.get(i);
            entityString += e.i + "," + e.j + "," + e.EntityID + "," + e.clientName;
            sendData(entityString, address, clientPort);
        }
    }

    void sendData(String s, InetAddress address, int clientPORT) {
        byte[] bytes = s.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, clientPORT);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) {
        new GameServer(new Map());
    }

    void sendToAllExceptOne(String s, String clientName) {
        for (int i = 0; i < gameClients.size(); i++) {
            if (!clientName.equals(gameClients.elementAt(i).clientName))
                sendData(s, gameClients.elementAt(i).address, gameClients.elementAt(i).port);
        }
    }
}