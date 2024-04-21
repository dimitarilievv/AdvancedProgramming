package task7_TimeTable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class UnsupportedFormatException extends Exception{
    public UnsupportedFormatException(String line) {
        super(line);
    }
}
class InvalidTimeException extends Exception{
    public InvalidTimeException(String line) {
        super(line);
    }
}
class Time implements Comparable<Time>{
    int hour;
    int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String toString(TimeFormat format){
        StringBuilder sb=new StringBuilder();
        if(format==TimeFormat.FORMAT_24){
            sb.append(String.format("%2d:%02d",hour,minute));
        }
        else if(format==TimeFormat.FORMAT_AMPM){
            if(hour==0&&minute>=0 &&minute<=59){
                sb.append(String.format("%2d:%02d AM",hour+12,minute));
            }else if(hour>=1&&hour<=11&&minute>=0 &&minute<=59){
                sb.append(String.format("%2d:%02d AM",hour,minute));
            }else if(hour==12&&minute>=0 &&minute<=59){
                sb.append(String.format("%2d:%02d PM",hour,minute));
            }else{
                sb.append(String.format("%2d:%02d PM",hour-12,minute));
            }
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Time o) {
        if(this.hour==o.hour){
            return Integer.compare(this.minute,o.minute);
        }
        return Integer.compare(this.hour,o.hour);
    }
}
class TimeTable{
    List<Time> timeList;


    public TimeTable() {
        this.timeList = new ArrayList<>();
    }
    void readTimes(InputStream is) throws  UnsupportedFormatException, InvalidTimeException {
        Scanner sc=new Scanner(is);
        while(sc.hasNext()){
            String line= sc.next();
            if(!line.contains(".")&&!line.contains(":")){
                throw new UnsupportedFormatException(line);
            }
//            String splitChar=":";
//            if(line.contains(".")) splitChar="\\.";
            String []parts=line.split("[\\.:]");
            int hour=Integer.parseInt(parts[0]);
            int minutes=Integer.parseInt(parts[1]);
            if((hour<0||hour>23)&&(minutes<0||minutes>59)){
                throw new InvalidTimeException(line);
            }
            timeList.add(new Time(hour,minutes));
        }
    }
    void writeTimes(OutputStream outputStream, TimeFormat format){
        PrintWriter pw=new PrintWriter(outputStream);
        timeList.sort(Time::compareTo);
        for (Time time : timeList) {
            pw.println(time.toString(format));
        }
        pw.flush();
    }
}
public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}