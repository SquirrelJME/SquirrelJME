// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * Exception that may be thrown by {@link SystemCallError.checkError(int)}.
 *
 * @since 2020/01/12
 */
public class SystemCallException
	extends RuntimeException
{
	/** The system call ID. */
	public final int callid;
	
	/** The error code. */
	public final int code;
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/10/21
	 */
	public SystemCallException(int __sid, int __ec)
	{
		this.callid = __sid;
		this.code = __ec;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/10/21
	 */
	public SystemCallException(int __sid, int __ec, String __m)
	{
		super(__m);
		
		this.callid = __sid;
		this.code = __ec;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2019/10/21
	 */
	public SystemCallException(int __sid, int __ec, String __m, Throwable __t)
	{
		super(__m, __t);
		
		this.callid = __sid;
		this.code = __ec;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2019/10/21
	 */
	public SystemCallException(int __sid, int __ec, Throwable __t)
	{
		super(__t);
		
		this.callid = __sid;
		this.code = __ec;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/01/12
	 */
	@Override
	public String getMessage()
	{
		return "[SID=" + this.callid + ", ERR=" + this.code + "]: " +
			super.getMessage();
	}
}

