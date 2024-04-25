package lab7_task4_TermFrequency;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency {
    Map<String, Integer> map;
    int count;

    TermFrequency(InputStream inputStream, String[] stopWords) {
        this.map = new TreeMap<>();
        this.count = 0;
        Scanner sc = new Scanner(inputStream);
        List<String> stop = Arrays.asList(stopWords);
        while (sc.hasNext()) {
            String word = sc.next();
            word = word.toLowerCase().replace(',', '\0').replace('.', '\0').trim();
            if (stop.contains(word) || word.isEmpty()) {
                continue;
            }
            int v = map.computeIfAbsent(word, x -> 0);
            map.put(word, ++v);
            count++;

        }
    }

    public int countTotal() {
        return count;
    }

    int countDistinct() {
        return map.size();
    }

    public List<String> mostOften(int k) {
        return map.keySet().stream()
                .sorted(Comparator.comparing(x -> map.get(x))
                        .reversed())
                .limit(k)
                .collect(Collectors.toList());
    }

}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
