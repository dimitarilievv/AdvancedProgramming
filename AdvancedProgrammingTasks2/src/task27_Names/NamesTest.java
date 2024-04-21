package task27_Names;

import java.util.*;
import java.util.stream.Collectors;

class Names {
    Map<String, Integer> namesWithFrequency;

    public Names() {
        namesWithFrequency = new TreeMap<>();
    }

    public void addName(String name) {
        namesWithFrequency.putIfAbsent(name, 0);
        namesWithFrequency.computeIfPresent(name, (k, v) -> v + 1);
    }

    public void printN(int n) {
        namesWithFrequency.entrySet().stream()
                .filter(x -> x.getValue() >= n)
                .forEach(x -> {
                    Set<Character> characters = x.getKey().chars()
                            .mapToObj(c -> (char) Character.toLowerCase(c))
                            .collect(Collectors.toCollection(HashSet::new));
                    System.out.println(String.format("%s (%d) %d", x.getKey(), x.getValue(), characters.size()));
                });
    }

    public String findName(int len, int index) {
        List<String> uniqueNames = namesWithFrequency.keySet().stream()
                .filter(name -> name.length() < len)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        return uniqueNames.get(index%uniqueNames.size());
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

