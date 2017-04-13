import java.io.*;
import java.net.*;


class Handler extends Thread {
    private Socket connectionSocket;
    private String hello, rec, cap, send;
    private BufferedReader inFromUser, inFromClient;
    private DataOutputStream  outToClient;
    private Manager manager;

    Handler(Socket connectionSocket, Manager manager) {
        this.connectionSocket = connectionSocket;
        this.manager = manager;
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
                    String[] tokens = inFromClient.readLine().split(" ");
                    if (tokens[0].equals("SEARCH:")) {
                        System.out.println("IN");
                        cap = manager.search(tokens[1]) + "\n";
                    }   else
                    if (tokens[0].equals("ADD")) {
                        manager.addFilesFromUser(tokens[1]);
                        cap = "ADDED\n";
                    }
                    outToClient.writeBytes(cap);
                    if (cap.equals("END"))
                        connectionSocket.close();
                } catch(Exception e) {  break;  }
            }
        }
    }
}
