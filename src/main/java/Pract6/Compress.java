package Pract6;

import Pract4.Main;
import Pract5.SPIMI;
import org.github.jamm.MemoryMeter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Compress {

    public static void main(String[] args) throws IOException {
        ArrayList<String> test = new ArrayList<>();
        test.add("3*1$у");
//        System.out.println(FrontCompress.decompress(test));
        new Main();
        Main.main(new String[0]);
        FrontCompress.saveToFile(FrontCompress.compress(Main.finalArr), "src/main/java/texts/Compressed.txt");
        System.out.println(FrontCompress.decompress(FrontCompress.compress(Main.finalArr)));
        StringBuilder line = new StringBuilder();
//        File file = new File("src/main/java/texts/important");
//        SPIMI spimi = new SPIMI(600000);
//        spimi.SPIMIAgorithm(file);
//        spimi.mergeAllBlocks();
//        for (Map.Entry<String, List<String>> entry : SPIMI.dictionary.entrySet()) {
//            line.append(entry.getKey());
//            for (String b : entry.getValue()) {
//                line.append(Integer.parseInt(b.replaceAll("[a-zA-Z]+|\\.", "")));
//            }
//        }
//        File file1 = new File("src/main/java/texts/Compressed.txt");
//        try (FileWriter fw = new FileWriter(file1)) {
//            fw.write(line.toString().replaceAll(" ", ""));
//        }
//
//        MemoryMeter memoryMeter = new MemoryMeter();
//
//        System.out.println(memoryMeter.measure(line.toString().getBytes()) + " KB");
//        System.out.println((float) (memoryMeter.measure(line.toString().getBytes())) / 1048576 + " MB");
//
//        System.out.println(line.length());


    }


}

