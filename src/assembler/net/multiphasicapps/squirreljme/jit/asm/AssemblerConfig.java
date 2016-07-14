// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.asm;

/**
 * This is the configuration which may be passed to an assembler so that it
 * knows how native code should be generated.
 *
 * This class is immutable.
 *
 * @since 2016/07/02
 */
public final class AssemblerConfig
{
	/**
	 * Obtains the state of a given option to determine if it is enabled,
	 * disabled, or is not a valid option.
	 *
	 * @param __o The option to get.
	 * @return The state of the given option.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/02
	 */
	public AssemblerOptionState get(String __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Creates a new configuration which may be passed to the assembler so that
	 * when native code is to be generated it may be adjusted to better fit
	 * the target machine.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/02
	 */
	public static AssemblerConfig create()
		throws NullPointerException
	{
		throw new Error("TODO");
	}
}

