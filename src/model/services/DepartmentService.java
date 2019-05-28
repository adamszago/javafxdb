package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DAOFactory;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentService {

	DepartmentDAO dao = DAOFactory.createDepartmentDAO();
	
	public List<Department> findAll() {
		/*List<Department> lista = new ArrayList<>();
		lista.add(new Department(1, "Livraria"));
		lista.add(new Department(2, "Escritório"));
		lista.add(new Department(3, "Eletrodomésticos"));
		lista.add(new Department(4, "Cuidados Pessoais"));*/
		return dao.findAll();
	}
}
