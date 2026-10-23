import java.util.Locale;
import java.util.Scanner;

public class Main {

    //  KONSTANTA & DATA GLOBAL 
    static final int MAKS_MENU = 50;
    static final int MAKS_PESANAN = 50;
    static final int BIAYA_LAYANAN = 20000;      
    static final int BATAS_DISKON = 100000;     
    static final int BATAS_PROMO = 50000;        
    static final String[] KATEGORI = {"Makanan", "Minuman"};

    static Scanner input = new Scanner(System.in);
    static Menu[] daftarMenu = new Menu[MAKS_MENU];   
    static int jumlahMenu = 0;                        

    // METHOD UTAMA 
    public static void main(String[] args) {
        isiMenuAwal();

        int pilihan;
        do {
            System.out.println("\n==============================");
            System.out.println("      APLIKASI RESTORAN");
            System.out.println("==============================");
            System.out.println("1. Menu Pelanggan (Pesan)");
            System.out.println("2. Menu Pemilik (Kelola Menu)");
            System.out.println("0. Keluar");
            pilihan = bacaAngka("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    prosesPemesanan();
                    break;
                case 2:
                    kelolaMenu();
                    break;
                case 0:
                    System.out.println("Terima kasih. Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia, silakan ulangi.");
            }
        } while (pilihan != 0);
    }

    //  DATA AWAL 
    static void isiMenuAwal() {
        tambahKeArray(new Menu("Nasi Goreng", 25000, "Makanan"));
        tambahKeArray(new Menu("Ayam Bakar", 30000, "Makanan"));
        tambahKeArray(new Menu("Mie Goreng", 22000, "Makanan"));
        tambahKeArray(new Menu("Sate Ayam", 28000, "Makanan"));
        tambahKeArray(new Menu("Es Teh", 8000, "Minuman"));
        tambahKeArray(new Menu("Es Jeruk", 10000, "Minuman"));
        tambahKeArray(new Menu("Kopi Hitam", 12000, "Minuman"));
        tambahKeArray(new Menu("Jus Alpukat", 15000, "Minuman"));
    }

    // menambah menu ke array, lalu dirapihin sesuai urutan 
    static void tambahKeArray(Menu m) {
        daftarMenu[jumlahMenu] = m;
        jumlahMenu++;
        urutkanMenu();
    }

    // bubble sort sederhana berdasarkan kategori 
    static void urutkanMenu() {
        for (int i = 0; i < jumlahMenu - 1; i++) {
            for (int j = 0; j < jumlahMenu - 1 - i; j++) {
                if (daftarMenu[j].getKategori().equals("Minuman")
                        && daftarMenu[j + 1].getKategori().equals("Makanan")) {
                    Menu temp = daftarMenu[j];
                    daftarMenu[j] = daftarMenu[j + 1];
                    daftarMenu[j + 1] = temp;
                }
            }
        }
    }

    //  TAMPILKAN MENU (dikelompokkan per kategori)
    static void tampilkanDaftarMenu() {
        System.out.println("\n========== DAFTAR MENU ==========");
        for (String kat : KATEGORI) {                       
            System.out.println("--- " + kat.toUpperCase() + " ---");
            boolean ada = false;
            for (int i = 0; i < jumlahMenu; i++) {          
                if (daftarMenu[i].getKategori().equals(kat)) {
                    System.out.printf("%2d. %-20s %12s%n",
                            i + 1, daftarMenu[i].getNama(), rupiah(daftarMenu[i].getHarga()));
                    ada = true;
                }
            }
            if (!ada) {
                System.out.println("    (belum ada menu)");
            }
        }
        System.out.println("=================================");
    }

    // PEMESANAN 
    static void prosesPemesanan() {
        Menu[] pesanan = new Menu[MAKS_PESANAN];   
        int[] jumlah = new int[MAKS_PESANAN];      
        int n = 0;

        tampilkanDaftarMenu();
        System.out.println("Masukkan nomor menu yang dipesan. Ketik 'selesai' untuk mengakhiri.");

        while (true) {
            System.out.print("\nNomor menu / 'selesai': ");
            String teks = input.nextLine().trim();

            if (teks.equalsIgnoreCase("selesai")) {
                break;
            }

            // divalidasi disini , jadi harus sesuai angka sama sesuai nomor menunya. kalo ga valid diminta input lagi 
            if (!isAngka(teks)) {
                System.out.println("Input tidak valid! Masukkan nomor menu atau 'selesai'.");
                continue;
            }
            int no = Integer.parseInt(teks);
            if (no < 1 || no > jumlahMenu) {
                System.out.println("Nomor menu tidak ada dalam daftar! Silakan ulangi.");
                continue;
            }

            Menu dipilih = daftarMenu[no - 1];
            int qty;
            do {
                qty = bacaAngka("Jumlah " + dipilih.getNama() + ": ");
                if (qty < 1) {
                    System.out.println("Jumlah minimal 1.");
                }
            } while (qty < 1);

            // kalo menu udah pernah dipesan, jumlahnya ditambahin
            boolean sudahAda = false;
            for (int i = 0; i < n; i++) {
                if (pesanan[i] == dipilih) {
                    jumlah[i] += qty;
                    sudahAda = true;
                    break;
                }
            }
            if (!sudahAda) {
                if (n < MAKS_PESANAN) {
                    pesanan[n] = dipilih;
                    jumlah[n] = qty;
                    n++;
                } else {
                    System.out.println("Pesanan sudah penuh, ketik 'selesai'.");
                    continue;
                }
            }
            System.out.println("-> " + qty + "x " + dipilih.getNama() + " ditambahkan ke pesanan.");
        }

        if (n == 0) {
            System.out.println("Belum ada menu yang dipesan.");
            return;
        }
        hitungDanCetakStruk(pesanan, jumlah, n);
    }

