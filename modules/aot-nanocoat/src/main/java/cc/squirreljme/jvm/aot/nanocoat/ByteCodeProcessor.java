// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTargets;

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
	
	/** Address to basic block group IDs. */
	private final Map<Integer, Integer> _addrToGroupId =
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
		
		// Reverse jump targets for instructions
		Map<Integer, InstructionJumpTargets> reverseJumpsTable =
			__code.reverseJumpTargets();
		
		// Break down function into basic blocks
		Map<Integer, BasicBlock> basicBlocks = this._basicBlocks;
		Map<Integer, Integer> addrToGroupId = this._addrToGroupId;
		List<Instruction> blockInstructions = new ArrayList<>();
		boolean lastSingular = false;
		for (int logicalAddr = 0, maxLogicalAddr = __code.instructionCount();
			logicalAddr <= maxLogicalAddr; logicalAddr++)
		{
			Instruction instruction = null;
			boolean doSplit;
			boolean singular = false;
			
			// Always split after every instruction to force a new block
			if (logicalAddr == maxLogicalAddr)
				doSplit = true;
			
			// Otherwise depends on the instruction
			else
			{
				// Get instruction
				instruction = __code.getByIndex(logicalAddr);
				int physicalAddr = instruction.address();
				int op = instruction.operation();
				
				// Are these singular instructions
				switch (op)
				{
						// Method invocation, can go to other pieces of code
					case InstructionIndex.INVOKEINTERFACE:
					case InstructionIndex.INVOKEDYNAMIC:
					case InstructionIndex.INVOKESPECIAL:
					case InstructionIndex.INVOKESTATIC:
					case InstructionIndex.INVOKEVIRTUAL:
						
						// Jumping to other parts in code
					case InstructionIndex.LOOKUPSWITCH:
					case InstructionIndex.TABLESWITCH:
					case InstructionIndex.GOTO:
					case InstructionIndex.GOTO_W:
					case InstructionIndex.RETURN:
					case InstructionIndex.ARETURN:
					case InstructionIndex.IRETURN:
					case InstructionIndex.LRETURN:
					case InstructionIndex.FRETURN:
					case InstructionIndex.DRETURN:
						
						// These do toss exceptions
					case InstructionIndex.ATHROW:
						
						// These can toss exceptions
					case InstructionIndex.ARRAYLENGTH:
					case InstructionIndex.CHECKCAST:
					case InstructionIndex.GETFIELD:
					case InstructionIndex.PUTFIELD:
					case InstructionIndex.NEW:
					case InstructionIndex.NEWARRAY:
					case InstructionIndex.ANEWARRAY:
					case InstructionIndex.MULTIANEWARRAY:
						
						// Monitors can gain or lose control
					case InstructionIndex.MONITORENTER:
					case InstructionIndex.MONITOREXIT:
						
						// Software math conversion
					case InstructionIndex.I2F:
					case InstructionIndex.I2D:
					case InstructionIndex.I2L:
					case InstructionIndex.L2I:
					case InstructionIndex.L2F:
					case InstructionIndex.L2D:
					case InstructionIndex.F2I:
					case InstructionIndex.F2L:
					case InstructionIndex.F2D:
					case InstructionIndex.D2I:
					case InstructionIndex.D2L:
					case InstructionIndex.D2F:
						
						// Software long operations
					case InstructionIndex.LADD:
					case InstructionIndex.LAND:
					case InstructionIndex.LCMP:
					case InstructionIndex.LDIV:
					case InstructionIndex.LMUL:
					case InstructionIndex.LNEG:
					case InstructionIndex.LOR:
					case InstructionIndex.LREM:
					case InstructionIndex.LSHL:
					case InstructionIndex.LSHR:
					case InstructionIndex.LSUB:
					case InstructionIndex.LUSHR:
					case InstructionIndex.LXOR:
						
						// Software float operations
					case InstructionIndex.FADD:
					case InstructionIndex.FCMPG:
					case InstructionIndex.FCMPL:
					case InstructionIndex.FDIV:
					case InstructionIndex.FMUL:
					case InstructionIndex.FNEG:
					case InstructionIndex.FREM:
					case InstructionIndex.FSUB:
						
						// Software double operations
					case InstructionIndex.DADD:
					case InstructionIndex.DCMPG:
					case InstructionIndex.DCMPL:
					case InstructionIndex.DDIV:
					case InstructionIndex.DMUL:
					case InstructionIndex.DNEG:
					case InstructionIndex.DREM:
					case InstructionIndex.DSUB:
						
						// Array access
					case InstructionIndex.CASTORE:
					case InstructionIndex.CALOAD:
					case InstructionIndex.BALOAD:
					case InstructionIndex.BASTORE:
					case InstructionIndex.AASTORE:
					case InstructionIndex.AALOAD:
					case InstructionIndex.DALOAD:
					case InstructionIndex.DASTORE:
					case InstructionIndex.FALOAD:
					case InstructionIndex.FASTORE:
					case InstructionIndex.IASTORE:
					case InstructionIndex.IALOAD:
					case InstructionIndex.LASTORE:
					case InstructionIndex.LALOAD:
					case InstructionIndex.SALOAD:
					case InstructionIndex.SASTORE:
						
						// ...
						singular = true;
						break;
				}
				
				// If this instruction jumps to itself, comes from a later
				// event, has exception based jumps, comes from multiple
				// address points, or are certain instructions
				// then split the block
				InstructionJumpTargets reverseJumps = reverseJumpsTable.get(
					physicalAddr);				
				doSplit = reverseJumps != null &&
					(reverseJumps.hasSameOrLaterAddress(physicalAddr) ||
					reverseJumps.hasAnyException() ||
					reverseJumps.countNormal() >= 2 ||
					singular);
			}
			
			// Only split if there are actual instructions to split
			if ((doSplit || lastSingular) && !blockInstructions.isEmpty())
			{
				// Each block has a specific group Id
				int groupId = basicBlocks.size();
				
				// If we jump to another block, we need to know where to go
				addrToGroupId.put(blockInstructions.get(0).address(), groupId);
				
				// Setup block and clear
				BasicBlock block = new BasicBlock(groupId,
					blockInstructions);
				basicBlocks.put(groupId, block);
				blockInstructions.clear();
			}
			
			// Only add instructions after the block
			if (instruction != null)
				blockInstructions.add(instruction);
			
			// Last instruction was singular?
			lastSingular = singular;
		}
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
		
		// Put each instruction on its own line
		CSourceWriter writer = __block.writer();
		writer.freshLine();
		
		// Depends on the target operation
		writer.tokens("/*", __instruction.mnemonic(), "*/");
		/*switch (__instruction.operation())
		{
			default:
				throw Debugging.todo(__instruction);
		}*/
	}
}
