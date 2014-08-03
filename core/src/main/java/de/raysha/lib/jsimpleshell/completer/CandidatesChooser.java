package de.raysha.lib.jsimpleshell.completer;

import java.util.Collection;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;
import de.raysha.lib.jsimpleshell.annotation.Param;

/**
 * This interface is responsible for choosing candidates for special {@link Param}eters.
 * 
 * @author rainu
 *
 */
public interface CandidatesChooser {

	/**
	 * This method will be called if should be completed.
	 * 
	 * @param paramSpec The parameter which is affected.
	 * @param part The already typed part of parameter.
	 * @return All possible {@link Candidates}.
	 */
	public Candidates chooseCandidates(ShellCommandParamSpec paramSpec, String part);
	
	public static class Candidates {
		private final int index;
		private final Collection<String> values;
		
		/**
		 * @param values Contains all possible candidates as String.
		 */
		public Candidates(Collection<String> values) {
			this(values, 0);
		}
		
		/**
		 * 
		 * @param values Contains all possible candidates as String.
		 * @param index The index of the parameter for which the completion will be relative.
		 */
		public Candidates(Collection<String> values, int index) {
			this.values = values;
			this.index = index;
		}
		
		public Collection<String> getValues() {
			return values;
		}
		
		public int getIndex() {
			return index;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			result = prime * result
					+ ((values == null) ? 0 : values.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Candidates other = (Candidates) obj;
			if (index != other.index)
				return false;
			if (values == null) {
				if (other.values != null)
					return false;
			} else if (!values.equals(other.values))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "{" + index + ":" + values + "}";
		}
	}
}
