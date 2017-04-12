
import java.io.*;
import java.net.*;

class Server {

    public static void main(String argv[]) throws Exception
    {
        String rec;
        String cap;
        String send;
        ServerSocket welcomeSocket = new ServerSocket(6789);

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        while(true){
            try{
                rec = inFromClient.readLine();
                cap = rec.toUpperCase() + '\n';
                outToClient.writeBytes(cap);
                if(cap.equals("END")){
                    connectionSocket.close();
                    welcomeSocket.close();
                }
            } catch(Exception e) {
                break;
            }
        }
    }
}
