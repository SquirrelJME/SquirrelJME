// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is used to configure the input and output of a JIT operation. This
 * is used as a singular source for configuration to simplify passing of
 * arguments along with the potential of adding more details when required.
 *
 * @since 2016/07/04
 */
public final class JITOutputConfig
{
	/**
	 * Initializes a blank configuration.
	 *
	 * @since 2016/07/04
	 */
	public JITOutputConfig()
	{
	}
	
	/**
	 * This is an immutable copy of an output configuration which does not
	 * change so that it may safely be used by the {@link JITOutputFactory}
	 * without worrying about state changes.
	 *
	 * @since 2016/07/04
	 */
	public static class Immutable
	{
		/**
		 * Initializes an immutable configuration which does not change.
		 *
		 * @param __joc The base configuration.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/04
		 */
		private Immutable(JITOutputConfig __joc)
			throws NullPointerException
		{
			// Lock
			if (__joc == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
	}
}

