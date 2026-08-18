package sortingvisualizer;

import javax.swing.JSlider;

public class SortingAlgorithms {

    // Thread control state flags
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
        resumeSorting(); // Unblock if paused so it can exit immediately
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

    public static void bubbleSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider) {
        resetFlags();
        int n = array.length;
        boolean swapped;

        try {
            do {
                swapped = false;
                for (int i = 1; i < n; i++) {
                    checkPauseAndStop();
                    if (array[i - 1] > array[i]) {
                        swap(array, i - 1, i);
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

    public static void insertionSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider) {
        resetFlags();
        int n = array.length;

        try {
            for (int i = 1; i < n; i++) {
                checkPauseAndStop();
                int key = array[i];
                int j = i - 1;

                while (j >= 0 && array[j] > key) {
                    checkPauseAndStop();
                    array[j + 1] = array[j];
                    j = j - 1;

                    visualizerPanel.setHighlightedBars(j + 1, j);
                    Thread.sleep(speedSlider.getValue());
                }
                array[j + 1] = key;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        visualizerPanel.resetHighlights();
    }

    public static void selectionSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider) {
        resetFlags();
        int n = array.length;

        try {
            for (int i = 0; i < n - 1; i++) {
                checkPauseAndStop();
                int minIndex = i;
                for (int j = i + 1; j < n; j++) {
                    checkPauseAndStop();
                    visualizerPanel.setHighlightedBars(minIndex, j);
                    Thread.sleep(speedSlider.getValue());

                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                }

                swap(array, minIndex, i);
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