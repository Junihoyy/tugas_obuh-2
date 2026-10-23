# Aplikasi Restoran Sederhana (Java)

Tugas Praktik 2 — program Java berbasis console untuk simulasi pemesanan di restoran, lengkap dengan perhitungan pajak, diskon, promo, struk, dan pengelolaan menu oleh pemilik.

## Fitur

**Menu Pelanggan**
- Menampilkan daftar menu yang dikelompokkan per kategori (Makanan dan Minuman)
- Memesan menu tanpa batas sampai mengetik `selesai`
- Input yang tidak valid (teks, nomor di luar daftar, jumlah < 1) akan diminta ulang
- Menghitung total biaya, pajak, biaya pelayanan, diskon, dan promo
- Mencetak struk pesanan ke layar

**Menu Pemilik**
- Tambah menu baru (bisa beberapa menu sekaligus)
- Ubah harga menu (dengan konfirmasi `Ya`)
- Hapus menu (dengan konfirmasi `Ya`)
- Bisa kembali ke menu sebelumnya

## Struktur File

| File | Keterangan |
|------|------------|
| `Menu.java` | Kelas `Menu` dengan atribut nama, harga, dan kategori |
| `Main.java` | Kelas utama: tampilan menu, pemesanan, perhitungan, struk, dan pengelolaan menu |

## Cara Menjalankan

Pastikan JDK 11 atau lebih baru sudah terpasang (`java -version`), lalu jalankan di terminal/PowerShell pada folder proyek:

```powershell
javac Main.java Menu.java
java Main
```

## Aturan Perhitungan

| Komponen | Aturan |
|----------|--------|
| Total biaya pesanan | Jumlah (harga × qty) semua item |
| Pajak | 10% dari total biaya pesanan |
| Biaya pelayanan | Rp 20.000 (tetap) |
| Diskon | 10% jika total biaya pesanan > Rp 100.000 |
| Promo | Beli 1 gratis 1 untuk minuman jika total biaya pesanan > Rp 50.000 (minuman termurah di pesanan digratiskan 1 unit) |

**Total bayar** = total biaya pesanan + pajak + biaya pelayanan − diskon − nilai promo

## Contoh Struk

```
==========================================================
                    STRUK PEMESANAN
==========================================================
No  Menu                  Qty         Harga         Total
----------------------------------------------------------
1   Nasi Goreng             2     Rp 25.000     Rp 50.000
2   Es Teh                  2      Rp 8.000     Rp 16.000
    + Es Teh                1        GRATIS          Rp 0
----------------------------------------------------------
Total Biaya Pesanan                             Rp 66.000
Pajak 10%                                        Rp 6.600
Biaya Pelayanan                                 Rp 20.000
Total Sebelum Diskon/Penawaran                  Rp 92.600
----------------------------------------------------------
Diskon 10%: tidak berlaku (belanja belum > Rp 100.000)
Promo Beli 1 Gratis 1 (Es Teh)                  -Rp 8.000
==========================================================
TOTAL BAYAR (setelah diskon/promo)              Rp 84.600
==========================================================
          Terima kasih atas kunjungan Anda!
```

## Konsep yang Digunakan

- Class, object, method, tipe data, variabel, dan identifier
- Array (`Menu[]` dan `int[]`) untuk menu dan pesanan
- Struktur keputusan: `if`, `if-else`, `else if`, nested `if`, `switch case`
- Struktur pengulangan: `for`, `for-each`, `while`, `do-while`
