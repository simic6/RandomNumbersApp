package com.example.randomnumberapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GuessingGameActivity extends AppCompatActivity {

    private EditText guessEditText;
    private Button submitGuessButton, backButton;
    private TextView feedbackTextView, instructionsTextView;

    private int randomNumber;
    private int attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessing_game);

        // Initialize UI components
        guessEditText = findViewById(R.id.guessEditText);
        submitGuessButton = findViewById(R.id.submitGuessButton);
        backButton = findViewById(R.id.backButton); // Initialize backButton
        feedbackTextView = findViewById(R.id.feedbackTextView);
        instructionsTextView = findViewById(R.id.instructionsTextView);

        // Initialize game variables
        generateRandomNumber();

        // Set up button listeners
        submitGuessButton.setOnClickListener(v -> checkGuess());

        // Set up back button listener to return to the main activity
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuessingGameActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish GuessingGameActivity to remove it from the back stack
        });
    }

    // Generate a random number between 1 and 100 (inclusive)
    private void generateRandomNumber() {
        Random random = new Random();
        randomNumber = random.nextInt(100) + 1;  // Random number between 1 and 100
        attempts = 0;
    }

    // Check the user's guess
    private void checkGuess() {
        String userInput = guessEditText.getText().toString();

        // Validate input
        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter a guess", Toast.LENGTH_SHORT).show();
            return;
        }

        int userGuess = Integer.parseInt(userInput);
        attempts++;

        if (userGuess < randomNumber) {
            feedbackTextView.setText("Higher! Attempts: " + attempts);
        } else if (userGuess > randomNumber) {
            feedbackTextView.setText("Lower! Attempts: " + attempts);
        } else {
            feedbackTextView.setText("Correct! You guessed it in " + attempts + " attempts.");
            instructionsTextView.setText("Game Over! Start a new game.");
        }
    }
}
