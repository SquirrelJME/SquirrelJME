// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This is called after a program has been installed and is used as a report.
 *
 * @since 2017/12/28
 */
public final class KernelProgramInstallReport
{
	/** The program which was installed. */
	protected final KernelProgram program;
	
	/** The error code if installation failed. */
	protected final int error;
	
	/**
	 * Specifies that the program installed without error.
	 *
	 * @param __p The program which was installed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public KernelProgramInstallReport(KernelProgram __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.program = __p;
		this.error = 0;
	}
	
	/**
	 * Specifies that the program could not be installed.
	 *
	 * @param __e The error code.
	 * @throws IllegalArgumentException If the error code is zero.
	 * @since 2017/12/28
	 */
	public KernelProgramInstallReport(int __e)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ0b Cannot initialize an error report with no
		// error.}
		if (__e == 0)
			throw new IllegalArgumentException("ZZ0b");
		
		this.program = null;
		this.error = __e;
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
	 * Returns the installed program.
	 *
	 * @return The installed program or {@code null} if it failed.
	 * @since 2017/12/28
	 */
	public final KernelProgram program()
	{
		return this.program;
	}
}

