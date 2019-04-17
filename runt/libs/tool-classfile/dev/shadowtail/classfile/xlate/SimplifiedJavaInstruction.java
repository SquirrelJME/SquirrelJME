// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueDouble;
import net.multiphasicapps.classfile.ConstantValueFloat;
import net.multiphasicapps.classfile.ConstantValueInteger;
import net.multiphasicapps.classfile.ConstantValueLong;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InstructionMnemonics;
import net.multiphasicapps.classfile.LookupSwitch;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.classfile.TableSwitch;

/**
 * This represents a simplified Java instruction which modifies the operation
 * that was performed to combine and make it more aliased. The primary goal
 * of this class is to simplify the translator code so it does not have to know
 * about every operation.
 *
 * For example {@code ALOAD_2:[]} will become {@code ALOAD:[2]}.
 *
 * @since 2019/04/03
 */
public final class SimplifiedJavaInstruction
{
	/** Store operation. */
	public static final int STORE =
		-2;
	
	/** Load operation. */
	public static final int LOAD =
		-3;
	
	/** Value return operation. */
	public static final int VRETURN =
		-4;
	
	/** Math operation. */
	public static final int MATH =
		-5;
	
	/** If (zero) operation. */
	public static final int IF =
		-6;
	
	/** Value comparison. */
	public static final int IF_CMP =
		-7;
	
	/** Primitive array store. */
	public static final int PASTORE =
		-8;
	
	/** Primitive array load. */
	public static final int PALOAD =
		-9;
	
	/** Stack shuffle. */
	public static final int STACKSHUFFLE =
		-10;
	
	/** Convert. */
	public static final int CONVERT =
		-11;
	
	/** Perform math on constant. */
	public static final int MATH_CONST =
		-12;
	
	/** The operation. */
	protected final int op;
	
	/** The arguments. */
	private final Object[] _args;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Translates a regular instruction to a simplified instruction.
	 *
	 * @param __inst The instruction to translate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public SimplifiedJavaInstruction(Instruction __inst)
		throws NullPointerException
	{
		if (__inst == null)
			throw new NullPointerException("NARG");
		
		// Start with base operations
		int op = __inst.operation();
		Object[] args = __inst.arguments();
		
		// Possibly map the instruction
		int baseop = op;
		switch (op)
		{
			case InstructionIndex.ALOAD:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.OBJECT,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.OBJECT,
						baseop - InstructionIndex.ALOAD_0,
					};
				break;
				
			case InstructionIndex.ARETURN:
				op = SimplifiedJavaInstruction.VRETURN;
				args = new Object[]
					{
						DataType.OBJECT,
					};
				break;
			
			case InstructionIndex.ASTORE:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.OBJECT,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.ASTORE_0:
			case InstructionIndex.ASTORE_1:
			case InstructionIndex.ASTORE_2:
			case InstructionIndex.ASTORE_3:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.OBJECT,
						baseop - InstructionIndex.ASTORE_0,
					};
				break;
				
