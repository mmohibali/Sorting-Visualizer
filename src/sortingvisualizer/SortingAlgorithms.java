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

    private static void playElementSound(int value) {
        // Map bar value (5 to 500) to an audible frequency range (150Hz to 850Hz)
        int freq = 150 + (value * 700) / 500;
        SortingAudio.playTone(freq, 15);
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

                        playElementSound(array[i]);
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

                        playElementSound(array[j + 1]);
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

                    playElementSound(array[j]);
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

    public static void mergeSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) {
        resetFlags();
        try {
            mergeSortHelper(array, 0, array.length - 1, visualizerPanel, speedSlider, metrics);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    private static void mergeSortHelper(int[] array, int left, int right, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) throws InterruptedException {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(array, left, mid, visualizerPanel, speedSlider, metrics);
            mergeSortHelper(array, mid + 1, right, visualizerPanel, speedSlider, metrics);
            merge(array, left, mid, right, visualizerPanel, speedSlider, metrics);
        }
    }

    private static void merge(int[] array, int left, int mid, int right, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) throws InterruptedException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(array, left, leftArr, 0, n1);
        System.arraycopy(array, mid + 1, rightArr, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            checkPauseAndStop();
            metrics.incrementComparisons();

            playElementSound(leftArr[i]);
            visualizerPanel.setHighlightedBars(left + i, mid + 1 + j);
            Thread.sleep(speedSlider.getValue());

            if (leftArr[i] <= rightArr[j]) {
                array[k] = leftArr[i];
                i++;
            } else {
                array[k] = rightArr[j];
                j++;
            }
            metrics.incrementSwaps();
            k++;
        }

        while (i < n1) {
            checkPauseAndStop();
            array[k] = leftArr[i];
            playElementSound(leftArr[i]);
            visualizerPanel.setHighlightedBars(k, left + i);
            Thread.sleep(speedSlider.getValue());
            i++;
            k++;
            metrics.incrementSwaps();
        }

        while (j < n2) {
            checkPauseAndStop();
            array[k] = rightArr[j];
            playElementSound(rightArr[j]);
            visualizerPanel.setHighlightedBars(k, mid + 1 + j);
            Thread.sleep(speedSlider.getValue());
            j++;
            k++;
            metrics.incrementSwaps();
        }
    }

    public static void quickSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) {
        resetFlags();
        try {
            quickSortHelper(array, 0, array.length - 1, visualizerPanel, speedSlider, metrics);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    private static void quickSortHelper(int[] array, int low, int high, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) throws InterruptedException {
        if (low < high) {
            int pi = partition(array, low, high, visualizerPanel, speedSlider, metrics);
            quickSortHelper(array, low, pi - 1, visualizerPanel, speedSlider, metrics);
            quickSortHelper(array, pi + 1, high, visualizerPanel, speedSlider, metrics);
        }
    }

    private static int partition(int[] array, int low, int high, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) throws InterruptedException {
        int pivot = array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            checkPauseAndStop();
            metrics.incrementComparisons();

            playElementSound(array[j]);
            visualizerPanel.setHighlightedBars(j, high);
            Thread.sleep(speedSlider.getValue());

            if (array[j] < pivot) {
                i++;
                swap(array, i, j);
                metrics.incrementSwaps();
            }
        }

        swap(array, i + 1, high);
        metrics.incrementSwaps();
        return (i + 1);
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}