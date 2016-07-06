// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.linux.powerpc;

import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;
import net.multiphasicapps.squirreljme.jit.JITTriplet;

/**
 * This output factory targets PowerPC Linux systems.
 *
 * @since 2016/07/04
 */
public class LinuxPPCOutputFactory
	extends JITOutputFactory
{
	/**
	 * Factory which targets generic Linux.
	 *
	 * @since 2016/07/04
	 */
	public LinuxPPCOutputFactory()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public JITOutput create(JITOutputConfig.Immutable __config)
		throws JITException, NullPointerException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DX01 The requested configuration is not
		// supported by this output factory. (The configuration})
		if (!supportsConfig(__config))
			throw new JITException(String.format(String.format("DX01 %s",
				__config));
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public boolean supportsConfig(JITOutputConfig.Immutable __config)
		throws NullPointerException
	{
		// Check
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Get the triplet
		JITTriplet triplet = __config.triplet();
		
		// Only generic Linux on any PowerPC 32-bit or 64-bit system (big
		// or little)
		int bits = triplet.bits();
		JITCPUEndian endian = triplet.endianess();
		return triplet.architecture().equals("powerpc") &&
			triplet.operatingSystem().equals("linux") &&
			triplet.operatingSystemVariant().equals("generic") &&
			(bits == 32 || bits == 64) &&
			(endian == JITCPUEndian.BIG || endian == JITCPUEndian.LITTLE);
	}
}

