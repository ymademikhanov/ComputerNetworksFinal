
import java.io.*;
import java.net.*;

class TCPClient {

    private static Socket clientSocket;
    private static DataOutputStream outToServer;
    private static BufferedReader inFromServer;

    public static int port;

    public static void main(String argv[]) throws Exception
    {

        ServerSocket welcomeSocket = new ServerSocket(0);
        port = welcomeSocket.getLocalPort();
        new ClientServerHandler().start();

        for (;;) {
            Socket socket = welcomeSocket.accept();
            new ClientPeerHandler(socket).start();
        }


    }
}