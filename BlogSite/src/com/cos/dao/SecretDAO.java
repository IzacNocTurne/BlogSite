package com.cos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cos.dto.SecretVO;
import com.cos.util.DBManager;

public class SecretDAO {
	PreparedStatement pstmt;
	ResultSet rs;
	
	public SecretVO getSecret(String name){
		String SQL = "SELECT * FROM secret where name = ?";
		try {
			Connection conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if(rs.next()){
				SecretVO secret = new SecretVO();
				secret.setName(rs.getString("name"));
				secret.setId(rs.getString("id"));
				secret.setPassword(rs.getString("password"));
				secret.setApikey(rs.getString("apikey"));
				secret.setApisecret(rs.getString("apisecret"));
				return secret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
