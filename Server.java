
import java.io.*;
import java.net.*;

class Server {
    static ServerSocket welcomeSocket;
    public static void main(String argv[]) throws Exception
    {
        welcomeSocket = new ServerSocket(6789);

        for (;;) {
            Socket socket = welcomeSocket.accept();
            new Handler(socket).start();
        }

    }
}
