// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.exceptions;

import cc.squirreljme.vm.springcoat.SpringException;

/**
 * This is thrown when the virtual machine is exiting.
 *
 * @since 2018/10/13
 */
public class SpringMachineExitException
	extends SpringFatalException
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

