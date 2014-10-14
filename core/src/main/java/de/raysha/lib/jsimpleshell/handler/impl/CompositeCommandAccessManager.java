package de.raysha.lib.jsimpleshell.handler.impl;

import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.handler.CommandAccessManager;
import de.raysha.lib.jsimpleshell.handler.CommandAccessManager.AccessDecision.Decision;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link CommandAccessManager} delegates all request to a list of
 * {@link CommandAccessManager}s.
 *
 * @author rainu
 */
public class CompositeCommandAccessManager implements CommandAccessManager {
	private final List<CommandAccessManager> delegates;
	private MessageResolver messageResolver;

	public CompositeCommandAccessManager(MessageResolver messageResolver){
		this.delegates = new ArrayList<CommandAccessManager>();
		this.messageResolver = messageResolver;
	}

	/**
	 * Add a {@link CommandAccessManager} as a delegate.
	 *
	 * @param manager The access manager.
	 */
	public void addCommandAccessManager(CommandAccessManager manager){
		this.delegates.add(manager);
	}

	/**
	 * Remove a {@link CommandAccessManager} from the delegates.
	 *
	 * @param manager The access manager.
	 */
	public void removeCommandAccessManager(CommandAccessManager manager){
		this.delegates.remove(manager);
	}

	@Override
	public AccessDecision checkCommandPermission(Context context) {
		if(delegates.isEmpty()) {
			return defaultAccessDecision();
		}

		List<AccessDecision> decisions = collectAccessDecisions(context);
		AccessDecision finalDecision = aggregateDecisions(decisions);

		return finalDecision;
	}

	private AccessDecision defaultAccessDecision() {
		return new AccessDecision(Decision.ALLOWED);
	}

	private List<AccessDecision> collectAccessDecisions(Context context) {
		List<AccessDecision> decisions = new ArrayList<AccessDecision>(delegates.size());

		for(CommandAccessManager curManager : delegates){
			AccessDecision decision = curManager.checkCommandPermission(context);
			decisions.add(decision);
		}

		return decisions;
	}

	private AccessDecision aggregateDecisions(List<AccessDecision> decisions) {
		Decision decision = null;
		String reason = null;

		 if(isMute(decisions)){
			decision = Decision.MUTE;
			reason = createAggregatedMuteReasonMessage(decisions);
		}else if(isAllowed(decisions)){
			decision = Decision.ALLOWED;
		}else{
			decision = Decision.DENIED;
			reason = createAggregatedDeniedReasonMessage(decisions);
		}

		return new AccessDecision(decision, reason);
	}

	private boolean isAllowed(List<AccessDecision> decisions) {
		for(AccessDecision decision : decisions){
			if(decision.getDecision() == Decision.DENIED){
				return false;
			}
		}

		return true;
	}

	private boolean isMute(List<AccessDecision> decisions) {
		for(AccessDecision decision : decisions){
			if(decision.getDecision() == Decision.MUTE){
				return true;
			}
		}

		return false;
	}

	private String createAggregatedDeniedReasonMessage(List<AccessDecision> decisions) {
		StringBuilder finalMessage = new StringBuilder();

		for(AccessDecision decision : decisions){
			if(decision.getDecision() == Decision.DENIED && decision.getReason() != null){
				String curMessage = messageResolver.resolveGeneralMessage(decision.getReason());

				finalMessage.append(curMessage);
				finalMessage.append("\n");
			}
		}

		return finalMessage.toString();
	}

	private String createAggregatedMuteReasonMessage(List<AccessDecision> decisions) {
		StringBuilder finalMessage = new StringBuilder();

		for(AccessDecision decision : decisions){
			if(decision.getDecision() == Decision.MUTE && decision.getReason() != null){
				String curMessage = messageResolver.resolveGeneralMessage(decision.getReason());

				finalMessage.append(curMessage);
				finalMessage.append("\n");
			}
		}

		return finalMessage.toString();
	}
}
