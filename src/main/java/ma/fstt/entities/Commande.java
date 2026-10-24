package ma.fstt.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Commande {

    private int id;
    private LocalDateTime dateCommande;
    private String statut;
    private Client client;
    private List<LigneDeCommande> lignes;

    public Commande() {
        this.lignes = new ArrayList<>();
    }

    public Commande(int id, LocalDateTime dateCommande,
                    String statut, Client client) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.client = client;
        this.lignes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<LigneDeCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneDeCommande> lignes) {
        this.lignes = lignes;
    }
}