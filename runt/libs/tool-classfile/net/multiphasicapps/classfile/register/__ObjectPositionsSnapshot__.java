// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.Arrays;

/**
 * This is a snapshot of object positions that appear within a state. Note that
 * these refer to register positions and as such, stack and local values do
 * not have a defined meaning here.
 *
 * @since 2019/03/22
 */
final class __ObjectPositionsSnapshot__
{
	/** The index where the stack starts. */
	protected final int stackstart;
	
	/** Position data. */
	private final int[] _pos;
	
	/**
	 * Initializes the positions.
	 *
	 * @param __ss The stack start position.
	 * @param __p The positions to use.
	 * @since 2019/03/22
	 */
	__ObjectPositionsSnapshot__(int __ss, int... __p)
	{
		this.stackstart = __ss;
		this._pos = (__p == null ? new int[0] : __p.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof __ObjectPositionsSnapshot__))
			return false;
		
		return Arrays.equals(this._pos,
			((__ObjectPositionsSnapshot__)__o)._pos);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final int hashCode()
	{
		int rv = 0;
		for (int i : this._pos)
			rv += i;
		return rv;
	}
}

