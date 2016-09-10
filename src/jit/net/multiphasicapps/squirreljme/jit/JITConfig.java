// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This represents and stores the configuration which is used to configure
 * how the JIT generates code.
 *
 * This class is serialized to a single string which is then stored in a
 * system property of the resultant executable so that at run-time the JIT may
 * be reconfigured to generate code for a target without requiring assistance.
 *
 * @since 2016/09/10
 */
public final class JITConfig
{
	/** The property which defines the target triplet. */
	public static final String TRIPLET_PROPERTY =
		"net.multiphasicapps.squirreljme.jit.triplet";
	
	/**
	 * Initializes the configuration from the given builder.
	 *
	 * @param __b The builder to get information from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	JITConfig(JITConfigBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This deserializes the JIT configuration from the given string and
	 * re-initializes any required fields from it.
	 *
	 * @param __s The string to deserialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public JITConfig(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Serializes this configuration so that it may be stored within a system
	 * property.
	 *
	 * @return The serialized form of the current configuration.
	 * @since 2016/09/10
	 */
	public final String serialize()
	{
		throw new Error("TODO");
	}
}

