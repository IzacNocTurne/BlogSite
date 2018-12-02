package com.cos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.cos.dto.SMSVO;
import com.cos.util.DBManager;

public class AdminDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;

	// insert_sms
	public int insert_sms(SMSVO sms) {
		String SQL = "INSERT INTO sms(id, msg, writedate, replycheck) VALUES(?, ?, now(), false)";
		Connection conn = DBManager.getConnection();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, sms.getId());
			pstmt.setString(2, sms.getMsg());
			pstmt.executeUpdate();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		return -1;
	}
}
