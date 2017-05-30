// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.mips;

import java.io.InputStream;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.java.ClassCompiler;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.LinkTable;

/**
 * This manages the configuration for MIPS based targets.
 *
 * @since 2017/05/29
 */
public class MIPSConfig
	extends JITConfig
{
	/**
	 * Initializes the MIPS configuration.
	 *
	 * @param __o The input JITconfiguration.
	 * @since 2017/05/30
	 */
	public MIPSConfig(Map<String, String> __o)
	{
		super(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public ClassCompiler compileClass(InputStream __is,
		LinkTable __lt)
		throws NullPointerException
	{
		// Check
		if (__is == null || __lt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

