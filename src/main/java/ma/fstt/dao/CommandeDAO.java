package ma.fstt.dao;

import ma.fstt.entities.Client;
import ma.fstt.entities.Commande;
import ma.fstt.service.CrudService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO implements CrudService<Commande> {

    private static final String SELECT_AVEC_CLIENT =
            "SELECT c.id AS commande_id, c.date_commande, c.status AS statut, " +
                    "cl.id AS client_id, cl.nom AS client_nom, " +
                    "cl.email AS client_email, cl.adresse AS client_adresse " +
                    "FROM commande c " +
                    "JOIN client cl ON c.client_id = cl.id ";

    @Override
    public Commande findById(int id) throws SQLException {
        String sql = SELECT_AVEC_CLIENT + "WHERE c.id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapCommande(result);
                }
            }
        }

        return null;
    }

    @Override
    public List<Commande> findAll() throws SQLException {
        String sql = SELECT_AVEC_CLIENT + "ORDER BY c.date_commande DESC";
        List<Commande> commandes = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                commandes.add(mapCommande(result));
            }
        }

        return commandes;
    }

    @Override
    public void save(Commande commande) throws SQLException {
        String sql = "INSERT INTO commande (status, client_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, commande.getStatut());
            statement.setInt(2, commande.getClient().getId());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    commande.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Commande commande) throws SQLException {
        String sql = "UPDATE commande "
                + "SET date_commande = ?, status = ?, client_id = ? "
                + "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(
                    1, Timestamp.valueOf(commande.getDateCommande()));
            statement.setString(2, commande.getStatut());
            statement.setInt(3, commande.getClient().getId());
            statement.setInt(4, commande.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM commande WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Commande mapCommande(ResultSet result) throws SQLException {
        Client client = new Client();
        client.setId(result.getInt("client_id"));
        client.setNom(result.getString("client_nom"));
        client.setEmail(result.getString("client_email"));
        client.setAdresse(result.getString("client_adresse"));

        Commande commande = new Commande();
        commande.setId(result.getInt("commande_id"));

        Timestamp date = result.getTimestamp("date_commande");
        if (date != null) {
            commande.setDateCommande(date.toLocalDateTime());
        }

        commande.setStatut(result.getString("statut"));
        commande.setClient(client);

        return commande;
    }
}