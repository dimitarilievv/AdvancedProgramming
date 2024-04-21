package task13_Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Component implements Comparable<Component>{
    String color;
    int weight;
    List<Component> components;

    public Component(String color, int weight) {
        components = new ArrayList<>();
        this.color = color;
        this.weight = weight;
    }

    @Override
    public int compareTo(Component o) {
        if(weight == o.weight){
            return color.compareTo(o.color);
        }

        return Integer.compare(weight,o.weight);
    }
    void addComponent(Component component){
        components.add(component);
        components = components.stream().sorted().collect(Collectors.toList());
    }
    public String print(String prefix){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%d:%s\n",prefix,weight,color));
        components.stream().forEach(c->sb.append(c.print(String.format("---%s",prefix))));
        return sb.toString();
    }
    void changeColor(int weight, String color){
        if(this.weight < weight){
            this.color = color;
        }
        components.stream().forEach(c->c.changeColor(weight,color));
    }
}

class Window{
    String ime;
    List<Component>components;
    public Window(String ime) {
        components = new ArrayList<>();
        this.ime = ime;
    }
    void addComponent(int position, Component component) throws InvalidPositionException {//0,2    3->2
        position=position-1;
        if(components.size()<=position){
            components.add(position,component);
            return;
        }
        throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!",position+1));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW "+ ime+"\n");
        for (int i = 0; i < components.size(); i++) {
            sb.append(i+1+":"+components.get(i).print(""));
        }
        return sb.toString();
    }

    void changeColor(int weight, String color){
        components.stream().forEach(c->c.changeColor(weight,color));
    }
    void swichComponents(int pos1, int pos2){// 0 1   dadeni 0 1   0 _ 2 3 4
        pos1--;pos2--;
        Component prv = components.remove(pos1);
        Component vtor = components.remove(pos2-1);

        components.add(pos1,vtor);
        components.add(pos2,prv);
    }
}

class InvalidPositionException extends Exception{
    public InvalidPositionException(String message) {
        super(message);
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

