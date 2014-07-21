/*
 * This file is part of the Cliche project, licensed under MIT License.
 * See LICENSE.txt file in root folder of Cliche sources.
 */

package de.raysha.lib.jsimpleshell;

/**
 * This interface is for classes that want to be aware of entering
 * and leaving each command loop.
 *
 * You might want some special resources to be allocated before CLI
 * starts; after conversation you want to free those resources.
 * By implementing this interface you get the ability to handle these
 * events.
 *
 * Note that since Shell can possibly have other means of operation
 * instead of commandLoop(), these methods may be not called.
 *
 * @author ASG
 */
public interface ShellManageable {

    /**
     * This method is called when it is about to enter the command loop.
     * 
     * @param shell That {@link Shell} which entered the loop.
     */
    void cliEnterLoop(Shell shell);

    /**
     * This method is called when Shell is leaving the command loop.
     * 
     * @param shell That {@link Shell} which leaved the loop.
     */
    void cliLeaveLoop(Shell shell);
}
