// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.util.ShellSort;

public class Collections
{
	private Collections()
	{
		throw new todo.TODO();
	}
	
	@SuppressWarnings({"unchecked"})
	public static <T> boolean addAll(Collection<? super T> __a, T... __b)
	{
		throw new todo.TODO();
	}
	
	public static <T> int binarySearch(List<? extends Comparable<? super T>>
		__a, T __b)
	{
		throw new todo.TODO();
	}
	
	public static <T> int binarySearch(List<? extends T> __a, T __b,
		Comparator<? super T> __c)
	{
		throw new todo.TODO();
	}
	
	public static <T> void copy(List<? super T> __a, List<? extends T> __b)
	{
		throw new todo.TODO();
	}
	
	public static boolean disjoint(Collection<?> __a, Collection<?> __b)
	{
		throw new todo.TODO();
	}
	
	public static <T> Enumeration<T> enumeration(Collection<T> __a)
	{
		throw new todo.TODO();
	}
	
	public static <T> void fill(List<? super T> __a, T __b)
	{
		throw new todo.TODO();
	}
	
	public static int frequency(Collection<?> __a, Object __b)
	{
		throw new todo.TODO();
	}
	
	public static int indexOfSubList(List<?> __a, List<?> __b)
	{
		throw new todo.TODO();
	}
	
	public static int lastIndexOfSubList(List<?> __a, List<?> __b)
	{
		throw new todo.TODO();
	}
	
	public static <T> ArrayList<T> list(Enumeration<T> __a)
	{
		throw new todo.TODO();
	}
	
	public static <T extends Object & Comparable<? super T>> T max(Collection
		<? extends T> __a)
	{
		throw new todo.TODO();
	}
	
	public static <T> T max(Collection<? extends T> __a, Comparator<? super T
		> __b)
	{
		throw new todo.TODO();
	}
	
	public static <T extends Object & Comparable<? super T>> T min(Collection
		<? extends T> __a)
	{
		throw new todo.TODO();
	}
	
	public static <T> T min(Collection<? extends T> __a, Comparator<? super T
		> __b)
	{
		throw new todo.TODO();
	}
	
	public static <T> boolean replaceAll(List<T> __a, T __b, T __c)
	{
		throw new todo.TODO();
	}
	
	public static void reverse(List<?> __a)
	{
		throw new todo.TODO();
	}
	
	public static <T> Comparator<T> reverseOrder()
	{
		throw new todo.TODO();
	}
	
	public static <T> Comparator<T> reverseOrder(Comparator<T> __a)
	{
		throw new todo.TODO();
	}
	
	public static void rotate(List<?> __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public static void shuffle(List<?> __a)
	{
		throw new todo.TODO();
	}
	
	public static void shuffle(List<?> __a, Random __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sorts the specified list.
	 *
	 * @param <T> The type of values to sort.
	 * @param __a The list to sort.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/10
	 */
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
	public static <T> void sort(List<T> __a, Comparator<? super T> __comp)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		ShellSort.<T>sort(__a, 0, __a.size(), __comp);
	}
	
	public static void swap(List<?> __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
}

