package classdemo1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandler  implements Runnable{
    Socket client;
    PrintWriter pw;
    Scanner scanner;
    BlockingQueue<String> messages;
    Quiz quiz;

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
    public ClientHandler(Socket client, BlockingQueue<String> messages, Quiz quiz)  throws IOException {
        this.client = client;
        this.pw = new PrintWriter(client.getOutputStream(), true);
        this.scanner = new Scanner(client.getInputStream());
        this.messages = messages;
        this.quiz = quiz;
    }



    public void protocol() throws IOException, InterruptedException {
        String msg = "";
        pw.println("Hi from server");
        String data = "";

        while (!msg.equals("CLOSE#")) {
            msg = scanner.nextLine();

            if (msg.contains("#")) {
                String[] strings = msg.split("#");
                String action = strings[0];
                if (strings.length > 1) {
                    data = strings[1];
                } else {
                    data = "";
                }
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
                        case "QUIZ":
                            doQuiz();
                            break;
                    }
                }

        }

        client.close();
    }

    private void doQuiz() {

        String msg = "";
        String answer;

        while (!msg.equals("stop")){
            pw.println("Pick a question or type stop to stop!");

            while (true) {
            msg = scanner.nextLine();
                try {
                    pw.println(quiz.getQuestion(Integer.parseInt(msg)));
                    break;
                } catch (Exception e) {
                    pw.println("Wrong input");
                }
            }

            if(!quiz.getQuestion(Integer.parseInt(msg)).equals("taken")) {
                answer = scanner.nextLine();


                if (answer.equalsIgnoreCase(quiz.getAnswer(Integer.parseInt(msg)))) {
                    pw.println("Correct");
                } else {
                    pw.println("Wrong!");
                }

                quiz.removeQuestion(Integer.parseInt(msg));
            }

        }



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
