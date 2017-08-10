// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class acts as the base for machine code output which is implemented by
 * each architecture. The machine code output does not care about any
 * optimizations that were performed, it just writes whatever instructions to
 * some output target. It should be noted that in most cases the output here
 * is intended really only to be written once rather than having multiple
 * variants of it. The machine code outputs are not intended in any way to
 * optimize what is input.
 *
 * @since 2017/08/07
 */
public abstract class MachineCodeOutput
{
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/**
	 * Initializes the base machine code output which sets the configuration
	 * and the target for the machine code.
	 *
	 * @param __conf The JIT configuration.
	 * @throws JITException If the configuration is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public MachineCodeOutput(JITConfig __conf)
		throws JITException, NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Returns the used JIT configuration.
	 *
	 * @return The JIT configuration.
	 * @since 2017/08/10
	 */
	public final JITConfig config()
	{
		return this.config;
	}
}

