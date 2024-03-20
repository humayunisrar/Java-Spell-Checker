# Spell Checker

This repository contains a Java program for a simple spell checker with a graphical user interface (GUI). The spell checker allows users to add words to a dictionary, check the spelling of a word, and provides suggestions for misspelled words.

## Features
- **Adding Words to Dictionary:** Users can add words to the dictionary, which is saved to a file for persistent storage.
- **Spell Checking:** Users can check the spelling of a word against the dictionary.
- **Suggestions for Misspelled Words:** If a word is misspelled, the program provides suggestions for corrections based on Levenshtein distance algorithm.

## Installation and Usage
1. Ensure you have Java Development Kit (JDK) installed on your system.
2. Clone this repository to your local machine.
   ```bash
   git clone https://github.com/your-username/spell-checker.git
   ```
3. Compile the Java files.
   ```bash
   javac Spell.java
   ```
4. Run the program.
   ```bash
   java Spell
   ```
5. The GUI application window will open. You can use the text field to input words and the buttons to add words to the dictionary or check the spelling of words.

## Code Structure
- `SpellChecker.java`: Contains the logic for spell checking, dictionary management, and Levenshtein distance algorithm implementation.
- `Spell.java`: Main class with GUI implementation using Swing framework.
- `dictionary.txt`: File used to store the dictionary words.

## How to Contribute
Contributions to improve the spell checker or add new features are welcome. Here's how you can contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/improvement`).
3. Make your changes and commit them (`git commit -am 'Add new feature'`).
4. Push the changes to your branch (`git push origin feature/improvement`).
5. Create a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
© 2024 Spell Checker Authors.
