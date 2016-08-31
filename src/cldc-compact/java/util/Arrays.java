// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public class Arrays
{
	private Arrays()
	{
		super();
		throw new Error("TODO");
	}
	
	/**
	 * Wraps the specified array allowing access to its data as a fixed size
	 * list. The returned {@link List} will have {@link RandomAccess}
	 * implemented.
	 *
	 * @param <T> The type of values contained within the array.
	 * @param __a The array to wrap.
	 * @return The specified array wrapped in a {@link List}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/31
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> List<T> asList(T... __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Wrap it
		return new __ArraysList__<T>(__a);
	}
	
	public static int binarySearch(long[] __a, long __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(long[] __a, int __b, int __c, long __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(int[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(int[] __a, int __b, int __c, int __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(short[] __a, short __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(short[] __a, int __b, int __c, short __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(char[] __a, char __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(char[] __a, int __b, int __c, char __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(byte[] __a, byte __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(byte[] __a, int __b, int __c, byte __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(double[] __a, double __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(double[] __a, int __b, int __c, double __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(float[] __a, float __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(float[] __a, int __b, int __c, float __d)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(Object[] __a, Object __b)
	{
		throw new Error("TODO");
	}
	
	public static int binarySearch(Object[] __a, int __b, int __c, Object __d
		)
	{
		throw new Error("TODO");
	}
	
	public static <T> int binarySearch(T[] __a, T __b, Comparator<? super T>
		__c)
	{
		throw new Error("TODO");
	}
	
	public static <T> int binarySearch(T[] __a, int __b, int __c, T __d,
		Comparator<? super T> __e)
	{
		throw new Error("TODO");
	}
	
	public static <T> T[] copyOf(T[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static <T, U> T[] copyOf(U[] __a, int __b, Class<? extends T[]>
		__c)
	{
		throw new Error("TODO");
	}
	
	public static byte[] copyOf(byte[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static short[] copyOf(short[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static int[] copyOf(int[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static long[] copyOf(long[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static char[] copyOf(char[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static float[] copyOf(float[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static double[] copyOf(double[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean[] copyOf(boolean[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(long[] __a, long[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(int[] __a, int[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(short[] __a, short[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(char[] __a, char[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(byte[] __a, byte[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(boolean[] __a, boolean[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(double[] __a, double[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(float[] __a, float[] __b)
	{
		throw new Error("TODO");
	}
	
	public static boolean equals(Object[] __a, Object[] __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(long[] __a, long __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(int[] __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(short[] __a, short __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(char[] __a, char __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(byte[] __a, byte __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(boolean[] __a, boolean __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(double[] __a, double __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(float[] __a, float __b)
	{
		throw new Error("TODO");
	}
	
	public static void fill(Object[] __a, Object __b)
	{
		throw new Error("TODO");
	}
	
	public static void sort(int[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(int[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(long[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(long[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(short[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(short[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(char[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(char[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(byte[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(byte[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(float[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(float[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(double[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(double[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static void sort(Object[] __a)
	{
		throw new Error("TODO");
	}
	
	public static void sort(Object[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static <T> void sort(T[] __a, Comparator<? super T> __b)
	{
		throw new Error("TODO");
	}
	
	public static <T> void sort(T[] __a, int __b, int __c, Comparator<? super
		T> __d)
	{
		throw new Error("TODO");
	}
}

