// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

/**
 * This must be thrown if the state change in a MIDlet has failed, however
 * it is deprecated and no longer should be used.
 *
 * @since 2019/09/25
 */
@Deprecated
public class MIDletStateChangeException
	extends Exception
{
	/**
	 * Initializes the exception with no message,
	 *
	 * @since 2019/09/25
	 */
	@Deprecated
	public MIDletStateChangeException()
	{
	}

	/**
	 * Initializes the exception with the given message,
	 *
	 * @param __s The message to use.
	 * @since 2019/09/25
	 */
	@Deprecated
	public MIDletStateChangeException(String __s)
	{
		super(__s);
	}
}

