// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This is thrown when there is a problem with the kernel.
 *
 * @since 2016/05/20
 */
public class KernelException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/05/29
	 */
	public KernelException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message to use.
	 * @since 2016/05/29
	 */
	public KernelException(String __m)
	{
		super(__m);
	}
}

