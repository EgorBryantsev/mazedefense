import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Maze {
    private int width, height;
    private int[][] grid;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;

        // Grid size is (2 * width + 1) x (2 * height + 1)
        grid = new int[2 * width + 1][2 * height + 1];

        // Initialize grid with walls
        for (int x = 0; x < grid.length; x++) {
            for (int y=0; y < grid[0].length; y++) {
                grid[x][y] = 0; // 0 represents wall
            }
        }

        generateMaze();

        // Designate tower placement areas
        designateTowerAreas();
    }

    private void generateMaze() {
        boolean[][] visited = new boolean[width][height];

        // Start from cell (0,0)
        generateMaze(0, 0, visited);
    }

    private void generateMaze(int x, int y, boolean[][] visited) {
        visited[x][y] = true;

        // Map cell coordinates to grid coordinates
        int gridX = x * 2 + 1;
        int gridY = y * 2 + 1;

        // Mark the current cell as path
        grid[gridX][gridY] = 1; // 1 represents path

        // Randomly order the directions
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{0, -1}); // Up
        directions.add(new int[]{1, 0}); // Right
        directions.add(new int[]{0, 1}); // Down
        directions.add(new int[]{-1, 0}); // Left

        Collections.shuffle(directions);

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && ny >= 0 && nx < width && ny < height && !visited[nx][ny]) {
                // Remove wall between current cell and neighbor
                int wallX = gridX + dir[0];
                int wallY = gridY + dir[1];
                grid[wallX][wallY] = 1; // Path

                // Recursively generate maze for neighbor cell
                generateMaze(nx, ny, visited);
            }
        }
    }

    private void designateTowerAreas() {
        // Mark cells adjacent to the path as tower placement areas
        for (int x = 1; x < grid.length - 1; x++) {
            for (int y =1; y < grid[0].length - 1; y++) {
                if (grid[x][y] == 1) { // If path
                    // Check adjacent cells
                    for (int dx = -1; dx <=1; dx++) {
                        for (int dy = -1; dy <=1; dy++) {
                            int nx = x + dx;
                            int ny = y + dy;

                            if (nx >= 0 && ny >= 0 && nx < grid.length && ny < grid[0].length) {
                                if (grid[nx][ny] == 0 && !isWallCell(nx, ny)) {
                                    // Mark as tower area
                                    grid[nx][ny] = 2; // 2 represents tower area
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isWallCell(int x, int y) {
        // Walls are at even coordinates in the grid
        return x % 2 == 0 && y % 2 == 0;
    }

    // Getter for the grid
    public int[][] getGrid() {
        return grid;
    }
}
