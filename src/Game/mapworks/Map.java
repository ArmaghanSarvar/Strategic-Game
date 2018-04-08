package Game.mapworks;

import Game.Players.Enemy;
import Game.Players.Player;
import Game.consts.Constants;
import Game.network.GameClient;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Armaghan on 7/3/2017.
 */
public class Map {
    JFileChooser jf = new JFileChooser();
    private Tile[][] tiles;
    private final int sizeX= 30, sizeY= 20;
    private int startX, startY, numOfTeams;
    private LinkedList<Enemy> enemies;
    private Player player;
    public GameClient gameClient;
    public boolean multiPlayer;

    public Map(){
        enemies= new LinkedList<>();
        player = new Player(this);
        tiles = new Tile[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
        for(int i = 0; i < Constants.MATRIX_SIZE; i++)
            for (int j=0; j< Constants.MATRIX_SIZE; j++)
            tiles[i][j] = new Tile();

        jf.setDialogTitle("choose your file");
        int returnVal = jf.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            File file= jf.getSelectedFile();
            String a = file.getAbsolutePath();
            loadMap(a);
        }

        startX = 0;
        startY = 0;

        for (int i = 0; i < numOfTeams - 1 ;i++) {
            enemies.add(new Enemy(this));
        }
        setTeamCastles();
    }
    public Map(Tile[][] tiles, GameClient gameClient)//for network
    {
        this.gameClient = gameClient;
        enemies = new LinkedList<>();
        player = new Player(this);
        this.tiles = tiles;
        multiPlayer = true;
    }

    public void loadMap(String a){
        File file = new File(a);
        try {
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < Constants.MATRIX_SIZE; i++)
                for (int j = 0; j < Constants.MATRIX_SIZE; j++)
                    tiles[i][j].setType(Integer.parseInt(scanner.next()));

            for (int i = 0; i < Constants.MATRIX_SIZE; i++)
                for (int j = 0; j < Constants.MATRIX_SIZE; j++) {
                    tiles[i][j].setElement(Integer.parseInt(scanner.next()));
                    if(tiles[i][j].getElement() == Constants.CASTLE) {
                        numOfTeams++;

                    }
                }
            for (int i = 0; i<Constants.MATRIX_SIZE; i++)
                for (int j = 0; j<Constants.MATRIX_SIZE; j++)
                    tiles[i][j].setMapPic(scanner.next());

            for (int i = 0; i<Constants.MATRIX_SIZE; i++)
                for (int j = 0; j<Constants.MATRIX_SIZE; j++)
                    tiles[i][j].setElementPic(scanner.next());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        checkWalkables();
        checkSailables();
    }

    public void panRight(){
        if(startX + sizeX < Constants.MATRIX_SIZE) {
            startX++;
        }
    }
    public void panDown(){
        if(startY + sizeY < Constants.MATRIX_SIZE)
            startY++;
    }

    public void panUp(){
        if(startY > 0)
            startY--;
    }

    public void panLeft(){
        if(startX >0)
            startX--;
    }

    private void setTeamCastles(){
        boolean playerCastles = false;
        int k=0;
        for (int i = 0; i < Constants.MATRIX_SIZE; i++)
            for (int j = 0; j < Constants.MATRIX_SIZE; j++){
                if(tiles[i][j].getElement() == Constants.CASTLE && !playerCastles){
                    player.setCastleIJ(i,j);
                    playerCastles= true;
                }
                else if(tiles[i][j].getElement() == Constants.CASTLE){
                  //  System.out.println("size of enemies " + enemies.size());
                    enemies.get(k).setCastleIJ(i,j);
                    k++;
                }
            }
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    private void checkWalkables(){
        Vector<int[]> coordinates= new Vector<>();
        for (int i=0; i<Constants.MATRIX_SIZE; i++)
            for (int j=0; j<Constants.MATRIX_SIZE; j++){
                if(tiles[i][j].getElement() == Constants.CASTLE)
                    coordinates.add(new int[]{i,j});
            }
        for (int m = 0; m < coordinates.size(); m++) {
            int i = coordinates.get(m)[0];
            int j = coordinates.get(m)[1];
            tiles[i][j+1].setWalkableOn(false);
            tiles[i+1][j].setWalkableOn(false);
            tiles[i+1][j+1].setWalkableOn(false);
        }
    }

    private void checkSailables(){
        for (int i=0; i< Constants.MATRIX_SIZE; i++)
            for (int j=0; j< Constants.MATRIX_SIZE; j++){
            if(getTiles()[i][j].getType() == Constants.LAND)
                tiles[i][j].setSailableOn(false);
                if(getTiles()[i][j].getType() == Constants.SHALLOW_WATER ||
                        getTiles()[i][j].getType() == Constants.DEEP_WATER )
                    tiles[i][j].setSailableOn(true);
            }
    }

    public Player getPlayer() {
        return player;
    }
    public Enemy getEnemy(String clientName)
    {
        for (int i = 0; i <enemies.size() ; i++) {
            if (enemies.get(i).clientName.equals(clientName))
                return enemies.get(i);
        }
        System.out.println("no enemy with this name");
        return null;
    }
}