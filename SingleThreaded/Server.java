package SingleThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void run() throws IOException {
        int port = 8081;
        try {
            ServerSocket serverSock = new ServerSocket(port);
            serverSock.setSoTimeout(10000);
            while (true) {
                System.out.println("System Listen on port : " + port);
                Socket clientSock = serverSock.accept();
                System.out.println("A Remote Client Connected : " + clientSock.getRemoteSocketAddress());
                PrintWriter toClient = new PrintWriter(clientSock.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                
                String clientMessage;
                while ((clientMessage = fromClient.readLine()) != null) {
                    System.out.println("Client Message: " + clientMessage);
                    
                    toClient.println("Echo: " + clientMessage);
                }
                
                // Close client connection when client disconnects
                System.out.println("Client disconnected: " + clientSock.getRemoteSocketAddress());
                clientSock.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}