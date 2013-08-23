package br.com.cineagora.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;


public class LogUtil {
	
	public static Log log;
	private String separator = getClass().getClassLoader().getResource(".").getPath();
	
	private String log4jConfigFile = separator + "src" + separator + "main"
			+ separator + "resources" + separator
			+ "log4J.properties";

	{ PropertyConfigurator.configure(log4jConfigFile);}

	public static Log openLog(Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}
	
}
