import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Network {
    public int              role;
    private final Printer   printer;
    private ServerSocket    serverSocket;
    private Socket          clientSocket;
    private PrintWriter     out;
    private BufferedReader  in;
    private String          ip;

    //Source: https://www.baeldung.com/a-guide-to-java-sockets
    public          Network(Printer printer)                {
        Scanner scan = new Scanner(System.in);
        this.printer = printer;
        System.out.print(printer.hostOrClientMess);
        if (scan.next().equals("H")) {
            role = 1;
            getIP();
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
    public void     getIP()                                 {
        try {
            InetAddress address = InetAddress.getLocalHost();
            printer.printIP(address.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public void     sendSignal(String input)                {
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