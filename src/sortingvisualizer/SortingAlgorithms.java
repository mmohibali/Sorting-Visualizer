package sortingvisualizer;

import javax.swing.JSlider;

public class SortingAlgorithms {

    private static volatile boolean isPaused = false;
    private static volatile boolean isStopped = false;

    public static void pauseSorting() { isPaused = true; }

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
        if (isStopped) throw new InterruptedException("Sorting stopped");
        while (isPaused) {
            synchronized (SortingAlgorithms.class) {
                SortingAlgorithms.class.wait();
            }
            if (isStopped) throw new InterruptedException("Sorting stopped");
        }
    }

    private static void playElementSound(int value) {
        int freq = 150 + (value * 700) / 500;
        SortingAudio.playTone(freq, 15);
    }

    public static void bubbleSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        boolean swapped;
        try {
            if (sidebar != null) sidebar.highlightLine(1);
            do {
                if (sidebar != null) sidebar.highlightLine(3);
                swapped = false;
                for (int i = 1; i < n; i++) {
                    if (sidebar != null) sidebar.highlightLine(4);
                    checkPauseAndStop();
                    metrics.incrementComparisons();
                    if (sidebar != null) sidebar.highlightLine(5);
                    if (array[i - 1] > array[i]) {
                        if (sidebar != null) sidebar.highlightLine(6);
                        swap(array, i - 1, i);
                        metrics.incrementSwaps();
                        swapped = true;
                        if (sidebar != null) sidebar.highlightLine(7);
                        playElementSound(array[i]);
                        visualizerPanel.setHighlightedBars(i - 1, i);
                        Thread.sleep(speedSlider.getValue());
                    }
                }
                n = n - 1;
            } while (swapped);
            if (sidebar != null) sidebar.highlightLine(12);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    public static void insertionSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        try {
            for (int i = 1; i < n; i++) {
                if (sidebar != null) sidebar.highlightLine(1);
                checkPauseAndStop();
                int key = array[i];
                if (sidebar != null) sidebar.highlightLine(2);
                int j = i - 1;
                if (sidebar != null) sidebar.highlightLine(3);
                while (j >= 0) {
                    if (sidebar != null) sidebar.highlightLine(4);
                    checkPauseAndStop();
                    metrics.incrementComparisons();
                    if (array[j] > key) {
                        if (sidebar != null) sidebar.highlightLine(5);
                        array[j + 1] = array[j];
                        metrics.incrementSwaps();
                        j = j - 1;
                        if (sidebar != null) sidebar.highlightLine(6);
                        playElementSound(array[j + 1]);
                        visualizerPanel.setHighlightedBars(j + 1, j);
                        Thread.sleep(speedSlider.getValue());
                    } else {
                        break;
                    }
                }
                array[j + 1] = key;
                if (sidebar != null) sidebar.highlightLine(8);
            }
            if (sidebar != null) sidebar.highlightLine(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    public static void selectionSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        try {
            for (int i = 0; i < n - 1; i++) {
                if (sidebar != null) sidebar.highlightLine(2);
                checkPauseAndStop();
                int minIndex = i;
                if (sidebar != null) sidebar.highlightLine(3);
                for (int j = i + 1; j < n; j++) {
                    if (sidebar != null) sidebar.highlightLine(4);
                    checkPauseAndStop();
                    metrics.incrementComparisons();
                    playElementSound(array[j]);
                    visualizerPanel.setHighlightedBars(minIndex, j);
                    Thread.sleep(speedSlider.getValue());
                    if (sidebar != null) sidebar.highlightLine(5);
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                        if (sidebar != null) sidebar.highlightLine(6);
                    }
                }
                if (minIndex != i) {
                    if (sidebar != null) sidebar.highlightLine(9);
                    swap(array, minIndex, i);
                    metrics.incrementSwaps();
                }
            }
            if (sidebar != null) sidebar.highlightLine(11);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    public static void mergeSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        try {
            mergeSortHelper(array, 0, array.length - 1, visualizerPanel, speedSlider, metrics, sidebar);
            if (sidebar != null) sidebar.highlightLine(7);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    private static void mergeSortHelper(int[] array, int left, int right, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) throws InterruptedException {
        if (sidebar != null) sidebar.highlightLine(1);
        if (left < right) {
            int mid = left + (right - left) / 2;
            if (sidebar != null) sidebar.highlightLine(2);
            mergeSortHelper(array, left, mid, visualizerPanel, speedSlider, metrics, sidebar);
            if (sidebar != null) sidebar.highlightLine(3);
            mergeSortHelper(array, mid + 1, right, visualizerPanel, speedSlider, metrics, sidebar);
            if (sidebar != null) sidebar.highlightLine(4);
            merge(array, left, mid, right, visualizerPanel, speedSlider, metrics);
            if (sidebar != null) sidebar.highlightLine(5);
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
            i++; k++;
            metrics.incrementSwaps();
        }

        while (j < n2) {
            checkPauseAndStop();
            array[k] = rightArr[j];
            playElementSound(rightArr[j]);
            visualizerPanel.setHighlightedBars(k, mid + 1 + j);
            Thread.sleep(speedSlider.getValue());
            j++; k++;
            metrics.incrementSwaps();
        }
    }

    public static void quickSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        try {
            quickSortHelper(array, 0, array.length - 1, visualizerPanel, speedSlider, metrics, sidebar);
            if (sidebar != null) sidebar.highlightLine(6);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    private static void quickSortHelper(int[] array, int low, int high, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) throws InterruptedException {
        if (sidebar != null) sidebar.highlightLine(1);
        if (low < high) {
            int pi = partition(array, low, high, visualizerPanel, speedSlider, metrics);
            if (sidebar != null) sidebar.highlightLine(2);
            quickSortHelper(array, low, pi - 1, visualizerPanel, speedSlider, metrics, sidebar);
            if (sidebar != null) sidebar.highlightLine(3);
            quickSortHelper(array, pi + 1, high, visualizerPanel, speedSlider, metrics, sidebar);
            if (sidebar != null) sidebar.highlightLine(4);
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

    public static void heapSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        try {
            if (sidebar != null) sidebar.highlightLine(1);
            for (int i = n / 2 - 1; i >= 0; i--) heapify(array, n, i, visualizerPanel, speedSlider, metrics);
            for (int i = n - 1; i > 0; i--) {
                if (sidebar != null) sidebar.highlightLine(2);
                checkPauseAndStop();
                if (sidebar != null) sidebar.highlightLine(3);
                swap(array, 0, i);
                metrics.incrementSwaps();
                if (sidebar != null) sidebar.highlightLine(4);
                heapify(array, i, 0, visualizerPanel, speedSlider, metrics);
            }
            if (sidebar != null) sidebar.highlightLine(6);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    private static void heapify(int[] array, int n, int i, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) throws InterruptedException {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        if (l < n) {
            metrics.incrementComparisons();
            if (array[l] > array[largest]) largest = l;
        }
        if (r < n) {
            metrics.incrementComparisons();
            if (array[r] > array[largest]) largest = r;
        }
        if (largest != i) {
            checkPauseAndStop();
            swap(array, i, largest);
            metrics.incrementSwaps();
            playElementSound(array[largest]);
            visualizerPanel.setHighlightedBars(i, largest);
            Thread.sleep(speedSlider.getValue());
            heapify(array, n, largest, visualizerPanel, speedSlider, metrics);
        }
    }

    public static void shellSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        try {
            for (int gap = n / 2; gap > 0; gap /= 2) {
                if (sidebar != null) sidebar.highlightLine(1);
                for (int i = gap; i < n; i++) {
                    if (sidebar != null) sidebar.highlightLine(2);
                    checkPauseAndStop();
                    int temp = array[i];
                    if (sidebar != null) sidebar.highlightLine(3);
                    int j = i;
                    while (j >= gap) {
                        if (sidebar != null) sidebar.highlightLine(4);
                        checkPauseAndStop();
                        metrics.incrementComparisons();
                        if (sidebar != null) sidebar.highlightLine(5);
                        if (array[j - gap] > temp) {
                            array[j] = array[j - gap];
                            metrics.incrementSwaps();
                            j -= gap;
                            playElementSound(array[j]);
                            visualizerPanel.setHighlightedBars(j, i);
                            Thread.sleep(speedSlider.getValue());
                        } else {
                            break;
                        }
                    }
                    array[j] = temp;
                }
            }
            if (sidebar != null) sidebar.highlightLine(8);
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

    public static void countingSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        if (n == 0) return;
        try {
            if (sidebar != null) sidebar.highlightLine(1);
            int max = array[0];
            for (int i = 1; i < n; i++) {
                checkPauseAndStop();
                metrics.incrementComparisons();
                if (array[i] > max) max = array[i];
            }
            int[] count = new int[max + 1];
            if (sidebar != null) sidebar.highlightLine(2);
            for (int i = 0; i < n; i++) {
                checkPauseAndStop();
                count[array[i]]++;
                playElementSound(array[i]);
                visualizerPanel.setHighlightedBars(i, -1);
                Thread.sleep(speedSlider.getValue());
            }
            if (sidebar != null) sidebar.highlightLine(3);
            int index = 0;
            for (int i = 0; i <= max; i++) {
                if (sidebar != null) sidebar.highlightLine(4);
                while (count[i] > 0) {
                    checkPauseAndStop();
                    if (sidebar != null) sidebar.highlightLine(5);
                    array[index] = i;
                    metrics.incrementSwaps();
                    playElementSound(array[index]);
                    visualizerPanel.setHighlightedBars(index, -1);
                    Thread.sleep(speedSlider.getValue());
                    index++;
                    count[i]--;
                }
            }
            if (sidebar != null) sidebar.highlightLine(8);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    public static void radixSort(int[] array, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics, PseudocodeSidebar sidebar) {
        resetFlags();
        int n = array.length;
        if (n == 0) return;
        try {
            if (sidebar != null) sidebar.highlightLine(1);
            int max = array[0];
            for (int i = 1; i < n; i++) {
                checkPauseAndStop();
                metrics.incrementComparisons();
                if (array[i] > max) max = array[i];
            }
            if (sidebar != null) sidebar.highlightLine(2);
            for (int exp = 1; max / exp > 0; exp *= 10) {
                if (sidebar != null) sidebar.highlightLine(3);
                countSortForRadix(array, n, exp, visualizerPanel, speedSlider, metrics);
            }
            if (sidebar != null) sidebar.highlightLine(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        visualizerPanel.resetHighlights();
    }

    private static void countSortForRadix(int[] array, int n, int exp, VisualizerPanel visualizerPanel, JSlider speedSlider, SortingMetrics metrics) throws InterruptedException {
        int[] output = new int[n];
        int[] count = new int[10];
        for (int i = 0; i < n; i++) {
            checkPauseAndStop();
            count[(array[i] / exp) % 10]++;
            playElementSound(array[i]);
            visualizerPanel.setHighlightedBars(i, -1);
            Thread.sleep(speedSlider.getValue());
        }
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            checkPauseAndStop();
            output[count[(array[i] / exp) % 10] - 1] = array[i];
            count[(array[i] / exp) % 10]--;
            metrics.incrementSwaps();
        }
        for (int i = 0; i < n; i++) {
            checkPauseAndStop();
            array[i] = output[i];
            playElementSound(array[i]);
            visualizerPanel.setHighlightedBars(i, -1);
            Thread.sleep(speedSlider.getValue());
        }
    }
}