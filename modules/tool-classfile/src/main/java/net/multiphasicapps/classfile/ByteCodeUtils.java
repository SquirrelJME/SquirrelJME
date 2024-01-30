// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerList;
import java.util.Arrays;

/**
 * Byte code utilities.
 *
 * @since 2024/01/30
 */
public final class ByteCodeUtils
{
	/**
	 * Not used.
	 *
	 * @since 2024/01/30
	 */
	private ByteCodeUtils()
	{
	}
	
	/**
	 * Returns the length of the operation at the given address.
	 *
	 * @param __code The method byte code.
	 * @param __codeOff Offset into the byte code.
	 * @param __a The address of the instruction to get the length of.
	 * @param __last Optional output which gets the last address?
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/17
	 */
	public static int instructionLength(byte[] __code, int __codeOff, int __a,
		int[] __last)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Real offset, since the code attribute is offset
		int aa = __a + __codeOff;
		
		// Read opcode and determine the base length
		int op = ByteCodeUtils.instructionOpCode(__code, __codeOff, __a);
		int rv = ByteCodeUtils.instructionOpCodeLength(op);
		
		// Depends on the operation
		switch (op)
		{
				/* {@squirreljme.error JC26 Unsupported instruction specified
				in the method byte code. (The operation; The address)} */
			case InstructionIndex.BREAKPOINT:
			case InstructionIndex.IMPDEP1:
			case InstructionIndex.IMPDEP2:
			case InstructionIndex.JSR:
			case InstructionIndex.JSR_W:
			case InstructionIndex.RET:
			case InstructionIndex.WIDE:
				throw new InvalidClassFormatException(
					String.format("JC26 %d %d", op, __a));
			
				/* {@squirreljme.error JC27 Invokedynamic is not supported in
				this virtual machine. (The address)} */
			case InstructionIndex.INVOKEDYNAMIC:
				throw new InvalidClassFormatException(
					String.format("JC27 %d", __a));
				
				// Operands with no arguments
			case InstructionIndex.AALOAD:
			case InstructionIndex.AASTORE:
			case InstructionIndex.ACONST_NULL:
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
			case InstructionIndex.ARETURN:
			case InstructionIndex.ARRAYLENGTH:
			case InstructionIndex.ASTORE_0:
			case InstructionIndex.ASTORE_1:
			case InstructionIndex.ASTORE_2:
			case InstructionIndex.ASTORE_3:
			case InstructionIndex.ATHROW:
			case InstructionIndex.BALOAD:
			case InstructionIndex.BASTORE:
			case InstructionIndex.CALOAD:
			case InstructionIndex.CASTORE:
			case InstructionIndex.D2F:
			case InstructionIndex.D2I:
			case InstructionIndex.D2L:
			case InstructionIndex.DADD:
			case InstructionIndex.DALOAD:
			case InstructionIndex.DASTORE:
			case InstructionIndex.DCMPG:
			case InstructionIndex.DCMPL:
			case InstructionIndex.DCONST_0:
			case InstructionIndex.DCONST_1:
			case InstructionIndex.DDIV:
			case InstructionIndex.DLOAD_0:
			case InstructionIndex.DLOAD_1:
			case InstructionIndex.DLOAD_2:
			case InstructionIndex.DLOAD_3:
			case InstructionIndex.DMUL:
			case InstructionIndex.DNEG:
			case InstructionIndex.DREM:
			case InstructionIndex.DRETURN:
			case InstructionIndex.DSTORE_0:
			case InstructionIndex.DSTORE_1:
			case InstructionIndex.DSTORE_2:
			case InstructionIndex.DSTORE_3:
			case InstructionIndex.DSUB:
			case InstructionIndex.DUP:
			case InstructionIndex.DUP2:
			case InstructionIndex.DUP2_X1:
			case InstructionIndex.DUP2_X2:
			case InstructionIndex.DUP_X1:
			case InstructionIndex.DUP_X2:
			case InstructionIndex.F2D:
			case InstructionIndex.F2I:
			case InstructionIndex.F2L:
			case InstructionIndex.FADD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.FASTORE:
			case InstructionIndex.FCMPG:
			case InstructionIndex.FCMPL:
			case InstructionIndex.FCONST_0:
			case InstructionIndex.FCONST_1:
			case InstructionIndex.FCONST_2:
			case InstructionIndex.FDIV:
			case InstructionIndex.FLOAD_0:
			case InstructionIndex.FLOAD_1:
			case InstructionIndex.FLOAD_2:
			case InstructionIndex.FLOAD_3:
			case InstructionIndex.FMUL:
			case InstructionIndex.FNEG:
			case InstructionIndex.FREM:
			case InstructionIndex.FRETURN:
			case InstructionIndex.FSTORE_0:
			case InstructionIndex.FSTORE_1:
			case InstructionIndex.FSTORE_2:
			case InstructionIndex.FSTORE_3:
			case InstructionIndex.FSUB:
			case InstructionIndex.I2B:
			case InstructionIndex.I2C:
			case InstructionIndex.I2D:
			case InstructionIndex.I2F:
			case InstructionIndex.I2L:
			case InstructionIndex.I2S:
			case InstructionIndex.IADD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.IAND:
			case InstructionIndex.IASTORE:
			case InstructionIndex.ICONST_0:
			case InstructionIndex.ICONST_1:
			case InstructionIndex.ICONST_2:
			case InstructionIndex.ICONST_3:
			case InstructionIndex.ICONST_4:
			case InstructionIndex.ICONST_5:
			case InstructionIndex.ICONST_M1:
			case InstructionIndex.IDIV:
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.ILOAD_3:
			case InstructionIndex.IMUL:
			case InstructionIndex.INEG:
			case InstructionIndex.IOR:
			case InstructionIndex.IREM:
			case InstructionIndex.IRETURN:
			case InstructionIndex.ISHL:
			case InstructionIndex.ISHR:
			case InstructionIndex.ISTORE_0:
			case InstructionIndex.ISTORE_1:
			case InstructionIndex.ISTORE_2:
			case InstructionIndex.ISTORE_3:
			case InstructionIndex.ISUB:
			case InstructionIndex.IUSHR:
			case InstructionIndex.IXOR:
			case InstructionIndex.L2D:
			case InstructionIndex.L2F:
			case InstructionIndex.L2I:
			case InstructionIndex.LADD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.LAND:
			case InstructionIndex.LASTORE:
			case InstructionIndex.LCMP:
			case InstructionIndex.LCONST_0:
			case InstructionIndex.LCONST_1:
			case InstructionIndex.LDIV:
			case InstructionIndex.LLOAD_0:
			case InstructionIndex.LLOAD_1:
			case InstructionIndex.LLOAD_2:
			case InstructionIndex.LLOAD_3:
			case InstructionIndex.LMUL:
			case InstructionIndex.LNEG:
			case InstructionIndex.LOR:
			case InstructionIndex.LREM:
			case InstructionIndex.LRETURN:
			case InstructionIndex.LSHL:
			case InstructionIndex.LSHR:
			case InstructionIndex.LSTORE_0:
			case InstructionIndex.LSTORE_1:
			case InstructionIndex.LSTORE_2:
			case InstructionIndex.LSTORE_3:
			case InstructionIndex.LSUB:
			case InstructionIndex.LUSHR:
			case InstructionIndex.LXOR:
			case InstructionIndex.MONITORENTER:
			case InstructionIndex.MONITOREXIT:
			case InstructionIndex.NOP:
			case InstructionIndex.POP:
			case InstructionIndex.POP2:
			case InstructionIndex.RETURN:
			case InstructionIndex.SALOAD:
			case InstructionIndex.SASTORE:
			case InstructionIndex.SWAP:
				break;
				
				// An additional byte
			case InstructionIndex.ALOAD:
			case InstructionIndex.ASTORE:
			case InstructionIndex.BIPUSH:
			case InstructionIndex.DLOAD:
			case InstructionIndex.DSTORE:
			case InstructionIndex.FLOAD:
			case InstructionIndex.FSTORE:
			case InstructionIndex.ILOAD:
			case InstructionIndex.ISTORE:
			case InstructionIndex.LDC:
			case InstructionIndex.LLOAD:
			case InstructionIndex.LSTORE:
			case InstructionIndex.NEWARRAY:
				rv++;
				break;
				
				// Operations with two bytes following
			case InstructionIndex.ANEWARRAY:
			case InstructionIndex.CHECKCAST:
			case InstructionIndex.GETFIELD:
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.GOTO:
			case InstructionIndex.IF_ACMPEQ:
			case InstructionIndex.IF_ACMPNE:
			case InstructionIndex.IFEQ:
			case InstructionIndex.IFGE:
			case InstructionIndex.IFGT:
			case InstructionIndex.IF_ICMPEQ:
			case InstructionIndex.IF_ICMPGE:
			case InstructionIndex.IF_ICMPGT:
			case InstructionIndex.IF_ICMPLE:
			case InstructionIndex.IF_ICMPLT:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IFLE:
			case InstructionIndex.IFLT:
			case InstructionIndex.IFNE:
			case InstructionIndex.IFNONNULL:
			case InstructionIndex.IFNULL:
			case InstructionIndex.IINC:
			case InstructionIndex.INSTANCEOF:
			case InstructionIndex.INVOKESPECIAL:
			case InstructionIndex.INVOKESTATIC:
			case InstructionIndex.INVOKEVIRTUAL:
			case InstructionIndex.LDC2_W:
			case InstructionIndex.LDC_W:
			case InstructionIndex.NEW:
			case InstructionIndex.PUTFIELD:
			case InstructionIndex.PUTSTATIC:
			case InstructionIndex.SIPUSH:
			case InstructionIndex.WIDE_ALOAD:
			case InstructionIndex.WIDE_ASTORE:
			case InstructionIndex.WIDE_DLOAD:
			case InstructionIndex.WIDE_DSTORE:
			case InstructionIndex.WIDE_FLOAD:
			case InstructionIndex.WIDE_FSTORE:
			case InstructionIndex.WIDE_ILOAD:
			case InstructionIndex.WIDE_ISTORE:
			case InstructionIndex.WIDE_LLOAD:
			case InstructionIndex.WIDE_LSTORE:
				rv += 2;
				break;
				
				// Three bytes
			case InstructionIndex.MULTIANEWARRAY:
				rv += 3;
				break;
				
				// Four bytes
			case InstructionIndex.INVOKEINTERFACE:
			case InstructionIndex.GOTO_W:
			case InstructionIndex.WIDE_IINC:
				rv += 4;
				break;
			
				// Table switch, the length of this instruction varies due to
				// alignment and the count contained within
			case InstructionIndex.TABLESWITCH:
				//             tusaddr +4    +8
			    // +0          +4      +8    +12    [+16x4         ]...
				// op  x  x  x default lowdx highdx [(highdx-lowdx)]...
				//    op  x  x default lowdx highdx [(highdx-lowdx)]...
				//       op  x default lowdx highdx [(highdx-lowdx)]...
				//          op default lowdx highdx [(highdx-lowdx)]...
				// tuspadlen includes the opcode
				int tusaddr = ((aa + 4) & (~3)),
					tuspadlen = tusaddr - aa;
				rv = tuspadlen + 12 + (4 *
					(Instruction.__readInt(__code, tusaddr + 8) -
					Instruction.__readInt(__code, tusaddr + 4) + 1));
				break;
				
				// Lookup switch, the length of this instruction varies due to
				// alignment and the number of contained entries.
			case InstructionIndex.LOOKUPSWITCH:
				// The instruction is in this format:
				//             lusaddr +4    +8
				// +0          +4      +8    [+12x8       ]...
				// op  x  x  x default count [match offset]...
				//    op  x  x default count [match offset]...
				//       op  x default count [match offset]...
				//          op default count [match offset]...
				// luspadlen includes the opcode
				int lusaddr = ((aa + 4) & (~3)),
					luspadlen = lusaddr - aa;
				rv = luspadlen + 8 + (8 * Instruction.__readInt(__code,
					lusaddr + 4));
				break;
			
				/* {@squirreljme.error JC28 Cannot get the length of the
				specified operation because it is not valid. (The operation;
				The address; The operation before this one)} */
			default:
				throw new InvalidClassFormatException(
					String.format("JC28 %d %d %d", op, __a,
					((__last != null && __last.length > 0) ? __last[0] : -1)));
		}
		
