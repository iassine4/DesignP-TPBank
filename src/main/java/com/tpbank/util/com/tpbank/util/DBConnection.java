package com.tpbank.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class DBConnection {

    // volatile : évite des effets bizarres en multi-thread (lecture/écriture mémoire)
    private static volatile Connection connection;

    // constructeur privé : empêche "new DBConnection()" => Singleton
    private DBConnection() {
    }

    // point d’entrée unique pour récupérer la connexion
    public static Connection getInstance() {
        // double-check locking : rapide (pas synchronized à chaque appel)
        if (connection == null) {
            synchronized (DBConnection.class) {
                if (connection == null) {
                    connection = createConnection();
                }
            }
        }
        return connection;
    }

    // méthode interne : lit le fichier properties et ouvre la connexion JDBC
    private static Connection createConnection() {
        Properties props = new Properties();

        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Fichier db.properties introuvable dans le classpath.");
            }

            props.load(in);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            // exception “propre” : on remonte une erreur claire, avec la cause
            throw new IllegalStateException("Impossible d'ouvrir la connexion à la base de données.", e);
        }
    }
}
