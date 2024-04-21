package task24_Risk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Risk{
    int processAttacksData (InputStream is){
        Scanner sc=new Scanner(is);
        int wins=0;;
        while(sc.hasNextLine()){
            String []parts=sc.nextLine().split(";");
            String []attacker=parts[0].split(" ");
            String []defender=parts[1].split(" ");
            ArrayList<Integer> att=new ArrayList<>();
            ArrayList<Integer> def=new ArrayList<>();
            att.add(Integer.parseInt(attacker[0]));
            att.add(Integer.parseInt(attacker[1]));
            att.add(Integer.parseInt(attacker[2]));
            def.add(Integer.parseInt(defender[0]));
            def.add(Integer.parseInt(defender[1]));
            def.add(Integer.parseInt(defender[2]));
            //5 3 4  2 4 1
            //5 4 3  4 2 1
            Collections.sort(att);
            Collections.sort(def);
            int winsAttacker=0;
            for(int i=0;i<att.size();i++){
                if(att.get(i)>def.get(i)){
                    winsAttacker++;
                }
            }
            if(winsAttacker==3){
                wins++;
            }
        }
        return wins;
    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}