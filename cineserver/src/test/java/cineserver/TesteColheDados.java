package cineserver;

import java.io.IOException;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.util.JsoupUtil;

//Para o teste rodar, comentar no persistence.xml
//<provider>org.hibernate.ejb.HibernatePersistence</provider>
//<jta-data-source>java:/CineDS</jta-data-source>

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations={"classpath:/META-INF/spring/datasource-context-test.xml"})
public class TesteColheDados {
	
	@Autowired
	JsoupUtil jsoup;
	
	@Resource(type=CinemaDao.class)
	CinemaDao cinemaDao;
	
	@Test
	@Transactional
	public void test() throws IOException {
		String urlCinema = "http://www.adorocinema.com/programacao/em-torno-298363";
		Set<Cinema> cinemas = jsoup.fazRequestNosDados(urlCinema);
		for (Cinema c : cinemas) {
			cinemaDao.create(c);
		}
	}

}
