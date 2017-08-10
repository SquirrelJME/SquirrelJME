// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.mips;

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.bin.FragmentBuilder;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITConfigValue;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is used to generate MIPS machine code from a generic assembly like
 * representation.
 *
 * @since 2017/08/09
 */
public class MIPSCodeOutput
	extends MachineCodeOutput
{
	/** The target fragment. */
	protected final FragmentBuilder out;
	
	/**
	 * Initializes the MIPS code output.
	 *
	 * @param __conf The JIT configuration.
	 * @param __out The output fragment builder.
	 * @throws JITException If the configuration is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public MIPSCodeOutput(JITConfig __conf, FragmentBuilder __out)
		throws JITException, NullPointerException
	{
		super(__conf);
		
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

