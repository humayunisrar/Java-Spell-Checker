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
            for (String word : dictionary) {// dictionary ke words ko file me write karega  
                writer.write(word);// writing word in file
                writer.newLine();// new line for next word
            }
        } catch (IOException e) {//exception handling means agar file write nhi ho rhi to ye exception throw karega
            e.printStackTrace();//exception print karega
        }
    }

    void addToDictionary(String word) {// ye wala method word ko dictionary me add karega
        int index = Collections.binarySearch(dictionary, word.toLowerCase());//dictionary me word ko search karega

        // Handle the case when the word is not found
        if (index < 0) {    // If the word is not found in the dictionary to add it
            dictionary.add(-index - 1, word.toLowerCase());//dictionary me word ko add karega
            saveDictionaryToFile();  // Save the updated dictionary to the file
        }
    }

    boolean isSpelledCorrectly(String word) {// ye wala method word ko dictionary me search karega
        return Collections.binarySearch(dictionary, word.toLowerCase()) >= 0;//dictionary me word ko search karega or agar word mil jaye to true return karega
    }

    ArrayList<String> suggestCorrections(String misspelledWord) {// ye wala method word ko dictionary me search karega or agar word nahi mil jaye to uske similar words ko arraylist me add karega or return karega
        ArrayList<String> suggestions = new ArrayList<>();//arraylist for similar words of misspelled word means suggestions dega agar word nahi mila to
        for (String word : dictionary) {//dictionary ke words ko check karega or agar word nahi mila to uske similar words ko arraylist me add karega
            if (isSimilar(word, misspelledWord)) {//dictionary ke words ko misspelled word ke sath compare karega
                suggestions.add(word);//dictionary ke words ko arraylist me add karega
            }//agar word mil jaye to suggestions me add nahi karega
        }
        return suggestions;//returning arraylist means suggestions ko return karega
    }

    private boolean isSimilar(String word1, String word2) {// ye wala method word1 or word2 ko compare karega or agar similar honge to true return karega or agar nahi to false
        
        // Implement a more precise similarity check (e.g., Levenshtein distance)
        int distance = calculateLevenshteinDistance(word1, word2);// word1 or word2 ko compare karega or agar similar honge to distance 0 hoga or agar nahi to distance 1 se zyada hoga distance means ke kitne letters different hai dono words me
        return distance <= 1;// agar distance 1 se kam ya equal hoga to true return karega or agar nahi to false
    }

    private int calculateLevenshteinDistance(String s1, String s2) {// ye wala method word1 or word2 ko compare karega or agar similar honge to distance 0 hoga or agar nahi to distance 1 se zyada hoga distance means ke kitne letters different hai dono words me ye upar wale method me use/call hoga 

        int m = s1.length();//length of word1
        int n = s2.length();//length of word2 phir dono words ke length ko compare karega

        int[][] dp = new int[m + 1][n + 1];// 2d array for dp means dynamic programming (dp is used for storing the sub problems of a problem so that we can use them later on) iska faida ye hai ke agar word1 or word2 ke length same hai to ye 2d array me 0 return karega or agar length different hai to ye 2d array me 1 se zyada return karega phir upar wale method me use/call hoga

        for (int i = 0; i <= m; i++) {// word1 ke letters ko compare karega
            for (int j = 0; j <= n; j++) {// word2 ke letters ko compare karega or agar word1 or word2 ke letters same honge to 0 return karega or agar nahi to 1 se zyada return karega

                if (i == 0) {// agar word1 ka length 0 hoga to 0 return karega or agar nahi to 1 se zyada return karega
                    dp[i][j] = j;// agar word1 ka length 0 hoga to 0 return karega or agar nahi to 1 se zyada return karega dp yaha pe 2d array hai or i or j 2d array ke indexes hai

                } else if (j == 0) {// agar word2 ka length 0 hoga to 0 return karega or agar nahi to 1 se zyada return karega
                    dp[i][j] = i;// agar word2 ka length 0 hoga to 0 return karega or agar nahi to 1 se zyada return karega dp yaha pe 2d array hai or i or j 2d array ke indexes hai
                } else {// agar word1 or word2 ke length same honge to ye 2d array me 0 return karega or agar nahi to ye 2d array me 1 se zyada return karega
                    dp[i][j] = min(// agar word1 or word2 ke length same honge to ye 2d array me 0 return karega or agar nahi to ye 2d array me 1 se zyada return karega
                            dp[i - 1][j] + 1,// -1 YAHA PE YE HAI KE AGAR WORD1 OR WORD2 KE LENGTH SAME HONGE TO YE 2D ARRAY ME 0 RETURN KAREGA OR AGAR NAHI TO YE 2D ARRAY ME 1 SE ZYADA RETURN KAREGA 
                            dp[i][j - 1] + 1,// +1 yaha pe ye hai ke agar word1 or word2 ke length same honge to ye 2d array me 0 return karega or agar nahi to ye 2d array me 1 se zyada return karega 
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)// s1 or s2 ke letters ko compare karega or agar similar honge to 0 return karega or agar nahi to 1 se zyada return karega char at means ke word1 or word2 ke letters ko compare karega i-1 or j-1 means ke word1 or word2 ke letters ko compare karega or agar similar honge to 0 return karega or agar nahi to 1 se zyada return karega
                    );
                }
            }
        }

        return dp[m][n];// m or n 2d array ke indexes hai or agar word1 or word2 ke length same honge to ye 2d array me 0 return karega or agar nahi to ye 2d array me 1 se zyada return karega dp yaha pe 2d array hai or m or n 2d array ke indexes hai
    }

    private int min(int a, int b, int c) {// ye wala method 3 numbers ko compare karega or sab se chota number return karega 3 number is liye compare karega kiun ke minimum 3 numbers me se 1 number return karna hai
        return Math.min(Math.min(a, b), c);// maths ke min method se 3 numbers ko compare karega or sab se chota number return karega 3 number is liye compare karega kiun ke minimum 3 numbers me se 1 number return karna hai
    }
    
}

