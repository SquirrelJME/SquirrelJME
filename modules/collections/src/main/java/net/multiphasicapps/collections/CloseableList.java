// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is based on top of {@link ArrayList} and is the same interface except
 * that it is {@link Closeable} and on close will attempt to close all
 * elements within the list.
 *
 * @since 2017/11/28
 */
public class CloseableList<T extends Closeable>
	extends ArrayList<T>
	implements Closeable
{
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void close()
		throws IOException
	{
		int count = 0;
		IOException defer = null;
		for (int i = 0, n = this.size(); i < n; i++)
			try
			{
				T v = this.get(i);
				if (v != null)
					v.close();
			}
			catch (IOException e)
			{
				if (defer == null)
					defer = e;
				count++;
			}
		
		/* {@squirreljme.error AC01 There was at least one exception which
		occurred while closing the list. (The number of exceptions thrown)} */
		if (defer != null)
			throw new IOException(String.format("AC01 %d", count), defer);
	}
	
	/**
	 * Adds the specified item to the list and returns it.
	 *
	 * @param __t The element to add.
	 * @return {@code __t}.
	 * @since 2017/11/28
	 */
	public final T addThis(T __t)
	{
		this.add(__t);
		return __t;
	}
	
	/**
	 * Adds the specified item to the list and returns it.
	 *
	 * @param <E> The class to cast to.
	 * @param __t The element to add.
	 * @param __cl The class to cast to on return.
	 * @return {@code __t} cast to {@code E}.
	 * @throws NullPointerException If no class was specified.
	 * @since 2017/11/28
	 */
	public final <E extends T> E addThis(E __t, Class<E> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.add(__t);
		return __cl.cast(__t);
	}
}

