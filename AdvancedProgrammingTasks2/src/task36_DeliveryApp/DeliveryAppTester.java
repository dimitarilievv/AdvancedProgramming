package task36_DeliveryApp;

import java.util.*;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class Address {
    private String name;
    private Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}

class User {
    private String ID;
    private String name;
    private Map<String, Address> addresses;
    private List<Float> moneySpent;

    public User(String ID, String name) {
        this.ID = ID;
        this.name = name;
        this.addresses = new HashMap<>();
        this.moneySpent = new ArrayList<>();
    }

    public double totalMoneySpent() {
        return moneySpent.stream().mapToDouble(i -> i).sum();
    }

    public double averageMoneySpent() {
        if (moneySpent.isEmpty()) {
            return 0;
        }
        return totalMoneySpent() / moneySpent.size();
    }
    public void addAddress(String name, Location location) {
        addresses.put(name, new Address(name, location));
    }

    public void processOrder(float cost) {
        moneySpent.add(cost);
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public List<Float> getMoneySpent() {
        return moneySpent;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                ID, name, moneySpent.size(), totalMoneySpent(), averageMoneySpent());
    }


}

class DeliveryPerson {
    private final String id;
    private final String name;
    private Location currLocation;
    private final List<Float> moneyEarned;

    public DeliveryPerson(String id, String name, Location currLocation) {
        this.id = id;
        this.name = name;
        this.currLocation = currLocation;
        moneyEarned = new ArrayList<>();
    }

    public double totalMoneyEarned() {
        return moneyEarned.stream().mapToDouble(i -> i).sum();
    }

    public double averageMoneyEarned() {
        if (moneyEarned.isEmpty()) {
            return 0;
        }
        return totalMoneyEarned() / moneyEarned.size();
    }

    public int compareDistanceToRestaurant(DeliveryPerson other, Location restaurantLocation) {
        int currentDistance = currLocation.distance(restaurantLocation);
        int otherDistance = other.currLocation.distance(restaurantLocation);
        if (currentDistance == otherDistance) {
            //Во случај да има повеќе доставувачи кои се најблиску до
            // ресторанот - се избира доставувачот со најмалку извршени достави досега.
            return Integer.compare(this.moneyEarned.size(), other.moneyEarned.size());
        } else {
            return currentDistance - otherDistance;
        }
    }

    public void processOrder(int distance, Location location) {
        currLocation = location;
        moneyEarned.add((float) (90 + (distance / 10) * 10));
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f"
                , id, name, moneyEarned.size(), totalMoneyEarned(), averageMoneyEarned());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrLocation() {
        return currLocation;
    }

    public List<Float> getMoneyEarned() {
        return moneyEarned;
    }

    public void setCurrLocation(Location currLocation) {
        this.currLocation = currLocation;
    }
}

class Restaurant {
    private String name;
    private String id;
    private Location location;

    private final List<Float> moneyEarned;

    public Restaurant(String id, String name, Location location) {
        this.name = name;
        this.id = id;
        this.location = location;
        moneyEarned = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public double totalMoneyEarned() {
        return moneyEarned.stream().mapToDouble(i -> i).sum();
    }

    public double averageMoneyEarned() {
        if (moneyEarned.isEmpty()) {
            return 0;
        }
        return totalMoneyEarned() / moneyEarned.size();
    }

    public void processOrder(float cost) {
        moneyEarned.add(cost);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, moneyEarned.size(), totalMoneyEarned(), averageMoneyEarned());
    }
}

class DeliveryApp {
    private final String name;
    private final Map<String, User> users;
    private final Map<String, DeliveryPerson> deliveryPeople;
    private final Map<String, Restaurant> restaurants;

    public DeliveryApp(String name) {
        this.name = name;
        users = new HashMap<>();
        deliveryPeople = new HashMap<>();
        restaurants = new HashMap<>();
    }

    public void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }

    public void registerDeliveryPerson(String id, String name, Location location) {
        deliveryPeople.put(id, new DeliveryPerson(id, name, location));
    }

    public void addRestaurant(String id, String name, Location location) {
        restaurants.put(id, new Restaurant(id, name, location));
    }

    public void addAddress(String id, String name, Location location) {
        users.get(id).addAddress(name, location);
    }


    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::totalMoneySpent).thenComparing(User::getID).reversed())
                .forEach(i -> System.out.println(i));
    }

    public void printRestaurants() {
        restaurants.values().stream()
                .sorted(Comparator.comparing(Restaurant::averageMoneyEarned).thenComparing(Restaurant::getId).reversed())
                .forEach(i -> System.out.println(i));
    }

    public void printDeliveryPeople() {
        deliveryPeople.values().stream()
                .sorted(Comparator.comparing(DeliveryPerson::totalMoneyEarned).thenComparing(DeliveryPerson::getId).reversed())
                .forEach(i -> System.out.println(i));
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User user = users.get(userId);
        Address address = user.getAddresses().get(userAddressName);
        Restaurant restaurant = restaurants.get(restaurantId);

        DeliveryPerson deliveryPerson = deliveryPeople.values().stream()
                .min((l, r) -> l.compareDistanceToRestaurant(r, restaurant.getLocation())).get();

        int distance = deliveryPerson.getCurrLocation().distance(restaurant.getLocation());
        deliveryPerson.processOrder(distance, address.getLocation());
        user.processOrder(cost);
        restaurant.processOrder(cost);
    }

}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}

