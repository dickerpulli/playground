package de.tbosch.commons.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread Context
 */
public final class Context {

	/** Der Zugang zum Thread. */
	private static final ThreadLocal<Context> tls = new ThreadLocal<Context>() {
		/**
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		protected Context initialValue() {
			return new Context();
		}
	};

	/** Die Konstante für die Benutzerkennung */
	public static final String BENUTZER_KENNUNG = "BENUTZER_KENNUNG";

	/** Die Konstante für die Locale. */
	public static final String LOCALE = "locale";

	/** Konstante für die Sitzungs-ID */
	public static final String SITZUNG_ID = "sitzung-id";

	/** Die Konstante für den Namen des lokalen ServletContext (<code>display-name</code>). */
	public static final String SERVLET_CONTEXT_NAME = "local-servlet-context-name";

	/** Konstante für den Servlet Kontext */
	public static final String SERVLET_CONTEXT = "local-servlet-context";

	/** Konstante für die Server-URL */
	public static final String SERVER_URL = "serverURL";

	/**
	 * Liefere den aktuellen Context.
	 * 
	 * @return der aktuelle Context
	 */
	public static Context getContext() {
		return (Context) tls.get();
	}

	/** Die {@link Map}für die Begriffe. */
	private HashMap<String, Object> begriffe = new HashMap<String, Object>();

	/**
	 * @see Object#Object()
	 */
	Context() {
		super();
	}

	/**
	 * Entferne alle Begriffe aus diesem Kontext.
	 */
	public void clear() {
		begriffe.clear();
	}

	/**
	 * Liefere den Wert für den angegebenen Begriff.
	 * 
	 * @param begriff
	 *            der Begriff zu dem der Wert geholt wird
	 * @return der Wert des Begriffs oder <code>null</code>
	 */
	public Object get(String begriff) {
		return begriffe.get(begriff);
	}

	/**
	 * Setze einen Begriff in diesen Kontext.
	 * 
	 * @param begriff
	 *            der Begriff zu dem der Wert geholt wird
	 * @param wert
	 *            der Wert
	 */
	public void put(String begriff, Object wert) {
		begriffe.put(begriff, wert);
	}

	/**
	 * Setze die Begriffe in diesen Kontext.
	 * 
	 * @param begriffe
	 *            die Begriffe
	 */
	public void putAll(Map<String, Object> begriffe) {
		for (Map.Entry<String, Object> begriff : begriffe.entrySet()) {
			put(begriff.getKey(), begriff.getValue());
		}
	}

	/**
	 * Entferne den Begriff aus dem Kontext.
	 * 
	 * @param begriff
	 *            der Begriff der entfernt wird
	 */
	public void remove(String begriff) {
		begriffe.remove(begriff);
	}

	/**
	 * gibt zurück, ob der gesuchte Begriff im Context vorhanden ist
	 * 
	 * @param begriff
	 * @return true, wenn der Begriff im Context ist, false wenn nicht
	 */
	public boolean containsKey(String begriff) {
		return begriffe.containsKey(begriff);

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Context[ ").append(Thread.currentThread());

		for (Map.Entry<String, Object> begriff : begriffe.entrySet()) {
			buffer.append(" (").append(begriff.getKey()).append('=').append(begriff.getValue()).append(')');
		}

		buffer.append(']');
		return buffer.toString();
	}

}
