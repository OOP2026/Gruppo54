package implementazioneDao;

import dao.StanzaDAO;
import model.Stanza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link StanzaDAO} per il DBMS PostgreSQL.
 * Gestisce l'estrazione e il monitoraggio delle stanze fisiche allocate nei diversi reparti.
 */
public class PostgresStanzaDAO implements StanzaDAO {

    /**
     * Recupera dal database l'elenco di tutte le stanze appartenenti a un determinato reparto.
     * Converte l'ID numerico in stringa per uniformarsi alla tipizzazione VARCHAR della tabella.
     *
     * @param codiceReparto L'identificativo numerico del reparto.
     * @return List&lt;Stanza&gt; L'elenco delle stanze associate, o una lista vuota in caso di errore.
     */
    @Override
    public List<Stanza> ottieniStanzePerReparto(int codiceReparto) {
        List<Stanza> listaStanze = new ArrayList<>();
        String query = "SELECT numero_stanza, piano FROM stanza WHERE codice_reparto = ?";

        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // CORRETTO: Viene ripristinato il setString poiché la colonna sul DB è VARCHAR/character varying
            pstmt.setString(1, String.valueOf(codiceReparto));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int numeroStanza = rs.getInt("numero_stanza");
                    int piano = rs.getInt("piano");

                    Stanza s = new Stanza(numeroStanza, piano);
                    listaStanze.add(s);
                }
            }

        } catch (SQLException e) {
            System.err.println("ERRORE CRITICO NEL RECUPERO DELLE STANZE PER IL REPARTO " + codiceReparto + ": " + e.getMessage());
        }

        return listaStanze;
    }
}