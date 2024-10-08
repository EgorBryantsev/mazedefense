import javax.swing.*;

public class MazeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int mazeWidth = 40;  // 40 blocks wide
            int mazeHeight = 20; // 20 blocks high
            Maze maze = new Maze(mazeWidth, mazeHeight);
            int[][] mazeGrid = maze.getGrid();

            JFrame frame = new JFrame("Maze Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            MazePanel mazePanel = new MazePanel(mazeGrid);

            // Ensure the maze fills the window
            frame.add(mazePanel);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
            frame.setVisible(true);

            // Repaint the mazePanel when the window is resized
            frame.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    mazePanel.repaint();
                }
            });
        });
    }
}
