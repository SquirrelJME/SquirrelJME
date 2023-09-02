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
import cc.squirreljme.c.CRootExpressionBuilder;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CSwitchBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.c.std.CStdIntType;
import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmCompareOp;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmFunctions;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmInvokeType;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmShiftOp;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLinkBuilder;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Container;
import cc.squirreljme.jvm.aot.nanocoat.linkage.Linkage;
import cc.squirreljme.jvm.aot.nanocoat.table.StaticTableManager;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueType;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InstructionJumpTargets;
import net.multiphasicapps.classfile.IntMatchingJumpTable;
import net.multiphasicapps.classfile.JavaStackShuffleType;
import net.multiphasicapps.classfile.MethodDescriptor;
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
	
	/** The link table for the class. */
	protected final ClassLinkBuilder linkTable =
		new ClassLinkBuilder();
	
	/** The stack map table at runtime. */
	protected final StackMapTablePairs stackMap;
	
	/** Is this static? */
	protected final boolean isStatic;
	
	/** Variable placement mappings. */
	protected final VariablePlacementMap variablePlacements;
	
	/** The table manager. */
	protected final StaticTableManager tables;
	
	/** Basic block mappings. */
	private final Map<Integer, BasicBlock> _basicBlocks =
		new SortedTreeMap<>();
	
	/** Address to basic block group IDs. */
	private final Map<Integer, Integer> _addrToGroupId =
		new SortedTreeMap<>();
	
	/** Code variables for writing. */
	private volatile __CodeVariables__ _codeVars;
	
	/** Initial variables to not get cleared by garbage collection. */
	private volatile CFunctionBlock _initVars;
	
	/**
	 * Initializes the byte code processor.
	 *
	 * @param __tables Table manager.
	 * @param __code The code being processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public ByteCodeProcessor(StaticTableManager __tables,
		ByteCode __code)
		throws NullPointerException
	{
		if (__tables == null || __code == null)
			throw new NullPointerException("NARG");
		
		this.tables = __tables;
		this.code = __code;
		this.stackMap = __code.stackMapTableFull();
		this.isStatic = !__code.isInstance();
		this.variablePlacements = new VariablePlacementMap(
			!__code.isInstance(), __code.type(), __code.maxLocals(),
			__code.maxStack(), this.stackMap);
		
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
			CFunctionBlock initVarsBase;
			try (CFunctionBlock initVars = splices.splice(0))
			{
				// Setup code variables where the variables get defined and
				// such
				__CodeVariables__ codeVars = new __CodeVariables__(initVars);
				this._codeVars = codeVars;
				
				// Remember this, so it does not get GCed
				this._initVars = initVars;
				initVarsBase = initVars;
				
				// Keep track of the current top state, so we need not worry
				// about pushing or popping
				initVars.declare(codeVars.currentFrame());
				
				// Declare linkage variable
				initVars.declare(codeVars.linkage());
				
				// Set top variable
				initVars.variableSet(codeVars.currentFrame(),
					CExpressionBuilder.builder()
						.identifier(codeVars.currentThread())
						.dereferenceStruct()
						.identifier(JvmTypes.VMTHREAD.type(CStructType.class)
							.member("top"))
						.build());
				
				// Set linkage variable
				initVars.variableSet(codeVars.linkage(),
					CExpressionBuilder.builder()
						.identifier(codeVars.currentFrame())
						.dereferenceStruct()
						.identifier(JvmTypes.VMFRAME
							.type(CStructType.class)
							.member("linkage"))
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
							.member("pc"))
						.build()))
				{
					// Start of function call, initializes accordingly
					cases.nextCase(Constants.SJME_NANOCOAT_START_CALL);
					
					// --- Initialization ---
					// Return here so the initialization does get complete
					body.returnValue(Constants.TRUE);
						
					// Return from call when execution of method finishes
					cases.nextCase(Constants.SJME_NANOCOAT_END_CALL);
				
					// --- Exit ---
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
				try (CFunctionBlock initVars = initVarsBase)
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
				// Do nothing
			case InstructionIndex.NOP:
				break;
			
			case InstructionIndex.ATHROW:
				this.__doAThrow(__block);
				break;
				
				// Check that value is the proper type
			case InstructionIndex.CHECKCAST:
				this.__doCheckCast(__block,
					__instruction.argument(0, ClassName.class));
				break;
				
				// Check that value is the proper type, without an exception
			case InstructionIndex.INSTANCEOF:
				this.__doInstanceOf(__block,
					__instruction.argument(0, ClassName.class));
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
				
				// Null value
			case InstructionIndex.ACONST_NULL:
				this.__doAConstNull(__block);
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
				
			case InstructionIndex.LCMP:
			case InstructionIndex.FCMPG:
			case InstructionIndex.FCMPL:
			case InstructionIndex.DCMPG:
			case InstructionIndex.DCMPL:
				this.__doCompareSoft(__block,
					ByteCodeProcessor.__commonPrimitive(op),
					ByteCodeProcessor.__commonCompareOp(op));
				break;
				
				// Reference comparison
			case InstructionIndex.IF_ACMPEQ:
			case InstructionIndex.IF_ACMPNE:
				this.__doIfCmpReference(__block,
					ByteCodeProcessor.__commonCompareIf(op),
					this.__addressToGroup(__instruction, 0));
				break;
			
				// Integer comparison
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
				this.__doInvoke(__block, JvmInvokeType.SPECIAL,
					__instruction.argument(0, MethodReference.class));
				break;
				
			case InstructionIndex.INVOKEINTERFACE:
				this.__doInvoke(__block, JvmInvokeType.INTERFACE,
					__instruction.argument(0, MethodReference.class));
				break;
				
			case InstructionIndex.INVOKESTATIC:
				this.__doInvoke(__block, JvmInvokeType.STATIC,
					__instruction.argument(0, MethodReference.class));
				break;
				
			case InstructionIndex.INVOKEVIRTUAL:
				this.__doInvoke(__block, JvmInvokeType.VIRTUAL,
					__instruction.argument(0, MethodReference.class));
				break;
				
			case InstructionIndex.ANEWARRAY:
				this.__doNewArray(__block,
					__instruction.argument(0, ClassName.class));
				break;
				
				// New array with multiple dimensions
			case InstructionIndex.MULTIANEWARRAY:
				this.__doNewArrayMulti(__block,
					__instruction.argument(0, ClassName.class),
					__instruction.argument(1, Integer.class));
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
				
			case InstructionIndex.ARETURN:
			case InstructionIndex.IRETURN:
			case InstructionIndex.LRETURN:
			case InstructionIndex.FRETURN:
			case InstructionIndex.DRETURN:
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
			case InstructionIndex.WIDE_LLOAD:
			case InstructionIndex.WIDE_FLOAD:
			case InstructionIndex.WIDE_DLOAD:
				this.__doPrimitiveLoadStore(__block, false,
					ByteCodeProcessor.__commonPrimitive(op),
					__instruction.intArgument(0));
				break;
				
			case InstructionIndex.WIDE_ISTORE:
			case InstructionIndex.WIDE_LSTORE:
			case InstructionIndex.WIDE_FSTORE:
			case InstructionIndex.WIDE_DSTORE:
				this.__doPrimitiveLoadStore(__block, true,
					ByteCodeProcessor.__commonPrimitive(op),
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
				
				// Software math
			case InstructionIndex.LADD:
			case InstructionIndex.LSUB:
			case InstructionIndex.LMUL:
			case InstructionIndex.LDIV:
			case InstructionIndex.LREM:
			case InstructionIndex.LAND:
			case InstructionIndex.LOR:
			case InstructionIndex.LXOR:
			case InstructionIndex.FADD:
			case InstructionIndex.FSUB:
			case InstructionIndex.FMUL:
			case InstructionIndex.FDIV:
			case InstructionIndex.FREM:
			case InstructionIndex.DADD:
			case InstructionIndex.DSUB:
			case InstructionIndex.DMUL:
			case InstructionIndex.DDIV:
			case InstructionIndex.DREM:
				this.__doMathSoft(__block,
					ByteCodeProcessor.__commonPrimitive(op),
					ByteCodeProcessor.__commonMathOp(op));
				break;
				
				// Integer conversion
			case InstructionIndex.I2B:
			case InstructionIndex.I2S:
			case InstructionIndex.I2C:
				this.__doConvertInteger(__block,
					ByteCodeProcessor.__commonPrimitive(op, true));
				break;
				
				// Software conversion
			case InstructionIndex.I2L:
			case InstructionIndex.I2F:
			case InstructionIndex.I2D:
			case InstructionIndex.L2I:
			case InstructionIndex.L2F:
			case InstructionIndex.L2D:
			case InstructionIndex.F2I:
			case InstructionIndex.F2L:
			case InstructionIndex.F2D:
			case InstructionIndex.D2I:
			case InstructionIndex.D2L:
			case InstructionIndex.D2F:
				this.__doConvertSoft(__block,
					ByteCodeProcessor.__commonPrimitive(op, false),
					ByteCodeProcessor.__commonPrimitive(op, true));
				break;
				
				// Bit shift
			case InstructionIndex.ISHL:
			case InstructionIndex.ISHR:
			case InstructionIndex.IUSHR:
				this.__doShiftInteger(__block,
					ByteCodeProcessor.__commonShiftOp(op));
				break;
				
				// Software shift bit
			case InstructionIndex.LSHL:
			case InstructionIndex.LSHR:
			case InstructionIndex.LUSHR:
				this.__doShiftSoftware(__block,
					ByteCodeProcessor.__commonPrimitive(op, false),
					ByteCodeProcessor.__commonShiftOp(op));
				break;
				
				// Integer Negative
			case InstructionIndex.INEG:
				this.__doNegativeInteger(__block);
				break;
				
				// Soft Negative
			case InstructionIndex.LNEG:
			case InstructionIndex.FNEG:
			case InstructionIndex.DNEG:
				this.__doNegativeSoft(__block,
					ByteCodeProcessor.__commonPrimitive(op, false));
				break;
				
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
				
				// Get/Put field
			case InstructionIndex.GETFIELD:
			case InstructionIndex.PUTFIELD:
				this.__doFieldGetPut(__block, false,
					__instruction.argument(0, FieldReference.class),
					(op == InstructionIndex.PUTFIELD));
				break;
				
				// Get/Put static field
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.PUTSTATIC:
				this.__doFieldGetPut(__block, true,
					__instruction.argument(0, FieldReference.class),
					(op == InstructionIndex.PUTSTATIC));
				break;
				
				// Store or load reference from array
			case InstructionIndex.AALOAD:
			case InstructionIndex.AASTORE:
				this.__doArrayAccess(__block,
					JvmPrimitiveType.OBJECT,
					ByteCodeProcessor.__commonStoreLoad(op));
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
				this.__doArrayAccess(__block,
					ByteCodeProcessor.__commonPrimitive(op),
					ByteCodeProcessor.__commonStoreLoad(op));
				break;
				
				// Length of an array
			case InstructionIndex.ARRAYLENGTH:
				this.__doArrayLength(__block);
				break;
				
				// Increment integer value and store into local
			case InstructionIndex.WIDE_IINC:
				this.__doIncrementInteger(__block,
					__instruction.argument(0, Integer.class),
					__instruction.argument(1, Integer.class));
				break;
				
				// Synchronization monitors
			case InstructionIndex.MONITORENTER:
			case InstructionIndex.MONITOREXIT:
				this.__doMonitor(__block,
					(op == InstructionIndex.MONITORENTER));
				break;
			
			default:
				throw Debugging.todo(__instruction);
		}
	}
	
	/**
	 * Returns the variable placement map.
	 *
	 * @return The variable placement map.
	 * @since 2023/08/13
	 */
	public VariablePlacementMap variablePlacements()
	{
		return this.variablePlacements;
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
	 * Pushes null constant to the stack.
	 *
	 * @param __block The block to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doAConstNull(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Just push null to the stack
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_REFERENCE,
			codeVars.currentFrame(),
			CVariable.NULL);
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
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Copy reference over
		__block.functionCall((__store ? JvmFunctions.NVM_LOCAL_POP_REFERENCE :
				JvmFunctions.NVM_LOCAL_PUSH_REFERENCE),
			codeVars.currentFrame(),
			CExpressionBuilder.builder()
				.number(__localDx)
				.build());
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
	private void __doArrayAccess(CFunctionBlock __block,
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
		CExpression index = codeVars.temporary(2)
			.access(JvmTypes.JINT);
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
				instance.accessTemp(JvmTypes.JOBJECT),
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
				instance.accessTemp(JvmTypes.JOBJECT),
				index);
			__block.functionCall(JvmFunctions.NVM_STACK_PUSH_ANY_FROM_TEMP,
				codeVars.currentFrame(),
				value.tempIndex());
		}
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
		JvmTemporary len = codeVars.temporary(1);
		__block.functionCall(JvmFunctions.NVM_ARRAY_LENGTH,
			codeVars.currentFrame(),
			instance.accessTemp(JvmTypes.JOBJECT),
			len.reference(JvmTypes.JINT));
		
		// Push to the stack
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
			codeVars.currentFrame(),
			len.access(JvmTypes.JINT));
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
	 * Checks if the value is the given type.
	 *
	 * @param __block The block to write to.
	 * @param __class The class to check.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doCheckCast(CFunctionBlock __block, ClassName __class)
		throws IOException, NullPointerException
	{
		if (__block == null || __class == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Load instance
		JvmTemporary instance = codeVars.temporary(0);
		__block.variableSetViaFunction(instance.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVars.currentFrame());
		
		// Refer to other class, then determine if it is that class
		__block.functionCall(
			JvmFunctions.NVM_CHECK_CAST,
			codeVars.currentFrame(),
			instance.accessTemp(JvmTypes.JOBJECT),
			codeVars.linkageReference(this.linkTable.classObject(
				__class), "classObject"));
		
		// Check exception
		this.__checkThrow(__block);
		
		// Push self
		__block.functionCall(
			JvmFunctions.NVM_STACK_PUSH_REFERENCE,
			codeVars.currentFrame(),
			instance.accessTemp(JvmTypes.JOBJECT));
	}
	
	/**
	 * Performs a soft comparison of values.
	 *
	 * @param __block The block to write to.
	 * @param __type The type to act on.
	 * @param __op The operation to perform.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doCompareSoft(CFunctionBlock __block,
		JvmPrimitiveType __type, JvmCompareOp __op)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null || __op == null)
			throw new NullPointerException("NARG");
		
		// Just do a completely normal invocation here!
		this.__doInvoke(__block, JvmInvokeType.STATIC,
			__type.softCompare(__op));
	}
	
	/**
	 * Converts an integer value.
	 *
	 * @param __block The block to write to.
	 * @param __to The target type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doConvertInteger(CFunctionBlock __block,
		JvmPrimitiveType __to)
		throws IOException, NullPointerException
	{
		if (__block == null || __to == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in integer value
		CExpression value = codeVars.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(value,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVars.currentFrame());
		
		// What do we cast to?
		CTypeProvider cast;
		switch (__to)
		{
			case BOOLEAN_OR_BYTE:
				cast = JvmTypes.JBYTE;
				break;
				
			case SHORT:
				cast = JvmTypes.JSHORT;
				break;
				
			case CHARACTER:
				cast = JvmTypes.JCHAR;
				break;
				
			default:
				throw Debugging.oops();
		}
		
		// Push conversion
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
			codeVars.currentFrame(),
			CExpressionBuilder.builder()
				.cast(cast, value)
				.build());
	}
	
	/**
	 * Converts a value using software.
	 *
	 * @param __block The block to write to.
	 * @param __from The source type.
	 * @param __to The target type.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doConvertSoft(CFunctionBlock __block,
		JvmPrimitiveType __from, JvmPrimitiveType __to)
		throws IOException, NullPointerException
	{
		if (__block == null || __from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Just do a completely normal invocation here!
		this.__doInvoke(__block, JvmInvokeType.STATIC,
			__from.softConvert(__to));
	}
	
	/**
	 * Gets or puts a field value.
	 *
	 * @param __block The block to write to.
	 * @param __static Is this a static access?
	 * @param __field The field to get from.
	 * @param __store Is the field being written to?
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doFieldGetPut(CFunctionBlock __block, boolean __static,
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
		JvmTemporary instance;
		if (__static)
			instance = null;
		else
		{
			instance = codeVars.temporary(1);
			__block.variableSetViaFunction(instance.tempIndex(),
				JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
				codeVars.currentFrame());
		}
		
		// Call put handler
		if (__store)
		{
			// Call put handler
			__block.functionCall(JvmFunctions.NVM_FIELD_PUT,
				codeVars.currentFrame(),
				(instance != null ?
					instance.accessTemp(JvmTypes.JOBJECT) :
					CVariable.NULL),
				codeVars.linkageReference(this.linkTable.fieldAccess(
					__static, __field, true), "fieldAccess"),
				value.referenceTemp(JvmTypes.ANY));
		}
		
		// Call get handler
		else
		{
			__block.variableSetViaFunction(value.tempIndex(),
				JvmFunctions.NVM_FIELD_GET_TO_TEMP,
				codeVars.currentFrame(),
				(instance != null ?
					instance.accessTemp(JvmTypes.JOBJECT) :
					CVariable.NULL),
				codeVars.linkageReference(
					this.linkTable.fieldAccess(__static, __field,
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
	 * Performs object comparison.
	 * 
	 * @param __block The block to write to.
	 * @param __compare The comparison to make.
	 * @param __targetGroupId The target group ID.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doIfCmpReference(CFunctionBlock __block,
		CComparison __compare, int __targetGroupId)
		throws IOException, NullPointerException
	{
		if (__block == null || __compare == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVariables = this.__codeVars();
		
		// Pop from stack
		JvmTemporary a = codeVariables.temporary(0);
		JvmTemporary b = codeVariables.temporary(1);
		__block.variableSetViaFunction(b.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVariables.currentFrame());
		__block.variableSetViaFunction(a.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVariables.currentFrame());
		
		// Compare value
		this.__doIf(__block, __compare, __targetGroupId,
			a.accessTemp(JvmTypes.JOBJECT),
			b.accessTemp(JvmTypes.JOBJECT));
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
				.compare(object.accessTemp(JvmTypes.JOBJECT),
					(__null ? CComparison.EQUALS : CComparison.NOT_EQUALS),
					CVariable.NULL)
			.build()))
		{
			// Change grouping
			this.__jumpToGroup(iffy, __targetGroupId);
		}
	}
	
	/**
	 * Increments an integer in a local.
	 *
	 * @param __block The block to write to.
	 * @param __index The index of the local.
	 * @param __by The amount to increment by.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doIncrementInteger(CFunctionBlock __block,
		int __index, int __by)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in value
		CExpression value = codeVars.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(value,
			JvmFunctions.NVM_LOCAL_LOAD_INTEGER,
			codeVars.currentFrame(),
			CBasicExpression.number(__index));
		
		// Write new value
		__block.functionCall(JvmFunctions.NVM_LOCAL_STORE_INTEGER,
			codeVars.currentFrame(),
			CBasicExpression.number(__index),
			CExpressionBuilder.builder()
				.math(value, CMathOperator.ADD,
					CBasicExpression.number(__by))
				.build());
	}
	
	/**
	 * Generates {@code instanceof} check.
	 *
	 * @param __block The block to write to.
	 * @param __class The class to check.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doInstanceOf(CFunctionBlock __block, ClassName __class)
		throws IOException, NullPointerException
	{
		if (__block == null || __class == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Load instance
		JvmTemporary instance = codeVars.temporary(0);
		__block.variableSetViaFunction(instance.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVars.currentFrame());
		
		// Refer to other class, then determine if it is that class
		__block.functionCall(
			JvmFunctions.NVM_STACK_PUSH_INTEGER_IS_INSTANCE_OF,
			codeVars.currentFrame(),
			instance.accessTemp(JvmTypes.JOBJECT),
			codeVars.linkageReference(this.linkTable.classObject(
				__class), "classObject"));
	}
	
	/**
	 * Invokes a "normal" method.
	 *
	 * @param __block The output block.
	 * @param __type Is this a static invocation?
	 * @param __method The method being invoked.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doInvoke(CFunctionBlock __block, JvmInvokeType __type,
		MethodReference __method)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null || __method == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Just perform the function handler call, it will accordingly
		// put things on the stack and otherwise
		__block.functionCall(JvmFunctions.NVM_INVOKE,
			codeVars.currentFrame(),
			codeVars.linkageReference(
				this.linkTable.invoke(__type, __method), "invoke"));
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
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Depends on the type
		JvmTemporary temp;
		switch (__value.type())
		{
				// Integer value
			case INTEGER:
				__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
					codeVars.currentFrame(),
					CBasicExpression.number((Integer)__value.boxedValue()));
				break;
				
				// Long value
			case LONG:
				{
					long value = (Long)__value.boxedValue();
					__block.functionCall(
						JvmFunctions.NVM_STACK_PUSH_LONG_PARTS,
						codeVars.currentFrame(),
						CBasicExpression.number((int)(value >>> 32L)),
						CBasicExpression.number((int)value));
				}
				break;
				
				// Float value
			case FLOAT:
				__block.functionCall(JvmFunctions.NVM_STACK_PUSH_FLOAT_RAW,
					codeVars.currentFrame(),
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
						codeVars.currentFrame(),
						CBasicExpression.number((int)(value >>> 32L)),
						CBasicExpression.number((int)value));
				}
				break;
			
			case STRING:
			case CLASS:
				// Get temporary, needed for object storage
				temp = codeVars.temporary(0);
				
				// Load string or class
				if (__value.type() == ConstantValueType.STRING)
					__block.variableSetViaFunction(temp.tempIndex(),
						JvmFunctions.NVM_LOOKUP_STRING_INTO_TEMP,
						codeVars.currentFrame(),
						codeVars.linkageReference(this.linkTable.string(
							__value.boxedValue().toString()), 
							"stringObject"));
				else
					__block.variableSetViaFunction(temp.tempIndex(),
						JvmFunctions.NVM_LOOKUP_CLASS_OBJECT_INTO_TEMP,
						codeVars.currentFrame(),
						codeVars.linkageReference(this.linkTable.classObject(
							__value.boxedValue().toString()),
							"classObject"));
				
				// Push it
				__block.functionCall(
					JvmFunctions.NVM_STACK_PUSH_REFERENCE_FROM_TEMP,
					codeVars.currentFrame(),
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
	 * Performs software math on a type, these get mapped to static invocations
	 * instead.
	 * 
	 * @param __block The block to write to.
	 * @param __type The type of math being performed.
	 * @param __mathOp The operation being performed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doMathSoft(CFunctionBlock __block, JvmPrimitiveType __type,
		CMathOperator __mathOp)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null || __mathOp == null)
			throw new NullPointerException("NARG");
		
		// Just do a completely normal invocation here!
		this.__doInvoke(__block, JvmInvokeType.STATIC,
			__type.softMath(__mathOp));
	}
	
	/**
	 * Enters or exits a monitor.
	 *
	 * @param __block The block to enter.
	 * @param __enter Entering the monitor?
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doMonitor(CFunctionBlock __block, boolean __enter)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in reference
		JvmTemporary instance = codeVars.temporary(0);
		__block.variableSetViaFunction(instance.tempIndex(),
			JvmFunctions.NVM_STACK_POP_REFERENCE_TO_TEMP,
			codeVars.currentFrame());
		
		// Perform monitor logic
		this.__monitor(__block, __enter,
			instance.accessTemp(JvmTypes.JOBJECT));
	}
	
	/**
	 * Negates an integer. 
	 *
	 * @param __block The block to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doNegativeInteger(CFunctionBlock __block)
		throws IOException, NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in value
		CExpression value = codeVars.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(value,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVars.currentFrame());
		
		// Push back on, but negate it
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
			codeVars.currentFrame(),
			CExpressionBuilder.builder()
				.negative(value)
				.build());
	}
	
	/**
	 * Negates a value via software. 
	 *
	 * @param __block The block to write to.
	 * @param __type The type being handled.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doNegativeSoft(CFunctionBlock __block,
		JvmPrimitiveType __type)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Just do a completely normal invocation here!
		this.__doInvoke(__block, JvmInvokeType.STATIC,
			__type.softNegative());
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
			codeVars.currentFrame(),
			codeVars.linkageReference(this.linkTable.classObject(
				__what), "classObject"));
		
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
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in length
		CExpression length = codeVars.temporary(0)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(length,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVars.currentFrame());
		
		// Perform allocation
		JvmTemporary object = codeVars.temporary(1);
		__block.variableSetViaFunction(object.tempIndex(),
			JvmFunctions.NVM_NEW_ARRAY_INTO_TEMP,
			codeVars.currentFrame(),
			codeVars.linkageReference(this.linkTable.classObject(
				__componentType), "classObject"),
			length);
		
		// Did this throw anything?
		this.__checkThrow(__block);
		
		// Push to the stack
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_REFERENCE_FROM_TEMP,
			codeVars.currentFrame(),
			object.tempIndex());
	}
	
	/**
	 * Allocates a multi instance array.
	 *
	 * @param __block The block to write to.
	 * @param __type The type of array.
	 * @param __dimensions The dimensions in the class.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doNewArrayMulti(CFunctionBlock __block, ClassName __type,
		int __dimensions)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Get class instance for the target
		JvmTemporary classObject = codeVars.temporary(0);
		__block.variableSetViaFunction(classObject.tempIndex(),
			JvmFunctions.NVM_LOOKUP_CLASS_OBJECT_INTO_TEMP,
			codeVars.currentFrame(),
			codeVars.linkageReference(this.linkTable.classObject(
				__type), "classObject"));
		
		// Determine the method call actually used
		FieldDescriptor[] args = new FieldDescriptor[__dimensions + 1];
		args[0] = new ClassName("java/lang/Class").field();
		for (int i = 0; i < __dimensions; i++)
			args[i + 1] = FieldDescriptor.INTEGER;
		
		// Just do a completely normal invocation here!
		MethodDescriptor descriptor = new MethodDescriptor(
			__type.field().addDimensions(__dimensions), args);
		this.__doInvoke(__block, JvmInvokeType.STATIC,
			new MethodReference(
				"cc/squirreljme/runtime/cldc/lang/ArrayUtils",
				"multiANewArray",
				descriptor,
				false));
	}
	
	/**
	 * Store integer to local variable.
	 *
	 * @param __block The block to write to.
	 * @param __store Store value?
	 * @param __type The type to store.
	 * @param __localDx The local to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	private void __doPrimitiveLoadStore(CFunctionBlock __block,
		boolean __store, JvmPrimitiveType __type, int __localDx)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Which function to actually call?
		JvmFunctions function;
		if (__store)
			switch (__type)
			{
				case INTEGER:
					function = JvmFunctions.NVM_LOCAL_PUSH_INTEGER;
					break;
					
				case LONG:
					function = JvmFunctions.NVM_LOCAL_PUSH_LONG;
					break;
				
				case FLOAT:
					function = JvmFunctions.NVM_LOCAL_PUSH_FLOAT;
					break;
				
				case DOUBLE:
					function = JvmFunctions.NVM_LOCAL_PUSH_DOUBLE;
					break;
					
				default:
					throw Debugging.oops();
			}
		else
			switch (__type)
			{
				case INTEGER:
					function = JvmFunctions.NVM_LOCAL_POP_INTEGER;
					break;
					
				case LONG:
					function = JvmFunctions.NVM_LOCAL_POP_LONG;
					break;
				
				case FLOAT:
					function = JvmFunctions.NVM_LOCAL_POP_FLOAT;
					break;
				
				case DOUBLE:
					function = JvmFunctions.NVM_LOCAL_POP_DOUBLE;
					break;
					
				default:
					throw Debugging.oops();
			}
		
		// Pop over
		__block.functionCall(function,
			codeVars.currentFrame(),
			CBasicExpression.number(__localDx));
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
			codeVars.currentFrame(),
			CVariable.NULL);
		
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
			codeVars.currentFrame(),
			value.referenceTemp(JvmTypes.ANY));
		
		// Jump to the outro directly
		__block.gotoLabel(Constants.OUTRO_LABEL);
	}
	
	/**
	 * Shifts integer value.
	 *
	 * @param __block The block to write to.
	 * @param __op The operation to use while shifting.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doShiftInteger(CFunctionBlock __block, JvmShiftOp __op)
		throws IOException, NullPointerException
	{
		if (__block == null || __op == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Read in shift amount and value
		CExpression rawShiftBy = codeVars.temporary(0)
			.access(JvmTypes.JINT);
		CExpression value = codeVars.temporary(1)
			.access(JvmTypes.JINT);
		__block.variableSetViaFunction(rawShiftBy,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVars.currentFrame());
		__block.variableSetViaFunction(value,
			JvmFunctions.NVM_STACK_POP_INTEGER,
			codeVars.currentFrame());
		
		// The right shift amount is always AND the maximum bit count
		CExpression shiftBy = CExpressionBuilder.builder()
			.math(rawShiftBy, CMathOperator.AND,
				CBasicExpression.number(31))
			.build();
		
		// Determine the expression used for the shift
		CRootExpressionBuilder expression = CExpressionBuilder.builder();
		switch (__op)
		{
			case SIGNED_SHIFT_LEFT:
				expression.math(value, CMathOperator.SHIFT_LEFT, shiftBy);
				break;
				
			case SIGNED_SHIFT_RIGHT:
				expression.math(value, CMathOperator.SHIFT_RIGHT, shiftBy);
				break;
				
			case UNSIGNED_SHIFT_RIGHT:
				expression.cast(JvmTypes.JINT,
					CExpressionBuilder.builder()
						.math(CExpressionBuilder.builder()
								.cast(CStdIntType.UINT32, value)
							.build(),
							CMathOperator.SHIFT_RIGHT,
							shiftBy)
						.build());
				break;
				
			default:
				throw Debugging.oops();
		}
		
		// Do the actual shift
		__block.functionCall(JvmFunctions.NVM_STACK_PUSH_INTEGER,
			codeVars.currentFrame(),
			expression.build());
	}
	
	/**
	 * Shifts integer value.
	 *
	 * @param __block The block to write to.
	 * @param __type The type of value being shifted.
	 * @param __op The operation to use while shifting.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	private void __doShiftSoftware(CFunctionBlock __block,
		JvmPrimitiveType __type, JvmShiftOp __op)
		throws IOException, NullPointerException
	{
		if (__block == null || __type == null || __op == null)
			throw new NullPointerException("NARG");
		
		// Just do a completely normal invocation here!
		this.__doInvoke(__block, JvmInvokeType.STATIC,
			__type.softShift(__op));
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
				in[sourceSlot].referenceTemp(JvmTypes.ANY));
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
					.member("pc"))
			.build(),
			CExpressionBuilder.builder()
					.number(__targetGroupId)
				.build());
	}
	
	/**
	 * Performs monitor entry or exit.
	 *
	 * @param __block The block to write to.
	 * @param __enter Enter the monitor?
	 * @param __instance The instance to enter on.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/19
	 */
	private void __monitor(CFunctionBlock __block, boolean __enter,
		CExpression __instance)
		throws IOException, NullPointerException
	{
		if (__block == null || __instance == null)
			throw new NullPointerException("NARG");
		
		__CodeVariables__ codeVars = this.__codeVars();
		
		// Perform monitor change
		__block.functionCall(JvmFunctions.NVM_MONITOR,
			codeVars.currentFrame(), __instance,
			(__enter ? Constants.TRUE : Constants.FALSE));
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
			case InstructionIndex.IF_ACMPEQ:
				return CComparison.EQUALS;
				
			case InstructionIndex.IFNE:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IF_ACMPNE:
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
		
		/* {@squirreljme.error NC71 Unknown comparison operation.} */
		throw new NoSuchElementException("NC71");
	}
	
	/**
	 * Returns the comparison operation.
	 *
	 * @param __op The operation to get for.
	 * @return The comparison operation.
	 * @since 2023/07/16
	 */
	private static JvmCompareOp __commonCompareOp(int __op)
	{
		switch (__op)
		{
			case InstructionIndex.LCMP:
				return JvmCompareOp.CMP;
				
			case InstructionIndex.FCMPG:
			case InstructionIndex.DCMPG:
				return JvmCompareOp.CMPG;
				
			case InstructionIndex.FCMPL:
			case InstructionIndex.DCMPL:
				return JvmCompareOp.CMPL;
		}
		
		/* {@squirreljme.error NC74 Unknown comparison operation.} */
		throw new NoSuchElementException("NC74");
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
		
		/* {@squirreljme.error NC98 Unknown operation.} */
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
		return ByteCodeProcessor.__commonPrimitive(__op, false);
	}
	
	/**
	 * Returns the primitive to use for the given operation.
	 * 
	 * @param __op The operation.
	 * @param __second Second value?
	 * @return The primitive for the given operation.
	 * @throws NoSuchElementException If the operation is not valid.
	 * @since 2023/07/16
	 */
	private static JvmPrimitiveType __commonPrimitive(int __op,
		boolean __second)
		throws NoSuchElementException
	{
		// Second value?
		if (__second)
			switch (__op)
			{
				case InstructionIndex.I2B:
					return JvmPrimitiveType.BOOLEAN_OR_BYTE;
					
				case InstructionIndex.I2S:
					return JvmPrimitiveType.SHORT;
					
				case InstructionIndex.I2C:
					return JvmPrimitiveType.CHARACTER;
					
				case InstructionIndex.L2I:
				case InstructionIndex.F2I:
				case InstructionIndex.D2I:
					return JvmPrimitiveType.INTEGER;
					
				case InstructionIndex.I2L:
				case InstructionIndex.F2L:
				case InstructionIndex.D2L:
					return JvmPrimitiveType.LONG;
					
				case InstructionIndex.I2F:
				case InstructionIndex.L2F:
				case InstructionIndex.D2F:
					return JvmPrimitiveType.FLOAT;
					
				case InstructionIndex.I2D:
				case InstructionIndex.L2D:
				case InstructionIndex.F2D:
					return JvmPrimitiveType.DOUBLE;
			}
		
		// First value
		else
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
				case InstructionIndex.WIDE_ILOAD:
				case InstructionIndex.WIDE_ISTORE:
				case InstructionIndex.IADD:
				case InstructionIndex.ISUB:
				case InstructionIndex.IMUL:
				case InstructionIndex.IDIV:
				case InstructionIndex.IREM:
				case InstructionIndex.IAND:
				case InstructionIndex.IOR:
				case InstructionIndex.IXOR:
				case InstructionIndex.I2L:
				case InstructionIndex.I2F:
				case InstructionIndex.I2D:
				case InstructionIndex.I2B:
				case InstructionIndex.I2S:
				case InstructionIndex.I2C:
				case InstructionIndex.ISHL:
				case InstructionIndex.ISHR:
				case InstructionIndex.IUSHR:
					return JvmPrimitiveType.INTEGER;
					
				case InstructionIndex.LALOAD:
				case InstructionIndex.LASTORE:
				case InstructionIndex.WIDE_LLOAD:
				case InstructionIndex.WIDE_LSTORE:
				case InstructionIndex.LADD:
				case InstructionIndex.LSUB:
				case InstructionIndex.LMUL:
				case InstructionIndex.LDIV:
				case InstructionIndex.LREM:
				case InstructionIndex.LAND:
				case InstructionIndex.LOR:
				case InstructionIndex.LXOR:
				case InstructionIndex.LCMP:
				case InstructionIndex.L2I:
				case InstructionIndex.L2F:
				case InstructionIndex.L2D:
				case InstructionIndex.LSHL:
				case InstructionIndex.LSHR:
				case InstructionIndex.LUSHR:
				case InstructionIndex.LNEG:
					return JvmPrimitiveType.LONG;
					
				case InstructionIndex.FALOAD:
				case InstructionIndex.FASTORE:
				case InstructionIndex.WIDE_FLOAD:
				case InstructionIndex.WIDE_FSTORE:
				case InstructionIndex.FADD:
				case InstructionIndex.FSUB:
				case InstructionIndex.FMUL:
				case InstructionIndex.FDIV:
				case InstructionIndex.FREM:
				case InstructionIndex.FCMPG:
				case InstructionIndex.FCMPL:
				case InstructionIndex.F2I:
				case InstructionIndex.F2L:
				case InstructionIndex.F2D:
				case InstructionIndex.FNEG:
					return JvmPrimitiveType.FLOAT;
					
				case InstructionIndex.DALOAD:
				case InstructionIndex.DASTORE:
				case InstructionIndex.WIDE_DLOAD:
				case InstructionIndex.WIDE_DSTORE:
				case InstructionIndex.DADD:
				case InstructionIndex.DSUB:
				case InstructionIndex.DMUL:
				case InstructionIndex.DDIV:
				case InstructionIndex.DREM:
				case InstructionIndex.DCMPG:
				case InstructionIndex.DCMPL:
				case InstructionIndex.D2I:
				case InstructionIndex.D2L:
				case InstructionIndex.D2F:
				case InstructionIndex.DNEG:
					return JvmPrimitiveType.DOUBLE;
			}
		
		/* {@squirreljme.error NC99 Unknown operation.} */
		throw new NoSuchElementException("NC99");
	}
	
	/**
	 * Returns the shift operation used for the given operation.
	 *
	 * @param __op The operation used.
	 * @return The shift operation that is used.
	 * @throws NoSuchElementException On null arguments.
	 * @since 2023/07/16
	 */
	private static JvmShiftOp __commonShiftOp(int __op)
		throws NoSuchElementException
	{
		switch (__op)
		{
			case InstructionIndex.ISHL:
			case InstructionIndex.LSHL:
				return JvmShiftOp.SIGNED_SHIFT_LEFT;
				
			case InstructionIndex.ISHR:
			case InstructionIndex.LSHR:
				return JvmShiftOp.SIGNED_SHIFT_RIGHT;
				
			case InstructionIndex.IUSHR:
			case InstructionIndex.LUSHR:
				return JvmShiftOp.UNSIGNED_SHIFT_RIGHT;
		}
		
		/* {@squirreljme.error NC9l Unknown operation.} */
		throw new NoSuchElementException("NC9l");
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
			case InstructionIndex.AALOAD:
			case InstructionIndex.BALOAD:
			case InstructionIndex.SALOAD:
			case InstructionIndex.CALOAD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.DALOAD:
				return false;
				
			case InstructionIndex.AASTORE:
			case InstructionIndex.BASTORE:
			case InstructionIndex.SASTORE:
			case InstructionIndex.CASTORE:
			case InstructionIndex.IASTORE:
			case InstructionIndex.LASTORE:
			case InstructionIndex.FASTORE:
			case InstructionIndex.DASTORE:
				return true;
		}
		
		/* {@squirreljme.error NC9a Unknown operation.} */
		throw new NoSuchElementException("NC9a");
	}
}
