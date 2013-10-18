package de.tbosch.commons.persistence.validator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SingleTableSubclass;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

/**
 * Testet das Leersein eines Strings bei Ignorierung von Steuer- und Leerzeichen.
 * 
 * @author tbo
 */
@SuppressWarnings("serial")
public class NotEmptyAfterTrimValidator implements Validator<NotEmptyAfterTrim>, PropertyConstraint, Serializable {

	/**
	 * @see org.hibernate.validator.Validator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(NotEmptyAfterTrim parameters) {
		// gewollt leer
	}

	/**
	 * @see org.hibernate.validator.Validator#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {
		if (value == null)
			return false;
		else
			return ((String) value).trim().length() > 0;
	}

	/**
	 * @see org.hibernate.validator.PropertyConstraint#apply(org.hibernate.mapping.Property)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void apply(Property property) {
		if (!(property.getPersistentClass() instanceof SingleTableSubclass)
				&& !(property.getValue() instanceof Collection)) {
			// single table should not be forced to null
			if (!property.isComposite()) { // composite should not add not-null
				// on all columns
				Iterator<Column> iter = property.getColumnIterator();
				while (iter.hasNext()) {
					iter.next().setNullable(false);
				}
			}
		}
	}

}
