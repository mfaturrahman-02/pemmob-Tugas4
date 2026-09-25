package com.example.tugas4;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nrp, nama;
    private DatabaseHelper openDb;
    private SQLiteDatabase dbku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = findViewById(R.id.nrp);
        nama = findViewById(R.id.nama);

        Button btnSimpan = findViewById(R.id.btnSimpan);
        Button btnCari = findViewById(R.id.btnCari);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnHapus = findViewById(R.id.btnHapus);

        openDb = new DatabaseHelper(this);
        dbku = openDb.getWritableDatabase();

        btnSimpan.setOnClickListener(v -> simpan());
        btnCari.setOnClickListener(v -> cari());
        btnUpdate.setOnClickListener(v -> update());
        btnHapus.setOnClickListener(v -> hapus());
    }

    @Override
    protected void onDestroy() {
        if (dbku != null && dbku.isOpen()) {
            dbku.close();
        }
        if (openDb != null) {
            openDb.close();
        }
        super.onDestroy();
    }

    private void simpan() {
        String nrpInput = nrp.getText().toString().trim();
        String namaInput = nama.getText().toString().trim();

        if (nrpInput.isEmpty() || namaInput.isEmpty()) {
            Toast.makeText(this, "NRP dan Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues data = new ContentValues();
        data.put("nrp", nrpInput);
        data.put("nama", namaInput);

        long result = dbku.insertWithOnConflict("mhs", null, data, SQLiteDatabase.CONFLICT_IGNORE);
        if (result == -1) {
            Toast.makeText(this, "Gagal Simpan: NRP sudah ada!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_LONG).show();
            clearInputs();
        }
    }

    private void cari() {
        String nrpInput = nrp.getText().toString().trim();

        if (nrpInput.isEmpty()) {
            Toast.makeText(this, "Masukkan NRP yang ingin dicari!", Toast.LENGTH_SHORT).show();
            return;
        }

        try (Cursor cur = dbku.rawQuery("SELECT nama FROM mhs WHERE nrp = ?", new String[]{nrpInput})) {
            if (cur.moveToFirst()) {
                int namaIndex = cur.getColumnIndex("nama");
                if (namaIndex != -1) {
                    nama.setText(cur.getString(namaIndex));
                    Toast.makeText(this, "Data Ditemukan", Toast.LENGTH_LONG).show();
                }
            } else {
                nama.setText("");
                Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void update() {
        String nrpInput = nrp.getText().toString().trim();
        String namaInput = nama.getText().toString().trim();

        if (nrpInput.isEmpty() || namaInput.isEmpty()) {
            Toast.makeText(this, "NRP dan Nama baru harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues data = new ContentValues();
        data.put("nama", namaInput);

        int rowsAffected = dbku.update("mhs", data, "nrp = ?", new String[]{nrpInput});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Data Terupdate", Toast.LENGTH_LONG).show();
            clearInputs();
        } else {
            Toast.makeText(this, "Gagal Update: NRP tidak ditemukan!", Toast.LENGTH_LONG).show();
        }
    }

    private void hapus() {
        String nrpInput = nrp.getText().toString().trim();

        if (nrpInput.isEmpty()) {
            Toast.makeText(this, "Masukkan NRP yang ingin dihapus!", Toast.LENGTH_SHORT).show();
            return;
        }

        int rowsAffected = dbku.delete("mhs", "nrp = ?", new String[]{nrpInput});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Data Terhapus", Toast.LENGTH_LONG).show();
            clearInputs();
        } else {
            Toast.makeText(this, "Gagal Hapus: NRP tidak ditemukan!", Toast.LENGTH_LONG).show();
        }
    }

    private void clearInputs() {
        nrp.setText("");
        nama.setText("");
    }
}