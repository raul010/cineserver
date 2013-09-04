package br.com.cineagora.aspect;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.management.BadStringOperationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import br.com.cineagora.util.Cronometro;
import br.com.cineagora.util.Util;

@Aspect
@Component
public class ProfilesELogs {

	public static final Logger LOG = LogManager.getLogger(ProfilesELogs.class.getName());
	public static final String PACKAGE_PATH = "br.com.cineagora";
	private static final String TODOS_RESOURCES = "execution(* " + PACKAGE_PATH + ".resources.*Resource.*(..))";
	
	public static final String PACKAGE_NIVEL_BAIXO = "execution(* " + PACKAGE_PATH + ".*.*(..))";
	public static final String PACKAGE_NIVEL_MEDIO = "execution(* " + PACKAGE_PATH + ".*.*.*(..))";
	public static final String PACKAGE_NIVEL_ALTO = "execution(* " + PACKAGE_PATH + ".*.*.*.*(..))";

	@Around("execution(* "+ PACKAGE_PATH + ".dao.CinemaDaoImpl.findAll(..))")
	public Object profileBaseOuCache(ProceedingJoinPoint pjp) throws Throwable {
		Util.logAuditStatsInfo();
		Cronometro profile = new Cronometro();
		try {
			profile.start();
			return pjp.proceed();
		} finally {
			profile.stop();
			profile.print(pjp.getSignature().toShortString());
		}
	}
	
	/**
	 * Intercepta todos os niveis de br.com.meuApp, ou seja, toda aplicacao (exceto Resources).
	 * @param joinPoint
	 */
	@Before("(" + PACKAGE_NIVEL_BAIXO + 
			" || " + PACKAGE_NIVEL_MEDIO + 
			" || " + PACKAGE_NIVEL_ALTO + ")" +
			" && !" + TODOS_RESOURCES)
	public void verificaCharset(JoinPoint joinPoint) {
		String texto = null;
		for (Object obj : joinPoint.getArgs()) {
			
			if (!(obj instanceof String)) 
				continue;
			
			texto  = (String) obj;
			
			texto = trataEncoding(joinPoint, texto);
				
		}
	}

	/**
	 * Intercepta todos os Resourses, e faz o Decoder antes de tratar o Encoding
	 * @param joinPoint
	 */
	@Before(TODOS_RESOURCES)
	public void verificaCharsetResources(JoinPoint joinPoint) {
		String texto = null;
		for (Object obj : joinPoint.getArgs()) {
			
			
			if (!(obj instanceof String)) 
				continue;
			
			texto  = (String) obj;
			
			try {
				texto = URLDecoder.decode(texto, Util.UTF_8);

			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			texto = trataEncoding(joinPoint, texto);
		}
	}
	
	private String trataEncoding(JoinPoint joinPoint, String texto) {
		if(Util.isStringUTF8(texto))
			return texto;
		
		texto = Util.converteParaUTF8(texto);
		
		if(Util.isStringUTF8(texto))
			LOG.warn("String nao era UTF-8, mas foi convertido agora");
		else	
			LOG.warn(joinPoint.getSignature() + " - " + texto);
		return texto;
	}
	
}
