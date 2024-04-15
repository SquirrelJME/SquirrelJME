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
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * Wraps a reference based list iterator and exposes the elements underneath.
 * 
 * If the reference was garbage collected then {@link IllegalStateException}
 * will be thrown.
 *
 * @param <T> The list contents.
 * @since 2022/08/27
 */
public abstract class ReferenceList<T>
	extends AbstractList<T>
{
	/** The source list. */
	protected final List<Reference<T>> source;
	
	/**
	 * Initializes the list.
	 * 
	 * @param __list The list to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/27
	 */
	ReferenceList(List<Reference<T>> __list)
		throws NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		this.source = __list;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void add(int __index, T __item)
	{
		List<Reference<T>> source = this.source;
		
		if (__item == null)
			source.add(__index, null);
		else if (__item instanceof Reference)
			source.add(__index, (Reference<T>)__item);
		else
			source.add(__index, new WeakReference<T>(__item));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public void clear()
	{
		this.source.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public T get(int __i)
		throws IndexOutOfBoundsException
	{
		Reference<T> ref = this.source.get(__i);
		
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
	public Iterator<T> iterator()
	{
		return new ReferenceIterator<>(this.source.iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public ListIterator<T> listIterator()
	{
		return new ReferenceListIterator<>(this.source.listIterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public ListIterator<T> listIterator(int __i)
	{
		return new ReferenceListIterator<>(this.source.listIterator(__i));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public T remove(int __i)
	{
		Reference<T> item = this.source.remove(__i);
		
		if (item == null)
			return null;
		
		T rv = item.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T set(int __i, T __v)
	{
		List<Reference<T>> source = this.source;
		
		Reference<T> item;
		if (__v == null)
			item = source.set(__i, null);
		else if (__v instanceof Reference)
			item = source.set(__i, (Reference<T>)__v);
		else
			item = source.set(__i, new WeakReference<T>(__v));
			
		T rv = item.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/27
	 */
	@Override
	public int size()
	{
		return this.source.size();
	}
	
	/**
	 * Wraps the given list.
	 * 
	 * @param <T> The element type.
	 * @param __list The list to wrap.
	 * @return The wrapped list.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/27
	 */
	public static <T> ReferenceList<T> of(List<Reference<T>> __list)
		throws NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		// Is already this?
		if (__list instanceof ReferenceList)
			return (ReferenceList<T>)__list;
		
		if (__list instanceof RandomAccess)
			return new __RandomAccess__<T>(__list);
		return new __IteratorAccess__<T>(__list);
	}
	
	/**
	 * A list that is accessed via iterator.
	 * 
	 * @param <T> The element type.
	 * @since 2022/08/27
	 */
	private static final class __IteratorAccess__<T>
		extends ReferenceList<T>
	{
		/**
		 * Initializes the list.
		 * 
		 * @param __list The source list.
		 * @throws NullPointerException On null arguments.
		 * @since 2022/08/27
		 */
		__IteratorAccess__(List<Reference<T>> __list)
			throws NullPointerException
		{
			super(__list);
		}
	}
	
	/**
	 * A list that is also a random access list.
	 * 
	 * @param <T> The element type.
	 * @since 2022/08/27
	 */
	private static final class __RandomAccess__<T>
		extends ReferenceList<T>
		implements RandomAccess
	{
		/**
		 * Initializes the list.
		 * 
		 * @param __list The source list.
		 * @throws NullPointerException On null arguments.
		 * @since 2022/08/27
		 */
		__RandomAccess__(List<Reference<T>> __list)
			throws NullPointerException
		{
			super(__list);
		}
	}
}
