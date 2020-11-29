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
import cc.squirreljme.jvm.summercoat.constants.ClassInfoProperty;
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
			ClassInfoProperty.INT_ALLOCATION_SIZE);
		if (allocSize <= 0)
			throw new MLECallError("ZZ4j");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
