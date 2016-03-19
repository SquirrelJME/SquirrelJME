// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cache;

/**
 * This is thrown when an {@link Instantiable} is used which is not valid
 * because it is not one.
 *
 * @since 2016/03/19
 */
public class IllegalInstantiableException
	extends RuntimeException
{
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/03/19
	 */
	public IllegalInstantiableException(String __s)
	{
		super(__s);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __s The message.
	 * @param __t The cause of it.
	 * @since 2016/03/19
	 */
	public IllegalInstantiableException(String __s, Throwable __t)
	{
		super(__s, __t);
	}
}

