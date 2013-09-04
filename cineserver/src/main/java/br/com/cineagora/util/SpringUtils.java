package br.com.cineagora.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("singleton")
@Component
public class SpringUtils implements ApplicationContextAware {
	
	private static HttpServletRequest httpServletRequest;
	private static ServletContext servletContext;
	
    @Autowired
	public void setRequest(HttpServletRequest httpServletRequest) {
		SpringUtils.httpServletRequest = httpServletRequest;
	}
    @Autowired
    public void setServletContext(ServletContext servletContext) {
    	SpringUtils.servletContext = servletContext;
    }

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		initializeApplicationContext(applicationContext);
	}

	private static void initializeApplicationContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static HttpServletRequest getServletRequest() {
		return httpServletRequest;
	}
	public static ServletContext getServletContext() {
		return servletContext;
	}
}
