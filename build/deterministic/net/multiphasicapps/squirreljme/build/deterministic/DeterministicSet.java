// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.deterministic;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This is a set which is deterministic.
 *
 * This class is not thread safe.
 *
 * Internally this class implements a set using {@link DeterministicMap}.
 *
 * @param <V> The value to store in the set.
 * @since 2016/10/28
 */
public class DeterministicSet<V extends Comparable<V>>
	extends AbstractSet<V>
{
	/** This is a flagging object for objects which are stored in the set. */
	private static final Object _STORED =
		new Object();
	
	/** The backing map. */
	private final Map<V, Object> _backing =
		new DeterministicMap<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public boolean add(V __v)
	{
		// If the put returns null then it would have been modified
		return null == this._backing.put(__v, _STORED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public Iterator<V> iterator()
	{
		return this._backing.keySet().iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public int size()
	{
		return this._backing.size();
	}
}

