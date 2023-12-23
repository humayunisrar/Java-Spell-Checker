import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

class SpellChecker {
    private ArrayList<String> dictionary;
    private String dictionaryFilePath = "dictionary.txt";

    SpellChecker() {
        this.dictionary = loadDictionaryFromFile();
    }

   private ArrayList<String> loadDictionaryFromFile() {
    ArrayList<String> loadedDictionary = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            loadedDictionary.add(line.toLowerCase().trim());  // Convert to lowercase
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return loadedDictionary;
}


    private void saveDictionaryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFilePath))) {
            for (String word : dictionary) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addToDictionary(String word) {
        int index = Collections.binarySearch(dictionary, word.toLowerCase());

        // Handle the case when the word is not found
        if (index < 0) {
            dictionary.add(-index - 1, word.toLowerCase());
            saveDictionaryToFile();  // Save the updated dictionary to the file
        }
    }

    boolean isSpelledCorrectly(String word) {
        return Collections.binarySearch(dictionary, word.toLowerCase()) >= 0;
    }

    ArrayList<String> suggestCorrections(String misspelledWord) {
        ArrayList<String> suggestions = new ArrayList<>();
        for (String word : dictionary) {
            if (isSimilar(word, misspelledWord)) {
                suggestions.add(word);
            }
        }
        return suggestions;
    }

    private boolean isSimilar(String word1, String word2) {
        // Implement a more precise similarity check (e.g., Levenshtein distance)
        int distance = calculateLevenshteinDistance(word1, word2);
        return distance <= 1;
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1,
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }

        return dp[m][n];
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
    
}


public class SpellCheckerGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Spell Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        SpellChecker spellChecker = new SpellChecker();

        JTextField textField = new JTextField();
        JButton addButton = new JButton("Add to Dictionary");
        JButton checkButton = new JButton("Check");
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Times New Roman", Font.BOLD, 14));
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        addButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        checkButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        
        Color customTextColor = Color.BLACK;  // Customize the text color
        addButton.setForeground(customTextColor);
        checkButton.setForeground(customTextColor);

        Color customBackgroundColor = Color.LIGHT_GRAY;  // Customize the background color
        addButton.setBackground(customBackgroundColor);
        checkButton.setBackground(customBackgroundColor);
        frame.setBackground(customBackgroundColor);

        addButton.setPreferredSize(new Dimension(100, 30));
        checkButton.setPreferredSize(new Dimension(100, 30));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textField.getText().toLowerCase();
                if (!inputText.isEmpty() && !spellChecker.isSpelledCorrectly(inputText)) {
                    spellChecker.addToDictionary(inputText);
                    textField.setText("");
                    resultArea.setText("Word added to dictionary: " + inputText);
                }
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textField.getText().toLowerCase();
                if (spellChecker.isSpelledCorrectly(inputText)) {
                    resultArea.setText("Correct spelling");
                } else {
                    ArrayList<String> suggestions = spellChecker.suggestCorrections(inputText);
                    if (!suggestions.isEmpty()) {
                        resultArea.setText("Suggestions: " + String.join(", ", suggestions));
                    } else {
                        resultArea.setText("No suggestions found");
                    }
                }
            }
        });
        
        frame.setLayout(new GridLayout(4, 1));
        frame.add(textField);
        frame.add(addButton);
        frame.add(checkButton);
        frame.add(resultArea);

        frame.setVisible(true);
    }
   
}
