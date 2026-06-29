package postgres_dao;

import dao.UtenteDAO;
import model.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementazione concreta dell'interfaccia {@link UtenteDAO} per il DBMS PostgreSQL.
 * Gestisce i flussi di autenticazione iniziale al sistema ospedaliero, istanziando
 * polimorficamente il corretto profilo utente in base al ruolo censito nel DB.
 */
public class PostgresUtenteDAO implements UtenteDAO {

    /**
     * Sottopone a verifica le credenziali di accesso fornite (username e password).
     * Se l'autenticazione ha esito positivo, analizza il ruolo restituito dal record ed istanzia
     * l'oggetto di modello specifico (Amministratore o Medico), sfruttando il polimorfismo.
     *
     * @param login    Lo username inserito dall'utente.
     * @param password La password inserita dall'utente.
     * @return Utente  L'istanza specifica dell'utente autenticato (Medico o Amministratore), o null in caso di credenziali errate.
     */
    @Override
    public Utente login(String login, String password) {
        String query = "SELECT ruolo, nome, cognome, specializzazione FROM utente WHERE username = ? AND password = ?";

        try (Connection conn = database.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String ruolo = rs.getString("ruolo");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");

                    if ("AMMINISTRATORE".equals(ruolo)) {
                        return new model.Amministratore(login, nome, cognome);
                    } else if ("MEDICO".equals(ruolo)) {
                        String specializzazione = rs.getString("specializzazione");
                        System.out.println("DB ha trovato il medico: " + nome + " " + cognome + ", Spec: " + specializzazione);
                        return new model.Medico(login, nome, cognome, specializzazione);
                    }
                }
            }
        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con la stampa del messaggio d'errore nativo ordinata
            System.err.println("ERRORE CRITICO DURANTE IL LOGIN, UTENTE O PASSWORD ERRATI");
            System.err.println("Dettaglio SQLException: " + e.getMessage());
        }
        return null;
    }
}