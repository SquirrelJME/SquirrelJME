// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This represents the constant pool which exists within a class.
 *
 * Java ME targets cannot have constants pertaining to invokedynamic.
 *
 * @since 2016/03/13
 */
public class InterpreterClassPool
{
	/** The class which owns the constant pool. */
	protected final InterpreterClass owner;	
	
	/**
	 * Initializes and interprets the constant pool of a class.
	 *
	 * @param __cl The class owning the pool.
	 * @param __is The input data for the constant pool.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/13
	 */
	InterpreterClassPool(InterpreterClass __cl, DataInputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__cl == null || __is == null)
			throw new NullPointerException();
		
		// Set
		owner = __cl;
		
		throw new Error("TODO");
	}
}

