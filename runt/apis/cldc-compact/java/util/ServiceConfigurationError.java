// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is thrown when a service has not been configured properly.
 *
 * @since 2018/12/06
 */
public class ServiceConfigurationError
	extends Error
{
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/06
	 */
	public ServiceConfigurationError(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the error with a message and the given cause.
	 *
	 * @param __m The message to use.
	 * @param __t The cause of this error.
	 * @since 2018/12/06
	 */
	public ServiceConfigurationError(String __m, Throwable __t)
	{
		super(__m, __t);
	}
}

