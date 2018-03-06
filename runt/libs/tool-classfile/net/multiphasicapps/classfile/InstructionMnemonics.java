// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This contains instruction mnemonics.
 *
 * @since 2017/05/20
 */
public final class InstructionMnemonics
{
	/**
	 * Not used.
	 *
	 * @since 2017/05/29
	 */
	private InstructionMnemonics()
	{
	}
	
	/**
	 * Returns the instruction mnemonic.
	 *
	 * @param __op The operation to get.
	 * @return The mnemonic for the instruction.
	 * @since 2017/05/20
	 */
	public static String toString(int __op)
	{
		switch (__op)
		{
			case InstructionIndex.NOP: return "NOP";
			case InstructionIndex.ACONST_NULL: return "ACONST_NULL";
			case InstructionIndex.ICONST_M1: return "ICONST_M1";
			case InstructionIndex.ICONST_0: return "ICONST_0";
			case InstructionIndex.ICONST_1: return "ICONST_1";
			case InstructionIndex.ICONST_2: return "ICONST_2";
			case InstructionIndex.ICONST_3: return "ICONST_3";
			case InstructionIndex.ICONST_4: return "ICONST_4";
			case InstructionIndex.ICONST_5: return "ICONST_5";
			case InstructionIndex.LCONST_0: return "LCONST_0";
			case InstructionIndex.LCONST_1: return "LCONST_1";
			case InstructionIndex.FCONST_0: return "FCONST_0";
			case InstructionIndex.FCONST_1: return "FCONST_1";
			case InstructionIndex.FCONST_2: return "FCONST_2";
			case InstructionIndex.DCONST_0: return "DCONST_0";
			case InstructionIndex.DCONST_1: return "DCONST_1";
			case InstructionIndex.BIPUSH: return "BIPUSH";
			case InstructionIndex.SIPUSH: return "SIPUSH";
			case InstructionIndex.LDC: return "LDC";
			case InstructionIndex.LDC_W: return "LDC_W";
			case InstructionIndex.LDC2_W: return "LDC2_W";
			case InstructionIndex.ILOAD: return "ILOAD";
			case InstructionIndex.LLOAD: return "LLOAD";
			case InstructionIndex.FLOAD: return "FLOAD";
			case InstructionIndex.DLOAD: return "DLOAD";
			case InstructionIndex.ALOAD: return "ALOAD";
			case InstructionIndex.ILOAD_0: return "ILOAD_0";
			case InstructionIndex.ILOAD_1: return "ILOAD_1";
			case InstructionIndex.ILOAD_2: return "ILOAD_2";
			case InstructionIndex.ILOAD_3: return "ILOAD_3";
			case InstructionIndex.LLOAD_0: return "LLOAD_0";
			case InstructionIndex.LLOAD_1: return "LLOAD_1";
			case InstructionIndex.LLOAD_2: return "LLOAD_2";
			case InstructionIndex.LLOAD_3: return "LLOAD_3";
			case InstructionIndex.FLOAD_0: return "FLOAD_0";
			case InstructionIndex.FLOAD_1: return "FLOAD_1";
			case InstructionIndex.FLOAD_2: return "FLOAD_2";
			case InstructionIndex.FLOAD_3: return "FLOAD_3";
			case InstructionIndex.DLOAD_0: return "DLOAD_0";
			case InstructionIndex.DLOAD_1: return "DLOAD_1";
			case InstructionIndex.DLOAD_2: return "DLOAD_2";
			case InstructionIndex.DLOAD_3: return "DLOAD_3";
			case InstructionIndex.ALOAD_0: return "ALOAD_0";
			case InstructionIndex.ALOAD_1: return "ALOAD_1";
			case InstructionIndex.ALOAD_2: return "ALOAD_2";
			case InstructionIndex.ALOAD_3: return "ALOAD_3";
			case InstructionIndex.IALOAD: return "IALOAD";
			case InstructionIndex.LALOAD: return "LALOAD";
			case InstructionIndex.FALOAD: return "FALOAD";
			case InstructionIndex.DALOAD: return "DALOAD";
			case InstructionIndex.AALOAD: return "AALOAD";
			case InstructionIndex.BALOAD: return "BALOAD";
			case InstructionIndex.CALOAD: return "CALOAD";
			case InstructionIndex.SALOAD: return "SALOAD";
			case InstructionIndex.ISTORE: return "ISTORE";
			case InstructionIndex.LSTORE: return "LSTORE";
			case InstructionIndex.FSTORE: return "FSTORE";
			case InstructionIndex.DSTORE: return "DSTORE";
			case InstructionIndex.ASTORE: return "ASTORE";
			case InstructionIndex.ISTORE_0: return "ISTORE_0";
			case InstructionIndex.ISTORE_1: return "ISTORE_1";
			case InstructionIndex.ISTORE_2: return "ISTORE_2";
			case InstructionIndex.ISTORE_3: return "ISTORE_3";
			case InstructionIndex.LSTORE_0: return "LSTORE_0";
			case InstructionIndex.LSTORE_1: return "LSTORE_1";
			case InstructionIndex.LSTORE_2: return "LSTORE_2";
			case InstructionIndex.LSTORE_3: return "LSTORE_3";
			case InstructionIndex.FSTORE_0: return "FSTORE_0";
			case InstructionIndex.FSTORE_1: return "FSTORE_1";
			case InstructionIndex.FSTORE_2: return "FSTORE_2";
			case InstructionIndex.FSTORE_3: return "FSTORE_3";
			case InstructionIndex.DSTORE_0: return "DSTORE_0";
			case InstructionIndex.DSTORE_1: return "DSTORE_1";
			case InstructionIndex.DSTORE_2: return "DSTORE_2";
			case InstructionIndex.DSTORE_3: return "DSTORE_3";
			case InstructionIndex.ASTORE_0: return "ASTORE_0";
			case InstructionIndex.ASTORE_1: return "ASTORE_1";
			case InstructionIndex.ASTORE_2: return "ASTORE_2";
			case InstructionIndex.ASTORE_3: return "ASTORE_3";
			case InstructionIndex.IASTORE: return "IASTORE";
			case InstructionIndex.LASTORE: return "LASTORE";
			case InstructionIndex.FASTORE: return "FASTORE";
			case InstructionIndex.DASTORE: return "DASTORE";
			case InstructionIndex.AASTORE: return "AASTORE";
			case InstructionIndex.BASTORE: return "BASTORE";
			case InstructionIndex.CASTORE: return "CASTORE";
			case InstructionIndex.SASTORE: return "SASTORE";
			case InstructionIndex.POP: return "POP";
			case InstructionIndex.POP2: return "POP2";
			case InstructionIndex.DUP: return "DUP";
			case InstructionIndex.DUP_X1: return "DUP_X1";
			case InstructionIndex.DUP_X2: return "DUP_X2";
			case InstructionIndex.DUP2: return "DUP2";
			case InstructionIndex.DUP2_X1: return "DUP2_X1";
			case InstructionIndex.DUP2_X2: return "DUP2_X2";
			case InstructionIndex.SWAP: return "SWAP";
			case InstructionIndex.IADD: return "IADD";
			case InstructionIndex.LADD: return "LADD";
			case InstructionIndex.FADD: return "FADD";
			case InstructionIndex.DADD: return "DADD";
			case InstructionIndex.ISUB: return "ISUB";
			case InstructionIndex.LSUB: return "LSUB";
			case InstructionIndex.FSUB: return "FSUB";
			case InstructionIndex.DSUB: return "DSUB";
			case InstructionIndex.IMUL: return "IMUL";
			case InstructionIndex.LMUL: return "LMUL";
			case InstructionIndex.FMUL: return "FMUL";
			case InstructionIndex.DMUL: return "DMUL";
			case InstructionIndex.IDIV: return "IDIV";
			case InstructionIndex.LDIV: return "LDIV";
			case InstructionIndex.FDIV: return "FDIV";
			case InstructionIndex.DDIV: return "DDIV";
			case InstructionIndex.IREM: return "IREM";
			case InstructionIndex.LREM: return "LREM";
			case InstructionIndex.FREM: return "FREM";
			case InstructionIndex.DREM: return "DREM";
			case InstructionIndex.INEG: return "INEG";
			case InstructionIndex.LNEG: return "LNEG";
			case InstructionIndex.FNEG: return "FNEG";
			case InstructionIndex.DNEG: return "DNEG";
			case InstructionIndex.ISHL: return "ISHL";
			case InstructionIndex.LSHL: return "LSHL";
			case InstructionIndex.ISHR: return "ISHR";
			case InstructionIndex.LSHR: return "LSHR";
			case InstructionIndex.IUSHR: return "IUSHR";
			case InstructionIndex.LUSHR: return "LUSHR";
			case InstructionIndex.IAND: return "IAND";
			case InstructionIndex.LAND: return "LAND";
			case InstructionIndex.IOR: return "IOR";
			case InstructionIndex.LOR: return "LOR";
			case InstructionIndex.IXOR: return "IXOR";
			case InstructionIndex.LXOR: return "LXOR";
			case InstructionIndex.IINC: return "IINC";
			case InstructionIndex.I2L: return "I2L";
			case InstructionIndex.I2F: return "I2F";
			case InstructionIndex.I2D: return "I2D";
			case InstructionIndex.L2I: return "L2I";
			case InstructionIndex.L2F: return "L2F";
			case InstructionIndex.L2D: return "L2D";
			case InstructionIndex.F2I: return "F2I";
			case InstructionIndex.F2L: return "F2L";
			case InstructionIndex.F2D: return "F2D";
			case InstructionIndex.D2I: return "D2I";
			case InstructionIndex.D2L: return "D2L";
			case InstructionIndex.D2F: return "D2F";
			case InstructionIndex.I2B: return "I2B";
			case InstructionIndex.I2C: return "I2C";
			case InstructionIndex.I2S: return "I2S";
			case InstructionIndex.LCMP: return "LCMP";
			case InstructionIndex.FCMPL: return "FCMPL";
			case InstructionIndex.FCMPG: return "FCMPG";
			case InstructionIndex.DCMPL: return "DCMPL";
			case InstructionIndex.DCMPG: return "DCMPG";
			case InstructionIndex.IFEQ: return "IFEQ";
			case InstructionIndex.IFNE: return "IFNE";
			case InstructionIndex.IFLT: return "IFLT";
			case InstructionIndex.IFGE: return "IFGE";
			case InstructionIndex.IFGT: return "IFGT";
			case InstructionIndex.IFLE: return "IFLE";
			case InstructionIndex.IF_ICMPEQ: return "IF_ICMPEQ";
			case InstructionIndex.IF_ICMPNE: return "IF_ICMPNE";
			case InstructionIndex.IF_ICMPLT: return "IF_ICMPLT";
			case InstructionIndex.IF_ICMPGE: return "IF_ICMPGE";
			case InstructionIndex.IF_ICMPGT: return "IF_ICMPGT";
			case InstructionIndex.IF_ICMPLE: return "IF_ICMPLE";
			case InstructionIndex.IF_ACMPEQ: return "IF_ACMPEQ";
			case InstructionIndex.IF_ACMPNE: return "IF_ACMPNE";
			case InstructionIndex.GOTO: return "GOTO";
			case InstructionIndex.JSR: return "JSR";
			case InstructionIndex.RET: return "RET";
			case InstructionIndex.TABLESWITCH: return "TABLESWITCH";
			case InstructionIndex.LOOKUPSWITCH: return "LOOKUPSWITCH";
			case InstructionIndex.IRETURN: return "IRETURN";
			case InstructionIndex.LRETURN: return "LRETURN";
			case InstructionIndex.FRETURN: return "FRETURN";
			case InstructionIndex.DRETURN: return "DRETURN";
			case InstructionIndex.ARETURN: return "ARETURN";
			case InstructionIndex.RETURN: return "RETURN";
			case InstructionIndex.GETSTATIC: return "GETSTATIC";
			case InstructionIndex.PUTSTATIC: return "PUTSTATIC";
			case InstructionIndex.GETFIELD: return "GETFIELD";
			case InstructionIndex.PUTFIELD: return "PUTFIELD";
			case InstructionIndex.INVOKEVIRTUAL: return "INVOKEVIRTUAL";
			case InstructionIndex.INVOKESPECIAL: return "INVOKESPECIAL";
			case InstructionIndex.INVOKESTATIC: return "INVOKESTATIC";
			case InstructionIndex.INVOKEINTERFACE: return "INVOKEINTERFACE";
			case InstructionIndex.INVOKEDYNAMIC: return "INVOKEDYNAMIC";
			case InstructionIndex.NEW: return "NEW";
			case InstructionIndex.NEWARRAY: return "NEWARRAY";
			case InstructionIndex.ANEWARRAY: return "ANEWARRAY";
			case InstructionIndex.ARRAYLENGTH: return "ARRAYLENGTH";
			case InstructionIndex.ATHROW: return "ATHROW";
			case InstructionIndex.CHECKCAST: return "CHECKCAST";
			case InstructionIndex.INSTANCEOF: return "INSTANCEOF";
			case InstructionIndex.MONITORENTER: return "MONITORENTER";
			case InstructionIndex.MONITOREXIT: return "MONITOREXIT";
			case InstructionIndex.WIDE: return "WIDE";
			case InstructionIndex.MULTIANEWARRAY: return "MULTIANEWARRAY";
			case InstructionIndex.IFNULL: return "IFNULL";
			case InstructionIndex.IFNONNULL: return "IFNONNULL";
			case InstructionIndex.GOTO_W: return "GOTO_W";
			case InstructionIndex.JSR_W: return "JSR_W";
			case InstructionIndex.BREAKPOINT: return "BREAKPOINT";
			case InstructionIndex.IMPDEP1: return "IMPDEP1";
			case InstructionIndex.IMPDEP2: return "IMPDEP2";
			case InstructionIndex.WIDE_ALOAD: return "WIDE_ALOAD";
			case InstructionIndex.WIDE_ILOAD: return "WIDE_ILOAD";
			case InstructionIndex.WIDE_LLOAD: return "WIDE_LLOAD";
			case InstructionIndex.WIDE_FLOAD: return "WIDE_FLOAD";
			case InstructionIndex.WIDE_DLOAD: return "WIDE_DLOAD";
			case InstructionIndex.WIDE_ASTORE: return "WIDE_ASTORE";
			case InstructionIndex.WIDE_ISTORE: return "WIDE_ISTORE";
			case InstructionIndex.WIDE_LSTORE: return "WIDE_LSTORE";
			case InstructionIndex.WIDE_FSTORE: return "WIDE_FSTORE";
			case InstructionIndex.WIDE_DSTORE: return "WIDE_DSTORE";
			case InstructionIndex.WIDE_IINC: return "WIDE_IINC";
			
				// Unknown
			default:
				return String.format("UNKNOWN_%04X", __op);
		}
	}
}

