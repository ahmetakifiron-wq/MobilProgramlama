package com.example.dvizuygulamas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView chfText, usdText, jpyText, tryText, cadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chfText = findViewById(R.id.chfText);
        usdText = findViewById(R.id.usdText);
        jpyText = findViewById(R.id.jpyText);
        tryText = findViewById(R.id.tryText);
        cadText = findViewById(R.id.cadText);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRates(v);
            }
        });
    }

    public void getRates(View view) {
        DownloadData downloadData = new DownloadData();
        try {
            // Not: Fixer.io ücretsiz sürümde sadece base=EUR destekler ve HTTPS bazen sorun çıkarabilir.
            String url = "http://data.fixer.io/api/latest?access_key=c25c78265cc6accc45c61b97e15b800e";
            downloadData.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } catch (Exception e) {
                Log.e("DownloadError", "Hata: " + e.getMessage());
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                tryText.setText("Veri alınamadı! (Bağlantı Hatası)");
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(s);
                boolean success = jsonObject.getBoolean("success");

                if (success) {
                    JSONObject rates = jsonObject.getJSONObject("rates");

                    chfText.setText("CHF: " + rates.getString("CHF"));
                    usdText.setText("USD: " + rates.getString("USD"));
                    jpyText.setText("JPY: " + rates.getString("JPY"));
                    tryText.setText("TRY: " + rates.getString("TRY"));
                    cadText.setText("CAD: " + rates.getString("CAD"));
                } else {
                    // API hata mesajını göster
                    JSONObject error = jsonObject.getJSONObject("error");
                    String info = error.getString("info");
                    tryText.setText("API Hatası: " + info);
                }

            } catch (Exception e) {
                tryText.setText("JSON Ayrıştırma Hatası!");
                e.printStackTrace();
            }
        }
    }
}