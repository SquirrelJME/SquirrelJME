// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This implements a read-only entry set.
 *
 * @since 2016/10/24
 */
final class __ReadOnlySet__<I extends ProjectInfo>
	extends AbstractSet<Map.Entry<ProjectName, I>>
{
	/** The entry set to access. */
	private final Set<Map.Entry<ProjectName, I>> _base;
		
	/**
	 * Initializes the read only set.
	 *
	 * @param __m The map to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/24
	 */
	__ReadOnlySet__(Map<ProjectName, I> __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Get base
		this._base = __m.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/24
	 */
	@Override
	public Iterator<Map.Entry<ProjectName, I>> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/24
	 */
	@Override
	public int size()
	{
		return this._base.size();
	}
	
	/**
	 * The map entry, wrapped to be read-only.
	 *
	 * @since 2016/10/24
	 */
	private final class __Entry__
		implements Map.Entry<ProjectName, I>
	{
		/** The key. */
		private final ProjectName _key;
		
		/** The value. */
		private final I _value;
		
		/**
		 * Initializes the entry.
		 *
		 * @param __e The entry to use.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/24
		 */
		private __Entry__(Map.Entry<ProjectName, I> __e)
			throws NullPointerException
		{
			// Check
			if (__e == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._key = __e.getKey();
			this._value = __e.getValue();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public boolean equals(Object __a)
		{
			// Must be another entry
			if (!(__a instanceof Map.Entry))
				return false;
			
			// Cast
			Map.Entry<?, ?> e = (Map.Entry<?, ?>)__a;
			return Objects.equals(this._key, e.getKey()) &&
				Objects.equals(this._value, e.getValue());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public ProjectName getKey()
		{
			return this._key;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public I getValue()
		{
			return this._value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public int hashCode()
		{
			return this._key.hashCode() ^ this._value.hashCode();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public I setValue(I __a)
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
	
	/**
	 * Iterator for the entry set.
	 *
	 * @since 2016/10/24
	 */
	private final class __Iterator__
		implements Iterator<Map.Entry<ProjectName, I>>
	{
		/** Iterator for the entry set. */
		private final Iterator<Map.Entry<ProjectName, I>> _iterator =
			__ReadOnlySet__.this._base.iterator();
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public boolean hasNext()
		{
			return this._iterator.hasNext();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public Map.Entry<ProjectName, I> next()
		{
			return new __Entry__(this._iterator.next());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/24
		 */
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

