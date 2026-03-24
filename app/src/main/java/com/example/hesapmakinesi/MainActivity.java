package com.example.hesapmakinesi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText s1, s2;
    TextView sonuc;

    Button btnTopla, btnCikar, btnCarp, btnBol;
    Button btnKare, btnKarekok, btnYuzde, btnTers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // EditText ve TextView bağlantıları
        s1 = findViewById(R.id.edtSayi1);
        s2 = findViewById(R.id.edtSayi2);
        sonuc = findViewById(R.id.txtSonuc);

        // Buton bağlantıları
        btnTopla = findViewById(R.id.btnTopla);
        btnCikar = findViewById(R.id.btnCikar);
        btnCarp = findViewById(R.id.btnCarp);
        btnBol = findViewById(R.id.btnBol);
        btnKare = findViewById(R.id.btnKare);
        btnKarekok = findViewById(R.id.btnKarekok);
        btnYuzde = findViewById(R.id.btnYuzde);
        btnTers = findViewById(R.id.btnTers);

        // ➕ Toplama
        btnTopla.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());
            double b = Double.parseDouble(s2.getText().toString());
            sonuc.setText("Sonuç: " + (a + b));
        });

        // ➖ Çıkarma
        btnCikar.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());
            double b = Double.parseDouble(s2.getText().toString());
            sonuc.setText("Sonuç: " + (a - b));
        });

        // ✖ Çarpma
        btnCarp.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());
            double b = Double.parseDouble(s2.getText().toString());
            sonuc.setText("Sonuç: " + (a * b));
        });

        // ➗ Bölme
        btnBol.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());
            double b = Double.parseDouble(s2.getText().toString());

            if (b == 0) {
                sonuc.setText("Sıfıra bölünemez!");
            } else {
                sonuc.setText("Sonuç: " + (a / b));
            }
        });

        // x²
        btnKare.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());
            sonuc.setText("Sonuç: " + (a * a));
        });

        // √
        btnKarekok.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());

            if (a < 0) {
                sonuc.setText("Negatif sayının karekökü alınamaz!");
            } else {
                sonuc.setText("Sonuç: " + Math.sqrt(a));
            }
        });

        // %
        btnYuzde.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());
            sonuc.setText("Sonuç: " + (a / 100));
        });

        // 1/x
        btnTers.setOnClickListener(v -> {
            double a = Double.parseDouble(s1.getText().toString());

            if (a == 0) {
                sonuc.setText("Sıfırın tersi yok!");
            } else {
                sonuc.setText("Sonuç: " + (1 / a));
            }
        });
    }
}