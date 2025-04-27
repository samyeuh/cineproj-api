package com.cineproj.dao;

import com.cineproj.model.Projection;
import com.cineproj.utils.Database;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Map.Entry;

import org.postgresql.util.PGobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectionDAO {

	private ObjectMapper mapper = new ObjectMapper();
    public FilmDAO filmDAO = new FilmDAO();
    public CinemaDAO cinemaDAO = new CinemaDAO();

    public void insertProjection(Projection projection) throws SQLException, JsonProcessingException {
        String sql = "INSERT INTO projection (film_id, cinema_id, date_debut, date_fin, calendrier) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";

        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setObject(1, projection.getFilm());
        stmt.setObject(2, projection.getCinema());
        stmt.setDate(3, Date.valueOf(projection.getDateDebut()));
        stmt.setDate(4, Date.valueOf(projection.getDateFin()));

        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        jsonObject.setValue(mapper.writeValueAsString(projection.getCalendrier()));
        stmt.setObject(5, jsonObject);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            projection.setId(UUID.fromString(rs.getString("id")));
        }
    }
    
    public void updateProjection(Projection projection) throws SQLException, JsonProcessingException {
        Connection conn = Database.getConnection();

        // Vérifier si le film existe
        PreparedStatement filmCheckStmt = conn.prepareStatement("SELECT id FROM films WHERE id = ?");
        filmCheckStmt.setObject(1, projection.getFilm().getId());
        ResultSet filmCheckRs = filmCheckStmt.executeQuery();
        if (!filmCheckRs.next()) {
            throw new IllegalArgumentException("Film ID does not exist in the database.");
        }

        // Vérifier si le cinema existe
        PreparedStatement cinemaCheckStmt = conn.prepareStatement("SELECT id FROM cinemas WHERE id = ?");
        cinemaCheckStmt.setObject(1, projection.getCinema().getId());
        ResultSet cinemaCheckRs = cinemaCheckStmt.executeQuery();
        if (!cinemaCheckRs.next()) {
            throw new IllegalArgumentException("Cinema ID does not exist in the database.");
        }

        
        String updateProjectionSql = "UPDATE projections SET film_id = ?, cinema_id = ?, start_date = ?, end_date = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(updateProjectionSql);
        stmt.setObject(1, projection.getFilm().getId());
        stmt.setObject(2, projection.getCinema().getId());
        stmt.setDate(3, Date.valueOf(projection.getDateDebut()));
        stmt.setDate(4, Date.valueOf(projection.getDateFin()));
        stmt.setObject(5, projection.getId());
        stmt.executeUpdate();

        
        PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM projection_schedule WHERE projection_id = ?");
        stmt2.setObject(1, projection.getId());
        stmt2.executeUpdate();

        
        if (projection.getCalendrier() != null && !projection.getCalendrier().isEmpty()) {
            String insertScheduleSql = "INSERT INTO projection_schedule (projection_id, day_of_week, hour) VALUES (?, ?, ?)";
            PreparedStatement stmt3 = conn.prepareStatement(insertScheduleSql);
            for (Map.Entry<String, List<LocalTime>> entry : projection.getRawCalendrier().entrySet()) {
                String day = entry.getKey();
                for (LocalTime time : entry.getValue()) {
                    if (time != null) {
                        stmt3.setObject(1, projection.getId());
                        stmt3.setString(2, day.toLowerCase());
                        stmt3.setTime(3, java.sql.Time.valueOf(time));
                        stmt3.addBatch();
                    }
                }
            }
            stmt3.executeBatch();
        }
    }

    
    public void deleteProjection(String id) throws SQLException {
        Connection conn = Database.getConnection();
        
        PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM projection_schedule WHERE projection_id = ?");
        stmt1.setObject(1, UUID.fromString(id.trim()));
        stmt1.executeUpdate();

        
        PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM projections WHERE id = ?");
        stmt2.setObject(1, UUID.fromString(id.trim()));
        stmt2.executeUpdate();
        
    }

    public Projection getProjectionById(String id) throws SQLException {
    	List<Projection> projections = searchProjections(id, null, null, null, null, null, null);
    	return projections.isEmpty() ? null : projections.get(0);
    }
    
    
    public List<Projection> searchProjections(String id, String film, String cinema, String dateDebut, String dateFin, List<String> daysOfWeek, String hour) throws SQLException {
    	Map<UUID, Projection> projectionMap = new LinkedHashMap<>();
    	StringBuilder sql = new StringBuilder("SELECT projections.*, projection_schedule.day_of_week, projection_schedule.hour\r\n" + 
    			"FROM projections\r\n" + 
    			"INNER JOIN projection_schedule ON projections.id = projection_schedule.projection_id WHERE 1=1");
    	
    	if (id != null && !id.trim().isEmpty()) sql.append(" AND projections.id = ?");
    	if (film != null && !film.trim().isEmpty()) sql.append(" AND projections.film_id = ?");
    	if (cinema != null && !cinema.trim().isEmpty()) sql.append(" AND projections.cinema_id = ?");
    	if (dateDebut != null && !dateDebut.trim().isEmpty()) sql.append(" AND projections.start_date = ?");
    	if (dateFin != null && !dateFin.trim().isEmpty()) sql.append(" AND projections.end_date = ?");
    	if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
    	    sql.append(" AND LOWER(projection_schedule.day_of_week) IN (");
    	    sql.append(String.join(",", Collections.nCopies(daysOfWeek.size(), "LOWER(?)")));
    	    sql.append(")");
    	}
    	if (hour != null && !hour.trim().isEmpty()) sql.append(" AND projection_schedule.hour = ?");
    	
    	Connection conn = Database.getConnection();
    	PreparedStatement stmt = conn.prepareStatement(sql.toString());
    	
    	int i = 1;
    	if (id != null && !id.trim().isEmpty()) stmt.setObject(i++, UUID.fromString(id));
    	if (film != null && !film.trim().isEmpty()) stmt.setObject(i++, UUID.fromString(film));
    	if (cinema != null && !cinema.trim().isEmpty()) stmt.setObject(i++, UUID.fromString(cinema));
    	if (dateDebut != null && !dateDebut.trim().isEmpty()) stmt.setDate(i++, java.sql.Date.valueOf(dateDebut));
    	if (dateFin != null && !dateFin.trim().isEmpty()) stmt.setDate(i++, java.sql.Date.valueOf(dateFin));
    	if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
    	    for (String day : daysOfWeek) {
    	        stmt.setString(i++, day);
    	    }
    	}
    	if (hour != null && !hour.trim().isEmpty()) stmt.setTime(i++, java.sql.Time.valueOf(hour));
    	
    	ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
	        UUID projId = UUID.fromString(rs.getString("id"));
	        Projection projection = projectionMap.get(projId);
	        if (projection == null) {
	            projection = new Projection();
	            projection.setId(projId);
	            projection.setFilm(filmDAO.getFilmById(rs.getString("film_id")));
	            projection.setCinema(cinemaDAO.getCinemaById(rs.getString("cinema_id")));
	            projection.setDateDebut(LocalDate.parse(rs.getString("start_date")));
	            projection.setDateFin(LocalDate.parse(rs.getString("end_date")));
	            projection.setCalendrier(new HashMap<>());
	            projectionMap.put(projId, projection);
	        }
	        
		    String day = rs.getString("day_of_week");
		    String timeStr = rs.getString("hour");
		    if (timeStr != null) {
		        LocalTime time = LocalTime.parse(timeStr);
		        projection.getRawCalendrier()
		                  .computeIfAbsent(day, k -> new ArrayList<>())
		                  .add(time);
		   }
		}
		
		return new ArrayList<>(projectionMap.values()); 
    }
}

