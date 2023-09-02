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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.StackMapTableEntry;
import net.multiphasicapps.classfile.StackMapTablePairs;
import net.multiphasicapps.classfile.StackMapTableState;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This maps multiple {@link VariablePlacement} for each instruction address
 * to individual mappings.
 *
 * @see VariablePlacement
 * @since 2023/08/09
 */
public class VariablePlacementMap
{
	/** Placements for variables. */
	protected final Map<VariablePlacement, VariablePlacement> toNano;
	
	/** Variable limits. */
	protected final VariableLimits limits;
	
	/** The thrown variable index. */
	protected final int thrownVariableIndex;
	
	
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
		Map<VariablePlacement, VariablePlacement> placements =
			new LinkedHashMap<>();
		int[] typeCounts = new int[JvmPrimitiveType.NUM_JAVA_TYPES];
		
		// If this is a non-static method, then the first argument is always
		// implicitly an object, which then means that all invocations are
		// treated as if they were static
		int usedLocals = 0;
		if (!__isStatic)
			VariablePlacementMap.__seed(placements, typeCounts,
				JvmPrimitiveType.OBJECT, usedLocals++);
		
		// Load in arguments following
		int numArgs = __type.argumentCount();
		for (int i = 0; i < numArgs; i++)
			VariablePlacementMap.__seed(placements, typeCounts,
				JvmPrimitiveType.of(__type.argument(i)).javaType(),
				usedLocals++);
		
		// The first reference after any argument is always the thrown
		// variable, even if the method throws nothing and has a zero-size
		// stack
		this.thrownVariableIndex = VariablePlacementMap.__seed(placements,
			typeCounts, JvmPrimitiveType.OBJECT,
			__maxLocals + __maxStack).index;
		  
		// Need to map for each address all the possible stack states, both
		// input and output
		for (int address : __stackMap.addresses())
			for (StackMapTableState state : __stackMap.get(address))
			{
				int maxTotal = __maxLocals + state.depth();
				for (int i = 0; i < maxTotal; i++)
				{
					// Get entry
					StackMapTableEntry entry = (i < __maxLocals ?
						state.getLocal(i) :
						state.getStack(i - __maxLocals));
					
					// Ignore nothing and top variables
					if (entry.isTop() || entry.isNothing())
						continue;
					
					// Load it in
					VariablePlacementMap.__seed(placements, typeCounts,
						JvmPrimitiveType.of(entry.type()).javaType(), i);
				}
			}
		
		// Store placements
		this.toNano = UnmodifiableMap.of(placements);
		this.limits = new VariableLimits(typeCounts);
	}
	
	/**
	 * Returns a placement from Java. 
	 *
	 * @param __type The type used.
	 * @param __index The index of the local.
	 * @return The NanoCoat placement.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public VariablePlacement toNano(JvmPrimitiveType __type, int __index)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the limits for this placement map.
	 *
	 * @return The placement map limits.
	 * @since 2023/08/13
	 */
	public VariableLimits limits()
	{
		return this.limits;
	}
	
	/**
	 * The variable used for values which are thrown.
	 *
	 * @return The index where thrown variables are placed.
	 * @since 2023/08/09
	 */
	public int thrownVariableIndex()
	{
		return this.thrownVariableIndex;
	}
	
	/**
	 * Returns a placement from Java.
	 *
	 * @param __placements The target placements.
	 * @param __typeCounts The current type counts.
	 * @param __type The type used.
	 * @param __index The index of the local.
	 * @return The NanoCoat placement.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	private static VariablePlacement __seed(
		Map<VariablePlacement, VariablePlacement> __placements,
		int[] __typeCounts, JvmPrimitiveType __type, int __index)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__placements == null || __typeCounts == null || __type == null)
			throw new NullPointerException("NARG");
		if (__index < 0)
			throw new IndexOutOfBoundsException("NEGV");
		
		// Build key
		VariablePlacement key = new VariablePlacement(__type, __index);
		
		// Has this already been determined?
		VariablePlacement result = __placements.get(key);
		if (result != null)
			return result;
		
		// Setup target mapping
		result = new VariablePlacement(__type,
			__typeCounts[__type.ordinal()]++);
		__placements.put(key, result);
		
		// Debug
		Debugging.debugNote("VarMap %s -> %s", key, result);
		
		// Use target result
		return result;
	}
}
