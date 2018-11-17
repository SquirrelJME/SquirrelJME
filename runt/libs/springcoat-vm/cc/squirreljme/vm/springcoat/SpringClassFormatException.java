// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import net.multiphasicapps.classfile.ClassName;

/**
 * This is thrown when the format of a class is not correct, it is invalid.
 *
 * @since 2018/08/05
 */
public class SpringClassFormatException
	extends SpringException
{
	/** The name of the class. */
	protected final ClassName name;
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __cn The class which was not valid.
	 * @throws NullPointerException If no class was specified.
	 * @since 2018/08/05
	 */
	public SpringClassFormatException(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		this.name = __cn;
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __cn The class which was not valid.
	 * @param __m The message.
	 * @throws NullPointerException If no class was specified.
	 * @since 2018/08/05
	 */
	public SpringClassFormatException(ClassName __cn, String __m)
		throws NullPointerException
	{
		super(__m);
		
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		this.name = __cn;
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __cn The class which was not valid.
	 * @param __m The message.
	 * @param __c The cause.
	 * @throws NullPointerException If no class was specified.
	 * @since 2018/08/05
	 */
	public SpringClassFormatException(ClassName __cn, String __m,
		Throwable __c)
		throws NullPointerException
	{
		super(__m, __c);
		
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		this.name = __cn;
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __cn The class which was not valid.
	 * @param __c The cause.
	 * @throws NullPointerException If no class was specified.
	 * @since 2018/08/05
	 */
	public SpringClassFormatException(ClassName __cn, Throwable __c)
		throws NullPointerException
	{
		super(__c);
		
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		this.name = __cn;
	}
	
	/**
	 * Returns the name of the class.
	 *
	 * @return The class name.
	 * @since 2018/08/05
	 */
	public final ClassName name()
	{
		return this.name;
	}
}

