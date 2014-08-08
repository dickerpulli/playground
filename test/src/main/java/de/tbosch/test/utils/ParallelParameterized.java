package de.tbosch.test.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.Parameterized;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.TestClass;

/**
 * Erweitert den TestRunner {@link Parameterized} um die Möglichkeit der parallelen Testfallausführung.
 * 
 * @author Thomas Bosch
 */
public class ParallelParameterized extends Parameterized {

	/**
	 * Annotation für die Methode in der Testklasse, die angibt wieviele Threads parallel laufen sollen.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface ThreadCount {
		// marker
	}

	private static class ThreadPoolScheduler implements RunnerScheduler {

		private final ExecutorService executor;

		/**
		 * Konstruktor.
		 * 
		 * @param threadNum
		 */
		public ThreadPoolScheduler(int threadNum) {
			executor = Executors.newFixedThreadPool(threadNum);
		}

		/**
		 * @see org.junit.runners.model.RunnerScheduler#finished()
		 */
		@Override
		public void finished() {
			executor.shutdown();
			try {
				executor.awaitTermination(8, TimeUnit.HOURS);
			} catch (InterruptedException exc) {
				throw new RuntimeException(exc);
			}
		}

		/**
		 * @see org.junit.runners.model.RunnerScheduler#schedule(java.lang.Runnable)
		 */
		@Override
		public void schedule(Runnable childStatement) {
			executor.submit(childStatement);
		}
	}

	/**
	 * Konstruktor.
	 * 
	 * @param klass
	 * @throws Throwable
	 */
	public ParallelParameterized(Class<?> klass) throws Throwable {
		super(klass);
		int threadNum = (Integer) getThreadCountMethod(getTestClass()).invokeExplosively(null);
		setScheduler(new ThreadPoolScheduler(threadNum));
	}

	private FrameworkMethod getThreadCountMethod(TestClass testClass) throws Exception {
		List<FrameworkMethod> methods = testClass.getAnnotatedMethods(ThreadCount.class);
		for (FrameworkMethod each : methods) {
			int modifiers = each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
				return each;
			}
		}
		throw new Exception("No public static ThreadNumber method on class " + testClass.getName());
	}

}