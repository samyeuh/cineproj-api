package com.cineproj.utils;

import com.cineproj.model.Cinema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CinemaDAO {

    // INSERT
    public void insertCinema(Cinema cinema) throws SQLException {
        String sql = "INSERT INTO cinema (name, address, city, owner_id) VALUES (?, ?, ?, ?) RETURNING id";

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, cinema.getName());
            stmt.setString(2, cinema.getAdresse());
            stmt.setString(3, cinema.getCity());
            stmt.setObject(4, cinema.getOwner_id());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cinema.setId(UUID.fromString(rs.getString("id")));
            }
        }

    // SELECT ALL
    public List<Cinema> getAllCinemas() throws SQLException {
        List<Cinema> cinemas = new ArrayList<>();
        String sql = "SELECT * FROM cinema";

        Connection conn = Database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            cinemas.add(mapCinema(rs));
        }

        return cinemas;
    }

    // SELECT BY ID
    public Cinema getCinemaById(UUID id) throws SQLException {
        String sql = "SELECT * FROM cinema WHERE id = ?";

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setObject(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return mapCinema(rs);
        }

        return null;
    }

    // UPDATE
    public boolean updateCinema(Cinema cinema) {
        String sql = "UPDATE cinema SET name = ?, address = ?, city = ?, owner_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cinema.getName());
            stmt.setString(2, cinema.getAdresse());
            stmt.setString(3, cinema.getCity());
            stmt.setObject(4, cinema.getOwner_id());
            stmt.setObject(5, cinema.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteCinema(UUID id) {
        String sql = "DELETE FROM cinema WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // MAP CINEMA
    private Cinema mapCinema(ResultSet rs) throws SQLException {
        Cinema cinema = new Cinema();
        cinema.setId(UUID.fromString(rs.getString("id")));
        cinema.setName(rs.getString("name"));
        cinema.setAdresse(rs.getString("address"));
        cinema.setCity(rs.getString("city"));
        cinema.setOwner_id(UUID.fromString(rs.getString("owner_id")));
        return cinema;
    }
}
