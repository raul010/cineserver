package br.com.cineagora.util.agendamentos;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.ehcache.CacheManager;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

public class Diario {
	@PersistenceContext
	private EntityManager em;
	private Session session;
	private SessionFactory sf;

	
	// Deleta todos os Registros da as 3 horas da madrugada e em seguida, deleta
	// o cache
	@Transactional
	@Scheduled(cron = "0 25 13 * * ?")
	public void deletaReigstrosDoBancoDeDados() {
		session = em.unwrap(Session.class);
		sf = session.getSessionFactory();

		Map<String, ClassMetadata> m = sf.getAllClassMetadata();

		// Deleta todos registros das tabelas, iterando pelo nome dela.
		for (ClassMetadata tabela : m.values()) {
			AbstractEntityPersister entidade = (AbstractEntityPersister) tabela;

			String hql = String.format("delete from %s", entidade.getTableName());
			Query query = session.createQuery(hql);
			query.setLockMode("deleta_tudo", LockMode.WRITE);
			query.executeUpdate();
		}
		deletaCache();
	}

	// //Deleta todos registros das tabelas, iterando pelo nome dela.
	// for(String key : m.keySet()){
	// AbstractEntityPersister aep = (AbstractEntityPersister) m.get(key);
	// String hql = String.format("delete from %s",aep.getTableName());
	// Query query = session.createQuery(hql);
	// query.executeUpdate();
	// }
	@Transactional
	private void deletaCache() {
		//http://ehcache.org/documentation/code-samples
		//http://ehcache.org/xref-test/net/sf/ehcache/CacheManagerTest.html
		
		CacheManager instanceManager = CacheManager.getInstance();
		if(instanceManager != null)
			instanceManager.shutdown();
		
		
		
		
	}
}
