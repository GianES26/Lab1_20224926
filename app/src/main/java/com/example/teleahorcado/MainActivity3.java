package com.example.teleahorcado;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity3 extends AppCompatActivity {

    private TextView wordDisplay;
    private ImageView headImage, torsoImage, rightArmImage, leftArmImage, leftLegImage, rightLegImage;
    private Button[] letterButtons;
    private String[] cybersecurityWords = {"firewall", "malware", "phishing", "encryption", "hacker", "virus", "trojan", "spyware", "backup", "antivirus"};
    private String[] networksWords = {"proxy", "gateway", "mascara", "router", "switch", "bridge", "hub", "vlan", "firewall", "dns"};
    private String[] fiberOpticsWords = {"cable", "fiber", "signal", "attenuation", "splicing", "connector", "transceiver", "wavelength", "modem", "optical"};
    private String currentWord;
    private ArrayList<Integer> usedLetters;
    private int wrongGuesses;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        wordDisplay = findViewById(R.id.wordDisplay);
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

        String theme = getIntent().getStringExtra("theme");
        String[] words = networksWords; // Default
        if ("cybersecurity".equals(theme)) words = cybersecurityWords;
        else if ("networks".equals(theme)) words = networksWords;
        else if ("fiberOptics".equals(theme)) words = fiberOpticsWords;

        startGame(words);
        setupLetterButtons();
        findViewById(R.id.newGameButton).setOnClickListener(v -> resetGame());
    }

    private void startGame(String[] words) {
        currentWord = words[new Random().nextInt(words.length)];
        usedLetters = new ArrayList<>();
        wrongGuesses = 0;
        startTime = System.currentTimeMillis();
        updateWordDisplay();
        hideHangmanParts();
        enableLetters();
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
            letterButtons[i].setOnClickListener(v -> guessLetter((char)('A' + finalI)));
        }
    }

    private void guessLetter(char letter) {
        letter = Character.toLowerCase(letter);
        int buttonIndex = letter - 'a';
        letterButtons[buttonIndex].setEnabled(false);
        if (currentWord.contains(String.valueOf(letter))) {
            usedLetters.add((int)letter);
            updateWordDisplay();
        } else {
            wrongGuesses++;
            showHangmanPart();
            checkLoss();
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

    private void checkWin() {
        if (wordDisplay.getText().toString().replace(" ", "").equals(currentWord)) {
            long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
            Toast.makeText(this, "¡Ganaste! Tiempo: " + timeTaken + "s", Toast.LENGTH_LONG).show();
        }
    }

    private void checkLoss() {
        if (wrongGuesses == 6) {
            Toast.makeText(this, "¡Perdiste! Palabra: " + currentWord, Toast.LENGTH_LONG).show();
        }
    }

    private void resetGame() {
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}