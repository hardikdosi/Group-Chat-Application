/**
 *
 * @author Hardik
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServer {

    private static final int PORT_NUMBER = 4444;
    private static HashSet<String> clientNames = new HashSet<String>();
    private static HashSet<PrintWriter> clientPrintWriterList = new HashSet<PrintWriter>();

    ChatServer() throws IOException {
        System.out.println("Server started Successfully !");
        System.out.println("IP Address : " + InetAddress.getLocalHost());

        ServerSocket SVRSOCK = new ServerSocket(PORT_NUMBER);

        while (true) {
            Socket newSocket = SVRSOCK.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
            PrintWriter out = new PrintWriter(newSocket.getOutputStream(), true);

            while (true) {
                String s = in.readLine();
                if ("DETATCH_NOW".equals(s)) {
                    break;
                }
            }
            ServerThread newThread = new ServerThread(newSocket);
            newThread.start();
        }
    }

    private static class ServerThread extends Thread {

        private String clientName;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clientName = in.readLine();

                if (!clientNames.contains(clientName)) {
                    clientNames.add(clientName);
                } else {
                    System.out.println("Name Already Exits.");
                    System.exit(0);
                }

                clientPrintWriterList.add(out);

                for (PrintWriter writer : clientPrintWriterList) {
                    if (writer == out) {
                        writer.println("Welcome " + clientName + "!\n");
                    } else {
                        writer.println("\n\t*** " + clientName + " has entered the Chat. ***\n");
                    }
                }

                while (true) {                                      //Listening all the Clients and Broadcasting the messages to all of them
                    String input = in.readLine();
                    for (PrintWriter writer : clientPrintWriterList) {
                        if (writer == out) {
                            writer.println("\t\tMe : " + input);
                        } else {
                            writer.println(clientName + " : " + input);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                if (clientName != null) {
                    clientNames.remove(clientName);
                }
                if (out != null) {
                    clientPrintWriterList.remove(out);
                }

                for (PrintWriter writer : clientPrintWriterList) {
                    writer.println("\n\t*** " + clientName + " is Offline. ***\n");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
    /*public static void main(String args[]) throws IOException {
     new ChatServer();
     }*/
}
