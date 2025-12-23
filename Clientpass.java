import java.io.*;
import java.net.*;

public class Clientpass {
    public static void main(String[] args) throws Exception {
        // Yahan "localhost" ki jagah apne PaaS ka URL ya Public IP dalein
        String host = "localhost"; 
        int port = 5000;

        System.out.println("Connecting to " + host + "...");
        Socket socket = new Socket(host, port);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        String name = kb.readLine();
        out.println(name);

        // Thread for receiving messages
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("\n" + msg);
                }
            } catch (Exception e) {
                System.out.println("Disconnected from server.");
            }
        }).start();

        // Main thread for sending messages
        String text;
        while ((text = kb.readLine()) != null) {
            out.println(text);
        }
    }
}