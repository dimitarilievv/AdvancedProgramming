package task28_Faculty;

//package zad28_faculty;
import java.util.*;
//NOT FINISHED
class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Student {
    String id;
    int yearsOfStudies;
    //key=reden broj na semestar value=semestar
    Map<Integer, Term> terms;
    TreeSet<String> allCourses;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        terms = new HashMap<>(yearsOfStudies * 2);
        for (int i = 0; i < yearsOfStudies; i++) {
            terms.putIfAbsent(i + 1, new Term());
        }
        allCourses=new TreeSet<>();
    }

    public double averageGradeForStudent() {
        return terms.values().stream().mapToDouble(i -> i.averageGradeForTerm()).average().orElse(0);
    }

    public int totalGrades() {
        return terms.values().stream().mapToInt(i -> i.numberGrades()).sum();
    }

    public boolean graduated() {
        int g = totalGrades();
        return (yearsOfStudies == 3 && g == 18) || (yearsOfStudies == 4 && g == 24);
    }
    public int totalNumberOfPassedCourses(){
        return terms.values().stream()
                .mapToInt(i->i.numberOfCoursesPassed())
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Student: ").append(id).append("\n");
        terms.entrySet().forEach(i->{
            sb.append("Term ").append(i.getKey()).append(":\n");
            sb.append(i.getValue().toString());
        });
        sb.append(String.format("Average grade: %.2f\n",averageGradeForStudent()));
        sb.append(String.format("Courses attended: %s\n",String.join(",",allCourses)));
        return sb.toString();
    }
    public String shortReport(){
        return String.format(" Student: %s Courses passed: %d Average grade: %.2f",
                id,totalNumberOfPassedCourses(),averageGradeForStudent());
    }
}

class Term {
    //key=ime na kurs value=ocena
    Map<String, List<Integer>> courses;

    public Term() {
        courses = new HashMap<>();
    }

    public double averageGradeForTerm() {
        return courses.values().stream()
                .flatMap(i -> i.stream())
                .mapToInt(j -> j)
                .average().orElse(0);

    }

    public void addGrade(String course, int grade) {
        courses.putIfAbsent(course, new ArrayList<>());
        courses.get(course).add(grade);

    }

    public int numberGrades() {
        return (int) courses.values().stream()
                .flatMap(i -> i.stream())
                .mapToInt(j -> j)
                .count();
    }
    public int numberGradesOfCourse(String course){
        if(courses.containsKey(course))
            return courses.get(course).size();
        return 0;
    }
    public boolean isCoursePassed(String course){
        if(courses.get(course).size()==3){
            return true;
        }
        return false;
    }
    public int numberOfCoursesPassed(){
        return (int) courses.entrySet().stream()
                .filter(c->isCoursePassed(c.getKey()))
                .count();
    }
    @Override
    public String toString() {
        return String.format("Courses for term: %d\nAverage grade for term: %.2f\n",
                courses.size(),averageGradeForTerm());
    }
}

class Faculty {
    //key id value studentot
    Map<String, Student> students;
    List<String> graduatedLogs;
    public Faculty() {
        students = new TreeMap<>();
        graduatedLogs=new ArrayList<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.putIfAbsent(id, new Student(id, yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        int yearsOfStudies = students.get(studentId).yearsOfStudies;
        if (term > yearsOfStudies) {
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, studentId));
        }
        if (students.get(studentId).terms.get(term).numberGradesOfCourse(courseName) >= 3) {
            throw new OperationNotAllowedException(String.format(" Student %s already has 3 grades in term %d", studentId, term));
        }
        students.get(studentId).terms.get(term).addGrade(courseName, grade);
        students.get(studentId).allCourses.add(courseName);
        if(students.get(studentId).graduated()){
            graduatedLogs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years",
                    studentId,students.get(studentId).averageGradeForStudent(),
                    students.get(studentId).yearsOfStudies));
            students.remove(studentId);
        }
    }

    String getFacultyLogs() {
        return String.join("\n",graduatedLogs);
    }

    String getDetailedReportForStudent(String id) {
        Student student=students.get(id);
        if(student!=null){
            return student.toString();
        }
        return "not found";
    }

    void printFirstNStudents(int n) {
        Comparator<Student> comparator=Comparator.comparing(Student::totalGrades)
                .thenComparing(Student::averageGradeForStudent).reversed();
        students.values().stream().sorted(comparator)
                .limit(Math.min(students.size(),n))
                .forEach(i->{
                    System.out.println(i.shortReport());
                });
    }

    void printCourses() {

    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
        sc.close();
    }

}
