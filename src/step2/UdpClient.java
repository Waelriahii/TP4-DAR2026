package step2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Étape 2 : client UDP qui envoie des messages préfixés par le nom d'utilisateur. */
public class UdpClient {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom d'utilisateur : ");
        String username = sc.nextLine().trim();

        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddr = InetAddress.getByName("localhost");

        System.out.println("Tapez vos messages ('quit' pour quitter) :");
        while (true) {
            String text = sc.nextLine();
            if (text.equalsIgnoreCase("quit")) break;
            byte[] data = ("[" + username + "] : " + text).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(data, data.length, serverAddr, UdpServer.PORT));
        }
        socket.close();
        sc.close();
    }
}
