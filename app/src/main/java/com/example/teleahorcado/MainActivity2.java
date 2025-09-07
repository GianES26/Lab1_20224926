package com.example.teleahorcado;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private TextView nameText;
    private String savedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nameText = findViewById(R.id.nameText);
        savedName = getIntent().getStringExtra("name");
        if (savedName != null) nameText.setText("Bienvenido " + savedName);

        if (savedInstanceState != null) {
            savedName = savedInstanceState.getString("savedName");
            if (savedName != null) nameText.setText("Bienvenido " + savedName);
        }

        findViewById(R.id.cybersecurityButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            intent.putExtra("theme", "cybersecurity");
            intent.putExtra("name", savedName);
            startActivity(intent);
        });
        findViewById(R.id.networksButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            intent.putExtra("theme", "networks");
            intent.putExtra("name", savedName);
            startActivity(intent);
        });
        findViewById(R.id.fiberOpticsButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            intent.putExtra("theme", "fiberOptics");
            intent.putExtra("name", savedName);
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedName", savedName);
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
        } else if (item.getItemId() == R.id.stats) {
            Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
            intent.putExtra("name", savedName);
            intent.putExtra("startTime", new java.text.SimpleDateFormat("dd/MM/yyyy – hh:mm a", java.util.Locale.getDefault()).format(new java.util.Date()));
            intent.putExtra("totalGames", 0); // No games yet
            intent.putStringArrayListExtra("gameHistory", new java.util.ArrayList<>());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}