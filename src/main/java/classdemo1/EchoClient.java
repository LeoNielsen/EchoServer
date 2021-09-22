package classdemo1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    Socket client;
    PrintWriter pw;
    Scanner scanner;

    public void connect(String address, int port) throws IOException {
        client = new Socket(address,port);
        pw = new PrintWriter(client.getOutputStream(), true);
        scanner = new Scanner(client.getInputStream());
        System.out.println(scanner.nextLine());

        Scanner keyboard = new Scanner(System.in);
        boolean keepRunning = true;
        while (keepRunning){
            String msgToSend = keyboard.nextLine(); //Blocking Call
            pw.println(msgToSend);
            System.out.println(scanner.nextLine());
            if (msgToSend.equals("CLOSE#")){
                keepRunning = false;
            }
        }
        pw.println("CLOSE#");
        client.close();
    }

    public static void main(String[] args) throws IOException {

        new EchoClient().connect("localhost",2345);



    }
}