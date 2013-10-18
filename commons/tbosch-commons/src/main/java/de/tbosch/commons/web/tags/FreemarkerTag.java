package de.tbosch.commons.web.tags;

import java.io.StringWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.IncludePage;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/**
 * Diese Tag liest ein Freemarker-Template aus, welches im Tag im resourcePath als Parameter übergeben wurde.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
@SuppressWarnings("serial")
public class FreemarkerTag extends TagSupport {

	private static final String ATTR_FM_SESSION = ".freemarker.Session";
	private static final String ATTR_FM_REQUEST_PARAMS = ".freemarker.RequestParameters";
	private static final String ATTR_FM_REQUEST = ".freemarker.Request";
	private static final String ATTR_FM_APPLICATION = ".freemarker.Application";
	private static final String ATTR_FM_JSPTAGLIBS = ".freemarker.JspTaglibs";

	private String resourcePath;

	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			ServletContext servletContext = pageContext.getServletContext();
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

			// Freemarker Konfiguration und Template erstellen
			Configuration cfg = (Configuration) servletContext
					.getAttribute(FreemarkerInitializationServlet.FREEMARKER_CONFIGURATION);
			Template temp = cfg.getTemplate(resourcePath);
			temp.setEncoding(pageContext.getResponse().getCharacterEncoding());

			// Taglibs in den ServletContext schreiben
			TaglibFactory taglibs = new TaglibFactory(servletContext);
			servletContext.setAttribute(ATTR_FM_JSPTAGLIBS, taglibs);

			// ServletContextModel aus dem Context erstellen
			// !!! FIXME: immer neu erstellen?
			ServletContextHashModel servletContextModel = new ServletContextHashModel((GenericServlet) pageContext
					.getAttribute(PageContext.PAGE), cfg.getObjectWrapper());
			servletContext.setAttribute(ATTR_FM_APPLICATION, servletContextModel);

			// RequestModel aus dem Request erstellen
			HttpRequestHashModel requestModel = (HttpRequestHashModel) pageContext.getRequest().getAttribute(
					ATTR_FM_REQUEST);
			if (requestModel == null || requestModel.getRequest() != request) {
				requestModel = new HttpRequestHashModel(request, response, cfg.getObjectWrapper());
				request.setAttribute(ATTR_FM_REQUEST, requestModel);
				request.setAttribute(ATTR_FM_REQUEST_PARAMS, new HttpRequestParametersHashModel(request));
			}

			// SessionModel aus der Session erstellen
			HttpSessionHashModel sessionModel;
			HttpSession session = request.getSession(false);
			if (session != null) {
				sessionModel = (HttpSessionHashModel) session.getAttribute(ATTR_FM_SESSION);
				if (sessionModel == null) {
					// !!! FIXME
					// || sessionModel.isZombie()
					sessionModel = new HttpSessionHashModel(session, cfg.getObjectWrapper());
					session.setAttribute(ATTR_FM_SESSION, sessionModel);
					// if (!sessionModel.isZombie()) {
					// macht nix: initializeSession(request, response);
					// }
				}
			} else {
				sessionModel = new HttpSessionHashModel(session, cfg.getObjectWrapper());
			}

			// Create hash model wrapper for request parameters
			HttpRequestParametersHashModel requestParametersModel = (HttpRequestParametersHashModel) request
					.getAttribute(ATTR_FM_REQUEST_PARAMS);

			// Schreibe den pageContext in die Map, die Freemarker übergeben bekommt,
			// damit der Context auch bei der Konstruktion des Templates vorhanden ist
			SimpleHash root = new SimpleHash();
			root.put("page", new JspContextModel(pageContext, JspContextModel.PAGE_SCOPE));
			root.put("request", new JspContextModel(pageContext, JspContextModel.REQUEST_SCOPE));
			root.put("session", new JspContextModel(pageContext, JspContextModel.SESSION_SCOPE));
			root.put("application", new JspContextModel(pageContext, JspContextModel.APPLICATION_SCOPE));
			root.put("any", new JspContextModel(pageContext, JspContextModel.ANY_SCOPE));
			root
					.put(FreemarkerServlet.KEY_JSP_TAGLIBS, (TemplateModel) servletContext
							.getAttribute(ATTR_FM_JSPTAGLIBS));
			root.put(FreemarkerServlet.KEY_APPLICATION, servletContextModel);
			root.put(FreemarkerServlet.KEY_APPLICATION_PRIVATE, servletContextModel);
			root.put(FreemarkerServlet.KEY_REQUEST, requestModel);
			root.put(FreemarkerServlet.KEY_REQUEST_PRIVATE, requestModel);
			root.put(FreemarkerServlet.KEY_INCLUDE, new IncludePage(request, response));
			root.put(FreemarkerServlet.KEY_SESSION, sessionModel);
			root.put(FreemarkerServlet.KEY_REQUEST_PARAMETERS, requestParametersModel);

			// Schreibe das Template temporär in einen StringWriter, da die Servlet-API
			// kein eigenes Flush des Streams erlaubt und im process() ein Flush stattfindet
			StringWriter sw = new StringWriter();
			temp.process(root, sw);
			pageContext.getOut().write(sw.getBuffer().toString());
		} catch (Exception e) {
			throw new JspException(e);
		}
		return super.doStartTag();
	}

	// Getter / Setter

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

}
