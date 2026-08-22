# Sorting Algorithms Visualizer

An advanced, high-performance Java desktop application designed to bridge the gap between theoretical computer science and practical execution through real-time visualization, live pseudocode tracking, dynamic audio feedback, and side-by-side algorithmic racing.

---

## Table of Contents
- [Overview](#overview)
- [Key Features & Architectural Modules](#key-features--architectural-modules)
- [Algorithmic Suite & Complexity Reference](#algorithmic-suite--complexity-reference)
- [GUI Architecture & Layout](#gui-architecture--layout)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation & Running](#installation--running)
- [System Workflow](#system-workflow)
- [Project Structure](#project-structure)
- [Visualizations](#visualizations)

---

## Overview

**Sorting Algorithms Visualizer** is a robust educational and analytical framework built using Java Swing/AWT. Designed for high responsiveness, it utilizes multi-threaded background workers (`SwingWorker` and `ExecutorService`) to render smooth frame updates without freezing the user interface. 

Whether studying the inner mechanics of classic comparison sorts or comparing the runtime efficiency of distribution sorts in real-time, this tool provides granular telemetry, audio synthesis mapped to element values, and live line-by-line pseudocode tracking.

---

## Key Features & Architectural Modules

- **Live Pseudocode Sidebar:** Real-time, line-by-line algorithm tracking that highlights active execution steps (comparisons, loops, swaps) as sorting occurs.
- **Dual-Mode Race View:** Compare any two sorting algorithms side-by-side in real-time split-screen view to evaluate performance differences visually.
- **Dynamic Audio Feedback Engine:** Multithreaded frequency-based tone generation mapped directly to element values during sorting operations.
- **Custom Theme Engine:** Dynamically switch between polished color palettes like Cyberpunk Neon, Matrix Terminal, and Sunset Dusk.
- **Diverse Data Distributions:** Instant generation of Random, Nearly Sorted, Reversed, and Few-Unique datasets to test best-case and worst-case algorithmic behaviors.
- **Live Metrics Dashboard:** Tracks real-time comparisons, swaps, array accesses, elapsed execution time, and dynamic Big-O complexity badges ($O(n \log n)$, $O(n^2)$, etc.).

---

## Algorithmic Suite & Complexity Reference

| Algorithm | Best Time | Average Time | Worst Time | Space Complexity | Stability |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Bubble Sort** | $O(n)$ | $O(n^2)$ | $O(n^2)$ | $O(1)$ | Stable |
| **Insertion Sort** | $O(n)$ | $O(n^2)$ | $O(n^2)$ | $O(1)$ | Stable |
| **Selection Sort** | $O(n^2)$ | $O(n^2)$ | $O(n^2)$ | $O(1)$ | Unstable |
| **Merge Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n \log n)$ | $O(n)$ | Stable |
| **Quick Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n^2)$ | $O(\log n)$ | Unstable |
| **Heap Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n \log n)$ | $O(1)$ | Unstable |
| **Shell Sort** | $O(n \log n)$ | $O(n \log n)$ | $O(n^2)$ | $O(1)$ | Unstable |
| **Counting Sort** | $O(n + k)$ | $O(n + k)$ | $O(n + k)$ | $O(k)$ | Stable |
| **Radix Sort** | $O(nk)$ | $O(nk)$ | $O(nk)$ | $O(n + k)$ | Stable |

---

## GUI Architecture & Layout

The interface is structured into three primary control and display zones:

1. **North Control Panel & Analytics Dashboard:**
   - Single algorithm buttons or Dual-Mode race selectors.
   - Global execution controls (`Pause`, `Resume`, `Stop`).
   - Live telemetry counters (Comparisons, Swaps, Accesses, Elapsed Time) and Big-O badges.
2. **Center Visualizer Workspace (`JSplitPane`):**
   - Renders animated bar charts with real-time color-coding for active elements, pivots, and sorted partitions[cite: 17].
3. **East Pseudocode Sidebar:**
   - Displays clean pseudocode blocks for the active algorithm with dynamic line highlighting driven by execution threads.
4. **South Control Panel:**
   - Distribution selectors (`Random`, `Nearly Sorted`, `Reversed`, `Few Unique`), array generator, speed adjustment slider ($1\text{ms}$ to $150\text{ms}$), theme selector, and audio toggle.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher.
- **Operating System:** Compatible with Windows, macOS, and Linux[cite: 17].

### Installation & Running
1. Clone the repository to your local directory:
   ```bash
   git clone https://github.com/your-username/sorting-algorithms-visualizer.git
   cd sorting-algorithms-visualizer
   ```
2. Compile the source files:
Navigate to the project directory and compile the package
   ```bash
   javac sortingvisualizer/*.java
   ```
3. Run the application:
   ```bash
   java sortingvisualizer.SortingVisualizer
   ```
## System Workflow
1. Select Mode: Choose between Single Visualizer mode (with Live Pseudocode) or toggle Dual Mode for a side-by-side race.

2. Choose Data Distribution: Pick from Random, Nearly Sorted, Reversed, or Few Unique distributions and click Generate Array.

3. Configure Speed & Audio: Adjust the speed slider and toggle sound feedback as desired.

4. Execute Algorithm: Click any algorithm button (e.g., Merge or Quick) to start background sorting via SwingWorker.

5. Monitor Execution: Watch live bar movements, listen to audio frequencies, observe live pseudocode line highlights, and analyze comparative metrics on the dashboard.

## Project Structure
   ```bash
   sorting-algorithms-visualizer/
   │
   ├── sortingvisualizer/
   │   ├── SortingVisualizer.java     # Main application frame, layout, and event handling
   │   ├── SortingAlgorithms.java     # Core sorting logic, thread coordination, and hooks
   │   ├── VisualizerPanel.java       # Custom rendering canvas for bar charts and color themes
   │   ├── PseudocodeSidebar.java     # Live line-by-line pseudocode tracking component
   │   ├── SortingMetrics.java        # Real-time telemetry tracker (comparisons, swaps, timer)
   │   ├── SortingAudio.java          # Multithreaded frequency tone generator
   │   └── SortingThemes.java         # Color palette manager (Cyberpunk, Matrix, Sunset)
   │
   └── README.md                      # Complete project documentation
   ```
## Visualizations
1. Single Mode with Live Pseudocode Sidebar
<img width="1266" height="711" alt="image" src="https://github.com/user-attachments/assets/0b77b64d-8ea6-4333-b3e2-130171603177" />
Description: Active single-mode execution showing the bar visualization panel alongside the live pseudocode sidebar tracking the exact line of execution.


2. Dual-Mode Race View
<img width="1265" height="713" alt="image" src="https://github.com/user-attachments/assets/79f3bb48-8e88-4b69-8c00-f3b64fc51bc1" />
Description: Split-screen comparative race pitting two distinct algorithms against each other on identical datasets with real-time performance telemetry.
