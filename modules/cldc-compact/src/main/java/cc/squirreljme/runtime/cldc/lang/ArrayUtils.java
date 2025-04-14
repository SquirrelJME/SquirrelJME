// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.IntegerArray;
import cc.squirreljme.runtime.cldc.util.IntegerIntegerArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This method contains the code which is able to initialize multi-dimensional
 * arrays and perform other array related tasks.
 *
 * @since 2018/11/03
 */
@SquirrelJMEVendorApi
public final class ArrayUtils
{
	/** Boolean array. */
	public static final byte ARRAY_BOOLEAN =
		1;
		
	/** The first array type. */
	public static final byte FIRST_TYPE =
		ArrayUtils.ARRAY_BOOLEAN;
	
	/** Byte array. */
	public static final byte ARRAY_BYTE =
		2;
	
	/** Short array. */
	public static final byte ARRAY_SHORT =
		3;
	
	/** Character array. */
	public static final byte ARRAY_CHARACTER =
		4;
	
	/** Integer array. */
	public static final byte ARRAY_INTEGER =
		5;
	
	/** Long array. */
	public static final byte ARRAY_LONG =
		6;
	
	/** Float array. */
	public static final byte ARRAY_FLOAT =
		7;
	
	/** Double array. */
	public static final byte ARRAY_DOUBLE =
		8;
	
	/** Object array. */
	public static final byte ARRAY_OBJECT =
		9;
	
	/** The number of array types. */
	public static final byte NUM_ARRAY_TYPES =
		10;
	
	/**
	 * Not used.
	 *
	 * @since 2018/11/03
	 */
	private ArrayUtils()
	{
	}
	
	/**
	 * Checks if both arrays are equal
	 * 
	 * @param __a The first array.
	 * @param __b The second array.
	 * @return If the arrays are equal.
	 * @since 2020/11/15
	 */
	public static boolean arrayEquals(Object __a, Object __b)
	{
		// Same exact array reference?
		if (__a == __b)
			return true;
		
		// Mismatch on nulls?
		if ((__a == null) != (__b == null))
			return false;
		
		// Check for object first
		if (__a instanceof Object[])
		{
			if (!(__b instanceof Object[]))
				return false;
			
			return Arrays.equals((Object[])__a, (Object[])__b);
		}
		
		// Must be the same type otherwise
		if (__a.getClass() != __b.getClass())
			return false;
		
		// Otherwise, this depends on the type
		if (__a instanceof boolean[])
			return Arrays.equals((boolean[])__a, (boolean[])__b);
		else if (__a instanceof byte[])
			return Arrays.equals((byte[])__a, (byte[])__b);
		else if (__a instanceof short[])
			return Arrays.equals((short[])__a, (short[])__b);
		else if (__a instanceof char[])
			return Arrays.equals((char[])__a, (char[])__b);
		else if (__a instanceof int[])
			return Arrays.equals((int[])__a, (int[])__b);
		else if (__a instanceof long[])
			return Arrays.equals((long[])__a, (long[])__b);
		else if (__a instanceof float[])
			return Arrays.equals((float[])__a, (float[])__b);
		else
			return Arrays.equals((double[])__a, (double[])__b);
	}
	
	/**
	 * Allocates a new array.
	 * 
	 * @param <T> The class to return as.
	 * @param __class The class to return as.
	 * @param __type The type of array to allocate.
	 * @param __len The length of the array.
	 * @return The type of array used.
	 * @throws ClassCastException If the given type is not correct for the
	 * given array.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws IndexOutOfBoundsException If the array length is negative.
	 * @since 2021/12/27
	 */
	public static <T> T arrayNew(Class<T> __class, int __type, int __len)
		throws ClassCastException, IllegalArgumentException,
			IndexOutOfBoundsException
	{
		switch (__type)
		{
			case ArrayUtils.ARRAY_BOOLEAN:
				return __class.cast(new boolean[__len]);
				
			case ArrayUtils.ARRAY_BYTE:
				return __class.cast(new byte[__len]);
				
			case ArrayUtils.ARRAY_SHORT:
				return __class.cast(new short[__len]);
			
			case ArrayUtils.ARRAY_CHARACTER:
				return __class.cast(new char[__len]);
				
			case ArrayUtils.ARRAY_INTEGER:
				return __class.cast(new int[__len]);
				
			case ArrayUtils.ARRAY_LONG:
				return __class.cast(new long[__len]);
				
			case ArrayUtils.ARRAY_FLOAT:
				return __class.cast(new float[__len]);
				
			case ArrayUtils.ARRAY_DOUBLE:
				return __class.cast(new double[__len]);
				
			case ArrayUtils.ARRAY_OBJECT:
				return __class.cast(Arrays.copyOf(new Object[0], __len,
					(Class<? extends Object[]>)((Object)__class)));
			
				/* {@squirreljme.error ZZ5f Invalid array type.} */
			default:
				throw new IllegalArgumentException("ZZ5f");
		}
	}
	
