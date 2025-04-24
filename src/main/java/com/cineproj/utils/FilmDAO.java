package com.cineproj.utils;

import com.cineproj.model.Film;
import com.cineproj.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FilmDAO {

    // Insert un film dans Supabase
    public void insertFilm(Film film) throws SQLException {
        String sql = "INSERT INTO films (titre, duree_en_minute, lang, soustitres, realisateur, acteurs, age_min) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, film.getTitre());
        stmt.setInt(2, film.getDureeEnMinute());
        stmt.setString(3, film.getLang());
        stmt.setString(4, film.getSoustitres());
        stmt.setString(5, film.getRealisateur());
        stmt.setString(6, String.join(",", film.getActeurs()));
        stmt.setInt(7, film.getAgeMin());

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            UUID generatedId = UUID.fromString(rs.getString("id"));
            film.setId(generatedId);
        }

        } 

    public List<Film> getAllFilms() throws SQLException {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM films";

        Connection conn = Database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Film film = new Film();
            film.setId(UUID.fromString(rs.getString("id")));
            film.setTitre(rs.getString("titre"));
            film.setDureeEnMinute(rs.getInt("duree_en_minute"));
            film.setLang(rs.getString("lang"));
            film.setSoustitres(rs.getString("soustitres"));
            film.setRealisateur(rs.getString("realisateur"));
            String acteursStr = rs.getString("acteurs");
            film.setActeurs(Arrays.asList(acteursStr.split(",")));
            film.setAgeMin(rs.getInt("age_min"));

            films.add(film);
        }

        return films;
    }

	public Film getFilmById(UUID filmId) throws SQLException {
		List<Film> films = new ArrayList<>();
		String sql = "SELECT * FROM films WHERE id = ?";
		
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, filmId.toString());
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next()) {
			Film film = new Film();
            film.setId(UUID.fromString(rs.getString("id")));
            film.setTitre(rs.getString("titre"));
            film.setDureeEnMinute(rs.getInt("duree_en_minute"));
            film.setLang(rs.getString("lang"));
            film.setSoustitres(rs.getString("soustitres"));
            film.setRealisateur(rs.getString("realisateur"));
            String acteursStr = rs.getString("acteurs");
            film.setActeurs(Arrays.asList(acteursStr.split(",")));
            film.setAgeMin(rs.getInt("age_min"));
            
            films.add(film);
		}
		
		return films.get(0);
	}
}
