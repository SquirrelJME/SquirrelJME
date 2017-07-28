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
public final class StackMapType
{
	/** The class name if this has one. */
	protected final ClassName name;
	
	/** Is this type initialized? */
	protected final boolean isinitialized;
	
	/**
	 * Initializes the stack map type.
	 *
	 * @param __cn The name of the associated class.
	 * @param __init Is this object initialized?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public StackMapType(ClassName __cn, boolean __init)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __cn;
		this.isinitialized = __init;
	}
}

