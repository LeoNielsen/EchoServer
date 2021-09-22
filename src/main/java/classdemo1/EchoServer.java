package classdemo1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class EchoServer {
    public static final int DEFAULT_PORT = 2345;
    private  ServerSocket serverSocket;

    //Protocol
    //send bedsked til klient lige efter den er conneted

    private void handleClient(Socket socket) throws IOException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(socket.getInputStream());
        pw.println("You are connected sent a String to get it upper cased, send 'stop' to stop");
        String message = scanner.nextLine(); //blocking call
        while (!message.equals("stop")){
            pw.println(message.toUpperCase());
            message = scanner.nextLine(); //blocking call
        }
        pw.println("connection is closing");
        socket.close();
    }


    private void startServer(int port) throws IOException {
        BlockingQueue<String> messages = new ArrayBlockingQueue<String>(10);

        CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

        Dispatcher dispatcher = new Dispatcher(messages, clients);

        serverSocket = new ServerSocket(port);
        System.out.println("server started, listening on: " + port);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Thread t = new Thread(dispatcher);
        t.start();

        while (true) {
            Socket client = serverSocket.accept(); //Blocking call
            ClientHandler cl = new ClientHandler(client, messages);
            executorService.execute(cl);

            clients.add(cl);

//            System.out.println("waiting for a client");
//            System.out.println("new client connected");
//            handleClient(client);
        }

    }

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if(args.length == 1){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                System.out.println("Invalid port number, using default port: " + DEFAULT_PORT);
            }
        }
        new EchoServer().startServer(port);

    }
}
