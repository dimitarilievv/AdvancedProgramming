package task16_MojDDV2;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Objects;
import java.util.Scanner;
import java.util.*;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int totalSum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", totalSum));
    }
}
class Item{
    String type;
    int price;

    public Item(String type,int price) {
        this.type = type;
        this.price = price;
    }
    public Item(){
        type="A";
        price=0;
    }
    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }
    public double tax(){
        if(type.equals("A")){
            return price*0.18*0.15;
        }else if(type.equals("B")){
            return price*0.05*0.15;
        }
        return 0;
    }

}
class User{
    String id;
    List<Item> items;

    public User(String id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public User(){
        id="None";
        items=new ArrayList<>();
    }

    int totalSum(){
        int sum=0;
        for (Item item : items) {
            sum += item.price;
        }
        return sum;
//        return items.stream().mapToInt(Item::getPrice).sum();
    }
    double totalTax(){
//        double sum=0.0;
//        for (Item item : items) {
//            sum += item.tax();
//        }
//        return sum;
        return items.stream().mapToDouble(Item::tax).sum();
    }
    public void check() throws AmountNotAllowedException {
        if(totalSum() > 30000) {
            throw new AmountNotAllowedException(totalSum());
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f",id,totalSum(),totalTax());
    }
}
class MojDDV{
    List<User> users;

    public MojDDV() {
        users = new ArrayList<>();
    }
    void readRecords(InputStream is) {
        Scanner sc=new Scanner(is);
        while(sc.hasNextLine()){
            String line= sc.nextLine();
            String []parts=line.split("\\s+");

            User u=new User();
            u.setId(parts[0]);

            for(int i=1;i< parts.length;i+=2){
                u.addItem(new Item(parts[i+1],Integer.parseInt(parts[i])));
            }


            try{
                u.check();
            }catch (Exception e){
                System.out.println(e.getMessage());
                continue;
            }

            users.add(u);
        }
    }
    void printTaxReturns(OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        for (User user : users) {
            pw.println(user);
        }
//        users.forEach(pw::println);
        pw.flush();
    }
    public void printStatistics(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        DoubleSummaryStatistics summary = users.stream().mapToDouble(User::totalTax).summaryStatistics();
        pw.println(String.format("min:\t%.3f", summary.getMin()));
        pw.println(String.format("max:\t%.3f", summary.getMax()));
        pw.println(String.format("sum:\t%.3f", summary.getSum()));
        pw.println(String.format("count:\t%d", summary.getCount()));
        pw.println(String.format("avg:\t%.3f", summary.getAverage()));

        pw.flush();
    }

}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);



    }
}