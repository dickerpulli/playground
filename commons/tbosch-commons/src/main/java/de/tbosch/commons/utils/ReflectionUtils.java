package de.tbosch.commons.utils;

import java.lang.reflect.Method;

/**
 * Reflection Utilities für Reflection in Testklassen
 * 
 * @author tbo
 */
public class ReflectionUtils {

	/**
	 * privater Konstruktor, da Utitlity-Klasse
	 */
	private ReflectionUtils() {
		// empty
	}

	/**
	 * Führt eine private Methode über die Spring-ReflectionUtils aus.
	 * 
	 * @param clazz
	 *            Die Klasse, in der die Methode deklariert ist
	 * @param methodName
	 *            Der Name der Methode
	 * @param object
	 *            Das Objekt, auf dem die Methode ausgeführt wird
	 * @param argsClass
	 *            Die Klassen der Argumente in der Methodendeklaration
	 * @param argsValue
	 *            Die Objekte der Argumente des Methodeaufrufs
	 * @return Der Rückgabewert der Methode
	 */
	public static <C,A> Object invokePrivateMethod(Class<C> clazz, String methodName, C object, Class<A>[] argsClass,
			A[] argsValue) {
		Method method = org.springframework.util.ReflectionUtils.findMethod(clazz, methodName, argsClass);
		org.springframework.util.ReflectionUtils.makeAccessible(method);
		return org.springframework.util.ReflectionUtils.invokeMethod(method, object, argsValue);
	}

}
