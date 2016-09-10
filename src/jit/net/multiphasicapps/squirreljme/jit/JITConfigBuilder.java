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
 * This class is used to build instances of {@link JITConfig} which is used
 * to configure the JIT compiler system.
 *
 * @since 2016/09/10
 */
public class JITConfigBuilder
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Builds the configuration to use.
	 *
	 * @return The resultant configuration.
	 * @throws JITException If the configuration is not valid.
	 * @since 2016/09/10
	 */
	public JITConfig build()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			return new JITConfig(this);
		}
	}
}

