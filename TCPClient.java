
import java.io.*;
import java.net.*;

class TCPClient {

    private static Socket clientSocket;
    private static DataOutputStream outToServer;
    private static BufferedReader inFromServer;

    //private find(String fileName);
    //private download(Selected);

    public static void main(String argv[]) throws Exception
    {


        new ClientServerHandler().start();
        ServerSocket welcomeSocket = new ServerSocket(8888);

        for (;;) {
            Socket socket = welcomeSocket.accept();
            new ClientPeerHandler(socket).start();
        }


    }
}