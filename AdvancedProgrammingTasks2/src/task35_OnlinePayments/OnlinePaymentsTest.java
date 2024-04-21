package task35_OnlinePayments;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class Item{
    String item_name;
    int price;

    public Item(String item_name, int price) {
        this.item_name = item_name;
        this.price = price;
    }

    public String getItem_name() {
        return item_name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s %d",item_name,price);
    }
}
class OnlinePayments{
    Map<String, List<Item>> map;

    public OnlinePayments() {
        map=new HashMap<>();
    }

    public void readItems(InputStream in) {
        Scanner sc=new Scanner(in);
        while (sc.hasNextLine()){
            String line=sc.nextLine();
            String []parts=line.split(";");
            String index=parts[0];
            String name=parts[1];
            int price=Integer.parseInt(parts[2]);
            map.computeIfAbsent(index,x->new ArrayList<>());
            map.computeIfPresent(index,(k,v)->{
                v.add(new Item(name,price));
                return v;
            });
        }
    }
    public void printStudentReport(String id, PrintStream out) {
        PrintWriter pw=new PrintWriter(out);
        if(!map.containsKey(id)) {
            pw.println("Student " + id + " not found!");
            pw.flush();
            return;
        }
        pw.write(String.format("Student: %s Net: %d Fee: %d Total: %d\nItems:\n",id,net(id),fee(id),net(id)+fee(id)));
        List<Item> items = map.get(id).stream()
                .sorted(Comparator.comparing(Item::getPrice).reversed())
                .collect(Collectors.toList());
        IntStream.range(0, items.size())
                .forEach(i -> pw.println((i + 1) + ". " + items.get(i)));
        pw.flush();
    }
    int net(String id){
        List<Item> list=map.get(id);
        return list.stream().mapToInt(i->i.getPrice()).sum();
    }
    int fee(String id){
        int f= (int) Math.round(net(id)*0.0114);
        f= Math.max(3, Math.min(300, f));
        return f;
    }
}
public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}