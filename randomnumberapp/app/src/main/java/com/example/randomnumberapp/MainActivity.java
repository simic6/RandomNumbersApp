package com.example.randomnumberapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView numberTextView;
    private EditText minRangeEditText, maxRangeEditText;
    private Button generateButton, viewHistoryButton, guessingGameButton;
    private RandomNumberDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        numberTextView = findViewById(R.id.numberTextView);
        minRangeEditText = findViewById(R.id.minRangeEditText);
        maxRangeEditText = findViewById(R.id.maxRangeEditText);
        generateButton = findViewById(R.id.generateButton);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);
        guessingGameButton = findViewById(R.id.guessingGameButton);

        // Initialize the database helper
        dbHelper = new RandomNumberDatabaseHelper(this);

        // Set up button listeners
        generateButton.setOnClickListener(v -> generateRandomNumber());
        viewHistoryButton.setOnClickListener(v -> openHistoryActivity());
        guessingGameButton.setOnClickListener(v -> openGuessingGameActivity());
    }

    // Generate a random number within the specified range and display it
    private void generateRandomNumber() {
        // Get min and max range values from the EditTexts
        String minRangeText = minRangeEditText.getText().toString();
        String maxRangeText = maxRangeEditText.getText().toString();

        // Validate the input fields
        if (minRangeText.isEmpty() || maxRangeText.isEmpty()) {
            Toast.makeText(this, "Please enter both min and max range", Toast.LENGTH_SHORT).show();
            return;
        }

        int minRange = Integer.parseInt(minRangeText);
        int maxRange = Integer.parseInt(maxRangeText);

        // Ensure min range is less than max range
        if (minRange >= maxRange) {
            Toast.makeText(this, "Min range must be less than max range", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate the random number
        int randomNumber = (int) (Math.random() * (maxRange - minRange + 1)) + minRange;

        // Display the random number in the TextView
        numberTextView.setText("Random Number: " + randomNumber);

        // Save the generated random number to the database
        saveNumberToDatabase(randomNumber);
    }

    // Save the generated random number to the database
    private void saveNumberToDatabase(int number) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RandomNumberDatabaseHelper.COLUMN_NUMBER, number);
        db.insert(RandomNumberDatabaseHelper.TABLE_NAME, null, values);
    }

    // Open the HistoryActivity to view the generated numbers
    private void openHistoryActivity() {
        // Start the HistoryActivity
        startActivity(new Intent(this, HistoryActivity.class));
    }

    // Open the GuessingGameActivity to play the guessing game
    private void openGuessingGameActivity() {
        // Start the GuessingGameActivity
        startActivity(new Intent(this, GuessingGameActivity.class));
    }
}
