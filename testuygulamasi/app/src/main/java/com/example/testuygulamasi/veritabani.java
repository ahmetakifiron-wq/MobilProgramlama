package com.example.testuygulamasi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class veritabani extends SQLiteOpenHelper {
    private static final String VERITABANI_ADI = "TestSistemi.db";
    private static final int SURUM = 1;

    public veritabani(Context c) {
        super(c, VERITABANI_ADI, null, SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // PDF'teki yapıya uygun olarak tabloyu oluşturuyoruz
        String sql = "CREATE TABLE IF NOT EXISTS Sorular (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "soru TEXT, " +
                "secA TEXT, " +
                "secB TEXT, " +
                "secC TEXT, " +
                "dogruCevap TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int eski, int yeni) {
        db.execSQL("DROP TABLE IF EXISTS Sorular");
        onCreate(db);
    }
}