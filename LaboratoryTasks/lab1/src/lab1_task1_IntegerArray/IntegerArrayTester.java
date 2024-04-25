package lab1_task1_IntegerArray;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

final class IntegerArray{
    private int []niza;

    public IntegerArray(int[] a) {
        this.niza = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            niza[i]=a[i];
        }
    }
    public int length(){
        return niza.length;
    }
    public int getElementAt(int i){
        return niza[i];
    }
    public int sum(){
        return Arrays.stream(niza).sum();
    }
    public double average(){
        return (double) sum() /niza.length;
    }
    public IntegerArray getSorted(){
        return new IntegerArray(Arrays.stream(niza).sorted().toArray());
    }
    public IntegerArray concat(IntegerArray ia){
        int []nova=new int[niza.length+ia.length()];
        for (int i = 0; i < niza.length; i++) {
            nova[i]=niza[i];
        }
        int j=0;
        for(int i=niza.length;i<ia.length()+niza.length;i++){
            nova[i]=ia.getElementAt(j);
            j++;
        }
        return new IntegerArray(nova);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("[");
        for (int i = 0; i < niza.length-1; i++) {//mozno
            sb.append(niza[i]).append(", ");
        }
        sb.append(niza[niza.length-1]).append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerArray that = (IntegerArray) o;
        return Arrays.equals(niza, that.niza);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(niza);
    }
}
class ArrayReader{
    public static IntegerArray readIntegerArray(InputStream input){
        Scanner sc=new Scanner(input);
        int n=sc.nextInt();
        int []niza = new int[n];
        for (int i = 0; i <n; i++) {
            niza[i]=sc.nextInt();
        }
        return new IntegerArray(niza);
    }
}
public class IntegerArrayTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        IntegerArray ia = null;
        switch (s) {
            case "testSimpleMethods":
                ia = new IntegerArray(generateRandomArray(scanner.nextInt()));
                testSimpleMethods(ia);
                break;
            case "testConcat":
                testConcat(scanner);
                break;
            case "testEquals":
                testEquals(scanner);
                break;
            case "testSorting":
                testSorting(scanner);
                break;
            case "testReading":
                testReading(new ByteArrayInputStream(scanner.nextLine().getBytes()));
                break;
            case "testImmutability":
                int a[] = generateRandomArray(scanner.nextInt());
                ia = new IntegerArray(a);
                testSimpleMethods(ia);
                testSimpleMethods(ia);
                IntegerArray sorted_ia = ia.getSorted();
                testSimpleMethods(ia);
                testSimpleMethods(sorted_ia);
                sorted_ia.getSorted();
                testSimpleMethods(sorted_ia);
                testSimpleMethods(ia);
                a[0] += 2;
                testSimpleMethods(ia);
                ia = ArrayReader.readIntegerArray(new ByteArrayInputStream(integerArrayToString(ia).getBytes()));
                testSimpleMethods(ia);
                break;
        }
        scanner.close();
    }

    static void testReading(InputStream in) {
        IntegerArray read = ArrayReader.readIntegerArray(in);
        System.out.println(read);
    }

    static void testSorting(Scanner scanner) {
        int[] a = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        System.out.println(ia.getSorted());
    }

    static void testEquals(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        int[] c = readArray(scanner);
        IntegerArray ia = new IntegerArray(a);
        IntegerArray ib = new IntegerArray(b);
        IntegerArray ic = new IntegerArray(c);
        System.out.println(ia.equals(ib));
        System.out.println(ia.equals(ic));
        System.out.println(ib.equals(ic));
    }

    static void testConcat(Scanner scanner) {
        int[] a = readArray(scanner);
        int[] b = readArray(scanner);
        IntegerArray array1 = new IntegerArray(a);
        IntegerArray array2 = new IntegerArray(b);
        IntegerArray concatenated = array1.concat(array2);
        System.out.println(concatenated);
    }

    static void testSimpleMethods(IntegerArray ia) {
        System.out.print(integerArrayToString(ia));
        System.out.println(ia);
        System.out.println(ia.sum());
        System.out.printf("%.2f\n", ia.average());
    }


    static String integerArrayToString(IntegerArray ia) {
        StringBuilder sb = new StringBuilder();
        sb.append(ia.length()).append('\n');
        for (int i = 0; i < ia.length(); ++i)
            sb.append(ia.getElementAt(i)).append(' ');
        sb.append('\n');
        return sb.toString();
    }

    static int[] readArray(Scanner scanner) {
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
        }
        return a;
    }


    static int[] generateRandomArray(int k) {
        Random rnd = new Random(k);
        int n = rnd.nextInt(8) + 2;
        int a[] = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = rnd.nextInt(20) - 5;
        }
        return a;
    }

}

