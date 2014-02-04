package de.tbosch.commons.exceptions;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import de.tbosch.commons.utils.MessageUtils;

/**
 * <p>
 * Abstrakte Basisexception, von der alle Exceptions in Admiral Anwendungen erben. Grundsätzlich wird nur mit unchecked
 * exceptions gearbeitet, um das explizite fangen und weiterleiten von Exceptions zu vermeiden.
 * </p>
 * <p>
 * Es wird empfohlen für jeden Architekturblock eine eigene Exceptions Hierarchie unterhalb dieser Basis Exception
 * aufzubauen, um zentrale Exception Behandlungen zu ermöglichen
 * </p>
 * <p>
 * Die <code>AbstractFrameworkException</code> stellt einige erweiterte Funktionen im Vergleich zu der
 * {@link RuntimeException} zur Verfügung. Wichtigste Features sind die Dynamisierung und die Internationalisierung der
 * Exceptions.
 * </p>
 * <h3>Internationalisierung</h3>
 * <p>
 * Der <code>AbstractFrameworkException</code> werden als <code>message</code> Parameter keine Nachrichten, sonderen die
 * Schlüssel innerhalb eines Ressouce Bundles mitgegeben. Dies ermöglicht einen Internationalisierung von Nachrichten
 * und die einfach Anapassung von Nachrichten für bestimmte Anwendungen, Benutzergruppen und Mandanten.
 * </p>
 * <h4>Namenskonventionen für <code>ResourceBundle</code></h4>
 * <ul>
 * <li>Die <code>ResourceBundle</code> beginnen alle mit dem Prefix <code>ExceptionMessages</code></li>
 * <li>Der Applikationsname, der über den Servlet Context Namen ermittelt wird, mit einem "-" getrennt</li>
 * <li>Das Default <code>ResouceBundle</code> liegt im Framework und hat keinen Context Namen</li>
 * <li>Der Postfix ist die <code>Locale</code> wie in {@link ResourceBundle} beschrieben</li>
 * </ul>
 * <h4>Reihenfolge und Auflösung des richtigen <code>ResourceBundle</code></h4>
 * <ol>
 * <li>Aus dem <code>Context</code> werden <code>Context.LOCALE</code> und <code>Context.SERVLET_CONTEXT_NAME</code>
 * gelesen</li>
 * <li>Das default <code>ResourceBundle</code> aus dem Framework wird für die entsprechende <code>Locale</code> gelesen</li>
 * <li>Es wird versucht das ResourceBundle für den Anwendung und die Local zu laden. Die Nachrichten aus dem default
 * <code>ResourceBundle</code> und dem Applikations <code>ResourceBundle</code> werden zusammengeführt, wobei bei
 * gleichen Message Keys die Nachrichten aus dem Applikations <code>ResourceBundle</code> verwendet werden.</li>
 * </ol>
 * <h3>Dynamisierung</h3>
 * <p>
 * Die Dynamisierung einer Exception beruht darauf, dass in den meisten Fällen Kontextinformationen benötigt werden, um
 * den Grund für eine Exception zu ermitteln. Deshalb kann der <code>AbstractFrameworkException</code> im Konstruktor
 * eine Liste mit Objekten übergeben werden, die dann auf Basis von {@link MessageFormat} in die gelesene Nachricht mit
 * eingebunden werden. Nachfolgendes Beispiel zeigt die Verwendung:
 * </p>
 * <p>
 * Die Message wird im <code>ResourceBundle</code> wie folgt definiert:
 * 
 * <pre>
 * partner_nicht_gefunden.business.exception = Der Partner mit der ID {0} und dem Namen {1} konnte nicht gefunden werden.
 * </pre>
 * 
 * Innerhalb der Nachricht gibt der Platzhalter <code>{0}</code> an, dass er durch den ersten übergebenen Parameter
 * ersetzt werden soll.
 * </p>
 * <p>
 * Die entsprechende Exception würde dann wie folgt geworfen werden:
 * 
 * <pre>
 * throw new IrgendeineBusinessException(&quot;partner_nicht_gefunden.business.exception&quot;, partner.getId(), partner.getName());
 * </pre>
 * 
 * </p>
 * 
 * @see RuntimeException
 * @see org.springframework.core.NestedRuntimeException
 * @author tbo
 */
public abstract class AbstractException extends RuntimeException {

	/** default serial ID */
	private static final long serialVersionUID = 1L;

	/** Name des ResouceBundle für die Exception Nachrichten */
	public static final String MESSAGE_RESOURCE_BUNDLE_NAME = "ExceptionMessages";

	/**
	 * Die Messageparameter, die für die Exception Nachricht gesetzt werde sollen
	 */
	private Object[] messageParameter;

