// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mmu;

/**
 * This is thrown when an attempt was made to modify the address by performing
 * a mathematical operation on it. For example, adding bytes which would result
 * in a value that overflows memory would result in an invalid value.
 *
 * @since 2016/06/09
 */
public class MemoryAddressOperationException
	extends MemoryException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/06/09
	 */
	public MemoryAddressOperationException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/06/09
	 */
	public MemoryAddressOperationException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/06/09
	 */
	public MemoryAddressOperationException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/06/09
	 */
	public MemoryAddressOperationException(Throwable __c)
	{
		super(__c);
	}
}

