// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericOutput;
import net.multiphasicapps.squirreljme.jit.generic.GenericOutputFactory;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;

/**
 * This is the factory which produces output which can be used to target any
 * MIPS based system.
 *
 * @since 2016/08/07
 */
public class MIPSOutputFactory
	extends GenericOutputFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	protected GenericOutput internalCreate(JITOutputConfig.Immutable __config)
		throws JITException, NullPointerException
	{
		return new MIPSOutput(__config);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public boolean supportsConfig(JITOutputConfig.Immutable __config)
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Accept any MIPS CPU
		JITTriplet triplet = __config.triplet();
		return triplet.architecture().equals("mips");
	}
}

