// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.IOException;

/**
 * This is thrown when the connection was not available.
 *
 * @since 2019/05/06
 */
public class ConnectionNotFoundException
	extends IOException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/06
	 */
	public ConnectionNotFoundException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/06
	 */
	public ConnectionNotFoundException(String __m)
	{
		super(__m);
	}
}