		// Set last
		if (__last != null && __last.length > 0)
			__last[0] = op;
		
		return rv;
	}
	
	/**
	 * Returns the op code of the given instruction.
	 *
	 * @param __code The byte code to read from.
	 * @param __codeOff The code offset.
	 * @param __a The address of the instruction.
	 * @return The resultant opcode.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public static int instructionOpCode(byte[] __code, int __codeOff, int __a)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Read operation
		int op = (__code[__codeOff + __a] & 0xFF);
		if (op == InstructionIndex.WIDE)
		{
			/* {@squirreljme.error JC25 The wide instruction cannot be the
			last instruction in a method. (The address)} */
			if (__codeOff + __a + 1 >= __code.length)
				throw new InvalidClassFormatException(
					String.format("JC25 %d", __a));
			
			return (op << 8) | (__code[__codeOff + __a + 1] & 0xFF);
		}
		
		return op;
	}
	
	/**
	 * Determines the length of the opcode instruction.
	 *
	 * @param __code The byte code to read from.
	 * @param __codeOff The code offset.
	 * @param __a The address of the instruction.
	 * @return The length of the opcode.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public static int instructionOpCodeLength(byte[] __code, int __codeOff,
		int __a)
		throws InvalidClassFormatException, NullPointerException
	{
		return ByteCodeUtils.instructionOpCodeLength(
			ByteCodeUtils.instructionOpCode(__code, __codeOff, __a));
	}
	
	/**
	 * Determines the length of the opcode instruction.
	 *
	 * @param __opCode The opcode instruction.
	 * @return The length of the opcode.
	 * @since 2024/01/30
	 */
	public static int instructionOpCodeLength(int __opCode)
	{
		// Determine the base length of it
		if ((__opCode >>> 8) == InstructionIndex.WIDE)
			return 2;
		return 1;
	}
	
	/**
	 * Returns the arguments for the given instruction.
	 *
	 * @param __code The byte code to read from.
	 * @param __codeOff The code offset.
	 * @param __a The address of the instruction.
	 * @return The resultant argument types.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public static InstructionRawArgumentType[] instructionRawArguments(
		byte[] __code, int __codeOff, int __a)
		throws InvalidClassFormatException, NullPointerException
	{
		return ByteCodeUtils.instructionRawArguments(
			ByteCodeUtils.instructionOpCode(__code, __codeOff, __a), __a);
	}
	
	/**
	 * Returns the arguments for the given instruction.
	 *
	 * @param __opCode The opcode.
	 * @param __a The address of the instruction.
	 * @return The resultant argument types.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public static InstructionRawArgumentType[] instructionRawArguments(
		int __opCode, int __a)
		throws InvalidClassFormatException, NullPointerException
	{
		// Depends on the operation
		switch (__opCode)
		{
				// No arguments
			case InstructionIndex.ATHROW:
			case InstructionIndex.ARETURN:
			case InstructionIndex.DRETURN:
			case InstructionIndex.FRETURN:
			case InstructionIndex.IRETURN:
			case InstructionIndex.LRETURN:
			case InstructionIndex.RETURN:
			case InstructionIndex.AALOAD:
			case InstructionIndex.AASTORE:
			case InstructionIndex.ACONST_NULL:
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
			case InstructionIndex.ARRAYLENGTH:
			case InstructionIndex.ASTORE_0:
			case InstructionIndex.ASTORE_1:
			case InstructionIndex.ASTORE_2:
			case InstructionIndex.ASTORE_3:
			case InstructionIndex.BALOAD:
			case InstructionIndex.BASTORE:
			case InstructionIndex.CALOAD:
			case InstructionIndex.CASTORE:
			case InstructionIndex.D2F:
			case InstructionIndex.D2I:
			case InstructionIndex.D2L:
			case InstructionIndex.DADD:
			case InstructionIndex.DALOAD:
			case InstructionIndex.DASTORE:
			case InstructionIndex.DCMPG:
			case InstructionIndex.DCMPL:
			case InstructionIndex.DCONST_0:
			case InstructionIndex.DCONST_1:
			case InstructionIndex.DDIV:
			case InstructionIndex.DLOAD_0:
			case InstructionIndex.DLOAD_1:
			case InstructionIndex.DLOAD_2:
			case InstructionIndex.DLOAD_3:
			case InstructionIndex.DMUL:
			case InstructionIndex.DNEG:
			case InstructionIndex.DREM:
			case InstructionIndex.DSTORE_0:
			case InstructionIndex.DSTORE_1:
			case InstructionIndex.DSTORE_2:
			case InstructionIndex.DSTORE_3:
			case InstructionIndex.DSUB:
			case InstructionIndex.DUP:
			case InstructionIndex.DUP2:
			case InstructionIndex.DUP2_X1:
			case InstructionIndex.DUP2_X2:
			case InstructionIndex.DUP_X1:
			case InstructionIndex.DUP_X2:
			case InstructionIndex.F2D:
			case InstructionIndex.F2I:
			case InstructionIndex.F2L:
			case InstructionIndex.FADD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.FASTORE:
			case InstructionIndex.FCMPG:
			case InstructionIndex.FCMPL:
			case InstructionIndex.FCONST_0:
			case InstructionIndex.FCONST_1:
			case InstructionIndex.FCONST_2:
			case InstructionIndex.FDIV:
			case InstructionIndex.FLOAD_0:
			case InstructionIndex.FLOAD_1:
			case InstructionIndex.FLOAD_2:
			case InstructionIndex.FLOAD_3:
			case InstructionIndex.FMUL:
			case InstructionIndex.FNEG:
			case InstructionIndex.FREM:
			case InstructionIndex.FSTORE_0:
			case InstructionIndex.FSTORE_1:
			case InstructionIndex.FSTORE_2:
			case InstructionIndex.FSTORE_3:
			case InstructionIndex.FSUB:
			case InstructionIndex.I2B:
			case InstructionIndex.I2C:
			case InstructionIndex.I2D:
			case InstructionIndex.I2F:
			case InstructionIndex.I2L:
			case InstructionIndex.I2S:
			case InstructionIndex.IADD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.IAND:
			case InstructionIndex.IASTORE:
			case InstructionIndex.ICONST_0:
			case InstructionIndex.ICONST_1:
			case InstructionIndex.ICONST_2:
			case InstructionIndex.ICONST_3:
			case InstructionIndex.ICONST_4:
			case InstructionIndex.ICONST_5:
			case InstructionIndex.ICONST_M1:
			case InstructionIndex.IDIV:
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.ILOAD_3:
			case InstructionIndex.IMUL:
			case InstructionIndex.INEG:
			case InstructionIndex.IOR:
			case InstructionIndex.IREM:
			case InstructionIndex.ISHL:
			case InstructionIndex.ISHR:
			case InstructionIndex.ISTORE_0:
			case InstructionIndex.ISTORE_1:
			case InstructionIndex.ISTORE_2:
			case InstructionIndex.ISTORE_3:
			case InstructionIndex.ISUB:
			case InstructionIndex.IUSHR:
			case InstructionIndex.IXOR:
			case InstructionIndex.L2D:
			case InstructionIndex.L2F:
			case InstructionIndex.L2I:
			case InstructionIndex.LADD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.LAND:
			case InstructionIndex.LASTORE:
			case InstructionIndex.LCMP:
			case InstructionIndex.LCONST_0:
			case InstructionIndex.LCONST_1:
			case InstructionIndex.LDIV:
			case InstructionIndex.LLOAD_0:
			case InstructionIndex.LLOAD_1:
			case InstructionIndex.LLOAD_2:
			case InstructionIndex.LLOAD_3:
			case InstructionIndex.LMUL:
			case InstructionIndex.LNEG:
			case InstructionIndex.LOR:
			case InstructionIndex.LREM:
			case InstructionIndex.LSHL:
			case InstructionIndex.LSHR:
			case InstructionIndex.LSTORE_0:
			case InstructionIndex.LSTORE_1:
			case InstructionIndex.LSTORE_2:
			case InstructionIndex.LSTORE_3:
			case InstructionIndex.LSUB:
			case InstructionIndex.LUSHR:
			case InstructionIndex.LXOR:
			case InstructionIndex.MONITORENTER:
			case InstructionIndex.MONITOREXIT:
			case InstructionIndex.NOP:
			case InstructionIndex.POP:
			case InstructionIndex.POP2:
			case InstructionIndex.SALOAD:
			case InstructionIndex.SASTORE:
			case InstructionIndex.SWAP:
				return new InstructionRawArgumentType[0];
				
				// Single signed byte
			case InstructionIndex.BIPUSH:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.SIGNED_BYTE
					};
				
				// Single unsigned byte
			case InstructionIndex.ALOAD:
			case InstructionIndex.ILOAD:
			case InstructionIndex.LLOAD:
			case InstructionIndex.FLOAD:
			case InstructionIndex.DLOAD:
			case InstructionIndex.ASTORE:
			case InstructionIndex.ISTORE:
			case InstructionIndex.LSTORE:
			case InstructionIndex.FSTORE:
			case InstructionIndex.DSTORE:
			case InstructionIndex.LDC:
			case InstructionIndex.NEWARRAY:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.UNSIGNED_BYTE
					};
				
				// Single signed short
			case InstructionIndex.SIPUSH:
			case InstructionIndex.GOTO:
			case InstructionIndex.IFNONNULL:
			case InstructionIndex.IFNULL:
			case InstructionIndex.IF_ACMPEQ:
			case InstructionIndex.IF_ACMPNE:
			case InstructionIndex.IF_ICMPEQ:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IF_ICMPLT:
			case InstructionIndex.IF_ICMPGE:
			case InstructionIndex.IF_ICMPGT:
			case InstructionIndex.IF_ICMPLE:
			case InstructionIndex.IFEQ:
			case InstructionIndex.IFNE:
			case InstructionIndex.IFLT:
			case InstructionIndex.IFGE:
			case InstructionIndex.IFGT:
			case InstructionIndex.IFLE:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.SIGNED_SHORT
					};
				
				// Single Unsigned short
			case InstructionIndex.ANEWARRAY:
			case InstructionIndex.CHECKCAST:
			case InstructionIndex.INSTANCEOF:
			case InstructionIndex.NEW:
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.PUTSTATIC:
			case InstructionIndex.GETFIELD:
			case InstructionIndex.PUTFIELD:
			case InstructionIndex.INVOKEINTERFACE:
			case InstructionIndex.INVOKESPECIAL:
			case InstructionIndex.INVOKESTATIC:
			case InstructionIndex.INVOKEVIRTUAL:
			case InstructionIndex.LDC_W:
			case InstructionIndex.LDC2_W:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.UNSIGNED_SHORT
					};
				
				// Unsigned byte + byte
			case InstructionIndex.IINC:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.UNSIGNED_BYTE,
						InstructionRawArgumentType.SIGNED_BYTE
					};
				
			// Unsigned short + short
			case InstructionIndex.WIDE_IINC:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.UNSIGNED_SHORT,
						InstructionRawArgumentType.SIGNED_SHORT
					};
				
				// Unsigned short + unsigned byte
			case InstructionIndex.MULTIANEWARRAY:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.UNSIGNED_SHORT,
						InstructionRawArgumentType.UNSIGNED_BYTE
					};
				
				// Lookup switch lookup table
			case InstructionIndex.LOOKUPSWITCH:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.padding(
							((__a + 4) & (~3))),
						InstructionRawArgumentType.LOOKUPSWITCH
					};
				
				// Table switch lookup table
			case InstructionIndex.TABLESWITCH:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.padding(
							((__a + 4) & (~3))),
						InstructionRawArgumentType.TABLESWITCH
					};
				
				// Invoke dynamic, ignored
			case InstructionIndex.INVOKEDYNAMIC:
				return new InstructionRawArgumentType[]
					{
						InstructionRawArgumentType.UNSIGNED_SHORT,
						InstructionRawArgumentType.SIGNED_BYTE,
						InstructionRawArgumentType.SIGNED_BYTE
					};
				
				/* {@squirreljme.error JC37 The operation at the specified
				address is not supported yet. (The operation; The name of
				the operation; The address it is at)} */
			default:
				throw new RuntimeException(String.format("JC37 %d %s %d",
					__opCode, InstructionMnemonics.toString(__opCode), __a));
		}
	}
	
	/**
	 * Reads raw instruction arguments.
	 *
	 * @param __code The code to read from.
	 * @param __codeOff The code offset.
	 * @param __a The address of the instruction.
	 * @return The resultant raw arguments.
	 * @throws InvalidClassFormatException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public static int[] readRawArguments(
		byte[] __code, int __codeOff, int __a)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Get opcode length and argument types
		int readPos = __codeOff + __a + ByteCodeUtils.instructionOpCodeLength(
			__code, __codeOff, __a);
		InstructionRawArgumentType[] rawTypes =
			ByteCodeUtils.instructionRawArguments(__code, __codeOff, __a);
		
		// Read in arguments
		IntegerList result = new IntegerList();
		for (InstructionRawArgumentType rawType : rawTypes)
		{
			// Depends on the type
			switch (rawType)
			{
				case PADDING_0:
					break;
				
				case PADDING_1:
					readPos += 1;
					break;
				
				case PADDING_2:
					readPos += 2;
					break;
				
				case PADDING_3:
					readPos += 3;
					break;
				
				case SIGNED_BYTE:
					result.addInteger(Instruction.__readByte(__code,
						readPos));
					readPos += 1;
					break;
				
				case UNSIGNED_BYTE:
					result.addInteger(Instruction.__readUnsignedByte(__code,
						readPos));
					readPos += 1;
					break;
				
				case SIGNED_SHORT:
					result.addInteger(Instruction.__readShort(__code,
						readPos));
					readPos += 2;
					break;
				
				case UNSIGNED_SHORT:
					result.addInteger(Instruction.__readUnsignedShort(
						__code, readPos));
					readPos += 2;
					break;
				
				case INTEGER:
					result.addInteger(Instruction.__readInt(
						__code, readPos));
					readPos += 4;
					break;
				
				case LOOKUPSWITCH:
					// Default branch
					result.addInteger(Instruction.__readInt(
						__code, readPos));
					readPos += 4;
					
					// Number of pairs
					int numPairs = Instruction.__readInt(__code, readPos);
					readPos += 4;
					
					// Read in all pairs
					for (int i = 0; i < numPairs; i++)
					{
						result.addInteger(
							Instruction.__readInt(__code, readPos));
						readPos += 4;
						result.addInteger(
							Instruction.__readInt(__code, readPos));
						readPos += 4;
					}
					break;
				
				case TABLESWITCH:
					// Default branch
					result.addInteger(Instruction.__readInt(
						__code, readPos));
					readPos += 4;
					
					// Low value
					int lo = Instruction.__readInt(__code, readPos);
					result.addInteger(lo);
					readPos += 4;
					
					// High value
					int hi = Instruction.__readInt(__code, readPos);
					result.addInteger(hi);
					readPos += 4;
					
					// Read jump offsets
					for (int i = 0, n = (hi - lo) + 1; i < n; i++)
					{
						result.addInteger(Instruction.__readInt(
							__code, readPos));
						readPos += 4;
					}
					break;
					
				default:
					throw Debugging.todo();
			}
		}
		
		// Is the result base off?
		return result.toIntegerArray();
	}
}
