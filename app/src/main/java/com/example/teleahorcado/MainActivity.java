package com.example.teleahorcado;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button playButton;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        playButton = findViewById(R.id.playButton);
        title = findViewById(R.id.title);
        registerForContextMenu(title);

        playButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("name", name);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Ingresa un nombre", Toast.LENGTH_SHORT).show();
            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                playButton.setEnabled(s.length() > 0);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_color, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.blue) {
            title.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
            return true;
        } else if (item.getItemId() == R.id.green) {
            title.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
            return true;
        } else if (item.getItemId() == R.id.red) {
            title.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            return true;
        }
        return super.onContextItemSelected(item);
    }
}