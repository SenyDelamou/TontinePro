package groupe1;

import java.sql.*;

public class DatabaseConnection {

    // Configuration de la connexion
    private static final String URL = "jdbc:mysql://localhost:3306/tontinepro";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Mode Hors-ligne (DÉMO)
    public static final boolean OFFLINE_MODE = true;

    // Instance unique (Singleton)
    private static Connection connection = null;

    /**
     * Obtenir la connexion à la base de données
     */
    public static Connection getConnection() {
        if (OFFLINE_MODE)
            return null;
        try {
            if (connection == null || connection.isClosed()) {
                // Charger le driver MySQL
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("❌ Driver MySQL non trouvé !");
                    System.err.println("Vérifiez que mysql-connector-j est bien dans le classpath.");
                    return null;
                }

                // Établir la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à la base de données réussie !");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données !");
            System.err.println("Détails: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fermer la connexion
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Connexion fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tester la connexion
     */
    public static boolean testConnection() {
        Connection conn = getConnection();
        return conn != null;
    }

    /**
     * Obtenir une instance de PreparedStatement (à fermer par l'appelant)
     */
    public static PreparedStatement prepareStatement(String query) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                return conn.prepareStatement(query);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la préparation de la requête : " + query);
            e.printStackTrace();
        }
        return null;
    }
}
