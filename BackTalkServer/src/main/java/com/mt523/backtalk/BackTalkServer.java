package com.mt523.backtalk;

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

import com.mt523.backtalk.packets.CardPacket;

class Server {

    // ServerSocket to open socket connections with clients
    private ServerSocket serverSocket;

    // Socket connected to client
    private Socket socket;

    // Port that server listens on
    private static final int PORT = 4242;

    // Database connection object
    private Connection connection;

    // Object to interface with database
    private Statement statement;

    // Queries to be run on the database
    private String query;

    // Set of cards to be served to clients
    private Vector<CardPacket> deck = new Vector<>();

    // Address of the database which holds the content
    private final String dbAddr = "jdbc:mysql://backtalk.cri0r2kpn2sn.us-west-1.rds.amazonaws.com/"
            + "backtalk?user=admin&password=theH3r0ofCanton";

    public Server() {

        try {
            // Connect to database ---------------------------------------------
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(dbAddr);
            statement = connection.createStatement();

            // Populate Deck ---------------------------------------------------
            query = "SELECT * FROM cards";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                deck.add(new CardPacket(resultSet.getInt("id"), resultSet
                        .getString("question"), resultSet.getString("answer"),
                        resultSet.getString("category")));
                System.out.println(deck.lastElement().toString());
            }

            // Initialize server -----------------------------------------------
            serverSocket = new ServerSocket(PORT);
            System.out.printf("Server listening on port %d.\n", PORT);
            while (true) {
                socket = serverSocket.accept();
                System.out.printf("Connected to %s.\n",
                        socket.getRemoteSocketAddress());
                ObjectOutputStream output = new ObjectOutputStream(
                        socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(
                        socket.getInputStream());
                socket.close();
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
