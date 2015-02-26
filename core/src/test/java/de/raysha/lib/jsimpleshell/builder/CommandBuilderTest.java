package de.raysha.lib.jsimpleshell.builder;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import de.raysha.lib.jsimpleshell.annotation.CommandDefinition;

public class CommandBuilderTest {

	public static interface TestInterface {
		public void public1();
	}

	public static class TestClass implements TestInterface{

		public void public1(){}
		public void getOne(){}
		public void setOne(){}

		public void isOne(){}
		private void setTwo(){}

		private void private1(){}
		void private2(){}
		protected void protected1(){}
	}

	@Test
	public void allPublic(){
		check(new String[]{"public1", "getOne", "setOne", "isOne"},
			CommandBuilder.allPublic(new TestClass()));
	}

	@Test
	public void complete(){
		check(new String[]{"public1", "getOne", "setOne", "isOne", "setTwo", "private1", "private2", "protected1"},
			CommandBuilder.complete(new TestClass()));
	}

	@Test
	public void allGetter(){
		check(new String[]{"getOne", "isOne"},
			CommandBuilder.allGetter(new TestClass()));
	}

	@Test
	public void allSetter(){
		check(new String[]{"setOne"},
			CommandBuilder.allSetter(new TestClass()));
	}

	@Test
	public void fromClass(){
		check(new String[]{"public1"},
			CommandBuilder.fromClass(new TestClass(), TestInterface.class));
	}

	private void check(String[] expected, Collection<CommandDefinition> given){
		check(Arrays.asList(expected), given);
	}

	private void check(Collection<String> expected, Collection<CommandDefinition> given){
		assertEquals("There are different count of elements!" + given, expected.size(), given.size());

		for(String e : expected){
			boolean found = false;
			for(CommandDefinition def : given){
				if(e.equals(def.getMethod().getName())){
					found = true;
					break;
				}
			}

			assertTrue("Method \"" + e + "\" not found!", found);
		}
	}
}
