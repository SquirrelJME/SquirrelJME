// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a set which allows enumeration values to be stored in it efficiently
 * using only a bit field.
 *
 * @since 2018/03/21
 */
public final class EnumSet<T extends Enum<T>>
	extends AbstractSet<T>
{
	/** The type of class being stored. */
	protected final Class<T> type;
	
	/** The number of used bits so far. */
	protected final int numbits;
	
	/** The number of ints in use. */
	protected final int numints;
	
	/** The values which are in the set, split into fields. */
	private int[] _bits;
	
	/** Is the null element in this set? */
	private boolean _hasnull;
	
	/**
	 * Initializes an empty enumeration set.
	 *
	 * @param __cl The class type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public EnumSet(Class<T> __cl)
		throws NullPointerException
	{
		this(__cl, EmptySet.<T>empty());
	}
	
	/**
	 * Initializes an enumeration set with the given initial values.
	 *
	 * @param __cl The class type to store.
	 * @param __v The initial values to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public EnumSet(Class<T> __cl, Collection<? extends T> __v)
		throws NullPointerException
	{
		if (__cl == null || __v == null)
			throw new NullPointerException("NARG");
		
		this.type = __cl;
		
		// Setup storage bits
		this.numbits = 0;
		this.numints = 0;
		this._bits = new int[0];
		
		// Add values
		for (T v : __v)
			this.add(v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean add(T __v)
	{
		// Adding null element?
		if (__v == null)
		{
			boolean hasnull = this._hasnull;
			if (hasnull)
				return false;
			else
			{
				this._hasnull = true;
				return true;
			}
		}
		
		// Need a place to store
		if (true)
			throw new todo.TODO();
		
		// Determine bit position
		int bit = __v.ordinal(),
			high = bit >>> 5;
		bit &= 0x1F;
		int flag = (1 << bit);
		
		// Did it have it before?
		int[] bits = this._bits;
		boolean had = ((bits[high] & flag) != 0);
		
		// Set it
		bits[high] |= flag;
		
		// The collection only changes if it did not have the bit
		return !had;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean contains(Object __o)
	{
		// Only has null if it was explicitely added
		if (__o == null)
			return this._hasnull;
		
		if (!this.type.isInstance(__o))
			return false;
		
		// Determine bit position
		int bit = ((Enum)__o).ordinal(),
			high = bit >>> 5;
		bit &= 0x1F;
		
		return 0 != (this._bits[high] & (1 << bit));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final Iterator<T> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final boolean remove(Object __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/21
	 */
	@Override
	public final int size()
	{
		throw new todo.TODO();
	}
}

