// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigSerializer;

/**
 * This is the configuration that is used to configure the JIT for the MIPS
 * translation engine.
 *
 * @since 2017/02/02
 */
public class MIPSConfig
	extends JITConfig<MIPSConfig>
{
	/**
	 * Initializes the MIPS config.
	 *
	 * @param __kvp The key/value pairs.
	 * @since 2017/02/02
	 */
	public MIPSConfig(String... __kvp)
	{
		super(__kvp);
	}
	
	/**
	 * Initializes the MIPS config.
	 *
	 * @param __kvp The key/value pairs.
	 * @since 2017/02/02
	 */
	public MIPSConfig(Map<String, String> __kvp)
	{
		super(__kvp);
	}
	
	/**
	 * Returns the revision of the MIPS CPU to target.
	 *
	 * @return The revision of the CPU to target.
	 * @since 2017/02/13
	 */
	public MIPSRevision mipsRevision()
	{
		String v = internalValue("mips.revision");
		if (v == null)
			return MIPSRevision.I;
		return MIPSRevision.of(v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/02
	 */
	@Override
	public MIPSConfigSerializer serializer()
	{
		return MIPSConfigSerializer.instance();
	}
}

