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

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @param <C> The configuration class.
 * @since 2017/02/02
 */
public interface JITConfig<C extends JITConfig<C>>
{
	/**
	 * Returns the class which is used to serialize and de-serialize the
	 * JIT.
	 *
	 * @return The serializer for this JIT configuration.
	 * @since 2017/02/01
	 */
	public abstract JITConfigSerializer<C> serializer();
}

