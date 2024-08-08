// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Emulates {@link ObjectShelf}.
 *
 * @since 2021/12/26
 */
@SquirrelJMEVendorApi
public class EmulatedObjectShelf
{
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyZ(
		boolean[] __src,
		int __srcOff,
		boolean[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyB(
		byte[] __src,
		int __srcOff,
		byte[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyS(
		short[] __src,
		int __srcOff,
		short[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyC(
		char[] __src,
		int __srcOff,
		char[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyI(
		int[] __src,
		int __srcOff,
		int[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyJ(
		long[] __src,
		int __srcOff,
		long[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyF(
		float[] __src,
		int __srcOff,
		float[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Copies the given arrays. If the source and destination are the same
	 * array, the copy operation will not collide with itself.
	 * 
	 * @param __src The source array.
	 * @param __srcOff The source offset.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The elements to copy.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static void arrayCopyD(
		 double[] __src,
		int __srcOff,
		double[] __dest,
		int __destOff,
		int __len)
	{
		System.arraycopy(__src, __srcOff, __dest, __destOff, __len);
	}
	
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(boolean[] __b, int __o, int __l,
		boolean __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
		
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(byte[] __b, int __o, int __l,
		byte __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
		
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(short[] __b, int __o, int __l,
		short __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
		
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(char[] __b, int __o, int __l,
		char __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
		
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(int[] __b, int __o, int __l,
		int __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
		
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(long[] __b, int __o, int __l,
		long __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
		
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(float[] __b, int __o, int __l,
		float __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
	
	/**
	 * Fills part of the given array with the specified value.
	 * 
	 * @param __b The buffer to fill.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the bytes to fill.
	 * @param __v The value to store into the buffer.
	 * @throws MLECallError If any argument is {@code null}, or the offset
	 * and/or length exceed the array bounds.
	 * @since 2021/12/26
	 */
	@SquirrelJMEVendorApi
	public static void arrayFill(double[] __b, int __o, int __l,
		double __v)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Invalid fill arguments.");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = __v;
	}
}
