package model.services;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.SellerDAO;
import model.entities.Seller;

public class SellerService {

	SellerDAO dao = DAOFactory.createSellerDAO();
	
	public List<Seller> findAll() {
		return dao.findAll();
	}
}
