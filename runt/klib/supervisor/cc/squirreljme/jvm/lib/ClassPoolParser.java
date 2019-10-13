// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.Assembly;

/**
 * This class is used to parse individual pool treads.
 *
 * @see ClassDualPoolParser
 * @since 2019/10/12
 */
public final class ClassPoolParser
{
	/** The address where the constant pool is. */
	public final int romaddress;
	
	/** The size of this pool. */
	private int _size =
		-1;
	
	/**
	 * Initializes the constant pool parser.
	 *
	 * @param __ra The ROM address.
	 * @since 2019/10/12
	 */
	public ClassPoolParser(int __ra)
	{
		this.romaddress = __ra;
	}
	
	/**
	 * Returns the size of the constant pool.
	 *
	 * @return The pool size.
	 * @since 2019/10/13
	 */
	public final int size()
	{
		// If the size is negative, it has never been read before
		int rv = this._size;
		if (rv < 0)
			this._size = (rv = Assembly.memReadInt(this.romaddress,
				ClassPoolConstants.OFFSET_OF_INT_ENTRY_OFFSET));
		
		return rv;
	}
}

