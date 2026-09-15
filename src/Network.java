import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Network {
    public int              role;
    private ServerSocket    serverSocket;
    private Socket          clientSocket;
    private final Printer   printer;
    private PrintWriter     out;
    private BufferedReader  in;
    private String          ip;

    //Quelle: https://www.baeldung.com/a-guide-to-java-sockets
    public          Network(Printer printer)                {
        Scanner scan = new Scanner(System.in);
        this.printer = printer;
        System.out.println(printer.hostOrClientMess);
        if (scan.next().equals("H")) {
            role = 1;
            System.out.println(printer.waitClientMess);
        } else {
            role = 0;
            System.out.println(printer.enterIPMess);
            ip = scan.next();}
    }

    public void     buildConnection() throws IOException    {
        if (role == 1) {
            serverSocket = new ServerSocket(6666);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            if (Printer.cont.equals(in.readLine())) {
                out.println(Printer.cont);
                System.out.println(printer.connectedMess);
            }
        } else {
            clientSocket = new Socket(ip, 6666);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(Printer.cont);
            System.out.println(printer.connectingMess);
            if (Printer.cont.equals(in.readLine()))
                System.out.println(printer.connectedMess);
        }
    }
    public void     closeConnection() throws IOException    {
        in.close();
        out.close();
        clientSocket.close();
        if (role == 1)
            serverSocket.close();
    }
    public void     sendSignal(String input)                    {
        out.println(input);
    }
    public String   receiveSignal()                         {
        try {
            return in.readLine();
        } catch (IOException e) {
            return printer.errorMess;
        }
    }
}
