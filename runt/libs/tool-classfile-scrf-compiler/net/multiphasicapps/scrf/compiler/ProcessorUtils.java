// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.scrf.MemoryType;

/**
 * This contains utilities that can be used for processing.
 *
 * @since 2019/02/05
 */
public final class ProcessorUtils
{
	/**
	 * Not used.
	 *
	 * @since 2019/02/05
	 */
	private ProcessorUtils()
	{
	}
	
	/**
	 * Returns the memory type of the given field.
	 *
	 * @param __f The field to get the type of.
	 * @return The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/05
	 */
	public static final MemoryType memoryTypeOf(FieldReference __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		return ProcessorUtils.memoryTypeOf(__f.memberType());
	}
	
	/**
	 * Returns the memory type of the given field.
	 *
	 * @param __f The field to get the type of.
	 * @return The type of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/05
	 */
	public static final MemoryType memoryTypeOf(FieldDescriptor __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Pointer Types
		if (__f.isObject())
			return MemoryType.POINTER;
		
		// Primitives otherwise
		switch (__f.primitiveType())
		{
			case BYTE:
				return MemoryType.BYTE;
			
			case SHORT:
			case CHARACTER:
				return MemoryType.SHORT;
			
			case INTEGER:
			case FLOAT:
				return MemoryType.INTEGER;
			
			case LONG:
			case DOUBLE:
				return MemoryType.LONG;
			
			default:
				throw new todo.OOPS(__f.toString());
		}
	}
}

