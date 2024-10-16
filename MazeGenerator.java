import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class MazeGenerator extends JPanel {
    private static final int GRID_ROWS = 20; // 20 rows tall
    private static final int GRID_COLS = 40; // 40 columns wide

    private int cellWidth; // Calculated based on the window size
    private int cellHeight;

    private int[][] grid = new int[GRID_ROWS][GRID_COLS];
    private boolean[][] visited;

    public MazeGenerator() {
        setBackground(Color.WHITE);
        generateMaze();

        // Add a component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint(); // Repaint when the window is resized
            }
        });
    }

    private void generateMaze() {
        // Initialize grid with walls (0) and paths (1)
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                grid[row][col] = 0; // Wall
            }
        }

        // Place 12 2x2 black boxes evenly distributed
        placeBlackBoxes(12);

        // Set initial paths, avoiding the borders and black boxes
        for (int row = 1; row < GRID_ROWS - 1; row += 2) {
            for (int col = 1; col < GRID_COLS - 1; col += 2) {
                if (grid[row][col] == 0) {
                    grid[row][col] = 1; // Path
                }
            }
        }

        int cellSizeRow = (GRID_ROWS - 1) / 2;
        int cellSizeCol = (GRID_COLS - 1) / 2;
        visited = new boolean[cellSizeRow][cellSizeCol];

        Random rand = new Random();
        int startX = rand.nextInt(cellSizeRow);
        int startY = rand.nextInt(cellSizeCol);

        // Start maze generation from a random cell
        generateMazeRecursive(startX, startY);

        // Create loops to have multiple paths
        createLoops(5); // Adjust the number for more or fewer loops
    }

    private void generateMazeRecursive(int x, int y) {
        visited[x][y] = true;

        // Directions: 0 - North, 1 - South, 2 - East, 3 - West
        Integer[] dirs = {0, 1, 2, 3};
        Collections.shuffle(java.util.Arrays.asList(dirs));

        for (int dir : dirs) {
            int nx = x, ny = y;
            int betweenRow = x * 2 + 1;
            int betweenCol = y * 2 + 1;

            switch (dir) {
                case 0: // North
                    nx = x - 1;
                    betweenRow -= 1;
                    break;
                case 1: // South
                    nx = x + 1;
                    betweenRow += 1;
                    break;
                case 2: // East
                    ny = y + 1;
                    betweenCol += 1;
                    break;
                case 3: // West
                    ny = y - 1;
                    betweenCol -= 1;
                    break;
            }

            if (nx >= 0 && ny >= 0 && nx < visited.length && ny < visited[0].length && !visited[nx][ny]) {
                if (betweenRow > 0 && betweenRow < GRID_ROWS - 1 && betweenCol > 0 && betweenCol < GRID_COLS - 1) {
                    grid[betweenRow][betweenCol] = 1; // Remove wall
                }
                generateMazeRecursive(nx, ny);
            }
        }
    }

    private void placeBlackBoxes(int count) {
        int boxesPlaced = 0;
        int rowsPerBox = GRID_ROWS / 4;
        int colsPerBox = GRID_COLS / 4;

        for (int i = 0; i < 4 && boxesPlaced < count; i++) {
            for (int j = 0; j < 3 && boxesPlaced < count; j++) {
                int rowStart = i * rowsPerBox + rowsPerBox / 2;
                int colStart = j * colsPerBox + colsPerBox / 2;

                // Ensure the box is within bounds and doesn't overwrite borders
                if (rowStart + 1 < GRID_ROWS - 1 && colStart + 1 < GRID_COLS - 1) {
                    grid[rowStart][colStart] = 0; // Wall
                    grid[rowStart][colStart + 1] = 0; // Wall
                    grid[rowStart + 1][colStart] = 0; // Wall
                    grid[rowStart + 1][colStart + 1] = 0; // Wall
                    boxesPlaced++;
                }
            }
        }
    }

    private void createLoops(int numberOfLoops) {
        Random rand = new Random();
        int loopsCreated = 0;

        while (loopsCreated < numberOfLoops) {
            int row = rand.nextInt((GRID_ROWS - 3) / 2) * 2 + 1;
            int col = rand.nextInt((GRID_COLS - 3) / 2) * 2 + 1;

            ArrayList<int[]> neighbors = new ArrayList<>();

            // Check all four directions
            if (row > 2) neighbors.add(new int[]{row - 2, col}); // North
            if (row < GRID_ROWS - 3) neighbors.add(new int[]{row + 2, col}); // South
            if (col > 2) neighbors.add(new int[]{row, col - 2}); // West
            if (col < GRID_COLS - 3) neighbors.add(new int[]{row, col + 2}); // East

            if (!neighbors.isEmpty()) {
                int[] neighbor = neighbors.get(rand.nextInt(neighbors.size()));
                int betweenRow = (row + neighbor[0]) / 2;
                int betweenCol = (col + neighbor[1]) / 2;
                if (grid[betweenRow][betweenCol] == 0) {
                    grid[betweenRow][betweenCol] = 1; // Remove wall to create loop
                    loopsCreated++;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        cellWidth = getWidth() / GRID_COLS;
        cellHeight = getHeight() / GRID_ROWS;

        // Draw the grid
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                if (grid[row][col] == 1) {
                    g.setColor(Color.WHITE); // Path
                } else {
                    g.setColor(Color.BLACK); // Wall
                }
                g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }

        // Optional: Draw grid lines
        g.setColor(Color.GRAY);
        for (int i = 0; i <= GRID_COLS; i++) {
            g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
        }
        for (int i = 0; i <= GRID_ROWS; i++) {
            g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Generator");
        MazeGenerator maze = new MazeGenerator();
        frame.add(maze);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}