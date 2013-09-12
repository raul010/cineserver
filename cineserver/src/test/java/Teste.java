import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class Teste {
	
	@Test
	public void teste2(){
		Map mapa = new HashMap<>();
		mapa.put(0, "");
		mapa.put(-1, "");
		mapa.put(1, "");
	}
	
	//@Test
	public void teste() {
		Cinema cine = new Cinema();

		Filme filme1 = new Filme();
		Filme filme2 = new Filme();
		Filme filme3 = new Filme();

		Filme filme4 = new Filme();
		Filme filme5 = new Filme();
		Filme filme6 = new Filme();
		
		filme1.nome = "A";
		filme2.nome = "B";
		filme3.nome = "C";
		
		//veio da base
		Set<Filme> filmesBase = new HashSet<Filme>();
		filmesBase.add(filme1); //A
		filmesBase.add(filme2); //B
		filmesBase.add(filme3); //C
		
		//Filmes que sera atribuido ao objeto Cinema corrente
		filme4.nome = "C";
		filme5.nome = "D";
		filme6.nome = "E";

		Set<Filme> filmesCineCorrente = new HashSet<Filme>();
	    filmesCineCorrente.add(filme4);
	    filmesCineCorrente.add(filme5);
	    filmesCineCorrente.add(filme6);
	    
	    cine.filmes = filmesCineCorrente;
	  
	    
	    for (Filme filme : filmesBase) {
			System.out.println("Base: " + filme.nome);
		}
	    for (Filme cineFilme : cine.filmes) {
	    	System.out.println("Filmes do Cinema corrente: " + cineFilme.nome);
	    }
	    cine.filmes.addAll(filmesBase);
	    
	    for (Filme filme : cine.filmes) {
	    	System.out.println("Filmes salvos na base: " + filme.nome);
		}
	    
	    
	    
		
	}
}
class Filme{
	String nome;
	Set<Cinema> cinema;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Filme))
			return false;
		Filme other = (Filme) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
}
class Cinema {
	String nome;
	Set<Filme> filmes;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cinema))
			return false;
		Cinema other = (Cinema) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
}
class Horario {
	int hora;
}
