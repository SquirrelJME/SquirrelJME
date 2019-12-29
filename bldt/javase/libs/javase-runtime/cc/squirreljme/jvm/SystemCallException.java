// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This is an exception for system calls.
 *
 * @since 2019/12/29
 */
public class SystemCallException
	extends RuntimeException
{
	/** The error code. */
	public final int code;
	
	/**
	 * Initializes the exception.
	 *
	 * @param __code The error code.
	 * @throws IllegalArgumentException If this is zero or positive.
	 * @since 2019/12/29
	 */
	public SystemCallException(int __code)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AF0f Invalid system call error code.}
		if (__code >= 0)
			throw new IllegalArgumentException("AF0f");
		
		this.code = __code;
	}
}

