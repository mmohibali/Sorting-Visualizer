package sortingvisualizer;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PseudocodeSidebar extends JPanel {
    private JTextPane codeTextPane;
    private StyledDocument doc;
    private SimpleAttributeSet defaultStyle;
    private SimpleAttributeSet highlightStyle;
    private final Map<String, String[]> pseudocodeMap = new HashMap<>();
    private String currentAlgo = "Bubble";

    public PseudocodeSidebar() {
        setLayout(new BorderLayout());
        setBackground(new Color(17, 17, 27));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(69, 71, 90)),
            " Live Pseudocode ",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            new Color(137, 180, 250)
        ));

        codeTextPane = new JTextPane();
        codeTextPane.setEditable(false);
        codeTextPane.setBackground(new Color(17, 17, 27));
        codeTextPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        doc = codeTextPane.getStyledDocument();
        
        defaultStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(defaultStyle, new Color(205, 214, 244));
        
        highlightStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(highlightStyle, new Color(255, 255, 255));
        StyleConstants.setBackground(highlightStyle, new Color(49, 50, 68));
        StyleConstants.setBold(highlightStyle, true);
        
        JScrollPane scrollPane = new JScrollPane(codeTextPane);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        populatePseudocode();
        loadAlgorithm("Bubble");
    }

    private void populatePseudocode() {
        pseudocodeMap.put("Bubble", new String[]{
            "procedure bubbleSort(A)",       // 0
            "  n = length(A)",              // 1
            "  repeat",                     // 2
            "    swapped = false",          // 3
            "    for i = 1 to n-1 do",      // 4
            "      if A[i-1] > A[i] then",    // 5
            "        swap(A[i-1], A[i])",   // 6
            "        swapped = true",       // 7
            "      end if",                 // 8
            "    end for",                  // 9
            "    n = n - 1",                // 10
            "  until not swapped",          // 11
            "end procedure"                 // 12
        });

        pseudocodeMap.put("Insertion", new String[]{
            "procedure insertionSort(A)",    // 0
            "  for i = 1 to length(A)-1 do",// 1
            "    key = A[i]",               // 2
            "    j = i - 1",                // 3
            "    while j >= 0 and A[j] > key do", // 4
            "      A[j+1] = A[j]",          // 5
            "      j = j - 1",              // 6
            "    end while",                // 7
            "    A[j+1] = key",             // 8
            "  end for",                    // 9
            "end procedure"                  // 10
        });

        pseudocodeMap.put("Selection", new String[]{
            "procedure selectionSort(A)",    // 0
            "  n = length(A)",              // 1
            "  for i = 0 to n-1 do",        // 2
            "    minIdx = i",               // 3
            "    for j = i+1 to n do",      // 4
            "      if A[j] < A[minIdx] then", // 5
            "        minIdx = j",           // 6
            "      end if",                 // 7
            "    end for",                  // 8
            "    swap(A[i], A[minIdx])",    // 9
            "  end for",                    // 10
            "end procedure"                  // 11
        });
        
        pseudocodeMap.put("Merge", new String[]{
            "procedure mergeSort(A, l, r)",  // 0
            "  if l < r then",              // 1
            "    mid = (l + r) / 2",        // 2
            "    mergeSort(A, l, mid)",     // 3
            "    mergeSort(A, mid + 1, r)", // 4
            "    merge(A, l, mid, r)",      // 5
            "  end if",                     // 6
            "end procedure"                  // 7
        });

        pseudocodeMap.put("Quick", new String[]{
            "procedure quickSort(A, low, high)", // 0
            "  if low < high then",         // 1
            "    pi = partition(A, low, high)", // 2
            "    quickSort(A, low, pi - 1)", // 3
            "    quickSort(A, pi + 1, high)", // 4
            "  end if",                     // 5
            "end procedure"                  // 6
        });

        pseudocodeMap.put("Heap", new String[]{
            "procedure heapSort(A)",         // 0
            "  buildMaxHeap(A)",            // 1
            "  for i = length(A)-1 down to 1 do", // 2
            "    swap(A[0], A[i])",         // 3
            "    heapify(A, i, 0)",         // 4
            "  end for",                    // 5
            "end procedure"                  // 6
        });

        pseudocodeMap.put("Shell", new String[]{
            "procedure shellSort(A)",        // 0
            "  for gap = n/2 down to 1 do", // 1
            "    for i = gap to n-1 do",    // 2
            "      temp = A[i]",            // 3
            "      for j = i down to gap do", // 4
            "        if A[j-gap] > temp swap", // 5
            "      end for",                // 6
            "    end for",                  // 7
            "end procedure"                  // 8
        });

        pseudocodeMap.put("Counting", new String[]{
            "procedure countingSort(A)",     // 0
            "  count = array of zeros",     // 1
            "  for x in A do count[x]++",   // 2
            "  for i in count do",          // 3
            "    while count[i] > 0 do",    // 4
            "      A[index++] = i",         // 5
            "    end while",                // 6
            "  end for",                    // 7
            "end procedure"                  // 8
        });

        pseudocodeMap.put("Radix", new String[]{
            "procedure radixSort(A)",        // 0
            "  max = getMax(A)",            // 1
            "  for exp = 1 to max do",      // 2
            "    countingSortByDigit(A, exp)", // 3
            "  end for",                    // 4
            "end procedure"                  // 5
        });
    }

    public void loadAlgorithm(String algoName) {
        this.currentAlgo = algoName;
        String[] lines = pseudocodeMap.getOrDefault(algoName, new String[]{"No pseudocode available."});
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        codeTextPane.setText(sb.toString());
        doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
        codeTextPane.setCaretPosition(0);
    }

    public void highlightLine(int lineIndex) {
        SwingUtilities.invokeLater(() -> {
            try {
                doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
                Element root = doc.getDefaultRootElement();
                if (lineIndex >= 0 && lineIndex < root.getElementCount()) {
                    Element lineElement = root.getElement(lineIndex);
                    int start = lineElement.getStartOffset();
                    int end = lineElement.getEndOffset() - 1;
                    if (end >= start) {
                        doc.setCharacterAttributes(start, end - start, highlightStyle, true);
                    }
                }
            } catch (Exception ignored) {}
        });
    }
}