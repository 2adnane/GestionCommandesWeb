package ma.fstt.dao;

import ma.fstt.entities.Client;
import ma.fstt.service.CrudService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements CrudService<Client> {

    @Override
    public Client findById(int id) throws SQLException {
        String sql = "SELECT id, nom, email, adresse FROM client WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapClient(result);
                }
            }
        }

        return null;
    }

    @Override
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT id, nom, email, adresse FROM client";
        List<Client> clients = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                clients.add(mapClient(result));
            }
        }

        return clients;
    }

    @Override
    public void save(Client client) throws SQLException {
        String sql = "INSERT INTO client (nom, email, adresse) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, client.getNom());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getAdresse());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    client.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET nom = ?, email = ?, adresse = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getNom());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getAdresse());
            statement.setInt(4, client.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Client mapClient(ResultSet result) throws SQLException {
        Client client = new Client();
        client.setId(result.getInt("id"));
        client.setNom(result.getString("nom"));
        client.setEmail(result.getString("email"));
        client.setAdresse(result.getString("adresse"));
        return client;
    }
}