package model;

/**
 * Rappresenta l'entità TurnoLavorativo all'interno del dominio ospedaliero.
 * Modella la pianificazione oraria del personale medico, definendo il giorno della settimana
 * e la specifica fascia oraria di servizio per consentire la validazione temporale delle prestazioni erogate.
 */
public class TurnoLavorativo {
    /** Il giorno della settimana associato al turno lavorativo (es. "LUNEDI", "MARTEDI"). */
    private String giorno;

    /** La fascia oraria di validità del turno, espressa nel formato standard (es. "08:00 - 14:00"). */
    private String fasciaOraria;

    /** Il riferimento all'entità {@link Medico} titolare del turno lavorativo specifico. */
    private Medico medico;

    /**
     * Costruttore completo della classe TurnoLavorativo.
     *
     * @param giorno       Il giorno della settimana in cui il turno è attivo.
     * @param fasciaOraria L'intervallo orario coperto dal turno di lavoro.
     * @param medico       L'istanza del medico associata a questa pianificazione.
     */
    public TurnoLavorativo(String giorno, String fasciaOraria, Medico medico) {
        this.giorno = giorno;
        this.fasciaOraria = fasciaOraria;
        this.medico = medico;
    }

    /**
     * Restituisce il giorno della settimana associato al turno.
     * @return String Il giorno del turno.
     */
    public String getGiorno() { return giorno; }

    /**
     * Configura il giorno della settimana associato al turno.
     * @param giorno Il nuovo giorno da assegnare.
     */
    public void setGiorno(String giorno) { this.giorno = giorno; }

    /**
     * Restituisce la fascia oraria di servizio coperta dal turno.
     * @return String La stringa della fascia oraria.
     */
    public String getFasciaOraria() { return fasciaOraria; }

    /**
     * Configura la fascia oraria di servizio coperta dal turno.
     * @param fasciaOraria La nuova stringa oraria da impostare.
     */
    public void setFasciaOraria(String fasciaOraria) { this.fasciaOraria = fasciaOraria; }

    /**
     * Restituisce l'oggetto Medico titolare del turno.
     * @return Medico L'istanza del medico associata.
     */
    public Medico getMedico() { return medico; }

    /**
     * Configura e associa il medico titolare al turno lavorativo.
     * @param medico La nuova istanza di Medico da collegare.
     */
    public void setMedico(Medico medico) { this.medico = medico; }
}