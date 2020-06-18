// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.constants.LineEndingType;
import cc.squirreljme.jvm.mle.constants.BuiltInEncodingType;
import cc.squirreljme.jvm.mle.constants.BuiltInLocaleType;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.constants.VMType;

/**
 * Run-time shelf which contains system information.
 *
 * @since 2020/06/09
 */
public final class RuntimeShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/09
	 */
	private RuntimeShelf()
	{
	}
	
	/**
	 * Returns the current time in milliseconds since UTC.
	 *
	 * @return The current time in milliseconds since UTC.
	 * @since 2020/06/18
	 */
	public static native long currentTimeMillis();
	
	/**
	 * Returns the encoding of the system.
	 *
	 * @return The encoding of the system, see {@link BuiltInEncodingType}.
	 * @see BuiltInEncodingType
	 * @since 2020/06/11
	 */
	public static native int encoding();
	
	/**
	 * Exits the virtual machine.
	 *
	 * @param __code The exit code.
	 * @since 2020/06/16
	 */
	public static native void exit(int __code);
	
	/**
	 * Returns the line ending type of the system.
	 *
	 * @return The line ending type of the system, see {@link LineEndingType}.
	 * @see LineEndingType
	 * @since 2020/06/09
	 */
	public static native int lineEnding();
	
	/**
	 * The built-in locate to use.
	 *
	 * @return The built-in locale, see {@link BuiltInLocaleType}.
	 * @see BuiltInLocaleType
	 * @since 2020/06/11
	 */
	public static native int locale();
	
	/**
	 * Returns the number of monotonic nanoseconds that have elapsed.
	 *
	 * @return The monotonic nanosecond clock.
	 * @since 2020/06/18
	 */
	public static native long nanoTime();
	
	/**
	 * Returns the system property for the given key, if there is one.
	 *
	 * @param __key The property key.
	 * @return The value of the system property or {@code null}.
	 * @since 2020/06/17
	 */
	public static native String systemProperty(String __key);
	
	/**
	 * Returns a special virtual machine description.
	 *
	 * @param __type The {@link VMDescriptionType}.
	 * @return The string for the given description.
	 * @since 2020/06/17
	 */
	public static native String vmDescription(int __type);
	
	/**
	 * Returns a statistic of the virtual machine.
	 *
	 * @param __type The {@link VMStatisticType}.
	 * @return The value of the statistic, or {@code 0L} if not valid or not
	 * available.
	 * @since 2020/06/17
	 */
	public static native long vmStatistic(int __type);
	
	/**
	 * Returns the current {@link VMType}.
	 *
	 * @return The current {@link VMType}.
	 * @since 2020/06/16
	 */
	public static native int vmType();
}
