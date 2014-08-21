package de.raysha.lib.jsimpleshell.example;

import de.raysha.lib.jsimpleshell.annotation.Command;
import de.raysha.lib.jsimpleshell.annotation.Inject;
import de.raysha.lib.jsimpleshell.handler.MessageResolver;
import de.raysha.lib.jsimpleshell.handler.MessageResolverDependent;
import de.raysha.lib.jsimpleshell.handler.impl.AbstractMessageResolver;

/**
 * If you want to resolve you own messages or you wants to override the default messages
 * (such like the default command names, descriptions ... and so on) you have to implement
 * your own {@link MessageResolver}. After that you must add these resolver to the shell handlers
 * (in the most cases the {@link MessageResolver} should be an aux-handler). You can also implements
 * multiple resolvers! If you want to use the {@link MessageResolver} that is currently used you can
 * just annotate a field/method via {@link Inject} or implement the {@link MessageResolverDependent} interface!
 *
 * @author rainu
 *
 */
public class MessageResolving extends AbstractMessageResolver {

	//get access to the currently used message resolver
	@Inject
	private MessageResolver resolver;

	@Override
	protected String resolveMessage(String msg) {
		//if an message should be resolve the shell will ask me! Because I am a registered MessageResolver :)
		return msg.replace("general.message", "This is a resolved message!");
	}

	@Command
	public String resolveMessage(){
		//we use the currently used message resolve to resolve my own message
		return resolver.resolveGeneralMessage("general.message");
	}

}
