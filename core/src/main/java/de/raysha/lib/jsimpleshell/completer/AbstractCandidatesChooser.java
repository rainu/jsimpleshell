package de.raysha.lib.jsimpleshell.completer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.raysha.lib.jsimpleshell.ShellCommandParamSpec;

/**
 * This class contains some helper methods for a {@link CandidatesChooser}.
 *
 * @author rainu
 */
public abstract class AbstractCandidatesChooser implements CandidatesChooser {
	private Set<Class<?>> responsibleFor = new HashSet<Class<?>>();
	private Set<String> sResponsibleFor = new HashSet<String>();

	public AbstractCandidatesChooser() {
		this(new Class<?>[]{}, new String[]{});
	}

	/**
	 * Define a {@link CandidatesChooser} that is responsible for the given
	 * custom types.
	 *
	 * @param responsibleFor The responsible custom types.
	 */
	public AbstractCandidatesChooser(String...responsibleFor) {
		this(new Class<?>[]{}, responsibleFor);
	}

	/**
	 * Define a {@link CandidatesChooser} that is responsible for the given
	 * parameter classes.
	 *
	 * @param responsibleFor The responsible parameter classes.
	 */
	public AbstractCandidatesChooser(Class<?>...responsibleFor) {
		this(responsibleFor, new String[]{});
	}

	/**
	 * Define a {@link CandidatesChooser} that is responsible for the given
	 * parameter classes and custom types.
	 *
	 * @param responsibleFor The responsible parameter classes.
	 * @param sResponsibleFor The responsible custom types.
	 */
	public AbstractCandidatesChooser(Class<?>[] responsibleFor, String[] sResponsibleFor) {
		this.responsibleFor.addAll(Arrays.asList(responsibleFor));
		this.sResponsibleFor.addAll(Arrays.asList(sResponsibleFor));
	}

	/**
	 * Checks if this {@link CandidatesChooser} is responsible for the given
	 * Parameter. The list of classes that this chooser is responsible for
	 * can be set at the constructor.
	 *
	 * @param paramSpec The given {@link ShellCommandParamSpec}.
	 * @return If this {@link CandidatesChooser} is responsible for the given parameter.
	 */
	protected boolean responsibleFor(ShellCommandParamSpec paramSpec) {
		if(paramSpec.getType() != null && !"".equals(paramSpec.getType().trim())){
			return sResponsibleFor.contains(paramSpec.getType());
		}

		//no custom type is given
		Class<?> pClass = getParameterClass(paramSpec);
		return responsibleFor.contains(pClass);
	}

	protected Class<?> getParameterClass(ShellCommandParamSpec paramSpec) {
		Class<?> pClass = paramSpec.getValueClass();
		if(pClass.isArray()) pClass = paramSpec.getValueClass().getComponentType();

		return pClass;
	}
}
