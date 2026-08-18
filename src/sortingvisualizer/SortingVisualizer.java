package sortingvisualizer;

import java.util.function.BiConsumer;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SortingVisualizer extends JFrame {
    private static final int ARRAY_SIZE = 50;
    private static final int ARRAY_MIN_VALUE = 5;
    private static final int ARRAY_MAX_VALUE = 500;

    private int[] array;
    private VisualizerPanel visualizerPanel;
    private JComboBox<String> arrayTypeSelector;
    private JSlider speedSlider;
    private JButton pauseBtn, resumeBtn, stopBtn;
    private JLabel comparisonsLabel, swapsLabel, timeLabel;
    private final SortingMetrics metrics = new SortingMetrics();
    private Timer metricsUpdateTimer;
    private final List<JComponent> controlsToDisable = new ArrayList<>();

    public SortingVisualizer() {
        setTitle("Sorting Algorithms Visualizer");
        setSize(1024, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(new Color(30, 30, 46));
        setLayout(new BorderLayout());

        array = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);

        visualizerPanel = new VisualizerPanel(array);
        add(visualizerPanel, BorderLayout.CENTER);

        // North Container for Controls & Metrics Dashboard
        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));

        // Metrics Dashboard Panel
        JPanel metricsPanel = new JPanel();
        metricsPanel.setBackground(new Color(17, 17, 27));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        metricsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 2));

        comparisonsLabel = createMetricLabel("Comparisons: 0");
        swapsLabel = createMetricLabel("Swaps: 0");
        timeLabel = createMetricLabel("Time: 0 ms");

        metricsPanel.add(comparisonsLabel);
        metricsPanel.add(swapsLabel);
        metricsPanel.add(timeLabel);
        northContainer.add(metricsPanel);

        // Top Control Panel (Algorithm Selectors & Execution State)
        JPanel topControlPanel = new JPanel();
        topControlPanel.setBackground(new Color(24, 24, 37));
        topControlPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel titleLabel = new JLabel("Visualizer ");
        titleLabel.setForeground(new Color(205, 214, 244));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topControlPanel.add(titleLabel);

        JButton bubbleBtn = createStyledButton("Bubble Sort", new Color(137, 180, 250));
        bubbleBtn.addActionListener(e -> runSortingTask((arr, panel) -> SortingAlgorithms.bubbleSort(arr, panel, speedSlider, metrics)));

        JButton insertionBtn = createStyledButton("Insertion Sort", new Color(166, 227, 161));
        insertionBtn.addActionListener(e -> runSortingTask((arr, panel) -> SortingAlgorithms.insertionSort(arr, panel, speedSlider, metrics)));

        JButton selectionBtn = createStyledButton("Selection Sort", new Color(250, 179, 135));
        selectionBtn.addActionListener(e -> runSortingTask((arr, panel) -> SortingAlgorithms.selectionSort(arr, panel, speedSlider, metrics)));

        topControlPanel.add(bubbleBtn);
        topControlPanel.add(insertionBtn);
        topControlPanel.add(selectionBtn);

        // Execution State Buttons
        pauseBtn = createStyledButton("Pause", new Color(249, 226, 175));
        pauseBtn.addActionListener(e -> {
            SortingAlgorithms.pauseSorting();
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
        });
        pauseBtn.setEnabled(false);

        resumeBtn = createStyledButton("Resume", new Color(148, 226, 213));
        resumeBtn.addActionListener(e -> {
            SortingAlgorithms.resumeSorting();
            resumeBtn.setEnabled(false);
            pauseBtn.setEnabled(true);
        });
        resumeBtn.setEnabled(false);

        stopBtn = createStyledButton("Stop", new Color(243, 139, 168));
        stopBtn.addActionListener(e -> {
            SortingAlgorithms.stopSorting();
            stopActiveMetrics();
            visualizerPanel.resetHighlights();
            setControlsEnabled(true);
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(false);
            stopBtn.setEnabled(false);
        });
        stopBtn.setEnabled(false);

        topControlPanel.add(pauseBtn);
        topControlPanel.add(resumeBtn);
        topControlPanel.add(stopBtn);

        controlsToDisable.add(bubbleBtn);
        controlsToDisable.add(insertionBtn);
        controlsToDisable.add(selectionBtn);

        northContainer.add(topControlPanel);
        add(northContainer, BorderLayout.NORTH);

        // Bottom Control Panel (Array Distribution & Speed Sliders)
        JPanel bottomControlPanel = new JPanel();
        bottomControlPanel.setBackground(new Color(24, 24, 37));
        bottomControlPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        bottomControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JLabel arrayTypeLabel = new JLabel("Distribution:");
        arrayTypeLabel.setForeground(new Color(205, 214, 244));
        arrayTypeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bottomControlPanel.add(arrayTypeLabel);

        String[] distributions = {"Random", "Nearly Sorted", "Reversed", "Few Unique"};
        arrayTypeSelector = new JComboBox<>(distributions);
        arrayTypeSelector.setBackground(new Color(49, 50, 68));
        arrayTypeSelector.setForeground(new Color(205, 214, 244));
        arrayTypeSelector.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bottomControlPanel.add(arrayTypeSelector);

        JButton generateBtn = createStyledButton("Generate Array", new Color(245, 194, 231));
        generateBtn.addActionListener(e -> generateNewArray());
        bottomControlPanel.add(generateBtn);

        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setForeground(new Color(205, 214, 244));
        speedLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bottomControlPanel.add(speedLabel);

        speedSlider = new JSlider(1, 150, 30);
        speedSlider.setBackground(new Color(24, 24, 37));
        speedSlider.setForeground(new Color(205, 214, 244));
        speedSlider.setFocusable(false);
        bottomControlPanel.add(speedSlider);

        controlsToDisable.add(arrayTypeSelector);
        controlsToDisable.add(generateBtn);

        add(bottomControlPanel, BorderLayout.SOUTH);

        // Swing Timer to refresh metrics display every 50ms during sorting
        metricsUpdateTimer = new Timer(50, e -> updateMetricsDisplay());
    }

    private JLabel createMetricLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(166, 227, 161));
        label.setFont(new Font("Monospaced", Font.BOLD, 13));
        return label;
    }

    private JButton createStyledButton(String text, Color accentColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(accentColor);
        button.setForeground(new Color(17, 17, 27));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setControlsEnabled(boolean enabled) {
        for (JComponent control : controlsToDisable) {
            control.setEnabled(enabled);
        }
    }

    private void runSortingTask(BiConsumer<int[], VisualizerPanel> algorithm) {
        setControlsEnabled(false);
        pauseBtn.setEnabled(true);
        resumeBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        metrics.reset();
        metrics.startTimer();
        metricsUpdateTimer.start();

        new SortingTask(algorithm).execute();
    }

    private void updateMetricsDisplay() {
        comparisonsLabel.setText("Comparisons: " + metrics.getComparisons());
        swapsLabel.setText("Swaps: " + metrics.getSwaps());
        timeLabel.setText("Time: " + metrics.getElapsedTime() + " ms");
    }

    private void stopActiveMetrics() {
        metrics.stopTimer();
        metricsUpdateTimer.stop();
        updateMetricsDisplay();
    }

    private void generateNewArray() {
        String selected = (String) arrayTypeSelector.getSelectedItem();
        if (selected == null) selected = "Random";

        switch (selected) {
            case "Nearly Sorted":
                array = generateNearlySortedArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
                break;
            case "Reversed":
                array = generateReversedArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
                break;
            case "Few Unique":
                array = generateFewUniqueArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
                break;
            case "Random":
            default:
                array = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
                break;
        }

        visualizerPanel.setArrayToVisualize(array);
        metrics.reset();
        updateMetricsDisplay();
    }

    private int[] generateRandomArray(int size, int minValue, int maxValue) {
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }
        return arr;
    }

    private int[] generateNearlySortedArray(int size, int minValue, int maxValue) {
        int[] arr = generateRandomArray(size, minValue, maxValue);
        Arrays.sort(arr);
        Random random = new Random();
        int swaps = Math.max(1, size / 20);
        for (int k = 0; k < swaps; k++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        return arr;
    }

    private int[] generateReversedArray(int size, int minValue, int maxValue) {
        int[] arr = generateRandomArray(size, minValue, maxValue);
        Arrays.sort(arr);
        for (int i = 0; i < size / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[size - 1 - i];
            arr[size - 1 - i] = temp;
        }
        return arr;
    }

    private int[] generateFewUniqueArray(int size, int minValue, int maxValue) {
        int[] arr = new int[size];
        Random random = new Random();
        int[] uniqueValues = new int[5];
        for (int i = 0; i < uniqueValues.length; i++) {
            uniqueValues[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }
        for (int i = 0; i < size; i++) {
            arr[i] = uniqueValues[random.nextInt(uniqueValues.length)];
        }
        return arr;
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
            stopActiveMetrics();
            visualizerPanel.resetHighlights();
            setControlsEnabled(true);
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(false);
            stopBtn.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer visualizer = new SortingVisualizer();
            visualizer.setVisible(true);
        });
    }
}