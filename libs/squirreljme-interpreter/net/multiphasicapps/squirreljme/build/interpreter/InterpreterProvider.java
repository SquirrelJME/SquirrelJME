// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.build.interpreter.mips.MIPSProvider;

/**
 * This is the base class for providers for the interpreter. The interpreter
 * requires the JIT to generate machine code along with having an execution
 * engine that can execute the machine code the JIT generates.
 *
 * @since 2017/04/02
 */
public abstract class InterpreterProvider
{
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/**
	 * Initializes the base provider for the interpreter.
	 *
	 * @param __conf The JIT configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/02
	 */
	protected InterpreterProvider(JITConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Returns the JIT configuration.
	 *
	 * @return The JIT config.
	 * @since 2017/04/02
	 */
	public final JITConfig config()
	{
		return this.config;
	}
	
	/**
	 * Initializes the interpreter provider with the given input map.
	 *
	 * @param __kv The JIT configuration options.
	 * @return The provider for the given architecture.
	 * @throws IllegalArgumentException If the architecture is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/02
	 */
	public static InterpreterProvider of(Map<String, String> __kv)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__kv == null)
			throw new NullPointerException("NARG");
		
		// Default to MIPS if not specified
		__kv = new HashMap<>(__kv);
		String arch = __kv.get("generic.arch");
		if (arch == null)
			__kv.put("generic.arch", (arch = "mips"));
		
		// Depends on the architecture
		switch (arch)
		{
			case "mips":		return new MIPSProvider(__kv);
			
				// {@squirreljme.error AV05 Unknown architecture specified.
				// (The architecture)}
			default:
				throw new IllegalArgumentException(String.format("AV05 %s",
					arch));
		}
	}
}

