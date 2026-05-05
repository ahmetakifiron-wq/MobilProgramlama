package com.example.testuygulamasi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText etSoru, etA, etB, etC, etDogru;
    Button btnEkle, btnGuncelle, btnSil, btnBasla;
    TextView tvSoruGoster, tvSonuc;
    RadioGroup rgSecenekler;
    RadioButton rbA, rbB, rbC;

    veritabani v1;
    ArrayList<Question> soruListesi = new ArrayList<>();
    int mevcutSoruIndex = 0;
    int puan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        v1 = new veritabani(this);
        initViews();

        btnEkle.setOnClickListener(v -> soruEkle());
        btnSil.setOnClickListener(v -> soruSil());
        btnGuncelle.setOnClickListener(v -> soruGuncelle());

        btnBasla.setOnClickListener(v -> {
            mevcutSoruIndex = 0;
            puan = 0;
            soruListesi.clear();
            tvSonuc.setText("");
            testiYukle();
        });

        rgSecenekler.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                cevabiKontrolEt(checkedId);
            }
        });
    }

    private void initViews() {
        etSoru = findViewById(R.id.etSoru); etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB); etC = findViewById(R.id.etC);
        etDogru = findViewById(R.id.etDogru);
        btnEkle = findViewById(R.id.btnEkle); btnGuncelle = findViewById(R.id.btnGuncelle);
        btnSil = findViewById(R.id.btnSil); btnBasla = findViewById(R.id.btnBasla);
        tvSoruGoster = findViewById(R.id.tvSoruGoster); tvSonuc = findViewById(R.id.tvSonuc);
        rgSecenekler = findViewById(R.id.rgSecenekler);
        rbA = findViewById(R.id.rbA); rbB = findViewById(R.id.rbB); rbC = findViewById(R.id.rbC);
    }

    private void soruEkle() {
        SQLiteDatabase db = v1.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("soru", etSoru.getText().toString());
        cv.put("secA", etA.getText().toString());
        cv.put("secB", etB.getText().toString());
        cv.put("secC", etC.getText().toString());
        cv.put("dogruCevap", etDogru.getText().toString().toUpperCase());
        db.insert("Sorular", null, cv); // PDF'teki insert mantığı[cite: 1]
        Toast.makeText(this, "Soru Eklendi", Toast.LENGTH_SHORT).show();
    }

    private void soruSil() {
        SQLiteDatabase db = v1.getWritableDatabase();
        db.delete("Sorular", "soru=?", new String[]{etSoru.getText().toString()}); // PDF'teki delete mantığı[cite: 1]
        Toast.makeText(this, "Soru Silindi", Toast.LENGTH_SHORT).show();
    }

    private void soruGuncelle() {
        SQLiteDatabase db = v1.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("secA", etA.getText().toString());
        cv.put("secB", etB.getText().toString());
        cv.put("secC", etC.getText().toString());
        cv.put("dogruCevap", etDogru.getText().toString().toUpperCase());
        db.update("Sorular", cv, "soru=?", new String[]{etSoru.getText().toString()}); // PDF'teki update mantığı[cite: 1]
    }

    private void testiYukle() {
        SQLiteDatabase db = v1.getReadableDatabase();
        Cursor c = db.query("Sorular", null, null, null, null, null, "RANDOM()", "5");
        while (c.moveToNext()) {
            soruListesi.add(new Question(
                    c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)
            ));
        }
        c.close();
        if (soruListesi.size() > 0) soruGoster();
        else Toast.makeText(this, "Önce soru ekleyin!", Toast.LENGTH_SHORT).show();
    }

    private void soruGoster() {
        if (mevcutSoruIndex < soruListesi.size()) {
            Question q = soruListesi.get(mevcutSoruIndex);
            tvSoruGoster.setText((mevcutSoruIndex + 1) + ". " + q.soru);
            rbA.setText(q.a); rbB.setText(q.b); rbC.setText(q.c);
            rgSecenekler.clearCheck();
        } else {
            tvSoruGoster.setText("Test Bitti!");
            tvSonuc.setText("Toplam Puan: " + (puan * 20));
            rgSecenekler.setVisibility(RadioGroup.GONE);
        }
    }

    private void cevabiKontrolEt(int checkedId) {
        String secilen = "";
        if (checkedId == R.id.rbA) secilen = "A";
        else if (checkedId == R.id.rbB) secilen = "B";
        else if (checkedId == R.id.rbC) secilen = "C";

        if (secilen.equals(soruListesi.get(mevcutSoruIndex).dogru)) {
            puan++;
        }
        mevcutSoruIndex++;
        tvSoruGoster.postDelayed(this::soruGoster, 500); // Yarım saniye bekle ve geç
    }

    // Yardımcı Veri Modeli
    class Question {
        String soru, a, b, c, dogru;
        Question(String s, String a, String b, String c, String d) {
            this.soru = s; this.a = a; this.b = b; this.c = c; this.dogru = d;
        }
    }

}