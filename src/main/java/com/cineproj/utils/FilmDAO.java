package com.cineproj.utils;

import com.cineproj.model.Film;

import java.sql.*;

public class FilmDAO {

    public void insertFilm(Film film) {
        String sql = "INSERT INTO film (titre, duree_en_minute, lang, soustitres, realisateur, acteurs, age_min) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getTitre());
            stmt.setInt(2, film.getDureeEnMinute());
            stmt.setString(3, film.getLang());
            stmt.setString(4, film.getSoustitres());
            stmt.setString(5, film.getRealisateur());
            stmt.setString(6, String.join(",", film.getActeurs()));
            stmt.setInt(7, film.getAgeMin());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
