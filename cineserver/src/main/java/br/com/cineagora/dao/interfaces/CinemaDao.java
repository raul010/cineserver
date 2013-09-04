package br.com.cineagora.dao.interfaces;

import java.util.List;

import br.com.cineagora.model.Cinema;
import br.com.cineagora.util.enums.Cidade;

public interface CinemaDao extends GenericDao<Cinema>{
	public List<? extends Cinema> findAll(Class<? extends Cinema> clazz);
}
