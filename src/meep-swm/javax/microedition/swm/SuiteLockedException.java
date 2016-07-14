// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import java.io.IOException;

/**
 * This is thrown when a suite cannot be removed, either because it is not
 * permitted or it is currently in use.
 *
 * @since 2016/06/24
 */
public class SuiteLockedException
	extends IOException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/06/24
	 */
	public SuiteLockedException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/06/24
	 */
	public SuiteLockedException(String __s)
	{
		super(__s);
	}
}

