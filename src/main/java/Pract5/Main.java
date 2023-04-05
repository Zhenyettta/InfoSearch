package Pract5;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long time_m = System.nanoTime();
        File file = new File("src/main/java/texts/important");
        runSPIMIAlgorithm(file);
        time_m = System.nanoTime() - time_m;
        System.out.println(time_m / 1000000000 + " s");

    }

    public static void runSPIMIAlgorithm(File folder) throws IOException {
        System.out.println(java.lang.Runtime.getRuntime().maxMemory());
        SPIMI spimi = new SPIMI(600000);
        spimi.SPIMIAgorithm(folder);
        spimi.mergeAllBlocks();
    }
}