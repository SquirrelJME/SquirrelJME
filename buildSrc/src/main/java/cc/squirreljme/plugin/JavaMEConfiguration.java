// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

/**
 * Defines a Java ME Configuration.
 *
 * @since 2020/02/15
 */
public final class JavaMEConfiguration
{
	/**
	 * Defines full configuration.
	 *
	 * @param __full The full string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	public JavaMEConfiguration(String __full)
		throws NullPointerException
	{
		if (__full == null)
			throw new NullPointerException("No configuration specified");
		
		throw new Error("TODO");
	}
}

