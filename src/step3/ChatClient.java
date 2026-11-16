package step3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Étape 3 : client de chat UDP (envoi + réception simultanés via un thread). */
public class ChatClient {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom d'utilisateur : ");
        String username = sc.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddr = InetAddress.getByName(args.length > 0 ? args[0] : "localhost");

        // Thread de réception
        Thread receiver = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                while (!socket.isClosed()) {
                    DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                    socket.receive(p);
                    System.out.println(new String(p.getData(), 0, p.getLength(), StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                // socket fermé : fin du thread
            }
        });
        receiver.setDaemon(true);
        receiver.start();

        // Message d'arrivée : permet au serveur d'enregistrer ce client
        send(socket, serverAddr, "[" + username + "] a rejoint le chat");

        System.out.println("Tapez vos messages ('quit' pour quitter) :");
        while (true) {
            String text = sc.nextLine();
            if (text.equalsIgnoreCase("quit")) {
                send(socket, serverAddr, "[" + username + "] a quitté le chat");
                break;
            }
            send(socket, serverAddr, "[" + username + "] : " + text);
        }
        socket.close();
        sc.close();
    }

    private static void send(DatagramSocket s, InetAddress addr, String text) throws Exception {
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        s.send(new DatagramPacket(data, data.length, addr, ChatServer.PORT));
    }
}
