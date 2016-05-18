// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.execfinder;

/**
 * This interface describes an executable which binds to a JAR or some other
 * internal storage for executables. This class describes which kind of program
 * the executable is and how it is ran. When it comes to execution, the
 * executable may run differently. For example the executable could be part of
 * executable ROM and as such starting execution would essentially just be
 * setting up a state and then setting the program counter to the entry address
 * of the program
 *
 * @since 2016/05/16
 */
public interface Executable
{
	/**
	 * Returns the type of executable that this is.
	 *
	 * @return The type of executable that this is.
	 * @since 2016/05/18
	 */
	public abstract Type type();
	
	/**
	 * The type of executable that this is.
	 *
	 * @since 2016/05/18
	 */
	public static enum Type
	{
		/** A classic {@code Main-Class}/{@code Class-Path} program. */
		JAVA,
		
		/** A MIDlet. */
		APPLICATION,
		
		/** A LIBlet. */
		LIBRARY,
		
		/** A single class (which has a main). */
		CLASS,
		
		/** End. */
		;
	}
}

