// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter.mips;

import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.mips.MIPSConfig;
import net.multiphasicapps.squirreljme.build.interpreter.InterpreterProvider;

/**
 * This provides the JIT and environment for MIPS CPUs.
 *
 * @since 2017/04/02
 */
public class MIPSProvider
	extends InterpreterProvider
{
	/**
	 * Initializes the MIPS provider.
	 *
	 * @param __kv The keys to use for the JIT configuration.
	 * @since 2017/04/02
	 */
	public MIPSProvider(Map<String, String> __kv)
	{
		super(__initConfig(__kv));
	}
	
	/**
	 * Initializes the MIPS configuration.
	 *
	 * @param __kv The key/value map for the JIT.
	 * @return The MIPS configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/02
	 */
	private static MIPSConfig __initConfig(Map<String, String> __kv)
		throws NullPointerException
	{
		// Check
		if (__kv == null)
			throw new NullPointerException("NARG");
		
		// Clone
		__kv = new HashMap<>(__kv);
		
		// Default to the first edition mips
		if (!__kv.containsKey("mips.cpu"))
			__kv.put("mips.cpu", "i");
		
		// Default to 32-bit
		if (!__kv.containsKey("generic.bits"))
			__kv.put("generic.bits", "32");
		
		// Default to big endian
		if (!__kv.containsKey("generic.endianess"))
			__kv.put("generic.endianess", "big");
		
		// Create
		return new MIPSConfig(__kv);
	}
}

