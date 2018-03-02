// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.datagrampackets;

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
	
	/** The local name. */
	protected final String localname;
	
	/** The remote name. */
	protected final String remotename;
	
	/**
	 * This contains the details for the remotely thrown exception.
	 *
	 * @param __m The message for the exception.
	 * @param __t The cause of the exception.
	 * @param __l The class type of the thrown exception.
	 * @param __bl The base class type of the thrown exception.
	 * @param __t The remote trace of the exception.
	 * @throws NullPointerException If {@code __l}, {@code __ln}, or
	 * {@code __rn} is null.
	 * @since 2018/01/07
	 */
	public RemoteThrowableDetail(String __m, Throwable __c,
		String __l, String __bl, String __t, String __ln, String __rn)
		throws NullPointerException
	{
		if (__l == null || __ln == null || __rn == null)
			throw new NullPointerException("NARG");
		
		this.message = __m;
		this.cause = __c;
		this.type = __l;
		this.basetype = (__bl != null ? __bl : __l);
		this.trace = __t;
		this.localname = __ln;
		this.remotename = __rn;
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
	 * Returns the local name of the stream.
	 *
	 * @return The stream local name.
	 * @since 2018/01/18
	 */
	public final String localName()
	{
		return this.localname;
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
	 * Returns the remote name of the stream.
	 *
	 * @return The stream remote name.
	 * @since 2018/01/18
	 */
	public final String remoteName()
	{
		return this.remotename;
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

