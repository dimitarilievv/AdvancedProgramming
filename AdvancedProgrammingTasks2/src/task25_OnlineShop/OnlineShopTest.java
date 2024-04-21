package task25_OnlineShop;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int soldQuantity;
    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        soldQuantity=0;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void sell(int quantity) {
        soldQuantity+=quantity;
    }
    public static Comparator<Product> productComparator(COMPARATOR_TYPE comparatorType){
        if(comparatorType==COMPARATOR_TYPE.OLDEST_FIRST){
            return Comparator.comparing(Product::getCreatedAt);
        }else if(comparatorType==COMPARATOR_TYPE.HIGHEST_PRICE_FIRST){
            return Comparator.comparing(Product::getPrice).reversed();
        }else if(comparatorType==COMPARATOR_TYPE.MOST_SOLD_FIRST){
            return Comparator.comparing(Product::getSoldQuantity).reversed();
        }else if(comparatorType==COMPARATOR_TYPE.LEAST_SOLD_FIRST){
            return Comparator.comparing(Product::getSoldQuantity);
        }else if(comparatorType==COMPARATOR_TYPE.LOWEST_PRICE_FIRST){
            return Comparator.comparing(Product::getPrice);
        }else if(comparatorType==COMPARATOR_TYPE.NEWEST_FIRST){
            return Comparator.comparing(Product::getCreatedAt).reversed();
        } else{
            return productComparator(COMPARATOR_TYPE.NEWEST_FIRST);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", price=").append(price);
        sb.append(", quantitySold=").append(soldQuantity);
        sb.append('}');
        return sb.toString();
    }
}


class OnlineShop {
    Map<String,Product> productsById;
    Map<String,Set<Product>> productsByCategory;
    OnlineShop() {
        productsById=new HashMap<>();
        productsByCategory=new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product p=new Product(category, id, name, createdAt, price);
        productsById.putIfAbsent(id,p);
        productsByCategory.putIfAbsent(category,new HashSet<>());
        productsByCategory.get(category).add(p);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if(!productsById.containsKey(id))
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!",id));
        Product p=productsById.get(id);
        p.sell(quantity);
        return p.price*quantity;

    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<Product> sortedProducts;
        if(category==null){
            sortedProducts=productsById.values().stream()
                    .sorted(Product.productComparator(comparatorType))
                    .collect(Collectors.toList());
        }else{
            sortedProducts=productsByCategory.get(category).stream()
                    .sorted(Product.productComparator(comparatorType))
                    .collect(Collectors.toList());
        }
        int index=0;
        for (Product sortedProduct : sortedProducts) {
            if(result.get(index).size()==pageSize){
                result.add(new ArrayList<>());
                index++;
            }
            result.get(index).add(sortedProduct);
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

