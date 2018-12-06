package com.cos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.cos.dto.ReBoardVO;
import com.cos.util.DBManager;

public class ReBoardDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;

	// insert
	public int insert(ReBoardVO reboard) {
		String SQL = "INSERT INTO reboard VALUES(?,?,?,now(),?)";
		Connection conn = DBManager.getConnection();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, reboard.getRenum());
			pstmt.setString(2, reboard.getId());
			pstmt.setString(3, reboard.getRecontent());
			pstmt.setInt(4, reboard.getNum());
			pstmt.executeUpdate();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		return -1;
	}

	// select_renum
	public int select_renum() {
		String SQL = "SELECT MAX(renum) FROM reboard";
		Connection conn = DBManager.getConnection();
		try {
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int renum = rs.getInt(1);
				return renum;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return -1;
	}

	// select_all
	public ArrayList<ReBoardVO> select_all(int num) {
		String SQL = "SELECT * FROM reboard WHERE num = ? ORDER BY writedate DESC";
		Connection conn = DBManager.getConnection();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			ArrayList<ReBoardVO> list = new ArrayList<>();
			while (rs.next()) {
				ReBoardVO reboard = new ReBoardVO();
				reboard.setRenum(rs.getInt("renum"));
				reboard.setId(rs.getString("id"));
				reboard.setRecontent(rs.getString("recontent"));
				reboard.setWirtedate(rs.getString("writedate"));
				reboard.setNum(rs.getInt("num"));
				list.add(reboard);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt, rs);
		}
		return null;
	}

	// delete
	public int delete(int renum) {
		String SQL = "DELETE FROM reboard WHERE renum = ?";
		Connection conn = DBManager.getConnection();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, renum);
			pstmt.executeUpdate();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(conn, pstmt);
		}
		return -1;
	}
	
	// delete_boardnum
	public int delete_num(int num) {
		String SQL = "DELETE FROM reboard WHERE num = ?";
		Connection conn = DBManager.getConnection();
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, num);
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