    // HITUNG TOTAL & CETAK STRUK 
    static void hitungDanCetakStruk(Menu[] pesanan, int[] jumlah, int n) {
        int subtotal = 0;
        for (int i = 0; i < n; i++) {
            subtotal += pesanan[i].getHarga() * jumlah[i];
        }

        int pajak = subtotal * 10 / 100;
        int layanan = BIAYA_LAYANAN;

        int diskon = 0;
        boolean dapatDiskon = false;
        if (subtotal > BATAS_DISKON) {
            diskon = subtotal * 10 / 100;
            dapatDiskon = true;
        }

        int nilaiPromo = 0;
        String namaPromo = "";
        boolean dapatPromo = false;
        if (subtotal > BATAS_PROMO) {
            int idxMinuman = -1;
            for (int i = 0; i < n; i++) {
                if (pesanan[i].getKategori().equals("Minuman")) {
                    if (idxMinuman == -1 || pesanan[i].getHarga() < pesanan[idxMinuman].getHarga()) {
                        idxMinuman = i;
                    }
                }
            }
            if (idxMinuman != -1) {
                nilaiPromo = pesanan[idxMinuman].getHarga();
                namaPromo = pesanan[idxMinuman].getNama();
                dapatPromo = true;
            }
        }

        // total akhir
        int totalSebelumPotongan = subtotal + pajak + layanan;
        int totalAkhir = totalSebelumPotongan - diskon - nilaiPromo;

        // CETAK STRUK 
        String garis = "=".repeat(58);
        String garisTipis = "-".repeat(58);
        System.out.println("\n" + garis);
        System.out.println("                    STRUK PEMESANAN");
        System.out.println(garis);
        System.out.printf("%-3s %-20s %4s %13s %13s%n", "No", "Menu", "Qty", "Harga", "Total");
        System.out.println(garisTipis);
        for (int i = 0; i < n; i++) {
            int totalItem = pesanan[i].getHarga() * jumlah[i];
            System.out.printf("%-3d %-20s %4d %13s %13s%n", i + 1, pesanan[i].getNama(),
                    jumlah[i], rupiah(pesanan[i].getHarga()), rupiah(totalItem));
        }
        if (dapatPromo) {
            System.out.printf("    %-20s %4s %13s %13s%n", "+ " + namaPromo, "1", "GRATIS", rupiah(0));
        }
        System.out.println(garisTipis);
        System.out.printf("%-36s %20s%n", "Total Biaya Pesanan", rupiah(subtotal));
        System.out.printf("%-36s %20s%n", "Pajak 10%", rupiah(pajak));
        System.out.printf("%-36s %20s%n", "Biaya Pelayanan", rupiah(layanan));
        System.out.printf("%-36s %20s%n", "Total Sebelum Diskon/Penawaran", rupiah(totalSebelumPotongan));
        System.out.println(garisTipis);

        if (dapatDiskon) {
            System.out.printf("%-36s %20s%n", "Diskon 10% (belanja > Rp 100.000)", "-" + rupiah(diskon));
        } else {
            System.out.println("Diskon 10%: tidak berlaku (belanja belum > Rp 100.000)");
        }
        if (dapatPromo) {
            System.out.printf("%-36s %20s%n", "Promo Beli 1 Gratis 1 (" + namaPromo + ")", "-" + rupiah(nilaiPromo));
        } else if (subtotal > BATAS_PROMO) {
            System.out.println("Promo Beli 1 Gratis 1: tidak berlaku (tidak ada minuman di pesanan)");
        } else {
            System.out.println("Promo Beli 1 Gratis 1: tidak berlaku (belanja belum > Rp 50.000)");
        }

        System.out.println(garis);
        System.out.printf("%-36s %20s%n", "TOTAL BAYAR (setelah diskon/promo)", rupiah(totalAkhir));
        System.out.println(garis);
        System.out.println("          Terima kasih atas kunjungan Anda!");
    }

