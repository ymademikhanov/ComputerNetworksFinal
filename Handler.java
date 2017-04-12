import java.io.*;
import java.net.*;


class Handler extends Thread {
    Socket connectionSocket;
    String rec, cap, send;
    BufferedReader inFromUser, inFromClient;
    DataOutputStream  outToClient;
    Manager m;

    Handler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
        this.m = new Manager();
    }

    @Override
    public void run() {
        try {
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (Exception e) {
            return;
        }

        String hello;
        try {
            hello = inFromClient.readLine();
        } catch(Exception e) {
            return;
        }

        if (hello.equals("HELLO")) {
            try {
                outToClient.writeBytes("HI\n");
            } catch(Exception e) {
                return;
            }

            while (true) {
                try{
                    rec = inFromClient.readLine();
                    cap = rec.toUpperCase() + '\n';
                    outToClient.writeBytes(cap);

                    if (cap.equals("END")) {
                        connectionSocket.close();
                    }
                } catch(Exception e) {
                    break;
                }
            }
        }
    }
}
