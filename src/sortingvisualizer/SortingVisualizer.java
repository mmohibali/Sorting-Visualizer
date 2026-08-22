package sortingvisualizer;

/**
 * Sorting Algorithms Visualizer
 * 
 * Author: Developer
 * Description: An interactive Java-based educational tool to visualize sorting algorithms in real-time.
 */

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

    private int[] arrayLeft;
    private int[] arrayRight;

    private VisualizerPanel panelLeft;
    private VisualizerPanel panelRight;
    private PseudocodeSidebar pseudocodeSidebar;
    private JPanel centerContainer;
    private JSplitPane splitPane;

    private JPanel algoCardPanel;
    private CardLayout algoCardLayout;
    private JComboBox<String> leftAlgoBox;
    private JComboBox<String> rightAlgoBox;
    private JButton raceStartBtn;

    private JComboBox<String> arrayTypeSelector;
    private JComboBox<SortingThemes.Theme> themeSelector;
    private JSlider speedSlider;
    private JCheckBox soundToggle;
    private JCheckBox dualModeToggle;

    private JButton pauseBtn, resumeBtn, stopBtn;
    
    // Left Metrics
    private JLabel comparisonsLabelLeft, swapsLabelLeft, accessesLabelLeft, timeLabelLeft;
    private JLabel timeBadgeLabelLeft, spaceBadgeLabelLeft;
    
    // Right Metrics
    private JLabel comparisonsLabelRight, swapsLabelRight, accessesLabelRight, timeLabelRight;
    private JLabel timeBadgeLabelRight, spaceBadgeLabelRight;
    private JSeparator metricsSeparator;

    private final SortingMetrics metricsLeft = new SortingMetrics();
    private final SortingMetrics metricsRight = new SortingMetrics();
    private Timer metricsUpdateTimer;
    
    private SwingWorker<Void, Void> activeTaskLeft;
    private SwingWorker<Void, Void> activeTaskRight;
    
    private final List<JComponent> controlsToDisable = new ArrayList<>();

    public SortingVisualizer() {
        setTitle("Sorting Algorithms Visualizer");
        setSize(1280, 720);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(new Color(30, 30, 46));
        setLayout(new BorderLayout());

        // Initialize Arrays & Panels
        arrayLeft = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
        arrayRight = arrayLeft.clone();

        panelLeft = new VisualizerPanel(arrayLeft);
        panelRight = new VisualizerPanel(arrayRight);
        
        panelLeft.setPreferredSize(new Dimension(600, 500));
        panelRight.setPreferredSize(new Dimension(600, 500));
        panelLeft.setMinimumSize(new Dimension(50, 50));
        panelRight.setMinimumSize(new Dimension(50, 50));

        centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(new Color(30, 30, 46));
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeft, panelRight);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(4);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        
        splitPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                splitPane.setDividerLocation(0.5);
            }
        });

        centerContainer.add(panelLeft, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // Sidebar
        pseudocodeSidebar = new PseudocodeSidebar();
        pseudocodeSidebar.setPreferredSize(new Dimension(280, 0));
        add(pseudocodeSidebar, BorderLayout.EAST);

        // --- North Container ---
        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));

        // Real-Time Analytics Dashboard Panel
        JPanel metricsPanel = new JPanel();
        metricsPanel.setBackground(new Color(17, 17, 27));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        metricsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));

        comparisonsLabelLeft = createMetricLabel("Comparisons: 0");
        swapsLabelLeft = createMetricLabel("Swaps: 0");
        accessesLabelLeft = createMetricLabel("Array Accesses: 0");
        timeLabelLeft = createMetricLabel("Time: 0 ms");
        timeBadgeLabelLeft = createBadgeLabel("Time: -", new Color(137, 180, 250));
        spaceBadgeLabelLeft = createBadgeLabel("Space: -", new Color(203, 166, 247));

        comparisonsLabelRight = createMetricLabel("R-Comp: 0");
        swapsLabelRight = createMetricLabel("R-Swaps: 0");
        accessesLabelRight = createMetricLabel("R-Acc: 0");
        timeLabelRight = createMetricLabel("R-Time: 0 ms");
        timeBadgeLabelRight = createBadgeLabel("Time: -", new Color(137, 180, 250));
        spaceBadgeLabelRight = createBadgeLabel("Space: -", new Color(203, 166, 247));

        metricsSeparator = new JSeparator(JSeparator.VERTICAL);
        metricsSeparator.setPreferredSize(new Dimension(2, 20));
        metricsSeparator.setForeground(new Color(69, 71, 90));

        metricsPanel.add(comparisonsLabelLeft);
        metricsPanel.add(swapsLabelLeft);
        metricsPanel.add(accessesLabelLeft);
        metricsPanel.add(timeLabelLeft);
        metricsPanel.add(timeBadgeLabelLeft);
        metricsPanel.add(spaceBadgeLabelLeft);
        
        metricsPanel.add(metricsSeparator);
        
        metricsPanel.add(comparisonsLabelRight);
        metricsPanel.add(swapsLabelRight);
        metricsPanel.add(accessesLabelRight);
        metricsPanel.add(timeLabelRight);
        metricsPanel.add(timeBadgeLabelRight);
        metricsPanel.add(spaceBadgeLabelRight);

        setRightMetricsVisible(false);
        northContainer.add(metricsPanel);

        // Top Control Panel
        JPanel topControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 3));
        topControlPanel.setBackground(new Color(24, 24, 37));
        topControlPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        algoCardLayout = new CardLayout();
        algoCardPanel = new JPanel(algoCardLayout);
        algoCardPanel.setBackground(new Color(24, 24, 37));

        JPanel singleAlgoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        singleAlgoPanel.setBackground(new Color(24, 24, 37));

        JLabel titleLabel = new JLabel("Visualizer: ");
        titleLabel.setForeground(new Color(205, 214, 244));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        singleAlgoPanel.add(titleLabel);

        singleAlgoPanel.add(createAlgoBtn("Bubble", new Color(137, 180, 250)));
        singleAlgoPanel.add(createAlgoBtn("Insertion", new Color(166, 227, 161)));
        singleAlgoPanel.add(createAlgoBtn("Selection", new Color(250, 179, 135)));
        singleAlgoPanel.add(createAlgoBtn("Merge", new Color(203, 166, 247)));
        singleAlgoPanel.add(createAlgoBtn("Quick", new Color(243, 139, 168)));
        singleAlgoPanel.add(createAlgoBtn("Heap", new Color(249, 226, 175)));
        singleAlgoPanel.add(createAlgoBtn("Shell", new Color(148, 226, 213)));
        singleAlgoPanel.add(createAlgoBtn("Counting", new Color(180, 190, 254)));
        singleAlgoPanel.add(createAlgoBtn("Radix", new Color(235, 160, 172)));

        JPanel dualAlgoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dualAlgoPanel.setBackground(new Color(24, 24, 37));

        JLabel dualTitle = new JLabel("Dual Race: ");
        dualTitle.setForeground(new Color(205, 214, 244));
        dualTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        dualAlgoPanel.add(dualTitle);

        String[] algos = {"Bubble", "Insertion", "Selection", "Merge", "Quick", "Heap", "Shell", "Counting", "Radix"};
        leftAlgoBox = new JComboBox<>(algos);
        rightAlgoBox = new JComboBox<>(algos);
        rightAlgoBox.setSelectedItem("Insertion");
        
        raceStartBtn = createStyledButton("Start Race", new Color(166, 227, 161));
        raceStartBtn.addActionListener(e -> runDualSortingTask());

        dualAlgoPanel.add(new JLabel("Left: ")).setForeground(Color.WHITE);
        dualAlgoPanel.add(leftAlgoBox);
        dualAlgoPanel.add(new JLabel(" Right: ")).setForeground(Color.WHITE);
        dualAlgoPanel.add(rightAlgoBox);
        dualAlgoPanel.add(raceStartBtn);

        algoCardPanel.add(singleAlgoPanel, "SINGLE");
        algoCardPanel.add(dualAlgoPanel, "DUAL");

        topControlPanel.add(algoCardPanel);

        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 20));
        sep.setForeground(new Color(69, 71, 90));
        topControlPanel.add(sep);

        pauseBtn = createStyledButton("Pause", new Color(249, 226, 175));
        pauseBtn.addActionListener(e -> {
            SortingAlgorithms.pauseSorting();
            metricsLeft.pauseTimer();
            metricsRight.pauseTimer();
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
        });
        pauseBtn.setEnabled(false);

        resumeBtn = createStyledButton("Resume", new Color(148, 226, 213));
        resumeBtn.addActionListener(e -> {
            SortingAlgorithms.resumeSorting();
            metricsLeft.resumeTimer();
            metricsRight.resumeTimer();
            resumeBtn.setEnabled(false);
            pauseBtn.setEnabled(true);
        });
        resumeBtn.setEnabled(false);

        stopBtn = createStyledButton("Stop", new Color(243, 139, 168));
        stopBtn.addActionListener(e -> stopActiveTasks());
        stopBtn.setEnabled(false);

        topControlPanel.add(pauseBtn);
        topControlPanel.add(resumeBtn);
        topControlPanel.add(stopBtn);
        
        controlsToDisable.add(leftAlgoBox);
        controlsToDisable.add(rightAlgoBox);
        controlsToDisable.add(raceStartBtn);

        northContainer.add(topControlPanel);
        add(northContainer, BorderLayout.NORTH);

        // --- Bottom Control Panel ---
        JPanel bottomControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        bottomControlPanel.setBackground(new Color(24, 24, 37));
        bottomControlPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        dualModeToggle = new JCheckBox("Dual Mode", false);
        dualModeToggle.setBackground(new Color(24, 24, 37));
        dualModeToggle.setForeground(new Color(245, 194, 231));
        dualModeToggle.setFont(new Font("SansSerif", Font.BOLD, 12));
        dualModeToggle.setFocusable(false);
        dualModeToggle.addItemListener(e -> toggleDualMode(dualModeToggle.isSelected()));
        bottomControlPanel.add(dualModeToggle);
        controlsToDisable.add(dualModeToggle);

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
        controlsToDisable.add(arrayTypeSelector);

        JButton generateBtn = createStyledButton("Generate Array", new Color(245, 194, 231));
        generateBtn.addActionListener(e -> generateNewArray());
        bottomControlPanel.add(generateBtn);
        controlsToDisable.add(generateBtn);

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
                panelLeft.applyTheme(selectedTheme);
                panelRight.applyTheme(selectedTheme);
            }
        });
        bottomControlPanel.add(themeSelector);

        soundToggle = new JCheckBox("Sound", true);
        soundToggle.setBackground(new Color(24, 24, 37));
        soundToggle.setForeground(new Color(205, 214, 244));
        soundToggle.setFocusable(false);
        soundToggle.addItemListener(e -> SortingAudio.setSoundEnabled(soundToggle.isSelected()));
        bottomControlPanel.add(soundToggle);

        add(bottomControlPanel, BorderLayout.SOUTH);

        metricsUpdateTimer = new Timer(50, e -> updateMetricsDisplay());
    }
    
    private JButton createAlgoBtn(String name, Color color) {
        JButton btn = createStyledButton(name, color);
        btn.addActionListener(e -> runSingleSortingTask(name));
        controlsToDisable.add(btn);
        return btn;
    }

    private void toggleDualMode(boolean isDual) {
        centerContainer.removeAll();
        if (isDual) {
            arrayRight = arrayLeft.clone();
            panelRight.setArrayToVisualize(arrayRight);
            metricsRight.reset();

            splitPane.setLeftComponent(panelLeft);
            splitPane.setRightComponent(panelRight);

            centerContainer.add(splitPane, BorderLayout.CENTER);
            algoCardLayout.show(algoCardPanel, "DUAL");
            setRightMetricsVisible(true);
            pseudocodeSidebar.setVisible(false);
            
            centerContainer.revalidate();
            centerContainer.repaint();
            
            SwingUtilities.invokeLater(() -> {
                int width = centerContainer.getWidth();
                if (width > 0) splitPane.setDividerLocation(width / 2);
                else splitPane.setDividerLocation(0.5);
            });
        } else {
            centerContainer.add(panelLeft, BorderLayout.CENTER);
            algoCardLayout.show(algoCardPanel, "SINGLE");
            setRightMetricsVisible(false);
            pseudocodeSidebar.setVisible(true);
            centerContainer.revalidate();
            centerContainer.repaint();
        }
        updateMetricsDisplay();
    }

    private void setRightMetricsVisible(boolean visible) {
        metricsSeparator.setVisible(visible);
        comparisonsLabelRight.setVisible(visible);
        swapsLabelRight.setVisible(visible);
        accessesLabelRight.setVisible(visible);
        timeLabelRight.setVisible(visible);
        timeBadgeLabelRight.setVisible(visible);
        spaceBadgeLabelRight.setVisible(visible);
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

    private void updateComplexities(String algo, boolean isLeft) {
        String t = "", s = "";
        switch (algo) {
            case "Bubble": case "Insertion": case "Selection": t = "O(n²)"; s = "O(1)"; break;
            case "Merge": t = "O(n log n)"; s = "O(n)"; break;
            case "Quick": t = "O(n log n)"; s = "O(log n)"; break;
            case "Heap": case "Shell": t = "O(n log n)"; s = "O(1)"; break;
            case "Counting": t = "O(n + k)"; s = "O(k)"; break;
            case "Radix": t = "O(nk)"; s = "O(n + k)"; break;
        }
        if (isLeft) {
            timeBadgeLabelLeft.setText("Time: " + t);
            spaceBadgeLabelLeft.setText("Space: " + s);
        } else {
            timeBadgeLabelRight.setText("Time: " + t);
            spaceBadgeLabelRight.setText("Space: " + s);
        }
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
    
    private void runSingleSortingTask(String algoName) {
        pseudocodeSidebar.loadAlgorithm(algoName);
        updateComplexities(algoName, true);
        prepareForSort();

        metricsLeft.reset();
        metricsLeft.startTimer();
        metricsUpdateTimer.start();

        activeTaskLeft = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                switch (algoName) {
                    case "Bubble": SortingAlgorithms.bubbleSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Insertion": SortingAlgorithms.insertionSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Selection": SortingAlgorithms.selectionSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Merge": SortingAlgorithms.mergeSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Quick": SortingAlgorithms.quickSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Heap": SortingAlgorithms.heapSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Shell": SortingAlgorithms.shellSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Counting": SortingAlgorithms.countingSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                    case "Radix": SortingAlgorithms.radixSort(arrayLeft, panelLeft, speedSlider, metricsLeft, pseudocodeSidebar); break;
                }
                return null;
            }
            @Override
            protected void done() {
                metricsLeft.stopTimer();
                checkIfFinished();
            }
        };
        activeTaskLeft.execute();
    }
    
    private void runDualSortingTask() {
        String leftAlgo = (String) leftAlgoBox.getSelectedItem();
        String rightAlgo = (String) rightAlgoBox.getSelectedItem();
        
        updateComplexities(leftAlgo, true);
        updateComplexities(rightAlgo, false);
        prepareForSort();

        metricsLeft.reset();
        metricsRight.reset();
        metricsLeft.startTimer();
        metricsRight.startTimer();
        metricsUpdateTimer.start();

        activeTaskLeft = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                switch (leftAlgo) {
                    case "Bubble": SortingAlgorithms.bubbleSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Insertion": SortingAlgorithms.insertionSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Selection": SortingAlgorithms.selectionSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Merge": SortingAlgorithms.mergeSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Quick": SortingAlgorithms.quickSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Heap": SortingAlgorithms.heapSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Shell": SortingAlgorithms.shellSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Counting": SortingAlgorithms.countingSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                    case "Radix": SortingAlgorithms.radixSort(arrayLeft, panelLeft, speedSlider, metricsLeft, null); break;
                }
                return null;
            }
            @Override
            protected void done() {
                metricsLeft.stopTimer();
                checkIfFinished();
            }
        };

        activeTaskRight = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                switch (rightAlgo) {
                    case "Bubble": SortingAlgorithms.bubbleSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Insertion": SortingAlgorithms.insertionSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Selection": SortingAlgorithms.selectionSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Merge": SortingAlgorithms.mergeSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Quick": SortingAlgorithms.quickSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Heap": SortingAlgorithms.heapSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Shell": SortingAlgorithms.shellSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Counting": SortingAlgorithms.countingSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                    case "Radix": SortingAlgorithms.radixSort(arrayRight, panelRight, speedSlider, metricsRight, null); break;
                }
                return null;
            }
            @Override
            protected void done() {
                metricsRight.stopTimer();
                checkIfFinished();
            }
        };
        
        activeTaskLeft.execute();
        activeTaskRight.execute();
    }
    
    private void prepareForSort() {
        setControlsEnabled(false);
        pauseBtn.setEnabled(true);
        resumeBtn.setEnabled(false);
        stopBtn.setEnabled(true);
    }

    private void checkIfFinished() {
        boolean leftDone = (activeTaskLeft == null || activeTaskLeft.isDone());
        boolean rightDone = (!dualModeToggle.isSelected() || activeTaskRight == null || activeTaskRight.isDone());

        if (leftDone && rightDone) {
            metricsUpdateTimer.stop();
            panelLeft.resetHighlights();
            if (dualModeToggle.isSelected()) panelRight.resetHighlights();
            
            setControlsEnabled(true);
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            updateMetricsDisplay();
        }
    }

    private void updateMetricsDisplay() {
        if (dualModeToggle.isSelected()) {
            comparisonsLabelLeft.setText("L-Comp: " + metricsLeft.getComparisons());
            swapsLabelLeft.setText("L-Swaps: " + metricsLeft.getSwaps());
            accessesLabelLeft.setText("L-Acc: " + metricsLeft.getArrayAccesses());
            timeLabelLeft.setText("L-Time: " + metricsLeft.getElapsedTime() + " ms");
            
            comparisonsLabelRight.setText("R-Comp: " + metricsRight.getComparisons());
            swapsLabelRight.setText("R-Swaps: " + metricsRight.getSwaps());
            accessesLabelRight.setText("R-Acc: " + metricsRight.getArrayAccesses());
            timeLabelRight.setText("R-Time: " + metricsRight.getElapsedTime() + " ms");
        } else {
            comparisonsLabelLeft.setText("Comparisons: " + metricsLeft.getComparisons());
            swapsLabelLeft.setText("Swaps: " + metricsLeft.getSwaps());
            accessesLabelLeft.setText("Array Accesses: " + metricsLeft.getArrayAccesses());
            timeLabelLeft.setText("Time: " + metricsLeft.getElapsedTime() + " ms");
        }
    }

    private void stopActiveTasks() {
        SortingAlgorithms.stopSorting();
        metricsLeft.stopTimer();
        metricsRight.stopTimer();
        metricsUpdateTimer.stop();
        
        panelLeft.resetHighlights();
        if (dualModeToggle.isSelected()) panelRight.resetHighlights();
        
        setControlsEnabled(true);
        pauseBtn.setEnabled(false);
        resumeBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        updateMetricsDisplay();
    }

    private void generateNewArray() {
        String selected = (String) arrayTypeSelector.getSelectedItem();
        if (selected == null) selected = "Random";

        switch (selected) {
            case "Nearly Sorted": arrayLeft = generateNearlySortedArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE); break;
            case "Reversed": arrayLeft = generateReversedArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE); break;
            case "Few Unique": arrayLeft = generateFewUniqueArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE); break;
            case "Random": default: arrayLeft = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE); break;
        }

        panelLeft.setArrayToVisualize(arrayLeft);
        metricsLeft.reset();

        if (dualModeToggle.isSelected()) {
            arrayRight = arrayLeft.clone();
            panelRight.setArrayToVisualize(arrayRight);
            metricsRight.reset();
        }
        
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer visualizer = new SortingVisualizer();
            visualizer.setVisible(true);
        });
    }
}