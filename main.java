//Mert Büyükaksoy - 19360859058


import java.util.Arrays;
import java.util.Random;



public class main {
    static final int NUM_URUN = 10; // 10 adet ürün olsun
    static final int NUM_PARCACIK = 40; // Parçacık sayısı
    static final int NUM_TEKRAR = 100; // Tekrar sayısı

    static final int[][] uzaklıkMatris = { //Ürünlerin birbirine olan uzaklıkları
            {2, 5, 3, 10, 7, 6, 8, 9, 4, 12},
            {4, 3, 8, 6, 12, 9, 7, 5, 6, 10},
            {6, 9, 4, 7, 8, 10, 3, 12, 8, 2},
            {7, 11, 5, 3, 9, 6, 4, 10, 7, 1},
            {10, 2, 7, 4, 8, 12, 5, 11, 9, 3},
            {11, 4, 9, 2, 7, 11, 6, 10, 8, 1},
            {5, 8, 3, 10, 6, 12, 4, 9, 7, 4},
            {7, 10, 4, 9, 5, 11, 3, 8, 6, 1},
            {9, 3, 6, 12, 8, 5, 2, 11, 10, 4},
            {11, 6, 6, 3, 7, 9, 4, 10, 3, 7}
    };

    public static void main(String[] args) {
        Random random = new Random();

        int[][] particles = new int[NUM_PARCACIK][NUM_URUN]; // Parçacık konumları
        double[][] velocities = new double[NUM_PARCACIK][NUM_URUN]; // Parçacık hızları

        for (int i = 0; i < NUM_PARCACIK; i++) {
            particles[i] = rastgeleDizilim(); // Rastgele yerleşim düzenleri oluşturma
        }

        int[] bestLayout = particles[0];
        double bestFitness = calculateFitness(particles[0]);

        for (int tekrar = 0; tekrar < NUM_TEKRAR; tekrar++) {
            for (int i = 0; i < NUM_PARCACIK; i++) {
                double fitness = calculateFitness(particles[i]);
                if (fitness < bestFitness) {
                    bestFitness = fitness;
                    bestLayout = Arrays.copyOf(particles[i], particles[i].length);
                }

                double inertia = 0.5;//yükseldikçe parçacık geçmiş hareketine güvenir ,düştükçe daha rastgele hareket olur.
                double c1 = 2.0;//kendi en iyi konumuna hareket
                double c2 = 2.0;//sürüdeki en iyi konuma hareket
                double r1 = random.nextDouble();//yeni hızlarını belirlerlenki katsayılar
                double r2 = random.nextDouble();

                for (int j = 0; j < NUM_URUN; j++) {
                    velocities[i][j] = inertia * velocities[i][j] +
                            c1 * r1 * (bestLayout[j] - particles[i][j]) +//personal beste olan uzaklık
                            c2 * r2 * (bestLayout[j] - particles[i][j]);// global beste olan uzaklık
                }

                for (int j = 0; j < NUM_URUN; j++) {
                    particles[i][j] += velocities[i][j];//hız değeri o boyuttaki konuma ekle =konum
                }

                for (int j = 0; j < NUM_URUN; j++) {
                    particles[i][j] = Math.min(Math.max(particles[i][j], 0), NUM_URUN - 1);//çözüm uzayının dışına çıkmasını engeller
                }
            }

            if (tekrar % 10 == 0) {
                System.out.println("Iteration: " + tekrar);
                System.out.println("Best Layout: " + Arrays.toString(bestLayout));
                System.out.println("Fitness: " + bestFitness);
                System.out.println("--------------------------------");
            }
        }
    }

    static int[] rastgeleDizilim() {    //rastgele depo yerleşimi
        int[] dizilim = new int[NUM_URUN];
        for (int i = 0; i < NUM_URUN; i++) {
            dizilim[i] = i;
        }
        shuffleArray(dizilim);
        return dizilim;
    }

    static void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);//Rastgele indeks seç. Mevcut elemanının yer değiştireceği indis
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

     static double calculateFitness(int[] dizilim) {
        double totalDistance = 0;
        for (int i = 0; i < dizilim.length - 1; i++) {
            totalDistance += uzaklıkMatris[dizilim[i]][dizilim[i + 1]]; // Toplam mesafeyi hesaplama
        }
        return totalDistance; // Uygunluk (fitness) olarak toplam mesafeyi döndürme
    }
}
