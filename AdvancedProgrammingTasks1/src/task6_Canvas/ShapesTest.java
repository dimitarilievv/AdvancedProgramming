package task6_Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}
interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}
abstract class Shape implements Scalable,Stackable{
    private String ID;
    private Color color;

    public Shape(String ID, Color color) {
        this.ID = ID;
        this.color = color;
    }

    public String getID() {
        return ID;
    }

    public Color getColor() {
        return color;
    }
}
class Circle extends Shape{
    private float radius;

    public Circle(String ID, Color color,float radius) {
        super(ID, color);
        this.radius=radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius=scaleFactor*radius;
    }

    @Override
    public float weight() {
        return (float)(radius*radius*Math.PI);
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f",getID(),getColor(),weight());
    }
}
class Rectangle extends Shape{
    private float width;
    private float height;

    public Rectangle(String ID, Color color,float width,float height) {
        super(ID, color);
        this.width=width;
        this.height=height;
    }

    @Override
    public void scale(float scaleFactor) {
        width=width*scaleFactor;
        height=height*scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f",getID(),getColor(),weight());
    }
}
class Canvas{
    private ArrayList<Shape> list;

    public Canvas() {
        this.list = new ArrayList<Shape>();
    }
    void add(String id, Color color, float radius){
        Circle c=new Circle(id,color,radius);
        addToList(c);
    }
    void add(String id, Color color, float width, float height){

        Rectangle r=new Rectangle(id,color,width,height);
        addToList(r);
    }
    public void addToList(Shape s)
    {
        if(list.isEmpty())
        {
            list.add(s);
            return;
        }

        for(int i=0;i<list.size();i++){
            if(list.get(i).weight()<s.weight()){
                list.add(i,s);
                return;
            }
        }
        list.add(s);
    }

    void scale(String id,float scaleFactor){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID().equals(id)) {
                Shape s = list.get(i);

                list.remove(s);
                s.scale(scaleFactor);
                addToList(s);

                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (int i=0;i< list.size();i++) {
            sb.append(list.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

//enum Color {
//    RED, GREEN, BLUE
//}

