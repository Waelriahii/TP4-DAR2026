package step3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/** Étape 3 : serveur de chat UDP avec diffusion à tous les autres clients. */
public class ChatServer {
    public static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(PORT));
        System.out.println("Serveur de chat UDP en écoute sur le port " + PORT);

        Set<SocketAddress> clients = new CopyOnWriteArraySet<>();
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            SocketAddress sender = packet.getSocketAddress();
            String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

            // Enregistre l'expéditeur s'il n'est pas déjà connu
            if (clients.add(sender)) {
                System.out.println("Nouveau client : " + sender);
            }
            System.out.println("De " + sender + " -> " + msg);

            // Diffusion à tous les autres clients
            byte[] data = msg.getBytes(StandardCharsets.UTF_8);
            for (SocketAddress client : clients) {
                if (!client.equals(sender)) {
                    socket.send(new DatagramPacket(data, data.length, client));
                }
            }
        }
    }
}
