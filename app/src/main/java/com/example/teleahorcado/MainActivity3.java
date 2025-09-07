package com.example.teleahorcado;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity3 extends AppCompatActivity {

    private TextView wordDisplay, resultText, jokerStatus;
    private ImageView headImage, torsoImage, rightArmImage, leftArmImage, leftLegImage, rightLegImage;
    private Button[] letterButtons;
    private Button jokerButton;
    private String[] cybersecurityWords = {"firewall", "malware", "phishing", "encryption", "hacker", "virus", "trojan", "spyware", "backup", "antivirus"};
    private String[] networksWords = {"proxy", "gateway", "mascara", "router", "switch", "bridge", "hub", "vlan", "firewall", "dns"};
    private String[] fiberOpticsWords = {"cable", "fiber", "signal", "attenuation", "splicing", "connector", "transceiver", "wavelength", "modem", "optical"};
    private String currentWord;
    private ArrayList<Integer> usedLetters;
    private int wrongGuesses;
    private long startTime;
    private boolean gameEnded;
    private int totalJokers;
    private int consecutiveCorrect;
    private boolean gameCompleted;
    private String playerName;
    private int totalGames;
    private ArrayList<String> gameHistory;
    private String startDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        wordDisplay = findViewById(R.id.wordDisplay);
        resultText = findViewById(R.id.resultText);
        jokerStatus = findViewById(R.id.jokerStatus);
        headImage = findViewById(R.id.headImage);
        torsoImage = findViewById(R.id.torsoImage);
        rightArmImage = findViewById(R.id.rightArmImage);
        leftArmImage = findViewById(R.id.leftArmImage);
        leftLegImage = findViewById(R.id.leftLegImage);
        rightLegImage = findViewById(R.id.rightLegImage);
        letterButtons = new Button[]{
                findViewById(R.id.buttonA), findViewById(R.id.buttonB), findViewById(R.id.buttonC),
                findViewById(R.id.buttonD), findViewById(R.id.buttonE), findViewById(R.id.buttonF),
                findViewById(R.id.buttonG), findViewById(R.id.buttonH), findViewById(R.id.buttonI),
                findViewById(R.id.buttonJ), findViewById(R.id.buttonK), findViewById(R.id.buttonL),
                findViewById(R.id.buttonM), findViewById(R.id.buttonN), findViewById(R.id.buttonO),
                findViewById(R.id.buttonP), findViewById(R.id.buttonQ), findViewById(R.id.buttonR),
                findViewById(R.id.buttonS), findViewById(R.id.buttonT), findViewById(R.id.buttonU),
                findViewById(R.id.buttonV), findViewById(R.id.buttonW), findViewById(R.id.buttonX),
                findViewById(R.id.buttonY), findViewById(R.id.buttonZ)
        };
        jokerButton = findViewById(R.id.jokerButton);

        playerName = getIntent().getStringExtra("name");
        String theme = getIntent().getStringExtra("theme");
        String[] words = networksWords; // Default
        if ("cybersecurity".equals(theme)) words = cybersecurityWords;
        else if ("networks".equals(theme)) words = networksWords;
        else if ("fiberOptics".equals(theme)) words = fiberOpticsWords;

        totalJokers = 0;
        consecutiveCorrect = 0;
        gameEnded = false;
        gameCompleted = false;
        totalGames = 0;
        gameHistory = new ArrayList<>();
        startDateTime = new SimpleDateFormat("dd/MM/yyyy – hh:mm a", Locale.getDefault()).format(new Date());
        startTime = System.currentTimeMillis();
        updateJokerStatus();
        startGame(words);
        setupLetterButtons();
        setupJokerButton();
        findViewById(R.id.newGameButton).setOnClickListener(v -> {
            if (!gameCompleted) {
                long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
                gameHistory.add("Juego " + totalGames + ": Canceló / Tiempo: " + timeTaken + "s");
            }
            resetGame();
        });
    }

    private void startGame(String[] words) {
        currentWord = words[new Random().nextInt(words.length)];
        usedLetters = new ArrayList<>();
        wrongGuesses = 0;
        gameEnded = false;
        gameCompleted = false;
        totalGames++;
        startTime = System.currentTimeMillis(); // Reset start time
        updateWordDisplay();
        hideHangmanParts();
        enableLetters();
        resultText.setText("");
    }

    private void updateWordDisplay() {
        StringBuilder display = new StringBuilder();
        for (char c : currentWord.toCharArray()) {
            display.append(usedLetters.contains((int)c) ? c : '_').append(' ');
        }
        wordDisplay.setText(display.toString());
        checkWin();
    }

    private void setupLetterButtons() {
        for (int i = 0; i < letterButtons.length; i++) {
            int letter = 'A' + i;
            letterButtons[i].setText(String.valueOf((char)letter));
            final int finalI = i;
            letterButtons[i].setOnClickListener(v -> {
                if (!gameEnded) {
                    guessLetter((char)('A' + finalI));
                }
            });
        }
    }

    private void setupJokerButton() {
        jokerButton.setOnClickListener(v -> {
            if (!gameEnded && totalJokers > 0) {
                useJoker();
                totalJokers--;
                updateJokerStatus();
            } else if (totalJokers == 0) {
                Toast.makeText(this, "No tienes comodines disponibles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guessLetter(char letter) {
        letter = Character.toLowerCase(letter);
        int buttonIndex = letter - 'a';
        letterButtons[buttonIndex].setEnabled(false);
        if (currentWord.contains(String.valueOf(letter))) {
            usedLetters.add((int)letter);
            consecutiveCorrect++;
            if (consecutiveCorrect == 4) {
                totalJokers++;
                consecutiveCorrect = 0;
                Toast.makeText(this, "¡Ganaste un comodín!", Toast.LENGTH_SHORT).show();
            }
            updateWordDisplay();
        } else {
            consecutiveCorrect = 0;
            wrongGuesses++;
            showHangmanPart();
            checkLoss();
        }
        updateJokerStatus();
    }

    private void useJoker() {
        ArrayList<Character> unguessedLetters = new ArrayList<>();
        for (char c : currentWord.toCharArray()) {
            if (!usedLetters.contains((int)c)) {
                unguessedLetters.add(c);
            }
        }
        if (!unguessedLetters.isEmpty()) {
            char revealed = unguessedLetters.get(new Random().nextInt(unguessedLetters.size()));
            usedLetters.add((int)revealed);
            updateWordDisplay();
        }
    }

    private void showHangmanPart() {
        switch (wrongGuesses) {
            case 1: headImage.setVisibility(View.VISIBLE); break;
            case 2: torsoImage.setVisibility(View.VISIBLE); break;
            case 3: rightArmImage.setVisibility(View.VISIBLE); break;
            case 4: leftArmImage.setVisibility(View.VISIBLE); break;
            case 5: leftLegImage.setVisibility(View.VISIBLE); break;
            case 6: rightLegImage.setVisibility(View.VISIBLE); break;
        }
    }

    private void hideHangmanParts() {
        headImage.setVisibility(View.GONE);
        torsoImage.setVisibility(View.GONE);
        rightArmImage.setVisibility(View.GONE);
        leftArmImage.setVisibility(View.GONE);
        leftLegImage.setVisibility(View.GONE);
        rightLegImage.setVisibility(View.GONE);
    }

    private void enableLetters() {
        for (Button button : letterButtons) button.setEnabled(true);
    }

    private void updateJokerStatus() {
        jokerStatus.setText(totalJokers + "/" + Math.min(consecutiveCorrect, 3));
    }

    private void checkWin() {
        if (!gameEnded && wordDisplay.getText().toString().replace(" ", "").equals(currentWord)) {
            long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
            resultText.setText("Ganó / Tiempo: " + timeTaken + "s");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            gameEnded = true;
            gameCompleted = true;
            long endTime = (System.currentTimeMillis() - startTime) / 1000;
            gameHistory.add("Juego " + totalGames + ": Ganó / Tiempo: " + endTime + "s");
            disableLetters();
        }
    }

    private void checkLoss() {
        if (!gameEnded && wrongGuesses == 6) {
            long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
            resultText.setText("Perdió / Tiempo: " + timeTaken + "s");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            gameEnded = true;
            gameCompleted = true;
            gameHistory.add("Juego " + totalGames + ": Perdió / Tiempo: " + timeTaken + "s");
            disableLetters();
        }
    }

    private void disableLetters() {
        for (Button button : letterButtons) {
            if (button.isEnabled()) {
                button.setEnabled(false);
            }
        }
    }

    private void resetGame() {
        gameEnded = false;
        gameCompleted = false;
        startTime = System.currentTimeMillis(); // Reset start time
        String theme = getIntent().getStringExtra("theme");
        String[] words = networksWords; // Default
        if ("cybersecurity".equals(theme)) words = cybersecurityWords;
        else if ("networks".equals(theme)) words = networksWords;
        else if ("fiberOptics".equals(theme)) words = fiberOpticsWords;
        startGame(words);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            if (!gameCompleted) {
                long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
                gameHistory.add("Juego " + totalGames + ": Canceló / Tiempo: " + timeTaken + "s");
            }
            finish();
            return true;
        } else if (item.getItemId() == R.id.stats) {
            Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
            intent.putExtra("name", playerName);
            intent.putExtra("startTime", startDateTime);
            intent.putExtra("totalGames", totalGames);
            ArrayList<String> statsHistory = new ArrayList<>(gameHistory);
            if (!gameCompleted && !gameEnded) {
                long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
                statsHistory.add("Juego " + totalGames + ": En Curso / Tiempo: " + timeTaken + "s");
            }
            intent.putStringArrayListExtra("gameHistory", statsHistory);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}