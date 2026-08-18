package sortingvisualizer;

public class SortingMetrics {
    private int comparisons = 0;
    private int swaps = 0;
    private long startTime = 0;
    private long elapsedTime = 0;

    public synchronized void reset() {
        comparisons = 0;
        swaps = 0;
        elapsedTime = 0;
    }

    public synchronized void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public synchronized void stopTimer() {
        if (startTime > 0) {
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }

    public synchronized void incrementComparisons() {
        comparisons++;
    }

    public synchronized void incrementSwaps() {
        swaps++;
    }

    public synchronized int getComparisons() {
        return comparisons;
    }

    public synchronized int getSwaps() {
        return swaps;
    }

    public synchronized long getElapsedTime() {
        if (startTime > 0 && elapsedTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return elapsedTime;
    }
}