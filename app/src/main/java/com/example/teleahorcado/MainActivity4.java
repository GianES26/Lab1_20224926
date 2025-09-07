package com.example.teleahorcado;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        TextView playerNameText = findViewById(R.id.playerNameText);
        TextView startTimeText = findViewById(R.id.startTimeText);
        TextView totalGamesText = findViewById(R.id.totalGamesText);
        TextView gamesList = findViewById(R.id.gamesList);

        String playerName = getIntent().getStringExtra("name");
        String startTime = getIntent().getStringExtra("startTime");
        int totalGames = getIntent().getIntExtra("totalGames", 0);
        ArrayList<String> gameHistory = getIntent().getStringArrayListExtra("gameHistory");

        playerNameText.setText("Jugador: " + playerName);
        startTimeText.setText("Inicio: " + startTime);
        totalGamesText.setText("Cantidad de partidas: " + totalGames);

        StringBuilder gamesText = new StringBuilder("Partidas:\n");
        if (gameHistory != null) {
            for (String game : gameHistory) {
                if (game.contains("Ganó")) {
                    gamesText.append("<font color='#00FF00'>").append(game).append("</font><br>");
                } else if (game.contains("Perdió")) {
                    gamesText.append("<font color='#FF0000'>").append(game).append("</font><br>");
                } else if (game.contains("Canceló")) {
                    gamesText.append("<font color='#FFFF00'>").append(game).append("</font><br>");
                } else {
                    gamesText.append("<font color='#000000'>").append(game).append("</font><br>");
                }
            }
        }
        gamesList.setText(android.text.Html.fromHtml(gamesText.toString()));
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