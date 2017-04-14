
import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class Server {
    static ServerSocket welcomeSocket;
    static Manager manager;
    static boolean running = false;
    static  JButton startButton = new JButton("start");

    static class ButtonEventListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!running) {
                manager = new Manager();
                running = true;
                try {
                    welcomeSocket = new ServerSocket(8888);
                    for (;;) {
                        Socket socket = welcomeSocket.accept();
                        new Handler(socket, manager).start();
                    }
                } catch (Exception ee) {
                    return;
                }
            }
		}
	}

    public static void main(String argv[]) throws Exception
    {
        JFrame app = new JFrame();
        JPanel panel = new JPanel();

        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(400, 300);
        app.setVisible(true);
        app.add(panel);

        app.getContentPane().setLayout(new FlowLayout());

        JTextField t = new JTextField("Enter server address", 8);
        t.setHorizontalAlignment(JTextField.RIGHT);

        app.getContentPane().add(t);
        startButton.addActionListener(new ButtonEventListener());
        startButton.setText("Start server at " + InetAddress.getLocalHost().getHostAddress());
        panel.add(startButton);
    }
}
