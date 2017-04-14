
import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class Server {
    static ServerSocket welcomeSocket;
    static Manager manager;

    public static void main(String argv[]) throws Exception {
        welcomeSocket = new ServerSocket(8888);
        manager = new Manager();

        System.out.println("Server runs at " + InetAddress.getLocalHost().getHostAddress());
        for (;;) {
            Socket socket = welcomeSocket.accept();
            new Handler(socket, manager).start();
        }
    }
}
