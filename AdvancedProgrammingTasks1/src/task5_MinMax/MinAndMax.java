package task5_MinMax;

import java.util.Comparator;
import java.util.Scanner;
//class T{
//    int min;
//    int max;
//
//    public T(int min, int max) {
//        this.min = min;
//        this.max = max;
//    }
//}
class MinMax<T extends Comparable<T>>{
    T min;
    T max;
    private int updates;
    private int countMin;
    private int countMax;

    public MinMax() {
        updates=0;
        countMin=0;
        countMax=0;
    }
    void update(T element){
        if(updates==0){
            max=element;
            min=element;
        }
        if(min.compareTo(element)>0){
            min=element;
            countMin=1;
        }else if(min.compareTo(element)==0){
            countMin++;
        }

        if(max.compareTo(element)<0){
            max=element;
            countMax=1;
        }else if(max.compareTo(element)==0){
            countMax++;
        }

        updates++;
    }
    public T max(){
        return max;
    }
    public T min(){
        return min;
    }

    public String toString() {
        return min + " " + max + " " + (updates - countMin - countMax) + "\n";
    }
}
public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}