package task27_Risk2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Risk {
    void processAttacksData(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String[] parts = sc.nextLine().split(";");
            String first = parts[0];
            String second = parts[1];
            ArrayList<Integer> left = new ArrayList<>();
            ArrayList<Integer> right = new ArrayList<>();
            String[] firstParts = first.split(" ");
            left.add(Integer.parseInt(firstParts[0]));
            left.add(Integer.parseInt(firstParts[1]));
            left.add(Integer.parseInt(firstParts[2]));
            String[] secondParts = second.split(" ");
            right.add(Integer.parseInt(secondParts[0]));
            right.add(Integer.parseInt(secondParts[1]));
            right.add(Integer.parseInt(secondParts[2]));
            Collections.sort(left);
            Collections.reverse(left);
            Collections.sort(right);
            Collections.reverse(right);
            // 3 4 5  1 2 4  3 0
            // 5 4 3  5 2 1  2 1 vtoriot
            int winsLeft=0;
            int winsRight=0;
            for(int i=0;i<left.size();i++){
                if(left.get(i)>right.get(i)){
                    winsLeft++;
                }else if(left.get(i)<right.get(i)){
                    winsRight++;
                }else{
                    winsRight++;
                }
            }
            System.out.println(winsLeft+" "+winsRight);
        }
    }
}

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
