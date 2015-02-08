package com.mt523.backtalk;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

class Server {
  
   private ServerSocket serverSocket;
   private Socket socket;
   private static final int PORT = 4242;
   private Connection connection;
   private Statement statement;
   private ResultSet resultSet;
   private String query;
   private final String dbAddr = 
      "jdbc:mysql://backtalk.cri0r2kpn2sn.us-west-1.rds.amazonaws.com/" +
      "backtalk?user=admin&password=theH3r0ofCanton";

   public Server(){
      try {
         // Connect to database        
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         connection = DriverManager.getConnection(dbAddr);
         statement = connection.createStatement();
         query = "SELECT * FROM cards";

         /*
         serverSocket = new ServerSocket(PORT);
         System.out.printf("Server listening on port %d.\n", PORT);
         socket = serverSocket.accept();
         System.out.printf("Connected to %s.\n", socket.getRemoteSocketAddress());
         */

         resultSet = statement.executeQuery(query);
         while(resultSet.next()) {
            System.out.println("Question: " + resultSet.getString("question"));
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

