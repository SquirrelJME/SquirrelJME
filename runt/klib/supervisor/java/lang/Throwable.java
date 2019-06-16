// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is the base class for all throwable types.
 *
 * @since 2019/05/25
 */
public class Throwable
{
	/** The message to use. */
	transient final String _message;
	
	/** The cause of this exception. */
	transient final Throwable _cause;
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/25
	 */
	public Throwable()
	{
		this._message = null;
		this._cause = null;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/25
	 */
	public Throwable(String __m)
	{
		this._message = __m;
		this._cause = null;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2019/05/25
	 */
	public Throwable(String __m, Throwable __t)
	{
		this._message = __m + "hiya";
		this._cause = __t;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2019/05/25
	 */
	public Throwable(Throwable __t)
	{
		this._message = null;
		this._cause = __t;
	}
	
	/**
	 * Returns the message.
	 *
	 * @return The message used.
	 * @since 2019/06/16
	 */
	public String getMessage()
	{
		return this._message;
	}
}

