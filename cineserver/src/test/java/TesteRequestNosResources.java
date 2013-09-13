import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.negocio.CinemaService;
import br.com.cineagora.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TesteRequestNosResources {
	@Resource(type=CinemaDao.class)
	private CinemaDao cinemaDao;
	private @Autowired CinemaService cinemaComponent;
	
	private @Autowired WebApplicationContext wac;
	private @Autowired MockHttpSession session;

	private MockMvc mockMvc;
	private ObjectMapper jsonObjectMapper;

	public static String linkResourceSP = "http://localhost:8080/cineserver/cinemas/cidade-estado?cidade=São Paulo&estado=São Paulo";
	public static String linkResourceRJ = "http://localhost:8080/cineserver/cinemas/"
			+ "cidade-estado?cidade=Rio de Janeiro&estado=Rio de Janeiro";

	@Before
	public void inicializaWebServlet() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.defaultRequest(get("/")
						.contextPath("/cineserver"))
				.build();
		jsonObjectMapper = new ObjectMapper();
	}

	//@Repeat(2)
	@Test
	public void fazRequest() throws Exception {
		String[] links = new String[] { linkResourceSP, linkResourceRJ };
		for (String link : links) {

			//link = Util.fazEncodeParaASCII(link);
			String result = this.mockMvc.perform(
					get(link)
					.contentType(MediaType.valueOf("text/html;charset=UTF-8"))
					.characterEncoding("UTF-8")
					)
					//Espera
					.andExpect(status().isOk())
					.andExpect(content().encoding(Util.UTF_8))
					.andReturn()
					.getResponse()
					.getContentAsString()
			// .andExpect(jsonPath("$.cidade").value("Barueri"))
			;
			System.out.println(result);
			
		}
	}

}
