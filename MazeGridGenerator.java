import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class MazeGridGenerator {
    private static final int GR = 20;
    private static final int GC = 40;

    private int cellWidth;
    private int cellHeight;

    private int[][] grid = new int[GR][GC];
    private boolean[][] visited;
    
    public MazeGridGenerator() {
        setBackground(Color.GREEN);
        generateMaze();

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });

    private void generateMaze() {

        for (int row = 0; row < GR; row++) {
            for (int col = 0; col < GC; col++) {
                grid[row][col] = 0; 
            }
        }

        for (int row = 1; row < GR - 1; row += 2) {
            for (int col = 1; col < GC - 1; col += 2) {
                if (grid[row][col] == 0) {
                    grid[row][col] = 1; // Path
                }
            }
        }

        int cellSizeRow = (GR - 1) / 2;
        int cellSizeCol = (GC - 1) / 2;
        visited = new boolean[cellSizeRow][cellSizeCol];

        Random rand = new Random();
        int startX = rand.nextInt(cellSizeRow);
        int startY = rand.nextInt(cellSizeCol);

        // Start maze generation from a random cell
        generateMazeRecursive(startX, startY);

        // Create loops to have multiple paths
        createLoops(10); // Adjust the number for more or fewer loops
    }
}
