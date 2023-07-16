// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CComparison;
import cc.squirreljme.c.CExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFunctionBlock;
import cc.squirreljme.c.CFunctionBlockSplices;
import cc.squirreljme.c.CIfBlock;
import cc.squirreljme.c.CMathOperator;
import cc.squirreljme.c.CPointerType;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CSwitchBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLinkTable;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Container;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Linkage;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InstructionJumpTargets;
import net.multiphasicapps.classfile.IntMatchingJumpTable;
import net.multiphasicapps.classfile.JavaStackShuffleType;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.classfile.StackMapTablePair;
import net.multiphasicapps.classfile.StackMapTablePairs;

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
	
	/** The method being processed. */
	protected final Method method;
	
	/** The link table for the class. */
	protected final ClassLinkTable linkTable;
	
	/** The stack map table at runtime. */
	protected final StackMapTablePairs stackMap;
	
	/** Basic block mappings. */
	private final Map<Integer, BasicBlock> _basicBlocks =
		new SortedTreeMap<>();
	
	/** Address to basic block group IDs. */
	private final Map<Integer, Integer> _addrToGroupId =
		new SortedTreeMap<>();
	
	/** Code variables for writing. */
	private volatile __CodeVariables__ _codeVars;
	
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
		this.method = __method.method;
		this.linkTable = __method.linkTable;
		this.stackMap = __code.stackMapTableFull();
		
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
				instruction = __code.getByIndex(logicalAddr).normalize();
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
					case InstructionIndex.IF_ACMPEQ:
					case InstructionIndex.IF_ACMPNE:
					case InstructionIndex.IF_ICMPEQ:
					case InstructionIndex.IF_ICMPGE:
					case InstructionIndex.IF_ICMPGT:
					case InstructionIndex.IF_ICMPLE:
					case InstructionIndex.IF_ICMPLT:
					case InstructionIndex.IF_ICMPNE:
					case InstructionIndex.IFEQ:
					case InstructionIndex.IFGE:
					case InstructionIndex.IFGT:
					case InstructionIndex.IFLE:
					case InstructionIndex.IFLT:
					case InstructionIndex.IFNE:
					case InstructionIndex.IFNONNULL:
					case InstructionIndex.IFNULL:
						
						// Returning from method
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
		
		// Splice method into three parts...
		// intro, where variables go
		// middle, where the giant switch block goes
		// outro, where the end stuff goes to clean up accordingly
		try (CFunctionBlockSplices splices = __block.splice(3))
		{
			// Write initial top variables
			try (CFunctionBlock initVars = splices.splice(0))
			{
				// Setup code variables where the variables get defined and
				// such
				__CodeVariables__ codeVars = new __CodeVariables__(initVars);
				this._codeVars = codeVars;
				
				// Keep track of the current top state, so we need not worry
				// about pushing or popping
				initVars.declare(codeVars.currentFrame());
				
				// Set known variables
				initVars.variableSet(codeVars.currentFrame(),
					CExpressionBuilder.builder()
						.identifier(codeVars.currentThread())
						.dereferenceStruct()
						.identifier(JvmTypes.VMTHREAD.type(CStructType.class)
							.member("top"))
						.build());
			}
			
			// Middle splice is the main body of the function
			try (CFunctionBlock body = splices.splice(1))
			{
				__CodeVariables__ codeVars = this.__codeVars();
				
				// Switch case based on the current group index
				try (CSwitchBlock cases = body.switchCase(
					CExpressionBuilder.builder()
						.identifier(codeVars.currentFrame())
						.dereferenceStruct()
						.identifier(JvmTypes.VMFRAME.type(CStructType.class)
							.member("groupIndex"))
						.build()))
				{
					// Start of function call, initializes accordingly
					cases.nextCase(Constants.SJME_NANOCOAT_START_CALL);
					
					// If synchronized, lock on monitor implicitly here
					Method method = this.method;
					if (method.flags().isSynchronized())
						throw Debugging.todo();
					
					// --- Initialization ---
					// Return here so the initialization does get complete
					body.returnValue(Constants.TRUE);
						
					// Return from call when execution of method finishes
					cases.nextCase(Constants.SJME_NANOCOAT_END_CALL);
				
					// --- Exit ---
					// If synchronized, unlock on monitor implicitly here
					if (method.flags().isSynchronized())
						throw Debugging.todo();
					
					// Now return
					body.returnValue(Constants.FALSE);
					
					// --- Instruction Blocks ---
					// Write each basic block
					for (Map.Entry<Integer, BasicBlock> entry :
						this._basicBlocks.entrySet())
					{
						// Setup next case
						BasicBlock basicBlock = entry.getValue();
						cases.nextCase(entry.getKey());
						
						// Write all instructions in the block
						for (Instruction instruction :
							basicBlock.instructions())
						{
							// Clear temporary count
							codeVars._numTemporaries = 0;
							
							// Handle instruction
							this.processInstruction(cases, instruction);
							
							// If there are temporaries, clear them out
							if (codeVars._numTemporaries > 0)
								cases.functionCall(
									JvmFunctions.NVM_TEMP_DISCARD,
									codeVars.currentFrame());
						}
						
						// Do not fall through
						cases.breakCase();
					}
				}
			}
			
			// Declare local temporary variables
			__CodeVariables__ codeVars = this.__codeVars();
			if (codeVars.maxTemporaries() > 0)
				try (CFunctionBlock initVars = splices.splice(0))
				{
					initVars.declare(CVariable.of(
						JvmTypes.ANY.type().arrayType(
							codeVars.maxTemporaries()),
						Constants.TEMPORARY));
				}
			
			// End outer base method with a throw check always
			try (CFunctionBlock outroBlock = splices.splice(2))
			{
				// This is the outro of the method
				outroBlock.label(Constants.OUTRO_LABEL);
				
				// Always check throwing
				this.__checkThrow(outroBlock);
				
				// Return true from this call instance
				outroBlock.returnValue(Constants.TRUE);
				
				// Exception handler implementation, keeps it together
				outroBlock.label(Constants.THROW_HANDLER_LABEL);
				this.__handleThrow(outroBlock);
				
				// Exception was thrown, so return false to indicate it
				outroBlock.returnValue(Constants.FALSE);
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
	public void processInstruction(CFunctionBlock __block,
		Instruction __instruction)
		throws IOException, NullPointerException
	{
		if (__block == null || __instruction == null)
			throw new NullPointerException("NARG");
		
		// Stack state, used for some operations
		StackMapTablePair stackPair = this.stackMap.get(
			__instruction.address());
		
		// Depends on the target operation
		int op = __instruction.operation();
		switch (op)
		{
			case InstructionIndex.ATHROW:
				this.__doAThrow(__block);
				break;
			
			case InstructionIndex.POP:
			case InstructionIndex.POP2:
			case InstructionIndex.DUP:
			case InstructionIndex.DUP_X1:
			case InstructionIndex.DUP_X2:
			case InstructionIndex.DUP2:
			case InstructionIndex.DUP2_X1:
			case InstructionIndex.DUP2_X2:
			case InstructionIndex.SWAP:
				this.__doStackShuffle(__block,
					JavaStackShuffleType.ofOperation(op)
						.findShuffleFunction(stackPair.input));
				break;
				
			case InstructionIndex.LDC_W:
			case InstructionIndex.LDC2_W:
				this.__doLdc(__block,
					__instruction.argument(0, ConstantValue.class));
				break;
				
			case InstructionIndex.IFNULL:
			case InstructionIndex.IFNONNULL:
				this.__doIfMaybeNull(__block,
					op == InstructionIndex.IFNULL,
					this.__addressToGroup(__instruction, 0));
				break;
				
			case InstructionIndex.IFEQ:
			case InstructionIndex.IFNE:
			case InstructionIndex.IFLT:
			case InstructionIndex.IFLE:
			case InstructionIndex.IFGT:
			case InstructionIndex.IFGE:
				this.__doIfZeroInt(__block,
					ByteCodeProcessor.__commonCompareIf(op),
					this.__addressToGroup(__instruction, 0));
				break;
			
			case InstructionIndex.IF_ICMPEQ:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IF_ICMPLT:
			case InstructionIndex.IF_ICMPLE:
			case InstructionIndex.IF_ICMPGT:
			case InstructionIndex.IF_ICMPGE:
				this.__doIfCmpInt(__block,
					ByteCodeProcessor.__commonCompareIf(op),
					this.__addressToGroup(__instruction, 0));
				break;
				
			case InstructionIndex.INVOKESPECIAL:
				this.__doInvokeSpecial(__block,
					__instruction.argument(0, MethodReference.class));
				break;
				
			case InstructionIndex.INVOKEINTERFACE:
			case InstructionIndex.INVOKESTATIC:
			case InstructionIndex.INVOKEVIRTUAL:
				this.__doInvokeNormal(__block,
					op == InstructionIndex.INVOKESTATIC,
					__instruction.argument(0, MethodReference.class));
				break;
				
			case InstructionIndex.ANEWARRAY:
				this.__doNewArray(__block,
					__instruction.argument(0, ClassName.class));
				break;
			
			case InstructionIndex.NEW:
				this.__doNew(__block,
					__instruction.argument(0, ClassName.class));
				break;
				
			case InstructionIndex.NEWARRAY:
				this.__doNewArray(__block,
					__instruction.argument(0, PrimitiveType.class)
						.toClassName());
				break;
				
			case InstructionIndex.RETURN:
				this.__doReturn(__block);
				break;
				
			case InstructionIndex.IRETURN:
				this.__doReturnValue(__block);
				break;
				
			case InstructionIndex.WIDE_ALOAD:
				this.__doALoadStore(__block, false,
					__instruction.intArgument(0));
				break;
				
			case InstructionIndex.WIDE_ASTORE:
				this.__doALoadStore(__block, true,
					__instruction.intArgument(0));
				break;
				
			case InstructionIndex.WIDE_ILOAD:
				this.__doILoadStore(__block, false,
					__instruction.intArgument(0));
				break;
				
			case InstructionIndex.WIDE_ISTORE:
				this.__doILoadStore(__block, true,
					__instruction.intArgument(0));
				break;
				
				// Integer math
			case InstructionIndex.IADD:
			case InstructionIndex.ISUB:
			case InstructionIndex.IMUL:
			case InstructionIndex.IDIV:
			case InstructionIndex.IREM:
			case InstructionIndex.IAND:
			case InstructionIndex.IOR:
			case InstructionIndex.IXOR:
				this.__doMathInteger(__block,
					ByteCodeProcessor.__commonMathOp(op));
				break;
				
				// Bit shift
			case InstructionIndex.ISHL:
			case InstructionIndex.ISHR:
			case InstructionIndex.IUSHR:
				throw Debugging.todo();
				
				// Integer Negative
			case InstructionIndex.INEG:
				throw Debugging.todo();
				
				// Lookup/table switch
			case InstructionIndex.LOOKUPSWITCH:
			case InstructionIndex.TABLESWITCH:
				this.__doJumpTable(__block,
					__instruction.argument(0, IntMatchingJumpTable.class));
				break;
				
				// Direct jump
			case InstructionIndex.GOTO_W:
				this.__jumpToGroup(__block,
					this.__addressToGroup(__instruction, 0));
				break;
				
				// Put field
			case InstructionIndex.GETFIELD:
			case InstructionIndex.PUTFIELD:
				this.__doFieldGetPut(__block,
					__instruction.argument(0, FieldReference.class),
					(op == InstructionIndex.PUTFIELD));
				break;
				
				// Store or load primitive from array
			case InstructionIndex.BALOAD:
			case InstructionIndex.SALOAD:
			case InstructionIndex.CALOAD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.DALOAD:
			case InstructionIndex.BASTORE:
			case InstructionIndex.SASTORE:
			case InstructionIndex.CASTORE:
			case InstructionIndex.IASTORE:
			case InstructionIndex.LASTORE:
			case InstructionIndex.FASTORE:
			case InstructionIndex.DASTORE:
				this.__doPrimitiveArrayAccess(__block,
					ByteCodeProcessor.__commonPrimitive(op),
					ByteCodeProcessor.__commonStoreLoad(op));
				break;
				
				// Length of an array
			case InstructionIndex.ARRAYLENGTH:
				this.__doArrayLength(__block);
				break;
			
			default:
				throw Debugging.todo(__instruction);
		}
	}
	
	/**
	 * Returns a group ID for the given instruction jump table.
	 * 
	 * @param __instruction The target instruction.
	 * @param __jumpId The jump ID in the jump table.
	 * @return The group ID for the instruction.
	 * @since 2023/07/04
	 */
	private int __addressToGroup(Instruction __instruction, int __jumpId)
		throws NullPointerException
	{
		if (__instruction == null)
			throw new NullPointerException("NARG");
		
		return this.__addressToGroup(
			__instruction.jumpTargets().get(__jumpId).target());
	}
	
	/**
	 * Returns a group ID for the given target address.
	 * 
	 * @param __target The target address.
	 * @return The group ID for the instruction.
	 * @since 2023/07/04
	 */
	private int __addressToGroup(int __target)
	{
		return this._addrToGroupId.get(__target);
	}
	
	/**
	 * Returns the code variables for the processor.
	 * 
	 * @return The code variables.
	 * @since 2023/07/05
	 */
	private __CodeVariables__ __codeVars()
	{
		__CodeVariables__ rv = this._codeVars;
		if (rv == null)
			throw Debugging.oops();
		return rv;
	}
	
	/**
	 * Checks if an exception was thrown.
	 * 
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	private void __checkThrow(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Check if there is something waiting to be thrown
		try (CIfBlock iffy = __block.branchIf(CExpressionBuilder.builder()
				.compare(codeVars.waitingThrown(),
					CComparison.NOT_EQUALS, CVariable.NULL)
			.build()))
		{
			iffy.gotoLabel(Constants.THROW_HANDLER_LABEL);
		}
	}
	
	/**
	 * Performs reference loading.
	 *
	 * @param __block The block to write into.
	 * @param __store Store value?
	 * @param __localDx The local index.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2032/05/31
	 */
	private void __doALoadStore(CFunctionBlock __block, boolean __store,
		int __localDx)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		// Copy reference over
		__CodeVariables__ codeVars = this.__codeVars();
		__block.functionCall((__store ? JvmFunctions.NVM_LOCAL_POP_REFERENCE :
				JvmFunctions.NVM_LOCAL_PUSH_REFERENCE),
			codeVars.currentFrame(),
			CExpressionBuilder.builder()
				.number(__localDx)
				.build());
	}
	
	/**
	 * Gets the length of the given array.
	 * 
	 * @param __block The block to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/06
	 */
	private void __doArrayLength(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in instance
		JvmTemporary instance = codeVars.temporary(0);
		__block.variableSetViaFunction(instance.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVars.currentFrame());
		
		// Get length of it
		CExpression length = codeVars.temporary(1)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(length,
			JvmFunctions.NVM_ARRAY_LENGTH,
			codeVars.currentFrame(),
			instance.accessTemp(JvmTypes.JOBJECT.pointerType()));
		
		// Push to the stack
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
			codeVars.currentFrame(),
			length);
	}
	
	/**
	 * Performs a throw of an exception.
	 * 
	 * @param __block The block to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doAThrow(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in object off the stack
		__block.functionCall(JvmFunctions.NVM_STACK_POP_REFERENCE_THEN_THROW,
			codeVars.currentFrame());
		
		// Perform throw check now
		this.__checkThrow(__block);
	}
	
	/**
	 * Gets a field value.
	 *
	 * @param __block The block to write to.
	 * @param __field The field to get from.
	 * @param __store Is this a write to the field?
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doFieldGetPut(CFunctionBlock __block,
		FieldReference __field, boolean __store)
		throws IOException, NullPointerException
	{
		if (__block == null || __field == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read value
		JvmTemporary value = codeVars.temporary(0);
		if (__store)
			__block.variableSetViaFunction(value.tempIndex(),
				JvmFunctions.NVM_STACK_POP_ANY_TO_TEMP,
				codeVars.currentFrame());
		
		// Read instance to act on
		JvmTemporary instance = codeVars.temporary(1);
		__block.variableSetViaFunction(instance.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVars.currentFrame());
		
		// Call put handler
		if (__store)
		{
			// Call put handler
			__block.functionCall(JvmFunctions.NVM_FIELD_PUT,
				codeVars.currentFrame(),
				instance.accessTemp(JvmTypes.JOBJECT.pointerType()),
				codeVars.linkageReference(this.linkTable.fieldAccess(
					this.method.nameAndType(), false, __field,
					true), "fieldAccess"),
				value.accessTemp(JvmTypes.ANY));
		}
		
		// Call get handler
		else
		{
			__block.variableSetViaFunction(value.tempIndex(),
				JvmFunctions.NVM_FIELD_GET_TO_TEMP,
				codeVars.currentFrame(),
				instance.accessTemp(JvmTypes.JOBJECT.pointerType()),
				codeVars.linkageReference(
					this.linkTable.fieldAccess(this.method.nameAndType(),
						false, __field,
						false), "fieldAccess"));
			
			// Push value
			__block.functionCall(JvmFunctions.NVM_STACK_PUSH_ANY_FROM_TEMP,
				codeVars.currentFrame(),
				value.tempIndex());
		}
	}
	
	/**
	 * Performs a comparison.
	 * 
	 * @param __block The block to write to.
	 * @param __compare The comparison to make.
	 * @param __targetGroupId The jump target.
	 * @param __a A value.
	 * @param __b B value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2032/07/05
	 */
	private void __doIf(CFunctionBlock __block, CComparison __compare,
		int __targetGroupId, CExpression __a, CExpression __b)
		throws IOException, NullPointerException
	{
		if (__block == null || __compare == null || __a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Perform check against values
		try (CIfBlock iffy = __block.branchIf(
			CExpressionBuilder.builder()
				.compare(__a, __compare, __b)
			.build()))
		{
			// Change grouping
			this.__jumpToGroup(iffy, __targetGroupId);
		}
	}
	
	/**
	 * Performs integer comparison, popping two values from the stack and
	 * comparing them.
	 * 
	 * @param __block The block to write to.
	 * @param __compare The comparison to make.
	 * @param __targetGroupId The target group ID.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/05
	 */
	private void __doIfCmpInt(CFunctionBlock __block, CComparison __compare,
		int __targetGroupId)
		throws IOException, NullPointerException
	{
		if (__block == null || __compare == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Pop from stack
		CExpression a = codeVariables.temporary(0)
			.access(JvmTypes.JINT);
		CExpression b = codeVariables.temporary(1)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(b,
			JvmFunctions.NVM_STACK_POP_INTEGER,
				codeVariables.currentFrame());
		__block.variableSetViaFunction(a,
			JvmFunctions.NVM_STACK_POP_INTEGER,
				codeVariables.currentFrame());
		
		// Compare value
		this.__doIf(__block, __compare, __targetGroupId, a, b);
	}
	
	/**
	 * Compares against zero, popping a single value.
	 *
	 * @param __block The block to write to.
	 * @param __compare The comparison to make.
	 * @param __targetGroupId The jump target if a successful branch.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doIfZeroInt(CFunctionBlock __block, CComparison __compare,
		int __targetGroupId)
		throws IOException, NullPointerException
	{
		if (__block == null || __compare == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Pop from stack
		CExpression value = codeVariables.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(value,
			JvmFunctions.NVM_STACK_POP_INTEGER,
				codeVariables.currentFrame());
		
		// Compare value
		this.__doIf(__block, __compare, __targetGroupId, value,
			Constants.ZERO);
	}
	
	/**
	 * Writes a null check.
	 *
	 * @param __block The block to write.
	 * @param __null If checking against null.
	 * @param __targetGroupId The target group ID for the jump.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/03
	 */
	private void __doIfMaybeNull(CFunctionBlock __block, boolean __null,
		int __targetGroupId)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Pop from stack
		JvmTemporary object = codeVariables.temporary(0);
		__block.variableSetViaFunction(object.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
				codeVariables.currentFrame());
		
		// Perform check on object, if NULL or not
		try (CIfBlock iffy = __block.branchIf(
			CExpressionBuilder.builder()
				.compare(object.referenceTemp(JvmTypes.JOBJECT.type()
						.pointerType()),
					(__null ? CComparison.EQUALS : CComparison.NOT_EQUALS),
					CVariable.NULL)
			.build()))
		{
			// Change grouping
			this.__jumpToGroup(iffy, __targetGroupId);
		}
	}
	
	/**
	 * Invokes a method in generic terms.
	 *
	 * @param __block The output block.
	 * @param __linkage The method linkage.
	 * @param __method The method being invoked.
	 * @param __funcHandler The function handler.
	 * @param __linkWhat What is being referred to in the link table?
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doInvokeGeneric(CFunctionBlock __block,
		Container<? extends Linkage> __linkage,
		MethodReference __method, JvmFunctions __funcHandler,
		String __linkWhat)
		throws IOException, NullPointerException
	{
		if (__block == null || __linkage == null || __method == null ||
			__funcHandler == null || __linkWhat == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Just perform the function handler call, it will accordingly
		// put things on the stack and otherwise
		__block.functionCall(__funcHandler,
			codeVars.currentState(),
			codeVars.currentThread(),
			codeVars.linkageReference(__linkage, __linkWhat));
	}
	
	/**
	 * Invokes a "normal" method.
	 *
	 * @param __block The output block.
	 * @param __static Is this a static invocation?
	 * @param __method The method being invoked.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doInvokeNormal(CFunctionBlock __block, boolean __static,
		MethodReference __method)
		throws IOException, NullPointerException
	{
		if (__block == null || __method == null)
			throw new NullPointerException("NARG");
		
		// Forward call
		this.__doInvokeGeneric(__block,
			this.linkTable.invokeNormal(this.method.nameAndType(),
				__static, __method),
			__method,
			JvmFunctions.NVM_INVOKE_NORMAL,
			"invokeNormal");
	}
	
	/**
	 * Invokes a special method.
	 * 
	 * @param __block The output block.
	 * @param __method The method being invoked.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	private void __doInvokeSpecial(CFunctionBlock __block,
		MethodReference __method)
		throws IOException, NullPointerException
	{
		if (__block == null || __method == null)
			throw new NullPointerException("NARG");
		
		// Forward call
		this.__doInvokeGeneric(__block,
			this.linkTable.invokeSpecial(this.method.nameAndType(), __method),
			__method,
			JvmFunctions.NVM_INVOKE_SPECIAL,
			"invokeSpecial");
	}
	
	/**
	 * Store integer to local variable.
	 *
	 * @param __block The block to write to.
	 * @param __store Store value?
	 * @param __localDx The local to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doILoadStore(CFunctionBlock __block, boolean __store,
		int __localDx)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Pop over
		__block.functionCall((__store ? JvmFunctions.NVM_LOCAL_POP_INTEGER :
			JvmFunctions.NVM_LOCAL_PUSH_INTEGER),
			CExpressionBuilder.builder()
				.identifier(codeVars.currentFrame())
				.build(),
			CExpressionBuilder.builder()
				.number(__localDx)
				.build());
	}
	
	/**
	 * Writes a jump table.
	 * 
	 * @param __block The block to write to.
	 * @param __jumpTable The jump table to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doJumpTable(CFunctionBlock __block,
		IntMatchingJumpTable __jumpTable)
		throws IOException, NullPointerException
	{
		if (__block == null || __jumpTable == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Read in the key for jumping
		CExpression key = codeVariables.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(key,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVariables.currentFrame());
		
		// Gigantic switch on the value
		try (CSwitchBlock branches = __block.switchCase(key))
		{
			InstructionJumpTarget[] targets = __jumpTable.targets();
			int numTargets = targets.length;
			
			// Normal branches
			for (int i = 1; i < numTargets; i++)
			{
				InstructionJumpTarget target = targets[i];
				
				// Start case here
				branches.nextCase(target.key());
				
				// Perform jump
				this.__jumpToGroup(branches,
					this.__addressToGroup(target.target()));
				
				// End with break
				branches.breakCase();
			}
			
			// Default branch
			branches.defaultCase();
			
			// Default branch is always zero
			InstructionJumpTarget target = targets[0];
			this.__jumpToGroup(branches,
				this.__addressToGroup(target.target()));
			
			// End with break
			branches.breakCase();
		}
	}
	
	/**
	 * Loads a constant value onto the stack.
	 * 
	 * @param __block The block to write to.
	 * @param __value The value to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doLdc(CFunctionBlock __block, ConstantValue __value)
		throws IOException, NullPointerException
	{
		if (__block == null || __value == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Depends on the type
		JvmTemporary temp;
		switch (__value.type())
		{
				// Integer value
			case INTEGER:
				__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
					codeVariables.currentFrame(),
					CBasicExpression.number((Integer)__value.boxedValue()));
				break;
				
				// Long value
			case LONG:
				{
					long value = (Long)__value.boxedValue();
					__block.functionCall(
						JvmFunctions.NVM_STACK_PUSH_LONG_PARTS,
						codeVariables.currentFrame(),
						CBasicExpression.number((int)(value >>> 32L)),
						CBasicExpression.number((int)value));
				}
				break;
				
				// Float value
			case FLOAT:
				__block.functionCall(JvmFunctions.NVM_STACK_PUSH_FLOAT_RAW,
					codeVariables.currentFrame(),
					CBasicExpression.number(Float.floatToRawIntBits(
						(Float)__value.boxedValue())));
				break;
				
				// Double value
			case DOUBLE:
				{
					long value = Double.doubleToRawLongBits(
						(Double)__value.boxedValue());
					__block.functionCall(
						JvmFunctions.NVM_STACK_PUSH_DOUBLE_RAW_PARTS,
						codeVariables.currentFrame(),
						CBasicExpression.number((int)(value >>> 32L)),
						CBasicExpression.number((int)value));
				}
				break;
			
			case STRING:
				// Get temporary, needed for string storage
				temp = codeVariables.temporary(0);
				
				// Load string then push it
				__block.variableSetViaFunction(temp.tempIndex(),
					JvmFunctions.NVM_LOOKUP_STRING_INTO_TEMP,
					codeVariables.currentThread(),
					CExpressionBuilder.builder()
							.string(__value.boxedValue().toString())
						.build());
				__block.functionCall(
					JvmFunctions.NVM_STACK_PUSH_REFERENCE_FROM_TEMP,
					codeVariables.currentFrame(),
					temp.tempIndex());
				break;
			
			default:
				throw Debugging.todo(__value.type());
		}
	}
	
	/**
	 * Performs an integer math operation.
	 * 
	 * @param __block The block to write to.
	 * @param __op The math operation to perform.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doMathInteger(CFunctionBlock __block, CMathOperator __op)
		throws IOException, NullPointerException
	{
		if (__block == null || __op == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		CExpression a = codeVariables.temporary(0)
			.access(JvmTypes.JINT);
		CExpression b = codeVariables.temporary(1)
			.access(JvmTypes.JINT);
		
		// Pop in both values
		__block.variableSetViaFunction(b,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVariables.currentFrame());
		__block.variableSetViaFunction(a,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVariables.currentFrame());
		
		// Perform operation
		__block.variableSet(a, CExpressionBuilder.builder()
				.math(a, __op, b)
			.build());
		
		// Push back on
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
			codeVariables.currentFrame(),
			a);
	}
	
	/**
	 * Allocates a new object.
	 * 
	 * @param __block The block to write to.
	 * @param __what What is being allocated?
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	private void __doNew(CFunctionBlock __block, ClassName __what)
		throws IOException, NullPointerException
	{
		if (__block == null || __what == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Allocate object
		JvmTemporary object = codeVars.temporary(0);
		__block.variableSetViaFunction(object.tempIndex(),
			JvmFunctions.NVM_NEW_INSTANCE_INTO_TEMP,
			codeVars.currentThread(),
			CExpressionBuilder.builder()
					.string(__what.toString())
				.build());
		
		// Did this throw anything?
		this.__checkThrow(__block);
		
		// Push to the stack
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_REFERENCE_FROM_TEMP,
			codeVars.currentFrame(),
			object.tempIndex());
	}
	
	/**
	 * Allocates a new array.
	 * 
	 * @param __block The block to write to.
	 * @param __componentType The component type of the array.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/05
	 */
	private void __doNewArray(CFunctionBlock __block,
		ClassName __componentType)
		throws IOException, NullPointerException
	{
		if (__block == null || __componentType == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Read in length
		CExpression length = codeVariables.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(length,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVariables.currentFrame());
		
		// Perform allocation
		JvmTemporary object = codeVariables.temporary(1);
		__block.variableSetViaFunction(object.tempIndex(),
			JvmFunctions.NVM_NEW_ARRAY_INTO_TEMP,
			codeVariables.currentThread(),
			CExpressionBuilder.builder()
					.string(__componentType.toString())
				.build(),
			length);
		
		// Did this throw anything?
		this.__checkThrow(__block);
		
		// Push to the stack
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_REFERENCE_FROM_TEMP,
			codeVariables.currentFrame(),
			object.tempIndex());
	}
	
	/**
	 * Accesses a value in a primitive array.
	 * 
	 * @param __block The block to write to.
	 * @param __type The type of primitive used.
	 * @param __store Storing a value?
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doPrimitiveArrayAccess(CFunctionBlock __block,
		JvmPrimitiveType __type, boolean __store)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Value to read/write
		JvmTemporary value = codeVars.temporary(0);
		
		// Read in the value?
		if (__store)
			__block.variableSetViaFunction(value.tempIndex(),
				JvmFunctions.NVM_STACK_POP_ANY_TO_TEMP,
				codeVars.currentFrame());
		
		// Read array index
		CExpression index = codeVars.temporary(2).access(JvmTypes.JINT);
		__block.variableSetViaFunction(index,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVars.currentFrame());
		
		// Read array instance
		JvmTemporary instance = codeVars.temporary(1);
		__block.variableSetViaFunction(instance.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVars.currentFrame());
		
		// Store value?
		if (__store)
		{
			__block.functionCall(JvmFunctions.NVM_ARRAY_STORE,
				codeVars.currentFrame(),
				CBasicExpression.number(__type.ordinal()),
				instance.accessTemp(JvmTypes.JOBJECT.pointerType()),
				index,
				value.referenceTemp(JvmTypes.ANY));
		}
		
		// Load value?
		else
		{
			__block.variableSetViaFunction(value.tempIndex(),
				JvmFunctions.NVM_ARRAY_LOAD_INTO_TEMP,
				codeVars.currentFrame(),
				CBasicExpression.number(__type.ordinal()),
				instance.accessTemp(JvmTypes.JOBJECT.pointerType()),
				index);
			__block.functionCall(JvmFunctions.NVM_STACK_PUSH_ANY_FROM_TEMP,
				codeVars.currentFrame(),
				value.tempIndex());
		}
	}
	
	/**
	 * Performs return from method.
	 * 
	 * @param __block The block to write in.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/03
	 */
	private void __doReturn(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Do actual return
		__block.functionCall(JvmFunctions.NVM_RETURN_FROM_METHOD,
			CExpressionBuilder.builder()
				.identifier(codeVars.currentState())
				.build());
		
		// Jump to the outro directly
		__block.gotoLabel(Constants.OUTRO_LABEL);
	}
	
	/**
	 * Returns a value from the method.
	 * 
	 * @param __block The block to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doReturnValue(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Pop into return value storage
		JvmTemporary value = codeVars.temporary(0);
		__block.variableSetViaFunction(value.tempIndex(),
			JvmFunctions.NVM_STACK_POP_ANY_TO_TEMP,
			codeVars.currentFrame());
		
		// Do actual return
		__block.functionCall(JvmFunctions.NVM_RETURN_FROM_METHOD,
			value.referenceTemp(JvmTypes.ANY));
		
		// Jump to the outro directly
		__block.gotoLabel(Constants.OUTRO_LABEL);
	}
	
	/**
	 * Performs stack shuffling.
	 * 
	 * @param __block The block to write to.
	 * @param __function The shuffle function used.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	private void __doStackShuffle(CFunctionBlock __block,
		JavaStackShuffleType.Function __function)
		throws IOException, NullPointerException
	{
		if (__block == null || __function == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Need to actually declare temporaries as being used
		int inCount = __function.in.logicalMax;
		JvmTemporary[] in = new JvmTemporary[inCount];
		for (int i = 0; i < inCount; i++)
			in[i] = codeVars.temporary(i);
		
		// Need to read in for temporaries, using "any" types are simpler
		for (int i = inCount - 1; i >= 0; i--)
			__block.variableSetViaFunction(
				in[i].tempIndex(),
				JvmFunctions.NVM_STACK_POP_ANY_TO_TEMP,
				codeVars.currentFrame());
		
		// Then have to push everything back in
		int outCount = __function.out.logicalMax;
		for (int i = 0; i < outCount; i++)
		{
			// Determine where this comes from
			int sourceSlot = __function.in.logicalSlot(
				__function.in.findVariableSlot(
				__function.out.logicalVariable(i)));
			
			// Push back from the source slot
			__block.functionCall(JvmFunctions.NVM_STACK_PUSH_ANY,
				codeVars.currentFrame(),
				in[sourceSlot].accessTemp(JvmTypes.ANY));
		}
	}
	
	/**
	 * Handles an exception being thrown.
	 * 
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/15
	 */
	private void __handleThrow(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Execute throw
		__block.functionCall(JvmFunctions.NVM_THROW_EXECUTE,
			codeVars.currentFrame());
	}
	
	/**
	 * Jumps to the given group ID.
	 * 
	 * @param __block The block to write to.
	 * @param __targetGroupId The target group ID.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __jumpToGroup(CFunctionBlock __block, int __targetGroupId)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		__block.variableSet(CExpressionBuilder.builder()
				.identifier(codeVariables.currentFrame())
				.dereferenceStruct()
				.identifier(codeVariables.currentFrame()
					.type(CPointerType.class)
					.dereferenceType(CStructType.class)
					.member("groupIndex"))
			.build(),
			CExpressionBuilder.builder()
					.number(__targetGroupId)
				.build());
	}
	
	/**
	 * Returns the comparison to use for the given {@code if} instruction.
	 * 
	 * @param __ifOp The {@code if} operation to get.
	 * @return The comparison used for the given instruction.
	 * @throws NoSuchElementException If the operation is not valid.
	 * @since 2023/07/04
	 */
	private static CComparison __commonCompareIf(int __ifOp)
		throws NoSuchElementException
	{
		switch (__ifOp)
		{
			case InstructionIndex.IFEQ:
			case InstructionIndex.IF_ICMPEQ:
				return CComparison.EQUALS;
				
			case InstructionIndex.IFNE:
			case InstructionIndex.IF_ICMPNE:
				return CComparison.NOT_EQUALS;
				
			case InstructionIndex.IFLT:
			case InstructionIndex.IF_ICMPLT:
				return CComparison.LESS_THAN;
				
			case InstructionIndex.IFLE:
			case InstructionIndex.IF_ICMPLE:
				return CComparison.LESS_EQUALS;
				
			case InstructionIndex.IFGT:
			case InstructionIndex.IF_ICMPGT:
				return CComparison.GREATER_THAN;
				
			case InstructionIndex.IFGE:
			case InstructionIndex.IF_ICMPGE:
				return CComparison.GREATER_EQUALS;
		}
		
		// {@squirreljme.error NC71 Unknown comparison operation.}
		throw new NoSuchElementException("NC71");
	}
	
	
	/**
	 * Determines the math operator to use.
	 * 
	 * @param __op The operation.
	 * @return The operator used.
	 * @throws NoSuchElementException If the operation is not valid.
	 * @since 2023/07/04
	 */
	private static CMathOperator __commonMathOp(int __op)
		throws NoSuchElementException
	{
		switch (__op)
		{
			case InstructionIndex.IADD:
			case InstructionIndex.LADD:
			case InstructionIndex.FADD:
			case InstructionIndex.DADD:
				return CMathOperator.ADD;
				
			case InstructionIndex.ISUB:
			case InstructionIndex.LSUB:
			case InstructionIndex.FSUB:
			case InstructionIndex.DSUB:
				return CMathOperator.SUBTRACT;
				
			case InstructionIndex.IMUL:
			case InstructionIndex.LMUL:
			case InstructionIndex.FMUL:
			case InstructionIndex.DMUL:
				return CMathOperator.MULTIPLY;
			
			case InstructionIndex.IDIV:
			case InstructionIndex.LDIV:
			case InstructionIndex.FDIV:
			case InstructionIndex.DDIV:
				return CMathOperator.DIVIDE;
			
			case InstructionIndex.IREM:
			case InstructionIndex.LREM:
			case InstructionIndex.FREM:
			case InstructionIndex.DREM:
				return CMathOperator.REMAINDER;
			
			case InstructionIndex.IAND:
			case InstructionIndex.LAND:
				return CMathOperator.AND;
			
			case InstructionIndex.IOR:
			case InstructionIndex.LOR:
				return CMathOperator.OR;
			
			case InstructionIndex.IXOR:
			case InstructionIndex.LXOR:
				return CMathOperator.XOR;
		}
		
		// {@squirreljme.error NC98 Unknown operation.}
		throw new NoSuchElementException("NC98");
	}
	
	/**
	 * Returns the primitive to use for the given operation.
	 * 
	 * @param __op The operation.
	 * @return The primitive for the given operation.
	 * @throws NoSuchElementException If the operation is not valid.
	 * @since 2023/07/16
	 */
	private static JvmPrimitiveType __commonPrimitive(int __op)
		throws NoSuchElementException
	{
		switch (__op)
		{
			case InstructionIndex.BALOAD:
			case InstructionIndex.BASTORE:
				return JvmPrimitiveType.BOOLEAN_OR_BYTE;
				
			case InstructionIndex.SALOAD:
			case InstructionIndex.SASTORE:
				return JvmPrimitiveType.SHORT;
				
			case InstructionIndex.CALOAD:
			case InstructionIndex.CASTORE:
				return JvmPrimitiveType.CHARACTER;
				
			case InstructionIndex.IALOAD:
			case InstructionIndex.IASTORE:
				return JvmPrimitiveType.INTEGER;
				
			case InstructionIndex.LALOAD:
			case InstructionIndex.LASTORE:
				return JvmPrimitiveType.LONG;
				
			case InstructionIndex.FALOAD:
			case InstructionIndex.FASTORE:
				return JvmPrimitiveType.FLOAT;
				
			case InstructionIndex.DALOAD:
			case InstructionIndex.DASTORE:
				return JvmPrimitiveType.DOUBLE;
		}
		
		// {@squirreljme.error NC99 Unknown operation.}
		throw new NoSuchElementException("NC99");
	}
	
	/**
	 * Determines whether this is a store or load operation.
	 * 
	 * @param __op The operation.
	 * @return {@code true} if this is a load operation.
	 * @throws NoSuchElementException If the operation is not valid.
	 * @since 2023/07/16
	 */
	private static boolean __commonStoreLoad(int __op)
		throws NoSuchElementException
	{
		switch (__op)
		{
			case InstructionIndex.BALOAD:
			case InstructionIndex.SALOAD:
			case InstructionIndex.CALOAD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.DALOAD:
				return false;
				
			case InstructionIndex.BASTORE:
			case InstructionIndex.SASTORE:
			case InstructionIndex.CASTORE:
			case InstructionIndex.IASTORE:
			case InstructionIndex.LASTORE:
			case InstructionIndex.FASTORE:
			case InstructionIndex.DASTORE:
				return true;
		}
		
		// {@squirreljme.error NC9a Unknown operation.}
		throw new NoSuchElementException("NC9a");
	}
}
