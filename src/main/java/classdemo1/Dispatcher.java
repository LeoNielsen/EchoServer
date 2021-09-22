package classdemo1;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher implements Runnable {
    BlockingQueue<String> messages;
    CopyOnWriteArrayList<ClientHandler> clients;

    public Dispatcher(BlockingQueue<String> messages, CopyOnWriteArrayList<ClientHandler> clients) {
        this.messages = messages;
        this.clients = clients;
    }

    @Override
    public void run() {
        String outMsg = "";
        while (true){
            try {
                outMsg = messages.take();
                for (ClientHandler client: clients) {
                    client.getPw().println(outMsg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
