// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * Stores a single index entry.
 *
 * @since 2016/08/15
 */
final class __Index__
{
	/** The information position. */
	final int _infopos;
	
	/** The data position. */
	final int _datapos;
	
	/** The entry size. */
	final int _size;
	
	/**
	 * Initializes the index.
	 *
	 * @param __bw The base writer to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	__Index__(__BaseWriter__ __bw)
		throws NullPointerException
	{
		// Check
		if (__bw == null)
			throw new NullPointerException("NARG");
		
		// Record
		this._infopos = __bw._startaddr;
		int ds;
		this._datapos = (ds = __bw._dataaddr);
		this._size = (__bw._dataend - ds);
	}
}

