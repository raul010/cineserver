package br.com.cineagora.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import br.com.cineagora.model.Cinema;

@Component
public class Util {
	
	private static EntityManager em;
	
	public static final String UTF_8 = "UTF-8";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final Logger LOG = LogManager.getLogger(Util.class.getName());

	// ':' windows, ';' unixes
	public static final String SEPARATOR = System.getProperty("path.separator");
	// '\' windows, '/' unixes
	public static final String FILE_SEP = System.getProperty("file.separator");
	// Arquivo de properties
	public static final String SITE_FILE = getCaminhoAbsolutoArquivo("WEB-INF",
			"recursos.properties");
	
	/**
	 * atribui entity a variavel estatica para uso simples
	 * @param emg
	 */
	@PersistenceContext
	public void setEntityManager (EntityManager emg) {
		em = emg;
	}

	public static String fazDecodeDaString(String string) {
		try {
			string = URLDecoder.decode(string, UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * <p>
	 * Trabalha de acordo com o Sistema Operacional.
	 * <p>
	 * <b>Exemplo de entrada:</b> <br>
	 * "src", "main", "resources", "META-INF", "recursos.properties"
	 * <p>
	 * <b>Exemplo de retorno:</b> <br>
	 * C:\Documents and Settings\Raul\Meus documentos\Arquivos_RAUL\developer\
	 * works
	 * \CineServer\cineserver\src\main\resources\META-INF\recursos.properties
	 */
	public static String getCaminhoAbsolutoArquivo(final String... arquivos) {
		String path = "";
		for (String f : arquivos) {
			// path += FILE_SEP + f;
			path += "/" + f;
		}
		return path;
	}

	private static Properties configuraArquivoDePropriedade(InputStream is) {
		Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao ler propriedade");
		}
		return prop;
	}

	/**
	 * <br>
	 * Retorna valor de arquivo baseado em chave=valor <br>
	 * Arquivo de propriedade padrao: "/WEB-INF/recursos.properties"
	 * 
	 * @param key
	 * @return
	 */
	public static String obtemValorDaPropriedade(String key) {
		InputStream is = SpringUtils.getServletContext().getResourceAsStream(SITE_FILE);
		Properties prop = configuraArquivoDePropriedade(is);

		return  prop.get(key).toString();
	}
	/**
	 * Faz o encode correto, apenas da parte query do link
	 * @param url
	 * @return ascII
	 */
	public static String fazEncodeParaASCII(String url) {
		URL urlApi;
		URI uri;
		String ascII;
		try {
			urlApi = new URL(url);
			uri = new URI(
					urlApi.getProtocol(),
					urlApi.getUserInfo(),
					urlApi.getHost(),
					urlApi.getPort(),
					urlApi.getPath(),
					urlApi.getQuery(),
					urlApi.getRef());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			// DEBUG
			throw new RuntimeException(e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			// DEBUG
			throw new RuntimeException(e);
		}
		ascII = uri.toASCIIString();
		return ascII;
	}

	public static String unescapeHtml(Element element) {
		return StringEscapeUtils.unescapeHtml4(element.text());
	}

	public static String unescapeHtml(String texto) {
		String texoResult = StringEscapeUtils.unescapeHtml4(texto);
		return texoResult;
	}


	public static void imprime(boolean text, Elements elements) {
		for (Element elem : elements) {
			if (text == true) {
				LOG.info(Util.unescapeHtml(elem.text()));
			} else {
				LOG.info(Util.unescapeHtml(elem.toString()));
			}
		}
		LOG.info("---------------------");
	}

	/**
	 * Modifica o Charset do texto passado
	 * @param texto
	 */
	public static String  converteParaUTF8(String texto) {
		return new CharsetDetector().getString(texto.getBytes(), Util.UTF_8);
	}
	/**
	 * @param texto
	 * @return charSet
	 */
	public static String descobreEncoding(String texto) {
		return new CharsetDetector().setText(texto.getBytes()).detect().getName();
	}
	public static boolean isStringUTF8(String texto) {
		String encodingDoTexto = descobreEncoding(texto);
		
		if(encodingDoTexto.contains(Util.UTF_8)) 
			return true;
		else 
			return false;
	}
	/**
	 * Analisa API Cache
	 */
	public static void analisaDadosDaAPIDeCacheParaLog() {
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
			LOG.error("Erro de Log (Inicializacao): " + e.getClass() + " " + e.getMessage());
			return;
		}
		
		try {
			LOG.info("");
			for (String r : stats.getCollectionRoleNames())
				LOG.info("Collection Rolenames: " + r);
			LOG.info("");
			for (String s : stats.getEntityNames()) {
				LOG.info("Entity Stats: " + s + " - " + stats.getEntityStatistics(s));
			}
			LOG.info("");

			LOG.info("------------- GENERAL STATS -------------");
			LOG.info("Connect Count: " + stats.getConnectCount());
			LOG.info("Flush Count: " + stats.getFlushCount());
			LOG.info("Prepare Statement Count: " + stats.getPrepareStatementCount());
			LOG.info("Close Statement Count: " + stats.getCloseStatementCount());
			LOG.info("Transaction Count : " + stats.getTransactionCount());

		} catch (Throwable e) {
			LOG.error("Erro de LOG (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}
		try {

			LOG.info("Collection Fetch Count: " + stats.getCollectionFetchCount());
			LOG.info("Collection Load Count: " + stats.getCollectionLoadCount());
			LOG.info("Collection Recreate Count: " + stats.getCollectionRecreateCount());
			LOG.info("Collection Remove Count: " + stats.getCollectionRemoveCount());
			LOG.info("Collection Update Count: " + stats.getCollectionUpdateCount());

			LOG.info("Entity Delete Count: " + stats.getEntityDeleteCount());
			LOG.info("Entity Fetch Count: " + stats.getEntityFetchCount());
			LOG.info("Entity Insert Count: " + stats.getEntityInsertCount());
			LOG.info("Entity Load Count: " + stats.getEntityLoadCount());
			LOG.info("Entity Update Count: " + stats.getEntityUpdateCount());

			LOG.info("Natural Id Cache Hit Count: " + stats.getNaturalIdCacheHitCount());
			LOG.info("Natural Id Cache Miss Count: " + stats.getNaturalIdCacheMissCount());
			LOG.info("Natural Id Cache Put Count: " + stats.getNaturalIdCachePutCount());
			LOG.info("Natural Id Query Execution Count: " + stats.getNaturalIdQueryExecutionCount());
			LOG.info("Natural Id Query Execution Max Time: " + stats.getNaturalIdQueryExecutionMaxTime());
			LOG.info("Natural Id Query Execution Max Time Region: " + stats.getNaturalIdQueryExecutionMaxTimeRegion());
			LOG.info("Optimistic Failure Count: " + stats.getOptimisticFailureCount());

		} catch (Throwable e) {
			LOG.error("Erro de Log (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}
		try {

			LOG.info("Query Cache Hit Count: " + stats.getQueryCacheHitCount());
			LOG.info("Query Cache Miss Count: " + stats.getQueryCacheMissCount());
			LOG.info("Query Cache Put Count: " + stats.getQueryCachePutCount());
			LOG.info("Query Cache Execution Count: " + stats.getQueryExecutionCount());
			LOG.info("Query Execution Max Time: " + stats.getQueryExecutionMaxTime());
			LOG.info("Query Execution Max Time Query String: " + stats.getQueryExecutionMaxTimeQueryString());

			LOG.info("Second Level Cache Hit Count: " + stats.getSecondLevelCacheHitCount());
			LOG.info("Second Level Cache Miss Count: " + stats.getSecondLevelCacheMissCount());
			LOG.info("Second Level Cache Put Count: " + stats.getSecondLevelCachePutCount());

			LOG.info("Session Close Count: " + stats.getSessionCloseCount());
			LOG.info("Session Open Count: " + stats.getSessionOpenCount());
			LOG.info("Start Time: " + stats.getStartTime());
			LOG.info("Successful Transaction Count: " + stats.getSuccessfulTransactionCount());
		} catch (Throwable e) {
			LOG.error("Erro de Log (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}

		try {
			LOG.info("");
			LOG.info("Second Level Cache Region Names:");
			for (String s : stats.getSecondLevelCacheRegionNames())
				LOG.info(s);
			LOG.info("");
			LOG.info("Update Timestamps Cache Hit Count : " + stats.getUpdateTimestampsCacheHitCount());
			LOG.info("Update Timestamps Cache Miss Count : " + stats.getUpdateTimestampsCacheMissCount());
			LOG.info("Update Timestamps Cache Put Count : " + stats.getUpdateTimestampsCachePutCount());
		} catch (Throwable e) {
			LOG.error("Erro de LOG (GENERAL STATS): " + e.getClass() + " " + e.getMessage());
		}
		try {

			LOG.info("-------- 2ND LEVEL REGION cinemaRegion --------");
			LOG.info("2nd L Cache Put Count: " + stats.getSecondLevelCachePutCount());
			LOG.info("Tamanho em memoria: " + secStats.getSizeInMemory());
			LOG.info("Hit Count: " + secStats.getHitCount());
			LOG.info("Miss Count: " + secStats.getMissCount());
			LOG.info("Element Count In Memory: " + secStats.getElementCountInMemory());
			LOG.info("Element Count On Disk: " + secStats.getElementCountOnDisk());
			LOG.info("Element Count On Disk: " + secStats.getEntries().toString());

		} catch (Throwable e) {
			LOG.error("Erro de LOG (2ND LEVEL REGION): " + e.getClass() + " " + e.getMessage());
		}
		try {

			LOG.info("----- Query Stats SELECT DISTINCT(c) FROM CinemaElement... -----");
			QueryStatistics queryStats = stats
					.getQueryStatistics("SELECT DISTINCT(c) FROM CinemaElement c LEFT JOIN FETCH c.filmes");
			LOG.info("Query Cache Put Count: " + queryStats.getCachePutCount());
			LOG.info("Query Cache Hit Count: " + queryStats.getCacheHitCount());
			LOG.info("Query Cache Miss Count: " + queryStats.getCacheMissCount());
			LOG.info("Execution Avg Time: " + queryStats.getExecutionAvgTime());
			LOG.info("Execution Count: " + queryStats.getExecutionCount());
			LOG.info("Execution Max Time: " + queryStats.getExecutionMaxTime());
			LOG.info("Execution Min Time: " + queryStats.getExecutionMinTime());
			LOG.info("Execution Row Count: " + queryStats.getExecutionRowCount());

		} catch (Throwable e) {
			LOG.error("Erro de Log (Query Stats SELECT DISTINCT(c) FROM CinemaElement...): " + e.getClass() + " "
					+ e.getMessage());
		}
		try {

			LOG.info("----- ENTITY STATS -----");
			EntityStatistics entityStats = stats.getEntityStatistics(Cinema.class.getName());
			LOG.info("Insert Count: " + entityStats.getInsertCount());
			LOG.info("Update Count: " + entityStats.getUpdateCount());
			LOG.info("Delete Count: " + entityStats.getDeleteCount());
			LOG.info("Fetch Count: " + entityStats.getFetchCount());
			LOG.info("Load Count: " + entityStats.getLoadCount());
			LOG.info("Optimistic Failure Count: " + entityStats.getOptimisticFailureCount());

		} catch (Throwable e) {
			LOG.error("Erro de Log (ENTITY STATS): " + e.getClass() + " " + e.getMessage());
		}
	}
}