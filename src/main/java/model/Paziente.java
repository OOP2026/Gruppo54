package model;

/**
 * Rappresenta l'entità logica Paziente all'interno del dominio ospedaliero.
 * Incapsula le informazioni anagrafiche minime necessarie alla registrazione
 * e identificazione del soggetto destinatario delle degenze e delle prestazioni cliniche. La teniamo per eventuali
 * implementazioni future
 */
public class Paziente {
    // RISOLTI CODE SMELLS: Dichiarati i campi come final per garantire l'immutabilità dei dati anagrafici
    private final String codiceFiscale;
    private final String nome;
    private final String cognome;

    /**
     * Costruttore completo della classe Paziente.
     *
     * @param codiceFiscale Il codice fiscale del paziente, avente valore di identificativo univoco.
     * @param nome          Il nome del paziente.
     * @param cognome       Il cognome del paziente.
     */
    public Paziente(String codiceFiscale, String nome, String cognome) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Restituisce il codice fiscale identificativo del paziente.
     * @return String Il codice fiscale.
     */
    public String getCodiceFiscale() { return codiceFiscale; }

    /**
     * Restituisce il nome del paziente.
     * @return String Il nome.
     */
    public String getNome() { return nome; }

    /**
     * Restituisce il cognome del paziente.
     * @return String Il cognome.
     */
    public String getCognome() { return cognome; }
}