package com.example.reflexminigames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView txtColorBest, txtButtonBest, txtMatch3, txtMatch4, txtMatch5;
    private LinearLayout layoutMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        db = new DatabaseHelper(this);
        txtColorBest = findViewById(R.id.bestColorScore);
        txtButtonBest = findViewById(R.id.bestButtonScore);
        txtMatch3 = findViewById(R.id.bestMatch3); // XML'de bu ID'leri kontrol et
        txtMatch4 = findViewById(R.id.bestMatch4);
        txtMatch5 = findViewById(R.id.bestMatch5);
        layoutMatrix = findViewById(R.id.layoutMatrixSelection);

        findViewById(R.id.cardColorGame).setOnClickListener(v -> startActivity(new Intent(this, ColorGameActivity.class)));
        findViewById(R.id.cardButtonGame).setOnClickListener(v -> startActivity(new Intent(this, ButtonGameActivity.class)));
        findViewById(R.id.cardMatchGame).setOnClickListener(v -> {
            layoutMatrix.setVisibility(layoutMatrix.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        findViewById(R.id.btn3x3).setOnClickListener(v -> startMatch(3));
        findViewById(R.id.btn4x4).setOnClickListener(v -> startMatch(4));
        findViewById(R.id.btn5x5).setOnClickListener(v -> startMatch(5));
    }

    // KRİTİK: Oyundan her geri dönüldüğünde skorları tazeler
    @Override
    protected void onResume() {
        super.onResume();
        txtColorBest.setText("👑 " + db.getBestScore("ColorGame"));
        txtButtonBest.setText("👑 " + db.getBestScore("ButtonGame"));
        txtMatch3.setText("3x3: " + db.getBestScore("Match_3x3"));
        txtMatch4.setText("4x4: " + db.getBestScore("Match_4x4"));
        txtMatch5.setText("5x5: " + db.getBestScore("Match_5x5"));
    }

    private void startMatch(int size) {
        Intent i = new Intent(this, MatchGameActivity.class);
        i.putExtra("MATRIX_SIZE", size);
        startActivity(i);
    }
}