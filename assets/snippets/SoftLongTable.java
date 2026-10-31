// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.nio.file.*;
import java.io.*;
import java.util.*;

/**
 * Generates soft long tables.
 *
 * @since 2026/10/31
 */
public class SoftLongTable
{
	/** The number of longs to store. */
	public static final int NUM_LONGS = 1024;
	
	public static void header(PrintStream __out, String __type, String __key,
		int __len)
	{
		__out.printf("#include \"sjme/stdTypes.h\"%n");
		__out.printf("#include \"testData.h\"%n");
		__out.printf("%n");
		__out.printf("const %s sjme_test_longs_%s[SJME_TEST_NUM_LONGS] = %n",
			__type, __key, __len);
		__out.printf("{%n");
	}
	
	public static void tail(PrintStream __out)
	{
		__out.printf("};%n");
		__out.printf("%n");
	}
	
	public static void dump(PrintStream __out, String __key, long... __data)
	{
		header(__out, "sjme_test_jlongPart", __key, __data.length);
		for (long __val : __data)
			__out.printf("\t{INT32_C(0x%08X), UINT32_C(0x%08X)},%n",
				__val >>> 32L, __val & 0xFFFFFFFFL);
		tail(__out);
	}
	
	public static void dump(PrintStream __out, String __key, double... __data)
	{
		header(__out, "sjme_test_jdoublePart", __key, __data.length);
		for (double __val : __data)
			__out.printf("\t{UINT32_C(0x%08X), UINT32_C(0x%08X)},%n",
				Double.doubleToRawLongBits(__val) >>> 32L,
				Double.doubleToRawLongBits(__val) & 0xFFFFFFFFL);
		tail(__out);
	}
	
	public static void dump(PrintStream __out, String __key, int... __data)
	{
		header(__out, "sjme_jint", __key, __data.length);
		for (int __val : __data)
			__out.printf("\tINT32_C(0x%08X),%n",
				__val);
		tail(__out);
	}
	
	public static void dump(PrintStream __out, String __key, float... __data)
	{
		header(__out, "sjme_juint", __key, __data.length);
		for (float __val : __data)
			__out.printf("\tUINT32_C(0x%08X),%n",
				Float.floatToRawIntBits(__val));
		tail(__out);
	}
	
	public static void dump(String __key, Object __data)
	{
		String cap = __key.substring(0, 1);
		cap = cap.toUpperCase();
		cap += __key.substring(1);
		
		try (OutputStream os = Files.newOutputStream(
			Paths.get(String.format("longs%s.c", cap)),
			StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE,
			StandardOpenOption.WRITE);
			PrintStream ps = new PrintStream(os))
		{
			if (__data instanceof long[])
				dump(ps, __key, (long[])__data);
			else if (__data instanceof double[])
				dump(ps, __key, (double[])__data);
			else if (__data instanceof float[])
				dump(ps, __key, (float[])__data);
			else
				dump(ps, __key, (int[])__data);
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Ignored.
	 * @since 2026/10/31
	 */
	public static void main(String... __args)
	{
		Random random = new Random(0x12345678L);
		
		long[] input = new long[NUM_LONGS];
		
		long[] add = new long[NUM_LONGS];
		long[] sub = new long[NUM_LONGS];
		long[] mul = new long[NUM_LONGS];
		long[] div = new long[NUM_LONGS];
		long[] rem = new long[NUM_LONGS];
		long[] and = new long[NUM_LONGS];
		long[] or = new long[NUM_LONGS];
		long[] xor = new long[NUM_LONGS];
		long[] shl = new long[NUM_LONGS];
		long[] shr = new long[NUM_LONGS];
		long[] ushr = new long[NUM_LONGS];
		long[] neg = new long[NUM_LONGS];
		int[] cmp = new int[NUM_LONGS];
		double[] toDouble = new double[NUM_LONGS];
		float[] toFloat = new float[NUM_LONGS];
		int[] toInteger = new int[NUM_LONGS];
		
		// Generate all the longs
		for (int i = 0; i < NUM_LONGS; i++)
			if (i < 128)
				input[i] = i - 64;
			else if (i < 256)
				input[i] = (256 - i);
			else if (i < 384)
				input[i] = Math.abs(random.nextLong() % 65536) *
					(((i & 1) != 0) ? -1 : 1);
			else
				input[i] = random.nextLong();
		
		// Calculate all values
		for (int i = 0; i < NUM_LONGS; i++)
		{
			long a = input[i];
			long b = input[(i + 1) % NUM_LONGS];
			
			neg[i] = -a;
			toDouble[i] = (double)a;
			toFloat[i] = (float)a;
			toInteger[i] = (int)a;
			
			add[i] = a + b;
			sub[i] = a - b;
			mul[i] = a * b;
			div[i] = (b == 0 ? 0 : a / b);
			rem[i] = (b == 0 ? 0 : a % b);
			and[i] = a & b;
			or[i] = a | b;
			xor[i] = a ^ b;
			shl[i] = a << b;
			shr[i] = a >> b;
			ushr[i] = a >>> b;
			
			cmp[i] = Integer.signum(Long.compare(a, b));
		}
		
		// Dump everything
		dump("in", input);
		dump("add", add);
		dump("sub", sub);
		dump("mul", mul);
		dump("div", div);
		dump("rem", rem);
		dump("and", and);
		dump("or", or);
		dump("xor", xor);
		dump("shl", shl);
		dump("shr", shr);
		dump("ushr", ushr);
		
		dump("neg", neg);
		dump("cmp", cmp);
		
		dump("int", toInteger);
		dump("double", toDouble);
		dump("float", toFloat);
	}
}
