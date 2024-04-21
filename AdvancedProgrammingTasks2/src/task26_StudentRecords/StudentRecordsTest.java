package task26_StudentRecords;

import java.io.*;
import java.util.*;

//not finished
class Student{
    String code;
    String direction;
    List<Integer> grades;

    public Student() {
        code ="";
        direction ="";
        grades =new ArrayList<>();
    }

    public Student(String code, String direction, List<Integer> grades) {
        this.code = code;
        this.direction = direction;
        this.grades = grades;
    }
    public double average(){
        return (double) grades.stream().mapToInt(i -> i).sum() / grades.size();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, average());
    }
}
class StudentRecords{
    Map<String,Set<Student>>students;

    public StudentRecords() {
        students=new HashMap<>();
    }
    public int readRecords(InputStream in) {
        Scanner sc=new Scanner(in);
        int counter=0;
        while(sc.hasNextLine()){
            String line=sc.nextLine();
            String []parts=line.split(" ");
            String code=parts[0];
            String direction=parts[1];
            List<Integer> list=new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                list.add(Integer.parseInt(parts[i]));
            }
            Student student=new Student(code,direction,list);
            students.computeIfAbsent(direction,i->new HashSet<>());
            students.computeIfPresent(direction,(k,v)->{
                v.add(student);
                return v;
            });
            counter++;
        }
        return counter;
    }

    public void writeTable(PrintStream out) {
        Comparator<Student> comparator= Comparator.comparing(Student::average)
                .thenComparing(Student::getCode).reversed();
        PrintWriter pw=new PrintWriter(new OutputStreamWriter(out));
        students.forEach((k,v)->{
            pw.println(k);
            v.stream().sorted(comparator)
                    .forEach(pw::println);
        });
        pw.flush();
    }

    public void writeDistribution(PrintStream out) {
//        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
//
//        // Sorting the nasoki by the total number of grades in descending order
//        List<String> sortedNasoki = students.keySet().stream()
//                .sorted(Comparator.comparingInt(nasoka -> -students.get(nasoka).stream().mapToInt(student -> student.oceni.size()).sum()))
//                .collect(Collectors.toList());
//
//        sortedNasoki.forEach(nasoka -> {
//            pw.println(nasoka);
//            Map<Integer, Long> gradeDistribution = students.get(nasoka).stream()
//                    .flatMap(student -> student.oceni.stream())
//                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//
//            gradeDistribution.entrySet().stream()
//                    .sorted(Map.Entry.comparingByKey())
//                    .forEach(entry -> {
//                        int grade = entry.getKey();
//                        long count = entry.getValue();
//                        pw.printf(" %d | %s(%d)\n", grade, "*".repeat((int) count/10), count);
//                    });
//        });
//
//        pw.flush();
    }
}
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}
