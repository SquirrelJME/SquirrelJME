// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This is thrown when there is an issue with installation.
 *
 * @since 2017/12/31
 */
@Deprecated
class __InstallException__
	extends RuntimeException
{
	/** The error code. */
	protected final int code;
	
	/**
	 * Initializes the exception with the given code and message.
	 *
	 * @param __code The error code.
	 * @param __m The message.
	 * @since 2017/12/31
	 */
	public __InstallException__(int __code, String __m)
	{
		super(__m);
		
		this.code = __code;
	}
	
	/**
	 * Returns the error code.
	 *
	 * @return The error code.
	 * @since 2017/12/31
	 */
	public final int code()
	{
		return code;
	}
}

