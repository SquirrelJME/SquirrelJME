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
import java.util.ArrayList;
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
public final class NCIPool
	extends AbstractList<NCIPoolEntry>
{
	/** Internal entry list. */
	private final NCIPoolEntry[] _entries;
	
	/**
	 * This initializes the constant pool.
	 *
	 * @param __ents The entries in the constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCIPool(NCIPoolEntry... __ents)
		throws NullPointerException
	{
		this(Arrays.<NCIPoolEntry>asList(__ents));
	}
	
	/**
	 * This initializes the constant pool.
	 *
	 * @param __ents The entries in the constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCIPool(Iterable<NCIPoolEntry> __ents)
		throws NullPointerException
	{
		// Check
		if (__ents == null)
			throw new NullPointerException("NARG");
		
		// Copy all elements
		List<NCIPoolEntry> dest = new ArrayList<>();
		for (NCIPoolEntry e : __ents)
			dest.add(e);
		
		// Lock in
		_entries = dest.<NCIPoolEntry>toArray(new NCIPoolEntry[dest.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIPoolEntry get(int __i)
	{
		return _entries[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public int size()
	{
		return _entries.length;
	}
}

