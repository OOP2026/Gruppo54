package model;

/**
 * Rappresenta l'entità Letto all'interno della struttura ospedaliera.
 * Ogni istanza corrisponde a un posto letto fisico censito nel database,
 * identificato in modo univoco a livello globale e monitorato nel suo stato di occupazione.
 */
public class Letto {
    /** Identificativo univoco del letto utilizzato come Chiave Primaria (PK) nel database. */
    private int idLetto;

    /** Numero progressivo del letto all'interno della specifica stanza di appartenenza. */
    private int numeroLetto;

    /** Flag booleano che indica lo stato di disponibilità in tempo reale del posto letto. */
    private boolean occupato;

    /**
     * Costruttore completo della classe Letto.
     *
     * @param idLetto     L'identificativo univoco globale del letto.
     * @param numeroLetto Il numero del letto relativo alla stanza.
     * @param occupato    Lo stato iniziale di occupazione del posto letto.
     */
    public Letto(int idLetto, int numeroLetto, boolean occupato) {
        this.idLetto = idLetto;
        this.numeroLetto = numeroLetto;
        this.occupato = occupato;
    }

    /**
     * Restituisce l'identificativo univoco del letto del database.
     * @return int L'ID del letto.
     */
    public int getIdLetto() { return idLetto; }

    /**
     * Configura l'identificativo univoco del letto.
     * @param idLetto Il nuovo ID da assegnare al letto.
     */
    public void setIdLetto(int idLetto) { this.idLetto = idLetto; }

    /**
     * Restituisce il numero del letto locale alla stanza.
     * @return int Il numero del letto.
     */
    public int getNumeroLetto() { return numeroLetto; }

    /**
     * Configura il numero del letto locale alla stanza.
     * @param numeroLetto Il nuovo numero di letto da impostare.
     */
    public void setNumeroLetto(int numeroLetto) { this.numeroLetto = numeroLetto; }

    /**
     * Verifica se il posto letto risulta attualmente occupato da un paziente.
     * @return boolean True se il letto è occupato, false se è libero.
     */
    public boolean isOccupato() { return occupato; }

    /**
     * Aggiorna lo stato di occupazione del posto letto.
     * @param occupato True per impostare il letto come occupato, false per liberarlo.
     */
    public void setOccupato(boolean occupato) { this.occupato = occupato; }
}