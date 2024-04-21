package task31_AdvancedProgrammingCourse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PointsNotValidException extends Exception{
    public PointsNotValidException() {
    }
}
class Student{
    String index;
    String name;
    int firstMidterm;
    int secondMidterm;
    int labsPoints;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.firstMidterm = 0;
        this.secondMidterm = 0;
        this.labsPoints = 0;
    }
    public void setFirstMidterm(int firstMidterm) {
        this.firstMidterm = firstMidterm;
    }

    public void setSecondMidterm(int secondMidterm) {
        this.secondMidterm = secondMidterm;
    }

    public void setLabsPoints(int labsPoints) {
        this.labsPoints = labsPoints;
    }
    double summaryPoints(){
        return firstMidterm*0.45+secondMidterm*0.45+labsPoints;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                index,name,firstMidterm,secondMidterm,labsPoints,summaryPoints(),grade());
    }

    public int grade() {
        int gr= (int) (summaryPoints()/10)+1;
        if(summaryPoints()<50){
            gr=5;
        }
        return gr;
    }

}
class AdvancedProgrammingCourse{
    Map<String,Student> studentsById;
    public AdvancedProgrammingCourse() {
        studentsById=new HashMap<>();
    }

    public void addStudent(Student student) {
        studentsById.put(student.index,student);
    }

    public void updateStudent (String idNumber, String activity, int points) throws PointsNotValidException {
        if(activity.equals("midterm1")){
            if(points<0|| points>100){
                throw new PointsNotValidException();
            }
            studentsById.get(idNumber).setFirstMidterm(points);
        }else if(activity.equals("midterm2")){
            if(points<0|| points>100){
                throw new PointsNotValidException();
            }
            studentsById.get(idNumber).setSecondMidterm(points);
        }else{
            if(points<0|| points>10){
                throw new PointsNotValidException();
            }
            studentsById.get(idNumber).setLabsPoints(points);
        }
    }

    public List<Student> getFirstNStudents (int n) {
        return studentsById.values()
                .stream()
                .sorted(Comparator.comparing(Student::summaryPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer,Integer> getGradeDistribution()  {
        Map<Integer,Integer> gradesFrequency=new TreeMap<>();
        IntStream.range(5,11)
                .forEach(i->gradesFrequency.put(i,0));
        studentsById.values()
                .forEach(i->gradesFrequency.computeIfPresent(i.grade(),(k,v)->v+1));
        return gradesFrequency;

    }

    public void printStatistics() {
        List<Student> passedStudents=studentsById.values().stream().filter(i->i.grade()>5).collect(Collectors.toList());
        DoubleSummaryStatistics ds=passedStudents.stream().mapToDouble(i->i.summaryPoints()).summaryStatistics();
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                ds.getCount(),ds.getMin(),ds.getAverage(),ds.getMax()));
    }
}
public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                try {
                    advancedProgrammingCourse.updateStudent(idNumber, activity, points);
                } catch (PointsNotValidException e) {
                    continue;
                }
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
