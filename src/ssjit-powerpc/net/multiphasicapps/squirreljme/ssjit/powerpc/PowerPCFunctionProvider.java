// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit.powerpc;

import net.multiphasicapps.squirreljme.ssjit.SSJITFunctionProvider;
import net.multiphasicapps.squirreljme.ssjit.SSJITVariant;

/**
 * This is the base class for functions which target PowerPC.
 *
 * @since 2016/06/25
 */
public abstract class PowerPCFunctionProvider
	extends SSJITFunctionProvider
{
	/**
	 * Initializes the function provider with the given architecture and an
	 * optional operating system.
	 *
	 * @param __arch The architecture to use.
	 * @param __os The operating system to use.
	 * @since 2016/06/25
	 */
	public PowerPCFunctionProvider(String __arch, String __os)
	{
		super(__arch, __os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public SSJITVariant genericVariant()
	{
		// Default to ancient PowerPC systems
		return DefaultPowerPCVariant.G1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public SSJITVariant[] variants()
	{
		// Just use the enumeration
		return DefaultPowerPCVariant.values();
	}
}

