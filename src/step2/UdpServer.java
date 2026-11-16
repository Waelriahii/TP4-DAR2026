package step2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** Étape 2 : serveur UDP unidirectionnel (affiche seulement les messages reçus). */
public class UdpServer {
    public static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(PORT));
        System.out.println("Serveur UDP en écoute sur le port " + PORT);

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("De " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " -> " + msg);
        }
    }
}
