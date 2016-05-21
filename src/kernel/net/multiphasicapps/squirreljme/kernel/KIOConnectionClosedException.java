// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.IOException;

/**
 * This is thrown by a socket when the connection between a client and a server
 * has been closed by either side.
 *
 * @since 2016/05/21
 */
public final class KIOConnectionClosedException
	extends IOException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/05/21
	 */
	public KIOConnectionClosedException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use for the exception.
	 * @since 2016/05/21
	 */
	public KIOConnectionClosedException(String __m)
	{
		super(__m);
	}
}

