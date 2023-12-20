// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.AbstractList;
import java.util.List;

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
	
	/**
	 * Flattens the given set of strings.
	 *
	 * @param __inPool The pool to allocate within.
	 * @param __strings The string to flatten.
	 * @return The resultant flat list.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the strings could not be flattened.
	 * @since 2023/12/17
	 */
	public static FlatList<CharStarPointer> flatten(AllocPool __inPool,
		List<String> __strings)
		throws NullPointerException, VMException
	{
		if (__inPool == null || __strings == null)
			throw new NullPointerException("NARG");
		
		return FlatList.flatten(__inPool,
			__strings.toArray(new String[__strings.size()]));
	}
	
	/**
	 * Flattens the given set of strings.
	 *
	 * @param __inPool The pool to allocate within.
	 * @param __strings The string to flatten.
	 * @return The resultant flat list.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the strings could not be flattened.
	 * @since 2023/12/17
	 */
	public static FlatList<CharStarPointer> flatten(AllocPool __inPool,
		String... __strings)
		throws NullPointerException, VMException
	{
		if (__strings == null)
			throw new NullPointerException("NARG");
		
		// Flatten natively
		long blockPtr = FlatList.__flatten(
			__inPool.pointerAddress(), __strings);
		
		// Wrap as list
		return new CharStarFlatList(
			new AllocLink(blockPtr, AllocPool.__getLink(blockPtr)));
	}
	
	/**
	 * Flattens the given array of strings.
	 *
	 * @param __poolPtr The pointer to the allocation pool.
	 * @param __strings The strings to flatten.
	 * @return The block pointer of the resultant list.
	 * @throws VMException If it could not be flattened.
	 * @since 2023/12/17
	 */
	private static native long __flatten(long __poolPtr, String[] __strings)
		throws VMException;
}
