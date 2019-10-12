// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

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
}

