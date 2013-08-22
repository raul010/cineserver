package br.com.cineagora.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.Filme;
import br.com.cineagora.model.element.CinemaElement;

@Repository
public class CinemaDaoImpl extends GenericDaoImpl<Cinema> implements CinemaDao {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Recupera elementos do tipo Cinema recebido como parametro
	 * @param Class<? extends Cinema>
	 */
	@Override
	@Transactional(readOnly=true)
	public Set<? extends Cinema> findAll(Class<? extends Cinema> clazz) {
		TypedQuery<CinemaElement> query = null;
		Set<CinemaElement> cinemas = new HashSet<CinemaElement>();
		
		if(clazz.getName().equals(Cinema.class.getName())) {
			//Nao ha necessidade de implementar ainda.
			
		} else if (clazz.getName().equals(CinemaElement.class.getName())){
			
			//Analisa API Cache
			Session session = em.unwrap(Session.class); 
			SessionFactory sf = session.getSessionFactory();
			SecondLevelCacheStatistics secStat = null;
			Map cacheEntries = null;
			Statistics stat = sf.getStatistics();
			if (stat != null)
				secStat = stat.getSecondLevelCacheStatistics("CinemaRegion");
				if(secStat != null 
						&& secStat.getEntries() != null
						&& secStat.getEntries().size() != 0)
					cacheEntries = secStat.getEntries();
			
				
			query = em.createNamedQuery("CinemaElement.findAll", CinemaElement.class);
			cinemas.addAll(query.getResultList());
			
			//Para serializar em JSON deve inicializar a Cinema.filme.cinemas, porem 
			//nao e necessario inicializar com os dados da base para esta apresentacao
			Iterator<CinemaElement> it = cinemas.iterator();
			while (it.hasNext()) {
				Set<Filme> filmes = it.next().getFilmes();
				//Hibernate.initialize(filmes);
				
				for(Filme filme : filmes) {
					filme.setCinemas(new HashSet<Cinema>());
				}
			}
			
			if (stat != null)
				secStat = stat.getSecondLevelCacheStatistics("CinemaRegion");
//				if(secStat != null 
//						&& secStat.getEntries() != null
//						&& secStat.getEntries().size() != 0)
//					cacheEntries = secStat.getEntries();
			
			
		} else {
			throw new RuntimeException("Recebeu como parametro outra classe filha de Cinema");
		}
		
		return cinemas;
	}
}
