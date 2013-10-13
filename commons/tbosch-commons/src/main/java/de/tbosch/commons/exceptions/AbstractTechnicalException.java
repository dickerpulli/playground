package de.tbosch.commons.exceptions;

/**
 * <p>
 * Basis <code>Exception</code> für alle technische Fehler. Technische Fehler sind Fehler, die auf technischer Ebene
 * entstehen. Beispielsweise bei Kommunikationsfehlern mit der Datenbank oder bei fehlerhaften Dateizugriffen.
 * </p>
 * 
 * @author tbo
 */
public abstract class AbstractTechnicalException extends AbstractException {

	/** default serial Id */
	private static final long serialVersionUID = 1L;

	/**
	 * @see AbstractException#AbstractHwExtException(String, Throwable, Object...)
	 */
	public AbstractTechnicalException(String messageKey, Throwable cause, Object... messageParameter) {
		super(messageKey, cause, messageParameter);
	}

	/**
	 * @see AbstractException#AbstractHwExtException(String, Throwable)
	 */
	public AbstractTechnicalException(String messageKey, Throwable cause) {
		super(messageKey, cause);
	}

	/**
	 * @see AbstractException#AbstractHwExtException(String, Object...)
	 */
	public AbstractTechnicalException(String messageKey, Object... messageParameter) {
		super(messageKey, messageParameter);
	}

	/**
	 * @see AbstractException#AbstractHwExtException(String)
	 */
	public AbstractTechnicalException(String messageKey) {
		super(messageKey);
	}

}
