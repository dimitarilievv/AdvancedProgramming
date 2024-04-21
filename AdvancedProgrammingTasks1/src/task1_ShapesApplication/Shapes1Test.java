package task1_ShapesApplication;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Square{
    int size;

    public Square(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
    public int perimeter(){
        return 4*size;
    }
}
class Canvas{
    String id;
    List<Square> listSquares;

    public Canvas(String id, List<Square> listSquares) {
        this.id = id;
        this.listSquares = listSquares;
    }

    public Canvas() {
        listSquares=new ArrayList<Square>();
        id="";
    }
    int maxPerimeterInOneCanva(){
//        int sum=0;
//        for (Square listSquare : listSquares) {
//            sum += listSquare.perimeter();
//        }
//        return sum;
        return listSquares.stream().mapToInt(i->i.perimeter()).sum();
    }
}
class ShapesApplication{
    List<Canvas> listCanvas;

    public ShapesApplication(List<Canvas> listCanvas) {
        this.listCanvas = listCanvas;
    }

    public ShapesApplication() {
        listCanvas=new ArrayList<Canvas>();
    }
    int readCanvases (InputStream inputStream) {
        Scanner sc=new Scanner(inputStream);
        int counter=0;
        while(sc.hasNextLine()){
            List<Square> listSq=new ArrayList<Square>();
            String []parts=sc.nextLine().split(" ");
            String id=parts[0];
            for (int i = 1; i < parts.length; i++) {
                listSq.add(new Square(Integer.parseInt(parts[i])));
                counter++;
            }
            listCanvas.add(new Canvas(id,listSq));
        }
        return counter;
    }
    void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter pw=new PrintWriter(outputStream);
        int max=0;
        int index=0;
        for (int i = 0; i < listCanvas.size(); i++) {
            if(listCanvas.get(i).maxPerimeterInOneCanva()>max){
                max=listCanvas.get(i).maxPerimeterInOneCanva();
                index=i;
            }
        }
//        listCanvas.stream().mapToInt(i->i.maxPerimeterInOneCanva()).max();
        pw.printf(listCanvas.get(index).id+" "+listCanvas.get(index).listSquares.size()+" "+max);
        pw.flush();
    }
    int maxPerimeterInAllCanvas(){
//        int max=0;
//        String id="";
//        for (int i = 0; i < listCanvas.size(); i++) {
//            if(listCanvas.get(i).maxPerimeterInOneCanva()>max){
//                max=listCanvas.get(i).maxPerimeterInOneCanva();
//            }
//        }
//        return max;
        return listCanvas.stream().mapToInt(i->i.maxPerimeterInOneCanva()).max().getAsInt();
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

