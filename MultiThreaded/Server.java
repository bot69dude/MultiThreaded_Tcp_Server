package MultiThreaded;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Server {

    private static final int DEFAULT_PORT = 8088;
    private static final AtomicLong requestCounter = new AtomicLong(0);
    private static final boolean VERBOSE_LOGGING = false; // Set to true for debugging
    
    public static void handleClient(Socket clientSock) {
        long requestId = requestCounter.incrementAndGet();
        
        if (VERBOSE_LOGGING) {
            System.out.println("Request #" + requestId + " from " + clientSock.getRemoteSocketAddress());
        }
        
        try (
            PrintWriter toClient = new PrintWriter(clientSock.getOutputStream(), true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSock.getInputStream()))
        ) {
            // Read the first line from client (if any)
            String clientMessage = fromClient.readLine();
            
            // Send immediate response - no need to wait for more input
            String response = "Echo Response #" + requestId + ": " + 
                            (clientMessage != null ? clientMessage : "Hello from server!");
            toClient.println(response);
            
            if (VERBOSE_LOGGING) {
                System.out.println("Responded to request #" + requestId);
            }
            
        } catch (IOException e) {
            if (VERBOSE_LOGGING) {
                System.err.println("Error handling request #" + requestId + ": " + e.getMessage());
            }
        } finally {
            try {
                clientSock.close();
            } catch (IOException ignored) {}
        }
        
        // Log every 1000 requests for performance monitoring
        if (requestId % 1000 == 0) {
            System.out.println("Processed " + requestId + " requests");
        }
    }

    public static void runServer(int port) {
        System.out.println("Starting optimized TCP Echo Server on port " + port + "...");
        System.out.println("Server optimized for high-throughput testing (10k+ requests)");
        System.out.println("Verbose logging: " + VERBOSE_LOGGING);
        
        try (ServerSocket serverSock = new ServerSocket(port);
             ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            // Optimize ServerSocket for high throughput
            serverSock.setReuseAddress(true);
            
            System.out.println("Server ready to accept connections...");
            
            while (true) {
                Socket clientSocket = serverSock.accept();
                
                // Optimize client socket for quick response
                clientSocket.setTcpNoDelay(true);
                clientSocket.setSoTimeout(30000); // 30 second timeout

                executor.submit(() -> handleClient(clientSocket));
            }

        } catch (IOException ex) {
            System.err.println("Server error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        // Allow port to be passed as a command-line argument
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port. Using default: " + DEFAULT_PORT);
            }
        }

        runServer(port);
    }
}
