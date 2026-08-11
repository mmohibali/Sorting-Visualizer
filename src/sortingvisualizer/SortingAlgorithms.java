package sortingvisualizer;

public class SortingAlgorithms {

    public static void bubbleSort(int[] array, VisualizerPanel visualizerPanel) {
        int n = array.length;
        boolean swapped;

        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (array[i - 1] > array[i]) {
                    swap(array, i - 1, i);
                    swapped = true;

                    visualizerPanel.setHighlightedBars(i - 1, i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } while (swapped);

        visualizerPanel.resetHighlights();
    }

    public static void insertionSort(int[] array, VisualizerPanel visualizerPanel) {
        int n = array.length;

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;

                visualizerPanel.setHighlightedBars(j + 1, j);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            array[j + 1] = key;
        }

        visualizerPanel.resetHighlights();
    }

    public static void selectionSort(int[] array, VisualizerPanel visualizerPanel) {
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                visualizerPanel.setHighlightedBars(minIndex, j);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            swap(array, minIndex, i);
        }

        visualizerPanel.resetHighlights();
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}