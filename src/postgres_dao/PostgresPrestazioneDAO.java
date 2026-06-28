package postgres_dao;

import dao.PrestazioneDAO;
import model.Prestazione;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link PrestazioneDAO} per PostgreSQL.
 * Gestisce la persistenza, storicizzazione e controllo di sovrapposizioni orarie
 * delle prestazioni mediche erogate.
 */
public class PostgresPrestazioneDAO implements PrestazioneDAO {

    /**
     * Registra una nuova prestazione medica inserendo i dati all'interno della tabella sul database.
     * Effettua la conversione esplicita dei tipi di dato temporali (Date, Time) e del tipo Custom Enum.
     *
     * @param prestazione L'oggetto prestazione da salvare.
     * @return boolean True se l'inserimento avviene con successo, false altrimenti.
     */
    @Override
    public boolean aggiungiPrestazione(Prestazione prestazione) {
        String query = "INSERT INTO prestazione (tipo_prestazione, data, orario, esito, id_ricovero, username_medico) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = database.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String tipoEnum = prestazione.getTipoPrestazione().toLowerCase();
            pstmt.setObject(1, tipoEnum, Types.OTHER);

            pstmt.setDate(2, java.sql.Date.valueOf(prestazione.getData()));

            String orarioStr = prestazione.getOrario();
            if (orarioStr.length() == 5) {
                orarioStr += ":00";
            }
            pstmt.setTime(3, java.sql.Time.valueOf(orarioStr));

            pstmt.setString(4, prestazione.getEsito());
            pstmt.setInt(5, prestazione.getIdRicovero());
            pstmt.setString(6, prestazione.getUsernameMedico());

            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;

        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con messaggio guidato
            System.err.println("ERRORE CRITICO NELL'INSERIMENTO DELLA PRESTAZIONE: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica sul database se un medico possiede già un'attività registrata nella stessa data e ora,
     * fungendo da vincolo di integrità a livello applicativo.
     *
     * @param usernameMedico Lo username identificativo del medico.
     * @param data La data della prestazione da ispezionare.
     * @param orario L'orario della prestazione da ispezionare.
     * @return boolean True se si riscontra una sovrapposizione oraria, false altrimenti.
     */
    @Override
    public boolean verificaPrestazioneSovrapposta(String usernameMedico, String data, String orario) {
        String query = "SELECT COUNT(*) FROM prestazione WHERE username_medico = ? AND data = ? AND orario = ?";

        String orarioCompleto = orario.trim();
        if (orarioCompleto.length() == 5) {
            orarioCompleto += ":00";
        }

        try (Connection conn = database.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, usernameMedico);
            pstmt.setDate(2, java.sql.Date.valueOf(data));
            pstmt.setTime(3, java.sql.Time.valueOf(orarioCompleto));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con messaggio guidato
            System.err.println("ERRORE CRITICO NELLA VERIFICA SOVRAPPOSIZIONE PRESTAZIONE: " + e.getMessage());
        }
        return false;
    }

    /**
     * Recupera lo storico cronologico decrescente di tutte le prestazioni erogate da un medico.
     *
     * @param usernameMedico Lo username identificativo del medico.
     * @return List&lt;Prestazione&gt; Elenco delle prestazioni associate.
     */
    @Override
    public List<Prestazione> ottieniPrestazioniPerMedico(String usernameMedico) {
        List<Prestazione> lista = new ArrayList<>();
        String query = "SELECT tipo_prestazione, data, orario, esito, id_ricovero FROM prestazione WHERE username_medico = ? ORDER BY data DESC, orario DESC";

        try (Connection conn = database.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, usernameMedico);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo_prestazione");
                    String dataPrestazione = rs.getDate("data").toString();
                    String orarioPrestazione = rs.getTime("orario").toString();
                    String esito = rs.getString("esito");
                    int idRicovero = rs.getInt("id_ricovero");
                    lista.add(new Prestazione(tipo, dataPrestazione, orarioPrestazione, esito, idRicovero, usernameMedico));
                }
            }
        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con messaggio guidato
            System.err.println("ERRORE CRITICO NEL RECUPERO DELLE PRESTAZIONI PER MEDICO: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Modifica l'esito (referto o annotazione medica) di una prestazione univocamente identificata
     * dalla combinazione logica della terna: ricovero, data e ora.
     *
     * @param idRicovero ID del ricovero associato.
     * @param data Data di esecuzione della prestazione.
     * @param orario Orario di esecuzione della prestazione.
     * @param nuovoEsito Testo del nuovo referto clinico.
     * @return boolean True se l'aggiornamento ha modificato correttamente il record, false altrimenti.
     */
    @Override
    public boolean aggiornaEsitoPrestazione(int idRicovero, String data, String orario, String nuovoEsito) {
        String query = "UPDATE prestazione SET esito = ? WHERE id_ricovero = ? AND data = ? AND orario = ?";

        try (Connection conn = database.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nuovoEsito);
            pstmt.setInt(2, idRicovero);
            pstmt.setDate(3, java.sql.Date.valueOf(data));
            pstmt.setTime(4, java.sql.Time.valueOf(orario));
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con messaggio guidato
            System.err.println("ERRORE CRITICO NELL'AGGIORNAMENTO ESITO PRESTAZIONE: " + e.getMessage());
            return false;
        }
    }
}