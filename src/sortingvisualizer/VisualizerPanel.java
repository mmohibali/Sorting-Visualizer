package sortingvisualizer;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class VisualizerPanel extends JPanel {
    private int[] array;
    private int highlight1 = -1;
    private int highlight2 = -1;
    private SortingThemes.Theme currentTheme = SortingThemes.CYBERPUNK;

    public VisualizerPanel(int[] array) {
        this.array = array;
        applyTheme(currentTheme);
    }

    public void setArrayToVisualize(int[] array) {
        this.array = array;
        repaint();
    }

    public void setHighlightedBars(int h1, int h2) {
        this.highlight1 = h1;
        this.highlight2 = h2;
        repaint();
    }

    public void resetHighlights() {
        this.highlight1 = -1;
        this.highlight2 = -1;
        repaint();
    }

    public void applyTheme(SortingThemes.Theme theme) {
        this.currentTheme = theme;
        setBackground(theme.background);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int barWidth = Math.max(1, panelWidth / array.length);

        int maxValue = 500;

        for (int i = 0; i < array.length; i++) {
            int barHeight = (int) ((double) array[i] / maxValue * (panelHeight - 40));
            int x = i * barWidth;
            int y = panelHeight - barHeight - 20;

            if (i == highlight1 || i == highlight2) {
                g2d.setColor(currentTheme.barHighlight);
            } else {
                g2d.setColor(currentTheme.barDefault);
            }

            g2d.fillRect(x + 1, y, barWidth - 2, barHeight);
        }
    }
}