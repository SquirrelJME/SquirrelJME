// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim.os.linux;

/**
 * This provides support for simulating 32-bit PowerPC Linux systems.
 *
 * @since 2016/07/04
 */
public class LinuxPPC32Provider
	extends LinuxProvider
{
	/**
	 * Initializes the base 32-bit PowerPC Linux provider.
	 *
	 * @since 2016/07/04
	 */
	public LinuxPPC32Provider()
	{
		this("generic");
	}
	
	/**
	 * Simulates a variant of a Linux system.
	 *
	 * @param __osvar The variant of Linux to use.
	 * @since 2016/07/04
	 */
	public LinuxPPC32Provider(String __osvar)
	{
		super("powerpc32", __osvar);
	}
}

