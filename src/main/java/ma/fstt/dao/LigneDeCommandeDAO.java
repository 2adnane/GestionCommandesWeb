package ma.fstt.dao;

import ma.fstt.entities.Client;
import ma.fstt.entities.Commande;
import ma.fstt.entities.LigneDeCommande;
import ma.fstt.entities.Produit;
import ma.fstt.service.CrudService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LigneDeCommandeDAO
        implements CrudService<LigneDeCommande> {

    private static final String SELECT_COMPLET =
            "SELECT lc.id AS ligne_id, lc.quantite, " +
                    "lc.prix_unitaire, " +
                    "c.id AS commande_id, c.date_commande, c.status AS statut, " +
                    "cl.id AS client_id, cl.nom AS client_nom, " +
                    "cl.email AS client_email, cl.adresse AS client_adresse, " +
                    "p.id AS produit_id, p.nom AS produit_nom, " +
                    "p.description AS produit_description, " +
                    "p.prix AS produit_prix, p.stock AS produit_stock " +
                    "FROM ligne_commande lc " +
                    "JOIN commande c ON lc.commande_id = c.id " +
                    "JOIN client cl ON c.client_id = cl.id " +
                    "JOIN produit p ON lc.produit_id = p.id ";

    @Override
    public LigneDeCommande findById(int id) throws SQLException {
        String sql = SELECT_COMPLET + "WHERE lc.id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapLigne(result);
                }
            }
        }

        return null;
    }

    @Override
    public List<LigneDeCommande> findAll() throws SQLException {
        String sql = SELECT_COMPLET + "ORDER BY lc.id";
        List<LigneDeCommande> lignes = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                lignes.add(mapLigne(result));
            }
        }

        return lignes;
    }

    public List<LigneDeCommande> findByCommandeId(int commandeId)
            throws SQLException {

        String sql = SELECT_COMPLET
                + "WHERE c.id = ? "
                + "ORDER BY lc.id";

        List<LigneDeCommande> lignes = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, commandeId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    lignes.add(mapLigne(result));
                }
            }
        }

        return lignes;
    }


    @Override
    public void save(LigneDeCommande ligne) throws SQLException {
        String sql = "INSERT INTO ligne_commande " +
                "(commande_id, produit_id, quantite, prix_unitaire) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, ligne.getCommande().getId());
            statement.setInt(2, ligne.getProduit().getId());
            statement.setInt(3, ligne.getQuantite());
            statement.setBigDecimal(4, ligne.getPrixUnitaire());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    ligne.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(LigneDeCommande ligne) throws SQLException {
        String sql = "UPDATE ligne_commande " +
                "SET commande_id = ?, produit_id = ?, " +
                "quantite = ?, prix_unitaire = ? " +
                "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, ligne.getCommande().getId());
            statement.setInt(2, ligne.getProduit().getId());
            statement.setInt(3, ligne.getQuantite());
            statement.setBigDecimal(4, ligne.getPrixUnitaire());
            statement.setInt(5, ligne.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ligne_commande WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private LigneDeCommande mapLigne(ResultSet result)
            throws SQLException {

        Client client = new Client();
        client.setId(result.getInt("client_id"));
        client.setNom(result.getString("client_nom"));
        client.setEmail(result.getString("client_email"));
        client.setAdresse(result.getString("client_adresse"));

        Commande commande = new Commande();
        commande.setId(result.getInt("commande_id"));
        commande.setDateCommande(
                result.getTimestamp("date_commande").toLocalDateTime());
        commande.setStatut(result.getString("statut"));
        commande.setClient(client);

        Produit produit = new Produit();
        produit.setId(result.getInt("produit_id"));
        produit.setNom(result.getString("produit_nom"));
        produit.setDescription(result.getString("produit_description"));
        produit.setPrix(result.getBigDecimal("produit_prix"));
        produit.setStock(result.getInt("produit_stock"));

        LigneDeCommande ligne = new LigneDeCommande();
        ligne.setId(result.getInt("ligne_id"));
        ligne.setQuantite(result.getInt("quantite"));
        ligne.setPrixUnitaire(result.getBigDecimal("prix_unitaire"));
        ligne.setCommande(commande);
        ligne.setProduit(produit);

        return ligne;
    }
}