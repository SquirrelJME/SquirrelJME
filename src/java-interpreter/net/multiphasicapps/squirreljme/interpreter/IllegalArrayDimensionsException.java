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
 * This is thrown when an attempt is made to create an array which has an\
 * illegal number of dimensions.
 *
 * @since 2016/03/05
 */
public class IllegalArrayDimensionsException
	extends InterpreterFailureException
{
	/**
	 * Initializes the exception with the given dimensional count.
	 *
	 * @param __dims The dimensions to use.
	 * @since 2016/03/05
	 */
	public IllegalArrayDimensionsException(int __dims)
	{
		super("Illegal array dimensions: " + __dims + ".");
	}
}

