// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.constants.MonitorResultType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This shelf supports object anything that has to do with objects.
 *
 * @since 2020/06/09
 */
@SuppressWarnings("UnstableApiUsage")
@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public static native boolean arrayCheckStore(
		@NotNull Object __array, @NotNull Object __val)
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
	@SquirrelJMEVendorApi
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull boolean[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull boolean[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull byte[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull byte[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull short[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull short[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull char[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull char[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull int[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull int[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull long[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull long[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull float[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull float[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayCopy(
		@Flow(sourceIsContainer=true, target="__dest",
			targetIsContainer=true) @NotNull double[] __src,
		@Range(from = 0, to = Integer.MAX_VALUE) int __srcOff,
		@NotNull double[] __dest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __destOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
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
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull boolean[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l, boolean __v)
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull byte[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l, byte __v)
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull short[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l, short __v)
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull char[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull int[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull long[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull float[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
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
	@SquirrelJMEVendorApi
	public static native void arrayFill(
		@Flow(target="__v",
			targetIsContainer=true) @NotNull double[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l, double __v)
		throws MLECallError;
	
	/**
	 * Returns the length of the array if this object is an array.
	 *
	 * @param __object The object to get the length of.
	 * @return The length of the array or a negative value if this is not an
	 * array.
	 * @since 2020/06/09
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	public static native int arrayLength(@NotNull Object __object);
	
	/**
	 * Allocates a new array.
	 *
	 * @param <T> The resultant type of the array.
	 * @param __type The type to allocate the array for.
	 * @param __len The length of the array.
	 * @return The newly allocated array as the given object.
	 * @since 2020/06/09
	 */
	@SquirrelJMEVendorApi
	public static native <T> T arrayNew(@NotNull TypeBracket __type,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
	
	/**
	 * Checks if the given thread holds the lock on the given method.
	 *
	 * @param __javaThread The Java thread to check if it holds the lock.
	 * @param __o The object to check.
	 * @return If the given thread holds the lock.
	 * @since 2020/06/17
	 */
	@SquirrelJMEVendorApi
	public static native boolean holdsLock(@NotNull Thread __javaThread,
		@NotNull Object __o);
	
	/**
	 * Returns the identity hashcode of the object.
	 *
	 * @param __o The object to get the hashcode of.
	 * @return The identity hashcode of the object.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native int identityHashCode(@NotNull Object __o);
	
	/**
	 * Checks if this object is an array.
	 * 
	 * @param __object The object to check.
	 * @return If this object is an array.
	 * @since 2021/04/07
	 */
	@SquirrelJMEVendorApi
	public static native boolean isArray(@NotNull Object __object);
	
	/**
	 * Checks if this object is an instance of the given type.
	 * 
	 * @param __o The object to check.
	 * @param __type The type it may be.
	 * @return If this object is an instance of the given type.
	 * @throws MLECallError If {@code __type} is null.
	 * @since 2021/02/07
	 */
	@SquirrelJMEVendorApi
	public static native boolean isInstance(@NotNull Object __o,
		@NotNull TypeBracket __type)
		throws MLECallError;
	
	/**
	 * Creates a new instance of the given type, this has the same effect
	 * as calling
	 * {@link ObjectShelf#newInstance(TypeBracket, TypeBracket[], Object...)}
	 * with {@code newInstance(__type, new TypeBracket[0], new Object[0])};
	 *
	 * @param __type The type to instantiate.
	 * @return The newly created object or {@code null} if there was no
	 * memory left.
	 * @since 2020/06/17
	 */
	@SquirrelJMEVendorApi
	public static native Object newInstance(@NotNull TypeBracket __type);
	
	/**
	 * Creates a new instance of the given object, calling the specific
	 * constructor.
	 * 
	 * @param __type The type to make a new instance of.
	 * @param __argTypes The types that the arguments make up 
	 * @param __args The arguments to the constructor.
	 * @return The newly created object or {@code null} if there was no
	 * memory left.
	 * @throws MLECallError If any argument is null (except for values within
	 * {@code __args} as a null argument may be valid, if an argument type
	 * is null, if a passed argument is not null and not an instance of the
	 * type, if the constructor does not exist, or if the argument count does
	 * not match the argument types count.
	 * @since 2020/07/14
	 */
	public static native Object newInstance(TypeBracket __type,
		TypeBracket[] __argTypes, Object... __args);
	
	/**
	 * Notifies the monitors holding onto this object.
	 * 
	 * @param __object The object to signal.
	 * @param __all Notify all threads?
	 * @return The {@link MonitorResultType}.
	 * @since 2020/06/22
	 */
	@SquirrelJMEVendorApi
	public static native int notify(@NotNull Object __object, boolean __all);
	
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
	@SquirrelJMEVendorApi
	@Blocking
	public static native int wait(@NotNull Object __object,
		@Range(from = 0, to = Integer.MAX_VALUE) long __ms,
		@Range(from = 0, to = 999999) int __ns);
}
