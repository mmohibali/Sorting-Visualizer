package sortingvisualizer;

public class SortingMetrics {
    private int comparisons = 0;
    private int swaps = 0;
    private long startTime = 0;
    private long elapsedTime = 0;
    private long pauseStartTime = 0;
    private boolean running = false;
    private boolean paused = false;

    public synchronized void reset() {
        comparisons = 0;
        swaps = 0;
        startTime = 0;
        elapsedTime = 0;
        pauseStartTime = 0;
        running = false;
        paused = false;
    }

    public synchronized void startTimer() {
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        running = true;
        paused = false;
    }

    public synchronized void pauseTimer() {
        if (running && !paused) {
            elapsedTime += (System.currentTimeMillis() - startTime);
            paused = true;
        }
    }

    public synchronized void resumeTimer() {
        if (running && paused) {
            startTime = System.currentTimeMillis();
            paused = false;
        }
    }

    public synchronized void stopTimer() {
        if (running) {
            if (!paused) {
                elapsedTime += (System.currentTimeMillis() - startTime);
            }
            running = false;
            paused = false;
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
        if (running && !paused) {
            return elapsedTime + (System.currentTimeMillis() - startTime);
        }
        return elapsedTime;
    }
}