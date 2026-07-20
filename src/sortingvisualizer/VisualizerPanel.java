package sortingvisualizer;

import javax.swing.*;
import java.awt.*;

public class VisualizerPanel extends JPanel {
    private static final int BAR_WIDTH = 10;
    private static final int BAR_SPACING = 5;
    private int[] arrayToVisualize;
    private int highlightedBar1 = -1;
    private int highlightedBar2 = -1;

    public VisualizerPanel(int[] arrayToVisualize) {
        this.arrayToVisualize = arrayToVisualize;
    }

    public void setHighlightedBars(int barIndex1, int barIndex2) {
        highlightedBar1 = barIndex1;
        highlightedBar2 = barIndex2;

        // Repaint the panel after setting highlighted bars
        repaint();
        try {
            Thread.sleep(100); // Adjust the delay for visualization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < arrayToVisualize.length; i++) {
            int barHeight = arrayToVisualize[i];
            int x = i * (BAR_WIDTH + BAR_SPACING);
            int y = getHeight() - barHeight;

            if (i == highlightedBar1 || i == highlightedBar2) {
                g.setColor(Color.RED); // Highlight the swapping bars in red
            } else {
                g.setColor(Color.GREEN);
            }

            g.fillRect(x, y, BAR_WIDTH, barHeight);
        }
    }
}
