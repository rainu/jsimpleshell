/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell.exception;


/**
 *
 * Root exception for Cliche.
 *
 * @author ASG
 */
public class CLIException extends Exception {
    public CLIException() {
        super();
    }
    public CLIException(String message) {
        super(message);
    }
    public CLIException(Throwable cause) {
        super(cause);
    }
    public CLIException(String message, Throwable cause) {
        super(message, cause);
    }
}
