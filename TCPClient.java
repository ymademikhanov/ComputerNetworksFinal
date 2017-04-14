
import java.io.*;
import java.net.*;

class TCPClient {

    private static Socket clientSocket;
    private static DataOutputStream outToServer;
    private static BufferedReader inFromServer;

    public static String serverIP;
    public static int port;


    public static void main(String argv[]) throws Exception
    {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please, write IP address of server:");
        serverIP = inFromServer.readLine();


        ServerSocket welcomeSocket = new ServerSocket(0);
        port = welcomeSocket.getLocalPort();
        new ClientServerHandler().start();

        for (;;) {
            Socket socket = welcomeSocket.accept();
            new ClientPeerHandler(socket).start();
        }


    }
}