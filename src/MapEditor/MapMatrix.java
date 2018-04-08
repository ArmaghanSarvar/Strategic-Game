 package MapEditor;

 import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

 /**
 * Created by Armaghan on 5/15/2017.
 */

public class MapMatrix {
    private int[][] matrix, elements;
//    elements is for storing fish, tree, goldmine, mountain
    private int startingPointX, startingPointY, size;
    private Cursor cursor;
    private Stack<StackElements> primaryStack;
    private Stack<StackElements> secondaryStack;
    int pi= -1, pj=-1, i=0, j=0, pItem =-1;
    private String[][] landPics;

    public MapMatrix(Cursor c){
        matrix = new int[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
        elements = new int[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
        landPics = new String[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
        size = 20;
        startingPointX =0;
        startingPointY =0;
        cursor = c;
        for( int i=0; i< Constants.MATRIX_SIZE; i++)
            for (int j=0; j< Constants.MATRIX_SIZE ; j++) {
                matrix[i][j] = Constants.SHALLOW_WATER;
                elements[i][j] = 7;
                landPics[i][j] = "";
            }
        primaryStack = new Stack<>();
        secondaryStack = new Stack<>();
    }

    public int[][] getElements() {
        return elements;
    }

    public void updateMatrix(int x, int y){
         i= xToi(x);
         j= yToj(y);
        if( !(i == pi && j == pj && (pItem == cursor.item)) && validIJ(i, j)) {
            if (cursor.getItem() == Constants.SHALLOW_WATER ||
                    cursor.getItem() == Constants.DEEP_WATER || cursor.getItem() == Constants.LAND) {
                matrix[i][j] = cursor.item;
                elements[i][j] = 7;
                if (cursor.getItem() != Constants.LAND) {
                    landPics[i][j] = "";
                }
            } else {
                if (cursor.getItem() == Constants.TREE && !hasCastleAround(i,j)) {
                    if (landPics[i][j].contains("1111"))
                        elements[i][j] = Constants.TREE;
                } else if (cursor.getItem() == Constants.FISH) {
                    if (matrix[i][j] == Constants.DEEP_WATER || matrix[i][j] == Constants.SHALLOW_WATER)
                        elements[i][j] = Constants.FISH;
                } else if (cursor.getItem() == Constants.GOLD_MINE) {
                    if (landPics[i][j].contains("1111"))
                        elements[i][j] = Constants.GOLD_MINE;
                } else if (cursor.getItem() == Constants.MOUNTAIN) {
                    if (matrix[i][j] == Constants.LAND)
                        elements[i][j] = Constants.MOUNTAIN;
                }
                else if(cursor.getItem() == Constants.Castle){
                    if(isCastlePlaceable(i,j) && !hasCastleAround(i,j))
                        elements[i][j] = Constants.Castle;
                    else if(isCastlePlaceable(i-1, j-1) && !hasCastleAround(i-1, j-1))
                        elements[i-1][j-1]= Constants.Castle;
                    else if(isCastlePlaceable(i, j-1) && !hasCastleAround(i, j-1))
                        elements[i][j-1]= Constants.Castle;
                    else if(isCastlePlaceable(i-1, j) && !hasCastleAround(i-1, j))
                        elements[i-1][j] = Constants.Castle;
                }
            }
            pi= i;
            pj =j;
            pItem = cursor.item;
            primaryStack.push(new StackElements(matrix, elements));
        }
        updateLandpics();
        scan();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getSize() {
        return size;
    }

     public String[][] getLandPics() {
         return landPics;
     }

     private boolean isCastlePlaceable(int i, int j){

        if (landPics[i][j].contains("1111") && landPics[i+1][j].contains("1111") &&
                landPics[i][j+1].contains("1111") && landPics[i+1][j+1].contains("1111"))
        if(elements[i][j] == 7 && elements[i+1][j] == 7 && elements[i][j+1]==7 && elements[i+1][j+1]==7)
            return true;
        return false;
     }

     private boolean hasCastleAround(int i, int j){
         return (validIJ(i,j) && (elements[i][j] == Constants.Castle || elements[i-1][j-1] == Constants.Castle ||
                 elements[i][j-1] == Constants.Castle || elements[i-1][j] == Constants.Castle));
     }

     private void updateLandpics(){
        String picName = "";
       for (int i=0 ; i< Constants.MATRIX_SIZE; i++)
           for (int j=0; j< Constants.MATRIX_SIZE ; j++){
           if(matrix[i][j] == Constants.LAND)
           {
               if(hasUpLand(i, j ))
                   picName += "1";
               else
                   picName += "0";

               if (hasRightLand(i, j))
                   picName += "1";
               else
                   picName +="0";

               if(hasDownLand(i , j))
                   picName += "1";
               else
                   picName+= "0";

               if (hasLeftLand(i, j))
                   picName += "1";
               else
                   picName +="0";

               picName+= ".png";

               landPics[i][j] = picName;
               }
               picName = "";

           }
    }

    private void scan(){
         for (int i=0; i< Constants.MATRIX_SIZE; i++)
             for (int j =0 ; j< Constants.MATRIX_SIZE ; j++){
             if(matrix[i][j] == Constants.LAND && !landPics[i][j].equals("1111.png"))
                 elements[i][j] = 7;
             }
    }

    private int xToi(int x){
         return startingPointX + (x/(Constants.MAP_WIDTH / size ));
    }

    private int yToj(int y){
       return startingPointY + (y/(Constants.MAP_HEIGHT / size) );

    }

    public void zoomIn(){
        if(size > 1)
            size--;
    }

    public void zoomOut(){
        if(size< Constants.MATRIX_SIZE)
            size++;
    }


    public void panDown(){
        if(startingPointY + size < Constants.MATRIX_SIZE)
            startingPointY++;
    }

    public void panUp(){
        if(startingPointY > 0)
            startingPointY--;
    }

    public void panRight(){
        if(startingPointX + size < Constants.MATRIX_SIZE )
            startingPointX++;
    }

    public void panLeft(){
        if(startingPointX > 0)
            startingPointX--;
    }

    private boolean validIJ(int i, int j) {
        if (i < 0 || j < 0)
            return false;
        if (i >= Constants.MATRIX_SIZE || j >= Constants.MATRIX_SIZE)
            return false;
        return true;
    }

    public int getStartingPointX() {
        return startingPointX;
    }


     public int getStartingPointY() {
        return startingPointY;
    }

     public void saveMap(String path) {
         PrintWriter printWriter;
         File savingFile = new File(path);
         int[][] temp1 = new int[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];
         int[][] temp2 = new int[Constants.MATRIX_SIZE][Constants.MATRIX_SIZE];

         for (int i = 0; i < Constants.MATRIX_SIZE; i++)
             System.arraycopy(matrix[i], 0, temp1[i], 0, Constants.MATRIX_SIZE);

         for (int i=0; i< Constants.MATRIX_SIZE; i++)
             System.arraycopy(elements[i], 0, temp2[i],0 ,Constants.MATRIX_SIZE  );
         try {
             savingFile.createNewFile();
         } catch (IOException e) {
             e.printStackTrace();
         }
         try {
             printWriter = new PrintWriter(path + ".txt");
             printArrayToFile(printWriter, temp1);
             printArrayToFile(printWriter, temp2);
             printMapPic(printWriter, temp1);
             printElementPic(printWriter, temp2);
             printWriter.close();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
     }

     private void printMapPic(PrintWriter printWriter, int[][] temp1) {
         for (int i = 0; i < Constants.MATRIX_SIZE; i++){
             for (int j = 0; j < Constants.MATRIX_SIZE; j++) {
                 printWriter.print(Images.getImageName(temp1[i][j]));
                 printWriter.print(landPics[i][j]);
                 printWriter.print(" ");
             }
         printWriter.println();
     }
     }

     private void printElementPic(PrintWriter printWriter, int[][] temp2) {
         for (int i = 0; i< Constants.MATRIX_SIZE ; i++)
         {
             for (int j=0; j< Constants.MATRIX_SIZE; j++) {
                 if(temp2[i][j] != 9)
                     printWriter.print(Images.getImageName(temp2[i][j]) + " ");
                 else printWriter.print("transparent.png ");
             }
             printWriter.println();
         }
     }

     private void printArrayToFile(PrintWriter printWriter, int[][] temp1) {
         for (int i = 0; i < Constants.MATRIX_SIZE; i++) {
             for (int j = 0; j < Constants.MATRIX_SIZE; j++)
                 printWriter.print(temp1[i][j] + " ");
             printWriter.println();
         }
     }

     public void loadMap(String str) {
         File file;
         Scanner scanner;
         try {
             file = new File(str);
             scanner = new Scanner(file);
             for (int i = 0; i < Constants.MATRIX_SIZE; i++)
                 for (int j = 0; j < Constants.MATRIX_SIZE; j++)
                     matrix[i][j] = Integer.parseInt(scanner.next());

             for (int i = 0; i < Constants.MATRIX_SIZE; i++)
                 for (int j = 0; j < Constants.MATRIX_SIZE; j++)
                     elements[i][j] = Integer.parseInt(scanner.next());

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
     }

     private boolean hasUpLand(int i, int j){
        if(i >= 0 && j -1 >= 0 && i < Constants.MATRIX_SIZE && j -1<Constants.MATRIX_SIZE)
        if(matrix[i][j -1 ]== Constants.LAND)
            return true;
        return false;

     }

     private boolean hasRightLand(int i, int j){
         if(i +1 >= 0 && j >=0 && i+1<Constants.MATRIX_SIZE && j <Constants.MATRIX_SIZE)
             if(matrix[i + 1][j]== Constants.LAND)
                 return true;
         return false;

     }

     private boolean hasDownLand(int i, int j){
         if(i >= 0 && j +1>=0 && i <Constants.MATRIX_SIZE && j+1<Constants.MATRIX_SIZE)
             if(matrix[i ][j + 1]== Constants.LAND)
                 return true;
         return false;

     }

     private boolean hasLeftLand(int i, int j){
         if(i -1 >= 0 && j >= 0 && i -1<Constants.MATRIX_SIZE && j < Constants.MATRIX_SIZE)
             if(matrix[i -1][j]== Constants.LAND)
                 return true;
         return false;
     }

//    public void pushToPrimary(StackElements s){
//        primaryStack.push(s);
//    }
//
//    public void undo(){
//        if(primaryStack.size() > 1)
//            secondaryStack.push(primaryStack.pop());
//        matrix = primaryStack.peek().getMatrix();
//        elements= primaryStack.peek().getElements();
//    }
//
//    public void reDo(){
//        if(secondaryStack.size() > 0)
//            primaryStack.push(secondaryStack.pop());
//
//        matrix = primaryStack.peek().getMatrix();
//        elements= primaryStackpeek().getElements();
//    }
}