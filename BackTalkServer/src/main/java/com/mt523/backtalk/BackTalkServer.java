package com.mt523.backtalk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.mt523.backtalk.packets.client.Card;
import com.mt523.backtalk.packets.client.DeckPacket;
import com.mt523.backtalk.packets.server.CardRequest.CardTier;
import com.mt523.backtalk.packets.server.ServerPacket;
import com.mt523.backtalk.packets.server.ServerPacket.IBackTalkServer;

class BackTalkServer {

    // ServerSocket to open socket connections with clients
    private ServerSocket serverSocket;

    // Port that server listens on
    private static final int PORT = 4242;

    // Database connection object
    private Connection connection;

    // Object to interface with database
    private Statement statement;

    // Queries to be run on the database
    private String query;

    // Set of cards to be served to clients
    private Vector<Card> defaultDeck;
    private Vector<Card> paid1;
    private Vector<Card> paid2;

    // Address of the database which holds the content
    private final String dbAddr = "jdbc:mysql://backtalk.cri0r2kpn2sn.us-west-1.rds.amazonaws.com/"
            + "backtalk?user=admin&password=theH3r0ofCanton";

    public BackTalkServer() {

        try {
            // Connect to database ---------------------------------------------
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(dbAddr);
            statement = connection.createStatement();

            // Populate Default Deck -------------------------------------------
            defaultDeck = new Vector<>();
            query = "SELECT * FROM cards";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                defaultDeck.add(new Card(resultSet.getInt("id"), resultSet
                        .getString("question"), resultSet.getString("answer"),
                        resultSet.getString("hint"), resultSet
                                .getString("category"), true, false));
                // System.out.println(defaultDeck.lastElement().toString());
            }

            // Initialize server -----------------------------------------------
            serverSocket = new ServerSocket(PORT);
            System.out.printf("Server listening on port %d.\n", PORT);
            while (true) {
                new ServerWorker(serverSocket.accept()).start();
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerWorker extends Thread implements IBackTalkServer {

        private Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;
        private ServerPacket serverPacket;

        public ServerWorker(Socket socket) {
            this.socket = socket;
            System.out.printf("Connected to %s.\n",
                    socket.getRemoteSocketAddress());
        }

        @Override
        public void run() {
            super.run();
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                serverPacket = (ServerPacket) input.readObject();
                serverPacket.setServer(this);
                serverPacket.handlePacket();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void serveCards(CardTier tier) {
            Vector<Card> deck = null;
            switch (tier) {
            case DEFAULT:
                deck = defaultDeck;
                break;
            case PAID1:
                deck = paid1;
                break;
            case PAID2:
                deck = paid2;
                break;
            default:
                System.err.println("This will literally never happen.");
            }
            try {
                output.writeObject(new DeckPacket(deck));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new BackTalkServer();
    }
}
