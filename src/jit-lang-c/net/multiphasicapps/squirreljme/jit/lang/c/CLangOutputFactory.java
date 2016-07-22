// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang.c;

import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.lang.LangOutputFactory;

/**
 * This is a factory which compiles Java byte code to C.
 *
 * @since 2016/07/09
 */
public class CLangOutputFactory
	extends LangOutputFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public JITOutput create(JITOutputConfig.Immutable __config)
		throws JITException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA01 The specified configuration is not
		// supported by this JIT output factory. (The configuration)}
		if (!supportsConfig(__config))
			throw new JITException(String.format("BA01 %s", __config));
		
		// Create
		return new CLangOutput(__config);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public boolean supportsConfig(JITOutputConfig.Immutable __config)
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Get the triplet
		JITTriplet triplet = __config.triplet();
		
		// Only specific things are checked/supported
		return "lang".equals(triplet.architecture()) &&
			JITCPUEndian.UNDEFINED.equals(triplet.endianess()) &&
			"c".equals(triplet.operatingSystem()) &&
			"posix".equals(triplet.operatingSystemVariant());
	}
}

