// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.kio;

/**
 * This is thrown when the remote end does not have a listening socket for
 * a given address and port.
 *
 * @since 2016/05/20
 */
public class KIOConnectionRefusedException
	extends KIOException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/05/20
	 */
	public KIOConnectionRefusedException()
	{
		super();
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2016/05/20
	 */
	public KIOConnectionRefusedException(String __m)
	{
		super(__m);
	}
}

