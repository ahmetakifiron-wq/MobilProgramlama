package com.example.reflexminigames;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class ColorGameActivity extends AppCompatActivity {
    private RelativeLayout layout;
    private TextView txtStatus, txtTimer;
    private long startTime;
    private boolean isClickable = false, gameRunning = false;
    private int totalScore = 0;
    private DatabaseHelper db;
    private CountDownTimer mainTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_game);
        db = new DatabaseHelper(this);
        layout = findViewById(R.id.colorGameLayout);
        txtStatus = findViewById(R.id.txtStatus);
        txtTimer = findViewById(R.id.txtTimer);

        findViewById(R.id.btnBackToMenu).setOnClickListener(v -> showExitDialog());

        // Tıklama olayını layout'a bağla
        layout.setOnClickListener(v -> handleTap());

        startInitialCountdown();
    }

    private void startInitialCountdown() {
        new CountDownTimer(3000, 1000) {
            public void onTick(long l) { txtStatus.setText(String.valueOf((l/1000)+1)); }
            public void onFinish() {
                gameRunning = true;
                start30SecondGame();
                triggerColorChange();
            }
        }.start();
    }

    private void start30SecondGame() {
        mainTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long l) { txtTimer.setText("Süre: " + l/1000); }
            public void onFinish() { finalizeGame(); }
        }.start();
    }

    private void triggerColorChange() {
        if (!gameRunning) return;
        isClickable = false;
        layout.setBackgroundColor(Color.parseColor("#121212")); // Koyu Gri/Siyah
        txtStatus.setText("BEKLE...");

        int delay = new Random().nextInt(2500) + 800;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!gameRunning) return;
            layout.setBackgroundColor(Color.GREEN);
            txtStatus.setText("DOKUN!");
            startTime = System.currentTimeMillis();
            isClickable = true;
        }, delay);
    }

    private void handleTap() {
        if (gameRunning && isClickable) {
            long reaction = System.currentTimeMillis() - startTime;
            totalScore += Math.max(0, 1000 - (int)reaction);
            triggerColorChange();
        } else if (gameRunning && !isClickable) {
            totalScore = Math.max(0, totalScore - 200); // Erken basma cezası
            txtStatus.setText("ERKEN!");
        }
    }

    private void finalizeGame() {
        gameRunning = false;
        db.addOrUpdateScore("ColorGame", totalScore);
        int best = db.getBestScore("ColorGame");

        String aiComment = totalScore >= best ? "İnanılmaz! Rekor kırdın!" : "Hızın fena değil ama daha iyisi mümkün.";

        new AlertDialog.Builder(this)
                .setTitle("SÜRE BİTTİ")
                .setMessage("Skorun: " + totalScore + "\nEn İyi: " + best + "\n\n" + aiComment)
                .setPositiveButton("TEKRAR", (d,w) -> recreate())
                .setNegativeButton("MENÜ", (d,w) -> finish())
                .setCancelable(false).show();
    }

    private void showExitDialog() {
        gameRunning = false; // Duraklat
        if(mainTimer != null) mainTimer.cancel();
        new AlertDialog.Builder(this).setTitle("ÇIKIŞ").setMessage("Menüye dönmek istiyor musun?")
                .setPositiveButton("EVET", (d,w) -> finish())
                .setNegativeButton("HAYIR", (d,w) -> { gameRunning = true; start30SecondGame(); triggerColorChange(); }).show();
    }
}