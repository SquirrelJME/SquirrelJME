// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.RandomAccess;
import net.multiphasicapps.collections.UnmodifiableIterator;

/**
 * Represents multiple class names.
 *
 * @since 2019/04/14
 */
public final class ClassNames
	extends AbstractList<ClassName>
	implements RandomAccess
{
	/** Names. */
	private final ClassName[] _names;
	
	/** Hash code. */
	private int _hash;
	
	/** String. */
	private Reference<String> _string;
	
	/**
	 * Initializes the class names.
	 *
	 * @param __n The names.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public ClassNames(ClassName... __n)
		throws NullPointerException
	{
		for (ClassName n : (__n =
			(__n == null ? new ClassName[0] : __n.clone())))
			if (n == null)
				throw new NullPointerException("NARG");
			
		this._names = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = super.hashCode());
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final ClassName get(int __i)
	{
		return this._names[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final int size()
	{
		return this._names.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public ClassName[] toArray()
	{
		return this._names.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/14
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = super.toString()));
		
		return rv;
	}
}

