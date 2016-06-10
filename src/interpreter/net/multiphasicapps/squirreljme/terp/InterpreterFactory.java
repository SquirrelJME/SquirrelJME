// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.util.Map;
import net.multiphasicapps.squirreljme.sm.StructureManager;

/**
 * This is a factory which is used to initialize an interpreter.
 *
 * This class is used with the {@link java.util.ServiceLoader} class.
 *
 * @since 2016/06/09
 */
public interface InterpreterFactory
{
	/**
	 * Creates an instance of the interpreter with the given arguments.
	 *
	 * @param __sm An optional structure manager which is used to manage and
	 * call the garbage collector on virtual machine objects.
	 * @param __args The arguments to pass to the interpreter.
	 * @return The created interpreter instance which uses the given arguments.
	 * @throws IllegalArgumentException If the input arguments are not valid.
	 * @since 2016/06/09
	 */
	public abstract Interpreter createInterpreter(StructureManager __sm,
		String... __args)
		throws IllegalArgumentException;
	
	/**
	 * Returns the "nice" name of the interpreter.
	 *
	 * @return The name of the interpreter.
	 * @since 2016/06/09
	 */
	public abstract String toString();
}

