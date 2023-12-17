// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.AbstractList;

/**
 * Represents a flat native list, base class as there can be sub-forms of this
 * depending on the data that is represented.
 *
 * @param <E> The type of element that is contained.
 * @since 2023/12/17
 */
public abstract class FlatList<E>
	extends AbstractList<E>
	implements Pointer
{
	/** The link that contains the list. */
	protected final AllocLink link;
	
	/** Cached list length. */
	private volatile int _length =
		-1;
	
	/** Cached element size. */
	private volatile int _elementSize =
		-1;
	
	/**
	 * Initializes the flat list.
	 * 
	 * @param __link The link used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/17
	 */
	public FlatList(AllocLink __link)
		throws NullPointerException
	{
		this.link = __link;
	}
	
	/**
	 * Returns the size of each element in the list.
	 *
	 * @return The element size.
	 * @since 2023/12/17
	 */
	public int elementSize()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/17
	 */
	@Override
	public E get(int __dx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/17
	 */
	@Override
	public long pointerAddress()
	{
		return this.link.pointerAddress();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/17
	 */
	@Override
	public E set(int __dx, E __v)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/17
	 */
	@Override
	public int size()
	{
		throw Debugging.todo();
	}
}
