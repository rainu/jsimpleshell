package de.raysha.lib.jsimpleshell;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import de.raysha.lib.jsimpleshell.io.Input;
import de.raysha.lib.jsimpleshell.io.Output;
import de.raysha.lib.jsimpleshell.util.ArrayHashMultiMap;
import de.raysha.lib.jsimpleshell.util.MultiMap;

public class ShellSettings {
	private final Input input;
	private final Output output;
	private final MultiMap<String, Object> auxHandlers;
	private final boolean displayTime;

	public ShellSettings(Input input, Output output, MultiMap<String, Object> auxHandlers, boolean displayTime) {
		this.input = input;
		this.output = output;
		this.auxHandlers = auxHandlers;
		this.displayTime = displayTime;
	}

	public ShellSettings createWithAddedAuxHandlers(MultiMap<String, Object> addAuxHandlers) {
		MultiMap<String, Object> allAuxHandlers = new ArrayHashMultiMap<String, Object>(auxHandlers);
		allAuxHandlers.putAll(addAuxHandlers);
		return new ShellSettings(input, output, allAuxHandlers, displayTime);
	}

	public Input getInput() {
		return input;
	}

	public Output getOutput() {
		return output;
	}

	public boolean isDisplayTime() {
		return displayTime;
	}

	public MultiMap<String, Object> getAuxHandlers() {
		return new UnmodifiableMultiMap<String, Object>(auxHandlers);
	}

	public static class UnmodifiableMultiMap<K, V> implements MultiMap<K, V> {
		private final MultiMap<K, V> delegate;

		public UnmodifiableMultiMap(MultiMap<K, V> delegate) {
			this.delegate = delegate;
		}

		public void put(K key, V value) {
			throw new UnsupportedOperationException();
		}

		public void putAll(MultiMap<K, V> map) {
			throw new UnsupportedOperationException();
		}

		public Collection<V> get(K key) {
			return Collections.unmodifiableCollection(delegate.get(key));
		}

		public Set<K> keySet() {
			return Collections.unmodifiableSet(delegate.keySet());
		}

		public void remove(K key, V value) {
			throw new UnsupportedOperationException();
		}

		public void removeAll(K key) {
			throw new UnsupportedOperationException();
		}

		public int size() {
			return delegate.size();
		}
	}
}