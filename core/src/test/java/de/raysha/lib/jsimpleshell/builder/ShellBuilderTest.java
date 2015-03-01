package de.raysha.lib.jsimpleshell.builder;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ShellBuilderTest {

	@Test
	public void testComplexBuild() throws IOException{
		Object handler = new Object();

		ShellBuilder builder = ShellBuilder.shell("shell")
			.behavior()
				.addHandler(handler)
			.look()
				.disableColor()
			.io()
				.setError(new FileOutputStream(File.createTempFile("jss", ".jss")))
			.root();

		assertEquals("shell", builder.model.getPrompt().render());
		assertTrue(builder.model.getHandlers().contains(handler));
		assertFalse(builder.model.isColorOutput());
		assertEquals(FileOutputStream.class, builder.model.getError().getClass());
	}
}
