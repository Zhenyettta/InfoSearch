package Pract6;

import Pract3.Main3Task;
import Pract3.Main3Task2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;



public class VB {
    static Map<String, String> map;
    static FileOutputStream fos;
	/*
    VBENCODENUMBER(n)
    1 bytes = <>
    2 while true
    3 do PREPEND(bytes, n mod 128)
    4 	if n < 128
    5 	then BREAK
    6 	n = n div 128
    7 bytes[LENGTH(bytes)] += 128
    8 return bytes
*/

    public static List<Long> VBEncode(List<Integer> numbers) {
        List<Long> bytestream = new ArrayList<Long>();
        long previousNumber = 0;
        for (long each : numbers) {
            bytestream.addAll(VBEncodenumber(each - previousNumber));
            previousNumber = each;
        }
        return bytestream;
    }

    // ------------------------------------------------------------------------------------------------------

    public static List<Long> VBEncodenumber(long n) {
        List<Long> bytes = new ArrayList<Long>(5);
        while (true) {
            bytes.add(0, n % 128);
            if (n < 128) break;
            n /= 128;
        }
        bytes.set(bytes.size() - 1, bytes.get(bytes.size() - 1) + 128);
        return bytes;
    }

    // ------------------------------------------------------------------------------------------------------

    public static List<Long> VBDecode(List<Long> bytestream) {
        long n = 0;
        long prevNum = 0;
        List<Long> numbers = new ArrayList<Long>();
        for (int i = 0; i < bytestream.size(); i++) {
            if (bytestream.get(i) < 128) {
                n = 128 * n + bytestream.get(i);
            } else {
                n = 128 * n + (bytestream.get(i) - 128);
                numbers.add(n + prevNum);
                prevNum = numbers.get(numbers.size() - 1);
                n = 0;
            }
        }
        return numbers;
    }

    public static void main(String[] args) throws Exception {
        fos = new FileOutputStream("src/main/java/texts/posting.bin");
        Pattern pattern = Pattern.compile("text\\d+.txt:");
        int vocabIdx = 0;
        int vocabPos = 0;
        int idx = 0;
         map = Main3Task2.getMap();
        long[] vocabPositions = new long[map.size()];

        writerVocabTable(toByteArray(VBEncodenumber(map.size())));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            final int[] previousId = {0};
            OutputStreamWriter vocabList = new OutputStreamWriter(new FileOutputStream("src/main/java/texts/vocab.bin"), StandardCharsets.US_ASCII);
            vocabPositions[vocabIdx++] = vocabPos;
            vocabList.write(entry.getKey());
            vocabPos += entry.getValue().length();

            writerVocabTable(toByteArray(VBEncodenumber(vocabPositions[idx++])));
            writerVocabTable(toByteArray(VBEncodenumber(fos.getChannel().position())));

            writerPost(toByteArray(VBEncodenumber(countFiles(entry.getKey()))));

             LinkedHashMap<String, ArrayList<Integer>> postings = Main3Task2.getPostingMapFromFile(entry.getKey());

             postings.forEach((s,v)->{
                 v.forEach(i->{
                     int gap = i - previousId[0];
                     previousId[0] = i;

                     try {
                         writerPost(toByteArray(VBEncodenumber(gap)));
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }


                 });

             });



        }

//        Scanner scanner = new Scanner(reader.readLine());
//        writer(scanner.next().getBytes());
//        String fileName;
//        ArrayList<Integer> list = new ArrayList<>();
//        while (scanner.hasNext()) {
//            String word = scanner.next();
//            if (word.matches(pattern.pattern())) {
//                fileName = word;
//                if (list.size() == 0)
//                    continue;
//                writer(fileName.getBytes());
//                writer(toByteArray(list));
//                list = new ArrayList<>();
//                continue;
//
//            }
//            list.add(Integer.parseInt(word));

//
//
//        }
//        //Testing
//        List<Integer> numbers = new ArrayList<>();
//        numbers.add(824);
//        numbers.add(829);
//        numbers.add(215406);

//        numbers.forEach(s -> System.out.println(Integer.toBinaryString(s)));
//        System.out.println();
//        for (int i = 0; i < VBEncode(numbers).size(); i++) {
//            System.out.println(Long.toBinaryString((VBEncode(numbers).get(i))));
//        }
//
//        try (FileInputStream fis = new FileInputStream("src/main/java/texts/BINARY.bin")) {
//            int content;
//            while ((content = fis.read()) != -1)
//                System.out.println(Long.toBinaryString(content));
//        }


    }


    static void writerPost(byte[] arr) throws IOException {
        fos.write(arr);

    }

    static void writerVocabTable(byte[] arr) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("src/main/java/texts/vocabTable.bin")) {
            fos.write(arr);
        }
    }

    static byte[] toByteArray(List<Long> list) {
        byte[] byteArray = new byte[list.size()];
        int index = 0;
        for (Long i : list) {
            byteArray[index] = i.byteValue();
            index++;
        }
        return byteArray;
    }
    static int countFiles(String key){
        Pattern pattern = Pattern.compile("text\\d+.txt:");
        String postings = map.get(key);
        int counter = 0;
        Scanner scan = new Scanner(postings);
        while (scan.hasNext()){
            if (scan.next().matches(pattern.pattern())) counter++;
        }
        return counter;
    }

}
