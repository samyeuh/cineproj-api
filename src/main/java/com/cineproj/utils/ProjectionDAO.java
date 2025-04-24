package com.cineproj.utils;

import com.cineproj.model.Projection;
import com.cineproj.model.Film;
import com.cineproj.model.Cinema;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.postgresql.util.PGobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectionDAO {

    private ObjectMapper mapper = new ObjectMapper();

    // INSERT
    public void insertProjection(Projection projection) throws SQLException, JsonProcessingException {
        String sql = "INSERT INTO projection (film_id, cinema_id, date_debut, date_fin, calendrier) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, projection.getFilm().getId());
            stmt.setObject(2, projection.getCinema().getId());
            stmt.setDate(3, Date.valueOf(projection.getDateDebut()));
            stmt.setDate(4, Date.valueOf(projection.getDateFin()));

            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(mapper.writeValueAsString(projection.getCalendrier()));
            stmt.setObject(5, jsonObject);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                projection.setId(rs.getInt("id"));
            }
        }
    }

    // GET ALL
    public List<Projection> getAllProjections() {
        List<Projection> list = new ArrayList<>();
        String sql = "SELECT * FROM projection";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapProjection(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Mapping ResultSet → Projection
    private Projection mapProjection(ResultSet rs) throws Exception {
        Projection projection = new Projection();
        projection.setId(rs.getInt("id"));

        // Film & Cinema (tu peux optimiser en évitant de faire plusieurs requêtes)
        UUID filmId = UUID.fromString(rs.getString("film_id"));
        UUID cinemaId = UUID.fromString(rs.getString("cinema_id"));

        projection.setFilm(new FilmDAO().getFilmById(filmId));
        projection.setCinema(new CinemaDAO().getCinemaById(cinemaId));

        projection.setDateDebut(rs.getDate("date_debut").toLocalDate());
        projection.setDateFin(rs.getDate("date_fin").toLocalDate());

        // Lecture JSON -> Map<String, List<LocalTime>>
        String json = rs.getString("calendrier");
        Map<String, List<String>> rawMap = mapper.readValue(json, Map.class);

        Map<String, List<LocalTime>> converted = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : rawMap.entrySet()) {
            List<LocalTime> times = new ArrayList<>();
            for (String s : entry.getValue()) {
                times.add(LocalTime.parse(s));
            }
            converted.put(entry.getKey(), times);
        }

        projection.setCalendrier(converted);
        return projection;
    }
}

