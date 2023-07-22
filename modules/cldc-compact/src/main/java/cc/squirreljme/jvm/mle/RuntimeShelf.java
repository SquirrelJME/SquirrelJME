// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.constants.BuiltInEncodingType;
import cc.squirreljme.jvm.mle.constants.BuiltInLocaleType;
import cc.squirreljme.jvm.mle.constants.ByteOrderType;
import cc.squirreljme.jvm.mle.constants.LineEndingType;
import cc.squirreljme.jvm.mle.constants.MemoryProfileType;
import cc.squirreljme.jvm.mle.constants.PhoneModelType;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.constants.VMType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Run-time shelf which contains system information.
 *
 * @since 2020/06/09
 */
@SquirrelJMEVendorApi
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
	 * Returns the byte order of the system.
	 * 
	 * @return The {@link ByteOrderType} of the system.
	 * @since 2021/02/09
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = ByteOrderType.class)
	public static native int byteOrder();
	
	/**
	 * Returns the current time in milliseconds since UTC.
	 *
	 * @return The current time in milliseconds since UTC.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native long currentTimeMillis();
	
	/**
	 * Returns the encoding of the system.
	 *
	 * @return The encoding of the system, see {@link BuiltInEncodingType}.
	 * @see BuiltInEncodingType
	 * @since 2020/06/11
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = BuiltInEncodingType.class)
	public static native int encoding();
	
	/**
	 * Exits the virtual machine.
	 *
	 * @param __code The exit code.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Contract("_ -> fail")
	public static native void exit(int __code);
	
	/**
	 * Suggests that garbage collection be done, this may happen now, in
	 * the future, or never as it is not defined.
	 * 
	 * @since 2021/01/04
	 */
	@SquirrelJMEVendorApi
	@Blocking
	public static native void garbageCollect();
	
	/**
	 * Returns the line ending type of the system.
	 *
	 * @return The line ending type of the system, see {@link LineEndingType}.
	 * @see LineEndingType
	 * @since 2020/06/09
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = LineEndingType.class)
	public static native int lineEnding();
	
	/**
	 * The built-in locate to use.
	 *
	 * @return The built-in locale, see {@link BuiltInLocaleType}.
	 * @see BuiltInLocaleType
	 * @since 2020/06/11
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = BuiltInLocaleType.class)
	public static native int locale();
	
	/**
	 * The memory profile of the system, should it go out of its way to
	 * conserve extra memory at a performance cost?
	 * 
	 * @return The {@link MemoryProfileType} of the system.
	 * @since 2021/02/19
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = MemoryProfileType.class)
	public static native int memoryProfile();
	
	/**
	 * Returns the number of monotonic nanoseconds that have elapsed.
	 *
	 * @return The monotonic nanosecond clock.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native long nanoTime();
	
	/**
	 * Returns the phone model that SquirrelJME is simulating itself as.
	 * 
	 * @return The {@link PhoneModelType}.
	 * @since 2022/02/14
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PhoneModelType.class)
	public static native int phoneModel();
	
	/**
	 * Returns the value of a system environment variable.
	 * 
	 * Not every platform and/or system may have these, so these should not
	 * be depended upon.
	 * 
	 * @param __key The environment variable key.
	 * @return The value of the variable if it is set, otherwise {@code null}.
	 * @throws MLECallError If key is {@code null}.
	 * @since 2023/02/02
	 */
	@SquirrelJMEVendorApi
	public static native String systemEnv(@NotNull String __key)
		throws MLECallError;
	
	/**
	 * Returns the system property for the given key, if there is one.
	 *
	 * @param __key The property key.
	 * @return The value of the system property or {@code null}.
	 * @throws MLECallError If {@code __key} is {@code null}.
	 * @since 2020/06/17
	 */
	@SquirrelJMEVendorApi
	public static native String systemProperty(@NotNull String __key)
		throws MLECallError;
	
	/**
	 * Returns a special virtual machine description.
	 *
	 * @param __type The {@link VMDescriptionType}.
	 * @return The string for the given description or {@code null} if not
	 * set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2020/06/17
	 */
	@SquirrelJMEVendorApi
	public static native String vmDescription(
		@MagicConstant(valuesFromClass = VMDescriptionType.class) int __type)
		throws MLECallError;
	
	/**
	 * Returns a statistic of the virtual machine.
	 *
	 * @param __type The {@link VMStatisticType}.
	 * @return The value of the statistic, or {@code 0L} if not set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2020/06/17
	 */
	@SquirrelJMEVendorApi
	public static native long vmStatistic(
		@MagicConstant(valuesFromClass = VMStatisticType.class) int __type)
		throws MLECallError;
	
	/**
	 * Returns the current {@link VMType}.
	 *
	 * @return The current {@link VMType}.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = VMType.class)
	public static native int vmType();
}
