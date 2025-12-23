import java.io.*;
import java.net.*;
import java.util.*;

public class Serverpass {
    static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // PaaS platforms port assign karte hain, agar nahi mile to 5000 use karega
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "5000"));
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server started on port: " + port);

        while (true) {
            Socket socket = server.accept();
            ClientHandler c = new ClientHandler(socket);
            clients.add(c);
            new Thread(c).start();
        }
    }

    static class ClientHandler implements Runnable {
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String name;

        ClientHandler(Socket s) throws Exception {
            socket = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        }

        public void run() {
            try {
                name = in.readLine();
                System.out.println(name + " joined");
                sendAll(name + " joined");

                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println(name + ": " + msg);
                    sendAll(name + ": " + msg);
                }
            } catch (Exception e) {
                // Connection lost handling
            } finally {
                clients.remove(this);
                System.out.println(name + " left");
                sendAll(name + " left");
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }

    static void sendAll(String msg) {
        for (ClientHandler c : clients) {
            c.out.println(msg);
        }
    }
}