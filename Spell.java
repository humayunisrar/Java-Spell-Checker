import javax.swing.*;//for GUI 
import java.awt.*;//for GUI 
import java.awt.event.ActionEvent;//for GUI actions (key press, mouse click)
import java.awt.event.ActionListener;//for GUI actions (key press, mouse click) 
import java.io.*;//for file handling
import java.util.ArrayList;//for arraylist
import java.util.Collections;//for arraylist management like add, delete, search etc

class SpellChecker {//class for spell checker logic spell checking ki is me lag rhi
   
    private ArrayList<String> dictionary; 
    private String dictionaryFilePath = "dictionary.txt"; 

    SpellChecker() {
        this.dictionary = loadDictionaryFromFile();
    }
                            //TRAVERSING THE DICTIONARY

   private ArrayList<String> loadDictionaryFromFile() { //private arraylist declare ki or ye wala method dictionary ko load karega file se
    ArrayList<String> loadedDictionary = new ArrayList<>();//Initializes an ArrayList named loadedDictionary to store the words read from the file.
    try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFilePath))) {//try block me buffered reader ka object banaya or file reader ka object banaya or dictionary file path ko read karne ke liye dictionary file path me dictionary file ka path hai. buffer reader allows us to read the file line by line and efficiently. while file reader focuses on reading characters.
        String line;//String variable named line to store the line read from the file.
        while ((line = reader.readLine()) != null) {//while loop to read the file line by line until the end of the file is reached.
            loadedDictionary.add(line.toLowerCase().trim());//Adds the line read from the file to the ArrayList named loadedDictionary. The toLowerCase() method converts the line to lowercase and the trim() method removes any leading or trailing spaces.
        }
    } catch (IOException e) {//catch for error handling means ke agar exception aye to ye catch block chalega
        e.printStackTrace();//To print the exception.
    }
    return loadedDictionary;//Returns the loadedDictionary jisme words read from the file.
}

                            // SAVING THE DICTIONARY

    private void saveDictionaryToFile() {  //This method is responsible for saving the current state of the dictionary to a file. like agar hum koi word add karte hai to wo dictionary me save ho jaye
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictionaryFilePath))) { //FileWriter is a class that writes character data to a file It writes data character by character and is often used for writing raw text data. BufferedWriter writes data in larger chunks, improving efficiency. It has methods like write(String str) and newLine() that allow you to write text more conveniently, especially when writing in larger chunks. 
            //here we are using both file writer and buffered writer. file writer is used to write the data to the file and buffered writer is used to write the data in larger chunks.
            for (String word : dictionary) {// for loop to traverse the dictionary and write each word to the file.
                writer.write(word);//write method to write the word to the file.
                writer.newLine();//newLine method to write a new line after each word.
            }
        } catch (IOException e) {//catch for error handling means ke agar exception aye to ye catch block chalega exception jese ke file not found exception 
            e.printStackTrace();//To print the exception.
        }
    }
           
                            //INSERTING WORDS INTO THE DICTIONARY
                            //INSERTION + BINARY SEARCH 


    void addToDictionary(String word) { //This method is responsible for adding a word to the dictionary.
        int index = Collections.binarySearch(dictionary, word.toLowerCase()); // Uses binary search (Collections.binarySearch) to find the index of the given word in the dictionary. The word.toLowerCase() ensures uniformity by converting the word to lowercase.

        
        if (index < 0) {   //Enters a conditional block if the word is not found in the dictionary (the binary search result is negative). index<0 because if the word is not found in the dictionary then the binary search result is negative.
            dictionary.add(-index - 1, word.toLowerCase());  // Adds the word to the dictionary at the appropriate index to maintain alphabetical order. The -index - 1 is used to convert the negative result of binarySearch to the index where the word should be inserted.
           
            //The dictionary.add(-index - 1, word.toLowerCase()) line adds the word to the dictionary at the calculated insertion point.
            //-index - 1 is used as the index because, in Java, when adding an element at a specific index using add(index, element), the element is inserted before the element currently at that position.
            // For example, if the word is to be inserted at index 2, the add method will insert the word before the element at index 2.
            // Therefore, the word will be inserted at index 1, which is the correct position to maintain alphabetical order.
            // if there is collision then the word will be inserted at the end of the collision.
            
            saveDictionaryToFile();  // Saves the dictionary to the file.
        }}
    
            // SPELL CHECKING USING BINARY SEARCH AND LEVENSHTEIN DISTANCE ALGORITHM

    boolean isSpelledCorrectly(String word) { //Uses binary search to check whether a given word is present in the dictionary. If the word is found, it returns true, otherwise, false. Method assumes that the dictionary is sorted in ascending order, as required by the binary search algorithm.
        return Collections.binarySearch(dictionary, word.toLowerCase()) >= 0; //Checks if the result of the binary search is greater than or equal to zero.
            //If the result is greater than or equal to zero, it means the word was found in the dictionary at the returned index. 
    }

    
    private boolean isSimilar(String word1, String word2) { // This method is responsible for checking whether two words are similar. It returns true if the words are similar, otherwise, false.
        
        int distance = calculateLevenshteinDistance(word1, word2);  //Calls the calculateLevenshteinDistance method to compute the Levenshtein distance between word1 and word2. The Levenshtein distance measures the minimum number of single-character edits (insertions, deletions, or substitutions) required to change one word into the other. word 1 
        return distance <= 1; //Returns true if the calculated Levenshtein distance is less than or equal to 1, indicating that the words are considered similar. Otherwise, it returns false.
    }
    ArrayList<String> suggestCorrections(String misspelledWord) { // This method is responsible for suggesting corrections for a misspelled word. It returns an ArrayList of strings containing the suggested corrections.
        ArrayList<String> suggestions = new ArrayList<>(); // Initializes an ArrayList named suggestions to store the suggested corrections.
        for (String word : dictionary) { // for loop to traverse the dictionary.
            if (isSimilar(word, misspelledWord)) { // Checks if the word is similar to the misspelled word.
                suggestions.add(word);// Adds the word to the suggestions ArrayList if it is similar to the misspelled word.
            }
        } 
          
        return suggestions;// Returns the suggestions ArrayList. 
    }

                //DISTANCE ALGO +( RECURSION + TRAVERSING )

    private int calculateLevenshteinDistance(String s1, String s2) {  //Declares a private method named calculateLevenshteinDistance that takes two strings, s1 and s2, as input and returns an integer representing the distance between them.

        int m = s1.length();// Declares an integer variable named m to store the length of the first string / word.
        int n = s2.length();// Declares an integer variable named n to store the length of the second string/ word.

        int[][] dp = new int[m + 1][n + 1]; // m + 1 and n + 1 are dimensions of 2D Array dp, where m is the length of string s1 and n is the length of string s2.
                                            // +1 is added to the dimensions because the first row and column of the 2D Array dp are used to store the base cases of the Levenshtein distance algorithm. base cases are the values of the Levenshtein distance when one of the strings is empty.
        for (int i = 0; i <= m; i++) {// for loop to traverse the character of first string / word.
            for (int j = 0; j <= n; j++) { // for loop to traverse the character of second string / word.

                if (i == 0) { // Enters a conditional block if the first string / word is empty.
                    dp[i][j] = j; // If i is 0, it means the prefix of s1 is an empty string. In this case, the distance is equal to the length of the prefix of s2 (j).
                } else if (j == 0) { // Enters a conditional block if the second string / word is empty.
                    dp[i][j] = i; // If j is 0, it means the prefix of s2 is an empty string. In this case, the distance is equal to the length of the prefix of s1 (i).
                } else { // Enters a conditional block if both strings / words are non-empty.
                    dp[i][j] = min( // it calculates the minimum of the three values and assigns it to dp[i][j].
                            dp[i - 1][j] + 1, // Represents deletion in s1.
                            dp[i][j - 1] + 1, // Represents insertion in s1.
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1) // Represents substitution in s1. The expression s1.charAt(i - 1) is used to access the character in s1 at the position represented by i - 1. 
                                 //Vn me hai puri explaination                                // -1 is liye because humne i ko 0 se start krna hai or index 0 se start hota hai. 
                            );
                }}}

        return dp[m][n]; // Returns the value of dp[m][n], which represents the distance between s1 and s2.
    }

    private int min(int a, int b, int c) { // Declares a private method named min that takes three integers, a, b, and c, as input and returns the minimum of the three integers.
        return Math.min(Math.min(a, b), c); // Returns the minimum of a, b, and c using the Math.min method.
    }
}
                // GUI 

public class Spell {//class for GUI
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
        checkButton.setForeground(customTextColor);//text color for button

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
                }}
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