public class SpellCheckerGUI {//class for GUI
    public static void main(String[] args) {//main method for GUI
        SwingUtilities.invokeLater(() -> createAndShowGUI());//GUI ko invoke karega invoke ka matlab ye hai ke GUI ko run karega
    }

    private static void createAndShowGUI() {// ye wala method GUI ko create karega or show karega means GUI ko bana ke run karega
        JFrame frame = new JFrame("Spell Checker");//frame for GUI 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// frame ko close karega exit on close ka matlab ye hai ke frame ko close karega default frame ko close karne ka method hai ye a rha hai java ki library se
        frame.setSize(600, 300);//frame ki size set karega

        SpellChecker spellChecker = new SpellChecker();//spell checker class ka object banayega kiun ke spell checker class me dictionary hai or dictionary ko load karna hai 

        JTextField textField = new JTextField();//text field for GUI
        JTextArea resultArea = new JTextArea();//text area for GUI
        JButton addButton = new JButton("Add to Dictionary");//button for GUI
        JButton checkButton = new JButton("Check");//button for GUI
        resultArea.setFont(new Font("Times New Roman", Font.BOLD, 16));//font for text area
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));//font for text field

        addButton.setFont(new Font("Times New Roman", Font.BOLD, 16));//font for button
        checkButton.setFont(new Font("Times New Roman", Font.BOLD, 16));//font for button

        Color customTextColor = Color.BLACK;  // Customize the text color
        addButton.setForeground(customTextColor);//text color for button
        checkButton.setForeground(customTextColor);////text color for button

        Color customBackgroundColor = Color.GRAY;  // Customize the background color
        addButton.setBackground(customBackgroundColor);//background color for button
        checkButton.setBackground(customBackgroundColor);//background color for button
        frame.setBackground(customBackgroundColor);//background color for frame

        addButton.setPreferredSize(new Dimension(150, 30));//size for button
        checkButton.setPreferredSize(new Dimension(150, 30));//size for button

        addButton.addActionListener(new ActionListener() {//action listener for button ye wala method button ke click ko handle karega
            @Override // Override the actionPerformed method of the ActionListener interface override ka matlab ye hai ke ye method ActionListener interface ke method ko override karega. Action listener jo hai wo interface hai or interface me method ko override karne ke liye ye @Override ka use karte hai
            public void actionPerformed(ActionEvent e) {//action performed method for button ye wala method button ke click ko handle karega matlab button ke click ko handle karega jab hum button pe click karenge to ye method chalega
                String inputText = textField.getText().toLowerCase();//text field se text ko read karega or lower case me convert karega
                if (!inputText.isEmpty() && !spellChecker.isSpelledCorrectly(inputText)) {//text field se text ko read karega or lower case me convert karega or agar text field khali nahi hai or agar text field me word nahi hai to ye wala condition chalega jab condition empty nahi hai or word nahi hai to ye wala condition chalega
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