// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.client;

import cc.squirreljme.runtime.cldc.library.Library;

/**
 * This is called after a library has been installed and is used as a report.
 *
 * @since 2017/12/28
 */
public final class LibraryInstallationReport
{
	/** The library which was installed. */
	protected final Library library;
	
	/** The error code if installation failed. */
	protected final int error;
	
	/** The error message supplied. */
	protected final String message;
	
	/**
	 * Specifies that the program installed without error.
	 *
	 * @param __l The library which was installed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public LibraryInstallationReport(Library __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.library = __l;
		this.error = 0;
		this.message = null;
	}
	
	/**
	 * Specifies that the program could not be installed.
	 *
	 * @param __e The error code.
	 * @param __m The message associated with the error.
	 * @throws IllegalArgumentException If the error code is zero.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public LibraryInstallationReport(int __e, String __m)
		throws IllegalArgumentException, NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AV08 Cannot initialize an error report with no
		// error.}
		if (__e == 0)
			throw new IllegalArgumentException("AV08");
		
		this.library = null;
		this.error = __e;
		this.message = __m;
	}
	
	/**
	 * Returns the error code if the program failed to install.
	 *
	 * @return The installation error code or {@code 0} on no error.
	 * @since 2017/12/28
	 */
	public final int error()
	{
		return this.error;
	}
	
	/**
	 * Returns the installed library.
	 *
	 * @return The installed library or {@code null} if it failed.
	 * @since 2017/12/28
	 */
	public final Library library()
	{
		return this.library;
	}
	
	/**
	 * Returns the message associated with the error.
	 *
	 * @return The error message.
	 * @since 2017/12/31
	 */
	public final String message()
	{
		return this.message;
	}
}

