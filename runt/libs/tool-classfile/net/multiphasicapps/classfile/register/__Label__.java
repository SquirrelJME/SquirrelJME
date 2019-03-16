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

/**
 * Label which refers to a location in code.
 *
 * @since 2019/03/16
 */
final class __Label__
{
	/** The locality. */
	public final String locality;
	
	/** The associated address. */
	public final int address;
	
	/**
	 * Initializes the lable.
	 *
	 * @param __l The locality.
	 * @param __a The address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/16
	 */
	public __Label__(String __l, int __a)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.locality = __l;
		this.address = __a;
	}
}

