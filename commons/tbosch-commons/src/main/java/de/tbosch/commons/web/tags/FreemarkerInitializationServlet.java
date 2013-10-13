package de.tbosch.commons.web.tags;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import freemarker.template.Configuration;

/**
 * Servlet welches bei der Initialisierung die Freemarker-Configuration in den Servlet-Context schreibt
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
@SuppressWarnings("serial")
public class FreemarkerInitializationServlet extends HttpServlet {

	public static final String FREEMARKER_CONFIGURATION = "FREEMARKER_CONFIG";

	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		try {
			Configuration cfg = new Configuration();
			ServletContext sc = getServletContext();
			cfg.setServletContextForTemplateLoading(sc, "/");
			sc.setAttribute(FREEMARKER_CONFIGURATION, cfg);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