	/**
	 * Die aufgelöste Nachricht über das Ressource Bundle.
	 */
	private String resolvedMessage;

	/**
	 * Der Name der Applikation, die die Message geworfen hat. Wird beim Erzeugen des Exception-Objekts initialisiert.
	 */
	private String applikationsName;

	/**
	 * Konstruktor, der die Exception mit einer Nachricht und dem Root-Cause initialisiert.
	 * 
	 * @param messageKey
	 *            Der Key auf die Message im Resource Bundle.
	 * @param cause
	 *            Der Root Cause
	 * @param messageParameter
	 *            Die Parameter, die in der Message ersetzt werden sollen
	 */
	public AbstractException(String messageKey, Throwable cause, Object... messageParameter) {
		super(messageKey, cause);
		this.messageParameter = messageParameter;
		init();
	}

	/**
	 * Konstruktor, der die Exception mit einer Nachricht und dem Root-Cause initialisiert.
	 * 
	 * @param messageKey
	 *            Der Key auf die Message im Resource Bundle.
	 * @param cause
	 *            Der Root Cause
	 */
	public AbstractException(String messageKey, Throwable cause) {
		super(messageKey, cause);
		init();
	}

	/**
	 * Konstruktor, der die Exception mit einer Nachricht initialisiert.
	 * 
	 * @param messageKey
	 *            Der Key auf die Message im Resource Bundle.
	 * @param messageParameter
	 *            Die Parameter, die in der Message ersetzt werden sollen
	 */
	public AbstractException(String messageKey, Object... messageParameter) {
		super(messageKey);
		this.messageParameter = messageParameter;
		init();
	}

	/**
	 * Konstruktor, der die Exception mit einer Nachricht initialisiert.
	 * 
	 * @param messageKey
	 *            Der Key auf die Message im Resource Bundle.
	 */
	public AbstractException(String messageKey) {
		super(messageKey);
		init();
	}

	/**
	 * Liefert die internationalisierte Nachricht der Exception zurück. TODO Terry: Sollte hier nicht besser
	 * getLocalizedMessage benutzt werden? getMessage wird auch mal außerhalb des Contexts, z.B. von Loggern,
	 * aufgerufen, und dann fehlt auch schon mal ein ResourceBundle.
	 * 
	 * @return Die Nachricht der Exception.
	 */
	@Override
	public String getMessage() {

		if (resolvedMessage != null) {
			return resolvedMessage;
		}

		// default Implementierung der Message holen
		String message = super.getMessage();
		message = MessageUtils.leseNachrichtAusResourceBundle(message, applikationsName, MESSAGE_RESOURCE_BUNDLE_NAME,
				messageParameter);

		// Benutzer vor die Message
		// if (Context.getContext().get(Context.BENUTZER_KENNUNG) != null) {
		// message = Context.getContext().get(Context.BENUTZER_KENNUNG) + ": " +
		// message;
		// }
		return message;
	}

	/**
	 * Liefert den MessageKey für das ResourceBundle zurück, der im Konstruktor übergeben wurde
	 * 
	 * @return MessageKey
	 */
	public String getMessageKey() {
		return super.getMessage();
	}

	/**
	 * Liefert den MessageKey und die angegebenen Parameter, ohne eine Übersetzung über ein Resourcebundle vorzunehmen.
	 * Z.B. für Logging-Zwecke, wenn das ResourceBundle nicht zur Verfügung steht oder eher der Key interessant ist.
	 * 
	 * @return Log-String aus MessageKey und Parametern
	 */
	public String getUntranslatedMessage() {
		return MessageUtils.erzeugeNachrichtOhneUebersetzung(super.getMessage(), messageParameter);
	}

	/**
	 * Initialisiert mit dem Namen der Applikation, die die Exception wirft. Wird bei ReST-Aufrufen benötigt, um auf dem
	 * Client das korrekte ResourceBundle ziehen zu können.
	 */
	protected void init() {
		// applikationsName = ((String)
		// Context.getContext().get(Context.SERVLET_CONTEXT_NAME));
	}

	/**
	 * Gibt die Message-Parameter zurück.
	 * 
	 * @return die Message-Parameter
	 */
	public Object[] getMessageParameter() {
		return messageParameter;
	}

	public String getResolvedMessage() {
		return resolvedMessage;
	}

	public void setResolvedMessage(String resolvedMessage) {
		this.resolvedMessage = resolvedMessage;
	}

	public String getApplikationsName() {
		return applikationsName;
	}

	public void setApplikationsName(String applikationsName) {
		this.applikationsName = applikationsName;
	}
}
