
import java.io.*;
import java.net.*;

class Server {
    static ServerSocket welcomeSocket;
    static Manager manager;

    public static void main(String argv[]) throws Exception
    {
        manager = new Manager();
        welcomeSocket = new ServerSocket(8888);
        for (;;) {
            Socket socket = welcomeSocket.accept();
            new Handler(socket, manager).start();
        }
    }
}
