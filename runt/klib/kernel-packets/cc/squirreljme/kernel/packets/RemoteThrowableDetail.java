// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.packets;

/**
 * This class contains the details for the remotely thrown exception.
 *
 * @since 2018/01/07
 */
public final class RemoteThrowableDetail
{
	/** The message. */
	protected final String message;
	
	/** The cause. */
	protected final Throwable cause;
	
	/** The base class type. */
	protected final String basetype;
	
	/** The class type. */
	protected final String type;
	
	/** The trace. */
	protected final String trace;
	
	/**
	 * This contains the details for the remotely thrown exception.
	 *
	 * @param __m The message for the exception.
	 * @param __t The cause of the exception.
	 * @param __l The class type of the thrown exception.
	 * @param __bl The base class type of the thrown exception.
	 * @param __t The remote trace of the exception.
	 * @throws NullPointerException If {@code __l} is null.
	 * @since 2018/01/07
	 */
	public RemoteThrowableDetail(String __m, Throwable __c,
		String __l, String __bl, String __t)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.message = __m;
		this.cause = __c;
		this.type = __l;
		this.basetype = (__bl != null ? __bl : __l);
		this.trace = __t;
	}
	
	/**
	 * Returns the base type of the exception.
	 *
	 * @return The exception base type.
	 * @since 2018/01/07
	 */
	public final String baseType()
	{
		return this.basetype;
	}
	
	/**
	 * Returns the cause of the exception.
	 *
	 * @return The exception cause.
	 * @since 2018/01/07
	 */
	public final Throwable cause()
	{
		return this.cause;
	}
	
	/**
	 * Returns the message for the exception.
	 *
	 * @return The message for the exception.
	 * @since 2018/01/7
	 */
	public final String message()
	{
		return this.message;
	}
	
	/**
	 * Returns the remote trace of the exception.
	 *
	 * @return The remote trace.
	 * @since 2018/01/07
	 */
	public final String trace()
	{
		return this.trace;
	}
	
	/**
	 * Returns the class type of the exception.
	 *
	 * @return The exception class type.
	 * @since 2018/01/06
	 */
	public final String type()
	{
		return this.type;
	}
}

