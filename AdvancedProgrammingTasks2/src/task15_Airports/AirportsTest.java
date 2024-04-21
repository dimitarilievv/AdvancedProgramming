package task15_Airports;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
class Airport{
    String name;
    String country;
    String code;
    int passengers;
    TreeSet<Flight> flights;
    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flights=new TreeSet<>(Flight::compareTo);
    }
    public void addFlightToAirport(Flight f){
        flights.add(f);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s (%s)\n",name,code));
        sb.append(country).append("\n");
        sb.append(passengers).append("\n");
        int index=1;
        for (Flight flight : flights) {
            sb.append(index++).append(". ");
            sb.append(flight.toString());
        }
        return sb.toString().trim();
    }
}
class Flight implements Comparable<Flight>{
    String from;
    String to;
    LocalTime arrivalTime;
    LocalTime departureTime;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        departureTime=LocalTime.of(0,0).plusMinutes(time);
        arrivalTime=departureTime.plusMinutes(duration);
        this.duration = duration;
    }
    int getHours(){
        return duration/60;
    }
    int getMinutes(){
        return duration%60;
    }
    @Override
    public String toString() {
        if(arrivalTime.isBefore(departureTime)){
            return String.format("%s-%s %s-%s +1d %dh%02dm\n",from,to,
                    departureTime.toString(),arrivalTime.toString(),getHours(),getMinutes());
        }
        return String.format("%s-%s %s-%s %dh%02dm\n",from,to,
                departureTime.toString(),arrivalTime.toString(),getHours(),getMinutes());
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public int compareTo(Flight o) {
        return Comparator.comparing(Flight::getTo).thenComparing(Flight::getDepartureTime).compare(this,o);
    }
    public int compareToReversed(Flight other){
        return Comparator.comparing(Flight::getDepartureTime).thenComparing(Flight::getArrivalTime).compare(this, other);

    }
}
class Airports{
    Map<String,Airport> airports;
    public Airports() {
        airports=new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code,new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        airports.get(from).addFlightToAirport(new Flight(from, to, time, duration));
    }

    public void showFlightsFromAirport(String code){
        System.out.println(airports.get(code).toString());
    }

    public void showDirectFlightsFromTo(String from, String to) {
        int count= (int) airports.get(from).flights.stream().filter(f->f.to.equals(to)).count();
        if(count==0){
            System.out.println(String.format("No flights from %s to %s",from,to));
            return;
        }
        StringBuilder sb=new StringBuilder();
        airports.get(from).flights.stream()
                .filter(i->i.to.equals(to))
                .sorted(Flight::compareTo)
                .forEach(s->sb.append(s.toString()).append("\n"));
        System.out.println(sb.toString().trim());
    }

    public void showDirectFlightsTo(String to) {
        StringBuilder sb=new StringBuilder();
        airports.values().stream()
                .flatMap(i->i.flights.stream())
                .filter(j->j.getTo().equals(to))
                .sorted(Flight::compareToReversed)
                .forEach(f->sb.append(f.toString()));
        System.out.println(sb.toString().trim());
    }
}
public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

