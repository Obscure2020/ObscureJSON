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

    private static JSONstring prep_str(String prefix, String body){
        return JSONstring.create(prefix + body);
    }

    public static void main(String[] args) throws Exception {
        String short_str = " wheels on the bus...";
        String long_str = " wheels on the bus go round and round";

        System.out.println("============================");
        System.out.println();
        JSONarray arr = JSONarray.create();
        for(int i=1; i<=10; i++){
            arr.add(JSONnumber.create((double) i));
        }
        System.out.println(arr.condensedPrint());
        System.out.println();
        System.out.println(arr.prettyPrint());
        System.out.println();

        System.out.println("============================");
        System.out.println();
        JSONarray uber_arr = JSONarray.create();
        for(int i=0; i<6; i++){
            uber_arr.add(arr);
        }
        System.out.println(uber_arr.condensedPrint());
        System.out.println();
        System.out.println(uber_arr.prettyPrint());
        System.out.println();

        System.out.println("============================");
        System.out.println();
        arr.clear();
        arr.add(prep_str("One", short_str));
        arr.add(prep_str("Two", short_str));
        arr.add(prep_str("Three", short_str));
        arr.add(prep_str("Four", short_str));
        System.out.println(arr.condensedPrint());
        System.out.println();
        System.out.println(arr.prettyPrint());
        System.out.println();

        System.out.println("============================");
        System.out.println();
        arr.clear();
        arr.add(prep_str("One", long_str));
        arr.add(prep_str("Two", long_str));
        arr.add(prep_str("Three", long_str));
        arr.add(prep_str("Four", long_str));
        System.out.println(arr.condensedPrint());
        System.out.println();
        System.out.println(arr.prettyPrint());
        System.out.println();

        System.out.println("============================");
        System.out.println();
        uber_arr.clear();
        for(int i=0; i<6; i++){
            uber_arr.add(arr);
        }
        System.out.println(uber_arr.condensedPrint());
        System.out.println();
        System.out.println(uber_arr.prettyPrint());
    }
}