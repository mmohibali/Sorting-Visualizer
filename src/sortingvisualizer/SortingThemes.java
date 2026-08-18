package sortingvisualizer;

import java.awt.Color;

public class SortingThemes {
    public static class Theme {
        public String name;
        public Color background;
        public Color panelBackground;
        public Color textPrimary;
        public Color barDefault;
        public Color barHighlight;
        public Color barSorted;

        public Theme(String name, Color background, Color panelBackground, Color textPrimary, Color barDefault, Color barHighlight, Color barSorted) {
            this.name = name;
            this.background = background;
            this.panelBackground = panelBackground;
            this.textPrimary = textPrimary;
            this.barDefault = barDefault;
            this.barHighlight = barHighlight;
            this.barSorted = barSorted;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final Theme CYBERPUNK = new Theme(
        "Cyberpunk Neon",
        new Color(13, 17, 23),   // Deep dark background
        new Color(22, 27, 34),   // Panel background
        new Color(240, 246, 252),// Crisp white text
        new Color(0, 229, 255),  // Electric cyan bars
        new Color(255, 0, 85),   // Hot neon pink highlight
        new Color(57, 255, 20)   // Neon green sorted
    );

    public static final Theme MATRIX = new Theme(
        "Matrix Terminal",
        new Color(10, 15, 10),   // Dark terminal background
        new Color(18, 28, 18),   // Dark olive surface
        new Color(100, 255, 100),// Bright green text
        new Color(0, 180, 80),   // Classic terminal green bars
        new Color(255, 255, 0),  // Bright yellow highlight
        new Color(0, 255, 150)   // Bright mint sorted
    );

    public static final Theme SUNSET = new Theme(
        "Sunset Dusk",
        new Color(26, 20, 35),   // Deep purple dark background
        new Color(40, 31, 56),   // Surface
        new Color(255, 224, 178),// Warm cream text
        new Color(255, 111, 97), // Coral orange bars
        new Color(255, 209, 102),// Bright golden highlight
        new Color(78, 205, 196)  // Teal sorted
    );

    public static Theme[] getAvailableThemes() {
        return new Theme[] { CYBERPUNK, MATRIX, SUNSET };
    }
}