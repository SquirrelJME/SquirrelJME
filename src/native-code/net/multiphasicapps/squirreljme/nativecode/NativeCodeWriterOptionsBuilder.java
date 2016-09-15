// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This is used to build {@link NativeCodeWriter} which is used to configure
 * the native code generator.
 *
 * @since 2016/09/15
 */
public class NativeCodeWriterOptionsBuilder
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Builds the native code writer options.
	 *
	 * @return An immutable set of options.
	 * @since 2016/09/15
	 */
	public final NativeCodeWriterOptions build()
	{
		// Lock
		synchronized (this.lock)
		{
			return new NativeCodeWriterOptions(this);
		}
	}
}

