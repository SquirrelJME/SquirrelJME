// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This is the base class for stack and local verification type storage.
 *
 * @since 2016/08/28
 */
public abstract class ClassStackMapTread
	extends AbstractList<ClassStackMapType>
	implements RandomAccess
{
	/** The number of entries in the tread. */
	protected final int count;
	
	/** The variable storage area. */
	protected final ClassStackMapType[] storage;
	
	/**
	 * Initializes the base tread.
	 *
	 * @param __n The number of entries.
	 * @since 2016/05/12
	 */
	ClassStackMapTread(int __n)
	{
		// Initialize
		this.count = __n;
		this.storage = new ClassStackMapType[__n];
	}
	
	/**
	 * Initializes the basic tread information from another tread.
	 *
	 * @param __t The tread to base off.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	protected ClassStackMapTread(ClassStackMapTread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		int n = __t.count;
		this.count = n;
		
		// Copy storage
		ClassStackMapType[] from = __t.storage;
		ClassStackMapType[] storage = new ClassStackMapType[n];
		this.storage = storage;
		for (int i = 0; i < n; i++)
			storage[i] = from[i];
	}
	
	/**
	 * Copies state from another state.
	 *
	 * @param __o The state to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	public void from(ClassStackMapTread __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Copy all variables
		int n = this.count;
		for (int i = 0; i < n; i++)
			set(i, __o.get(i));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final ClassStackMapType get(int __i)
	{
		// Always make sure there is a value here
		ClassStackMapType rv = storage[__i];
		if (rv == null)
			return ClassStackMapType.NOTHING;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/29
	 */
	@Override
	public final ClassStackMapType set(int __i, ClassStackMapType __t)
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		ClassStackMapType[] storage = this.storage;
		ClassStackMapType rv = storage[__i];
		storage[__i] = __t;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final int size()
	{
		return count;
	}
}

