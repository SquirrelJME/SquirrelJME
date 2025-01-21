// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Provides access to {@link String}s.
 *
 * @since 2025/01/20
 */
@SquirrelJMEVendorApi
public final class StringShelf
{
	/**
	 * Not used.
	 *
	 * @since 2025/01/20
	 */
	private StringShelf()
	{
	}
	
	/**
	 * Returns the character at the given index. 
	 *
	 * @param __string The string to read from.
	 * @param __index The index of the character.
	 * @return The resultant character.
	 * @throws MLECallError On null arguments, or if the index is not valid.
	 * @since 2025/01/20
	 */
	@SquirrelJMEVendorApi
	public static native int stringCharAt(@NotNull String __string,
		@Range(from = 0, to = Integer.MAX_VALUE) int __index)
		throws MLECallError;
	
	/**
	 * Returns the hash code of the given string.
	 *
	 * @param __string The string to get the hash of.
	 * @return The string hash.
	 * @throws MLECallError On null arguments.
	 * @since 2025/01/20
	 */
	@SquirrelJMEVendorApi
	public static native int stringHash(@NotNull String __string)
		throws MLECallError;
	
	/**
	 * Returns the length of the string.
	 *
	 * @param __string The string to get the length of.
	 * @return The string length.
	 * @throws MLECallError On null arguments.
	 * @since 2025/01/20
	 */
	@SquirrelJMEVendorApi
	public static native int stringLength(@NotNull String __string)
		throws MLECallError;
	
	/**
	 * Returns a string which has the value of the given string.
	 *
	 * @param __intern Should the resultant string be an intern string?
	 * @param __string The value to use.
	 * @return The resultant string.
	 * @throws MLECallError On null arguments.
	 * @since 2025/01/20
	 */
	@SquirrelJMEVendorApi
	public static native String stringValueOf(boolean __intern,
		@NotNull String __string)
		throws MLECallError;
	
	/**
	 * Returns a string which has the value of the given character array.
	 *
	 * @param __intern Should the resultant string be an intern string?
	 * @param __c The character values.
	 * @param __o The offset into the array.
	 * @param __l The length of the string.
	 * @return The resultant string.
	 * @throws MLECallError On null arguments; or the offset and/or length
	 * exceed the array bounds.
	 * @since 2025/01/20
	 */
	@SquirrelJMEVendorApi
	public static native String stringValueOf(boolean __intern,
		@NotNull char[] __c,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l)
		throws MLECallError;
}
