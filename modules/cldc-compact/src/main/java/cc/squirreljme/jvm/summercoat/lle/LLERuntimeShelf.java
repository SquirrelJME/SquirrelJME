// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.lle;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.mle.constants.BuiltInEncodingType;
import cc.squirreljme.jvm.mle.constants.BuiltInLocaleType;
import cc.squirreljme.jvm.mle.constants.LineEndingType;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.constants.VMType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.summercoat.SystemCall;
import cc.squirreljme.jvm.summercoat.constants.StaticVmAttribute;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.lang.OperatingSystemType;

/**
 * Run-time shelf which contains system information.
 *
 * @since 2020/06/09
 */
public final class LLERuntimeShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/09
	 */
	private LLERuntimeShelf()
	{
	}
	
	/**
	 * Returns the current time in milliseconds since UTC.
	 *
	 * @return The current time in milliseconds since UTC.
	 * @since 2020/06/18
	 */
	public static long currentTimeMillis()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the encoding of the system.
	 *
	 * @return The encoding of the system, see {@link BuiltInEncodingType}.
	 * @see BuiltInEncodingType
	 * @since 2020/06/11
	 */
	public static int encoding()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Exits the virtual machine.
	 *
	 * @param __code The exit code.
	 * @since 2020/06/16
	 */
	public static void exit(int __code)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Suggests that garbage collection be done, this may happen now, in
	 * the future, or never as it is not defined.
	 * 
	 * @since 2021/01/04
	 */
	public static void garbageCollect()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the line ending type of the system.
	 *
	 * @return The line ending type of the system, see {@link LineEndingType}.
	 * @see LineEndingType
	 * @since 2020/06/09
	 */
	public static int lineEnding()
	{
		switch (SystemCall.operatingSystem())
		{
			case OperatingSystemType.MAC_OS_CLASSIC:
				return LineEndingType.CR;
			
			case OperatingSystemType.MS_DOS:
			case OperatingSystemType.WINDOWS_9X:
			case OperatingSystemType.WINDOWS_CE:
			case OperatingSystemType.WINDOWS_NT:
			case OperatingSystemType.WINDOWS_WIN16:
			case OperatingSystemType.WINDOWS_UNKNOWN:
				return LineEndingType.CRLF;
			
			default:
				return LineEndingType.LF;
		}
	}
	
	/**
	 * The built-in locate to use.
	 *
	 * @return The built-in locale, see {@link BuiltInLocaleType}.
	 * @see BuiltInLocaleType
	 * @since 2020/06/11
	 */
	public static int locale()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the number of monotonic nanoseconds that have elapsed.
	 *
	 * @return The monotonic nanosecond clock.
	 * @since 2020/06/18
	 */
	public static long nanoTime()
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the system property for the given key, if there is one.
	 *
	 * @param __key The property key.
	 * @return The value of the system property or {@code null}.
	 * @throws MLECallError If {@code __key} is {@code null}.
	 * @since 2020/06/17
	 */
	public static String systemProperty(String __key)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns a special virtual machine description.
	 *
	 * @param __type The {@link VMDescriptionType}.
	 * @return The string for the given description or {@code null} if not
	 * set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2020/06/17
	 */
	public static String vmDescription(int __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns a statistic of the virtual machine.
	 *
	 * @param __type The {@link VMStatisticType}.
	 * @return The value of the statistic, or {@code 0L} if not set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2020/06/17
	 */
	public static long vmStatistic(int __type)
		throws MLECallError
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Returns the current {@link VMType}.
	 *
	 * @return The current {@link VMType}.
	 * @since 2020/06/16
	 */
	public static int vmType()
	{
		// The virtual machine type here is always SummerCoat
		return VMType.SUMMERCOAT;
	}
}
