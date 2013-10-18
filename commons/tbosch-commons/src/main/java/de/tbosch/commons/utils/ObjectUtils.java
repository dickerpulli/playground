package de.tbosch.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectUtils {

	private ObjectUtils() {
		// utils
	}

	/**
	 * Tiefe Kopie durch Serialisierung/Deserialisierung im Speicher
	 * 
	 * @param s
	 *            Element, kann null sein. Muss Serializable sein
	 * 
	 * @return Tiefe Kopie des Elements
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepCopy(final T s) {
		if (s == null) {
			return null;
		}
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(s);
			oos.close();
			bos.close();

			final byte[] data = bos.toByteArray();

			final ByteArrayInputStream bis = new ByteArrayInputStream(data);
			final ObjectInputStream ois = new ObjectInputStream(bis);
			final T result = (T) ois.readObject();
			ois.close();
			bis.close();

			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
