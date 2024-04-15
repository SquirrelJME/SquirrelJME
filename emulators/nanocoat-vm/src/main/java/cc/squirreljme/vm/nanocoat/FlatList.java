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
import java.util.Arrays;
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
	
	/** Are elements to be cached? */
	protected final boolean cacheElements;
	
	/** The cached values. */
	private volatile E[] _cache;
	
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
	 * @param __cacheElements Should elements be cached where possible?
	 * @param __existing Existing elements.
	 * @throws IllegalArgumentException If the list and flat list size are
	 * incorrect.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/17
	 */
	public FlatList(AllocLink __link, boolean __cacheElements, E... __existing)
		throws IllegalArgumentException, NullPointerException
	{
		this(__link, __cacheElements,
			(__existing != null ? Arrays.asList(__existing) : null));
	}
	
	/**
	 * Initializes the flat list.
	 * 
	 * @param __link The link used.
	 * @param __cacheElements Should elements be cached where possible?
	 * @param __existing Existing elements.
	 * @throws IllegalArgumentException If the list and flat list size are
	 * incorrect.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/27
	 */
	@SuppressWarnings("unchecked")
	public FlatList(AllocLink __link, boolean __cacheElements,
		List<E> __existing)
		throws IllegalArgumentException, NullPointerException
	{
		this.link = __link;
		this.cacheElements = __cacheElements;
		
		// Pre-cache values?
		if (__existing != null)
		{
			// Check to make sure the size is the same
			int len = __existing.size();
			if (__existing.size() != len)
				throw new IllegalArgumentException("Mismatched size!");
			
			// Store into remade cache
			E[] cache = (E[])new Object[len];
			for (int i = 0; i < len; i++)
				cache[i] = __existing.get(i);
			
			// Make sure it sticks around
			this._cache = cache;
		}
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
	 * {@inheritDoc}/
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
		return new CharStarFlatList(AllocLink.ofBlockPtr(blockPtr));
	}
	
	/**
	 * Initializes the list from the given array.
	 *
	 * @param __inPool The pool to allocate within.
	 * @param __ints The integers to store in the list.
	 * @return The resultant flat list of integers.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the list could not be initialized.
	 * @since 2023/12/20
	 */
	public static IntegerFlatList fromArray(AllocPool __inPool, int... __ints)
		throws NullPointerException, VMException
	{
		if (__inPool == null || __ints == null)
			throw new NullPointerException("NARG");
		
		// Map natively
		long blockPtr = FlatList.__fromArrayI(
			__inPool.pointerAddress(), __ints);
		
		// Wrap as list
		return new IntegerFlatList(AllocLink.ofBlockPtr(blockPtr));
	}
	
	/**
	 * Initializes the list from the given array.
	 *
	 * @param <E> The type of values to store.
	 * @param __inPool The pool to allocate within.
	 * @param __elements The pointers to store in the list.
	 * @return The resultant flat list of pointers.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the list could not be initialized.
	 * @since 2023/12/27
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Pointer> PointerFlatList<E> fromArray(
		AllocPool __inPool, E... __elements)
		throws NullPointerException, VMException
	{
		if (__inPool == null || __elements == null)
			throw new NullPointerException("NARG");
		
		// Get pointers to everything
		int length = __elements.length;
		long[] pointers = new long[length];
		for (int i = 0; i < length; i++)
		{
			E element = __elements[i];
			
			if (element == null)
				pointers[i] = 0;
			else
				pointers[i] = element.pointerAddress();
		}
		
		// Map natively
		long blockPtr = FlatList.__fromArrayP(
			__inPool.pointerAddress(), pointers);
		
		// Wrap as list
		return new PointerFlatList<E>(AllocLink.ofBlockPtr(blockPtr),
			__elements);
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
	
	/**
	 * Creates a new list from the given array.
	 *
	 * @param __poolPtr The pointer to the pool to allocate within.
	 * @param __ints The integers to set from.
	 * @return The pointer to the created list.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/20
	 */
	private static native long __fromArrayI(long __poolPtr, int[] __ints)
		throws VMException;
	
	/**
	 * Creates a new list from the given array.
	 *
	 * @param __poolPtr The pointer to the pool to allocate within.
	 * @param __ptrs The pointers to set from.
	 * @return The pointer to the created list.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/27
	 */
	private static native long __fromArrayP(long __poolPtr, long[] __ptrs)
		throws VMException;
}
