// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Basic block within the byte code.
 *
 * @since 2023/05/31
 */
public class BasicBlock
{
	/** The instructions in this block. */
	protected final List<Instruction> instructions;
	
	/** The group block ID. */
	protected final int groupId;
	
	/**
	 * Initializes the basic block.
	 * 
	 * @param __groupId The group ID.
	 * @param __instructions The instructions in the block.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public BasicBlock(int __groupId, List<Instruction> __instructions)
		throws NullPointerException
	{
		if (__instructions == null || __instructions.isEmpty())
			throw new NullPointerException("NARG");
		
		this.groupId = __groupId;
		this.instructions = UnmodifiableList.of(
			new ArrayList<>(__instructions));
	}
	
	/**
	 * Returns the instructions within the block.
	 * 
	 * @return The instructions within the block.
	 * @since 2023/05/31
	 */
	public List<Instruction> instructions()
	{
		return this.instructions;
	}
}
