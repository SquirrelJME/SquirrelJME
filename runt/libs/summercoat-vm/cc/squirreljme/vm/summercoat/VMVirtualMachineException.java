// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VMException;

/**
 * This is thrown when there is an issue with the virtual machine itself or
 * the code which is running.
 *
 * @since 2019/04/19
 */
@Deprecated
public class VMVirtualMachineException
	extends VMException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2019/04/19
	 */
	public VMVirtualMachineException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/04/19
	 */
	public VMVirtualMachineException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2019/04/19
	 */
	public VMVirtualMachineException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2019/04/19
	 */
	public VMVirtualMachineException(Throwable __c)
	{
		super(__c);
	}
}
