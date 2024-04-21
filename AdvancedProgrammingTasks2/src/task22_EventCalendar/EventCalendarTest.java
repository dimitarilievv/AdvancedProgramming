package task22_EventCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WrongDateException extends Exception{
    public WrongDateException(String message) {
        super(message);
    }
}
class Event implements Comparable<Event>{
    String name;
    String location;
    Date time;

    public Event(String name, String location, Date time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    @Override
    public String toString() {
        DateFormat dateFormat=new SimpleDateFormat("dd MMM, yyyy HH:mm");
        return String.format("%s at %s, %s",dateFormat.format(time), location, name);
    }

    public int getMonth() {
        Calendar c=Calendar.getInstance();
        c.setTime(time);
        return c.get(Calendar.MONTH);
    }

    @Override
    public int compareTo(Event o) {
        int date= time.compareTo(o.time);
        return date!=0 ? date : name.compareTo(o.name);
    }
}
class EventCalendar{
    int year;
    Set<Event> events;

    public EventCalendar(int year) {
        this.year = year;
        events=new TreeSet<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        if(c.get(Calendar.YEAR)!=year){
            throw new WrongDateException(String.format("Wrong date: %s",date));
        }
        events.add(new Event(name,location,date));

    }

    public void listEvents(Date date) {
        List<Event> filtered=events.stream().filter(x->compareDate(x.time,date))
                .collect(Collectors.toList());
        if(filtered.isEmpty()){
            System.out.println("No events on this day!");
            return;
        }
        filtered.forEach(i-> System.out.println(i));
    }
    boolean compareDate(Date d1, Date d2){
        Calendar c1=Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2=Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH) &&
                c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH) &&
                c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR);

    }
    public void listByMonth() {
        IntStream.range(0,12).forEach(i->{
            long count=events.stream().filter(x->x.getMonth()==i)
                    .count();
            System.out.printf("%d : %d%n",(i+1),count);
        });
    }
}
public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
