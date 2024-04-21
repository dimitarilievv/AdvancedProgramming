package task6_PayrollSystem;

import java.io.*;
import java.util.*;

interface Employee extends Comparable<Employee>{
    double getPayroll();
    String getLevel();
}

class HourlyEmployee  implements Employee{

    String id;
    String level;
    double regularH;
    double overtimeH;
    double paymentOfLevel;


    public HourlyEmployee(String id, String level, double time, double paymentOfLevel) {
        this.id = id;
        this.level = level;
        this.regularH = (time<40?time:40);
        this.overtimeH = (time<40?0:time-40);
        this.paymentOfLevel=paymentOfLevel;
    }
    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public double getPayroll() {
        return regularH*paymentOfLevel+overtimeH*paymentOfLevel*1.5;
    }

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparingDouble(Employee::getPayroll).reversed().thenComparing(Employee::getLevel).compare(this,o);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f", id, level, getPayroll(),regularH,overtimeH);
    }

}
class FreelanceEmployee implements Employee{

    String id;
    String level;
    List<Double> ticketPoints;
    double ticketRateOfLevel;

    public FreelanceEmployee(String id, String level, List<Double> ticketPoints, double ticketRateOfLevel) {
        this.id = id;
        this.level = level;
        this.ticketPoints = ticketPoints;
        this.ticketRateOfLevel=ticketRateOfLevel;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public double getPayroll() {
        return ticketPoints.stream().mapToDouble(points->points).sum()*ticketRateOfLevel;
    }

    @Override
    public int compareTo(Employee o) {
        return Comparator.comparingDouble(Employee::getPayroll).reversed().thenComparing(Employee::getLevel).compare(this,o);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d", id, level, getPayroll(),ticketPoints.size(),(int)ticketPoints.stream().mapToDouble(i->i).sum());
    }
}
class PayrollSystem{

    Map<String,Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;

    Map<String,Set<Employee>> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new TreeMap<>(Comparator.naturalOrder());

    }

    public void readEmployees(InputStream in) {
        Scanner scanner = new Scanner(in);

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts[0].equals("F")){
                addFreelancer(parts);
            }
            else {
                addHourlyEmployee(parts);
            }
        }
        scanner.close();
    }

    private void addHourlyEmployee(String[] parts) {
        String level = parts[2];
        Double ticketRateOfLevel = hourlyRateByLevel.get(level);
        double hours = Double.parseDouble(parts[3]);
        HourlyEmployee hourly = new HourlyEmployee(parts[1],level,hours,ticketRateOfLevel);
        employees.putIfAbsent(level,new TreeSet<>(Employee::compareTo));
        employees.get(level).add(hourly);
    }

    private void addFreelancer(String[] parts) {
        String level = parts[2];
        Double paymentOfLevel = ticketRateByLevel.get(level);
        List<Double> ticketPoints= new ArrayList<>();
        Arrays.stream(parts).skip(3).forEach(part->ticketPoints.add(Double.parseDouble(part)));
        FreelanceEmployee freelancer = new FreelanceEmployee(parts[1],level,ticketPoints,paymentOfLevel);
        employees.putIfAbsent(level,new TreeSet<>(Employee::compareTo));
        employees.get(level).add(freelancer);
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(PrintStream out, Set<String> levels) {

//        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(out));

        Map<String,Set<Employee>>  result = new TreeMap<>(Comparator.naturalOrder());

        employees.entrySet().stream().filter(entry -> levels.contains(entry.getKey())).forEach(entry->{
            result.putIfAbsent(entry.getKey(),new TreeSet<>());
            entry.getValue().forEach(employee -> result.get(entry.getKey()).add(employee));
        });

//        result.keySet().forEach(key->{
//            printWriter.println(String.format("LEVEL: %s",key));
//        });

        return result;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}
