package sortingvisualizer;

import java.util.function.BiConsumer;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortingVisualizer extends JFrame {
    private static final int ARRAY_SIZE = 50;
    private static final int ARRAY_MIN_VALUE = 5;
    private static final int ARRAY_MAX_VALUE = 500;

    private int[] array;
    private VisualizerPanel visualizerPanel;

    public SortingVisualizer() {
        setTitle("Sorting Algorithms Visualizer");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Dark theme background for main window
        getContentPane().setBackground(new Color(30, 30, 46));
        setLayout(new BorderLayout());

        array = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);

        visualizerPanel = new VisualizerPanel(array);
        add(visualizerPanel, BorderLayout.CENTER);

        // Control Panel (Top Header Bar)
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(24, 24, 37));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JLabel titleLabel = new JLabel("Sorting Visualizer ");
        titleLabel.setForeground(new Color(205, 214, 244));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        controlPanel.add(titleLabel);

        // Buttons
        JButton bubbleBtn = createStyledButton("Bubble Sort", new Color(137, 180, 250));
        bubbleBtn.addActionListener(e -> new SortingTask(SortingAlgorithms::bubbleSort).execute());

        JButton insertionBtn = createStyledButton("Insertion Sort", new Color(166, 227, 161));
        insertionBtn.addActionListener(e -> new SortingTask(SortingAlgorithms::insertionSort).execute());

        JButton selectionBtn = createStyledButton("Selection Sort", new Color(250, 179, 135));
        selectionBtn.addActionListener(e -> new SortingTask(SortingAlgorithms::selectionSort).execute());

        controlPanel.add(bubbleBtn);
        controlPanel.add(insertionBtn);
        controlPanel.add(selectionBtn);

        add(controlPanel, BorderLayout.NORTH);
    }

    private JButton createStyledButton(String text, Color accentColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(accentColor);
        button.setForeground(new Color(17, 17, 27)); // Dark text for contrast
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private int[] generateRandomArray(int size, int minValue, int maxValue) {
        int[] array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }

        return array;
    }

    private class SortingTask extends SwingWorker<Void, Void> {
        private final BiConsumer<int[], VisualizerPanel> sortingAlgorithm;

        public SortingTask(BiConsumer<int[], VisualizerPanel> sortingAlgorithm) {
            this.sortingAlgorithm = sortingAlgorithm;
        }

        @Override
        protected Void doInBackground() {
            sortingAlgorithm.accept(array, visualizerPanel);
            return null;
        }

        @Override
        protected void done() {
            visualizerPanel.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer visualizer = new SortingVisualizer();
            visualizer.setVisible(true);
        });
    }
}