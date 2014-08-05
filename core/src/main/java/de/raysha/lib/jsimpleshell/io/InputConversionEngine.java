/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell.io;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.raysha.lib.jsimpleshell.Token;
import de.raysha.lib.jsimpleshell.exception.CLIException;
import de.raysha.lib.jsimpleshell.exception.TokenException;
import de.raysha.lib.jsimpleshell.handler.InputConverter;

/**
 * This thing is responsible for converting strings to objects.
 * Elementary types can be handled by itself, and arbitrary types can be handled
 * by registering InputConverter instances.
 * It also gets all converters declared in a handler object 
 * through addDeclaredConverters method.
 * 
 * Used by Shell and will also be used by ShellCommand.
 *
 * @author ASG
 */
public class InputConversionEngine {

    private List<InputConverter> inputConverters = new ArrayList<InputConverter>();

    public void addConverter(InputConverter converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter == null");
        }
        inputConverters.add(converter);
    }

    public boolean removeConverter(InputConverter converter) {
        return inputConverters.remove(converter);
    }

    public Object convertInput(String string, Class aClass) throws Exception {
        for (InputConverter currentConverter : inputConverters ){
            Object conversionResult = currentConverter.convertInput(string, aClass);
            if (conversionResult != null) {
                if (!aClass.isAssignableFrom(conversionResult.getClass())) {
                    throw new CLIException("Registered asg.Cliche converter " +
                            currentConverter + " returns wrong result");
                } else {
                    return conversionResult;
                }
            }
        }
        return convertArgToElementaryType(string, aClass);
    }

    public final Object[] convertToParameters(List<Token> tokens, Class[] paramClasses, boolean isVarArgs)
            throws TokenException {

        assert isVarArgs || paramClasses.length == tokens.size()-1;

        Object[] parameters = new Object[paramClasses.length];
        for (int i = 0; i < parameters.length-1; i++) {
            try {
                parameters[i] = convertInput(tokens.get(i + 1).getString(), paramClasses[i]);
            } catch (CLIException ex) {
                throw new TokenException(tokens.get(i + 1), ex.getMessage());
            } catch (Exception e) {
                throw new TokenException(tokens.get(i + 1), e);
            }
        }
        int lastIndex = paramClasses.length-1;
        if (isVarArgs) {
            Class varClass = paramClasses[lastIndex];
            assert varClass.isArray();
            Class elemClass = varClass.getComponentType();
            Object theArray = Array.newInstance(elemClass, tokens.size() - paramClasses.length);
            for (int i = 0; i < Array.getLength(theArray); i++) {
                try {
                    Array.set(theArray, i, convertInput(
                            tokens.get(lastIndex + 1 + i).getString(),
                            elemClass));
                } catch (CLIException ex) {
                    throw new TokenException(tokens.get(lastIndex + 1 + i), ex.getMessage());
                } catch (Exception e) {
                    throw new TokenException(tokens.get(lastIndex + 1 + i), e);
                }
            }
            parameters[lastIndex] = theArray;
        } else if (lastIndex >= 0) {
            try {
                parameters[lastIndex] = convertInput(
                        tokens.get(lastIndex+1).getString(),
                        paramClasses[lastIndex]);
            } catch (CLIException ex) {
                throw new TokenException(tokens.get(lastIndex+1), ex.getMessage());
            } catch (Exception e) {
                throw new TokenException(tokens.get(lastIndex+1), e);
            }
        }

        return parameters;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object convertArgToElementaryType(String string, Class aClass) throws CLIException {
        if (aClass.equals(String.class) || aClass.isInstance(string)) {
            return string;
        } else if (aClass.equals(Integer.class) || aClass.equals(Integer.TYPE)) {
            return Integer.parseInt(string);
        } else if (aClass.equals(Long.class) || aClass.equals(Long.TYPE)) {
            return Long.parseLong(string);
        } else if (aClass.equals(Double.class) || aClass.equals(Double.TYPE)) {
            return Double.parseDouble(string);
        } else if (aClass.equals(Float.class) || aClass.equals(Float.TYPE)) {
            return Float.parseFloat(string);
        } else if (aClass.equals(Boolean.class) || aClass.equals(Boolean.TYPE)) {
        	if("true".equalsIgnoreCase(string)) return true;
        	if("false".equalsIgnoreCase(string)) return false;
        	
        	throw new CLIException("Can't convert string to " + aClass.getName());
        } else if (aClass.equals(BigDecimal.class)) {
            return new BigDecimal(string);
        } else if (aClass.equals(BigInteger.class)) {
            return new BigInteger(string);
        } else if (aClass.isEnum()){
        	try{
        		return Enum.valueOf(aClass, string);
        	}catch(Exception e){
        		throw new CLIException("Can't convert string to an enum-element.", e);
        	}
        } else {
            try {
                Constructor c = aClass.getConstructor(String.class);
                try {
                    return c.newInstance(string);
                } catch (Exception ex) {
                    throw new CLIException(String.format(
                            "Error instantiating class %c using string %s", aClass, string), ex);
                }
            } catch (NoSuchMethodException e) {
                throw new CLIException("Can't convert string to " + aClass.getName());
            }
        }
    }

    public void addDeclaredConverters(Object handler) {
    	if(handler instanceof InputConverter){
    		addConverter((InputConverter)handler);
    	}
    }
}
