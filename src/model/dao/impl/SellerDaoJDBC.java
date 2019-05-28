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
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDAO {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO SELLER " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());

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
	public void update(Seller obj) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE SELLER "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE ID = ?");
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());

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
			ps = conn.prepareStatement("DELETE FROM SELLER WHERE ID=?");
			ps.setInt(1, id);
			ps.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT SELLER.*, DEPARTMENT.NAME AS DepName " + "FROM SELLER INNER JOIN DEPARTMENT "
							+ "ON SELLER.DEPARTMENTID = DEPARTMENT.ID " + "WHERE SELLER.ID = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Department dept = instantiateDepartment(rs);
				Seller se = instantiateSeller(rs, dept);
				return se;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);

		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dept) throws SQLException {
		Seller se = new Seller();
		se.setId(rs.getInt("Id"));
		se.setName(rs.getString("Name"));
		se.setEmail(rs.getString("Email"));
		se.setBaseSalary(rs.getDouble("BaseSalary"));
		se.setBirthDate(rs.getDate("BirthDate"));
		se.setDepartment(dept);
		return se;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dept = new Department();
		dept.setId(rs.getInt("DepartmentId"));
		dept.setName(rs.getString("DepName"));
		return dept;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT SELLER.*, DEPARTMENT.NAME AS DepName " + "FROM SELLER INNER JOIN DEPARTMENT "
							+ "ON SELLER.DEPARTMENTID = DEPARTMENT.ID " + "ORDER BY NAME");
			rs = ps.executeQuery();
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> mapDepartment = new HashMap<>();
			while (rs.next()) {
				Department dept = mapDepartment.get(rs.getObject("DEPARTMENTID"));
				if (dept == null) {
					dept = instantiateDepartment(rs);
					mapDepartment.put(rs.getInt("DEPARTMENTID"), dept);
				}

				Seller se = instantiateSeller(rs, dept);
				sellers.add(se);
			}
			return sellers;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);

		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT SELLER.*, DEPARTMENT.NAME AS DepName " + "FROM SELLER INNER JOIN DEPARTMENT "
							+ "ON SELLER.DEPARTMENTID = DEPARTMENT.ID " + "WHERE DEPARTMENT.ID = ? " + "ORDER BY NAME");
			ps.setInt(1, department.getId());
			rs = ps.executeQuery();
			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> mapDepartment = new HashMap<>();
			while (rs.next()) {
				Department dept = mapDepartment.get(rs.getObject("DEPARTMENTID"));
				if (dept == null) {
					dept = instantiateDepartment(rs);
					mapDepartment.put(rs.getInt("DEPARTMENTID"), dept);
				}

				Seller se = instantiateSeller(rs, dept);
				sellers.add(se);
			}
			return sellers;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);

		}
	}

}
