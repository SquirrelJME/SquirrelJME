// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

import cc.squirreljme.runtime.cldc.system.type.ClassType;

/**
 * This is thrown when the remote end of a system call throws an error.
 *
 * @since 2018/03/14
 */
public final class SystemCallError
	extends Error
	implements SystemCallThrowable
{
	/** The class type of the exception. */
	protected final ClassType classtype;
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __cl The class type of the exception.
	 * @since 2018/03/14
	 */
	public SystemCallError(ClassType __cl)
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classtype = __cl;
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __cl The class type of the exception.
	 * @param __m The message.
	 * @since 2018/03/14
	 */
	public SystemCallError(ClassType __cl, String __m)
	{
		super(__m);
		
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classtype = __cl;
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __cl The class type of the exception.
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/03/14
	 */
	public SystemCallError(ClassType __cl, String __m, Throwable __c)
	{
		super(__m, __c);
		
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classtype = __cl;
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __cl The class type of the exception.
	 * @param __c The cause.
	 * @since 2018/03/14
	 */
	public SystemCallError(ClassType __cl, Throwable __c)
	{
		super(__c);
		
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classtype = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final ClassType classType()
	{
		return this.classtype;
	}
}

