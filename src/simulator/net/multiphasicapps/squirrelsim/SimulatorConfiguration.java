// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.io.IOException;
import java.io.Reader;

/**
 * This class contains a mutable configuration that the simulator will use
 * when it comes to initialization.
 *
 * @since 2016/06/14
 */
public class SimulatorConfiguration
{
	/**
	 * Initializes the simulator configuration which uses all defaults.
	 *
	 * @since 2016/06/14
	 */
	public SimulatorConfiguration()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This initializes the simulator configuration using the given
	 * configuration file.
	 *
	 * @param __r The configuration file to source from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public SimulatorConfiguration(Reader __r)
		throws IOException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

