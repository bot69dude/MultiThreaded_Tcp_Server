package SingleThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void run() throws IOException, UnknownHostException{
        int port = 8081;
        InetAddress address = InetAddress.getByName("localhost");
        Socket socket = new Socket(address ,port);
        
        PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Connected to server. Type 'quit' to exit.");
        
        while(true){
            System.out.print("Enter message: ");
            String userInput = fromUser.readLine();
            
            if(userInput.equalsIgnoreCase("quit")){
                break;
            }
        
            toServer.println(userInput);
            
            String response = fromServer.readLine();
            System.out.println("Server Response: " + response);
        }
        
        socket.close();
    }
    public static void main(String[] args){
        try {
            run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Client finished execution.");
    }
}
