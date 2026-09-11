import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Network {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public int role;
    String ip;

    public Network() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Host(H) or Client(C)?\n");
        if (scan.next().equals("H"))
            role = 1;
        else {
            role = 0;
            System.out.println("Bitte gib die IP ein...");
            ip = scan.next();
        }
    }

    public void buildConnection() throws IOException {
        if (role == 1) {
            serverSocket = new ServerSocket(6666);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            if ("Trying to connect...".equals(in.readLine())) {
                out.println("Connected");
                System.out.println("Connected");
            }
        } else {
            clientSocket = new Socket(ip, 6666);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("Trying to connect...");
            System.out.println("Trying to connect...");
            if ("Connected".equals(in.readLine()))
                System.out.println("Connected");
        }
    }

    public void sendSignal(String input) {
        out.println(input);
    }

    public String receiveSignal() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return "error";
        }
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        if (role == 1)
            serverSocket.close();
    }
}
