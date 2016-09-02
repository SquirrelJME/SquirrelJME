// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux.powerpc;

import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.linux.LinuxBuildInstance;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * This is the build instance for Linux PowerPC systems.
 *
 * @since 2016/09/02
 */
public class LinuxPowerPCBuildInstance
	extends LinuxBuildInstance
{
	/**
	 * Initializes the build instance.
	 *
	 * @param __conf The build configuration.
	 * @since 2016/09/02
	 */
	public LinuxPowerPCBuildInstance(BuildConfig __conf)
	{
		super(__conf);
		
		// {@squirreljme.error BU05 Only PowerPC is supported by this build
		// instance.}
		JITTriplet triplet = __conf.triplet();
		if (triplet.architecture() != "powerpc")
			throw new TargetNotSupportedException("BU05");
		
		throw new Error("TODO");
	}
}

