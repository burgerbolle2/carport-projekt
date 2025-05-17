package app.persistence;

import app.exceptions.DatabaseException;
import app.model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {

    /**
     * Finder et materiale på dets ID.
     */
    public static Material findById(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT material_id, name, type, price, description FROM materials WHERE material_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Material(
                            rs.getInt("material_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getBigDecimal("price"),
                            rs.getString("description")
                    );
                } else {
                    return null; // eller kaste DatabaseException hvis du foretrækker det
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af materialet med ID " + id, e);
        }
    }

    /**
     * Henter alle materialer.
     */
    public static List<Material> findAll(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT material_id, name, type, price, description FROM materials";
        List<Material> list = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Material(
                        rs.getInt("material_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBigDecimal("price"),
                        rs.getString("description")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af alle materialer", e);
        }
    }

    /**
     * Opretter et nyt materiale.
     */
    public static void createMaterial(String name,
                                      String type,
                                      java.math.BigDecimal price,
                                      String description,
                                      ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO materials (name, type, price, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setBigDecimal(3, price);
            ps.setString(4, description);

            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Kunne ikke oprette materiale");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB-fejl ved oprettelse af materiale", e);
        }
    }

    /**
     * Opdaterer et eksisterende materiale.
     */
    public static void updateMaterial(int id,
                                      String name,
                                      String type,
                                      java.math.BigDecimal price,
                                      String description,
                                      ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE materials SET name=?, type=?, price=?, description=? WHERE material_id=?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setBigDecimal(3, price);
            ps.setString(4, description);
            ps.setInt(5, id);

            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Kunne ikke opdatere materiale med ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB-fejl ved opdatering af materiale", e);
        }
    }

    /**
     * Sletter et materiale.
     */
    public static void deleteMaterial(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM materials WHERE material_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DatabaseException("Kunne ikke slette materiale med ID " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB-fejl ved sletning af materiale", e);
        }
    }
}

