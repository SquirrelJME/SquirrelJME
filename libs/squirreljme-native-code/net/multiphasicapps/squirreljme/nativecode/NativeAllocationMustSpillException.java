// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This is thrown when an attempt was made to allocate just to registers,
 * however no registers were available for storing the entire width of the
 * type.
 *
 * @since 2016/09/18
 */
public class NativeAllocationMustSpillException
	extends NativeCodeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/09/18
	 */
	public NativeAllocationMustSpillException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/09/18
	 */
	public NativeAllocationMustSpillException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/09/18
	 */
	public NativeAllocationMustSpillException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/09/18
	 */
	public NativeAllocationMustSpillException(Throwable __c)
	{
		super(__c);
	}
}

