// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is thrown when the virtual machine has ran out of memory.
 *
 * @since 2019/05/26
 */
public class OutOfMemoryError
	extends VirtualMachineError
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/26
	 */
	public OutOfMemoryError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/26
	 */
	public OutOfMemoryError(String __m)
	{
		super(__m);
	}
}

