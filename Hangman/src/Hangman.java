import javafx.application.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.event.*;

import java.io.*;

import java.util.*;

public class Hangman extends Application
{
    
   
   
    private String currentWord; // the randomly selected word
    private TextField guessField = new TextField(); // the user enters their guess here
    private Text currentWordText = new Text("");//show the current word (with - for unguessed letters)
    private Text wrongGuessesText = new Text("Wrong Guesses:");// show a list of incorrect guesses
    private Text outcomeText = new Text("");// show the outcome of each guess and the game
    private Text wrongGuessNumberText = new Text(""); // show how many incorrect guesses (or how many guesses remain)
    private int numWrongGuesses=0;// counter for number of guesses
    private static final int MAX_WRONG_GUESSES = 7;
    private static final Color TITLE_AND_OUTCOME_COLOR = Color.rgb(221, 160, 221);
    private boolean[] isWordPositionGuessed;
    private static final Color INFO_COLOR = Color.rgb(224, 255, 255);
    private static final Color WORD_COLOR = Color.rgb(224, 255, 255);
    private ArrayList<String> wordList= new ArrayList<String>();// word choice
    private ArrayList<Character> guessedCharactersList= new ArrayList<Character>();
    private Button playAgainButton = new Button("Play Again");
    
  
    @Override
    public void start(final Stage primaryStage) {
        final VBox mainVBox = new VBox();
        mainVBox.setStyle("-fx-background-color: royalblue");
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setSpacing(10.0);
       
        final Text welcomeText = new Text("Welcome to Hangman!");
        welcomeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 36.0));
        welcomeText.setFill(Hangman.TITLE_AND_OUTCOME_COLOR);
      
        final Text introText1 = new Text("Guess a letter.");
        final Text introText2 = new Text("You can make 7 wrong guesses!");
        introText1.setFont(Font.font("Helvetica", 24.0));
        introText1.setFill(Hangman.INFO_COLOR);
        introText2.setFont(Font.font("Helvetica", 24.0));
        introText2.setFill(Hangman.INFO_COLOR);
       
        final VBox introBox = new VBox(new Node[] { welcomeText, introText1, introText2 });
        introBox.setAlignment(Pos.CENTER);
        introBox.setSpacing(10.0);
        mainVBox.getChildren().add(introBox);
        
        
         gameStart();                 // starting
			
    
        currentWordText.setFont(Font.font("Helvetica", FontWeight.BOLD, 48.0));
        currentWordText.setFill(Hangman.WORD_COLOR);
        
        final HBox currentBox = new HBox(new Node[] { currentWordText });
        currentBox.setAlignment(Pos.CENTER);
        currentBox.setSpacing(10.0);
        mainVBox.getChildren().add(currentBox);
        
        final Text guessIntroText = new Text("Enter your guess: ");
        guessIntroText.setFont(Font.font("Helvetica", 26.0));
        guessIntroText.setFill(Hangman.INFO_COLOR);
        guessField.setOnAction(this::handleGuessField);
        
        final HBox guessBox = new HBox(new Node[] { guessIntroText, guessField });
        guessBox.setAlignment(Pos.CENTER);
        guessBox.setSpacing(10.0);
        mainVBox.getChildren().add(guessBox);
        outcomeText.setFont(Font.font("Helvetica", 28.0));
        outcomeText.setFill(Hangman.TITLE_AND_OUTCOME_COLOR);
       
        final HBox outcomeBox = new HBox(new Node[] { outcomeText });
        outcomeBox.setAlignment(Pos.CENTER);
        outcomeBox.setSpacing(10.0);
        mainVBox.getChildren().add(outcomeBox);
        wrongGuessesText.setFont(Font.font("Helvetica", 24.0));
        wrongGuessesText.setFill(Hangman.INFO_COLOR);
       
        
        final HBox wrongGuessesBox = new HBox(new Node[] { wrongGuessesText });
        wrongGuessesBox.setAlignment(Pos.CENTER);
        wrongGuessesBox.setSpacing(10.0);
        mainVBox.getChildren().add(wrongGuessesBox);
        wrongGuessNumberText.setFont(Font.font("Helvetica", 24.0));
        wrongGuessNumberText.setFill(Hangman.INFO_COLOR);
        
       
        
        
        wrongGuessNumberText.setFont(Font.font("Helvetica", 24));
		wrongGuessNumberText.setFill(INFO_COLOR);
		HBox wrongGuessNumberBox = new HBox(wrongGuessNumberText);
		wrongGuessNumberBox.setAlignment(Pos.CENTER);
		mainVBox.getChildren().add(wrongGuessNumberBox);
       
        
        final HBox buttonBox = new HBox(new Node[] { playAgainButton });
        buttonBox.setAlignment(Pos.CENTER);
        playAgainButton.setVisible(false);
        playAgainButton.setOnAction(this::PlayAgainhandleButton);
        mainVBox.getChildren().add(buttonBox);
        updateTextDisplays();
       
        
        
        final Scene scene = new Scene(mainVBox, 550.0, 500.0);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    
    private void gameStart() {//initialized the program
    	chooseWord();
    	numWrongGuesses=0;
        guessedCharactersList.clear();
        isWordPositionGuessed = new boolean[currentWord.length()];
        guessField.setDisable(false);
        guessField.clear();
        outcomeText.setText("");
        updateTextDisplays();
        playAgainButton.setVisible(false);
	
    
	}
    
   
    
    private void handleGuessField( ActionEvent event) {// field to put the character
         String userInput = guessField.getText();	
        guessField.clear();
       checkCharacter(userInput);
       checkAnswer();
        
  }
       
   
  
     private boolean wordIsGuessed() { //check the loop and see if word is guessed
        	
        	 for (int c = isWordPositionGuessed.length, i = 0; i < c; ++i) {
             	
                 if (!isWordPositionGuessed[i]) {
              	   return false;
      			}   
                 
      		}
        	 return true;
         }
     private void checkAnswer() {// check if the answer is right then return text
  	   if (wordIsGuessed()) {
      	   this.outcomeText.setText("Great Job, You Won");
             this.guessField.setDisable(true);
             this.playAgainButton.setVisible(true);
  	}else if (numWrongGuesses == MAX_WRONG_GUESSES) {
  		this.outcomeText.setText("Better Luck Next Time!" +"\n The word Is " + currentWord);
          this.guessField.setDisable(true);
          this.playAgainButton.setVisible(true);
  	}
  }
    
    private void updateTextDisplays() {// Update all the text javafx
        displayStringSetup();
        wrongGuessNumberText.setText("Number of Guesses Remaining = " + (MAX_WRONG_GUESSES - numWrongGuesses));
        Collections.sort(guessedCharactersList);
        wrongGuessesText.setText("Wrong Guesses: " + guessedCharactersList);
    }
    
   
    
    private void displayStringSetup() {//Display if its true it display the :letter or Character), when false it display "-".
        String displayString = "";
        for (int i = 0; i < isWordPositionGuessed.length; ++i) {
            if (isWordPositionGuessed[i]) {
                displayString = String.valueOf(displayString) + currentWord.charAt(i);
            }
            else {
                displayString = String.valueOf(displayString) + "-";
            }
        }
        currentWordText.setText(displayString);
    }
    
    
    private void chooseWord()  {//choose a random word from the file then 
    	Scanner s;
		try {
			s = new Scanner(new File("words.txt"));
			while (s.hasNext()){
			    wordList.add(s.next());
			}
			s.close();

	        Collections.shuffle(wordList, new Random());
	        System.out.print(wordList.get(0));//Checker for the console.
	        currentWord = wordList.get(0).toUpperCase();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    private void checkCharacter(String userInput) {
    	 if (isGuessValid(userInput)) {
             char userGuess = userInput.toUpperCase().charAt(0);// user input must be character only
            if ( (currentWord.contains(Character.toString(userGuess)) && isWordPositionGuessed[currentWord.indexOf(userGuess)]) || guessedCharactersList.contains(userGuess) ) {
                outcomeText.setText("You've already guessed that letter.");
                
            } else if (currentWord.contains(Character.toString(userGuess))) {// if the the character is right then make it true
                
            	for (int i = 0; i < currentWord.length(); ++i) {
            		if (currentWord.charAt(i) == userGuess) {
                        isWordPositionGuessed[i] = true;
                    }
                }
                outcomeText.setText("Good guess!");
                } else {
                    guessedCharactersList.add(userGuess);
                    ++numWrongGuesses;
                    outcomeText.setText(" Try again.");
                }
                updateTextDisplays();
            }
    }
    
    
    
    private boolean isGuessValid( String userInput) {// Exception handler

        try {
            if (userInput.length() < 1) {
                throw new EmptyFieldException();
            }
            if (userInput.length() > 1) {
                throw new LongCharacterException();
            }
            if (Character.isAlphabetic(userInput.charAt(0))== false) {
                throw new SymbolUseException();
            }
            return true;
        }
        catch (EmptyFieldException | LongCharacterException | SymbolUseException ex) {
           
            outcomeText.setText(ex.getMessage());
            guessField.clear();
            updateTextDisplays();
            return false;
        }
    }

    
    
    private void PlayAgainhandleButton( ActionEvent event) { // for play again button. Extra Credit.
    	gameStart();
    }
    
    
    
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
