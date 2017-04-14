
import java.io.*;
import java.net.*;

class ClientPeerHandler extends Thread {

    private Socket connectionSocket;
    private BufferedReader inFromPeer;
    private DataOutputStream  outToPeer;

    ClientPeerHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
        try {
            inFromPeer = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToPeer = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (Exception e) { return; }
    }

    @Override
    public void run() {
        try {
            inFromPeer.readLine();
        } catch(Exception e) {

        }

        //"GET file.txt"


    }

}