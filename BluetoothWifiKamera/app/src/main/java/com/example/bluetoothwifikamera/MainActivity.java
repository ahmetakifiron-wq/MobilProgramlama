package com.example.bluetoothwifikamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonB, buttonW, buttonC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonB = findViewById(R.id.buttonB);
        buttonW = findViewById(R.id.buttonW);
        buttonC = findViewById(R.id.buttonC);

        // Bluetooth Sayfasına Geçiş
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisB = new Intent(MainActivity.this, com.example.bluetoothwifikamera.Bluetooth.class);
                startActivity(gecisB);
            }
        });

        // Wifi Sayfasına Geçiş
        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisW = new Intent(MainActivity.this, com.example.bluetoothwifikamera.Wifi.class);
                startActivity(gecisW);
            }
        });

        // Kamera Sayfasına Geçiş
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisC = new Intent(MainActivity.this, com.example.bluetoothwifikamera.Camera.class);
                startActivity(gecisC);
            }
        });
    }
}