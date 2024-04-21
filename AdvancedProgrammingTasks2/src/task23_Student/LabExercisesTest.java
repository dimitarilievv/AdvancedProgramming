package task23_Student;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

class Student {
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public int totalPoints() {
        return points.stream().mapToInt(i -> i).sum();
    }

    public double averagePoints() {
        return totalPoints() / 10.0;
    }

    public boolean hasSignature() {
        return points.size() >= 8;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }
    int getYearOfStudies(){
        return 20-Integer.parseInt(index.substring(0,2));
    }

    public String toString() {
        if (hasSignature()) {
            return String.format("%s YES %.2f", index, averagePoints());
        }
        return String.format("%s NO %.2f", index, averagePoints());
    }

    public int compareToFailed(Student o1) {
        int index = this.index.compareTo(o1.index);
        if (index == 0) {
            return Double.compare(this.averagePoints(), o1.averagePoints());
        }
        return index;
    }

}

class LabExercises {
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        Comparator<Student> comparator = (o1, o2) -> {
            if (ascending) {
                double prosek = Double.compare(o1.averagePoints(), o2.averagePoints());
                if (prosek == 0) {
                    return o1.index.compareTo(o2.index);
                }
                return (int) prosek;
            } else {
                double prosek = Double.compare(o2.averagePoints(), o1.averagePoints());
                if (prosek == 0) {
                    return o2.index.compareTo(o1.index);
                }
                return (int) prosek;
            }
        };
        //        Comparator<Student> comparator=Comparator.comparing(Student::averagePoints).thenComparing(Student::getIndex);
        //        if(!ascending){
        //            comparator=comparator.reversed();
        //        }
        students.stream().sorted(comparator).limit(n).forEach(i -> System.out.println(i.toString()));
    }

    public List<Student> failedStudents() {
        //        Comparator<Student> comparator=Comparator.comparing(Student::getIndex).thenComparing(Student::averagePoints);
        return students.stream().sorted(Student::compareToFailed).filter(i -> !i.hasSignature()).collect(Collectors.toCollection(ArrayList::new));
    }

    public Map<Integer,Double> getStatisticsByYear() {
        Map<Integer,Double> sumPointByYear=new HashMap<>();//2godina 535poeni
        Map<Integer,Integer> countByYear=new HashMap<>();//2goina 200studenti
        //1NACIN
//       students.stream().filter(s->s.hasSignature()).forEach(s->{
//           sumPointByYear.putIfAbsent(s.getYearOfStudies(),0.0);
//           countByYear.putIfAbsent(s.getYearOfStudies(),0);
//
//           sumPointByYear.computeIfPresent(s.getYearOfStudies(),
//                   (k,v)->v+s.averagePoints());
//           countByYear.computeIfPresent(
//                   s.getYearOfStudies(),
//                   (k,v)->++v);
//       });
//       sumPointByYear.keySet().stream().forEach(year->sumPointByYear.computeIfPresent(
//               year,
//               (k,v)->v/countByYear.get(year)
//       ));
//       return sumPointByYear;

        //2NACIN
        return students.stream().filter(s->s.hasSignature()).collect(Collectors.groupingBy(
                Student::getYearOfStudies,
                Collectors.averagingDouble(Student::averagePoints)
        ));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}