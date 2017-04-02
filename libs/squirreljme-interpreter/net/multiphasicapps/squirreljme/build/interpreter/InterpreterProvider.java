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

import java.util.Map;
import net.multiphasicapps.squirreljme.build.interpreter.mips.MIPSProvider;

/**
 * This is the base class for providers .
 *
 * @since 2017/04/02
 */
public abstract class InterpreterProvider
{
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
		// Default to MIPS if not specified
		String arch = __kv.get("generic.arch");
		if (arch == null)
			arch = "mips";
		
		// Depends on the architecture
		switch (arch)
		{
				// {@squirreljme.error AV05 Unknown architecture specified.
				// (The architecture)}
			default:
				throw new IllegalArgumentException(String.format("AV05 %s",
					arch));
		}
	}
}

