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

import java.util.ArrayList;
import java.util.List;
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
	
	/** Instructions which exist in the block. */
	private final List<HLO> _ops =
		new ArrayList<HLO>();
	
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
	 * Appends the given operation.
	 *
	 * @param __op The operation to append.
	 * @throws JITException If it could not be appended.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/01
	 */
	public final void append(HLO __op)
		throws JITException, NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Add
		List<HLO> ops = this._ops;
		ops.add(__op);
		
		// Debug
		System.err.printf("DEBUG -- Append: %s%n", __op);
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
		
		append(new HLOCopy(__src, __dest));
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
		
		append(new HLOCountReference(__v, __up));
	}
	
	/**
	 * Returns the number of operations in this block.
	 *
	 * @return The number of operations in thiss block.
	 * @since 2017/09/01
	 */
	public final int size()
	{
		return this._ops.size();
	}
}

