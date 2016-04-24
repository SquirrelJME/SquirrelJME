// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class stores the constant pool.
 *
 * @since 2016/04/24
 */
public final class NCIConstantPool
	extends AbstractList<NCIConstantEntry>
{
	/**
	 * This initializes the constant pool.
	 *
	 * @param __ents The entries in the constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCIConstantPool(NCIConstantEntry... __ents)
		throws NullPointerException
	{
		this(Arrays.<NCIConstantEntry>asList(__ents);
	}
	
	/**
	 * This initializes the constant pool.
	 *
	 * @param __ents The entries in the constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCIConstantPool(Iterable<NCIConstantEntry> __ents)
		throws NullPointerException
	{
		// Check
		if (__ents == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIConstantEntry get(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
}

