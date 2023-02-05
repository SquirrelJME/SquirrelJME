// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.constants.MonitorResultType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This shelf supports object anything that has to do with objects.
 *
 * @since 2020/06/09
 */
@Api
public final class ObjectShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/09
	 */
	private ObjectShelf()
	{
	}
	
	/**
	 * Checks if the given object can be stored in the specified array.
	 * 
	 * @param __array The array to check.
	 * @param __val The value to check
	 * @return If the value can be stored in the given array.
	 * @throws MLECallError If given type is not an array or {@code __array}
	 * is {@code null}.
	 * @since 2021/02/07
	 */
	@Api
	public static native boolean arrayCheckStore(Object __array, Object __val)
		throws MLECallError;
	
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
	@Api
	public static native void arrayCopy(boolean[] __src, int __srcOff,
		boolean[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(byte[] __src, int __srcOff,
		byte[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(short[] __src, int __srcOff,
		short[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(char[] __src, int __srcOff,
		char[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(int[] __src, int __srcOff,
		int[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(long[] __src, int __srcOff,
		long[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(float[] __src, int __srcOff,
		float[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayCopy(double[] __src, int __srcOff,
		double[] __dest, int __destOff, int __len);
	
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
	@Api
	public static native void arrayFill(boolean[] __b, int __o, int __l,
		boolean __v)
		throws MLECallError;
		
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
	@Api
	public static native void arrayFill(byte[] __b, int __o, int __l,
		byte __v)
		throws MLECallError;
		
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
	@Api
	public static native void arrayFill(short[] __b, int __o, int __l,
		short __v)
		throws MLECallError;
		
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
	@Api
	public static native void arrayFill(char[] __b, int __o, int __l,
		char __v)
		throws MLECallError;
		
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
	@Api
	public static native void arrayFill(int[] __b, int __o, int __l,
		int __v)
		throws MLECallError;
		
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
	@Api
	public static native void arrayFill(long[] __b, int __o, int __l,
		long __v)
		throws MLECallError;
		
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
	@Api
	public static native void arrayFill(float[] __b, int __o, int __l,
		float __v)
		throws MLECallError;
	
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
	@Api
	public static native void arrayFill(double[] __b, int __o, int __l,
		double __v)
		throws MLECallError;
	
	/**
	 * Returns the length of the array if this object is an array.
	 *
	 * @param __object The object to get the length of.
	 * @return The length of the array or a negative value if this is not an
	 * array.
	 * @since 2020/06/09
	 */
	@Api
	public static native int arrayLength(Object __object);
	
	/**
	 * Allocates a new array.
	 *
	 * @param <T> The resultant type of the array.
	 * @param __type The type to allocate the array for.
	 * @param __len The length of the array.
	 * @return The newly allocated array as the given object.
	 * @since 2020/06/09
	 */
	@Api
	public static native <T> T arrayNew(TypeBracket __type, int __len);
	
	/**
	 * Checks if the given thread holds the lock on the given method.
	 *
	 * @param __javaThread The Java thread to check if it holds the lock.
	 * @param __o The object to check.
	 * @return If the given thread holds the lock.
	 * @since 2020/06/17
	 */
	@Api
	public static native boolean holdsLock(Thread __javaThread, Object __o);
	
	/**
	 * Returns the identity hashcode of the object.
	 *
	 * @param __o The object to get the hashcode of.
	 * @return The identity hashcode of the object.
	 * @since 2020/06/18
	 */
	@Api
	public static native int identityHashCode(Object __o);
	
	/**
	 * Checks if this object is an array.
	 * 
	 * @param __object The object to check.
	 * @return If this object is an array.
	 * @since 2021/04/07
	 */
	@Api
	public static native boolean isArray(Object __object);
	
	/**
	 * Checks if this object is an instance of the given type.
	 * 
	 * @param __o The object to check.
	 * @param __type The type it may be.
	 * @return If this object is an instance of the given type.
	 * @throws MLECallError If {@code __type} is null.
	 * @since 2021/02/07
	 */
	@Api
	public static native boolean isInstance(Object __o, TypeBracket __type)
		throws MLECallError;
	
	/**
	 * Creates a new instance of the given type.
	 *
	 * @param __type The type to instantiate.
	 * @return The newly created object or {@code null} if there was no
	 * memory left.
	 * @since 2020/06/17
	 */
	@Api
	public static native Object newInstance(TypeBracket __type);
	
	/**
	 * Notifies the monitors holding onto this object.
	 * 
	 * @param __object The object to signal.
	 * @param __all Notify all threads?
	 * @return The {@link MonitorResultType}.
	 * @since 2020/06/22
	 */
	@Api
	public static native int notify(Object __object, boolean __all);
	
	/**
	 * Waits on the given monitor.
	 * 
	 * If the monitor will block and SquirrelJME is running in cooperative
	 * single threaded mode, this will relinquish control of the current
	 * thread.
	 * 
	 * @param __object The object to wait on.
	 * @param __ms The milliseconds to wait.
	 * @param __ns The nanoseconds to wait.
	 * @return The {@link MonitorResultType}.
	 * @since 2020/06/22
	 */
	@Api
	public static native int wait(Object __object, long __ms, int __ns);
}
