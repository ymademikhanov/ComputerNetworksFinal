
import java.io.*;
import java.net.*;
import java.nio.file.Files;

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

    public void writeResponse(String s) {
        s += "\n";
        try {
            outToPeer.write(s.getBytes());
            outToPeer.flush();
        } catch(Exception e) {}
    }

    @Override
    public void run() {
        try {
            String request = inFromPeer.readLine();
            if(!request.substring(0, 3).equals("GET")) {
                connectionSocket.close();
            }
            String[] toks = request.split(" ", 2);
            String fileName = toks[1];

            for(FailMailFile x: ClientServerHandler.listFiles) {
                String name = x.getName() + '.' + x.getType();
                if(fileName.equals(name)) {
                    System.out.println("Send:" + name);

                    File file = new File("./share/" + fileName);
                    String content = new String(Files.readAllBytes(file.toPath()));

                    outToPeer.writeBytes(content + "\r\n");
                    outToPeer.flush();
                    break;
                }
            }

            connectionSocket.close();
        } catch(Exception e) {

        }
    }

}