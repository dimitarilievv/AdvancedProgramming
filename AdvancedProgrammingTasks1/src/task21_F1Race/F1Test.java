package task21_F1Race;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class Racer{
    String ime;
    ArrayList<String> list;

    public Racer(String ime, String lap1, String lap2, String lap3) {
        this.ime = ime;
        list=new ArrayList<>();
        list.add(lap1);
        list.add(lap2);
        list.add(lap3);
    }
    String findBestTime(){
        Collections.sort(list);
        return list.get(0);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s",ime,findBestTime());
    }
    //    int stringToTime(String str){
//        int sum=0;
//        String []parts=str.split(":");
//        int min=Integer.parseInt(parts[0]);
//        int sec=Integer.parseInt(parts[1]);
//        int msec=Integer.parseInt(parts[2]);
//        sum+=min*60*1000;
//        sum+=sec*1000;
//        sum+=msec;
//        return sum;
//    }
//    String timetoString(int time){
//        String s="";
//
//        int min=(time/1000/60);
//        int sec=(time-min*1000*60)/1000;
//        int msec=time%1000;
//        return String.format("%d:%02d:%03d",min,sec,msec);
//    }
}
class F1Race {
    List<Racer> list;
    public F1Race() {
        list=new ArrayList<>();
    }
    void readResults(InputStream inputStream){
        Scanner sc=new Scanner(inputStream);
        while(sc.hasNextLine()){
            String line=sc.nextLine();
            String []parts=line.split(" ");
            String ime=parts[0];
            String lap1=parts[1];
            String lap2=parts[2];
            String lap3=parts[3];
            list.add(new Racer(ime,lap1,lap2,lap3));
        }
    }
    void printSorted(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        list.sort(Comparator.comparing(Racer::findBestTime));
        for(int i=0;i<list.size();i++){
            pw.println((i+1)+". "+list.get(i).toString());
        }
        pw.close();
    }
}