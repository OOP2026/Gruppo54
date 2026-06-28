package model;

/**
 * Rappresenta l'entità Amministratore all'interno del sistema ospedaliero.
 * Estende la classe base {@link Utente} ereditandone le credenziali e i dati anagrafici,
 * e identifica gli utenti con privilegi di gestione risorse, anagrafica pazienti e ricoveri.
 */
public class Amministratore extends Utente {

    /**
     * Costruttore completo per la classe Amministratore.
     * Inizializza i dati identificativi dell'amministratore richiamando il costruttore della superclasse.
     *
     * @param login   Lo username univoco utilizzato per l'autenticazione.
     * @param nome    Il nome dell'amministratore.
     * @param cognome Il cognome dell'amministratore.
     */
    public Amministratore(String login, String nome, String cognome) {
        super(login, nome, cognome);
    }
}