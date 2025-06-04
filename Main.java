import java.io.*;
import java.util.*;
import ObscureJSON.*;

class Main {
    private static String readWholeFile(File input) throws FileNotFoundException{
        Scanner scan = new Scanner(input);
        StringBuilder sb = new StringBuilder();
        if(scan.hasNext()) sb.append(scan.nextLine());
        while(scan.hasNext()){
            sb.append('\n');
            sb.append(scan.nextLine());
        }
        scan.close();
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        int[] tests = {182700, 100000};
        for(int base : tests){
            for(int i=0; i<30; i++){
                double mult = Math.pow(10, -i);
                double num = base * mult;
                JSONnumber j_num = JSONnumber.create(num);
                String print = j_num.prettyPrint();
                double parse = Double.parseDouble(print);
                System.out.println(num + " -> " + print + " -> " + parse);
            }
        }
    }
}