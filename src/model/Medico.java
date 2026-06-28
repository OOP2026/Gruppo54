package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta l'entità Medico all'interno del sistema ospedaliero.
 * Estende la classe {@link Utente} specializzandola con attributi clinici
 * e mantenendo il riferimento alle prestazioni sanitarie da lui erogate.
 */
public class Medico extends Utente {
    /** L'area di specializzazione medica del professionista (es. Cardiologia, Chirurgia). */
    private String specializzazione;

    /** Elenco delle prestazioni cliniche (visite/interventi) eseguite dal medico. */
    private List<Prestazione> prestazioniEseguite;

    /**
     * Costruttore completo per la classe Medico.
     * Inizializza i dati anagrafici richiamando la superclasse e configura l'area clinica.
     *
     * @param login            Lo username univoco utilizzato per l'autenticazione.
     * @param nome             Il nome del medico.
     * @param cognome          Il cognome del medico.
     * @param specializzazione L'area di specializzazione medica.
     */
    public Medico(String login, String nome, String cognome, String specializzazione) {
        super(login, nome, cognome);
        this.specializzazione = specializzazione;
        this.prestazioniEseguite = new ArrayList<>();
    }

    /**
     * Restituisce la lista delle prestazioni eseguite dal medico.
     * @return List&lt;Prestazione&gt; Elenco delle prestazioni.
     */
    public List<Prestazione> getPrestazioniEseguite() {
        return prestazioniEseguite;
    }

    /**
     * Restituisce l'area di specializzazione del medico.
     * @return String La specializzazione.
     */
    public String getSpecializzazione() {
        return specializzazione;
    }

    /**
     * Configura l'elenco delle prestazioni eseguite dal medico.
     * @param prestazioniEseguite La nuova lista di prestazioni da associare.
     */
    public void setPrestazioniEseguite(List<Prestazione> prestazioniEseguite) {
        this.prestazioniEseguite = prestazioniEseguite;
    }

    /**
     * Configura l'area di specializzazione del medico.
     * @param specializzazione La stringa descrittiva della specializzazione.
     */
    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }

}