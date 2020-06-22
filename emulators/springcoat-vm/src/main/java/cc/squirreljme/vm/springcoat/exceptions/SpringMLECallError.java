// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.exceptions;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.springcoat.SpringConvertableThrowable;
import cc.squirreljme.vm.springcoat.SpringException;

/**
 * This is SpringCoat's variant of {@link MLECallError}.
 *
 * @since 2020/06/22
 */
public class SpringMLECallError
	extends SpringException
	implements SpringConvertableThrowable
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2020/06/22
	 */
	public SpringMLECallError()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/06/22
	 */
	public SpringMLECallError(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2020/06/22
	 */
	public SpringMLECallError(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2020/06/22
	 */
	public SpringMLECallError(Throwable __c)
	{
		super(__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public String targetClass()
	{
		return "cc/squirreljme/jvm/mle/exceptions/MLECallError";
	}
}
