package de.tbosch.batch.skip;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.classify.Classifier;

public class AsyncLimitCheckingItemSkipPolicy extends LimitCheckingItemSkipPolicy {

	public AsyncLimitCheckingItemSkipPolicy() {
		super();
	}

	public AsyncLimitCheckingItemSkipPolicy(int skipLimit, Classifier<Throwable, Boolean> skippableExceptionClassifier) {
		super(skipLimit, skippableExceptionClassifier);
	}

	public AsyncLimitCheckingItemSkipPolicy(int skipLimit, Map<Class<? extends Throwable>, Boolean> skippableExceptions) {
		super(skipLimit, skippableExceptions);
	}

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) {
		if (t instanceof ExecutionException) {
			return super.shouldSkip(t.getCause(), skipCount);
		}
		return super.shouldSkip(t, skipCount);
	}

}
