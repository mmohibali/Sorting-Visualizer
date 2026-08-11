package sortingvisualizer;

import javax.swing.*;
import java.awt.*;

public class VisualizerPanel extends JPanel {
    private int[] arrayToVisualize;
    private int highlightedBar1 = -1;
    private int highlightedBar2 = -1;

    public VisualizerPanel(int[] arrayToVisualize) {
        this.arrayToVisualize = arrayToVisualize;
        setBackground(new Color(30, 30, 46));
    }

    public void setArrayToVisualize(int[] arrayToVisualize) {
        this.arrayToVisualize = arrayToVisualize;
        resetHighlights(); // Clears lingering red bars when switching arrays
    }

    public void resetHighlights() {
        this.highlightedBar1 = -1;
        this.highlightedBar2 = -1;
        repaint();
    }

    public void setHighlightedBars(int barIndex1, int barIndex2) {
        this.highlightedBar1 = barIndex1;
        this.highlightedBar2 = barIndex2;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (arrayToVisualize == null || arrayToVisualize.length == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int totalBars = arrayToVisualize.length;
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        double barWidth = (double) panelWidth / totalBars;

        int maxValue = 1;
        for (int val : arrayToVisualize) {
            if (val > maxValue) {
                maxValue = val;
            }
        }

        for (int i = 0; i < totalBars; i++) {
            double scaledHeight = ((double) arrayToVisualize[i] / maxValue) * (panelHeight * 0.85);

            int x = (int) (i * barWidth);
            int y = panelHeight - (int) scaledHeight;
            int width = Math.max(1, (int) barWidth - 1);
            int height = (int) scaledHeight;

            if (i == highlightedBar1 || i == highlightedBar2) {
                g2d.setColor(new Color(235, 77, 75)); // Red highlight
            } else {
                g2d.setColor(new Color(108, 92, 231)); // Purple default
            }

            g2d.fillRect(x, y, width, height);
        }
    }
}