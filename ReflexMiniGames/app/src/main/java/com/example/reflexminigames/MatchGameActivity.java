package com.example.reflexminigames;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchGameActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView txtTimer;
    private int matrixSize, matchedPairs = 0, totalPairs;
    private List<Integer> colorList;
    private AppCompatButton firstSelectedCard = null;
    private int firstColor = -1;
    private boolean isProcessing = true;
    private DatabaseHelper db;
    private long gameStartTime;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private CountDownTimer countdownTimer;

    // Genişletilmiş Renk Paleti (Eşleşmeler için)
    private final int[] colors = {
            Color.parseColor("#E74C3C"), Color.parseColor("#3498DB"),
            Color.parseColor("#2ECC71"), Color.parseColor("#F1C40F"),
            Color.parseColor("#9B59B6"), Color.parseColor("#1ABC9C"),
            Color.parseColor("#E67E22"), Color.parseColor("#34495E"),
            Color.parseColor("#D35400"), Color.parseColor("#27AE60"),
            Color.parseColor("#2980B9"), Color.parseColor("#8E44AD")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_game);

        // Bileşenleri Bağla
        db = new DatabaseHelper(this);
        gridLayout = findViewById(R.id.matchGrid);
        txtTimer = findViewById(R.id.txtTimer);

        // Çıkış Butonu Fonksiyonu
        findViewById(R.id.btnBackToMenu).setOnClickListener(v -> showExitDialog());

        // Menüden gelen boyutu al
        matrixSize = getIntent().getIntExtra("MATRIX_SIZE", 3);

        initializeGameData();
        startPreparationSequence();
    }

    private void initializeGameData() {
        int totalCells = matrixSize * matrixSize;
        totalPairs = totalCells / 2;
        colorList = new ArrayList<>();

        // Renk çiftlerini listeye ekle
        for (int i = 0; i < totalPairs; i++) {
            colorList.add(colors[i % colors.length]);
            colorList.add(colors[i % colors.length]);
        }

        // Listeyi karıştır
        Collections.shuffle(colorList);

        // Matris tek sayı ise tam ortayı "X" (oyun dışı) yap
        if (totalCells % 2 != 0) {
            colorList.add(totalCells / 2, -1);
        }
    }

    private void startPreparationSequence() {
        // Kartları RENKLİ olarak oluştur (Ezber aşaması)
        renderBoard(true);

        // 3 saniyelik geri sayım başlat
        countdownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText("EZBERLE: " + ((millisUntilFinished / 1000) + 1));
                txtTimer.setTextColor(Color.YELLOW);
            }

            @Override
            public void onFinish() {
                // Kartları KAPAT ve oyunu başlat
                renderBoard(false);
                txtTimer.setText("BAŞLA!");
                txtTimer.setTextColor(Color.GREEN);
                gameStartTime = System.currentTimeMillis();
                isProcessing = false;
            }
        }.start();
    }

    private void renderBoard(boolean showColors) {
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(matrixSize);
        gridLayout.setRowCount(matrixSize);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        // Buton boyutlarını ekran genişliğine göre dinamik hesapla
        int btnSize = (screenWidth - (matrixSize * 30)) / matrixSize;

        for (int i = 0; i < colorList.size(); i++) {
            final AppCompatButton btn = new AppCompatButton(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = btnSize;
            params.height = btnSize;
            params.setMargins(8, 8, 8, 8);
            btn.setLayoutParams(params);

            final int color = colorList.get(i);

            if (color == -1) {
                // Ortadaki kare
                btn.setBackgroundResource(R.drawable.ic_cross);
                btn.setEnabled(false);
            } else {
                if (showColors) {
                    btn.setBackgroundColor(color);
                } else {
                    btn.setBackgroundColor(Color.parseColor("#444444")); // Kapalı hali gri
                }

                btn.setOnClickListener(v -> handleCardSelection(btn, color));
            }
            gridLayout.addView(btn);
        }
    }

    private void handleCardSelection(AppCompatButton btn, int color) {
        if (isProcessing || btn == firstSelectedCard) return;

        // Kartı aç
        btn.setBackgroundColor(color);

        if (firstSelectedCard == null) {
            firstSelectedCard = btn;
            firstColor = color;
        } else {
            isProcessing = true;
            if (color == firstColor) {
                // EŞLEŞME BAŞARILI
                mainHandler.postDelayed(() -> {
                    btn.setVisibility(View.INVISIBLE); // Kartları yok et veya devre dışı bırak
                    firstSelectedCard.setVisibility(View.INVISIBLE);
                    matchedPairs++;
                    clearSelection();
                    if (matchedPairs == totalPairs) finalizeGame();
                }, 300);
            } else {
                // EŞLEŞME HATALI
                mainHandler.postDelayed(() -> {
                    btn.setBackgroundColor(Color.parseColor("#444444"));
                    if (firstSelectedCard != null) {
                        firstSelectedCard.setBackgroundColor(Color.parseColor("#444444"));
                    }
                    clearSelection();
                }, 500);
            }
        }
    }

    private void clearSelection() {
        firstSelectedCard = null;
        firstColor = -1;
        isProcessing = false;
    }

    private void finalizeGame() {
        long timeTaken = (System.currentTimeMillis() - gameStartTime) / 1000;

        // PUAN HESABI (10.000 üzerinden azalan sistem)
        // Her saniye 150 puan götürür
        int finalScore = Math.max(0, 10000 - (int) (timeTaken * 150));

        String gameId = "Match_" + matrixSize + "x" + matrixSize;
        int currentBest = db.getBestScore(gameId);

        // Veritabanına kaydet
        db.addOrUpdateScore(gameId, finalScore);

        // Yapay Zeka Yorumu
        String aiMessage;
        if (finalScore > 8500) aiMessage = "Muazzam bir hafıza! Sen bir dâhilsin.";
        else if (finalScore > 6000) aiMessage = "Harika iş çıkardın, reflekslerin çok iyi.";
        else aiMessage = "Biraz daha odaklanırsan rekoru kırabilirsin!";

        // Sonuç Ekranı
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("OYUN TAMAMLANDI");
        builder.setMessage("Senin Skorun: " + finalScore +
                "\nEn İyi Skor: " + Math.max(currentBest, finalScore) +
                "\nSüre: " + timeTaken + " sn" +
                "\n\n" + aiMessage);

        builder.setPositiveButton("YENİDEN DENE", (d, w) -> recreate());
        builder.setNegativeButton("MENÜYE DÖN", (d, w) -> finish());
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("ÇIKIŞ")
                .setMessage("Mevcut oyunun iptal edilecek. Menüye dönmek istiyor musun?")
                .setPositiveButton("EVET", (d, w) -> finish())
                .setNegativeButton("HAYIR", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownTimer != null) countdownTimer.cancel();
        mainHandler.removeCallbacksAndMessages(null);
    }
}