package com.example.bluetoothwifikamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {
    private BluetoothAdapter bAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = findViewById(R.id.listView);
    }

    @SuppressLint("MissingPermission")
    public void on(View v) {
        if (bAdapter == null) {
            Toast.makeText(this, "Desteklenmiyor", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
    }

    @SuppressLint("MissingPermission")
    public void off(View v) {
        if (bAdapter != null) {
            bAdapter.disable();
            Toast.makeText(this, "Kapatıldı", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void list(View v) {
        pairedDevices = bAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
    }
}