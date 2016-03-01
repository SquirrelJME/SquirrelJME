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
 * This represents an interpreted method within a class.
 *
 * @since 2016/03/01
 */
public class InterpreterMethod
{
	/** The class this method is in. */
	protected final InterpreterClass inclass;	
	
	/**
	 * Initializes the interpreted method.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	InterpreterMethod(InterpreterClass __owner)
		throws NullPointerException
	{
		// Check
		if (__owner == null)
			throw new NullPointerException();
		
		// Set
		inclass = __owner;
	}
}

