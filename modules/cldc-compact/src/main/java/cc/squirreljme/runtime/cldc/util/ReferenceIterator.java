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
import java.util.Iterator;

/**
 * Wraps a reference based iterator and exposes the elements underneath.
 * 
 * If the reference was garbage collected then {@link IllegalStateException}
 * will be thrown.
 *
 * @param <T> The element type.
 * @since 2022/08/27
 */
public final class ReferenceIterator<T>
	implements Iterator<T>
{
	/** The iterator used. */
	protected final Iterator<Reference<T>> iterator;
	
	/**
	 * Initializes the iterator wrapper.
	 * 
	 * @param __iterator The iterator.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/27
	 */
	public ReferenceIterator(Iterator<Reference<T>> __iterator)
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
	public void remove()
	{
		this.iterator.remove();
	}
}
