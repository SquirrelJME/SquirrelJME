// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

import net.multiphasicapps.squirreljme.runtime.syscall.SystemCaller;

/**
 * This class provides access to the system's default system call interface
 * which interacts with the kernel.
 *
 * @since 2017/12/10
 */
public final class SystemCall
{
	/** The caller to use when making system calls. */
	public static final SystemCaller _CALLER =
		__systemCaller();
	
	/**
	 * Not used.
	 *
	 * @since 2017/12/10
	 */
	private SystemCall()
	{
	}
	
	/**
	 * This returns the instance of the system caller which is to be used.
	 *
	 * @return The system caller interface.
	 * @since 2017/12/10
	 */
	private static final SystemCaller __systemCaller()
	{
		return SystemCall._CALLER;
	}
}

