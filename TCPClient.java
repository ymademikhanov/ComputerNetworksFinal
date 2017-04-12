
import java.io.*;
import java.net.*;

class TCPClient {

    private static Socket clientSocket;
    private static DataOutputStream outToServer;
    private static BufferedReader inFromServer;

    //private find(String fileName);
    //private download(Selected);

    public static void writeResponse(String s) throws Throwable {
        s += "\n";
        outToServer.write(s.getBytes());
        outToServer.flush();
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

    public static void connect() throws Exception, Throwable{
        writeResponse("HELLO");

        String protocol = inFromServer.readLine();
        if(protocol.equals("HI")) {
            System.out.println("Connected.");
        }
        else {
            System.out.println("Wrong protocol!");
            clientSocket.close();
        }
    }

    public static void main(String argv[]) throws Exception
    {
        String send;
        String rec;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        try {
            clientSocket = new Socket("localhost", 8888);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // connect();

        System.out.println("Manual:");
        System.out.print("    find                -- to get list of accessible files or update current one");
        System.out.println("    download: <row>     -- to download file on <row> row\n");

        while(true) {
            String command = inFromUser.readLine();

            if(command.substring(0, 4).equals("find")) {
                //find();
            }
            else if(command.substring(0, 9).equals("download:")) {
                int row = getRow(command);
                System.out.println(row);

                if(row == -1) {
                    System.out.println("Wrong format.git");
                } else {
                    // download(row);
                }
            }
            else {
                System.out.println("Command not found.");
            }
        }
         

    }
}