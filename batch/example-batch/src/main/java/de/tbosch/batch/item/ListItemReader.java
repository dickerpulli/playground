package de.tbosch.batch.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

public class ListItemReader<T> implements ItemReader<List<T>>, ItemStream {

	private ItemReader<T> delegate;

	private final List<T> list = new ArrayList<>();

	@Override
	public List<T> read() throws Exception {
		T item = delegate.read();
		while (item != null) {
			if (list.size() == 0) {
				list.add(item);
				item = delegate.read();
			} else if (list.size() == 1) {
				list.add(item);
				List<T> ret = new ArrayList<>(list);
				list.clear();
				return ret;
			}
		}
		if (list.size() == 0) {
			return null;
		} else {
			List<T> ret = new ArrayList<>(list);
			list.clear();
			return ret;
		}
	}

	public void setDelegate(ItemReader<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (delegate instanceof ItemStream) {
			((ItemStream) delegate).open(executionContext);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		if (delegate instanceof ItemStream) {
			((ItemStream) delegate).update(executionContext);
		}
	}

	@Override
	public void close() throws ItemStreamException {
		if (delegate instanceof ItemStream) {
			((ItemStream) delegate).close();
		}
	}

}
