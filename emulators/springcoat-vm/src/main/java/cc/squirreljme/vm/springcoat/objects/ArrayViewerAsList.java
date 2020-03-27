// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.objects;

import java.util.AbstractList;

/**
 * This is a view of an array as a list type.
 *
 * @since 2020/03/22
 */
public final class ArrayViewerAsList<T>
	extends AbstractList<T>
{
	/** The array to view. */
	protected final ArrayViewer<T> array;
	
	/**
	 * Initializes the array viewer as a list.
	 *
	 * @param __array The array to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/22
	 */
	public ArrayViewerAsList(ArrayViewer<T> __array)
		throws NullPointerException
	{
		if (__array == null)
			throw new NullPointerException("NARG");
		
		this.array = __array;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/22
	 */
	@Override
	public T get(int __i)
	{
		return this.array.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/22
	 */
	@Override
	public T set(int __i, T __v)
	{
		ArrayViewer<T> array = this.array;
		
		T old = array.get(__i);
		array.set(__i, __v);
		return old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/22
	 */
	@Override
	public int size()
	{
		return this.array.length();
	}
}
