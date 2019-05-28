package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDAO {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO DEPARTMENT " + "(Name) "
					+ "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					obj.setId(rs.getInt(1));
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE DEPARTMENT "
					+ "SET Name = ? " + "WHERE ID = ?");
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());

			ps.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void deletById(Integer id) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("DELETE FROM DEPARTMENT WHERE ID=?");
			ps.setInt(1, id);
			ps.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT DEPARTMENT.* " + "FROM DEPARTMENT "
							+ "WHERE DEPARTMENT.ID = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Department dept = instantiateDepartment(rs);
				return dept;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);

		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setId(rs.getInt("Id"));
		dept.setName(rs.getString("Name"));
		return dept;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT DEPARTMENT.* " + "FROM DEPARTMENT "
							+ "ORDER BY NAME");
			rs = ps.executeQuery();
			List<Department> depts = new ArrayList<>();
			while (rs.next()) {
				Department dept = instantiateDepartment(rs);
				depts.add(dept);
			}
			return depts;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);

		}
	}

	

}
