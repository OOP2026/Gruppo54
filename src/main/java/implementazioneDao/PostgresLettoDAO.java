package implementazioneDao;

import dao.LettoDAO;
import model.Letto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link LettoDAO} per il DBMS PostgreSQL.
 * Gestisce l'interrogazione delle tabelle dei posti letto sfruttando i driver JDBC.
 */
public class PostgresLettoDAO implements LettoDAO {

    /**
     * Recupera dal database la lista di tutti i letti presenti in una determinata stanza.
     * Utilizza il costrutto try-with-resources per garantire il rilascio automatico delle risorse JDBC.
     *
     * @param numeroStanza Il numero identificativo della stanza.
     * @return List&lt;Letto&gt; L'elenco dei letti trovati, o una lista vuota in caso di errore o stanza vuota.
     */
    @Override
    public List<Letto> ottieniLettiPerStanza(int numeroStanza){
        List<Letto> listaLetti = new ArrayList<>();
        String query = "SELECT id_letto, numero_letto, occupato FROM letto WHERE numero_stanza = ?";

        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, numeroStanza);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idLetto = rs.getInt("id_letto");
                    int numeroLetto = rs.getInt("numero_letto");
                    boolean occupato = rs.getBoolean("occupato");
                    Letto l = new Letto(idLetto, numeroLetto, occupato);
                    listaLetti.add(l);
                }
            }
        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con una gestione pulita del messaggio d'errore
            System.err.println("ERRORE CRITICO NEL RECUPERO DEI LETTI PER LA STANZA " + numeroStanza + ": " + e.getMessage());
        }
        return listaLetti;
    }

    /**
     * Recupera i dettagli di un singolo posto letto partendo dal suo ID univoco (Chiave Primaria).
     *
     * @param idLetto L'identificativo univoco del letto nel database.
     * @return Letto L'istanza dell'oggetto Letto popolata con i dati del DB, o null se non trovato.
     */
    @Override
    public Letto ottieniLettoPerId(int idLetto) {
        String query = "SELECT numero_letto, occupato FROM letto WHERE id_letto = ?";

        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idLetto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numeroLetto = rs.getInt("numero_letto");
                    boolean occupato = rs.getBoolean("occupato");
                    return new Letto(idLetto, numeroLetto, occupato);
                }
            }
        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con una gestione pulita del messaggio d'errore
            System.err.println("ERRORE CRITICO NEL RECUPERO DEL SINGOLO LETTO PER ID " + idLetto + ": " + e.getMessage());
        }
        return null;
    }
}