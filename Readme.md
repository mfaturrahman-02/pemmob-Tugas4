# Tugas 4: Fungsionalitas, Kebenaran Skema & Query SQLite, Uji Persistensi Data

Sistem Manajemen Data Mahasiswa berbasis Android menggunakan database terstruktur **SQLite**. Aplikasi ini dibangun untuk memenuhi kriteria Tugas 4 Pemrograman Berbasis Bergerak.

---

## Kriteria Penilaian & Penjelasan Implementasi

### 1. Fungsionalitas (Operasi CRUD)
Aplikasi menyediakan 4 fungsi utama pengelolaan data mahasiswa:
* **Simpan (Create):** Menambahkan data mahasiswa baru (NRP dan Nama) ke dalam database SQLite.
* **Cari (Read):** Mencari Nama mahasiswa berdasarkan NRP yang diinputkan dan menampilkan hasilnya secara otomatis pada bidang teks Nama.
* **Update (Update):** Memperbarui Nama mahasiswa berdasarkan NRP yang dimasukkan.
* **Hapus (Delete):** Menghapus data mahasiswa dari database berdasarkan NRP.

---

### 2. Kebenaran Skema & Query SQLite
* **Skema Tabel (`mhs`):**
  ```sql
  CREATE TABLE IF NOT EXISTS mhs (
      nrp TEXT PRIMARY KEY,
      nama TEXT
  );
  ```
  * Kolom `nrp` ditetapkan sebagai **`PRIMARY KEY`** untuk menjamin keunikan identitas setiap mahasiswa dan mencegah duplikasi data.

* **Parameterized Query (Pencegahan SQL Injection):**
  * Seluruh query pencarian, pembaruan, dan penghapusan menggunakan *parameter binding* (`?`) untuk keamanan data dan menghindari error akibat karakter khusus.
  * Contoh:
    ```java
    dbku.rawQuery("SELECT nama FROM mhs WHERE nrp = ?", new String[]{nrpInput});
    ```

* **Manajemen Resource & Memory Leak:**
  * Penggunaan **`try-with-resources`** pada objek `Cursor` memastikan koneksi query selalu ditutup secara otomatis setelah digunakan.
  * Kelas `DatabaseHelper` ditutup dengan aman pada siklus hidup `onDestroy()` pada `MainActivity.java`.

---

### 3. Uji Persistensi Data (Data Persistence Test)
Data mahasiswa disimpan secara lokal dalam penyimpanan internal Android pada berkas database `/data/data/com.example.tugas4/databases/db_mahasiswa`.

**Langkah-Langkah Pengujian Persistensi:**
1. **Langkah 1:** Jalankan aplikasi, masukkan NRP (misal: `5025211001`) dan Nama (misal: `Budi Santoso`), lalu tekan tombol **Simpan**.
2. **Langkah 2:** Tutup aplikasi secara penuh (*Force Stop* atau hapus dari *Recent Apps*).
3. **Langkah 3:** Buka kembali aplikasi, masukkan NRP `5025211001`, lalu tekan tombol **Cari**.
4. **Hasil:** Nama `Budi Santoso` akan muncul kembali. Hal ini membuktikan bahwa data tersimpan secara **persisten** di SQLite meskipun aplikasi telah ditutup.

---

## Struktur File Utama Proyek

| File Path | Deskripsi |
| :--- | :--- |
| [`DatabaseHelper.java`](file:///D:/Semester%203/tugas4/app/src/main/java/com/example/tugas4/DatabaseHelper.java) | Kelas turunan `SQLiteOpenHelper` untuk pembuatan skema tabel `mhs`. |
| [`MainActivity.java`](file:///D:/Semester%203/tugas4/app/src/main/java/com/example/tugas4/MainActivity.java) | Logika utama pemrosesan tombol Simpan, Cari, Update, Hapus, serta penanganan database. |
| [`activity_main.xml`](file:///D:/Semester%203/tugas4/app/src/main/res/layout/activity_main.xml) | Tampilan antarmuka Form Input Mahasiswa. |
| [`AndroidManifest.xml`](file:///D:/Semester%203/tugas4/app/src/main/AndroidManifest.xml) | Berkas konfigurasi utama aplikasi Android. |

---
