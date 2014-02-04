package de.tbosch.commons.exceptions;

/**
 * <p>
 * Basis <code>Exception</code> für alle fachlichen Fehler. Fachliche Fehler sind Fehler die in den meisten Fällen auf
 * fehlerhaften Benutzereingaben beruhen. In der Regel werdeb diese Fehler dem Benutzer mit entsprechenden Fehlerangaben
 * angezeigt.
 * </p>
 * 
 * @author tbo
 */
public abstract class AbstractBusinessException extends AbstractException {

	/** default serial Id */
	private static final long serialVersionUID = 1L;

	/**
	 * @see AbstractException#AbstractHwExtException(String, Throwable, Object...)
	 */
	public AbstractBusinessException(String messageKey, Throwable cause, Object... messageParameter) {
		super(messageKey, cause, messageParameter);
	}

	/**
	 * @see AbstractException#AbstractHwExtException(String, Throwable)
	 */
	public AbstractBusinessException(String messageKey, Throwable cause) {
		super(messageKey, cause);
	}

	/**
	 * @see AbstractException#AbstractHwExtException(String, Object...)
	 */
	public AbstractBusinessException(String messageKey, Object... messageParameter) {
		super(messageKey, messageParameter);
	}

	/**
	 * @see AbstractException#AbstractHwExtException(String)
	 */
	public AbstractBusinessException(String messageKey) {
		super(messageKey);
	}

}
