/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.raysha.lib.jsimpleshell;

import org.junit.Before;
import org.junit.Test;

import de.raysha.lib.jsimpleshell.io.OutputConversionEngine;
import de.raysha.lib.jsimpleshell.io.OutputConverter;
import static org.junit.Assert.*;

/**
 *
 * @author ASG
 */
public class OutputConversionEngineTest implements OutputConverter{

    @Before
    public void setUp() {
        converter = new OutputConversionEngine();
        converter.addDeclaredConverters(this);
    }

    OutputConversionEngine converter;

    @Override
	public Object convertOutput(Object toBeFormatted) {
    	if (toBeFormatted instanceof String) {
            String result = String.format("[%s]", (String)toBeFormatted);
            return String.format("(%s)", (String)result);
        } else {
            return null;
        }
	}


    @Test
    public void testConvertOutput() {
        System.out.println("convertOutput");
        String toBeConverted = "a";
        String expected = "([a])";
        assertEquals(expected, converter.convertOutput(toBeConverted));
    }


}