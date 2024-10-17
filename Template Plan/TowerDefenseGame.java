import javax.swing.*;
import java.awt.*;

public class TowerDefenseGame extends JFrame {

    public TowerDefenseGame() {
        setTitle("Tower Defense Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen

        // Add the game panel (which will contain the maze, towers, etc.)
        add(new GamePanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TowerDefenseGame game = new TowerDefenseGame();
            game.setVisible(true);
        });
    }
}

class GamePanel extends JPanel {
    private int rows = 10;
    private int cols = 10;
    private Cell[][] grid;
    
    public GamePanel() {
        grid = new Cell[rows][cols]; // Initialize grid
        
        // Populate grid with empty cells for now
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Calculate cell size based on the panel's current size
        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].draw(g, cellWidth, cellHeight);
            }
        }
    }
}

class Cell {
    private int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void draw(Graphics g, int cellWidth, int cellHeight) {
        // For now, let's just draw each cell as a rectangle
        g.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
    }
}
