
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.lang.*;

class ClientServerHandler extends Thread {
    private Socket connectionSocket;
    private BufferedReader inFromUser, inFromServer;
    private DataOutputStream  outToServer;
    private List<FailMailFile> listFiles = new ArrayList<FailMailFile>();
    private List<FailMailFile> found = new ArrayList<FailMailFile>();

    private String myIP;
    private final int myPort = 8888;



    ClientServerHandler() {
    }

    public void writeResponse(String s) {
        s += "\n";
        try {
            outToServer.write(s.getBytes());
            outToServer.flush();
        } catch(Exception e) {}
    }

    public static int getRow(String command) {
        boolean started = false;
        int number = 0;

        for(int j = 10; j < command.length(); j++) {
            char ch = command.charAt(j);
            if(ch == ' ' || ch == '\n') {
                break;
            }
            if('0' <= ch && ch <= '9') {
                started = true;
                number = number * 10 + (int)(ch - '0');
            }
            else {
                return -1;
            }
        }
        if(!started)
            return -1;
        return number;
    }

    public String getFileName(String str) {
        String result = "";
        for(int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.')
                break;
            result += str.charAt(i);
        }
        return result;
    }

    public String getFileExt(String str) {
        String result = "";
        int i = 0;
        while(str.charAt(i) != '.') i++;

        return str.substring(i + 1);
    }

    public void listFilesForFolder(final File folder) throws Exception{
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String file = fileEntry.getName();
                String fileName = getFileName(file);
                String fileExt = getFileExt(file);
                long size = fileEntry.length();
                String date = sdf.format(fileEntry.lastModified());

                FailMailFile item = new FailMailFile(fileName, fileExt,  (int)size, date, myIP, myPort);
                listFiles.add(item);

            }
        }
    }

    public void connect() {
        try {
            writeResponse("HELLO");

            String protocol = inFromServer.readLine();
            if (protocol.equals("HI")) {
                System.out.println("Connected.");
            } else {
                System.out.println("Wrong protocol!");
                connectionSocket.close();
            }
        }catch(Exception e) {}
    }

    public void sendInfo() {
        String response = "ADD ";

        final File folder = new File("./share");

        try {
            listFilesForFolder(folder);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        for(FailMailFile item: listFiles)
            response += item;

        try {
            writeResponse(response);
            showTable(response);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void showTable(String s) {
//        if(!s.substring(0, 5).equals("FOUND")) {
//            System.out.println(s);
//            return;
//        }
        System.out.println("\nFound:");

        String header = String.format("%-40s %-20s %-10s", "Name", "Last Modified", "Size");
        System.out.println(header);

        found.clear();
        String name = "";
        String type = "";
        String date = "";
        String size = "";
        String ip = "";
        String port = "";

        int start = 0;
        int cnt = 0;

        for(int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);
            if(x == '<') {
                start = 1;
            }
            else if(x == '>') {
                String item = String.format("%-40s %-20s %-10s", name + "." + type, date, size);
                System.out.println(item);
                FailMailFile f = new FailMailFile(name, type, Integer.ParseInt(size), date, ip, Integer.ParseInt(port));

                name = type = date = size = ip = port = "";
                start = 0;
                cnt = 0;
            }
            else if(x == ',') {
                cnt++;
            }
            else {
                if(start == 1) {
                    if(cnt == 0)
                        name += x;
                    if(cnt == 1)
                        type += x;
                    if(cnt == 2)
                        size += x;
                    if(cnt == 3)
                        date += x;
                    if(cnt == 4)
                        ip += x;
                    if(cnt == 5)
                        port += x;
                }
            }
        }
        System.out.println();
    }

    public void download(int row) {
        // TO-DO
    }


    @Override
    public void run() {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        try {
            myIP = InetAddress.getLocalHost().getHostAddress();

            connectionSocket = new Socket("localhost", 8888);
            outToServer = new DataOutputStream(connectionSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        //connect();
        sendInfo();

        System.out.println("Manual:");
        System.out.println("    SEARCH:             -- to get list of accessible files or update current one");
        System.out.println("    download: <row>     -- to download file on <row> row\n");

        while(true) {
            String command = "";

            try {

                command = inFromUser.readLine();

            if(command.substring(0, 6).equals("SEARCH:")) {
                writeResponse(command);
                showTable(inFromServer.readLine());
            }
            else if(command.substring(0, 9).equals("download:")) {
                int row = getRow(command);
                System.out.println(row);

                if(row == -1) {
                    System.out.println("Wrong format.git");
                } else {
                    download(row);
                }
            }
            else {
                System.out.println("Command not found.");
            }

            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }


    }


}