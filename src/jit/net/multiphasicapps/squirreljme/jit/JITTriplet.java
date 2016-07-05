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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

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
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
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
		
		// Split into three forms
		String fullarch = __t.substring(0, dota),
			os = __t.substring(dota + 1, dotb),
			osvar = __t.substring(dotb + 1);
		
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
	
	/**
	 * Checks that triplet fragments are well formed.
	 *
	 * @param __s The string to check.
	 * @return {@code __s}, properly converted.
	 * @throws IllegalArgumentException If it is not well formed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	private static final String __check(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

