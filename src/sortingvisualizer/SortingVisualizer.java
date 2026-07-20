package sortingvisualizer;
import java.util.function.BiConsumer;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortingVisualizer extends JFrame {
    private static final int ARRAY_SIZE = 50;
    private static final int ARRAY_MIN_VALUE = 1;
    private static final int ARRAY_MAX_VALUE = 500;

    private int[] array;
    private VisualizerPanel visualizerPanel;

    public SortingVisualizer() {
        setTitle("Sorting Algorithms Visualizer");
        setSize(760, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        array = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);

        visualizerPanel = new VisualizerPanel(array);
        add(visualizerPanel);

        JButton startBubbleSortButton = new JButton("Bubble Sort");
        startBubbleSortButton.addActionListener(e -> {
            new SortingTask(SortingAlgorithms::bubbleSort).execute();
        });

        JButton startInsertionSortButton = new JButton("Insertion Sort");
        startInsertionSortButton.addActionListener(e -> {
            new SortingTask(SortingAlgorithms::insertionSort).execute();
        });

        JButton startSelectionSortButton = new JButton("Selection Sort");
        startSelectionSortButton.addActionListener(e -> {
            new SortingTask(SortingAlgorithms::selectionSort).execute();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(startBubbleSortButton);
        controlPanel.add(startInsertionSortButton);
        controlPanel.add(startSelectionSortButton);
        add(controlPanel, BorderLayout.SOUTH);
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

