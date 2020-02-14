// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMException;

/**
 * This is an exception in SummerCoat which is translated to an exception
 * for the virtual machine itself when it has a chance to do so.
 *
 * @since 2019/01/11
 */
public class VMRuntimeException
	extends VMException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2019/01/11
	 */
	public VMRuntimeException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/01/11
	 */
	public VMRuntimeException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2019/01/11
	 */
	public VMRuntimeException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2019/01/11
	 */
	public VMRuntimeException(Throwable __c)
	{
		super(__c);
	}
}

