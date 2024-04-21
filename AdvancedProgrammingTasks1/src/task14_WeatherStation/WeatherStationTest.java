package task14_WeatherStation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Weather{
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;
    public Weather(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        String dateString=date.toString();
        dateString=dateString.replace("UTC","GMT");
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",temperature,wind,humidity,visibility,dateString);
    }
}
class WeatherStation{

    List<Weather> list;
    int days;

    public WeatherStation(int days) {
        this.list = new ArrayList<>();
        this.days=days;
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        Weather w=new Weather(temperature,wind,humidity,visibility,date);

        if(list.isEmpty()){
            list.add(w);
            return;
        }
        Calendar now=Calendar.getInstance();
        now.setTime(date);

        Calendar lastTime=Calendar.getInstance();
        lastTime.setTime(list.get(list.size()-1).getDate());
        if(Math.abs(now.getTimeInMillis()-lastTime.getTimeInMillis())<2.5*60*1000){
            return;
        }
        list.add(w);

        ArrayList<Weather> toRemove=new ArrayList<>();
        for (Weather weather : list) {
            Calendar c=Calendar.getInstance();
            c.setTime(weather.getDate());
            if(Math.abs(now.getTimeInMillis()-c.getTimeInMillis())>days*86400000){
                toRemove.add(weather);
            }
        }
        list.removeAll(toRemove);

    }
    public int total(){
        return list.size();
    }
    public void status(Date from,Date to){
        ArrayList<Weather> newArr=new ArrayList<>();
        double avg=0.0;
        int ct=0;
        for (Weather weather : list) {
            Date d=weather.getDate();
            if((d.after(from) || d.equals(from))&&(d.before(to)||d.equals(to))){
                newArr.add(weather);
                avg+=weather.temperature;
                ct++;
            }
        }
        if(newArr.isEmpty()){
            throw new RuntimeException();
        }
        StringBuilder sb=new StringBuilder();
        for (Weather weather : newArr) {
            sb.append(weather).append("\n");
        }
        System.out.print(sb.toString());
        System.out.printf("Average temperature: %.2f\n",avg/ct);
    }
}
public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde