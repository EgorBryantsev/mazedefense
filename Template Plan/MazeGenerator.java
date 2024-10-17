import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MazeGenerator extends JPanel {

    private static final int ROWS = 20;
    private static final int COLS = 40;
    private int[][] maze;
    private int panelWidth;
    private int panelHeight;
    private int tileSize;
    private int pathStarts;

    private Map<Integer, Building> buildingMap = new HashMap<>();
    private int nextBuildingID = 1000;

    // Constructor
    public MazeGenerator() {
        maze = new int[ROWS][COLS];
        generateMaze();
        placeGraySquares();  // Place 2x2 gray squares after generating the maze

        // Add mouse listener to handle clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
    }

    // Generate a maze where initially the entire grid is walls (1)
    // and then carve a random path from the first column to the last column.
    private void generateMaze() {
        // Initialize all tiles to walls
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                maze[row][col] = 1;  // 1 represents a wall
            }
        }

        // Randomly choose a starting point in the first column 
        Random rand = new Random();
        int startRow = rand.nextInt(ROWS);
        pathStarts = 4;
        createPath(startRow, 1);
    }

    private void createPath(int startRow, int startCol) {
        Random rand = new Random();
        for (int x = 1; x <= pathStarts + 1; x++) {
            if (x > 1) {
                startCol = rand.nextInt(COLS);
                startRow = rand.nextInt(ROWS - 5) + 5;
            }
            int currentRow = startRow;

            // Random walk to carve a path to the last column
            for (int col = 1; col < COLS - 1; col++) {
                maze[currentRow][col] = 0;  // Carve a path
                // Randomly decide to move up, down, or stay in the same row
                int move = rand.nextInt(3);  // 0: up, 1: same, 2: down
                if (move == 0 && currentRow > 0) {
                    currentRow--;  // Move up 1
                    if (maze[currentRow][col - 1] == 1) {
                        checkBorders(currentRow, col);
                        if (currentRow > 1) {
                            currentRow--;
                            checkBorders(currentRow, col);
                        }
                    } else if (maze[currentRow][col - 1] == 0) {
                        currentRow += 1;
                    }
                } else if (move == 2 && currentRow < ROWS - 1) {
                    currentRow++;  // Move down 1
                    if (maze[currentRow][col - 1] == 1) {
                        checkBorders(currentRow, col);
                        if (currentRow < ROWS - 2) {
                            currentRow++;
                            checkBorders(currentRow, col);
                        }
                    } else if (maze[currentRow][col - 1] == 0) {
                        currentRow -= 1;
                    }
                } else if (move == 1 && col < COLS - 1) {
                    checkBorders(currentRow, col);
                }
                int back = rand.nextInt(10);
                int stepsBack = rand.nextInt(3) + 1;
                if (back == 0 && col > stepsBack + 1) {
                    for (int i = 0; i <= stepsBack; i++) {
                        col--;
                        checkBorders(currentRow, col);
                    }

                }
            }
        }
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                checkSquare(x, y);
            }
        }
        int k = 0;
        while (k < 1) {
            int m = rand.nextInt(ROWS);
            if (maze[m][1] == 0) {
                maze[m][0] = 2;
                k++;
            }
        }
        k = 0;
        while (k < 1) {
            int m = rand.nextInt(ROWS);
            if (maze[m][COLS - 2] == 0) {
                maze[m][COLS - 1] = 3;
                k++;
            }
        }
    }

    private void checkBorders(int randomRow, int randomCol) {
        maze[randomRow][randomCol] = 0;
    }

    private void checkSquare(int x, int y) {
        boolean up = (y > 0) && (maze[y - 1][x] == 0);
        boolean down = (y < ROWS - 1) && (maze[y + 1][x] == 0);
        boolean left = (x > 0) && (maze[y][x - 1] == 0);
        boolean right = (x < COLS - 1) && (maze[y][x + 1] == 0);

        boolean upLeft = (y > 0 && x > 0) && (maze[y - 1][x - 1] == 0);
        boolean downLeft = (y < ROWS - 1 && x > 0) && (maze[y + 1][x - 1] == 0);
        boolean upRight = (y > 0 && x < COLS - 1) && (maze[y - 1][x + 1] == 0);
        boolean downRight = (y < ROWS - 1 && x < COLS - 1) && (maze[y + 1][x + 1] == 0);

        if (maze[y][x] == 0) {
            if ((up && down) && (left && right) && (upLeft && downLeft) && (upRight && downRight)) {
                maze[y][x] = 1;
            } else if ((down) && left && right && downLeft && downRight) {
                maze[y][x] = 1;
            } else if (up && down && right && upRight && downRight) {
                maze[y][x] = 1;
            }
        }
    }

    // Method to place five 2x2 gray squares next to the path
    private void placeGraySquares() {
        Random rand = new Random();
        int squaresPlaced = 0;

        while (squaresPlaced < 10) {
            int row = rand.nextInt(ROWS - 1);
            int col = rand.nextInt(COLS - 1);

            // Check if the 2x2 area is all walls and has a neighboring path
            if (maze[row][col] == 1 && maze[row + 1][col] == 1 &&
                    maze[row][col + 1] == 1 && maze[row + 1][col + 1] == 1 &&
                    connectToPath(row, col)) {

                int buildingID = nextBuildingID++;
                Building building = new Building(4); // initial state n=4

                // Set the tiles for the building
                building.tiles.add(new Point(col, row));
                building.tiles.add(new Point(col, row + 1));
                building.tiles.add(new Point(col + 1, row));
                building.tiles.add(new Point(col + 1, row + 1));

                // Update maze entries
                maze[row][col] = buildingID;
                maze[row + 1][col] = buildingID;
                maze[row][col + 1] = buildingID;
                maze[row + 1][col + 1] = buildingID;

                // Add to buildingMap
                buildingMap.put(buildingID, building);

                squaresPlaced++;
            }
        }
    }

    private boolean connectToPath(int row, int col) {
        for (int r = row - 1; r <= row + 2; r++) {
            for (int c = col - 1; c <= col + 2; c++) {
                if ((r >= 0 && r < ROWS && c >= 0 && c < COLS && maze[r][c] == 0) &&
                        !(r >= 0 && r < ROWS && c >= 0 && c < COLS && maze[r][c] >= 1000)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Update the panel size based on the window size
    private void updateSize() {
        panelWidth = (int) (getWidth() * 0.9);  // 90% of window width
        panelHeight = (int) (getHeight() * 0.9);  // 90% of window height  
        tileSize = Math.min(panelWidth / COLS, panelHeight / ROWS);  // Adjust tile size
    }

    // Drawing the maze
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateSize();
        int xOffset = (getWidth() - panelWidth) / 2;
        int yOffset = (getHeight() - panelHeight) / 2;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int mazeValue = maze[row][col];
                if (mazeValue == 1) {
                    g.setColor(Color.BLACK);  // Walls are black
                    g.fillRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);
                } else if (mazeValue == 0) {
                    g.setColor(Color.WHITE);  // Paths are white
                    g.fillRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);
                } else if (mazeValue == 2) {
                    g.setColor(Color.GREEN);  // Start is green
                    g.fillRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);
                } else if (mazeValue == 3) {
                    g.setColor(Color.YELLOW);  // End is yellow
                    g.fillRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);
                } else if (mazeValue >= 1000) {
                    Building building = buildingMap.get(mazeValue);
                    if (building != null) {
                        g.setColor(Color.GRAY);  // Building is gray
                        g.fillRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);

                        // Only draw the state on the top-left tile of the building
                        if (col == building.tiles.get(0).x && row == building.tiles.get(0).y) {
                            g.setColor(Color.BLACK);
                            String stateStr = String.valueOf(building.state);
                            g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
                            FontMetrics fm = g.getFontMetrics();
                            int textWidth = fm.stringWidth(stateStr);
                            int textHeight = fm.getAscent();
                            int textX = xOffset + col * tileSize + (tileSize - textWidth) / 2;
                            int textY = yOffset + row * tileSize + (tileSize + textHeight) / 2 - fm.getDescent();
                            g.drawString(stateStr, textX, textY);
                        }
                    }
                } else {
                    g.setColor(Color.RED);
                    g.fillRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);
                }
                g.setColor(Color.GRAY);  // Draw grid lines
                g.drawRect(xOffset + col * tileSize, yOffset + row * tileSize, tileSize, tileSize);
            }
        }
    }

    // Handle mouse clicks
    private void handleClick(int x, int y) {
        int xOffset = (getWidth() - panelWidth) / 2;
        int yOffset = (getHeight() - panelHeight) / 2;

        int col = (x - xOffset) / tileSize;
        int row = (y - yOffset) / tileSize;

        if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
            int mazeValue = maze[row][col];
            if (mazeValue >= 1000) {
                Building building = buildingMap.get(mazeValue);
                if (building != null) {
                    building.incrementState();
                    System.out.println("Current state of the tile, maze[][] = " + building.state);

                    // Repaint to reflect the state change
                    repaint();
                }
            }
        }
    }

    // Set up the JFrame
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Maze Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Initial window size
        frame.add(new MazeGenerator());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}

// Building class to represent each building
class Building {
    int state; // current state n
    List<Point> tiles; // list of tiles (col, row) that this building occupies

    public Building(int state) {
        this.state = state;
        this.tiles = new ArrayList<>();
    }

    public void incrementState() {
        state++;
    }
}
