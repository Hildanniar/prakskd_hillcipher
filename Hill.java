package Hill;

import java.util.ArrayList;  
import java.util.Scanner;  
public class Hill {  
    //method untuk menerima matriks kunci 
    private static int[][] getKeyMatrix() {  
        Scanner sc = new Scanner(System.in);  
        System.out.println("Enter key matrix:");  
        String key = sc.nextLine();  
        //int len = key.length();  
        double sq = Math.sqrt(key.length());  
        if (sq != (long) sq) {  
            System.out.println("Cannot Form a square matrix");  
        }  
        int len = (int) sq;  
        int[][] keyMatrix = new int[len][len];  
        int k = 0;  
        for (int i = 0; i < len; i++)  
        {  
            for (int j = 0; j < len; j++)  
            {  
                keyMatrix[i][j] = ((int) key.charAt(k)) - 97;  
                k++;  
            }  
        }  
        return keyMatrix;  
    }  
    // method di bawah ini memeriksa apakah matriks kunci valid (det=0)  
    private static void isValidMatrix(int[][] keyMatrix) {  
        int det = keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0];  
        // If det=0, lempar pengecualian dan akhiri  
        if(det == 0) {  
            throw new java.lang.Error("Det equals to zero, invalid key matrix!");  
        }  
    }  
    // method ini memeriksa apakah matriks kunci terbalik valid (matriks mod26 = (1,0,0,1)
        private static void isValidReverseMatrix(int[][] keyMatrix, int[][] reverseMatrix) {  
        int[][] product = new int[2][2];  
        // Temukan matriks produk dari matriks kunci dikalikan matriks kunci terbalik 
        product[0][0] = (keyMatrix[0][0]*reverseMatrix[0][0] + keyMatrix[0][1] * reverseMatrix[1][0]) % 26;  
        product[0][1] = (keyMatrix[0][0]*reverseMatrix[0][1] + keyMatrix[0][1] * reverseMatrix[1][1]) % 26;  
        product[1][0] = (keyMatrix[1][0]*reverseMatrix[0][0] + keyMatrix[1][1] * reverseMatrix[1][0]) % 26;  
        product[1][1] = (keyMatrix[1][0]*reverseMatrix[0][1] + keyMatrix[1][1] * reverseMatrix[1][1]) % 26;  
        // Cek if a=1 and b=0 and c=0 and d=1  
        // Jika tidak, lempar pengecualian dan akhiri 
        if(product[0][0] != 1 || product[0][1] != 0 || product[1][0] != 0 || product[1][1] != 1) {  
            throw new java.lang.Error("Invalid reverse matrix found!");  
        }  
    }  
    // method ini menghitung matriks kunci terbalik  
    private static int[][] reverseMatrix(int[][] keyMatrix) {  
        int detmod26 = (keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0]) % 26; // Calc det  
        int factor;  
        int[][] reverseMatrix = new int[2][2];  
        // Carilah faktor yang benar bahwa  
        // factor*det = 1 mod 26  
        for(factor=1; factor < 26; factor++)  
        {  
            if((detmod26 * factor) % 26 == 1)  
            {  
                break;  
            }  
        }  
        // Hitung elemen matriks kunci terbalik menggunakan faktor yang ditemukan  
        reverseMatrix[0][0] = keyMatrix[1][1]           * factor % 26;  
        reverseMatrix[0][1] = (26 - keyMatrix[0][1])    * factor % 26;  
        reverseMatrix[1][0] = (26 - keyMatrix[1][0])    * factor % 26;  
        reverseMatrix[1][1] = keyMatrix[0][0]           * factor % 26;  
        return reverseMatrix;  
    }  
    // Metode ini menampilkan hasil enkripsi/dekripsi 
    private static void echoResult(String label, int adder, ArrayList<Integer> phrase) {  
        int i;  
        System.out.print(label);  
        // Loop untuk setiap pasangan  
        for(i=0; i < phrase.size(); i += 2) {  
            System.out.print(Character.toChars(phrase.get(i) + (64 + adder)));  
            System.out.print(Character.toChars(phrase.get(i+1) + (64 + adder)));  
            if(i+2 <phrase.size()) {  
                System.out.print("-");  
            }  
        }  
        System.out.println();  
    }  
    // method ini membuat enkripsi yang sebenarnya  
    public static void encrypt(String phrase, boolean alphaZero)  
    {  
        int i;  
        int adder = alphaZero ? 1 : 0; // Untuk perhitungan tergantung pada alfabet  
        int[][] keyMatrix;  
        ArrayList<Integer> phraseToNum = new ArrayList<>();  
        ArrayList<Integer> phraseEncoded = new ArrayList<>();  
        // Hapus semua karakter non-bahasa Inggris, dan ubah frasa menjadi huruf besar 
        phrase = phrase.replaceAll("[^a-zA-Z]","").toUpperCase();  
  
        // Jika panjang frasa bukan bilangan genap, tambahkan "X" untuk membuatnya genap  
        if(phrase.length() % 2 == 1) {  
            phrase += "X";  
        }  
        // Dapatkan matriks kunci 2x2 dari sc  
        keyMatrix = getKeyMatrix();  
        // Periksa apakah matriksnya valid (det != 0)  
        isValidMatrix(keyMatrix);  
        // Ubah karakter menjadi angka sesuai dengan karakternya
        // tempatkan di tabel ASCII dikurangi 64 posisi (A=65 di tabel ASCII)
        // Jika kita menggunakan alfabet A=0, kurangi satu lagi (penambah)
        for(i=0; i < phrase.length(); i++) {  
            phraseToNum.add(phrase.charAt(i) - (64 + adder));  
        }  
       // Temukan produk per pasangan frasa dengan matriks kunci modulo 26
        // Jika kita menggunakan alfabet A=1 dan hasilnya 0, ganti dengan 26 (Z)
        for(i=0; i < phraseToNum.size(); i += 2) {  
            int x = (keyMatrix[0][0] * phraseToNum.get(i) + keyMatrix[0][1] * phraseToNum.get(i+1)) % 26;  
            int y = (keyMatrix[1][0] * phraseToNum.get(i) + keyMatrix[1][1] * phraseToNum.get(i+1)) % 26;  
            phraseEncoded.add(alphaZero ? x : (x == 0 ? 26 : x ));  
            phraseEncoded.add(alphaZero ? y : (y == 0 ? 26 : y ));  
        }  
        // Cetak hasilnya  
        echoResult("Encoded phrase: ", adder, phraseEncoded);  
    }  
    // method ini membuat dekripsi yang sebenarnya
    public static void decrypt(String phrase, boolean alphaZero)  
    {  
        int i, adder = alphaZero ? 1 : 0;  
        int[][] keyMatrix, revKeyMatrix;  
        ArrayList<Integer> phraseToNum = new ArrayList<>();  
        ArrayList<Integer> phraseDecoded = new ArrayList<>();  
        // Hapus semua karakter non-bahasa Inggris, dan ubah frasa menjadi huruf besar
        phrase = phrase.replaceAll("[^a-zA-Z]","").toUpperCase();  
  
        // Dapatkan matriks kunci 2x2 dari sc 
        keyMatrix = getKeyMatrix();  
        // Periksa apakah matriksnya valid(det != 0)  
        isValidMatrix(keyMatrix);  
        // Ubah angka menjadi karakter sesuai dengan
        // tempatkan di tabel ASCII dikurangi 64 posisi (A=65 di tabel ASCII)
        // Jika kita menggunakan alfabet A=0, kurangi satu lagi (penambah) 
        for(i=0; i < phrase.length(); i++) {  
            phraseToNum.add(phrase.charAt(i) - (64 + adder));  
        }  
        // Temukan matriks kunci terbalik
        revKeyMatrix = reverseMatrix(keyMatrix);  
        // Periksa apakah matriks kunci terbalik valid (product = 1,0,0,1)  
        isValidReverseMatrix(keyMatrix, revKeyMatrix);  
        // Temukan produk per pasangan frasa dengan matriks kunci terbalik modulo 26 
        for(i=0; i < phraseToNum.size(); i += 2) {  
            phraseDecoded.add((revKeyMatrix[0][0] * phraseToNum.get(i) + revKeyMatrix[0][1] * phraseToNum.get(i+1)) % 26);  
            phraseDecoded.add((revKeyMatrix[1][0] * phraseToNum.get(i) + revKeyMatrix[1][1] * phraseToNum.get(i+1)) % 26);  
        }  
        // Cetak hasilnya  
        echoResult("Decoded phrase: ", adder, phraseDecoded);  
    }  
    //main method  
    public static void main(String[] args) {  
        String opt, phrase;  
        byte[] p;  
        Scanner sc = new Scanner(System.in);  
        System.out.println("Hill Cipher Implementation (2x2)");  
        System.out.println("-------------------------");  
        System.out.println("1. Encrypt text (A=0,B=1,...Z=25)");  
        System.out.println("2. Decrypt text (A=0,B=1,...Z=25)");   
        System.out.println();  
        System.out.println("Type any other character to exit");  
        System.out.println();  
        System.out.print("Select your choice: ");  
        opt = sc.nextLine();  
        switch (opt)  
        {  
            case "1":  
                System.out.print("Enter phrase to encrypt: ");  
                phrase = sc.nextLine();  
                encrypt(phrase, true);  
                break;  
            case "2":  
                System.out.print("Enter phrase to decrypt: ");  
                phrase = sc.nextLine();  
                decrypt(phrase, true);  
                break;  
        }  
    }  
}  