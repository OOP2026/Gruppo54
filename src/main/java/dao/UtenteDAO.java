package dao;

import model.Utente;

/**
 * Interfaccia DAO per la gestione dell'autenticazione, della sicurezza e dei profili utente del sistema.
 */
public interface UtenteDAO {

    /**
     * Autentica un utente verificando la corrispondenza delle sue credenziali d'accesso.
     * * @param login Lo username univoco dell'utente.
     * @param password La chiave di sicurezza associata.
     * @return Utente L'istanza polimorfica dell'utente autenticato (Medico o Amministratore), null se errate.
     */
    Utente login(String login, String password);
}