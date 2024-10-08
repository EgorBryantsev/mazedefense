import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private int[][] grid;

    public MazePanel(int[][] grid) {
        this.grid = grid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int gridWidth = grid.length;
        int gridHeight = grid[0].length;

        int cellWidth = getWidth() / gridWidth;
        int cellHeight = getHeight() / gridHeight;

        for (int x = 0; x < gridWidth; x++) {
            for (int y=0; y < gridHeight; y++) {
                int cell = grid[x][y];
                if (cell == 0) {
                    g.setColor(Color.BLACK); // Wall
                } else if (cell == 1) {
                    g.setColor(Color.WHITE); // Path
                } else if (cell == 2) {
                    g.setColor(Color.GREEN); // Tower area
                }
                g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
            }
        }
    }
}
