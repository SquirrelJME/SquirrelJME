// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.Map;

/**
 * This class is used to find services according to a JIT configuration by
 * CPU.
 *
 * This class is used with {@link java.util.ServiceLoader}
 *
 * @since 2017/08/09
 */
public interface JITConfigService
{
	/**
	 * Creates a JIT configuration using the given settings as a base.
	 *
	 * @param __v The configuration settings.
	 * @return The configuration to use.
	 * @throws JITException If the configuration is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public abstract JITConfig createConfig(
		Map<JITConfigKey, JITConfigValue> __v)
		throws JITException, NullPointerException;
	
	/**
	 * This checks if the given service is compatible with the given
	 * configuration value for the architecture.
	 *
	 * @param __v The architecture value to check.
	 * @return Whether or not the architecture matches.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public abstract boolean matchesArchitecture(JITConfigValue __v)
		throws NullPointerException;
}

