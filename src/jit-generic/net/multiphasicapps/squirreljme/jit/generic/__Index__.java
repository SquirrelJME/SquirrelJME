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

import net.multiphasicapps.squirreljme.os.generic.BlobContentType;

/**
 * Stores a single index entry.
 *
 * @since 2016/08/15
 */
final class __Index__
{
	/** The type of blob this is. */
	final BlobContentType _type;
	
	/** The data position. */
	final int _datapos;
	
	/** The entry size. */
	final int _size;
	
	/** The index in the global string table which stores the entry name. */
	final __StringEntry__ _namedx;
	
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
		int ds;
		this._type = __bw._type;
		this._datapos = (ds = __bw._dataaddr);
		this._size = (__bw._dataend - ds);
		this._namedx = __bw._owner._gpool.__loadString(__bw._name);
	}
}

