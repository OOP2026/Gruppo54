package model;

/**
 * Classe astratta che definisce il modello base per gli utenti del sistema ospedaliero.
 * Fornisce i campi comuni per l'autenticazione (login) e le generalità anagrafiche (nome e cognome),
 * fungendo da superclasse per la profilazione polimorfica (Amministratore e Medico).
 */
public abstract class Utente {
    /** Lo username univoco utilizzato dall'utente come credenziale d'accesso al sistema. */
    private String login;

    /** Il nome dell'utente. */
    private String nome;

    /** Il cognome dell'utente. */
    private String cognome;

    /**
     * Costruttore per le sottoclassi di Utente.
     * Permette l'inizializzazione dei dati comuni condivisi da tutti i profili del sistema.
     *
     * @param login   Lo username univoco d'accesso.
     * @param nome    Il nome del soggetto.
     * @param cognome Il cognome del soggetto.
     */
    public Utente(String login, String nome, String cognome) {
        this.login = login;
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Restituisce lo username (login) dell'utente.
     * @return String Il login.
     */
    public String getLogin() { return login; }

    /**
     * Configura lo username (login) dell'utente.
     * @param login Il nuovo username da impostare.
     */
    public void setLogin(String login) { this.login = login; }

    /**
     * Restituisce il nome dell'utente.
     * @return String Il nome.
     */
    public String getNome() { return nome; }

    /**
     * Configura il nome dell'utente.
     * @param nome Il nuovo nome da impostare.
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Restituisce il cognome dell'utente.
     * @return String Il cognome.
     */
    public String getCognome() { return cognome; }

    /**
     * Configura il cognome dell'utente.
     * @param cognome Il nuovo cognome da impostare.
     */
    public void setCognome(String cognome) { this.cognome = cognome; }
}