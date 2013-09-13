import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.negocio.CinemaService;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TesteRequestNoSite {
	
	@Resource(type=CinemaDao.class)
	CinemaDao cinemaDao;
	
	@Autowired
	CinemaService cinemaComponent;
	
	private static Logger log = LogManager.getLogger(TesteRequestNoSite.class.getName());
	
	@Test
	@Transactional
	public void test() throws IOException {
		String urlCinema = "http://www.adorocinema.com/programacao/em-torno-298363";
//		List<Cinema> cinemas = cinemaComponent.obtemCinemasDoSite(urlCinema);
//		for (Cinema c : cinemas) {
//			cinemaDao.create(c);
//		}
	}
}
