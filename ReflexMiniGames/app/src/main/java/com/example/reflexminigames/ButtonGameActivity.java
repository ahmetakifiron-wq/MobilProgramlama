package com.example.reflexminigames;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class ButtonGameActivity extends AppCompatActivity {
    private Button[] buttons = new Button[9];
    private TextView txtStatus, txtTimer;
    private int activeIdx = -1, score = 0;
    private long startTime;
    private boolean gameRunning = false;
    private DatabaseHelper db;
    private CountDownTimer mainTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_game);

        db = new DatabaseHelper(this);
        txtStatus = findViewById(R.id.txtStatus);
        txtTimer = findViewById(R.id.txtTimer);
        GridLayout grid = findViewById(R.id.buttonGrid);

        // EKRAN HESABI: Butonların tam kare ve simetrik olması için
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int btnSize = (screenWidth - 200) / 3; // Boşlukları çıkarıp 3'e bölüyoruz

        for (int i = 0; i < 9; i++) {
            buttons[i] = new Button(this);
            buttons[i].setBackgroundColor(Color.parseColor("#333333")); // Koyu Gri

            // Çizimindeki gibi bağımsız durmaları için Margin (Dış Boşluk)
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = btnSize;
            params.height = btnSize; // Kare olması için genişlik = yükseklik
            params.setMargins(15, 15, 15, 15);
            params.rowSpec = GridLayout.spec(i / 3);
            params.columnSpec = GridLayout.spec(i % 3);

            buttons[i].setLayoutParams(params);

            final int idx = i;
            buttons[i].setOnClickListener(v -> {
                if (gameRunning && idx == activeIdx) {
                    long reactionTime = System.currentTimeMillis() - startTime;
                    score += Math.max(0, 1000 - (int)reactionTime);
                    spawnNext();
                }
            });
            grid.addView(buttons[i]);
        }

        findViewById(R.id.btnBackToMenu).setOnClickListener(v -> showExitConfirm());
        startPreCountdown();
    }

    private void startPreCountdown() {
        new CountDownTimer(3000, 1000) {
            public void onTick(long l) { txtStatus.setText(String.valueOf((l/1000)+1)); }
            public void onFinish() {
                gameRunning = true;
                txtStatus.setText("VUR!");
                start30SecGame();
                spawnNext();
            }
        }.start();
    }

    private void start30SecGame() {
        mainTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long l) { txtTimer.setText("Kalan: " + l/1000); }
            public void onFinish() { finishGame(); }
        }.start();
    }

    private void spawnNext() {
        if (activeIdx != -1) buttons[activeIdx].setBackgroundColor(Color.parseColor("#333333"));
        activeIdx = new Random().nextInt(9);
        buttons[activeIdx].setBackgroundColor(Color.parseColor("#3498DB")); // Mavi hedef
        startTime = System.currentTimeMillis();
    }

    private void finishGame() {
        gameRunning = false;
        db.addOrUpdateScore("ButtonGame", score);
        int best = db.getBestScore("ButtonGame");
        String ai = score >= best ? "İnanılmaz! Yeni bir rekor!" : "Hızlanmalısın!";

        new AlertDialog.Builder(this)
                .setTitle("BİTTİ")
                .setMessage("Skor: " + score + "\nEn İyi: " + best + "\n\n" + ai)
                .setPositiveButton("TEKRAR", (d,w) -> recreate())
                .setNegativeButton("MENÜ", (d,w) -> finish())
                .setCancelable(false).show();
    }

    private void showExitConfirm() {
        gameRunning = false;
        if(mainTimer != null) mainTimer.cancel();
        new AlertDialog.Builder(this).setTitle("ÇIKIŞ").setMessage("Dönmek istiyor musun?")
                .setPositiveButton("EVET", (d,w) -> finish())
                .setNegativeButton("HAYIR", (d,w) -> {
                    gameRunning = true;
                    start30SecGame();
                }).show();
    }
}

