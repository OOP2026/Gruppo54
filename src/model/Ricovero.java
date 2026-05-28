package model;

import java.time.LocalDateTime;

public class Ricovero {
    private Paziente paziente;
    private Letto letto;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraDimissionePrevista;
    private LocalDateTime dataOraDimissioneEffettiva;

    public Ricovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraDimissionePrevista) {
        this.paziente = paziente;
        this.letto = letto;
        this.dataOraInizio = dataOraInizio;
        this.dataOraDimissionePrevista = dataOraDimissionePrevista;
    }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Letto getLetto() { return letto; }
    public void setLetto(Letto letto) { this.letto = letto; }

    public LocalDateTime getDataOraInizio() { return dataOraInizio; }
    public void setDataOraInizio(LocalDateTime dataOraInizio) { this.dataOraInizio = dataOraInizio; }

    public LocalDateTime getDataOraDimissionePrevista() { return dataOraDimissionePrevista; }
    public void setDataOraDimissionePrevista(LocalDateTime dataOraDimissionePrevista) { this.dataOraDimissionePrevista = dataOraDimissionePrevista; }

    public LocalDateTime getDataOraDimissioneEffettiva() { return dataOraDimissioneEffettiva; }
    public void setDataOraDimissioneEffettiva(LocalDateTime dataOraDimissioneEffettiva) { this.dataOraDimissioneEffettiva = dataOraDimissioneEffettiva; }
}