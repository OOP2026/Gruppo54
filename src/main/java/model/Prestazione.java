package model;

/**
 * Rappresenta l'entità Prestazione all'interno del sistema ospedaliero.
 * Include le informazioni relative ad un'attività clinica (es. visita medica o intervento chirurgico)
 * eseguita da un medico specifico in favore di un paziente durante un determinato periodo di ricovero.
 */
public class Prestazione {
    /** Identificativo univoco della prestazione utilizzato come Chiave Primaria (PK) nel database. */
    private int idPrestazione;

    /** Tipologia di attività clinica eseguita (es. "Visita Generica", "Intervento Chirurgico"). */
    private String tipoPrestazione;

    /** Data di esecuzione della prestazione in formato standard ISO (AAAA-MM-GG). */
    private String data;

    /** Orario specifico di inizio della prestazione in formato standard (HH:MM). */
    private String orario;

    /** Il referto clinico, le annotazioni o l'esito diagnostico della prestazione. */
    private String esito;

    /** L'identificativo univoco del ricovero (Chiave Esterna) entro cui ricade l'attività. */
    private int idRicovero;

    /** Lo username univoco (Chiave Esterna) del medico che ha effettuato l'intervento. */
    private String usernameMedico;

    /**
     * Costruttore completo utilizzato per l'istanziazione di nuove prestazioni.
     * Esclude l'identificativo numerico poiché delegato all'autoincremento (SERIAL) del DBMS.
     *
     * @param tipoPrestazione Il tipo di prestazione clinica erogata.
     * @param data            La data dell'evento.
     * @param orario          L'orario di erogazione.
     * @param esito           Il testo descrittivo dell'esito iniziale.
     * @param idRicovero      L'ID del ricovero del paziente associato.
     * @param usernameMedico  Lo username del medico operante.
     */
    public Prestazione(String tipoPrestazione, String data, String orario, String esito, int idRicovero, String usernameMedico) {
        this.tipoPrestazione = tipoPrestazione;
        this.data = data;
        this.orario = orario;
        this.esito = esito;
        this.idRicovero = idRicovero;
        this.usernameMedico = usernameMedico;
    }

    /**
     * Restituisce l'identificativo univoco della prestazione medica.
     * @return int L'ID della prestazione.
     */
    public int getIdPrestazione() { return idPrestazione; }

    /**
     * Configura l'identificativo univoco della prestazione medica.
     * @param idPrestazione Il nuovo ID da assegnare.
     */
    public void setIdPrestazione(int idPrestazione) { this.idPrestazione = idPrestazione; }

    /**
     * Restituisce la tipologia di prestazione erogata.
     * @return String Il tipo di prestazione.
     */
    public String getTipoPrestazione() { return tipoPrestazione; }

    /**
     * Configura la tipologia di prestazione erogata.
     * @param tipoPrestazione Il nome descrittivo del tipo di visita o intervento.
     */
    public void setTipoPrestazione(String tipoPrestazione) { this.tipoPrestazione = tipoPrestazione; }

    /**
     * Restituisce la data di esecuzione della prestazione.
     * @return String La data in formato stringa.
     */
    public String getData() { return data; }

    /**
     * Configura la data di esecuzione della prestazione.
     * @param data La stringa data in formato ISO da associare.
     */
    public void setData(String data) { this.data = data; }

    /**
     * Restituisce l'orario specifico dell'intervento clinico.
     * @return String L'orario in formato stringa.
     */
    public String getOrario() { return orario; }

    /**
     * Configura l'orario specifico dell'intervento clinico.
     * @param orario La stringa orario da associare.
     */
    public void setOrario(String orario) { this.orario = orario; }

    /**
     * Restituisce l'esito clinico o referto diagnostico memorizzato.
     * @return String Il testo dell'esito.
     */
    public String getEsito() { return esito; }

    /**
     * Aggiorna o imposta il testo descrittivo dell'esito clinico.
     * @param esito Il nuovo referto o esito da associare.
     */
    public void setEsito(String esito) { this.esito = esito; }

    /**
     * Restituisce l'ID del ricovero ospedaliero entro cui è stata svolta l'attività.
     * @return int L'ID del ricovero.
     */
    public int getIdRicovero() { return idRicovero; }

    /**
     * Configura il collegamento con il ricovero ospedaliero di riferimento.
     * @param idRicovero L'identificativo del ricovero da mappare.
     */
    public void setIdRicovero(int idRicovero) { this.idRicovero = idRicovero; }

    /**
     * Restituisce lo username del medico autore della prestazione.
     * @return String Lo username del medico.
     */
    public String getUsernameMedico() { return usernameMedico; }

    /**
     * Configura il collegamento con il medico autore della prestazione.
     * @param usernameMedico Lo username da associare.
     */
    public void setUsernameMedico(String usernameMedico) { this.usernameMedico = usernameMedico; }
}