// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This is used to manage pointers within SpringCoat.
 *
 * @since 2019/12/21
 */
public final class SpringPointerManager
{
	/** The next allocation address. */
	private int _next =
		4;
	
	/**
	 * Allocates and returns a new pointer area.
	 *
	 * @param __l The length to allocate.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2019/12/21
	 */
	public final SpringPointerArea allocate(int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BK3g Cannot allocate negative pointer space.}
		if (__l < 0)
			throw new IllegalArgumentException("BK3g");
		
		throw new todo.TODO();
	}
}

