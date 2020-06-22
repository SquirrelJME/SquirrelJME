// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.exceptions;

/**
 * This is thrown when there was an error made during a MLE call.
 *
 * @since 2020/06/22
 */
public class MLECallError
	extends VirtualMachineError
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2020/06/22
	 */
	public MLECallError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/06/22
	 */
	public MLECallError(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2020/06/22
	 */
	public MLECallError(String __m, Throwable __t)
	{
		super(__m);
		
		this.initCause(__t);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2020/06/22
	 */
	public MLECallError(Throwable __t)
	{
		this.initCause(__t);
	}
}
