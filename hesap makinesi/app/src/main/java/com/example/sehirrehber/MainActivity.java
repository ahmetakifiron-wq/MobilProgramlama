package com.example.sehirrehber;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Global Değişkenler (C'deki struct gibi hepsini burada tutuyoruz)
    LinearLayout layoutSecim, layoutDetay;
    Button btnBasla;
    ImageView imgRastgele, dImg1, dImg2, dImg3;
    TextView txtBaslik, txtAciklama;

    // Resim ID Dizisi (9 Adet - Drawable klasörüne attığın isimlerle aynı olmalı)
    int[] resimHavuzu = {
        R.drawable.ankara_1, R.drawable.ankara_2, R.drawable.ankara_3,
                R.drawable.istanbul_1, R.drawable.istanbul_2, R.drawable.istanbul_3,
                R.drawable.izmir_1, R.drawable.izmir_2, R.drawable.izmir_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Elemanları Tanımlama (Bağlantı Kurma)
        btnBasla = findViewById(R.id.btnBasla);
        layoutSecim = findViewById(R.id.layoutSecim);
        layoutDetay = findViewById(R.id.layoutDetay);
        imgRastgele = findViewById(R.id.imgRastgele);
        dImg1 = findViewById(R.id.detayImg1);
        dImg2 = findViewById(R.id.detayImg2);
        dImg3 = findViewById(R.id.detayImg3);
        txtBaslik = findViewById(R.id.txtBaslik);
        txtAciklama = findViewById(R.id.txtAciklama);

        // 1. ADIM: BAŞLA BUTONU
        btnBasla.setOnClickListener(v -> {
            btnBasla.setVisibility(View.GONE); // Başla butonunu gizle
            layoutSecim.setVisibility(View.VISIBLE); // Seçim ekranını aç

            // Rastgele resim seç
            int r = new Random().nextInt(9);
            imgRastgele.setImageResource(resimHavuzu[r]);
        });

        // 2. ADIM: ŞEHİR BUTONLARI
        findViewById(R.id.btnAnkara).setOnClickListener(v -> detayGoster("Ankara"));
        findViewById(R.id.btnIstanbul).setOnClickListener(v -> detayGoster("Istanbul"));
        findViewById(R.id.btnIzmir).setOnClickListener(v -> detayGoster("Izmir"));

        // 3. ADIM: GERİ BUTONU
        findViewById(R.id.btnGeri).setOnClickListener(v -> {
            layoutDetay.setVisibility(View.GONE);
            layoutSecim.setVisibility(View.VISIBLE);
        });
    }

    void detayGoster(String sehir) {
        layoutSecim.setVisibility(View.GONE);
        layoutDetay.setVisibility(View.VISIBLE);
        txtBaslik.setText(sehir);

        if (sehir.equals("Ankara")) {
            dImg1.setImageResource(R.drawable.ankara_1);
            dImg2.setImageResource(R.drawable.ankara_2);
            dImg3.setImageResource(R.drawable.ankara_3);
            txtAciklama.setText("Ankara Türkiye'nin Kalbidir.");
        } else if (sehir.equals("Istanbul")) {
            dImg1.setImageResource(R.drawable.istanbul_1);
            dImg2.setImageResource(R.drawable.istanbul_2);
            dImg3.setImageResource(R.drawable.istanbul_3);
            txtAciklama.setText("İstanbul İki Kıtayı Bağlar.");
        } else {
            dImg1.setImageResource(R.drawable.izmir_1);
            dImg2.setImageResource(R.drawable.izmir_2);
            dImg3.setImageResource(R.drawable.izmir_3);
            txtAciklama.setText("İzmir Ege'nin İncisidir.");
        }
    }
}