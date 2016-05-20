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

/**
 * This exception is used to indicate that the userspace process threw an
 * exception for the current thread.
 *
 * @since 2016/05/20
 */
public class UserSpaceException
	extends RuntimeException
{
	/**
	 * Initializes an exception with no description.
	 *
	 * @since 2016/05/20
	 */
	public UserSpaceException()
	{
		super((Throwable)null);
	}
	
	/**
	 * Initializes an exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/05/20
	 */
	public UserSpaceException(String __s)
	{
		super(__s, (Throwable)null);
	}
	
	/**
	 * Userspace exceptions may never have a cause assigned to them.
	 *
	 * @return {@code null}.
	 * @since 2016/05/20
	 */
	public final Throwable getCause()
	{
		return null;
	}
	
	/**
	 * Kernel exceptions may never have a cause assigned to them.
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

