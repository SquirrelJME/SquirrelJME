// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.StackMapTablePairs;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * This maps multiple {@link VariablePlacement} for each instruction address
 * to individual mappings.
 *
 * @see VariablePlacement
 * @see VariablePlacements
 * @since 2023/08/09
 */
public class VariablePlacementMap
{
	/**
	 * Initializes the variable placements.
	 *
	 * @param __isStatic Is this a static method?
	 * @param __type The method type, used for the input arguments.
	 * @param __maxLocals The maximum number of local variables.
	 * @param __maxStack The maximum number of stack variables.
	 * @param __stackMap The input stack map table.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public VariablePlacementMap(boolean __isStatic, MethodDescriptor __type,
		int __maxLocals, int __maxStack, StackMapTablePairs __stackMap)
		throws NullPointerException
	{
		if (__stackMap == null)
			throw new NullPointerException("NARG");
		
		// Setup array with maximum total number of entries
		int maxTotal = __maxLocals + __maxStack;
		VariablePlacement[] placements = new VariablePlacement[maxTotal];
		
		// If this is a non-static method, then the first argument is always
		// implicitly an object, which then means that all invocations are
		// treated as if they were static
		if (true)
			throw Debugging.todo();
		
		// The first reference after any argument is always the thrown
		// variable, even if the method throws nothing and has a zero-size
		// stack
		if (true)
			throw Debugging.todo();
		  
		// Need to map for each address
		for (int address : __stackMap.addresses())
		{
			throw Debugging.todo();
		}
		
		// Store mapping
		throw Debugging.todo();
	}
	
	public VariableLimits limits()
	{
		throw Debugging.todo();
	}
	
	/**
	 * The variable used for values which are thrown.
	 *
	 * @return The index where thrown variables are placed.
	 * @since 2023/08/09
	 */
	public int thrownVariableIndex()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Calculates the type order that the method uses.
	 *
	 * @param __isStatic Is this a static method?
	 * @param __type The method argument type.
	 * @return The primitive types that make up the method.
	 * @throws NullPointerException
	 * @since 2023/08/09
	 */
	public static List<JvmPrimitiveType> methodArguments(boolean __isStatic,
		MethodDescriptor __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		List<JvmPrimitiveType> result = new ArrayList<>(
			(__isStatic ? 1 : 0) + __type.argumentCount());
		
		// Non-static includes this
		if (!__isStatic)
			result.add(JvmPrimitiveType.OBJECT);
		
		// Process arguments
		for (FieldDescriptor arg : __type.arguments())
			result.add(JvmPrimitiveType.of(arg));
		
		return UnmodifiableList.of(result);
	}
}