	/**
	 * Reads from an array.
	 * 
	 * @param <T> The resultant boxed type.
	 * @param __cast The type to cast to.
	 * @param __type The array type.
	 * @param __a The array.
	 * @param __dx The index to read.
	 * @return The read value.
	 * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
	 * @throws ClassCastException If the wrong class is used.
	 * @throws IllegalArgumentException If the argument is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/01/05
	 */
	public static <T> T arrayGet(Class<T> __cast,
		int __type, Object __a, int __dx)
		throws ArrayIndexOutOfBoundsException, ClassCastException,
			IllegalArgumentException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		switch (__type)
		{
			case ArrayUtils.ARRAY_BOOLEAN:
				return __cast.cast(((boolean[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_BYTE:
				return __cast.cast(((byte[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_SHORT:
				return __cast.cast(((short[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_CHARACTER:
				return __cast.cast(((char[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_INTEGER:
				return __cast.cast(((int[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_LONG:
				return __cast.cast(((long[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_FLOAT:
				return __cast.cast(((float[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_DOUBLE:
				return __cast.cast(((double[])__a)[__dx]);
				
			case ArrayUtils.ARRAY_OBJECT:
				return __cast.cast(((Object[])__a)[__dx]);
			
				/* {@squirreljme.error ZZf7 Invalid array type.} */
			default:
				throw new IllegalArgumentException("ZZf7");
		}
	}
	
	/**
	 * Sets the value in the array.
	 *
	 * @param __a The array.
	 * @param __dx The index to set.
	 * @param __v The value to store.
	 * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
	 * @throws ClassCastException If the wrong class is used.
	 * @throws IllegalArgumentException If the argument is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/13
	 */
	public static void arraySet(Object __a, int __dx, Object __v)
		throws ArrayIndexOutOfBoundsException, ClassCastException,
			IllegalArgumentException, NullPointerException
	{
		ArrayUtils.arraySet(ArrayUtils.arrayType(__a), __a, __dx, __v);
	}
	
	/**
	 * Sets the value in the array.
	 *
	 * @param __type The type of array used.
	 * @param __a The array.
	 * @param __dx The index to set.
	 * @param __v The value to store.
	 * @throws ArrayIndexOutOfBoundsException If the index is out of bounds.
	 * @throws ClassCastException If the wrong class is used.
	 * @throws IllegalArgumentException If the argument is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/13
	 */
	public static void arraySet(int __type, Object __a, int __dx,
		Object __v)
		throws ArrayIndexOutOfBoundsException, ClassCastException,
			IllegalArgumentException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		switch (__type)
		{
			case ArrayUtils.ARRAY_BOOLEAN:
				((boolean[])__a)[__dx] = (Boolean)__v;
				break;
				
			case ArrayUtils.ARRAY_BYTE:
				((byte[])__a)[__dx] = ((Number)__v).byteValue();
				break;
				
			case ArrayUtils.ARRAY_SHORT:
				((short[])__a)[__dx] = ((Number)__v).shortValue();
				break;
				
			case ArrayUtils.ARRAY_CHARACTER:
				((char[])__a)[__dx] = (Character)__v;
				break;
				
			case ArrayUtils.ARRAY_INTEGER:
				((int[])__a)[__dx] = ((Number)__v).intValue();
				break;
				
			case ArrayUtils.ARRAY_LONG:
				((long[])__a)[__dx] = ((Number)__v).longValue();
				break;
				
			case ArrayUtils.ARRAY_FLOAT:
				((float[])__a)[__dx] = ((Number)__v).floatValue();
				break;
				
			case ArrayUtils.ARRAY_DOUBLE:
				((double[])__a)[__dx] = ((Number)__v).doubleValue();
				break;
				
			case ArrayUtils.ARRAY_OBJECT:
				((Object[])__a)[__dx] = __v;
				break;
			
				/* {@squirreljme.error ZZ0c Invalid array type.} */
			default:
				throw new IllegalArgumentException("ZZ0c");
		}
	}
	
	/**
	 * Returns the type of array the object is.
	 *
	 * @param __a The array type.
	 * @return The type of array this is.
	 * @throws IllegalArgumentException If not an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/13
	 */
	public static int arrayType(Object __a)
		throws IllegalArgumentException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		if (__a instanceof Object[])
			return ArrayUtils.ARRAY_OBJECT;
		else if (__a instanceof boolean[])
			return ArrayUtils.ARRAY_BOOLEAN;
		else if (__a instanceof byte[])
			return ArrayUtils.ARRAY_BYTE;
		else if (__a instanceof short[])
			return ArrayUtils.ARRAY_SHORT;
		else if (__a instanceof char[])
			return ArrayUtils.ARRAY_CHARACTER;
		else if (__a instanceof int[])
			return ArrayUtils.ARRAY_INTEGER;
		else if (__a instanceof long[])
			return ArrayUtils.ARRAY_LONG;
		else if (__a instanceof float[])
			return ArrayUtils.ARRAY_FLOAT;
		else if (__a instanceof double[])
			return ArrayUtils.ARRAY_DOUBLE;
		
		/* {@squirreljme.error ZZ0d Invalid array type. (The type)} */
		throw new IllegalArgumentException("ZZ0d " + __a.getClass().getName());
	}
	
	/**
	 * Flattens all the specified arrays into a new array. 
	 *
	 * @param __arrays The arrays to flatten.
	 * @return The flattened arrays.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public static int[] flatten(IntegerArray... __arrays)
		throws NullPointerException
	{
		if (__arrays == null)
			throw new NullPointerException("NARG");
		
		return ArrayUtils.flatten(Arrays.asList(__arrays));
	}
	
	/**
	 * Flattens all the specified arrays into a new array. 
	 *
	 * @param __arrays The arrays to flatten.
	 * @return The flattened arrays.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public static int[] flatten(List<IntegerArray> __arrays)
		throws NullPointerException
	{
		if (__arrays == null)
			throw new NullPointerException("NARG");
	
		// Determine the total size of the arrays
		int total = 0;
		int lastTotal;
		for (IntegerArray array : __arrays)
		{
			lastTotal = total;
			total += array.size();
			
			// Hopefully does not occur
			if (total < 0 || total < lastTotal)
				throw new IllegalStateException("OOPS");
		}
		
		// Create a giant array as the result, then copy into each one
		int[] result = new int[total];
		for (int i = 0, n = __arrays.size(), off = 0; i < n; i++)
		{
			// Get source array to copy from
			IntegerArray source = __arrays.get(i);
			int sourceLen = source.size();
			
			// Is there a faster more direct copy?
			if (source instanceof IntegerIntegerArray)
			{
				// Use direct copy
				((IntegerIntegerArray)source).copyFrom(0,
					result, off, sourceLen);
			
				// Move output up in a single chunk
				off += sourceLen;
			}
				
			// Otherwise, slow copy
			else
			{
				for (int j = 0; j < sourceLen; j++)
					result[off++] = source.get(j);
			}
		}
		
		// Return resultant array
		return result;
	}
	
	/**
	 * Flattens all the specified arrays into a new array. 
	 *
	 * @param __arrays The arrays to flatten.
	 * @return The flattened arrays.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public static int[] flattenPrimitive(int[]... __arrays)
		throws NullPointerException
	{
		if (__arrays == null)
			throw new NullPointerException("NARG");
		
		// Simpler operations?
		int n = __arrays.length;
		if (n == 0)
			return null;
		else if (n == 1)
			return __arrays[0].clone();
		
		// Wrap all the arrays accordingly
		List<IntegerArray> wrapped = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			wrapped.add(i, new IntegerIntegerArray(__arrays[i]));
		
		return ArrayUtils.flatten(wrapped);
	}
	
	/**
	 * Flattens all the specified arrays into a new array. 
	 *
	 * @param __arrays The arrays to flatten.
	 * @return The flattened arrays.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public static int[] flattenPrimitive(List<int[]> __arrays)
		throws NullPointerException
	{
		if (__arrays == null)
			throw new NullPointerException("NARG");
		
		// Simpler operations?
		int n = __arrays.size();
		if (n == 0)
			return null;
		else if (n == 1)
			return __arrays.get(0).clone();
		
		// Wrap arrays
		List<IntegerArray> wrapped = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			wrapped.add(i, new IntegerIntegerArray(__arrays.get(i)));
		
		return ArrayUtils.flatten(wrapped);
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @param __e Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d, int __e)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d, __e});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @param __e Length of dimension.
	 * @param __f Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d, int __e, int __f)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d, __e, __f});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @param __e Length of dimension.
	 * @param __f Length of dimension.
	 * @param __g Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d, __e, __f, __g});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @param __e Length of dimension.
	 * @param __f Length of dimension.
	 * @param __g Length of dimension.
	 * @param __h Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g,
		int __h)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d, __e, __f, __g, __h});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @param __e Length of dimension.
	 * @param __f Length of dimension.
	 * @param __g Length of dimension.
	 * @param __h Length of dimension.
	 * @param __i Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g,
		int __h, int __i)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d, __e, __f, __g, __h, __i});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __a Length of dimension.
	 * @param __b Length of dimension.
	 * @param __c Length of dimension.
	 * @param __d Length of dimension.
	 * @param __e Length of dimension.
	 * @param __f Length of dimension.
	 * @param __g Length of dimension.
	 * @param __h Length of dimension.
	 * @param __i Length of dimension.
	 * @param __j Length of dimension.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/04
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g,
		int __h, int __i, int __j)
		throws NegativeArraySizeException, NullPointerException
	{
		return ArrayUtils.multiANewArray(__type, __skip,
			new int[]{__a, __b, __c, __d, __e, __f, __g, __h, __i, __j});
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __dims The dimensions and the number of them to use.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/03
	 */
	@SquirrelJMEVendorApi
	public static Object multiANewArray(Class<?> __type, int __skip,
		int[] __dims)
		throws NegativeArraySizeException, NullPointerException
	{
		if (__type == null || __dims == null)
			throw new NullPointerException("NARG");
		
		// Count the number of dimensions represented in the type
		String typename = __type.getName();
		int typeDims = 0;
		while (typename.charAt(typeDims) == '[')
			typeDims++;
		
		/* {@squirreljme.error ZZ0e Negative number of dimensions available
		or input type is not correct for the array type.} */
		int dims = __dims.length - __skip;
		if (__skip < 0 || dims <= 0 || typeDims < dims)
			throw new IllegalArgumentException("ZZ0e");
		
		// Allocate array of this type
		int numElem = __dims[__skip];
		Object rv = ObjectShelf.arrayNew(
			TypeShelf.classToType(__type), numElem);
		
		// Need to determine the type for setting
		int type = ArrayUtils.arrayType(rv);
		
		// The array has more dimensions which must be set
		if (dims > 1)
		{
			// Remove a single brace from the class to cut down its dimensions
			// Class names use dot forms, so refer to classes using the class
			// handler.
			Class<?> subtype;
			try
			{
				subtype = Class.forName(typename.substring(1));
			}
			
			/* {@squirreljme.error ZZ0f Could not find the sub-type for
			multi-dimensional array.} */
			catch (ClassNotFoundException e)
			{
				throw new Error("ZZ0f", e);
			}
			
			// Skipping ahead by one
			int nextSkip = __skip + 1;
			
			// Allocate
			for (int i = 0; i < numElem; i++)
				ArrayUtils.arraySet(type, rv, i,
					ArrayUtils.multiANewArray(subtype, nextSkip, __dims));
		}
		
		return rv;
	}
}

