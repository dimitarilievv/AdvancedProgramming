package task15_MojDDV;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int totalSum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", totalSum));
    }
}

class Product {
    int price;
    String type;

    public Product(int price, String type) {
        this.price = price;
        this.type = type;
    }

    double ddvBill() {
        if (type.equals("A"))
            return price * 0.18 * 0.15;
        else if (type.equals("B"))
            return price * 0.05 * 0.15;
        else return 0;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}

class Bill {
    int id;
    List<Product> products;

    public Bill(int id, List<Product> products) {
        this.id = id;
        this.products = new ArrayList<>();
    }

    public Bill() {
        this.id=id;
        products =new ArrayList<>();
    }

    public void checkBill() throws AmountNotAllowedException {
        if (totalSum() > 30000) {
            throw new AmountNotAllowedException(totalSum());
        }
    }

    int totalSum() {
//        int sum = 0;
//        for (Proizvod proizvod : proizvodi) {
//            sum += proizvod.price;
//        }
//        return sum;
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    double totalTaxSum() {
//        double sum = 0.0;
//        for (Proizvod proizvod : proizvodi) {
//            sum += proizvod.ddvSmetka();
//        }
//        return sum;
        return products.stream().mapToDouble(Product::ddvBill).sum();
    }
    public void addProduct(Product p){
        products.add(p);
    }

    @Override
    public String toString() {
        return String.format("%d %d %.2f", id, totalSum(), totalTaxSum());
    }

    public void setId(int id) {
        this.id = id;
    }
}

class MojDDV {
    List<Bill> billList;

    public MojDDV(List<Bill> billList) {
        this.billList = new ArrayList<>();
    }

    public MojDDV() {
        billList = new ArrayList<>();
    }

    void readRecords(InputStream inputStream) throws AmountNotAllowedException {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            int id = Integer.parseInt(parts[0]);
            Bill s=new Bill();
            s.setId(id);
            List<Product> products = new ArrayList<>();
            for (int i = 1; i < parts.length; i += 2) {
                s.addProduct(new Product(Integer.parseInt(parts[i]), parts[i + 1]));
            }
            try {
                s.checkBill();
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
                continue;
            }
            billList.add(s);
        }
    }

    void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream));
        for (Bill b : billList) {
            pw.println(b.toString());
        }
        pw.flush();
    }

}

public class MojDDVTest {

    public static void main(String[] args) throws AmountNotAllowedException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}