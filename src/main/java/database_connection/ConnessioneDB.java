package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Classe di utilità per la gestione della connessione al database relazionale.
 * Fornisce un punto di accesso centralizzato per l'apertura dei canali di comunicazione
 * verso il server PostgreSQL tramite driver JDBC.
 */
public class ConnessioneDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_ospedale";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123"; // <-- Metti qui la tua password vera di pgAdmin
    /**
     * Stabilisce e restituisce una connessione attiva verso il database configurato.
     * Ogni chiamata a questo metodo crea un nuovo oggetto Connection che dovrà
     * essere opportunamente chiuso dall'utilizzatore (es. tramite costrutti try-with-resources).
     *
     * @return Connection Oggetto di connessione attivo verso la base dati.
     * @throws SQLException Se si verifica un errore di rete, credenziali errate o mancata risposta del server.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}//classe solo per contenere i dati del db