// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.hil;

import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class represents a high level basic block which contains instructions
 * and a sole entry point from other basic blocks.
 *
 * @since 2017/08/25
 */
public class HighLevelBlock
{
	/** The key which this block is associated with. */
	protected final BasicBlockKey key;
	
	/**
	 * Initializes the high level basic block.
	 *
	 * @param __key The key this block is associated with.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/25
	 */
	public HighLevelBlock(BasicBlockKey __key)
		throws NullPointerException
	{
		// Check
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.key = __key;
	}
	
	/**
	 * Appends a copy operation from one variable to another.
	 *
	 * @param __src The source variable.
	 * @param __dest The destination variable.
	 * @throws JITException If the copy operation could not be appended.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/26
	 */
	public final void appendCopy(TypedVariable __src, Variable __dest)
		throws JITException, NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Appends a reference count of the given object.
	 *
	 * @param __v The variable to count.
	 * @param __up If {@code true} then the reference is counted up.
	 * @throws JITException If the variable is not an object.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/26
	 */
	public final void appendCountReference(TypedVariable __v, boolean __up)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

