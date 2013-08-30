package br.com.cineagora.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Util {
		public static final String PATH = new File(".").getAbsolutePath();
		// ':' windows, ';' unixes
		public static final String SEPARATOR = System.getProperty("path.separator");
		// '\' windows, '/' unixes
		public static final String FILE_SEP = System.getProperty("file.separator");
		private static String path = null;
		
		/**
		 *  <p>Trabalha de acordo com o Sistema Operacional.
		 *  <p><b>Exemplo de entrada:</b>
		 *  <br>"src", "main", "resources", "META-INF", "recursos.properties"
		 *  <p><b>Exemplo de retorno:</b>
		 *  <br>C:\Documents and Settings\Raul\Meus documentos\Arquivos_RAUL\developer\
		 *  works\CineServer\cineserver\src\main\resources\META-INF\recursos.properties
		 */
		public static String getPathAbsolutoApp(final String... arquivos){
			path = PATH;
			for (String f : arquivos) {
				path += FILE_SEP + f;
			}
			return path;
		}
		
		public static List<String> obtemLinhasDoArquivo(String nomeDoArquivo) {
			List<String> linhas = null;
			File file = new File(nomeDoArquivo);
			try {
				linhas = FileUtils.readLines(file, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String string : linhas) {

				System.out.println(string);
			}
			return linhas;

		}
		public static String obtemPrimeiraLinhaDoArquivo(String nomeDoArquivo) {
			return obtemLinhasDoArquivo(nomeDoArquivo).get(0);
		}
		public static String fazEncodeParaASCII(String url) {
			URL urlApi;
			URI uri;
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
				//DEBUG
				throw new RuntimeException(e);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				//DEBUG
				throw new RuntimeException(e);
			}
			return uri.toASCIIString();
		}
}