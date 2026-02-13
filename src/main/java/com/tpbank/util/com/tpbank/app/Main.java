package com.tpbank.app;

import java.sql.Connection;
import com.tpbank.util.DBConnection;

public class Main {

    public static void main(String[] args) {
        Connection connection = DBConnection.getInstance(); // récupère la connexion (Singleton)
        System.out.println("Connexion OK : " + connection);
    }
}
