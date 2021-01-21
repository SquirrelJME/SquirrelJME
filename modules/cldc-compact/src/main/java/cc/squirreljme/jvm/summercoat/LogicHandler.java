// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.summercoat.brackets.ClassInfoBracket;
import cc.squirreljme.jvm.summercoat.constants.ClassProperty;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This handles specific JVM logic that cannot easily be handled in byte code
 * or at the execution layer.
 *
 * @since 2020/11/28
 */
public final class LogicHandler
{
	/**
	 * Not used.
	 * 
	 * @since 2020/11/28
	 */
	private LogicHandler()
	{
	}
	
	/**
	 * Garbage collects the given handle.
	 * 
	 * @param __p The pointer to clear.
	 * @since 2020/11/28
	 */
	public static void gcMemHandle(int __p)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Checks if the given class is initialized.
	 * 
	 * @param __info The class info to initialize.
	 * @return If the class is initialized.
	 * @since 2021/01/20
	 */
	public static boolean isClassInit(ClassInfoBracket __info)
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the given class.
	 * 
	 * @param __info The class info to initialize.
	 * @since 2020/11/28
	 */
	public static void initClass(ClassInfoBracket __info)
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Checks if this is an instance of the given class.
	 * 
	 * @param __o The object to check.
	 * @param __info The class information to check.
	 * @return If this is an instance of the given class.
	 * @since 2020/11/28
	 */
	public static boolean isInstance(int __o, ClassInfoBracket __info)
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Allocates a new array
	 * 
	 * @param __info The class to allocate.
	 * @param __len The array length.
	 * @return The allocated object data.
	 * @throws NegativeArraySizeException If the array is negatively sized.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/29
	 */
	public static Object newArray(ClassInfoBracket __info, int __len)
		throws NegativeArraySizeException, NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		if (__len < 0)
			throw new NegativeArraySizeException("" + __len);
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Allocates a new instance of the given class.
	 * 
	 * @param __info The class to allocate.
	 * @return The allocated object data.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/29
	 */
	public static Object newInstance(ClassInfoBracket __info)
		throws NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ4j Class has no allocated size?}
		int allocSize = SystemCall.classInfoGetProperty(__info,
			ClassProperty.SIZE_ALLOCATION);
		if (allocSize <= 0)
			throw new MLECallError("ZZ4j");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
