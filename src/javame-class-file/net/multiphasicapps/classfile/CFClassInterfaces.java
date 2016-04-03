// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This contains a mutable set of interfaces which a class implements.
 *
 * Classes cannot implement arrays.
 *
 * @since 2016/03/19
 */
public final class CFClassInterfaces
	extends AbstractSet<BinaryNameSymbol>
{
	/**
	 * Initializes the interface set.
	 *
	 * @param __bn The interfaces that the class uses.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	CFClassInterfaces(Set<BinaryNameSymbol> __bn)
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public Iterator<BinaryNameSymbol> iterator()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
}

