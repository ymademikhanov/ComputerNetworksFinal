import java.io.*;
import java.net.*;


class Handler extends Thread {
    private Socket connectionSocket;
    private String hello, rec, cap, send;
    private BufferedReader inFromUser, inFromClient;
    private DataOutputStream  outToClient;
    private Manager manager;

    private String ipaddress;

    Handler(Socket connectionSocket, Manager manager) {
        this.connectionSocket = connectionSocket;
        this.manager = manager;
        this.ipaddress = "";
    }

    @Override
    public void run() {

        try {
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (Exception e) { return; }

        try {
            hello = inFromClient.readLine();
        } catch(Exception e) {  return; }

        if (hello.equals("HELLO")) {
            try {
                outToClient.writeBytes("HI\n");
            } catch(Exception e) {  return; }

            while (true) {
                try{
                    String tin = inFromClient.readLine();
                    if (tin.equals("BYE")) {
                        outToClient.writeBytes("BYE\n");
                        connectionSocket.close();
                        manager.delete(ipaddress);
                    } else {
                        String[] tokens = tin.split(" ", 2);
                        if (tokens[0].equals("SEARCH:")) {
                            if (tokens[1].toLowerCase().equals("all")) {
                                cap = "FOUND: " + manager + "\n";
                            } else
                                cap = manager.search(tokens[1]) + "\n";
                            outToClient.writeBytes(cap);
                        }   else
                        if (tokens[0].equals("ADD")) {
                            ipaddress = manager.addFilesFromUser(tokens[1]);
                            cap = "ADDED\n";
                        }
                    }
                } catch(Exception e) {  break;  }
            }
        }
    }
}
