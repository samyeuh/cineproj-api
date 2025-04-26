package com.cineproj.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.cineproj.model.User;

public class AuthDAO {
	
	public void register(User user) throws SQLException {
		String sql = "INSERT INTO users (username, password)" +
					"VALUES (?, ?) RETURNING id";
		
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		stmt.setString(1, user.getUsername());
		stmt.setString(2, user.getPassword());
		
		ResultSet rs = stmt.executeQuery();
		
		if (rs.next()) {
			user.setId(UUID.fromString(rs.getString("id")));
		}
	}
	
	public User login(User user) throws Exception {
	    String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
	    
	    Connection conn = Database.getConnection();
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    
	    stmt.setString(1, user.getUsername());
	    stmt.setString(2, user.getPassword());
	    
	    ResultSet rs = stmt.executeQuery();
	    
	    if (rs.next()) {
	        user.setId(UUID.fromString(rs.getString("id")));
	        return user;
	    }
	    
	    throw new Exception("Login incorrect");
	}

}
