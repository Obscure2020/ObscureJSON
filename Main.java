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

    private static void properPrint(String message){
        String[] lines = message.split("\n");
        for(String l : lines) System.out.println(l);
    }

    public static void main(String[] args) throws Exception {
        JSONobject obj = JSONobject.create();

        JSONobject widget = JSONobject.create();
        obj.put("widget", widget);
        widget.put("debug", JSONstring.create("on"));

        JSONobject window = JSONobject.create();
        widget.put("window", window);
        window.put("title", JSONstring.create("Sample Konfabulator Widget"));
        window.put("name", JSONstring.create("main_window"));
        window.put("width", JSONnumber.create(500));
        window.put("height", JSONnumber.create(500));

        JSONobject image = JSONobject.create();
        widget.put("image", image);
        image.put("src", JSONstring.create("Images/Sun.png"));
        image.put("name", JSONstring.create("sun1"));
        image.put("hOffset", JSONnumber.create(250));
        image.put("vOffset", JSONnumber.create(250));
        image.put("alignment", JSONstring.create("center"));

        JSONobject text = JSONobject.create();
        widget.put("text", text);
        text.put("data", JSONstring.create("Click Here"));
        text.put("size", JSONnumber.create(36));
        text.put("style", JSONstring.create("bold"));
        text.put("name", JSONstring.create("text1"));
        text.put("hOffset", JSONnumber.create(250));
        text.put("vOffset", JSONnumber.create(100));
        text.put("alignment", JSONstring.create("center"));
        text.put("onMouseUp", JSONstring.create("sun1.opacity = (sun1.opacity / 100) * 90;"));

        properPrint(obj.prettyPrint());
    }
}