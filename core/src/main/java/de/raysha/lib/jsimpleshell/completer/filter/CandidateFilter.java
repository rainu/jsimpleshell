package de.raysha.lib.jsimpleshell.completer.filter;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * A class that implements this interface is responsible for filtering auto completion candidates.
 *
 * @author rainu
 */
public interface CandidateFilter {

	/**
	 * Decides whether a candidate should be filtered out.
	 *
	 * @return True if the candidate can be provided. Otherwise false.
	 */
	public boolean filter(FilterContext context);

	public class FilterContext {
		private final int cursor;
		private final String part;
		private final String type;
		private final String candidate;
		private final ShellCommandParamSpec spec;

		/**
		 * @param cursor The current position of the cursor.
		 * @param part The user typed part.
		 * @param type The type of the candidate.
		 * @param candidate The chosen candidate.
		 * @param spec The corresponding {@link ShellCommandParamSpec}.
		 */
		public FilterContext(int cursor, String part, String type, String candidate, ShellCommandParamSpec spec) {
			this.cursor = cursor;
			this.part = part;
			this.type = type;
			this.candidate = candidate;
			this.spec = spec;
		}

		public FilterContext(int cursor, String part, String type, String candidate) {
			this(cursor, part, type, candidate, null);
		}

		public int getCursor() {
			return cursor;
		}
		public String getPart() {
			return part;
		}
		public String getType() {
			return type;
		}
		public String getCandidate() {
			return candidate;
		}
		public ShellCommandParamSpec getSpec() {
			return spec;
		}

		@Override
		public String toString() {
			return "FilterContext [cursor=" + cursor + ", part=" + part
					+ ", type=" + type + ", candidate=" + candidate + ", spec="
					+ spec + "]";
		}
	}
}
