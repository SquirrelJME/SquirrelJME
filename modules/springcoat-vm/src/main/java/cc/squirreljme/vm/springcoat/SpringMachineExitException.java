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
 * This is thrown when the virtual machine is exiting.
 *
 * @since 2018/10/13
 */
public class SpringMachineExitException
	extends SpringException
{
	/** The exit code. */
	protected final int code;
	
	/**
	 * Initializes the exception with the given exit code.
	 *
	 * @param __code The exit code.
	 * @since 2018/10/13
	 */
	public SpringMachineExitException(int __code)
	{
		super("" + __code);
		
		this.code = __code;
	}
	
	/**
	 * Returns the exit code.
	 *
	 * @return The exit code.
	 * @since 2018/10/33
	 */
	public final int code()
	{
		return this.code;
	}
}

