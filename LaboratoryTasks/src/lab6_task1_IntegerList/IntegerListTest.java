package lab6_task1_IntegerList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class IntegerList{
    private List<Integer> integerList;

    public IntegerList() {
        integerList=new ArrayList<Integer>();
    }
    public IntegerList(Integer ...numbers){
        integerList=new ArrayList<>(Arrays.asList(numbers));
    }
    void add(int el, int idx){
        if(idx>=integerList.size()){
//            int length=idx-integerList.size();
//            for (int i = 0; i < length; i++) {
//                integerList.add(0);
//            }
//            integerList.add(el);
            IntStream.range(integerList.size(),idx)
                    .forEach(i->integerList.add(i,0));
//            while(idx>integerList.size()){
//                integerList.add(0);
//            }
        }
        integerList.add(idx,el);
    }
    int remove(int idx){
        checkIndex(idx);
        int element=integerList.get(idx);
        integerList.remove(idx);
        return element;
    }
    void set(int el,int idx){
        checkIndex(idx);
        integerList.set(idx,el);
    }
    int get(int idx){
        checkIndex(idx);
        return integerList.get(idx);
    }
    int count(int el){
        return (int) integerList.stream()
                .filter(i -> i.equals(el))
                .count();
    }
    void removeDuplicates(){
        //1vo
//        TreeSet<Integer> to_remove = new TreeSet<Integer>();
//        for ( Iterator<Integer> it = list.descendingIterator(); it.hasNext() ; ) {
//            int k = it.next();
//            if ( to_remove.contains(k) ) it.remove();
//            else if ( count(k) >= 2 ) to_remove.add(k);
        //2ro
//        for(int i = arr.size() - 1; i >= 1; i--) {
//            for(int j = i - 1; j >= 0; j--) {
//                if(Objects.equals(arr.get(i), arr.get(j))) {
//                    arr.remove(j);
//                    j++;
//                    break;
//                }
//            }
//        }
        Collections.reverse(integerList);
        integerList=integerList.stream()
                .distinct().collect(Collectors.toList());
        Collections.reverse(integerList);
    }
    int sumFirst(int k){
//        int sum=0;
//        for(int i=0;i<k;i++){
//          sum+=integerList.get(i);
//        }
//        return sum;
        return integerList.stream()
                .limit(k)
                .mapToInt(i->i)
                .sum();
    }
    int sumLast(int k){
        return integerList.stream()
                .skip((long)integerList.size()-k)
                .mapToInt(i->i)
                .sum();
    }
    void shiftRight(int idx, int k){
        checkIndex(idx);
        int target=(idx+k)%integerList.size();
        if(target<0)target=integerList.size()+target;
        Integer el=integerList.remove(idx);
        integerList.add(target,el);
    }
    void shiftLeft(int idx, int k){
        checkIndex(idx);
        int target=(idx-k)%integerList.size();
        if(target<0)target=integerList.size()+target;
        Integer el=integerList.remove(idx);
        integerList.add(target,el);
    }
    IntegerList addValue(int value){
        IntegerList il=new IntegerList();
        il.integerList= integerList.stream()
                .map(i->i+value)
                .collect(Collectors.toList());
        return il;
    }
    void checkIndex(int idx){
        if(idx<0 || idx>=integerList.size())
            throw new ArrayIndexOutOfBoundsException();
    }

    public int size() {
        return integerList.size();
    }
}
public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}