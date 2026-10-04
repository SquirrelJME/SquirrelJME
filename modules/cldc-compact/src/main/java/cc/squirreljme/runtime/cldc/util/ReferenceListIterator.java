// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ListIterator;

/**
 * Wraps a {@link ListIterator} of references, will throw
 * {@link IllegalStateException} if any elements were garbage collected.
 *
 * @param <T> The type to use.
 * @since 2022/08/27
 */
public final class ReferenceListIterator<T>
	implements ListIterator<T>
{
	/** The iterator used. */
	protected final ListIterator<Reference<T>> iterator;
	
	/**
	 * Initializes the iterator wrapper.
	 * 
	 * @param __iterator The iterator.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/27
	 */
	public ReferenceListIterator(ListIterator<Reference<T>> __iterator)
		throws NullPointerException
	{
		if (__iterator == null)
			throw new NullPointerException("NARG");
		
		this.iterator = __iterator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void add(T __v)
	{
		ListIterator<Reference<T>> iterator = this.iterator;
		
		if (__v == null)
			iterator.add(null);
		else if (__v instanceof Reference)
			iterator.add((Reference<T>)__v);
		else
			iterator.add(new WeakReference<T>(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public boolean hasPrevious()
	{
		return this.iterator.hasPrevious();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public T next()
	{
		Reference<T> ref = this.iterator.next();
		
		if (ref == null)
			return null;
		
		T rv = ref.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public int nextIndex()
	{
		return this.iterator.nextIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public T previous()
	{
		Reference<T> ref = this.iterator.previous();
		
		if (ref == null)
			return null;
		
		T rv = ref.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public int previousIndex()
	{
		return this.iterator.previousIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public void remove()
	{
		this.iterator.remove();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void set(T __v)
	{
		ListIterator<Reference<T>> iterator = this.iterator;
		
		if (__v == null)
			iterator.set(null);
		else if (__v instanceof Reference)
			iterator.set((Reference<T>)__v);
		else
			iterator.set(new WeakReference<T>(__v));
	}
}
