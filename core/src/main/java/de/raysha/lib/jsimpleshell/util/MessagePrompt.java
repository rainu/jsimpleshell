package de.raysha.lib.jsimpleshell.util;

import de.raysha.lib.jsimpleshell.PromptElement;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;

/**
 * This {@link PromptElement} will always display a fixed message. But the message
 * will be always resolved for each execute of the {@link PromptElement#render()} method.
 *
 * @author rainu
 *
 */
public class MessagePrompt implements PromptElement {
	private final MessageResolver messageResolver;
	private final String messageKey;

	public MessagePrompt(String msgKey, MessageResolver resolver) {
		this.messageKey = msgKey;
		this.messageResolver = resolver;
	}

	@Override
	public String render() {
		return messageResolver.resolveGeneralMessage(messageKey);
	}

}
