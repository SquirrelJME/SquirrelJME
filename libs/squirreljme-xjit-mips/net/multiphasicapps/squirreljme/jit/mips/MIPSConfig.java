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
import net.multiphasicapps.squirreljme.jit.RegisterDictionary;

/**
 * This is the configuration for the MIPS JIT.
 *
 * @since 2017/04/02
 */
public class MIPSConfig
	extends JITConfig
{
	/**
	 * Initializes the MIPS configuration.
	 *
	 * @param __kv The key/value pairs.
	 * @since 2017/04/02
	 */
	public MIPSConfig(String... __kv)
	{
		super(__kv);
	}
	
	/**
	 * Initializes the MIPS configuration.
	 *
	 * @param __kv The key/value pairs.
	 * @since 2017/04/02
	 */
	public MIPSConfig(Map<String, String> __kv)
	{
		super(__kv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public RegisterDictionary registerDictionary()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public JITConfigSerializer serializer()
	{
		throw new todo.TODO();
	}
}

