package cineserver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.cineagora.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("classpath:/META-INF/spring/datasource-context-test.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/datasource-context-test.xml" })
public class TesteRequestResources {
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockHttpSession session;

	MockMvc mockMvc;
	ObjectMapper jsonObjectMapper;

	public static String linkResource = "http://localhost:8080/cineserver/cinemas/"
			+ "cidade-estado?cidade=São+Paulo&estado=São+Paulo";
	static {
		linkResource = Util.fazEncodeParaASCII(linkResource);
	}
	/**
	 * Recursos de um web servlet padrão
	 */
	@Before
	public void inicializaWebServlet() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.defaultRequest(get("/")
				.contextPath("/cineserver"))
				.build();
		jsonObjectMapper = new ObjectMapper();
	}

	/**
	 * Faz request para testar get
	 * @throws Exception
	 */
	@Test
	public void fazRequest() throws Exception {
		this.mockMvc.perform(get(linkResource).accept(new MediaType("application", "json")))
				.andExpect(status().isOk())
				//.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.cidade").value("S%C3%A3o+Paulo"));
	}
	
}
