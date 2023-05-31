// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Instruction;

/**
 * This processes the byte code of a method.
 *
 * @since 2023/05/31
 */
public class ByteCodeProcessor
{
	/** The method code. */
	protected final ByteCode code;
	
	/** The processor this is in. */
	protected final Reference<MethodProcessor> methodProcessor;
	
	/** Basic block mappings. */
	private final Map<Integer, BasicBlock> _basicBlocks =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the byte code processor.
	 * 
	 * @param __method The method processor.
	 * @param __code The code being processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public ByteCodeProcessor(MethodProcessor __method, ByteCode __code)
		throws NullPointerException
	{
		if (__method == null || __code == null)
			throw new NullPointerException("NARG");
		
		this.methodProcessor = new WeakReference<>(__method);
		this.code = __code;
	}
	
	/**
	 * Processes writing of the C function.
	 * 
	 * @param __block The block to write into.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public void process(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		// Switch case based on the current group index
		try (CSwitch cases = __block.switchCase(
			"state->top->groupIndex"))
		{
			// Write each basic block
			for (Map.Entry<Integer, BasicBlock> entry :
				this._basicBlocks.entrySet())
			{
				// Setup next case
				BasicBlock basicBlock = entry.getValue();
				cases.nextCase(entry.getKey());
				
				// Write all instructions in the block
				for (Instruction instruction : basicBlock.instructions())
					this.processInstruction(cases, instruction);
				
				// Do not fall through
				cases.breakCase();
			}
		}
	}
	
	/**
	 * Processes instructions.
	 *
	 * @param __block The block to write code into.
	 * @param __instruction The instruction to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	private void processInstruction(CFunctionBlock __block,
		Instruction __instruction)
		throws IOException, NullPointerException
	{
		if (__block == null || __instruction == null)
			throw new NullPointerException("NARG");
		
		// Depends on the target operation
		switch (__instruction.operation())
		{
			default:
				throw Debugging.todo(__instruction);
		}
	}
}
