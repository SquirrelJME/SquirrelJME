// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.nanocoat.mle;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Used for expected NanoTest results.
 *
 * @since 2025/09/24
 */
@SquirrelJMEVendorApi
public final class NanoTestShelf
{
	/**
	 * Not used.
	 *
	 * @since 2025/09/24
	 */
	private NanoTestShelf()
	{
	}
	
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
}
