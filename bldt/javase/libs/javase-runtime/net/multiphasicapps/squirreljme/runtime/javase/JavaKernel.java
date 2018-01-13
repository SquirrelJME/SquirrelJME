// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.cldc.OperatingSystemType;

/**
 * This implements the kernel which is used on the initial Java SE
 * process and not the client processes.
 *
 * @since 2017/12/08
 */
public final class JavaKernel
	extends Kernel
{
	/**
	 * Initializes the kernel to run on Java systems.
	 *
	 * @since 2017/12/08
	 */
	public JavaKernel()
	{
		super(new JavaConfiguration());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/13
	 */
	@Override
	public final OperatingSystemType operatingSystemType()
	{
		String osname = System.getProperty("os.name", "").toLowerCase();
		
		// Java 7 does not run on Windows 98, so just return NT
		if (osname.contains("windows"))
			return OperatingSystemType.WINDOWS_NT;
		
		// Linux
		else if (osname.contains("linux"))
			return OperatingSystemType.LINUX;
		
		// Solaris
		else if (osname.contains("solaris"))
			return OperatingSystemType.SOLARIS;
		
		// Mac OS X
		else if (osname.contains("mac os x"))
			return OperatingSystemType.MAC_OS_X;
		
		return OperatingSystemType.UNKNOWN;
	}
}

