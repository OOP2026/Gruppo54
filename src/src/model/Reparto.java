package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Rappresenta l'entità Reparto all'interno della struttura ospedaliera.
 * Gestisce la ripartizione logica e medica dell'ospedale (es. Cardiologia, Terapia Intensiva)
 * e aggrega al suo interno la collezione delle stanze fisiche ad esso assegnate.
 */
public class Reparto {
    /** Identificativo univoco del reparto utilizzato come Chiave Primaria (PK) nel database. */
    private int idReparto;

    /** Nome descrittivo del reparto ospedaliero. */
    private String nome;

    /** * Elenco delle stanze associate a questo reparto.
     * RISOLTO CODE SMELL: Dichiarata come 'final' per preservare l'integrità del riferimento alla collezione.
     */
    private final List<Stanza> stanze;

    /**
     * Costruttore completo della classe Reparto.
     * Inizializza l'identificativo, il nome e predispone la lista vuota delle stanze.
     *
     * @param idReparto L'identificativo univoco del reparto.
     * @param nome      Il nome del reparto.
     */
    public Reparto(int idReparto, String nome) {
        this.idReparto = idReparto;
        this.nome = nome;
        this.stanze = new ArrayList<>();
    }

    /**
     * Restituisce l'identificativo univoco del reparto.
     * @return int L'ID del reparto.
     */
    public int getIdReparto() { return idReparto; }

    /**
     * Restituisce il nome del reparto.
     * @return String Il nome.
     */
    public String getNome() { return nome; }

    /**
     * Configura l'identificativo univoco del reparto.
     * @param idReparto Il nuovo ID da assegnare.
     */
    public void setIdReparto(int idReparto) { this.idReparto = idReparto; }

    /**
     * Configura il nome del reparto.
     * @param nome Il nuovo nome da impostare.
     */
    public void setNome(String nome) { this.nome = nome; }

    /**
     * Restituisce l'elenco delle stanze allocate nel reparto.
     * @return List&lt;Stanza&gt; La lista delle stanze.
     */
    public List<Stanza> getStanze() { return stanze; }

    /**
     * Metodo di utilità per aggiungere una stanza alla collezione interna del reparto.
     * @param s L'oggetto Stanza da inserire.
     */
    public void addStanza(Stanza s) {
        this.stanze.add(s);
    }
}