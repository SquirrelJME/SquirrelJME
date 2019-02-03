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

/**
 * This represents the type of memory something uses.
 *
 * @since 2019/02/03
 */
public enum MemoryType
{
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Pointer. */
	POINTER,
	
	/** End. */
	;
	
	/**
	 * Returns the memory type of the given field.
	 *
	 * @param __f The input field.
	 * @return The memory type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/03
	 */
	public static MemoryType of(FieldReference __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		return MemoryType.of(__f.memberType());
	}
	
	/**
	 * Returns the memory type of the given field.
	 *
	 * @param __f The input field.
	 * @return The memory type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/03
	 */
	public static MemoryType of(FieldDescriptor __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// If an object, treat as pointer
		if (__f.isObject())
			return POINTER;
		
		throw new todo.TODO();
	}
}

