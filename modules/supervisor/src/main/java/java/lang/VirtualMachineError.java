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
 * This is thrown when there is a problem with the virtual machine.
 *
 * @since 2019/05/26
 */
public class VirtualMachineError
	extends Error
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/26
	 */
	public VirtualMachineError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/26
	 */
	public VirtualMachineError(String __m)
	{
		super(__m);
	}
}

