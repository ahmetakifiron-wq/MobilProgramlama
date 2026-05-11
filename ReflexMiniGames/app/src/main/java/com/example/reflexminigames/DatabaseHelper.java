package com.example.reflexminigames;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "ReflexMasterDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE scores (game_name TEXT PRIMARY KEY, best_score INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {}

    public void addOrUpdateScore(String game, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        int currentBest = getBestScore(game);
        // Tüm oyunlarda artık yüksek puan (10.000 üzerinden) iyi olduğu için:
        if (newScore > currentBest) {
            ContentValues cv = new ContentValues();
            cv.put("game_name", game);
            cv.put("best_score", newScore);
            db.replace("scores", null, cv);
        }
    }

    public int getBestScore(String game) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT best_score FROM scores WHERE game_name=?", new String[]{game});
        int res = 0;
        if (c.moveToFirst()) res = c.getInt(0);
        c.close();
        return res;
    }
}