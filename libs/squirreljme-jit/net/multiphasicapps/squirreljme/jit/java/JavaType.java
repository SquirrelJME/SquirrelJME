// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This represents a type within the stack map table which maps to a type used
 * within the running virtual machine. Types in the stack map will usually be
 * initialized but they can be uninitialized in the event of {@code new}
 * operations being performed.
 *
 * @since 2017/07/26
 */
public final class JavaType
{
	/** The type this refers to. */
	protected final FieldDescriptor type;
	
	/** Is this type initialized? */
	protected final boolean isinitialized;
	
	/**
	 * Initializes the stack map type.
	 *
	 * @param __cn The name of the field.
	 * @param __init Is this object initialized?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public JavaType(ClassName __cn, boolean __init)
		throws NullPointerException
	{
		this(new FieldDescriptor((__cn.isArray() ? __cn.toString() :
			"L" + __cn + ";")), __init);
	}
	
	/**
	 * Initializes the stack map type.
	 *
	 * @param __f The field type.
	 * @param __init Is the value initialized.
	 * @throws IllegalStateException If an uninitialized primitive type is
	 * used.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public JavaType(FieldDescriptor __f, boolean __init)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Promote to integer since the VM does not have a representation for
		// types smaller than int
		if (__f.equals(FieldDescriptor.BOOLEAN) ||
			__f.equals(FieldDescriptor.BYTE) ||
			__f.equals(FieldDescriptor.SHORT) ||
			__f.equals(FieldDescriptor.CHARACTER))
			__f = FieldDescriptor.INTEGER;
		
		// {@squirreljme.error JI1s Cannot have a primitive type which is not
		// initialized.}
		if (__f.isPrimitive() && !__init)
			throw new IllegalStateException("JI1s");
		
		// Set
		this.type = __f;
		this.isinitialized = __init;
	}
}

