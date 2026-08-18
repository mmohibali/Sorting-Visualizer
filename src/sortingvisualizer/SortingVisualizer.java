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
    private JComboBox<SortingThemes.Theme> themeSelector;
    private JSlider speedSlider;
    private JCheckBox soundToggle;
    private JButton pauseBtn, resumeBtn, stopBtn;
    private JLabel comparisonsLabel, swapsLabel, accessesLabel, timeLabel;
    private JLabel timeBadgeLabel, spaceBadgeLabel;
    private final SortingMetrics metrics = new SortingMetrics();
    private Timer metricsUpdateTimer;
    private final List<JComponent> controlsToDisable = new ArrayList<>();

    public SortingVisualizer() {
        setTitle("Sorting Algorithms Visualizer");
        setSize(1280, 720);
        setMinimumSize(new Dimension(1000, 600));
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

        // Day 12: Real-Time Analytics Dashboard Panel with Badges
        JPanel metricsPanel = new JPanel();
        metricsPanel.setBackground(new Color(17, 17, 27));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        metricsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));

        comparisonsLabel = createMetricLabel("Comparisons: 0");
        swapsLabel = createMetricLabel("Swaps: 0");
        accessesLabel = createMetricLabel("Array Accesses: 0");
        timeLabel = createMetricLabel("Time: 0 ms");

        timeBadgeLabel = createBadgeLabel("Time: -", new Color(137, 180, 250));
        spaceBadgeLabel = createBadgeLabel("Space: -", new Color(203, 166, 247));

        metricsPanel.add(comparisonsLabel);
        metricsPanel.add(swapsLabel);
        metricsPanel.add(accessesLabel);
        metricsPanel.add(timeLabel);
        metricsPanel.add(timeBadgeLabel);
        metricsPanel.add(spaceBadgeLabel);
        northContainer.add(metricsPanel);

        // Single Row Top Control Panel
        JPanel topControlPanel = new JPanel();
        topControlPanel.setBackground(new Color(24, 24, 37));
        topControlPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        topControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 3));

        JLabel titleLabel = new JLabel("Visualizer: ");
        titleLabel.setForeground(new Color(205, 214, 244));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        topControlPanel.add(titleLabel);

        JButton bubbleBtn = createStyledButton("Bubble", new Color(137, 180, 250));
        bubbleBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n²)", "O(1)");
            runSortingTask((arr, panel) -> SortingAlgorithms.bubbleSort(arr, panel, speedSlider, metrics));
        });

        JButton insertionBtn = createStyledButton("Insertion", new Color(166, 227, 161));
        insertionBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n²)", "O(1)");
            runSortingTask((arr, panel) -> SortingAlgorithms.insertionSort(arr, panel, speedSlider, metrics));
        });

        JButton selectionBtn = createStyledButton("Selection", new Color(250, 179, 135));
        selectionBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n²)", "O(1)");
            runSortingTask((arr, panel) -> SortingAlgorithms.selectionSort(arr, panel, speedSlider, metrics));
        });

        JButton mergeBtn = createStyledButton("Merge", new Color(203, 166, 247));
        mergeBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n log n)", "O(n)");
            runSortingTask((arr, panel) -> SortingAlgorithms.mergeSort(arr, panel, speedSlider, metrics));
        });

        JButton quickBtn = createStyledButton("Quick", new Color(243, 139, 168));
        quickBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n log n)", "O(log n)");
            runSortingTask((arr, panel) -> SortingAlgorithms.quickSort(arr, panel, speedSlider, metrics));
        });

        JButton heapBtn = createStyledButton("Heap", new Color(249, 226, 175));
        heapBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n log n)", "O(1)");
            runSortingTask((arr, panel) -> SortingAlgorithms.heapSort(arr, panel, speedSlider, metrics));
        });

        JButton shellBtn = createStyledButton("Shell", new Color(148, 226, 213));
        shellBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n log n)", "O(1)");
            runSortingTask((arr, panel) -> SortingAlgorithms.shellSort(arr, panel, speedSlider, metrics));
        });

        JButton countingBtn = createStyledButton("Counting", new Color(180, 190, 254));
        countingBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(n + k)", "O(k)");
            runSortingTask((arr, panel) -> SortingAlgorithms.countingSort(arr, panel, speedSlider, metrics));
        });

        JButton radixBtn = createStyledButton("Radix", new Color(235, 160, 172));
        radixBtn.addActionListener(e -> {
            setAlgorithmComplexity("O(nk)", "O(n + k)");
            runSortingTask((arr, panel) -> SortingAlgorithms.radixSort(arr, panel, speedSlider, metrics));
        });

        topControlPanel.add(bubbleBtn);
        topControlPanel.add(insertionBtn);
        topControlPanel.add(selectionBtn);
        topControlPanel.add(mergeBtn);
        topControlPanel.add(quickBtn);
        topControlPanel.add(heapBtn);
        topControlPanel.add(shellBtn);
        topControlPanel.add(countingBtn);
        topControlPanel.add(radixBtn);

        // Separator between algorithm buttons and state controls
        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 20));
        sep.setForeground(new Color(69, 71, 90));
        topControlPanel.add(sep);

        // Execution State Controls
        pauseBtn = createStyledButton("Pause", new Color(249, 226, 175));
        pauseBtn.addActionListener(e -> {
            SortingAlgorithms.pauseSorting();
            metrics.pauseTimer();
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
        });
        pauseBtn.setEnabled(false);

        resumeBtn = createStyledButton("Resume", new Color(148, 226, 213));
        resumeBtn.addActionListener(e -> {
            SortingAlgorithms.resumeSorting();
            metrics.resumeTimer();
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
        controlsToDisable.add(mergeBtn);
        controlsToDisable.add(quickBtn);
        controlsToDisable.add(heapBtn);
        controlsToDisable.add(shellBtn);
        controlsToDisable.add(countingBtn);
        controlsToDisable.add(radixBtn);

        northContainer.add(topControlPanel);
        add(northContainer, BorderLayout.NORTH);

        // Bottom Control Panel (Array Distribution, Speed, Themes, and Sound)
        JPanel bottomControlPanel = new JPanel();
        bottomControlPanel.setBackground(new Color(24, 24, 37));
        bottomControlPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        bottomControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 4));

        JLabel arrayTypeLabel = new JLabel("Distribution:");
        arrayTypeLabel.setForeground(new Color(205, 214, 244));
        arrayTypeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bottomControlPanel.add(arrayTypeLabel);

        String[] distributions = {"Random", "Nearly Sorted", "Reversed", "Few Unique"};
        arrayTypeSelector = new JComboBox<>(distributions);
        arrayTypeSelector.setBackground(new Color(49, 50, 68));
        arrayTypeSelector.setForeground(new Color(205, 214, 244));
        arrayTypeSelector.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bottomControlPanel.add(arrayTypeSelector);

        JButton generateBtn = createStyledButton("Generate Array", new Color(245, 194, 231));
        generateBtn.addActionListener(e -> generateNewArray());
        bottomControlPanel.add(generateBtn);

        JLabel speedLabel = new JLabel("Speed:");
        speedLabel.setForeground(new Color(205, 214, 244));
        speedLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bottomControlPanel.add(speedLabel);

        speedSlider = new JSlider(1, 150, 30);
        speedSlider.setBackground(new Color(24, 24, 37));
        speedSlider.setForeground(new Color(205, 214, 244));
        speedSlider.setFocusable(false);
        speedSlider.setPreferredSize(new Dimension(120, 20));
        bottomControlPanel.add(speedSlider);

        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setForeground(new Color(205, 214, 244));
        themeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bottomControlPanel.add(themeLabel);

        themeSelector = new JComboBox<>(SortingThemes.getAvailableThemes());
        themeSelector.setBackground(new Color(49, 50, 68));
        themeSelector.setForeground(new Color(205, 214, 244));
        themeSelector.setFont(new Font("SansSerif", Font.PLAIN, 11));
        themeSelector.addActionListener(e -> {
            SortingThemes.Theme selectedTheme = (SortingThemes.Theme) themeSelector.getSelectedItem();
            if (selectedTheme != null) {
                visualizerPanel.applyTheme(selectedTheme);
            }
        });
        bottomControlPanel.add(themeSelector);

        soundToggle = new JCheckBox("Sound", true);
        soundToggle.setBackground(new Color(24, 24, 37));
        soundToggle.setForeground(new Color(205, 214, 244));
        soundToggle.setFocusable(false);
        soundToggle.addItemListener(e -> SortingAudio.setSoundEnabled(soundToggle.isSelected()));
        bottomControlPanel.add(soundToggle);

        controlsToDisable.add(arrayTypeSelector);
        controlsToDisable.add(generateBtn);

        add(bottomControlPanel, BorderLayout.SOUTH);

        metricsUpdateTimer = new Timer(50, e -> updateMetricsDisplay());
    }

    private JLabel createMetricLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(166, 227, 161));
        label.setFont(new Font("Monospaced", Font.BOLD, 13));
        return label;
    }

    private JLabel createBadgeLabel(String text, Color bg) {
        JLabel badge = new JLabel(text);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(new Color(17, 17, 27));
        badge.setBackground(bg);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        return badge;
    }

    public void setAlgorithmComplexity(String timeComplexity, String spaceComplexity) {
        timeBadgeLabel.setText("Time: " + timeComplexity);
        spaceBadgeLabel.setText("Space: " + spaceComplexity);
    }

    private JButton createStyledButton(String text, Color accentColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setBackground(accentColor);
        button.setForeground(new Color(17, 17, 27));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
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
        accessesLabel.setText("Array Accesses: " + metrics.getArrayAccesses());
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