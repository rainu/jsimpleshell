package de.raysha.lib.jsimpleshell.handler.impl;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

public class DefaultMessageResolverTest {

	DefaultMessageResolver resolver = DefaultMessageResolver.getInstance();

	@Test
	public void german(){
		resolver.setLocale(Locale.GERMAN);
		assertEquals("Kommando", resolver.resolveGeneralMessage("message.general.command"));
	}

	@Test
	public void english(){
		resolver.setLocale(Locale.ENGLISH);
		assertEquals("Command", resolver.resolveGeneralMessage("message.general.command"));
	}

	@Test
	public void notAvailable(){
		Locale.setDefault(Locale.JAPANESE);
		resolver.setLocale(Locale.JAPANESE);
		assertEquals("Command", resolver.resolveGeneralMessage("message.general.command"));
	}
}
