// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.Closeable;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a list which is also {@link Closeable} so that multiple directories
 * may be opened and closed accordingly.
 *
 * @since 2016/12/21
 */
class __CloseableList__<V extends Closeable>
	extends AbstractList<V>
	implements Closeable
{
	/** The backing list. */
	protected final List<V> store =
		new ArrayList<>();
	
	/**
	 * Constructs the closeable list.
	 *
	 * @since 2016/12/21
	 */
	__CloseableList__()
	{
	}
	
	/**
	 * Initializes the list with values copied from the given iterable.
	 *
	 * @param __from The source iteration to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/21
	 */
	__CloseableList__(Iterable<V> __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Copy values
		List<V> store = this.store;
		for (V v : __from)
			store.add(v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public void add(int __i, V __v)
	{
		this.store.add(__i, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close all items
		IOException defer = null;
		List<V> store = this.store;
		for (V v : store)
			try
			{
				if (v != null)
					v.close();
			}
			
			// Defer it for later
			catch (IOException e)
			{
				if (defer != null)
					defer = e;
			}
		
		// Deferred?
		if (defer != null)
			throw defer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public V get(int __i)
	{
		return this.store.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public V remove(int __i)
	{
		return this.store.remove(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public int size()
	{
		return this.store.size();
	}
}

