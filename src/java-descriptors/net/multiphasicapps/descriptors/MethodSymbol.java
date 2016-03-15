// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.descriptors;

/**
 * This symbol describes the arguments and the return value which a method
 * consumes and provides.
 *
 * @since 2016/03/15
 */
public final class MethodSymbol
	extends MemberTypeSymbol
{
	/**
	 * Initializes the method symbol.
	 *
	 * @param __s The method descriptor.
	 * @throws IllegalSymbolException If this is not a valid method symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	public MethodSymbol(String __s)
		throws IllegalSymbolException, NullPointerException
	{
		super(__s);
		
		throw new Error("TODO");
	}
}

