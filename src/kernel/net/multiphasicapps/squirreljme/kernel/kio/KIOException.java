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
 * This is the base class for kernel I/O related exception.
 *
 * Kernel exceptions may not have a cause.
 *
 * @since 2016/05/20
 */
public class KIOException
	extends RuntimeException
{
	/**
	 * Initializes an exception with no description.
	 *
	 * @since 2016/05/20
	 */
	public KIOException()
	{
		super(null);
	}
	
	/**
	 * Initializes an exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/05/20
	 */
	public KIOException(String __s)
	{
		super(__s, null);
	}
	
	/**
	 * Kernel I/O exceptions may never have a cause assigned to them.
	 *
	 * @return {@code null}.
	 * @since 2016/05/20
	 */
	public final Throwable getCause()
	{
		return null;
	}
	
	/**
	 * Kernel I/O exceptions may never have a cause assigned to them.
	 *
	 * @param __t This parameter is ignored.
	 * @return {@code this}.
	 * @since 2016/05/20
	 */
	public final Throwable initCause(Throwable __t)
	{
		return this;
	}
}

