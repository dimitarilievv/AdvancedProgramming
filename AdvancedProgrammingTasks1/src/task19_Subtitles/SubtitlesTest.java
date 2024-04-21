package task19_Subtitles;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;


public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

class Element {
    int number;
    int timeStart;
    int timeEnd;
    String text;

    public Element(int number, int timeStart, int timeEnd, String text) {
        this.number = number;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.text = text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(number).append("\n");
        sb.append(String.format("%s --> %s", timeToString(timeStart), timeToString(timeEnd))).append("\n");
        sb.append(text);
        return sb.toString();
    }

    static String timeToString(int time) {
        int h = time / (60 * 60 * 1000);
        time = time % (60 * 60 * 1000);
        int m = time / (60 * 1000);
        time = time % (60 * 1000);
        int s = time / 1000;
        int ms = time % 1000;
        return String.format("%02d:%02d:%02d,%03d", h, m, s, ms);
    }
    public void shift(int ms) {
        timeStart += ms;
        timeEnd += ms;
    }
}

class Subtitles {
    List<Element> list;

    Subtitles() {
        list = new ArrayList<>();
    }

    int loadSubtitles(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            int number = Integer.parseInt(sc.nextLine());
            String[] parts = sc.nextLine().split(" --> ");
            int timeStart = convertPartToInt(parts[0]);
            int timeEnd = convertPartToInt(parts[1]);
            StringBuilder text=new StringBuilder();
            while(true){
                if(!sc.hasNextLine())break;
                String line = sc.nextLine();
                if(line.trim().length()==0)break;
                text.append(line);
                text.append("\n");
            }
            list.add(new Element(number, timeStart, timeEnd, text.toString()));
        }
        return list.size();
    }

    void print() {
        for (Element element : list) {
            System.out.println(element);
        }
    }
    void shift(int ms){
        for (Element element : list) {
            element.shift(ms);
        }
    }

    int convertPartToInt(String time) {
        String[] parts1 = time.split(",");
        int milisec = Integer.parseInt(parts1[1]);
        String[] parts2 = parts1[0].split(":");
        int hours = Integer.parseInt(parts2[0]);
        int minutes = Integer.parseInt(parts2[1]);
        int sec = Integer.parseInt(parts2[2]);
        int res = milisec;
        res += sec * 1000;
        res += minutes * 60 * 1000;
        res += hours * 60 * 60 * 1000;
        return res;
    }
}
