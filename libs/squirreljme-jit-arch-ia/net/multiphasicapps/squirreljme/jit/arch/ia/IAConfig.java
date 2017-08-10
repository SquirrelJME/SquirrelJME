// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.ia;

import java.io.InputStream;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.bin.FragmentBuilder;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITConfigValue;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This manages the configuration for Intel architecture based targets.
 *
 * @since 2017/08/09
 */
public class IAConfig
	extends JITConfig
{
	/**
	 * Initializes the IA configuration.
	 *
	 * @param __o The input JIT configuration.
	 * @since 2017/05/30
	 */
	public IAConfig(Map<JITConfigKey, JITConfigValue> __o)
	{
		super(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/09
	 */
	@Override
	public MachineCodeOutput createMachineCodeOutput(FragmentBuilder __f)
		throws JITException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		return new IACodeOutput(this, __f);
	}
}

