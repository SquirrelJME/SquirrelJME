package nano;// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Used for expected NanoTest results.
 *
 * @since 2025/09/24
 */
@SquirrelJMEVendorApi
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
	 * Returns {@code null}.
	 *
	 * @return {@code null}.
	 * @since 2026/01/10
	 */
	@SquirrelJMEVendorApi
	public static native String[] makeArrayNull();
	
	/**
	 * Creates a string array.
	 *
	 * @param __n The number of elements.
	 * @return The resultant string array.
	 * @since 2026/01/05
	 */
	@SquirrelJMEVendorApi
	public static native String[] makeArrayString(int __n);
	
	/**
	 * There was a result, however it was void.
	 *
	 * @since 2025/09/24
	 */
	@SquirrelJMEVendorApi
	public static native void result();
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMEVendorApi
	public static native void result(int __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMEVendorApi
	public static native void result(float __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMEVendorApi
	public static native void result(long __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMEVendorApi
	public static native void result(double __v);
	
	/**
	 * The specified value was the given result.
	 *
	 * @param __v The result value.
	 * @since 2025/09/24
	 */
	@SquirrelJMEVendorApi
	public static native void result(String __v);
	
	/**
	 * This test needs to be written.
	 *
	 * @since 2025/11/26
	 */
	@SquirrelJMEVendorApi
	public static native void todo();
}
