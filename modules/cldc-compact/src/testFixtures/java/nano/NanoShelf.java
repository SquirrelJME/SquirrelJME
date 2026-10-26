package nano;// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Used for expected NanoTest results.
 *
 * @since 2025/09/24
 */
@SquirrelJMENativeApi(min = "0.4.0")
public final class NanoShelf
{
	/**
	 * Not used.
	 *
	 * @since 2025/09/24
	 */
	private NanoShelf()
	{
	}
	/**
	 * Creates a boolean array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant boolean array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native boolean[] makeArrayBoolean(int __n);
	
	/**
	 * Creates a byte array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant byte array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native byte[] makeArrayByte(int __n);
	
	/**
	 * Creates a character array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant character array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native char[] makeArrayChar(int __n);
	
	/**
	 * Creates a double array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant double array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native double[] makeArrayDouble(int __n);
	
	/**
	 * Creates a float array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant float array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native float[] makeArrayFloat(int __n);
	
	/**
	 * Creates an integer array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant integer array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native int[] makeArrayInteger(int __n);
	
	/**
	 * Creates a long array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant long array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native long[] makeArrayLong(int __n);
	
	/**
	 * Returns {@code null}.
	 *
	 * @return {@code null}.
	 * @since 2026/01/10
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native String[] makeArrayNull();
	
	/**
	 * Creates a short array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant short array.
	 * @since 2026/10/25
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native short[] makeArrayShort(int __n);
	
	/**
	 * Creates a string array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant string array.
	 * @since 2026/01/05
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native String[] makeArrayString(int __n);
	
	/**
	 * There was a result, however it was void.
	 *
	 * @since 2025/09/24
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result();
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(int __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2026/10/22
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(int[] __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(float __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2026/10/22
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(float[] __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(long __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2026/10/22
	 */
	public static native void result(long[] __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(double __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2026/10/22
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(double[] __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(String __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2026/10/22
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void result(String[] __v);
	
	/**
	 * This test needs to be written.
	 *
	 * @since 2025/11/26
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	public static native void todo();
}
