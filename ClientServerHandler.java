
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
    public static List<FailMailFile> listFiles = new ArrayList<FailMailFile>();
    private List<FailMailFile> found = new ArrayList<FailMailFile>();

    private String myIP;
    private int myPort;



    ClientServerHandler() {
        try {
            myIP = InetAddress.getLocalHost().getHostAddress();
            myPort = TCPClient.port;
            System.out.println("Client started at " + myIP + ":" + myPort + "\n");

            connectionSocket = new Socket(TCPClient.serverIP, 8888);
            outToServer = new DataOutputStream(connectionSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        } catch(Exception e) {
            System.out.println("Client-server error. " + e.getMessage());
        }

    }

    public void writeResponse(DataOutputStream  outTo, String s) {
        s += "\n";
        try {
            outTo.write(s.getBytes());
            outTo.flush();
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

    public void addFileToList(File fileEntry) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String file = fileEntry.getName();
        String fileName = getFileName(file);
        String fileExt = getFileExt(file);
        long size = fileEntry.length();
        String date = sdf.format(fileEntry.lastModified());

        FailMailFile item = new FailMailFile(fileName, fileExt,  (int)size, date, myIP, myPort);
        listFiles.add(item);
        writeResponse(outToServer, "ADD " + item);

    }

    public void listFilesForFolder(final File folder) throws Exception{
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                addFileToList(fileEntry);
            }
        }
    }

    public void connect() {
        try {
            writeResponse(outToServer, "HELLO");

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

        final File folder = new File("./share");

        try {
            listFilesForFolder(folder);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void showTable(String information2) {
        if(!information2.substring(0, 5).equals("FOUND")) {
            System.out.println(information2);
            return;
        }

        System.out.println("\nFound:");
        String[] toks = information2.split(" ", 2);
        String information = toks[1];

        String header = String.format("%3s %-40s %-20s %-10s", "#", "Name", "Last Modified", "Size");
        System.out.println(header);

        found.clear();

        String[] tokens = information.split("<");
        int index = 1;

        for (String token : tokens) {
            if (token.length() > 0) {
                String[] sub = token.split("[,>\n]+");

                String name = sub[0].trim();
                String type = sub[1].trim();
                int size = Integer.parseInt(sub[2].trim());
                String lastModifiedDate = sub[3].trim();
                String IPAddress = sub[4].trim();
                int port = Integer.parseInt(sub[5].trim());

                if(IPAddress.equals(myIP) && port == myPort)
                    continue;

                found.add(new FailMailFile(name, type, size, lastModifiedDate, IPAddress, port));
                String item = String.format("%3d %-40s %-20s %-10s", index, name + "." + type, lastModifiedDate, size);
                System.out.println(item);
                index++;
            }
        }
        System.out.println();
    }

    public void download(int row) {
        row--;
        FailMailFile selected = found.get(row);
        String fileName = selected.getName() + '.' + selected.getType();
        String ip = selected.getIPAddress();
        int port = selected.getPort();

        System.out.println("Downloading from " + ip + ":" + port + "\n");

        try {
            Socket peerSocket = new Socket(ip, port);
            BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
            DataOutputStream outToPeer = new DataOutputStream(peerSocket.getOutputStream());

            String request = "GET " + fileName;

            writeResponse(outToPeer, request);
            // creating new file
            File file = new File("./share/g" + fileName);
            file.getParentFile().mkdirs();
            file.createNewFile();

            PrintWriter writer = new PrintWriter("./share/g" + fileName, "UTF-8");

            while(true) {
                String data = inFromPeer.readLine();
                if(data == null) {
                    break;
                }
                writer.println(data);
            }
            writer.close();
            System.out.println("File uploaded.");

            //adding file to list and sending to server
            addFileToList(file);


            peerSocket.close();
        } catch(Exception e) {
            System.out.println("Error with downloading file");
        }
    }


    @Override
    public void run() {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        connect();
        sendInfo();

        System.out.println("Manual:");
        System.out.println("    search:             -- to get list of accessible files or update current one");
        System.out.println("    download: <row>     -- to download file on <row> row");
        System.out.println("    bye                 -- to close application\n");

        while(true) {
            String command = "";

            try {

                command = inFromUser.readLine();

            if(command.length() >= 7 && command.substring(0, 7).equals("search:")) {
                if(command.length() <= 8) {
                    writeResponse(outToServer, "SEARCH: ALL");
                }
                else {
                    writeResponse(outToServer, "SEARCH: " + command.substring(8));
                }
                showTable(inFromServer.readLine());
            }
            else if(command.length() >= 9 && command.substring(0, 9).equals("download:")) {
                int row = getRow(command);
                System.out.println(row);

                if(row == -1) {
                    System.out.println("Wrong format.git");
                } else {
                    download(row);
                }
            }
            else if(command.length() >= 3 && command.substring(0, 3).equals("bye")) {
                writeResponse(outToServer, "BYE");
                connectionSocket.close();
                break;
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