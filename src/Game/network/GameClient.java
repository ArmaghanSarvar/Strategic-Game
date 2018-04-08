package Game.network;

import Game.Movable;
import Game.Players.Enemy;
import Game.consts.Constants;
import Game.entities.*;
import Game.graphics.Frame;
import Game.mapworks.Map;
import Game.mapworks.Tile;
import Game.network.packets.*;
import Game.pathfinding.Coordinates;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

import static Game.entities.Entities.totalEntities;

/**
 * Created by ASUS UX360CA on 7/10/2017.
 */
public class GameClient implements Runnable {
    DatagramSocket socket;
    int ServerPORT = 1234;
    InetAddress address;
    public String clientName;

    Tile[][] tiles;
    Coordinates castleCoordinates;
    Map map;

    boolean running;

    int numberOfTilesReceived = 0;
    int numberOfEnemyReceived = 0;
    int EnemyNum = 0;

    public GameClient(String clientName) {
        this.clientName = clientName;
        running = true;

        tiles = new Tile[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
        for (int i = 0; i < Constants.MATRIX_SIZE; i++)
            for (int j = 0; j < Constants.MATRIX_SIZE; j++)
                tiles[i][j] = new Tile();

        makeConnection();
        new Thread(this).start();
        sendClientName();
    }

    void sendClientName() {
        sendData("00" + clientName);
    }

    public void sendData(String s)//you have to give a String which the two first letters stand for the id of packet and the rest of the string shows other information about client
    {
        byte[] bytes = s.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, ServerPORT);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void makeConnection() {
        try {
            socket = new DatagramSocket();//why nothing?
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
            case TILE: {
                packet = new TilePacket(data);
                int i = ((TilePacket) packet).i;
                int j = ((TilePacket) packet).j;
                int tileType = ((TilePacket) packet).type;
                int element = ((TilePacket) packet).element;
                String picName = ((TilePacket) packet).picName;
                String elementName = ((TilePacket) packet).elementName;

                tiles[i][j].setType(tileType);
                tiles[i][j].setElement(element);
                tiles[i][j].setMapPic(picName);
                tiles[i][j].setElementPic(elementName);

                numberOfTilesReceived++;

                if (numberOfTilesReceived == Constants.MATRIX_SIZE * Constants.MATRIX_SIZE) {
                    map = new Map(tiles, this);
                    sendRequestForEnemies();
                }
                break;
            }
            case NEW_ENEMY: {
                packet = new EnemyPacket(data);
                int i = ((EnemyPacket) packet).castleI;
                int j = ((EnemyPacket) packet).castleJ;
                String clientName = ((EnemyPacket) packet).clientName;
                numberOfEnemyReceived++;
                Enemy enemy = new Enemy(map, clientName);
                map.getEnemies().add(enemy);
                map.getTiles()[i][j].setElement(Constants.CASTLE);
                map.getTiles()[i][j].setWalkableOn(false);
                map.getTiles()[i + 1][j].setWalkableOn(false);
                map.getTiles()[i][j + 1].setWalkableOn(false);
                map.getTiles()[i + 1][j + 1].setWalkableOn(false);
                totalEntities.add(new Castle(i, j, enemy));
                if (EnemyNum == numberOfEnemyReceived) {
                    sendRequestForEntites();
                }
                break;
            }
            case PUTCASTLE: {
                packet = new CastlePacket(data);
                int i = ((CastlePacket) packet).i;
                int j = ((CastlePacket) packet).j;
                map.getTiles()[i][j].setElement(Constants.CASTLE);
                map.getTiles()[i][j].setWalkableOn(false);
                map.getTiles()[i + 1][j].setWalkableOn(false);
                map.getTiles()[i][j + 1].setWalkableOn(false);
                map.getTiles()[i + 1][j + 1].setWalkableOn(false);
                Enemy enemy = new Enemy(map, ((CastlePacket) packet).clientName);
                map.getEnemies().add(enemy);
                enemy.castleI = i;
                enemy.castleJ = j;
                totalEntities.add(new Castle(i, j, enemy));
                break;
            }
            case NEWENTITY: {
                packet = new NewEntity(data);
                int i = ((NewEntity) packet).i;
                int j = ((NewEntity) packet).j;
                int entityID = ((NewEntity) packet).entityID;
                String clientName = ((NewEntity) packet).clientName;

                if (entityID == Constants.CASTLE_ID) {
                    Entities.totalEntities.add(new Castle(i, j, map.getEnemy(clientName)));
                } else if (entityID == Constants.HUMAN_ID) {
                    Entities.totalEntities.add(new Human(i, j, map.getEnemy(clientName)));
                } else if (entityID == Constants.PORT_ID) {
                    Entities.totalEntities.add(new Port(i, j, map.getEnemy(clientName)));
                } else if (entityID == Constants.BARRACK_ID) {
                    Entities.totalEntities.add(new Barrack(i, j, map.getEnemy(clientName)));
                } else if (entityID == Constants.QUARRY_ID) {
                    Entities.totalEntities.add(new Quarry(i, j, map.getEnemy(clientName)));
                } else if (entityID == Constants.FISHING_SHIP_ID) {
                    Entities.totalEntities.add(new FishingShip(i, j, map.getEnemy(clientName)));
                } else if (entityID == Constants.SOLDIER_ID) {
                    Entities.totalEntities.add(new Soldier(i, j, map.getEnemy(clientName)));
                }
                break;
            }
            case FINISH: {
                startGame();
                break;
            }
            case ENEMY_NUMBER: {
                packet = new NumberPack(data);
                EnemyNum = ((NumberPack) packet).i;
                if (EnemyNum == 0) {
                    startGame();
                }
                break;
            }
            case MOVE: {
                packet = new MovePacket(data);

                int firstI = ((MovePacket) packet).firstI;
                int fistJ = ((MovePacket) packet).firstJ;
                int destI = ((MovePacket) packet).destI;
                int destJ = ((MovePacket) packet).destJ;

                ((Movable) Entities.entityOfDest(new Coordinates(firstI, fistJ))).move(new Coordinates(destI, destJ));
                break;
            }
            case REMOVE_ENTITY: {

                packet = new RemoveEntityPacket(data);
                int i = ((RemoveEntityPacket) packet).i;
                int j = ((RemoveEntityPacket) packet).j;
                String clientName = ((RemoveEntityPacket) packet).clientName;

                Entity e = Entities.entityOfDest(new Coordinates(i, j));
                totalEntities.remove(e);

                break;
            }
            case END_ELEMENT:{
                packet = new EndElementPacket(data);
                int i = ((EndElementPacket) packet).i;
                int j = ((EndElementPacket) packet).j;

                map.getTiles()[i][j].setElementPic("R/spring/transparent.png");
                map.getTiles()[i][j].setElement(7);
                map.getTiles()[i][j].setWalkableOn(true);

                break;
            }
            case PIC_CHANGE: {

                packet = new ChangeImagePack(data);

                int i = ((ChangeImagePack) packet).i;
                int j = ((ChangeImagePack) packet).j;
                String imageAddress = ((ChangeImagePack) packet).image;

                Entities.entityOfDest(new Coordinates(i,j)).setImage(new ImageIcon(imageAddress));

                break;
            }
        }
    }

    private Coordinates placeCastle(Tile[][] tile) {
        int i = 1, j = 1;
        while (!isAbleToPlaceCastle(i, j)) {
            i = (int) (Math.random() * Constants.MATRIX_SIZE);
            j = (int) (Math.random() * Constants.MATRIX_SIZE);
        }
        return new Coordinates(i, j);
    }

    private boolean isAbleToPlaceCastle(int i, int j) {
        if (tiles[i][j].isWalkableOn() && tiles[i + 1][j].isWalkableOn() && tiles[i][j + 1].isWalkableOn() && tiles[i + 1][j + 1].isWalkableOn())
            return true;
        return false;
    }

    private void sendCastlePlace(int i, int j) {
        sendData("03" + i + "," + j + "," + clientName);
    }

    private void sendRequestForEntites() {
        sendData("07");
    }

    private void sendRequestForEnemies() {
        sendData("34");
    }

    private void startGame() {
        castleCoordinates = placeCastle(tiles);
        int i = castleCoordinates.i;
        int j = castleCoordinates.j;
        map.getTiles()[i][j].setElement(Constants.CASTLE);
        map.getTiles()[i][j].setWalkableOn(false);
        map.getTiles()[i + 1][j].setWalkableOn(false);
        map.getTiles()[i][j + 1].setWalkableOn(false);
        map.getTiles()[i + 1][j + 1].setWalkableOn(false);
        sendCastlePlace(castleCoordinates.i, castleCoordinates.j);
        map.getPlayer().setCastleIJ(i, j);
        new Frame(map);
    }

    public void sendNewEntity(int i, int j, int ID, String clientName) {
        String s = "04";
        s += i + "," + j + "," + ID + "," + clientName;
        sendData(s);
    }

    public void sendMove(String clientName, int firstI, int firstJ, int destI, int destJ) {
        String s = "02";
        s += clientName + "," + firstI + "," + firstJ + "," + destI + "," + destJ;
        sendData(s);
    }

    public void sendOmitCharacter(int i, int j) {
        String s = "05";
        s += i + "," + j + "," + clientName;
        sendData(s);
    }

    public void sendEndElement(int i , int j , String clientName){
        String s = "14";
        s += i + "," + j + "," + clientName;
        sendData(s);
    }
    public void sendImage(int i, int j, String imageAddress, String clientName)
    {
        String s = "16";
        s += i + "," + j + "," + imageAddress + "," + clientName;
        sendData(s);
    }
    public static void main(String[] args) {
        new GameClient("dfs");
    }
}