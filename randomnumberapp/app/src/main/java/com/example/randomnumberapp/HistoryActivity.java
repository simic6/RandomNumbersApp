package com.example.randomnumberapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private TextView historyTextView, mostCommonTextView, sumTextView, avgTextView; // Added sum and avg TextViews
    private Button backButton, clearHistoryButton, sortAscendingButton, sortDescendingButton, sortOriginalOrderButton;
    private RandomNumberDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize UI components
        historyTextView = findViewById(R.id.historyTextView);
        mostCommonTextView = findViewById(R.id.mostCommonTextView);
        sumTextView = findViewById(R.id.sumTextView); // Sum TextView
        avgTextView = findViewById(R.id.avgTextView); // Average TextView
        backButton = findViewById(R.id.backButton);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        sortAscendingButton = findViewById(R.id.sortAscendingButton);
        sortDescendingButton = findViewById(R.id.sortDescendingButton);
        sortOriginalOrderButton = findViewById(R.id.sortOriginalOrderButton);

        // Initialize the database helper
        dbHelper = new RandomNumberDatabaseHelper(this);

        // Display history in ascending order by default
        displayHistory("ASC");
        showMostCommonNumber(); // Show most common number
        showSumAndAverage(); // Show sum and average

        // Set up Back button to return to the main activity
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish HistoryActivity to remove it from the back stack
        });

        // Clear history button: Clears all the data from the database
        clearHistoryButton.setOnClickListener(v -> clearHistory());

        // Sort Ascending button: Sort the history in ascending order
        sortAscendingButton.setOnClickListener(v -> {
            displayHistory("ASC");
            Toast.makeText(this, "Sorted in Ascending Order", Toast.LENGTH_SHORT).show();
            showMostCommonNumber(); // Update most common number after sorting
            showSumAndAverage(); // Update sum and average after sorting
        });

        // Sort Descending button: Sort the history in descending order
        sortDescendingButton.setOnClickListener(v -> {
            displayHistory("DESC");
            Toast.makeText(this, "Sorted in Descending Order", Toast.LENGTH_SHORT).show();
            showMostCommonNumber(); // Update most common number after sorting
            showSumAndAverage(); // Update sum and average after sorting
        });

        // Sort by Original Order button: Displays the history by the original insertion order
        sortOriginalOrderButton.setOnClickListener(v -> {
            displayHistory("original");
            Toast.makeText(this, "Sorted by Original Order", Toast.LENGTH_SHORT).show();
            showMostCommonNumber(); // Update most common number after sorting
            showSumAndAverage(); // Update sum and average after sorting
        });
    }

    // Method to display the history, sorting by the provided order
    private void displayHistory(String order) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        if (order.equals("original")) {
            cursor = db.rawQuery("SELECT * FROM " + RandomNumberDatabaseHelper.TABLE_NAME + " ORDER BY " + RandomNumberDatabaseHelper.COLUMN_ID + " ASC", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + RandomNumberDatabaseHelper.TABLE_NAME + " ORDER BY " + RandomNumberDatabaseHelper.COLUMN_NUMBER + " " + order, null);
        }

        StringBuilder history = new StringBuilder();
        while (cursor.moveToNext()) {
            int number = cursor.getInt(cursor.getColumnIndex(RandomNumberDatabaseHelper.COLUMN_NUMBER));
            history.append(number).append("\n");
        }
        cursor.close();

        historyTextView.setText(history.toString());
    }

    // Method to clear the history from the database
    private void clearHistory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.clearHistory(db);
        Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
        displayHistory("ASC"); // Refresh the history in ascending order after clearing
        mostCommonTextView.setText(""); // Clear most common number when history is cleared
        sumTextView.setText(""); // Clear sum when history is cleared
        avgTextView.setText(""); // Clear average when history is cleared
    }

    // Method to find and display the most common number in the history
    private void showMostCommonNumber() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + RandomNumberDatabaseHelper.COLUMN_NUMBER + " FROM " + RandomNumberDatabaseHelper.TABLE_NAME, null);

        Map<Integer, Integer> numberCountMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int number = cursor.getInt(cursor.getColumnIndex(RandomNumberDatabaseHelper.COLUMN_NUMBER));
            numberCountMap.put(number, numberCountMap.getOrDefault(number, 0) + 1);
        }
        cursor.close();

        int mostCommonNumber = -1;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : numberCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonNumber = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        if (mostCommonNumber != -1) {
            mostCommonTextView.setText("Most Common Number: " + mostCommonNumber + " (Appears " + maxCount + " times)");
        } else {
            mostCommonTextView.setText("No numbers found");
        }
    }

    // Method to calculate and display the sum and average of the numbers in history
    private void showSumAndAverage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + RandomNumberDatabaseHelper.COLUMN_NUMBER + " FROM " + RandomNumberDatabaseHelper.TABLE_NAME, null);

        int sum = 0;
        int count = 0;
        while (cursor.moveToNext()) {
            int number = cursor.getInt(cursor.getColumnIndex(RandomNumberDatabaseHelper.COLUMN_NUMBER));
            sum += number;
            count++;
        }
        cursor.close();

        // Display the sum and average
        if (count > 0) {
            double average = (double) sum / count;
            sumTextView.setText("Sum: " + sum);
            avgTextView.setText("Average: " + String.format("%.2f", average));
        } else {
            sumTextView.setText("Sum: 0");
            avgTextView.setText("Average: 0.00");
        }
    }
}
