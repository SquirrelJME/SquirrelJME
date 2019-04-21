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
 * This is thrown when there has been a change to the classes from compilation
 * time and the classes can no longer be loaded.
 *
 * @since 2019/04/17
 */
@Deprecated
public class VMIncompatibleClassChangeException
	extends VMException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2019/04/17
	 */
	public VMIncompatibleClassChangeException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/04/17
	 */
	public VMIncompatibleClassChangeException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2019/04/17
	 */
	public VMIncompatibleClassChangeException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2019/04/17
	 */
	public VMIncompatibleClassChangeException(Throwable __c)
	{
		super(__c);
	}
}

