// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.server;

/**
 * This is thrown when the error during installation is very basic.
 *
 * @since 2018/01/15
 */
class __PlainInstallError__
	extends RuntimeException
{
	/** The error code. */
	protected final int code;
	
	/**
	 * Initializes the plain error.
	 *
	 * @param __c The error code.
	 * @param __m The error message.
	 * @since 2018/01/15
	 */
	public __PlainInstallError__(int __c, String __m)
	{
		super(__m);
		
		this.code = __c;
	}
	
	/**
	 * Returns the error code.
	 *
	 * @return The error code.
	 * @since 2018/01/15
	 */
	public final int code()
	{
		return this.code;
	}
}

