import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.element.CinemaElement;


@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TestePersistencia {
	@Autowired
	Cinema cinemaTest;
	
	@Resource(type=CinemaDao.class)
	CinemaDao cinemaDao;
	
	@PersistenceContext
    EntityManager em;
	
	Cinema cinema;

	//@Test
	@Transactional
	public void recupera() {
		cinema = cinemaDao.find(100);
		System.err.println(cinema.getNome());
	}
	@Test
	@Transactional(readOnly=true)
	public void recuperaTudo() {
		List<? extends Cinema> cinemas = cinemaDao.findAll(CinemaElement.class);
		for (Cinema cinema : cinemas) {
			System.out.println(cinema.getNome());
		}
	}
}
