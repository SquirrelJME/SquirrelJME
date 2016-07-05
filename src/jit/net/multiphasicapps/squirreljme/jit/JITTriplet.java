// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This class provides a representation of triplets so that they may be
 * encoded and decoded.
 *
 * @since 2016/07/05
 */
public final class JITTriplet
{
	/** The architecture. */
	protected final String architecture;
	
	/** The number of bits used. */
	protected final int bits;
	
	/** The variant of the CPU. */
	protected final String cpuvar;
	
	/** The endianess of the CPU. */
	protected final JITCPUEndian endianess;
	
	/** The operating system. */
	protected final String os;
	
	/** The variant of the operating system. */
	protected final String osvar;
	
	/**
	 * This decodes the given input string as a triplet.
	 *
	 * @param __t The triplet to decode.
	 * @throws IllegalArgumentException If the triplet is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	public JITTriplet(String __t)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ED03 Expected two periods in the triplet.
		// (The triplet)}
		int dota = __t.indexOf('.');
		if (dota < 0)
			throw new IllegalArgumentException(String.format("ED03 %s", __t));
		int dotb = __t.indexOf('.', dota + 1);
		if (dotb < 0)
			throw new IllegalArgumentException(String.format("ED03 %s", __t));
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/05
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

