// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.HashMap;
import java.util.Map;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @param <C> The configuration class.
 * @since 2017/02/02
 */
public abstract class JITConfig<C extends JITConfig<C>>
{
	/** Mapping of configuration values. */
	private final Map<String, String> _values;
	
	/**
	 * Initializes the JIT configuration.
	 *
	 * @param __kvp Key/value pairs to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public JITConfig(String... __kvp)
		throws NullPointerException
	{
		this(new __KVPMap__(__kvp));
	}
	
	/**
	 * Initializes the JIT configuration.
	 *
	 * @param __kvp Key/value pairs to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public JITConfig(Map<String, String> __kvp)
		throws NullPointerException
	{
		// Check
		if (__kvp == null)
			throw new NullPointerException("NARG");
		
		// Copy
		this._values = new HashMap<>(__kvp);
	}
	
	/**
	 * Returns the class which is used to serialize and de-serialize the
	 * JIT.
	 *
	 * @return The serializer for this JIT configuration.
	 * @since 2017/02/01
	 */
	public abstract JITConfigSerializer<C> serializer();
}

