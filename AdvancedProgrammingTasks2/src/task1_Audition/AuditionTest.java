package task1_Audition;

import java.util.*;

class Participant{
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d",code,name,age);
    }
}
class Audition{
    Map<String,Set<Participant>> participantsByCity;
    Map<String,Set<String>> codesByCity;
    public Audition() {
        participantsByCity=new HashMap<>();
        codesByCity=new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        participantsByCity.putIfAbsent(city,new HashSet<>());
        codesByCity.putIfAbsent(city,new HashSet<>());
        Participant p=new Participant(city, code, name, age);
        if(!codesByCity.get(city).contains(p.code)){
            codesByCity.get(city).add(p.code);
            participantsByCity.get(city).add(p);
        }
    }

    public void listByCity(String city) {
        Comparator<Participant> comparator=Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge)
                .thenComparing(Participant::getCode);
        participantsByCity.get(city)
                .stream()
                .sorted(comparator)
                .forEach(i-> System.out.println(i));
    }
}
public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

