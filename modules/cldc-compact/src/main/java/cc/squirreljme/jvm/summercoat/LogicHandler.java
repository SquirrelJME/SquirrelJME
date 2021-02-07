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
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.summercoat.brackets.QuickCastCheckBracket;
import cc.squirreljme.jvm.summercoat.constants.ClassProperty;
import cc.squirreljme.jvm.summercoat.constants.StaticClassProperty;
import cc.squirreljme.jvm.summercoat.constants.StaticVmAttribute;
import cc.squirreljme.jvm.summercoat.lle.LLETypeShelf;
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
		TypeBracket arrayType = LLETypeShelf.objectType(__array);
		TypeBracket compType = Assembly.pointerToTypeBracket(
			LogicHandler.typeBracketGetProperty(arrayType,
				ClassProperty.TYPEBRACKET_COMPONENT));
		
		// Check down the class tree for a matching class
		TypeBracket valueType = LLETypeShelf.objectType(__value);
		for (TypeBracket at = valueType; at != null;)
		{
			// Is a match of this type
			if (compType == at)
				return true;
			
			// TODO: Check interfaces
			Assembly.ping();
			
			// Do we need to go down still?
			int superP = LogicHandler.typeBracketGetProperty(arrayType,
				ClassProperty.TYPEBRACKET_SUPER);
			if (superP == 0)
				break;
			
			// Go to the super class
			at = Assembly.pointerToTypeBracket(superP);
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
	 * @since 2021/02/07
	 */
	public static int typeBracketGetProperty(int __info, int __p)
		throws MLECallError
	{
		if (__info == 0)
			throw new MLECallError("NARG");
		
		// {@squirreljme.error ZZ4m Invalid class property. (The property)}
		if (__p <= 0 || __p >= ClassProperty.NUM_RUNTIME_PROPERTIES)
			throw new MLECallError("ZZ4m " + __p);
		
		return LogicHandler.listRead(__info, __p);
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
	public static int typeBracketGetProperty(TypeBracket __info, int __p)
		throws MLECallError
	{
		if (__info == null)
			throw new MLECallError("NARG");
		
		// {@squirreljme.error ZZ4m Invalid class property. (The property)}
		if (__p <= 0 || __p >= ClassProperty.NUM_RUNTIME_PROPERTIES)
			throw new MLECallError("ZZ4m " + __p);
		
		return LogicHandler.listRead(__info, __p);
	}
	
	/**
	 * Garbage collects the given handle.
	 * 
	 * @param __p The pointer to clear.
	 * @since 2020/11/28
	 */
	public static void gcMemHandle(int __p)
	{
		// TODO: Implement Garbage Collection of Memory Handles
		Assembly.ping();
		/*Assembly.breakpoint();
		throw Debugging.todo();*/
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
	public static boolean isClassInit(int __info)
	{
		if (__info == 0)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the given class.
	 * 
	 * @param __info The class info to initialize.
	 * @throws MLECallError If the class is {@code null}.
	 * @since 2020/11/28
	 */
	public static void initClass(TypeBracket __info)
		throws MLECallError
	{
		if (__info == null)
			throw new MLECallError("NARG");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
	
	/**
	 * Performs the same logic as {@link Class#isAssignableFrom(Class)}, 
	 * checks if the given class can be assigned to this one. The check is
	 * in the same order as {@code instanceof Object} that is
	 * {@code a.getClass().isAssignableFrom(b.getClass()) == (a instanceof b)}
	 * and {@code (Class<B>)a} does not throw {@link ClassCastException}.
	 * 
	 * @param __source The object to check.
	 * @param __target The target class to check.
	 * @param __quickCast State to store whether or not
	 * @return If this is an instance of the given class.
	 * @throws MLECallError On null arguments, except for
	 * {@code __quickCast}.
	 * @since 2020/11/28
	 */
	public static boolean isAssignableFrom(int __source, int __target,
		@SuppressWarnings("unused") QuickCastCheckBracket __quickCast)
		throws MLECallError
	{
		if (__source == 0 || __target == 0)
			throw new MLECallError("NARG");
		
		// Casting from one type to an array class?
		int ourDims = LogicHandler.typeBracketGetProperty(__source,
			StaticClassProperty.NUM_DIMENSIONS);
		int targetDims = LogicHandler.typeBracketGetProperty(__target,
			StaticClassProperty.NUM_DIMENSIONS);
		if (ourDims > 0 || targetDims > 0)
		{
			// Dimensional mismatch, this will generally not be compatible
			if (ourDims != targetDims)
			{
				// Are we casting from Foo[][]... to Object[]... or Object...?
				// We can lose dimensions but we cannot gain them
				if (0 != LogicHandler.typeBracketGetProperty(__target,
					StaticClassProperty.BOOLEAN_ROOT_IS_OBJECT))
					return targetDims < ourDims;
				
				// Not compatible
				return false;
			}
			
			// Since we are doing arrays, any array that has a compatible
			// root component can be casted into. So this adjusts the logic
			// accordingly
			return LogicHandler.isAssignableFrom(
				LogicHandler.typeBracketGetProperty(__source,
					ClassProperty.TYPEBRACKET_ROOT_COMPONENT),
				LogicHandler.typeBracketGetProperty(__target,
					ClassProperty.TYPEBRACKET_ROOT_COMPONENT),
				null);
		}
			
		// Check current and super classes for the class information
		for (int at = __source; at != 0;
			at = LogicHandler.typeBracketGetProperty(__source,
				ClassProperty.TYPEBRACKET_SUPER))
			if (at == __target)
				return true;
		
		// If not yet found, try all of the interfaces
		int allInts = LogicHandler.typeBracketGetProperty(__source,
			ClassProperty.TYPEBRACKET_ALL_INTERFACECLASSES);
		for (int i = 0, n = LogicHandler.listLength(allInts); i < n; i++)
			if (LogicHandler.listRead(allInts, i) == __target)
				return true;
		
		// Is not an instance
		return false;
	}
	
	/**
	 * Checks if this is an instance of the given class.
	 * 
	 * @param __o The object to check.
	 * @param __target The target class to check.
	 * @param __quickCast State to store whether or not
	 * @return If this is an instance of the given class.
	 * @throws MLECallError On null arguments, except for
	 * {@code __quickCast}.
	 * @since 2020/11/28
	 */
	public static boolean isInstance(int __o, int __target,
		@SuppressWarnings("unused") QuickCastCheckBracket __quickCast)
		throws MLECallError
	{
		if (__target == 0)
			throw new MLECallError("NARG");
		
		// Null objects are never an instance of anything
		if (__o == 0)
			return false;
		
		// Perform assignment check
		return LogicHandler.isAssignableFrom(LLETypeShelf.objectType(__o), __target, __quickCast);
	}
	
	/**
	 * Returns the length of the given list memory handle.
	 * 
	 * @param __o The list based memory handle to get the length of.
	 * @return The length of the list.
	 * @throws MLECallError On null arguments.
	 * @since 2021/02/07
	 */
	public static int listLength(int __o)
		throws MLECallError
	{
		if (__o == 0)
			throw new MLECallError("NARG");
			
		return Assembly.memHandleReadInt(__o, LogicHandler.staticVmAttribute(
			StaticVmAttribute.OFFSETOF_ARRAY_LENGTH_FIELD));
	}
	
	/**
	 * Reads from the given list based handle.
	 * 
	 * @param __o The handle to read from.
	 * @param __dx The index to read from.
	 * @return The value of the given index.
	 * @throws MLECallError If the handle is {@code null}.
	 * @since 2021/02/07
	 */
	public static int listRead(int __o, int __dx)
		throws MLECallError
	{
		if (__o == 0)
			throw new MLECallError("NARG");
		
		int arrayBase = SystemCall.arrayAllocationBase();
		return Assembly.memHandleReadInt(__o, arrayBase + (__dx * 4));
	}
	
	/**
	 * Reads from the given list based handle.
	 * 
	 * @param __o The handle to read from.
	 * @param __dx The index to read from.
	 * @return The value of the given index.
	 * @throws MLECallError If the handle is {@code null}.
	 * @since 2021/02/07
	 */
	public static int listRead(Object __o, int __dx)
		throws MLECallError
	{
		return LogicHandler.listRead(Assembly.objectToPointer(__o), __dx);
	}
	
	/**
	 * Reads from the given list based handle.
	 * 
	 * @param __o The handle to write to.
	 * @param __dx The index to write to.
	 * @param __v The value to write.
	 * @throws MLECallError If the handle is {@code null}.
	 * @since 2021/02/07
	 */
	public static void listWrite(int __o, int __dx, int __v)
		throws MLECallError
	{
		if (__o == 0)
			throw new MLECallError("NARG");
		
		int arrayBase = SystemCall.arrayAllocationBase();
		Assembly.memHandleWriteInt(__o, arrayBase + (__dx * 4), __v);
	}
	
	/**
	 * Reads from the given list based handle.
	 * 
	 * @param __o The handle to write to.
	 * @param __dx The index to write to.
	 * @param __v The value to write.
	 * @throws MLECallError If the handle is {@code null}.
	 * @since 2021/02/07
	 */
	public static void listWrite(Object __o, int __dx, int __v)
		throws MLECallError
	{
		LogicHandler.listWrite(Assembly.objectToPointer(__o), __dx, __v);
	}
	
	/**
	 * Allocates a new array
	 * 
	 * @param __info The class to allocate.
	 * @param __len The array length.
	 * @return The allocated object data.
	 * @throws NegativeArraySizeException If the array is negatively sized.
	 * @throws MLECallError On null arguments.
	 * @throws OutOfMemoryError If there is no memory remaining.
	 * @since 2020/11/29
	 */
	public static Object newArray(TypeBracket __info, int __len)
		throws NegativeArraySizeException, MLECallError,
			OutOfMemoryError
	{
		if (__info == null)
			throw new MLECallError("NARG");
		
		if (__len < 0)
			throw new NegativeArraySizeException("" + __len);
		
		// Determine how large the object needs to be
		int allocBase = LogicHandler.typeBracketGetProperty(__info,
			ClassProperty.SIZE_ALLOCATION);
		int allocSize = allocBase +
			(__len * LogicHandler.typeBracketGetProperty(__info,
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
	 * @throws MLECallError On null arguments.
	 * @throws OutOfMemoryError If there is no memory remaining.
	 * @since 2020/11/29
	 */
	public static Object newInstance(TypeBracket __info)
		throws MLECallError, OutOfMemoryError
	{
		if (__info == null)
			throw new MLECallError("NARG");
		
		// {@squirreljme.error ZZ4j Class has no allocated size?}
		int allocSize = LogicHandler.typeBracketGetProperty(__info,
			ClassProperty.SIZE_ALLOCATION);
		if (allocSize <= 0)
			throw new MLECallError("ZZ4j");
		
		// Allocate the object
		return LogicHandler.__allocObject(__info, allocSize);
	}
	
	/**
	 * This is the method that is called for all native and abstract methods
	 * within the virtual machine so that every method leads somewhere.
	 * 
	 * @throws PureVirtualMethodCallError Always.
	 * @since 2021/01/30
	 */
	public static void pureVirtualCall()
		throws PureVirtualMethodCallError
	{
		// {@squirreljme.error ZZ40 Pure virtual method call.}
		throw new PureVirtualMethodCallError("ZZ4o");
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
		
		return LogicHandler.listRead(SystemCall.staticVmAttributes(), __attr);
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
	private static Object __allocObject(TypeBracket __info,
		int __allocSize)
		throws NullPointerException, OutOfMemoryError
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		// This represents the kind of handle that gets allocated
		int memHandleKind = LogicHandler.typeBracketGetProperty(__info,
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
