/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.rainu.lib.jsimpleshell.io;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Output conversion engine is responsible for converting objects after they are returned
 * by command but before they are sent to the Output.
 * As with InputConversionEngine, it can automatically retrieve all converters declared inside
 * an object.
 *
 * All converters are applied to all objects, first-registered--last-applied.
 *
 * Used by Shell.
 *
 * @author ASG
 */
public class OutputConversionEngine {

    private List<OutputConverter> outputConverters = new ArrayList<OutputConverter>();

    public void addConverter(OutputConverter converter) {
        if (converter == null ) {
            throw new IllegalArgumentException("Converter == null");
        }
        outputConverters.add(converter);
    }

    public boolean removeConverter(OutputConverter converter) {
        return outputConverters.remove(converter);
    }

    public Object convertOutput(Object anObject) {
        Object convertedOutput = anObject;
        for (ListIterator<OutputConverter> it = outputConverters.listIterator(outputConverters.size()); it.hasPrevious();) {
            OutputConverter outputConverter = it.previous(); // last in --- first called.
            Object conversionResult = outputConverter.convertOutput(convertedOutput);
            if (conversionResult != null) {
                convertedOutput = conversionResult;
            }
        }
        return convertedOutput;
    }

    public void addDeclaredConverters(Object handler) {
    	if(handler instanceof OutputConverter){
    		addConverter((OutputConverter)handler);
    	}
    }

}
