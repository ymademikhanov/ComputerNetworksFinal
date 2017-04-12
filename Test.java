import java.util.*;
import java.io.*;
import java.net.*;

class Test {
    public static void main(String argv[]) {
        String information = "<We are blank never space getting back together,,,,mp3,400000,15/04/2012,192.123.23.2,30000>,\n<Blank Space Forever,mp3,1000000,15/04/2016,192.100.100.10,70000>,\n<I dont blank Wanna space Libe Forever,mp4,99999999,15/01/2017,100.100.10.0,8080>";
        Manager m = new Manager();
        m.addFilesFromUser(information);
        System.out.println(m.search("forever"));
        
    }
}
