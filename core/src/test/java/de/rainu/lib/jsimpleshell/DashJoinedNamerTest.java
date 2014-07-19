/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.rainu.lib.jsimpleshell;

import java.lang.reflect.Method;

import org.junit.Test;

import de.rainu.lib.jsimpleshell.CommandNamer;
import de.rainu.lib.jsimpleshell.DashJoinedNamer;
import de.rainu.lib.jsimpleshell.CommandNamer.NamingInfo;
import de.rainu.lib.jsimpleshell.annotation.Command;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class DashJoinedNamerTest {

    public DashJoinedNamerTest() {
    }

    private static class TestClass{
        @Command
        public void cliTestMethod1() { }
        @Command
        public void cmdTestMethod2() { }
    }

    @Test
    public void testNameCommand() throws NoSuchMethodException {
        System.out.println("nameCommand");
        CommandNamer namer = new DashJoinedNamer(true);
        Method[] methods = new Method[] {
            TestClass.class.getMethod("cliTestMethod1"),
            TestClass.class.getMethod("cmdTestMethod2"),
        };

        CommandNamer.NamingInfo[] expecteds = new CommandNamer.NamingInfo[] {
            new NamingInfo("test-method-1", new String[]{"tm1", "teme1"}),
            new NamingInfo("test-method-2", new String[]{"tm2", "teme2"}),
        };
        CommandNamer.NamingInfo[] results = new NamingInfo[methods.length];
        for (int i = 0; i < methods.length; i++) {
            results[i] = namer.nameCommand(methods[i]);
        }
        
        for (int i = 0; i < methods.length; i++) {
            System.out.println(results[i]);
            assertEquals(expecteds[i].commandName, results[i].commandName);
            assertArrayEquals(expecteds[i].possibleAbbreviations, results[i].possibleAbbreviations);
        }

    }

}