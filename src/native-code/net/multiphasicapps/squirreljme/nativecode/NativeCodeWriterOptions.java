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
 * This class contains options which are used to configure the native code
 * generator.
 *
 * This class is immutable.
 *
 * @since 2016/09/15
 */
public final class NativeCodeWriterOptions
{
	/**
	 * Initialize the native code writer.
	 *
	 * @param __b The builder containing options to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	NativeCodeWriterOptions(NativeCodeWriterOptionsBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

