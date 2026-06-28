package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Rappresenta l'entità Stanza all'interno della struttura ospedaliera.
 * Aggrega una collezione di posti letto geograficamente collocati al suo interno
 * e tiene traccia della propria collocazione logistica (numero e piano).
 */
public class Stanza {
    /** Numero identificativo della stanza, utilizzato come Chiave Primaria (PK) nel database. */
    private int numeroStanza;

    /** Il piano dell'edificio ospedaliero in cui si trova la stanza. */
    private int piano;

    /** * Elenco dei letti configurati e presenti all'interno della stanza.
     * RISOLTO CODE SMELL: Applicato il modificatore 'final' per garantire l'immutabilità del reference alla lista.
     */
    private final List<Letto> letti;

    /**
     * Costruttore completo della classe Stanza.
     * Inizializza il numero della stanza, il piano e predispone una lista vuota per i letti.
     *
     * @param numeroStanza Il numero identificativo della stanza.
     * @param piano        Il piano di ubicazione della stanza.
     */
    public Stanza(int numeroStanza, int piano) {
        this.numeroStanza = numeroStanza;
        this.piano = piano;
        this.letti = new ArrayList<>();
    }

    /**
     * Restituisce il numero identificativo della stanza.
     * @return int Il numero della stanza.
     */
    public int getNumeroStanza() { return numeroStanza; }

    /**
     * Configura il numero identificativo della stanza.
     * @param numeroStanza Il nuovo numero da impostare.
     */
    public void setNumeroStanza(int numeroStanza) { this.numeroStanza = numeroStanza; }

    /**
     * Restituisce il piano in cui è ubicata la stanza.
     * @return int Il numero del piano.
     */
    public int getPiano() { return piano; }

    /**
     * Configura il piano di ubicazione della stanza.
     * @param piano Il numero del piano da impostare.
     */
    public void setPiano(int piano) { this.piano = piano; }

    /**
     * Restituisce la lista dei letti contenuti all'interno della stanza.
     * @return List&lt;Letto&gt; L'elenco dei letti associati.
     */
    public List<Letto> getLetti() { return letti; }
}