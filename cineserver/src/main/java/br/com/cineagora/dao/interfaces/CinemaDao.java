package br.com.cineagora.dao.interfaces;

import java.util.Set;

import br.com.cineagora.model.Cinema;

public interface CinemaDao extends GenericDao<Cinema>{
	public Set<? extends Cinema> findAll(Class<? extends Cinema> clazz);
	
}
