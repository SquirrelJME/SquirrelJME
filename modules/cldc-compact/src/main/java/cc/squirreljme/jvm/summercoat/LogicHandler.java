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
	 * Returns the value of the given class property.
	 * 
	 * @param __info The information to get.
	 * @param __p The {@link ClassProperty}.
	 * @return The value of the given property.
	 * @throws MLECallError If {@code __info} is {@code null} or is not a
	 * valid class.
	 * @since 2020/11/29
	 */
	public static int classInfoGetProperty(ClassInfoBracket __info, int __p)
		throws MLECallError
	{
		if (__info == null)
			throw new MLECallError("NARG");
		
		// {@squirreljme.error ZZ4m Invalid class property. (The property)}
		if (__p <= 0 || __p >= ClassProperty.NUM_RUNTIME_PROPERTIES)
			throw new MLECallError("ZZ4m " + __p);
		
		int arrayBase = SystemCall.arrayAllocationBase();
		return Assembly.memHandleReadInt(__info, arrayBase + (__p * 4));
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
	 * @throws OutOfMemoryError If there is no memory remaining.
	 * @since 2020/11/29
	 */
	public static Object newArray(ClassInfoBracket __info, int __len)
		throws NegativeArraySizeException, NullPointerException,
			OutOfMemoryError
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		if (__len < 0)
			throw new NegativeArraySizeException("" + __len);
		
		// Determine how large the object needs to be
		int allocBase = LogicHandler.classInfoGetProperty(__info,
			ClassProperty.SIZE_ALLOCATION);
		int allocSize = allocBase +
			(__len * LogicHandler.classInfoGetProperty(__info,
				ClassProperty.INT_COMPONENT_CELL_SIZE));
		
		// Allocate the object
		Object rv = LogicHandler.__allocObject(__info, allocSize);
		
		// Set the length of the array
		Assembly.memHandleWriteInt(rv, SystemCall.offsetOfArrayLengthField(),
			__len);
		
		return rv;
	}
	
	/**
	 * Allocates a new instance of the given class.
	 * 
	 * @param __info The class to allocate.
	 * @return The allocated object data.
	 * @throws NullPointerException On null arguments.
	 * @throws OutOfMemoryError If there is no memory remaining.
	 * @since 2020/11/29
	 */
	public static Object newInstance(ClassInfoBracket __info)
		throws NullPointerException, OutOfMemoryError
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ4j Class has no allocated size?}
		int allocSize = LogicHandler.classInfoGetProperty(__info,
			ClassProperty.SIZE_ALLOCATION);
		if (allocSize <= 0)
			throw new MLECallError("ZZ4j");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Allocates an object and seeds initial information regarding it.
	 * 
	 * @param __info The class information.
	 * @param __allocSize The allocation size of the object.
	 * @return The allocated object.
	 * @throws NullPointerException On null arguments.
	 * @throws OutOfMemoryError If not enough memory is available.
	 * @since 2021/01/23
	 */
	private static Object __allocObject(ClassInfoBracket __info,
		int __allocSize)
		throws NullPointerException, OutOfMemoryError
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		// This represents the kind of handle that gets allocated
		int memHandleKind = LogicHandler.classInfoGetProperty(__info,
			ClassProperty.INT_MEMHANDLE_KIND);
		
		// Attempt to allocate a handle
		int rv = SystemCall.memHandleNew(__allocSize, memHandleKind);
		if (rv == 0)
		{
			// Attempt garbage collection
			System.gc();
			
			// Try again, but fail if it still fails
			rv = SystemCall.memHandleNew(__allocSize, memHandleKind);
			if (rv == 0)
				throw new OutOfMemoryError();
		}
		
		// Set the object type information
		Assembly.memHandleWriteObject(rv, SystemCall.offsetOfObjectTypeField(),
			__info);
		
		// Convert to represented object before returning.
		return Assembly.pointerToObject(rv);
	}
}
