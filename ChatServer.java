import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<Client> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);

        while (true) {
            System.out.println("Waiting...");
            try {
                Socket socket = server.accept();
                System.out.println("Client connected!");
                Client client = new Client(socket);
                clients.add(client);
                Thread thread = new Thread(client);
                thread.start();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void broadcast(String message, Client sender) {
        for (Client client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public static void removeClient(Client client) {
        clients.remove(client);
        System.out.println("Client disconnected. Total clients: " + clients.size());
    }
}