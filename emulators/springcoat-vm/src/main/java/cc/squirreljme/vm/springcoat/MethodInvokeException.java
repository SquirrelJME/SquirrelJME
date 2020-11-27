// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This wraps an exception that was thrown within an invoked method.
 *
 * @since 2020/09/15
 */
public class MethodInvokeException
	extends SpringException
{
	/** The exception thrown. */
	public final SpringObject exception;
	
	/**
	 * Initializes the invoked exception.
	 * 
	 * @param __exception The exception that was tossed.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/15
	 */
	public MethodInvokeException(SpringObject __exception)
		throws NullPointerException
	{
		if (__exception == null)
			throw new NullPointerException("NARG");
		
		this.exception = __exception;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/15
	 */
	@Override
	public final String toString()
	{
		return this.exception.toString();
	}
}
