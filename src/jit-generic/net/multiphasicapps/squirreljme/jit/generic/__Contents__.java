// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;

/**
 * This stores the table of contents temporarily for later output.
 *
 * @since 2016/07/29
 */
final class __Contents__
	extends AbstractList<__Contents__.__Entry__>
	implements RandomAccess
{
	/** Entry contents. */
	protected final List<__Entry__> entries =
		new ArrayList<>();
	
	/**
	 * Adds a single entry to the table of contents.
	 *
	 * @param __sp The start position.
	 * @param __ep The end position.
	 * @param __ct The type of content this is.
	 * @param __cn The name of the content entry.
	 * @throws JITException If the start and/or end position exceed 2GiB, or
	 * the end is before the start.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/29
	 */
	void __add(long __sp, long __ep, BlobContentType __ct, String __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__ct == null || __cn == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA0i Either the end position is before the start
		// position; the start position is negative; or the positions exceed
		// 2GiB. (The start position; The end position)}
		if (__ep < __sp || __sp > Integer.MAX_VALUE || __sp < 0)
			throw new JITException(String.format("BA0i %d %d", __sp, __ep));
		
		// Create entry
		this.entries.add(new __Entry__(__ct, __cn, (int)__sp, (int)__ep));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/29
	 */
	@Override
	public __Entry__ get(int __i)
	{
		return this.entries.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/29
	 */
	@Override
	public int size()
	{
		return this.entries.size();
	}
	
	/**
	 * Sorts all entries so that they are in order, this should be called
	 * last to ensure that worst case binary insertion does not happen every
	 * single time.
	 *
	 * @since 2016/07/29
	 */
	public void sortEntries()
	{
		Collections.<__Entry__>sort(this.entries);
	}
	
	/**
	 * A single content entry.
	 *
	 * @since 2016/07/29
	 */
	final class __Entry__
		implements Comparable<__Entry__>
	{
		/** The type of entry this is. */
		final BlobContentType _type;
		
		/** The name of this entry. */
		final String _name;
		
		/** The start position. */
		final int _startpos;
		
		/** The end position. */
		final int _endpos;
		
		/** The size. */
		final int _size;
		
		/**
		 * Initializes the entry information.
		 *
		 * @param __t The content type.
		 * @param __n The content name.
		 * @param __s The start position.
		 * @param __e The end position.
		 * @since 2016/07/29
		 */
		private __Entry__(BlobContentType __t, String __n, int __s, int __e)
		{
			// Check
			if (__t == null || __n == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._type = __t;
			this._name = __n;
			this._startpos = __s;
			this._endpos = __e;
			this._size = __e - __s;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/07/29
		 */
		@Override
		public int compareTo(__Entry__ __o)
			throws NullPointerException
		{
			// Check
			if (__o == null)
				throw new NullPointerException("NARG");
			
			// Compare
			return this._name.compareTo(__o._name);
		}
	}
}

