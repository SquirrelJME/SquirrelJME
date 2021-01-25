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
import cc.squirreljme.jvm.summercoat.constants.StaticVmAttribute;
import cc.squirreljme.jvm.summercoat.struct.ClassInfoStruct;
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
	 * Checks if the array can store the given value.
	 * 
	 * @param __array The array.
	 * @param __value The value to check.
	 * @return If the value can be stored in the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/24
	 */
	public static boolean checkArrayStore(Object __array, Object __value)
		throws NullPointerException
	{
		if (__array == null)
			throw new NullPointerException("NARG");
		
		// Storing null values is always okay
		if (__value == null)
			return true;
		
		// Determine the component type of the array
		ClassInfoBracket arrayType = LogicHandler.objectClassInfo(__array);
		ClassInfoBracket compType = Assembly.pointerToClassInfo(
			LogicHandler.classInfoGetProperty(arrayType,
				ClassProperty.CLASSINFO_COMPONENTCLASS));
		
		// Check down the class tree for a matching class
		ClassInfoBracket valueType = LogicHandler.objectClassInfo(__value);
		for (ClassInfoBracket at = valueType; at != null;)
		{
			// Is a match of this type
			if (compType == at)
				return true;
			
			// TODO: Check interfaces
			Assembly.ping();
			
			// Do we need to go down still?
			int superP = LogicHandler.classInfoGetProperty(arrayType,
				ClassProperty.CLASSINFO_SUPER);
			if (superP == 0)
				break;
			
			// Go to the super class
			at = Assembly.pointerToClassInfo(superP);
		}
		
		// Not a match
		return false;
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
	 * Checks if this object is an array.
	 * 
	 * @param __object The object to check.
	 * @return If this object is an array.
	 * @since 2021/01/24
	 */
	public static boolean isArray(Object __object)
	{
		// Null is never an array
		if (__object == null)
			return false;
		
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
		Assembly.memHandleWriteInt(rv, LogicHandler.staticVmAttribute(
			StaticVmAttribute.OFFSETOF_ARRAY_LENGTH_FIELD), __len);
		
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
		
		// Allocate the object
		return LogicHandler.__allocObject(__info, allocSize);
	}
	
	/**
	 * Returns the class information on the object, its type.
	 * 
	 * @param __o The object.
	 * @return The class information for the object.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/24
	 */
	public static ClassInfoBracket objectClassInfo(Object __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
			
		// Directly read the type
		return Assembly.pointerToClassInfo(
			Assembly.memHandleReadInt(__o, LogicHandler.staticVmAttribute(
				StaticVmAttribute.OFFSETOF_OBJECT_TYPE_FIELD)));
	}
	
	/**
	 * Returns the static virtual machine attribute.
	 * 
	 * @param __attr The {@link StaticVmAttribute} to get.
	 * @return The value of the metric.
	 * @throws IllegalArgumentException If the metric is not valid.
	 * @since 2021/01/24
	 */
	public static int staticVmAttribute(int __attr)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ4n Invalid Vm Metric, (Metric Id)}
		if (__attr <= 0 || __attr >= StaticVmAttribute.NUM_METRICS)
			throw new IllegalArgumentException("ZZ4n " + __attr);
		
		int arrayBase = SystemCall.arrayAllocationBase();
		return Assembly.memHandleReadInt(SystemCall.staticVmAttributes(),
			arrayBase + (__attr * 4));
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
		int rv = SystemCall.memHandleNew(memHandleKind, __allocSize);
		if (rv == 0)
		{
			// Attempt garbage collection
			System.gc();
			
			// Try again, but fail if it still fails
			rv = SystemCall.memHandleNew(memHandleKind, __allocSize);
			if (rv == 0)
				throw new OutOfMemoryError();
		}
		
		// Set the object type information
		Assembly.memHandleWriteObject(rv, LogicHandler.staticVmAttribute(
			StaticVmAttribute.OFFSETOF_OBJECT_TYPE_FIELD), __info);
		
		// Convert to represented object before returning.
		return Assembly.pointerToObject(rv);
	}
}
