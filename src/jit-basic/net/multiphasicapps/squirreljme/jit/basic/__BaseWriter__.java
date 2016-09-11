// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This is the base class for the class and resource writers.
 *
 * @since 2016/09/11
 */
abstract class __BaseWriter__
{
	/** The positioned data. */
	private final __Positioned__ _positioned;
	
	/**
	 * Initializes the resource writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __pos The positioned entry data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	__BaseWriter__(BasicNamespaceWriter __nsw, __Positioned__ __pos)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __pos == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._positioned = __pos;
		
		// Get the base output to write to, this is wrapped so sizes are known,
		// prevents closing the external output, and makes thing a bit more
		// sane
		ExtendedDataOutputStream dos = __nsw._output;
		throw new Error("TODO");
	}
}

