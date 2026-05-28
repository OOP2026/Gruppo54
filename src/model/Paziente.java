package model;

public class Paziente {
    private String codiceFiscale;
    private String nome;
    private String cognome;

    public Paziente(String codiceFiscale, String nome, String cognome) {
        this.codiceFiscale = codiceFiscale;
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getCodiceFiscale() { return codiceFiscale; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
}