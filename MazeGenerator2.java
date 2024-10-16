import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;

public class MazeGenerator2 extends JPanel {
    private int[][] maze2;
    private int width;
    private int height;
    private int cellSize;
    private Random random;

    public MazeGenerator2(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.maze2 = new int[width][height];
        this.random = new Random();
        generateMaze2();
    }

    private void generateMaze2() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i % 2 == 0 || j % 2 == 0) {
                    maze2[i][j] = 1; // wall
                } else {
                    maze2[i][j] = 0; // path
                }
            }
        }

        int startX = random.nextInt(width);
        int startY = random.nextInt(height);

        if (startX % 2 == 0) startX++;
        if (startY % 2 == 0) startY++;

        carvePath(startX, startY);
    }

    private void carvePath(int x, int y) {
        maze2[x][y] = 0; // mark as path

        int[] directions = { -2, 0, 2, 0, -2, 2, 0, 2 };
        int[] dx = { -1, 0, 1, 0, -1, 1, 0, 1 };
        int[] dy = { 0, -1, 0, 1, 0, 0, -1, 1 };

        while (true) {
            int dirIndex = random.nextInt(4);
            int newX = x + dx[dirIndex];
            int newY = y + dy[dirIndex];

            if (newX >= 0 && newX < width && newY >= 0 && newY < height && maze2[newX][newY] == 1) {
                maze2[newX - dx[dirIndex] / 2][newY - dy[dirIndex] / 2] = 0; // carve wall
                carvePath(newX, newY);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (maze2[i][j] == 0) {
                    g.setColor(Color.WHITE); // path
                } else {
                    g.setColor(Color.BLACK); // wall
                }
                g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MazeGenerator2 mazeGenerator2 = new MazeGenerator2(21, 11, 20);
                javax.swing.JFrame frame = new javax.swing.JFrame("Maze Generator2");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.add(mazeGenerator2);
                frame.setSize(420, 220);
                frame.setVisible(true);
            }
        });
    }
}