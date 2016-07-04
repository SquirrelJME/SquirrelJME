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

import net.multiphasicapps.squirrelsim.SimulationProvider;

/**
 * This implements the base support needed for Linux simulation.
 *
 * @since 2016/07/04
 */
public abstract class LinuxProvider
	extends SimulationProvider
{
	/**
	 * Initializes the base Linux support.
	 *
	 * @param __arch The architecture to simulate.
	 * @param __osvar The operating system variant.
	 * @since 2016/07/04
	 */
	public LinuxProvider(String __arch, String __osvar)
	{
		super(__arch, "linux", __osvar);
	}
}

