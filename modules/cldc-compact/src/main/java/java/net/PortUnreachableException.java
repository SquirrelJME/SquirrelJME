// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.net;

/**
 * This is thrown when the remote system has a port which cannot be reached.
 *
 * @since 2018/12/08
 */
public class PortUnreachableException
	extends SocketException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/08
	 */
	public PortUnreachableException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/08
	 */
	public PortUnreachableException(String __m)
	{
		super(__m);
	}
}

