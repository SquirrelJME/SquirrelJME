// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.lang.ArrayUtils;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests the {@code arrayFill} methods in {@link ObjectShelf}.
 *
 * @since 2021/12/27
 */
public class TestArrayFill
	extends TestConsumer<String>
{
	/** The length of the test array. */
	private static final int _LENGTH =
		16;
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/27
	 */
	@Override
	public void test(String __s)
		throws Throwable
	{
		int type = TestArrayFill.ofType(__s);
		
		// Allocate array
		Object array = ArrayUtils.arrayNew(Object.class, type,
			TestArrayFill._LENGTH);
		
		// Null argument
		try
		{
			TestArrayFill.fill(type,
				null, 0, TestArrayFill._LENGTH, 7);
		}
		catch (MLECallError ignored)
		{
			this.secondary("nullarg", true);
		}
		
		// Negative offset
		try
		{
			TestArrayFill.fill(type,
				array, -1, TestArrayFill._LENGTH, 7);
		}
		catch (MLECallError ignored)
		{
			this.secondary("oobnegoff", true);
		}
		
		// Negative length
		try
		{
			TestArrayFill.fill(type,
				array, 0, -1, 7);
		}
		catch (MLECallError ignored)
		{
			this.secondary("oobneglen", true);
		}
		
		// OOB on right side
		try
		{
			TestArrayFill.fill(type,
				array, 5, TestArrayFill._LENGTH, 7);
		}
		catch (MLECallError ignored)
		{
			this.secondary("oobonright", true);
		}
		
		// Do entire fill
		TestArrayFill.fill(type,
			array, 0, TestArrayFill._LENGTH, 1);
		int fillCount = 0;
		for (int i = 0; i < TestArrayFill._LENGTH; i++)
			fillCount += TestArrayFill.get(type, array, i).intValue();
		this.secondary("wholefill", fillCount);
		
		// Do partial fill
		TestArrayFill.fill(type,
			array, 2, TestArrayFill._LENGTH - 4, 0);
		int partCount = 0;
		for (int i = 0; i < TestArrayFill._LENGTH; i++)
			partCount += TestArrayFill.get(type, array, i).intValue();
		this.secondary("partfill", partCount);
	}
	
	/**
	 * Reads the given value.
	 * 
	 * @param __type The type to read as.
	 * @param __array The array to read from.
	 * @param __dx The index to read.
	 * @return The read value.
	 * @since 2021/01/05
	 */
	private static Number get(int __type, Object __array, int __dx)
	{
		Object rv = ArrayUtils.arrayGet(Object.class, __type, __array, __dx);
		
		if (rv instanceof Boolean)
			return (((Boolean)rv) ? 1 : 0);
		else if (rv instanceof Character)
			return (int)((Character)rv);
		return (Number)rv;
	}
	
	/**
	 * Fills the given array.
	 * 
	 * @param __type The type of array to fill.
	 * @param __a The array to fill.
	 * @param __o The offset into the array.
	 * @param __l The length to fill.
	 * @param __val The value to set.
	 * @since 2021/01/05
	 */
	public static void fill(int __type, Object __a,
		int __o, int __l, Object __val)
	{
		switch (__type)
		{
			case ArrayUtils.ARRAY_BOOLEAN:
				ObjectShelf.arrayFill((boolean[])__a, __o, __l,
					((__val instanceof Boolean) ? ((Boolean)__val) :
						(((Number)__val).intValue() != 0)));
				return;
				
			case ArrayUtils.ARRAY_BYTE:
				ObjectShelf.arrayFill((byte[])__a, __o, __l,
					((Number)__val).byteValue());
				return;
				
			case ArrayUtils.ARRAY_SHORT:
				ObjectShelf.arrayFill((short[])__a, __o, __l,
					((Number)__val).shortValue());
				return;
				
			case ArrayUtils.ARRAY_CHARACTER:
				ObjectShelf.arrayFill((char[])__a, __o, __l,
					((__val instanceof Character) ? ((Character)__val) :
						((char)((Number)__val).intValue())));
				return;
				
			case ArrayUtils.ARRAY_INTEGER:
				ObjectShelf.arrayFill((int[])__a, __o, __l,
					((Number)__val).intValue());
				return;
				
			case ArrayUtils.ARRAY_LONG:
				ObjectShelf.arrayFill((long[])__a, __o, __l,
					((Number)__val).longValue());
				return;
				
			case ArrayUtils.ARRAY_FLOAT:
				ObjectShelf.arrayFill((float[])__a, __o, __l,
					((Number)__val).floatValue());
				return;
				
			case ArrayUtils.ARRAY_DOUBLE:
				ObjectShelf.arrayFill((double[])__a, __o, __l,
					((Number)__val).doubleValue());
				return;
			
			default:
				throw new UntestableException("" + __type);
		}
	}
	
	/**
	 * Returns the type ID of the given array.
	 * 
	 * @param __id The id to get.
	 * @return The type of the given array.
	 * @since 2021/01/05
	 */
	public static int ofType(String __id)
	{
		switch (__id)
		{
			case "BOOLEAN":	return ArrayUtils.ARRAY_BOOLEAN;
			case "BYTE":	return ArrayUtils.ARRAY_BYTE;
			case "SHORT":	return ArrayUtils.ARRAY_SHORT;
			case "CHAR":	return ArrayUtils.ARRAY_CHARACTER;
			case "INT":		return ArrayUtils.ARRAY_INTEGER;
			case "LONG":	return ArrayUtils.ARRAY_LONG;
			case "FLOAT":	return ArrayUtils.ARRAY_FLOAT;
			case "DOUBLE":	return ArrayUtils.ARRAY_DOUBLE;
			
			default:
				throw new UntestableException(__id);
		}
	}
}
