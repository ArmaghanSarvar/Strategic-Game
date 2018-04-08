package MapEditor;

/**
 * Created by Armaghan on 5/20/2017.
 */
public class StackElements {
    int[][] matrix, elements;

    public StackElements(int[][] matrix, int[][] elements) {
        this.matrix = matrix;
        this.elements = elements;
    }


    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getElements() {
        return elements;
    }


    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void setElements(int[][] elements) {
        this.elements = elements;
    }
}
