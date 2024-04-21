package task25_ShoppingCart;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Product implements Comparable<Product> {
    String type;
    int productID;
    String productName;
    int productPrice;

    public Product(String type, int productID, String productName, int productPrice) {
        this.type = type;
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public Product(String line) {
//        PS;107965;Flour;409;800.78
//        WS;101569;Coca Cola;970;64
        String[] parts = line.split(";");
        this.type = parts[0];
        this.productID = Integer.parseInt(parts[1]);
        this.productName = parts[2];
        this.productPrice = Integer.parseInt(parts[3]);
    }

    @Override
    public int compareTo(Product o) {
        return Integer.compare(this.productPrice, o.productPrice);
    }

    abstract double totalPrice();

    @Override
    public String toString() {
        return String.format("%d - %.2f", productID, totalPrice());
    }

}

class WSProduct extends Product {
    int quantityWS;

    public WSProduct(String type, int productID, String productName, int productPrice, int quantityWS) {
        super(type, productID, productName, productPrice);
        this.quantityWS = quantityWS;
    }

    @Override
    double totalPrice() {
        return quantityWS * productPrice;
    }
//    void discount(){
//        productPrice=productPrice-(productPrice*0.1);
//    }
}

class PSProduct extends Product {
    double quantityPS;

    public PSProduct(String type, int productID, String productName, int productPrice, double quantityPS) throws InvalidOperationException {
        super(type, productID, productName, productPrice);
        if (quantityPS == 0) {
            throw new InvalidOperationException(String.format("The quantity of the product with id %d can not be 0.", productID));
        }
        this.quantityPS = quantityPS;
    }

    @Override
    double totalPrice() {
        return quantityPS / 1000 * productPrice;
    }
}

class ShoppingCart {
    List<Product> products;

    public ShoppingCart(List<Product> products) {
        this.products = products;
    }

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    void addItem(String itemData) throws InvalidOperationException {
        String[] parts = itemData.split(";");
        String type = parts[0];
        int id = Integer.parseInt(parts[1]);
        String name = parts[2];
        int price = Integer.parseInt(parts[3]);
        if (type.equals("PS")) {
            double quantity = Double.parseDouble(parts[4]);
            if (quantity == 0) {
                throw new InvalidOperationException(String.format("The quantity of the product with id %d can not be 0.", id));
            }
            products.add(new PSProduct(type, id, name, price, quantity));
        } else {
            int quantity = Integer.parseInt(parts[4]);
            if (quantity == 0) {
                throw new InvalidOperationException(String.format("The quantity of the product with id %d can not be 0.", id));
            }
            products.add(new WSProduct(type, id, name, price, quantity));
        }
    }

    void printShoppingCart(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
//        products.sort(Product::compareTo);
//        Collections.reverse(products);
//        products.sort(Comparator.comparingInt());
//        products.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        products.sort(Comparator.comparingDouble(Product::totalPrice).reversed());

        for (Product product : products) {
            pw.println(product);
        }
        pw.flush();
    }


    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if(discountItems.isEmpty()){
            throw new InvalidOperationException("There are no products with discount.");
        }
        PrintWriter pw=new PrintWriter(os);
        ArrayList<Product> discounted = new ArrayList<>();
        for (Product product : products) {
            for (Integer discountItem : discountItems) {
                if(product.productID==discountItem){
                    discounted.add(product);
                }
            }
        }
//        Collections.reverse(discounted);
        for (Product product : discounted) {
            double original=product.totalPrice();
            double discount= product.totalPrice()-(product.totalPrice()*0.1);
            pw.println(String.format("%s - %.2f",product.productID,original-discount));
        }
        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}