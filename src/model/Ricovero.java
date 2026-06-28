package model;

/**
 * Rappresenta l'entità Ricovero all'interno del sistema ospedaliero.
 * Questa classe modella la transazione di degenza di un paziente, tracciando
 * l'intervallo temporale di permanenza nella struttura e il posto letto ad esso assegnato.
 */
public class Ricovero {
    /** Identificativo univoco del ricovero utilizzato come Chiave Primaria (PK) nel database. */
    private int idRicovero;

    /** Data di inizio della degenza ospedaliera in formato standard ISO (AAAA-MM-GG). */
    private String dataInizio;

    /** Data programmata o effettiva di dimissione clinica in formato standard ISO (AAAA-MM-GG). */
    private String dataFine;

    /** Codice Fiscale del paziente associato al ricovero (Chiave Esterna). */
    private String cfPaziente;

    /** Identificativo univoco del posto letto assegnato (Chiave Esterna). */
    private int idLetto;

    /**
     * Costruttore completo utilizzato per la registrazione iniziale di un nuovo ricovero.
     * Esclude l'identificativo numerico del ricovero poiché autogenerato dal DBMS (SERIAL).
     *
     * @param dataInizio La data di inizio della degenza.
     * @param dataFine   La data prevista per le dimissioni del paziente.
     * @param cfPaziente Il codice fiscale del paziente da ricoverare.
     * @param idLetto    L'ID del letto da assegnare.
     */
    public Ricovero(String dataInizio, String dataFine, String cfPaziente, int idLetto) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.cfPaziente = cfPaziente;
        this.idLetto = idLetto;
    }

    /**
     * Restituisce l'identificativo univoco del ricovero ospedaliero.
     * @return int L'ID del ricovero.
     */
    public int getIdRicovero() { return idRicovero; }

    /**
     * Configura l'identificativo univoco del ricovero ospedaliero.
     * @param idRicovero Il nuovo ID da assegnare.
     */
    public void setIdRicovero(int idRicovero) { this.idRicovero = idRicovero; }

    /**
     * Restituisce la data di inizio del ricovero.
     * @return String La data in formato stringa.
     */
    public String getDataInizio() { return dataInizio; }

    /**
     * Configura la data di inizio del ricovero.
     * @param dataInizio La stringa data da associare.
     */
    public void setDataInizio(String dataInizio) { this.dataInizio = dataInizio; }

    /**
     * Restituisce la data prevista di fine ricovero.
     * @return String La data di dimissione in formato stringa.
     */
    public String getDataFine() { return dataFine; }

    /**
     * Configura la data prevista di fine ricovero.
     * @param dataFine La stringa data di dimissione da associare.
     */
    public void setDataFine(String dataFine) { this.dataFine = dataFine; }

    /**
     * Restituisce il codice fiscale del paziente associato al ricovero.
     * @return String Il codice fiscale del paziente.
     */
    public String getCfPaziente() { return cfPaziente; }

    /**
     * Configura il codice fiscale del paziente da associare al ricovero.
     * @param cfPaziente Il codice fiscale da impostare.
     */
    public void setCfPaziente(String cfPaziente) { this.cfPaziente = cfPaziente; }

    /**
     * Restituisce l'identificativo del posto letto assegnato al paziente.
     * @return int L'ID del letto.
     */
    public int getIdLetto() { return idLetto; }

    /**
     * Configura l'identificativo del posto letto da assegnare al paziente.
     * @param idLetto Il nuovo ID letto da mappare.
     */
    public void setIdLetto(int idLetto) { this.idLetto = idLetto; }
}