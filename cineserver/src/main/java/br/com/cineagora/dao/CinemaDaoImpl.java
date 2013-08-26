package br.com.cineagora.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.Filme;
import br.com.cineagora.model.apresentacao.FilmeCartaz;
import br.com.cineagora.model.element.CinemaElement;
import br.com.cineagora.util.JsoupUtil;
import br.com.cineagora.util.LogUtil;
import br.com.cineagora.util.colecoes.SortCinemaElementNome;
import br.com.cineagora.util.colecoes.SortFilmeCartazHorario;

@Repository
public class CinemaDaoImpl extends GenericDaoImpl<Cinema> implements CinemaDao {
	@PersistenceContext
	private EntityManager em;
	static Log log;
	static {
		log = LogUtil.openLog(CinemaDaoImpl.class);
	}
	long inicioAuditoria;

	/**
	 * Recupera elementos do tipo Cinema recebido como parametro
	 * 
	 * @param Class
	 *            <? extends Cinema>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<? extends Cinema> findAll(Class<? extends Cinema> clazz) {

		TypedQuery<CinemaElement> query = null;
		List<CinemaElement> cinemas = null;
		if (clazz.getName().equals(Cinema.class.getName())) {
			// Nao ha necessidade de implementar ainda.

		} else if (clazz.getName().equals(CinemaElement.class.getName())) {

			// AOP
			logAuditStatsInfo();
			iniciaContagemAuditoria();

			query = em.createNamedQuery("CinemaElement.findAll", CinemaElement.class);
			cinemas = query.getResultList();
			// Para serializar em JSON deve inicializar a Cinema.filme.cinemas,
			// porem nao e necessario
			// inicializar com os dados da base para busca por de filmes, por cinemas
			for (Cinema c : cinemas) {
				List<Filme> filmes = c.getFilmes();
				Collections.sort(filmes, new SortFilmeCartazHorario());
				// Hibernate.initialize(filmes);
				for (Filme filme : filmes) {
					if(filme != null)
						filme.setCinemas(new HashSet<Cinema>());
					else 
						throw new RuntimeException("Filme não poderia vir null");
				}
			}
			finalizaContagemAuditoria();
		} else {
			throw new RuntimeException("Recebeu como parametro outra classe filha de Cinema");
		}
		
		//Comparable no CinemaElment da lista cinemas e em Filmes, na lista cinemas.filmes
		Collections.sort(cinemas, new SortCinemaElementNome());
		
		
		return cinemas;
	}

	private void iniciaContagemAuditoria() {
		// considerando show sql=false, e sem estar em modo debug.
		inicioAuditoria = System.nanoTime();
	}

	private void finalizaContagemAuditoria() {
		long fim = System.nanoTime() - inicioAuditoria;
		double segundos = (double) fim / 1000000000.0;
		System.out.println("Duracao query (s) --> " + segundos);
	}

	/**
	 * Analisa API Cache
	 */
	private void logAuditStatsInfo() {
		Session session = null;
		SessionFactory sf = null;
		Statistics stats = null;
		SecondLevelCacheStatistics secStats = null;
		
		try {
			session = em.unwrap(Session.class);
			sf= session.getSessionFactory();
			stats =  sf.getStatistics();
			secStats =  stats.getSecondLevelCacheStatistics("cinemaRegion");
			
		} catch (Throwable e) {
			log.error("Erro de Log (Inicializacao): " + e.getClass() + " " + e.getMessage());
			return;
		}
		
		try {
			log.info("");
			for (String r : stats.getCollectionRoleNames())
				log.info("Collection Rolenames: " + r);
			log.info("");
			for (String s : stats.getEntityNames()) {
				log.info("Entity Stats: " + s + " - " + stats.getEntityStatistics(s));
			}
			log.info("");

			log.info("------------- GENERAL STATS -------------");
			log.info("Connect Count: " + stats.getConnectCount());
			log.info("Flush Count: " + stats.getFlushCount());
			log.info("Prepare Statement Count: " + stats.getPrepareStatementCount());
			log.info("Close Statement Count: " + stats.getCloseStatementCount());
			log.info("Transaction Count : " + stats.getTransactionCount());

		} catch (Throwable e) {
			log.error("Erro de Log (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}
		try {

			log.info("Collection Fetch Count: " + stats.getCollectionFetchCount());
			log.info("Collection Load Count: " + stats.getCollectionLoadCount());
			log.info("Collection Recreate Count: " + stats.getCollectionRecreateCount());
			log.info("Collection Remove Count: " + stats.getCollectionRemoveCount());
			log.info("Collection Update Count: " + stats.getCollectionUpdateCount());

			log.info("Entity Delete Count: " + stats.getEntityDeleteCount());
			log.info("Entity Fetch Count: " + stats.getEntityFetchCount());
			log.info("Entity Insert Count: " + stats.getEntityInsertCount());
			log.info("Entity Load Count: " + stats.getEntityLoadCount());
			log.info("Entity Update Count: " + stats.getEntityUpdateCount());

			log.info("Natural Id Cache Hit Count: " + stats.getNaturalIdCacheHitCount());
			log.info("Natural Id Cache Miss Count: " + stats.getNaturalIdCacheMissCount());
			log.info("Natural Id Cache Put Count: " + stats.getNaturalIdCachePutCount());
			log.info("Natural Id Query Execution Count: " + stats.getNaturalIdQueryExecutionCount());
			log.info("Natural Id Query Execution Max Time: " + stats.getNaturalIdQueryExecutionMaxTime());
			log.info("Natural Id Query Execution Max Time Region: " + stats.getNaturalIdQueryExecutionMaxTimeRegion());
			log.info("Optimistic Failure Count: " + stats.getOptimisticFailureCount());

		} catch (Throwable e) {
			log.error("Erro de Log (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}
		try {

			log.info("Query Cache Hit Count: " + stats.getQueryCacheHitCount());
			log.info("Query Cache Miss Count: " + stats.getQueryCacheMissCount());
			log.info("Query Cache Put Count: " + stats.getQueryCachePutCount());
			log.info("Query Cache Execution Count: " + stats.getQueryExecutionCount());
			log.info("Query Execution Max Time: " + stats.getQueryExecutionMaxTime());
			log.info("Query Execution Max Time Query String: " + stats.getQueryExecutionMaxTimeQueryString());

			log.info("Second Level Cache Hit Count: " + stats.getSecondLevelCacheHitCount());
			log.info("Second Level Cache Miss Count: " + stats.getSecondLevelCacheMissCount());
			log.info("Second Level Cache Put Count: " + stats.getSecondLevelCachePutCount());

			log.info("Session Close Count: " + stats.getSessionCloseCount());
			log.info("Session Open Count: " + stats.getSessionOpenCount());
			log.info("Start Time: " + stats.getStartTime());
			log.info("Successful Transaction Count: " + stats.getSuccessfulTransactionCount());
		} catch (Throwable e) {
			log.error("Erro de Log (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}

		try {
			log.info("");
			log.info("Second Level Cache Region Names:");
			for (String s : stats.getSecondLevelCacheRegionNames())
				log.info(s);
			log.info("");
			log.info("Update Timestamps Cache Hit Count : " + stats.getUpdateTimestampsCacheHitCount());
			log.info("Update Timestamps Cache Miss Count : " + stats.getUpdateTimestampsCacheMissCount());
			log.info("Update Timestamps Cache Put Count : " + stats.getUpdateTimestampsCachePutCount());
		} catch (Throwable e) {
			log.error("Erro de Log (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}
		try {

			log.info("-------- 2ND LEVEL REGION cinemaRegion --------");
			log.info("2nd L Cache Put Count: " + stats.getSecondLevelCachePutCount());
			log.info("Tamanho em memoria: " + secStats.getSizeInMemory());
			log.info("Hit Count: " + secStats.getHitCount());
			log.info("Miss Count: " + secStats.getMissCount());
			log.info("Element Count In Memory: " + secStats.getElementCountInMemory());
			log.info("Element Count On Disk: " + secStats.getElementCountOnDisk());
			log.info("Element Count On Disk: " + secStats.getEntries().toString());

		} catch (Throwable e) {
			log.error("Erro de Log (2ND LEVEL REGION): " + e.getClass() + " " + e.getMessage());
		}
		try {

			log.info("----- Query Stats SELECT DISTINCT(c) FROM CinemaElement... -----");
			QueryStatistics queryStats = stats
					.getQueryStatistics("SELECT DISTINCT(c) FROM CinemaElement c LEFT JOIN FETCH c.filmes");
			log.info("Query Cache Put Count: " + queryStats.getCachePutCount());
			log.info("Query Cache Hit Count: " + queryStats.getCacheHitCount());
			log.info("Query Cache Miss Count: " + queryStats.getCacheMissCount());
			log.info("Execution Avg Time: " + queryStats.getExecutionAvgTime());
			log.info("Execution Count: " + queryStats.getExecutionCount());
			log.info("Execution Max Time: " + queryStats.getExecutionMaxTime());
			log.info("Execution Min Time: " + queryStats.getExecutionMinTime());
			log.info("Execution Row Count: " + queryStats.getExecutionRowCount());

		} catch (Throwable e) {
			log.error("Erro de Log (Query Stats SELECT DISTINCT(c) FROM CinemaElement...): " + e.getClass() + " "
					+ e.getMessage());
		}
		try {

			log.info("----- ENTITY STATS -----");
			EntityStatistics entityStats = stats.getEntityStatistics(Cinema.class.getName());
			log.info("Insert Count: " + entityStats.getInsertCount());
			log.info("Update Count: " + entityStats.getUpdateCount());
			log.info("Delete Count: " + entityStats.getDeleteCount());
			log.info("Fetch Count: " + entityStats.getFetchCount());
			log.info("Load Count: " + entityStats.getLoadCount());
			log.info("Optimistic Failure Count: " + entityStats.getOptimisticFailureCount());

		} catch (Throwable e) {
			log.error("Erro de Log (ENTITY STATS): " + e.getClass() + " " + e.getMessage());
		}
	}
}
