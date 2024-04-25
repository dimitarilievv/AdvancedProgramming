package lab3_task1_Pizza;

// package PizzaOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException(String message) {
        super(message);
    }
}
class InvalidPizzaTypeException extends Exception{
    public InvalidPizzaTypeException(String message) {
        super(message);
    }
}
class ItemOutOfStockException extends Exception{
    public ItemOutOfStockException(String message) {
        super(message);
    }
}
class EmptyOrder extends Exception{
    public EmptyOrder(String message) {
        super(message);
    }
}
class OrderLockedException extends Exception{
    public OrderLockedException(String message) {
        super(message);
    }
}
interface Item{
    int getPrice();
    String getType();
}
class ExtraItem implements Item{
    String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(type.equals("Coke") || type.equals("Ketchup")){
            //(!Objects.equals(type,"Coke") && !Objects.equals(type,"Ketchup"))
            this.type = type;
        }else{
            throw new InvalidExtraTypeException("InvalidExtraTypeException");
        }
    }

    @Override
    public int getPrice() {
        if(type.equals("Ketchup")) return 3;
        else if(type.equals("Coke")) return 5;
        else return 0;
    }

    @Override
    public String getType() {
        if(type.equals("Ketchup")) return "Ketchup";
        else if(type.equals("Coke")) return "Coke";
        else return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
class PizzaItem implements Item{
    String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(type.equals("Standard") || type.equals("Pepperoni")||
                type.equals("Vegetarian")){
            this.type = type;
        }else{
            throw new InvalidPizzaTypeException("InvalidPizzaTypeException");
        }
    }

    @Override
    public int getPrice() {
        if(type.equals("Standard")) return 10;
        else if (type.equals("Pepperoni")) return 12;
        else if(type.equals("Vegetarian")) return 8;
        else return 0;
    }

    @Override
    public String getType() {
        if(type.equals("Standard")) return "Standard";
        else if (type.equals("Pepperoni")) return "Pepperoni";
        else if(type.equals("Vegetarian")) return "Vegetarian";
        else return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
class Product{
    Item item;
    int count;

    public Product(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
class Order{
    List<Product> productList;
    private boolean locked;
    public Order() {
        this.productList=new ArrayList<Product>();
        this.locked=false;
    }
    void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(count>10){
            throw new ItemOutOfStockException("Item"+item+"out of stock");
        }
        if(locked){
            throw new OrderLockedException("OrderLockedException");
        }
        for (Product product : productList) {
            if(product.item.equals(item)){
                product.setCount(count);
                return;
            }
        }
        productList.add(new Product(item,count));
    }
    public int getPrice(){
        return  productList.stream()
                .mapToInt(i -> i.item.getPrice()*i.getCount())
                .sum();
    }
    public void displayOrder(){
        for (int i = 0; i < productList.size(); i++) {
            System.out.println(String.format("%3d.%-15sx%2d%5d$",i+1,
                    productList.get(i).item.getType(),productList.get(i).count,productList.get(i).item.getPrice()*productList.get(i).count));
        }
        System.out.println(String.format("%-22s%5d$","Total:",getPrice()));
    }
    void removeItem(int i) throws OrderLockedException {
        if(i<0||i>=productList.size()){
            throw new ArrayIndexOutOfBoundsException(i);
        }
        if(locked){
            throw new OrderLockedException("OrderLockedException");
        }
        productList.remove(i);
    }
    void lock() throws EmptyOrder {
        if(productList.isEmpty()){
            throw new EmptyOrder("Empty Order");
        }else{
            locked=true;
        }
    }

}
public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}