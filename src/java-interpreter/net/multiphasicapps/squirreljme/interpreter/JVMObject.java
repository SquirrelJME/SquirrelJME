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

/**
 * This represents an object within the interpreter.
 *
 * @since 2016/03/01
 */
public class InterpreterObject
{
	/** The class this object is. */
	protected final InterpreterClass classtype;
	
	/**
	 * Initializes the object.
	 *
	 * @param __type This type of class this object is.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public InterpreterObject(InterpreterClass __type)
	{
		// Check
		if (__type == null)
			throw new NullPointerException();
		
		// Set
		classtype = __type;
	}
}

