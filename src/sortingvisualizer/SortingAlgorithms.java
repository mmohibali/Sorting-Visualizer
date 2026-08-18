package sortingvisualizer;

import javax.swing.JSlider;

public class SortingAlgorithms {

    private static volatile boolean isPaused = false;
    private static volatile boolean isStopped = false;

    public static void pauseSorting() {
        isPaused = true;
    }

    public static void resumeSorting() {
        isPaused = false;
        synchronized (SortingAlgorithms.class) {
            SortingAlgorithms.class.notifyAll();
        }
    }

    public static void stopSorting() {
        isStopped = true;
        resumeSorting();
    }

    public static void resetFlags() {
        isPaused = false;
        isStopped = false;
    }

    private static void checkPauseAndStop() throws InterruptedException {
        if (isStopped) {
            throw new InterruptedException("Sorting stopped");
        }
        while (isPaused) {
            synchronized (SortingAlgorithms.class) {
                SortingAlgorithms.class.wait();
            }
            if (isStopped) {
                throw new InterruptedException("Sorting stopped");
            }
        }
    }

    public static void bubbleSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) {
        resetFlags();
        int n = array.length;
        boolean swapped;

        try {
            do {
                swapped = false;
                for (int i = 1; i < n; i++) {
                    checkPauseAndStop();
                    metrics.incrementComparisons();

                    if (array[i - 1] > array[i]) {
                        swap(array, i - 1, i);
                        metrics.incrementSwaps();
                        swapped = true;

                        visualizerPanel.setHighlightedBars(i - 1, i);
                        Thread.sleep(speedSlider.getValue());
                    }
                }
            } while (swapped);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        visualizerPanel.resetHighlights();
    }

    public static void insertionSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) {
        resetFlags();
        int n = array.length;

        try {
            for (int i = 1; i < n; i++) {
                checkPauseAndStop();
                int key = array[i];
                int j = i - 1;

                while (j >= 0) {
                    checkPauseAndStop();
                    metrics.incrementComparisons();

                    if (array[j] > key) {
                        array[j + 1] = array[j];
                        metrics.incrementSwaps();
                        j = j - 1;

                        visualizerPanel.setHighlightedBars(j + 1, j);
                        Thread.sleep(speedSlider.getValue());
                    } else {
                        break;
                    }
                }
                array[j + 1] = key;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        visualizerPanel.resetHighlights();
    }

    public static void selectionSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) {
        resetFlags();
        int n = array.length;

        try {
            for (int i = 0; i < n - 1; i++) {
                checkPauseAndStop();
                int minIndex = i;
                for (int j = i + 1; j < n; j++) {
                    checkPauseAndStop();
                    metrics.incrementComparisons();

                    visualizerPanel.setHighlightedBars(minIndex, j);
                    Thread.sleep(speedSlider.getValue());

                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                }

                if (minIndex != i) {
                    swap(array, minIndex, i);
                    metrics.incrementSwaps();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        visualizerPanel.resetHighlights();
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}