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
 * This utility exists for the parsing of SquirrelJME's class files and allows
 * the bootstrap and class loaders the ability to read them.
 *
 * @since 2019/10/06
 */
public final class ClassFileParser
{
	/** The address where the class is. */
	public final int romaddress;
	
	/**
	 * Initializes the class file parser.
	 *
	 * @param __ra The ROM address.
	 * @since 2019/10/06
	 */
	public ClassFileParser(int __ra)
	{
		this.romaddress = __ra;
	}
}

