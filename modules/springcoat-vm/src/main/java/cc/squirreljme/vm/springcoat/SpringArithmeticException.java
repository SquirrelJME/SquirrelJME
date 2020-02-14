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

/**
 * This is thrown when a divide by zero occurs.
 *
 * @since 2018/12/04
 */
public class SpringArithmeticException
	extends SpringException
	implements SpringConvertableThrowable
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	public SpringArithmeticException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/12/04
	 */
	public SpringArithmeticException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/12/04
	 */
	public SpringArithmeticException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/12/04
	 */
	public SpringArithmeticException(Throwable __c)
	{
		super(__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String targetClass()
	{
		return "java/lang/ArithmeticException";
	}
}

