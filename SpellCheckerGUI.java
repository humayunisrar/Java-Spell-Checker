import javax.swing.*;//for GUI 
import java.awt.*;//for GUI 
import java.awt.event.ActionEvent;//for GUI actions (key press, mouse click)
import java.awt.event.ActionListener;//for GUI actions (key press, mouse click) 
import java.io.*;//for file handling
import java.util.ArrayList;//for arraylist
import java.util.Collections;//for arraylist management

class SpellChecker {//class for spell checker logic spell checking ki is me lag rhi
   
    private ArrayList<String> dictionary;//arraylist for dictionary
    private String dictionaryFilePath = "dictionary.txt";//dictionary file path basically file handling ke liye hai ke kis file se data read karna hai

    SpellChecker() {//constructor for spell checker class ye initialize karega dictionary ko (initialization means dictionary ko file se read karega)
        this.dictionary = loadDictionaryFromFile();//dictionary ko file se load karega
    }

   private ArrayList<String> loadDictionaryFromFile() {// ye wala method dictionary ke words ko lower case me convert karega or arraylist me add karega
    ArrayList<String> loadedDictionary = new ArrayList<>();//arraylist for lower case words
    try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFilePath))) {//file reader for reading file
        String line;//string for reading line by line
        while ((line = reader.readLine()) != null) {//reading line by line
            loadedDictionary.add(line.toLowerCase().trim());  // Convert to lowercase
        }
    } catch (IOException e) {//exception handling means agar file read nhi ho rhi to ye exception throw karega
        e.printStackTrace();//exception print karega
    }
    return loadedDictionary;//returning arraylist means dictionary ko return karega
}


    private void saveDictionaryToFile() {// ye wala method word ko dictionary ki file me save karega
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFilePath))) {//file writer for writing file
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
        JTextArea resultArea = new JTextArea();
        JButton addButton = new JButton("Add to Dictionary");
        JButton checkButton = new JButton("Check");
        resultArea.setFont(new Font("Times New Roman", Font.BOLD, 16));
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        addButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        checkButton.setFont(new Font("Times New Roman", Font.BOLD, 16));

        Color customTextColor = Color.BLACK;  // Customize the text color
        addButton.setForeground(customTextColor);
        checkButton.setForeground(customTextColor);

        Color customBackgroundColor = Color.GRAY;  // Customize the background color
        addButton.setBackground(customBackgroundColor);
        checkButton.setBackground(customBackgroundColor);
        frame.setBackground(customBackgroundColor);

        addButton.setPreferredSize(new Dimension(150, 30));
        checkButton.setPreferredSize(new Dimension(150, 30));

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

        // Use FlowLayout to make buttons appear side by side
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(checkButton);

        frame.setLayout(new GridLayout(4, 1));
        frame.add(textField);
        frame.add(resultArea);
        frame.add(buttonPanel); // Add the panel with buttons
        frame.setVisible(true);
    }
}