package task3_Discounts;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class Price {
    int price;
    int discountedPrice;

    public Price(int price, int discountedPrice) {
        this.price = price;
        this.discountedPrice = discountedPrice;
    }

    int discount() {
        return Math.abs(price - discountedPrice);
    }

    int getPercent() {
        return (int) Math.floor(100.0 - (100.0 / price * discountedPrice));
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", getPercent(), discountedPrice, price);
    }
}

class Store {
    String name;
    List<Price> prices;

    public Store(String name, List<Price> prices) {
        this.name = name;
        this.prices = prices;
    }

    int absoluteSum() {
        return prices.stream()
                .mapToInt(i -> i.discount())
                .sum();
    }

    double averageDiscount() {
        return prices.stream()
                .mapToInt(Price::getPercent)
                .average()
                .orElse(0);
    }

    @Override
    public String toString() {
        String result = prices.stream()
                .sorted(Comparator.comparing(Price::getPercent)
                        .thenComparing(Price::getPrice)
                        .reversed())
                .map(x -> String.format("%s", x))
                .collect(Collectors.joining("\n"));
        return String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n%s", name, averageDiscount(), absoluteSum(), result);
    }
}

class Discounts {
    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream in) {
        Scanner sc = new Scanner(in);
        int counter = 0;
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String[] parts = s.split("\\s+");
            String name = parts[0];
            List<Price> prices = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String[] price = parts[i].split(":");
                prices.add(new Price(Integer.parseInt(price[1]), Integer.parseInt(price[0])));
            }
            stores.add(new Store(name, prices));
            counter++;
        }
        return counter;

    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::averageDiscount).reversed())
                .limit(3).collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::absoluteSum))
                .limit(3)
                .collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
