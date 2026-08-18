package sortingvisualizer;

import javax.sound.sampled.*;

public class SortingAudio {
    private static final int SAMPLE_RATE = 8000;
    private static boolean soundEnabled = true;

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void playTone(int frequency, int durationMs) {
        if (!soundEnabled) return;

        // Run tone playback on a lightweight background thread to prevent audio lag
        new Thread(() -> {
            try {
                int numSamples = (SAMPLE_RATE * durationMs) / 1000;
                byte[] buffer = new byte[numSamples];

                for (int i = 0; i < numSamples; i++) {
                    double angle = 2.0 * Math.PI * i * frequency / SAMPLE_RATE;
                    buffer[i] = (byte) (Math.sin(angle) * 127);
                }

                AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                if (!AudioSystem.isLineSupported(info)) return;

                try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                    line.open(format, numSamples);
                    line.start();
                    line.write(buffer, 0, buffer.length);
                    line.drain();
                }
            } catch (Exception ignored) {
                // Ignore audio line exceptions to keep visualizer smooth
            }
        }).start();
    }
}