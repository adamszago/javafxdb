package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {

	
	public List<Department> findAll() {
		List<Department> lista = new ArrayList<>();
		lista.add(new Department(1, "Livraria"));
		lista.add(new Department(2, "Escritório"));
		lista.add(new Department(3, "Eletrodomésticos"));
		lista.add(new Department(4, "Cuidados Pessoais"));
		return lista;
	}
}
