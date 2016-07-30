// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.IOException;

/**
 * This represents an exception which can be handled without causing the
 * emulator to terminate when there is a problem reading and/or writing.
 *
 * @since 2016/07/30
 */
public class EmulatorIOException
	extends IOException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/07/30
	 */
	public EmulatorIOException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/07/30
	 */
	public EmulatorIOException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/07/30
	 */
	public EmulatorIOException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/07/30
	 */
	public EmulatorIOException(Throwable __c)
	{
		super(__c);
	}
}

