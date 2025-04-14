// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.NaturalComparator;
import cc.squirreljme.runtime.cldc.util.ShellSort;

@SuppressWarnings("ClassWithOnlyPrivateConstructors")
@Api
public class Collections
{
	private Collections()
	{
	}
	
	/**
	 * Does the same as {@link Collection#addAll(Collection)} with
	 * {@code __target.addAll(Arrays.asList(__v))}.
	 * 
	 * @param <T> The type to add.
	 * @param __target The target collection.
	 * @param __v The values to add.
	 * @return If the collection was modified.
	 * @throws IllegalArgumentException If a value of {@code __v} cannot be
	 * added to the collection.
	 * @throws NullPointerException On null arguments or the collection does
	 * not allow null arguments.
	 * @throws UnsupportedOperationException If adding elements is not
	 * supported.
	 * @since 2021/01/03
	 */
	@Api
	@SuppressWarnings({"unchecked"})
	public static <T> boolean addAll(Collection<? super T> __target, T... __v)
		throws IllegalArgumentException, NullPointerException,
			UnsupportedOperationException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		return __target.addAll(Arrays.asList(__v));
	}
	
	/**
	 * Performs a binary search of the given list to find the position in
	 * the list where {@code __key} is located or where it would be located
	 * 
	 * @param <T> The type of value to find.
	 * @param __list The list to search.
	 * @param __key The key to search for.
	 * @return The position of the item or {@code (-(insertion point) - 1)}
	 * where it would be found.
	 * @throws NullPointerException If there is no list specified.
	 * @since 2022/01/28
	 */
	@Api
	public static <T> int binarySearch(
		List<? extends Comparable<? super T>> __list, T __key)
		throws NullPointerException
	{
		return Collections.binarySearch(__list, __key, null);
	}
	
	/**
	 * Performs a binary search of the given list to find the position in
	 * the list where {@code __key} is located or where it would be located
	 * 
	 * @param <T> The type of value to find.
	 * @param __list The list to search.
	 * @param __key The key to search for.
	 * @param __compare The {@link Comparator} to use for sorting, if
	 * {@code null} then this will use the natural {@link Comparator}.
	 * @return The position of the item or {@code (-(insertion point) - 1)}
	 * where it would be found.
	 * @throws ClassCastException If the comparator is of the wrong type.
	 * @throws NullPointerException If there is no list specified.
	 * @since 2022/01/28
	 */
	@Api
	public static <T> int binarySearch(List<? extends T> __list, T __key,
		Comparator<? super T> __compare)
		throws NullPointerException, ClassCastException
	{
		// Check
		if (__list == null)
			throw new NullPointerException("NARG");
		
		int len = __list.size();
		int __from = 0;
		int __to = len;
		
		// If missing, get a comparator instance.
		if (__compare == null)
			__compare = NaturalComparator.<T>instance();
		
		// Empty list, will always be at the 0th index
		if (len == 0)
			return -1;
		
		// List has a single element, so only check that
		else if (len == 1)
		{
			T pv = __list.get(__from);
			
			// Is same
			if (__compare.compare(pv,__key) == 0)
				return __from;
			
			// Value is either the 0th or 1st element
			if (__compare.compare(pv, __key) < 0)
				return -1;
			else
				return -2;
		}
		
		// Use the same index
		__to -= 1;
		
		// Search for element at the pivot first, stop if the from and to are
		// at the same points
		while (__from <= __to)
		{
			// Calculate the pivot and use its value
			int p = __from + (((__to - __from) + 1) >> 1);
			T pv = __list.get(p);
			
			// Left of pivot?
			if (__compare.compare(__key, pv) < 0)
				__to = p - 1;
			
			// Right of pivot?
			else if (__compare.compare(__key, pv) > 0)
				__from = p + 1;
			
			// Match
			else
				return p;
		}
		
		return (-__from) - 1;
	}
	
	@Api
	public static <T> void copy(List<? super T> __a, List<? extends T> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static boolean disjoint(Collection<?> __a, Collection<?> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T> Enumeration<T> enumeration(Collection<T> __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T> void fill(List<? super T> __a, T __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int frequency(Collection<?> __a, Object __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int indexOfSubList(List<?> __a, List<?> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int lastIndexOfSubList(List<?> __a, List<?> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T> ArrayList<T> list(Enumeration<T> __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T extends Object & Comparable<? super T>> T max(Collection
		<? extends T> __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T> T max(Collection<? extends T> __a, Comparator<? super T
		> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T extends Object & Comparable<? super T>> T min(Collection
		<? extends T> __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T> T min(Collection<? extends T> __a, Comparator<? super T
		> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <T> boolean replaceAll(List<T> __a, T __b, T __c)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Reverses the specified list.
	 * 
	 * @param __list The list to reverse.
	 * @throws UnsupportedOperationException If the
	 * {@link List#set(int, Object)} method or {@link ListIterator#set(Object)}
	 * is not supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/29
	 */
	@Api
	@SuppressWarnings({"unchecked"})
	public static void reverse(List<?> __list)
		throws UnsupportedOperationException, NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		// If the list is empty or only has a single element then there is
		// no point in reversing the list
		int size = __list.size();
		if (size <= 1)
			return;
		
		// Setup blank queue that will be used as a double bounce back when
		// reversing elements
		Deque<Object> elements = new ArrayDeque<>(size);
		
		// Use the same iterator for going forwards and backwards 
		ListIterator<Object> it = (ListIterator<Object>)__list.listIterator();
		
		// Go forward and collect all items into the queue
		while (it.hasNext())
			elements.addLast(it.next());
		
		// Go back and drain the queue that was filled up
		while (it.hasPrevious())
		{
			it.previous();
			it.set(elements.removeFirst());
		}
	}
	
	/**
	 * Returns a comparator that is a reverse of the natural comparator
	 * order.
	 * 
	 * @param <T> The type to compare.
	 * @return The comparator for reverse order.
	 * @see NaturalComparator
	 * @since 2022/07/29
	 */
	@Api
	public static <T> Comparator<T> reverseOrder()
	{
		return Collections.reverseOrder(null);
	}
	
	/**
	 * Returns a comparator that reverses the given comparator.
	 * 
	 * @param <T> The type to compare.
	 * @param __comp The comparator to be reversed, if {@code null} then
	 * the natural order comparator is used the same as
	 * {@link Collections#reverseOrder()}.
	 * @return A comparator that reverses the 
	 */
	@Api
	public static <T> Comparator<T> reverseOrder(Comparator<T> __comp)
	{
		// If this is a reversal of a reversal, then undo that
		if (__comp instanceof __ReverseComparator__)
			return ((__ReverseComparator__<T>)__comp)._comparator;
		
		return new __ReverseComparator__<T>((__comp == null ?
			NaturalComparator.<T>instance() : __comp));
	}
	
	@Api
	public static void rotate(List<?> __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void shuffle(List<?> __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void shuffle(List<?> __a, Random __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sorts the specified list.
	 *
	 * @param <T> The type of values to sort.
	 * @param __a The list to sort.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/10
	 */
	@Api
	public static <T extends Comparable<? super T>> void sort(List<T> __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		ShellSort.<T>sort(__a, 0, __a.size(), null);
	}
	
	/**
	 * Sorts the specified list.
	 *
	 * @param <T> The type of values to sort.
	 * @param __a The list to sort.
	 * @param __comp The comparator to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/10
	 */
	@Api
	public static <T> void sort(List<T> __a, Comparator<? super T> __comp)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		ShellSort.<T>sort(__a, 0, __a.size(), __comp);
	}
	
	@Api
	public static void swap(List<?> __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
}

