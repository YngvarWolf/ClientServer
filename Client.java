import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    private Socket socket;
    private PrintStream out;
    private String name;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            Scanner in = new Scanner(is);
            out = new PrintStream(os);

            out.println("Welcome to the chat! Please enter your name:");
            name = in.nextLine();
            ChatServer.broadcast(name + " has joined the chat!", this);

            String input;
            while (!(input = in.nextLine()).equals("bye")) {
                ChatServer.broadcast(name + ": " + input, this);
            }

            ChatServer.broadcast(name + " has left the chat!", this);
            socket.close();
            ChatServer.removeClient(this);
        } catch (IOException e) {
            System.out.println("Client disconnected unexpectedly");
            ChatServer.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}