package br.com.cineagora.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.Filme;
import br.com.cineagora.model.element.CinemaElement;
import br.com.cineagora.util.colecoes.SortCinemaElementNome;
import br.com.cineagora.util.colecoes.SortFilmeCartazHorario;

@Repository
public class CinemaDaoImpl extends GenericDaoImpl<Cinema> implements CinemaDao {
	@PersistenceContext
	private EntityManager em;
	private static final Logger LOG = LogManager.getLogger(CinemaDaoImpl.class.getName());

	long inicioAuditoria;

	/**
	 * Recupera elementos do tipo Cinema recebido como parametro
	 * 
	 * @param Class
	 *            <? extends Cinema>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<? extends Cinema> findAll(Class<? extends Cinema> clazz) {
		TypedQuery<CinemaElement> query = null;
		List<CinemaElement> cinemas = null;
		if (clazz.getName().equals(Cinema.class.getName())) {
			// Nao ha necessidade de implementar ainda.

		} else if (clazz.getName().equals(CinemaElement.class.getName())) {

			query = em.createNamedQuery("CinemaElement.findAll", CinemaElement.class);
			cinemas = query.getResultList();
			for (Cinema c : cinemas) {
				//Ordena Filmes
				Set<Filme> filmes = new TreeSet<Filme>(new SortFilmeCartazHorario());
				filmes.addAll(c.getFilmes());
				c.setFilmes(filmes);
				
				for (Filme filme : filmes) {
					if(filme != null)
						filme.setCinemas(new HashSet<Cinema>());
					else 
						throw new RuntimeException("Filme nao poderia vir null");
				}
			}
		} else {
			throw new RuntimeException("Recebeu como parametro outra classe filha de Cinema");
		}
		
		//Comparable no CinemaElment da lista cinemas e em Filmes, na lista cinemas.filmes
		Collections.sort(cinemas, new SortCinemaElementNome());
		
		
		return cinemas;
	}

	@Override
	public void createAll(List<? extends Cinema> listaDeCinemas) {
		for (Cinema c : listaDeCinemas) {
			this.create(c);
		}
		
	}
	
}
