package ma.fstt.dao;

import ma.fstt.entities.Produit;
import ma.fstt.service.CrudService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO implements CrudService<Produit> {

    @Override
    public Produit findById(int id) throws SQLException {
        String sql = "SELECT id, nom, description, prix, stock "
                + "FROM produit WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapProduit(result);
                }
            }
        }

        return null;
    }

    @Override
    public List<Produit> findAll() throws SQLException {
        String sql = "SELECT id, nom, description, prix, stock FROM produit";
        List<Produit> produits = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                produits.add(mapProduit(result));
            }
        }

        return produits;
    }

    @Override
    public void save(Produit produit) throws SQLException {
        String sql = "INSERT INTO produit (nom, description, prix, stock) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, produit.getNom());
            statement.setString(2, produit.getDescription());
            statement.setBigDecimal(3, produit.getPrix());
            statement.setInt(4, produit.getStock());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    produit.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Produit produit) throws SQLException {
        String sql = "UPDATE produit "
                + "SET nom = ?, description = ?, prix = ?, stock = ? "
                + "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produit.getNom());
            statement.setString(2, produit.getDescription());
            statement.setBigDecimal(3, produit.getPrix());
            statement.setInt(4, produit.getStock());
            statement.setInt(5, produit.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM produit WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Produit mapProduit(ResultSet result) throws SQLException {
        Produit produit = new Produit();
        produit.setId(result.getInt("id"));
        produit.setNom(result.getString("nom"));
        produit.setDescription(result.getString("description"));
        produit.setPrix(result.getBigDecimal("prix"));
        produit.setStock(result.getInt("stock"));
        return produit;
    }
}