			case InstructionIndex.BALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.BYTE,
					};
				break;
				
			case InstructionIndex.BASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.BYTE,
					};
				break;
			
			case InstructionIndex.BIPUSH:
				op = InstructionIndex.LDC;
				args = new Object[]
					{
						new ConstantValueInteger(__inst.byteArgument(0)),
					};
				break;
				
			case InstructionIndex.CALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.CHARACTER,
					};
				break;
				
			case InstructionIndex.CASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.CHARACTER,
					};
				break;
			
			case InstructionIndex.D2F:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.DOUBLE,
						StackJavaType.FLOAT,
					};
				break;
			
			case InstructionIndex.D2I:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.DOUBLE,
						StackJavaType.INTEGER,
					};
				break;
			
			case InstructionIndex.D2L:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.DOUBLE,
						StackJavaType.LONG,
					};
				break;
			
			case InstructionIndex.DADD:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.ADD,
					};
				break;
				
			case InstructionIndex.DALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.DOUBLE,
					};
				break;
				
			case InstructionIndex.DASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.DOUBLE,
					};
				break;
			
			case InstructionIndex.DCMPG:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.CMPG,
					};
				break;
			
			case InstructionIndex.DCMPL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.CMPL,
					};
				break;
			
			case InstructionIndex.DCONST_0:
			case InstructionIndex.DCONST_1:
				op = InstructionIndex.LDC;
				args = new Object[]
					{
						new ConstantValueDouble(
							baseop - InstructionIndex.DCONST_0),
					};
				break;
				
			case InstructionIndex.DDIV:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.DIV,
					};
				break;
			
			
			case InstructionIndex.DLOAD:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.DOUBLE,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.DLOAD_0:
			case InstructionIndex.DLOAD_1:
			case InstructionIndex.DLOAD_2:
			case InstructionIndex.DLOAD_3:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.DOUBLE,
						baseop - InstructionIndex.DLOAD_0,
					};
				break;
			
			case InstructionIndex.DMUL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.MUL,
					};
				break;
				
			case InstructionIndex.DREM:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.REM,
					};
				break;
			
			case InstructionIndex.DRETURN:
				op = SimplifiedJavaInstruction.VRETURN;
				args = new Object[]
					{
						DataType.DOUBLE,
					};
				break;
			
			case InstructionIndex.DSTORE:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.DOUBLE,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.DSTORE_0:
			case InstructionIndex.DSTORE_1:
			case InstructionIndex.DSTORE_2:
			case InstructionIndex.DSTORE_3:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.DOUBLE,
						baseop - InstructionIndex.DSTORE_0,
					};
				break;
			
			case InstructionIndex.DSUB:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.DOUBLE,
						MathType.SUB,
					};
				break;
			
			case InstructionIndex.DUP:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.DUP,
					};
				break;
			
			case InstructionIndex.DUP_X1:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.DUP_X1,
					};
				break;
			
			case InstructionIndex.DUP_X2:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.DUP_X2,
					};
				break;
			
			case InstructionIndex.DUP2:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.DUP2,
					};
				break;
			
			case InstructionIndex.DUP2_X1:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.DUP2_X1,
					};
				break;
			
			case InstructionIndex.DUP2_X2:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.DUP2_X2,
					};
				break;
			
			case InstructionIndex.F2D:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.FLOAT,
						StackJavaType.DOUBLE,
					};
				break;
			
			case InstructionIndex.F2I:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.FLOAT,
						StackJavaType.INTEGER,
					};
				break;
			
			case InstructionIndex.F2L:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.FLOAT,
						StackJavaType.LONG,
					};
				break;
			
			case InstructionIndex.FADD:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.ADD,
					};
				break;
				
			case InstructionIndex.FALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.FLOAT,
					};
				break;
				
			case InstructionIndex.FASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.FLOAT,
					};
				break;
			
			case InstructionIndex.FCMPG:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.CMPG,
					};
				break;
			
			case InstructionIndex.FCMPL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.CMPL,
					};
				break;
			
			case InstructionIndex.FCONST_0:
			case InstructionIndex.FCONST_1:
			case InstructionIndex.FCONST_2:
				op = InstructionIndex.LDC;
				args = new Object[]
					{
						new ConstantValueFloat(
							baseop - InstructionIndex.FCONST_0),
					};
				break;
				
			case InstructionIndex.FDIV:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.DIV,
					};
				break;
			
			case InstructionIndex.FLOAD:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.FLOAT,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.FLOAD_0:
			case InstructionIndex.FLOAD_1:
			case InstructionIndex.FLOAD_2:
			case InstructionIndex.FLOAD_3:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.FLOAT,
						baseop - InstructionIndex.FLOAD_0,
					};
				break;
			
			case InstructionIndex.FMUL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.MUL,
					};
				break;
				
			case InstructionIndex.FREM:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.REM,
					};
				break;
				
			case InstructionIndex.FRETURN:
				op = SimplifiedJavaInstruction.VRETURN;
				args = new Object[]
					{
						DataType.FLOAT,
					};
				break;
			
			case InstructionIndex.FSTORE:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.FLOAT,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.FSTORE_0:
			case InstructionIndex.FSTORE_1:
			case InstructionIndex.FSTORE_2:
			case InstructionIndex.FSTORE_3:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.FLOAT,
						baseop - InstructionIndex.FSTORE_0,
					};
				break;
			
			case InstructionIndex.FSUB:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.FLOAT,
						MathType.SUB,
					};
				break;
			
			case InstructionIndex.GOTO_W:
				op = InstructionIndex.GOTO;
				break;
				
			case InstructionIndex.I2B:
				op = SimplifiedJavaInstruction.MATH_CONST;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.SIGN_X8,
						0,
					};
				break;
				
			case InstructionIndex.I2C:
				op = SimplifiedJavaInstruction.MATH_CONST;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.AND,
						0xFFFF,
					};
				break;
				
			case InstructionIndex.I2D:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.INTEGER,
						StackJavaType.DOUBLE,
					};
				break;
			
			case InstructionIndex.I2F:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.INTEGER,
						StackJavaType.FLOAT,
					};
				break;
			
			case InstructionIndex.I2L:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.INTEGER,
						StackJavaType.LONG,
					};
				break;
				
			case InstructionIndex.I2S:
				op = SimplifiedJavaInstruction.MATH_CONST;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.SIGN_HALF,
						0,
					};
				break;
				
			case InstructionIndex.IADD:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.ADD,
					};
				break;
				
			case InstructionIndex.IALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.INTEGER,
					};
				break;
				
			case InstructionIndex.IAND:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.AND,
					};
				break;
				
			case InstructionIndex.IASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.INTEGER,
					};
				break;
			
			case InstructionIndex.ICONST_M1:
			case InstructionIndex.ICONST_0:
			case InstructionIndex.ICONST_1:
			case InstructionIndex.ICONST_2:
			case InstructionIndex.ICONST_3:
			case InstructionIndex.ICONST_4:
			case InstructionIndex.ICONST_5:
				op = InstructionIndex.LDC;
				args = new Object[]
					{
						new ConstantValueInteger(
							baseop - InstructionIndex.ICONST_0),
					};
				break;
				
			case InstructionIndex.IDIV:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.DIV,
					};
				break;
				
			case InstructionIndex.IFEQ:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IFGE:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.GREATER_THAN_OR_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IFGT:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.GREATER_THAN,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IFLE:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.LESS_THAN_OR_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
			
			case InstructionIndex.IFLT:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.LESS_THAN,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
			
			case InstructionIndex.IFNE:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.NOT_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
			
			case InstructionIndex.IFNONNULL:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.OBJECT,
						CompareType.NOT_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
			
			case InstructionIndex.IFNULL:
				op = SimplifiedJavaInstruction.IF;
				args = new Object[]
					{
						DataType.OBJECT,
						CompareType.EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ACMPEQ:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.OBJECT,
						CompareType.EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ACMPNE:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.OBJECT,
						CompareType.NOT_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ICMPEQ:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ICMPGE:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.GREATER_THAN_OR_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ICMPGT:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.GREATER_THAN,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ICMPLE:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.LESS_THAN_OR_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ICMPLT:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.LESS_THAN,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
				
			case InstructionIndex.IF_ICMPNE:
				op = SimplifiedJavaInstruction.IF_CMP;
				args = new Object[]
					{
						DataType.INTEGER,
						CompareType.NOT_EQUALS,
						__inst.<InstructionJumpTarget>argument(0,
							InstructionJumpTarget.class),
					};
				break;
			
			case InstructionIndex.ILOAD:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.INTEGER,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.ILOAD_3:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.INTEGER,
						baseop - InstructionIndex.ILOAD_0,
					};
				break;
			
			case InstructionIndex.IMUL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.MUL,
					};
				break;
				
			case InstructionIndex.IOR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.OR,
					};
				break;
				
			case InstructionIndex.IREM:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.REM,
					};
				break;
				
			case InstructionIndex.IRETURN:
				op = SimplifiedJavaInstruction.VRETURN;
				args = new Object[]
					{
						DataType.INTEGER,
					};
				break;
			
			case InstructionIndex.ISHL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.SHL,
					};
				break;
				
			case InstructionIndex.ISHR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.SHR,
					};
				break;
			
			case InstructionIndex.ISTORE:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.INTEGER,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.ISTORE_0:
			case InstructionIndex.ISTORE_1:
			case InstructionIndex.ISTORE_2:
			case InstructionIndex.ISTORE_3:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.INTEGER,
						baseop - InstructionIndex.ISTORE_0,
					};
				break;
			
			case InstructionIndex.ISUB:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.SUB,
					};
				break;
				
			case InstructionIndex.IUSHR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.USHR,
					};
				break;
				
			case InstructionIndex.IXOR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.INTEGER,
						MathType.XOR,
					};
				break;
				
			case InstructionIndex.L2D:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.LONG,
						StackJavaType.DOUBLE,
					};
				break;
			
			case InstructionIndex.L2F:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.LONG,
						StackJavaType.FLOAT,
					};
				break;
			
			case InstructionIndex.L2I:
				op = SimplifiedJavaInstruction.CONVERT;
				args = new Object[]
					{
						StackJavaType.LONG,
						StackJavaType.INTEGER,
					};
				break;
				
			case InstructionIndex.LADD:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.ADD,
					};
				break;
				
			case InstructionIndex.LALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.LONG,
					};
				break;
				
			case InstructionIndex.LAND:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.AND,
					};
				break;
				
			case InstructionIndex.LASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.LONG,
					};
				break;
			
			case InstructionIndex.LCMP:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.CMPG,
					};
				break;
			
			case InstructionIndex.LCONST_0:
			case InstructionIndex.LCONST_1:
				op = InstructionIndex.LDC;
				args = new Object[]
					{
						new ConstantValueLong(
							baseop - InstructionIndex.LCONST_0),
					};
				break;
			
			case InstructionIndex.LDC2_W:
			case InstructionIndex.LDC_W:
				op = InstructionIndex.LDC;
				break;
				
			case InstructionIndex.LDIV:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.DIV,
					};
				break;
			
			case InstructionIndex.LLOAD:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.LONG,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.LLOAD_0:
			case InstructionIndex.LLOAD_1:
			case InstructionIndex.LLOAD_2:
			case InstructionIndex.LLOAD_3:
				op = SimplifiedJavaInstruction.LOAD;
				args = new Object[]
					{
						DataType.LONG,
						baseop - InstructionIndex.LLOAD_0,
					};
				break;
			
			case InstructionIndex.LMUL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.MUL,
					};
				break;
				
			case InstructionIndex.LOR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.OR,
					};
				break;
				
			case InstructionIndex.LREM:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.REM,
					};
				break;
				
			case InstructionIndex.LRETURN:
				op = SimplifiedJavaInstruction.VRETURN;
				args = new Object[]
					{
						DataType.LONG,
					};
				break;
				
			case InstructionIndex.LSHL:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.SHL,
					};
				break;
				
			case InstructionIndex.LSHR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.SHR,
					};
				break;
			
			case InstructionIndex.LSTORE:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.LONG,
						__inst.intArgument(0),
					};
				break;
			
			case InstructionIndex.LSTORE_0:
			case InstructionIndex.LSTORE_1:
			case InstructionIndex.LSTORE_2:
			case InstructionIndex.LSTORE_3:
				op = SimplifiedJavaInstruction.STORE;
				args = new Object[]
					{
						DataType.LONG,
						baseop - InstructionIndex.LSTORE_0,
					};
				break;
			
			case InstructionIndex.LSUB:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.SUB,
					};
				break;
				
			case InstructionIndex.LUSHR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.USHR,
					};
				break;
				
			case InstructionIndex.LXOR:
				op = SimplifiedJavaInstruction.MATH;
				args = new Object[]
					{
						DataType.LONG,
						MathType.XOR,
					};
				break;
			
				// Make all array allocations just use the class name
			case InstructionIndex.NEWARRAY:
				op = InstructionIndex.ANEWARRAY;
				args = new Object[]
					{
						ClassName.fromPrimitiveType(__inst.<PrimitiveType>
							argument(0, PrimitiveType.class)),
					};
				break;
			
			case InstructionIndex.POP:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.POP,
					};
				break;
			
			case InstructionIndex.POP2:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.POP2,
					};
				break;
				
			case InstructionIndex.SALOAD:
				op = SimplifiedJavaInstruction.PALOAD;
				args = new Object[]
					{
						PrimitiveType.SHORT,
					};
				break;
				
			case InstructionIndex.SASTORE:
				op = SimplifiedJavaInstruction.PASTORE;
				args = new Object[]
					{
						PrimitiveType.SHORT,
					};
				break;
			
			case InstructionIndex.SIPUSH:
				op = InstructionIndex.LDC;
				args = new Object[]
					{
						new ConstantValueInteger(__inst.shortArgument(0)),
					};
				break;
			
			case InstructionIndex.SWAP:
				op = SimplifiedJavaInstruction.STACKSHUFFLE;
				args = new Object[]
					{
						JavaStackShuffleType.SWAP,
					};
				break;
			
				// To reduce the number of handlers, convert table switches
				// to lookup switches
			case InstructionIndex.TABLESWITCH:
				op = InstructionIndex.LOOKUPSWITCH;
				args = new Object[]
					{
						__inst.<TableSwitch>argument(0, TableSwitch.class).
							asLookupSwitch(),
					};
				break;
			
			case InstructionIndex.WIDE_IINC:
				op = InstructionIndex.IINC;
				break;
		}
		
		// Store them
		this.op = op;
		this._args = args;
	}
	
	/**
	 * Returns the argument for the given index.
	 *
	 * @param __i The index of the argument.
	 * @return The argument.
	 * @throws IndexOutOfBoundsException If the argument index is not
	 * within bounds.
	 * @since 2019/03/24
	 */
	public Object argument(int __i)
		throws IndexOutOfBoundsException
	{
		return this._args[__i];
	}
	
	/**
	 * Returns the argument for the given index.
	 *
	 * @param <T> The type of argument to get.
	 * @param __i The index of the argument.
	 * @param __cl The class to cast to.
	 * @return The argument as the given class.
	 * @throws ClassCastException If the class is not of the given type.
	 * @throws IndexOutOfBoundsException If the argument index is not
	 * within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public <T> T argument(int __i, Class<T> __cl)
		throws ClassCastException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this._args[__i]);
	}
	
	/**
	 * The number of arguments the instruction takes.
	 *
	 * @return The argument count.
	 * @since 2019/04/03
	 */
	public final int argumentCount()
	{
		return this._args.length;
	}
	
	/**
	 * Returns all of the arguments.
	 *
	 * @return The arguments.
	 * @since 2019/04/03
	 */
	public final Object[] arguments()
	{
		return this._args.clone();
	}
	
	/**
	 * Obtains the given argument as a byte.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not an number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2019/04/03
	 */
	public byte byteArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).byteValue();
	}
	
	/**
	 * Obtains the given argument as an integer.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not an number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2019/04/03
	 */
	public int intArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).intValue();
	}
	
	/**
	 * Returns the potentially aliased operation.
	 *
	 * @return The operation.
	 * @since 2019/04/03
	 */
	public final int operation()
	{
		return this.op;
	}
	
	/**
	 * Obtains the given argument as a short.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not a number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2019/04/03
	 */
	public short shortArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).shortValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/03
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				SimplifiedJavaInstruction.mnemonic(this.op) + ":" +
				Arrays.asList(this._args)));
		
		return rv;
	}
	
	/**
	 * Returns the mnemonic of the operation.
	 *
	 * @param __op The operation to obtain.
	 * @return The mnemonic for the string.
	 * @since 2019/04/03
	 */
	public static final String mnemonic(int __op)
	{
		switch (__op)
		{
			case STORE:			return "STORE";
			case LOAD:			return "LOAD";
			case VRETURN:		return "VRETURN";
			case MATH:			return "MATH";
			case IF:			return "IF";
			case IF_CMP:		return "IF_CMP";
			case PASTORE:		return "PASTORE";
			case PALOAD:		return "PALOAD";
			case STACKSHUFFLE:	return "STACKSHUFFLE";
			case CONVERT:		return "CONVERT";
			case MATH_CONST:	return "MATH_CONST";
			
				// Fallback to standard stuff
			default:
				return InstructionMnemonics.toString(__op);
		}
	}
}

