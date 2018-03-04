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

import javax.microedition.swm.InstallErrorCodes;

/**
 * This is thrown when the error during installation is very basic.
 *
 * @since 2018/01/15
 */
class __PlainInstallError__
	extends RuntimeException
{
	/** The error code. */
	protected final InstallErrorCodes code;
	
	/**
	 * Initializes the plain error.
	 *
	 * @param __c The error code.
	 * @param __m The error message.
	 * @throws NullPointerException If no code was specified.
	 * @since 2018/01/15
	 */
	public __PlainInstallError__(InstallErrorCodes __c, String __m)
		throws NullPointerException
	{
		super(__m);
		
		if (__c == null)
			throw new NullPointerException("NARG");
		
		this.code = __c;
	}
	
	/**
	 * Returns the error code.
	 *
	 * @return The error code.
	 * @since 2018/01/15
	 */
	public final InstallErrorCodes code()
	{
		return this.code;
	}
}