    //  MENU PEMILIK 
    static void kelolaMenu() {
        int pilihan;
        do {
            System.out.println("\n===== PENGELOLAAN MENU (PEMILIK) =====");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("0. Kembali ke Menu Utama");
            pilihan = bacaAngka("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    tambahMenuBaru();
                    break;
                case 2:
                    ubahHargaMenu();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 0:
                    System.out.println("Kembali ke menu utama...");
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia, silakan ulangi.");
            }
        } while (pilihan != 0);
    }

    // tambah menu baru
    static void tambahMenuBaru() {
        int banyak;
        do {
            banyak = bacaAngka("Berapa menu baru yang ingin ditambahkan? ");
            if (banyak < 1) {
                System.out.println("Minimal 1 menu.");
            }
        } while (banyak < 1);

        for (int i = 1; i <= banyak; i++) {
            if (jumlahMenu >= MAKS_MENU) {
                System.out.println("Kapasitas menu penuh!");
                break;
            }
            System.out.println("\nMenu baru ke-" + i);

            String nama;
            do {
                System.out.print("Nama menu : ");
                nama = input.nextLine().trim();
                if (nama.isEmpty()) {
                    System.out.println("Nama tidak boleh kosong.");
                }
            } while (nama.isEmpty());

            int harga;
            do {
                harga = bacaAngka("Harga     : ");
                if (harga < 1) {
                    System.out.println("Harga harus lebih dari 0.");
                }
            } while (harga < 1);

            int kat;
            do {
                kat = bacaAngka("Kategori (1 = Makanan, 2 = Minuman): ");
                if (kat != 1 && kat != 2) {
                    System.out.println("Pilih 1 atau 2.");
                }
            } while (kat != 1 && kat != 2);

            tambahKeArray(new Menu(nama, harga, KATEGORI[kat - 1]));
            System.out.println("-> Menu '" + nama + "' berhasil ditambahkan.");
        }
    }

    // ubah harga
    static void ubahHargaMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Belum ada menu.");
            return;
        }
        tampilkanDaftarMenu();
        int no = bacaNomorMenu("Nomor menu yang ingin diubah harganya: ");
        Menu m = daftarMenu[no - 1];

        int hargaBaru;
        do {
            hargaBaru = bacaAngka("Harga baru untuk " + m.getNama() + ": ");
            if (hargaBaru < 1) {
                System.out.println("Harga harus lebih dari 0.");
            }
        } while (hargaBaru < 1);

        System.out.println("Ubah harga " + m.getNama() + " dari " + rupiah(m.getHarga())
                + " menjadi " + rupiah(hargaBaru) + "?");
        System.out.print("Ketik 'Ya' untuk melanjutkan: ");
        String konfirmasi = input.nextLine().trim();

        if (konfirmasi.equalsIgnoreCase("Ya")) {
            m.setHarga(hargaBaru);
            System.out.println("-> Harga berhasil diubah.");
        } else {
            System.out.println("-> Perubahan dibatalkan, daftar menu tidak berubah.");
        }
    }

    // hapus menu
    static void hapusMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Belum ada menu.");
            return;
        }
        tampilkanDaftarMenu();
        int no = bacaNomorMenu("Nomor menu yang ingin dihapus: ");
        Menu m = daftarMenu[no - 1];

        System.out.println("Hapus menu '" + m.getNama() + "' (" + rupiah(m.getHarga()) + ")?");
        System.out.print("Ketik 'Ya' untuk melanjutkan: ");
        String konfirmasi = input.nextLine().trim();

        if (konfirmasi.equalsIgnoreCase("Ya")) {
            for (int i = no - 1; i < jumlahMenu - 1; i++) {
                daftarMenu[i] = daftarMenu[i + 1];
            }
            daftarMenu[jumlahMenu - 1] = null;
            jumlahMenu--;
            System.out.println("-> Menu berhasil dihapus.");
        } else {
            System.out.println("-> Penghapusan dibatalkan, daftar menu tidak berubah.");
        }
    }

    // METHOD BANTU 

    static boolean isAngka(String s) {
        if (s.isEmpty() || s.length() > 9) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static int bacaAngka(String pesan) {
        while (true) {
            System.out.print(pesan);
            String s = input.nextLine().trim();
            if (isAngka(s)) {
                return Integer.parseInt(s);
            }
            System.out.println("Input harus berupa angka!");
        }
    }

    static int bacaNomorMenu(String pesan) {
        int no;
        do {
            no = bacaAngka(pesan);
            if (no < 1 || no > jumlahMenu) {
                System.out.println("Nomor menu tidak ada dalam daftar! Silakan ulangi.");
            }
        } while (no < 1 || no > jumlahMenu);
        return no;
    }

    // format angka menjadi rupiah
    static String rupiah(int nilai) {
        return "Rp " + String.format(Locale.US, "%,d", nilai).replace(',', '.');
    }
}