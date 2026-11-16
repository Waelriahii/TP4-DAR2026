# TP4 : Sockets en mode non-connecté UDP — Chat C/S

**Matière :** Développement d'applications réparties — LSI3 — 2026/2027
**Étudiant :** Wael Riahi

## Objectif
Implémenter en Java un service de chat client/serveur avec `DatagramSocket` (UDP) : d'abord unidirectionnel avec un client, puis bidirectionnel avec diffusion à plusieurs clients.

## Structure du projet
```
src/
├── step2/   UdpServer.java, UdpClient.java      (chat unidirectionnel)
└── step3/   ChatServer.java, ChatClient.java    (chat multi-clients)
```

## Exécution
1. Lancer le serveur (`UdpServer` ou `ChatServer`, port **1234**).
2. Lancer un ou plusieurs clients (dans IntelliJ : *Run > Edit Configurations > Allow multiple instances*).
3. Saisir un nom d'utilisateur puis des messages ; `quit` pour quitter.

## Étape 2 : Chat unidirectionnel
- Le serveur se lie au port 1234 via `InetSocketAddress` et affiche l'adresse (IP:port) de l'expéditeur et le message.
- Le client envoie les messages au format `[NomUtilisateur] : message`.


**Synthèse :** Le serveur reçoit bien chaque datagramme envoyé par le client et l'affiche avec l'adresse de l'expéditeur. La communication est à sens unique : le serveur ne renvoie rien.

## Étape 3 : Chat bidirectionnel avec diffusion
- Le serveur garde un ensemble (`CopyOnWriteArraySet<SocketAddress>`) des clients ayant déjà envoyé un message.
- À chaque message reçu, il enregistre l'expéditeur s'il est nouveau, puis le retransmet à tous les autres clients (sauf l'expéditeur).
- Le client utilise un **thread** dédié à la réception pour recevoir en temps réel pendant que le thread principal envoie.
- À la connexion, le client envoie un message « a rejoint le chat » pour s'enregistrer auprès du serveur.



**Synthèse :** Les messages d'un client sont diffusés en temps réel à tous les autres clients connectés. UDP étant sans connexion, la « connexion » est simulée par l'enregistrement de l'adresse de l'expéditeur côté serveur. Limites : pas de garantie de livraison ni d'ordre des messages, et pas de détection de déconnexion (le serveur continue d'envoyer aux clients enregistrés).

## Conclusion
Ce TP a permis de maîtriser `DatagramSocket`/`DatagramPacket`, la liaison avec `InetSocketAddress`, et la gestion de la concurrence côté client avec les threads.
