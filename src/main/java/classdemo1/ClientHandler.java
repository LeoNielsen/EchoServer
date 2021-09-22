package classdemo1;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandler  implements Runnable{
    Socket client;
    PrintWriter pw;
    Scanner scanner;
    BlockingQueue<String> messages;

    public ClientHandler(Socket client)  throws IOException {
        this.client = client;
        this.pw = new PrintWriter(client.getOutputStream(), true);
        this.scanner = new Scanner(client.getInputStream());
    }
    public ClientHandler(Socket client, BlockingQueue<String> messages)  throws IOException {
        this.client = client;
        this.pw = new PrintWriter(client.getOutputStream(), true);
        this.scanner = new Scanner(client.getInputStream());
        this.messages = messages;
    }

    public void protocol() throws IOException, InterruptedException {
        String msg = "";
        pw.println("Hi from server");

        while (!msg.equals("CLOSE#")) {
            msg = scanner.nextLine();

            if (msg.contains("#")) {
                String[] strings = msg.split("#");
                String action = strings[0];
                String data = strings[1];

                switch (action) {
                    case "UPPER":
                        pw.println(data.toUpperCase());
                        break;
                    case "LOWER":
                        pw.println(data.toLowerCase());
                        break;
                    case "REVERSE":
                        StringBuilder sb = new StringBuilder(data);
                        pw.println(sb.reverse());
                        break;
                    case "TRANSLATE":

                        break;
                    case "ALL":
                        messages.put(data);
                        break;
                }
            }


        }

        client.close();
    }

    public PrintWriter getPw() {
        return pw;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.protocol();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
