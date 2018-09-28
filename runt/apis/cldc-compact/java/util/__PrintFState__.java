// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This stores the state for printf parsing.
 *
 * @since 2018/09/24
 */
final class __PrintFState__
{
	/** Flags specified. */
	final boolean[] _flags =
		new boolean[__PrintFFlag__.COUNT];
	
	/** The argument index. */
	int _argdx =
		-1;
	
	/** The width. */
	int _width =
		-1;
	
	/**
	 * Was a width specified?
	 *
	 * @return If a width was specified.
	 * @since 2018/09/24
	 */
	final boolean __hasWidth()
	{
		return this._width >= 1;
	}
	
	/**
	 * Sets the argument index.
	 *
	 * @param __dx The index to set.
	 * @throws IllegalArgumentException If the index is not valid.
	 * @since 2018/09/24
	 */
	final void __setArgumentIndex(int __dx)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1n Argument index already set or is of an
		// invalid value. (The index used)}
		if (__dx <= 0 || this._argdx >= 1)
			throw new IllegalArgumentException("ZZ1n " + __dx);
		
		this._argdx = __dx;
	}
	
	/**
	 * Sets the conversion.
	 *
	 * @param __p The primary conversion.
	 * @param __s The secondary conversion.
	 * @throws IllegalArgumentException If the conversion is not valid.
	 * @since 2018/09/28
	 */
	final void __setConversion(int __p, int __s)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the specified flag.
	 *
	 * @param __c The flag to set.
	 * @return If the flag is valid.
	 * @throws IllegalArgumentException If the flag was duplicated.
	 * @since 2018/09/24
	 */
	final boolean __setFlag(char __c)
		throws IllegalArgumentException
	{
		// Is this flag one that exists?
		__PrintFFlag__ f = __PrintFFlag__.__decode(__c);
		if (f == null)
			return false;
		
		boolean[] flags = this._flags;
		
		// {@squirreljme.error ZZ1p Duplicate flag specified. (The flag)}
		int ord = f.ordinal();
		if (flags[ord])
			throw new IllegalArgumentException("ZZ1p " + __c);
		
		// Use it
		flags[ord] = true;
		return true;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param __w The width to use.
	 * @throws IllegalArgumentException If the width is not valid.
	 * @since 2018/09/24
	 */
	final void __setWidth(int __w)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1o Width already or is of an invalid value.
		// (The width used)}
		if (__w <= 0 || this._width >= 1)
			throw new IllegalArgumentException("ZZ1o " + __w);
		
		this._width = __w;
	}
}

