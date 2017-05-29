// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

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
			case __OperandIndex__.NOP: return "NOP";
			case __OperandIndex__.ACONST_NULL: return "ACONST_NULL";
			case __OperandIndex__.ICONST_M1: return "ICONST_M1";
			case __OperandIndex__.ICONST_0: return "ICONST_0";
			case __OperandIndex__.ICONST_1: return "ICONST_1";
			case __OperandIndex__.ICONST_2: return "ICONST_2";
			case __OperandIndex__.ICONST_3: return "ICONST_3";
			case __OperandIndex__.ICONST_4: return "ICONST_4";
			case __OperandIndex__.ICONST_5: return "ICONST_5";
			case __OperandIndex__.LCONST_0: return "LCONST_0";
			case __OperandIndex__.LCONST_1: return "LCONST_1";
			case __OperandIndex__.FCONST_0: return "FCONST_0";
			case __OperandIndex__.FCONST_1: return "FCONST_1";
			case __OperandIndex__.FCONST_2: return "FCONST_2";
			case __OperandIndex__.DCONST_0: return "DCONST_0";
			case __OperandIndex__.DCONST_1: return "DCONST_1";
			case __OperandIndex__.BIPUSH: return "BIPUSH";
			case __OperandIndex__.SIPUSH: return "SIPUSH";
			case __OperandIndex__.LDC: return "LDC";
			case __OperandIndex__.LDC_W: return "LDC_W";
			case __OperandIndex__.LDC2_W: return "LDC2_W";
			case __OperandIndex__.ILOAD: return "ILOAD";
			case __OperandIndex__.LLOAD: return "LLOAD";
			case __OperandIndex__.FLOAD: return "FLOAD";
			case __OperandIndex__.DLOAD: return "DLOAD";
			case __OperandIndex__.ALOAD: return "ALOAD";
			case __OperandIndex__.ILOAD_0: return "ILOAD_0";
			case __OperandIndex__.ILOAD_1: return "ILOAD_1";
			case __OperandIndex__.ILOAD_2: return "ILOAD_2";
			case __OperandIndex__.ILOAD_3: return "ILOAD_3";
			case __OperandIndex__.LLOAD_0: return "LLOAD_0";
			case __OperandIndex__.LLOAD_1: return "LLOAD_1";
			case __OperandIndex__.LLOAD_2: return "LLOAD_2";
			case __OperandIndex__.LLOAD_3: return "LLOAD_3";
			case __OperandIndex__.FLOAD_0: return "FLOAD_0";
			case __OperandIndex__.FLOAD_1: return "FLOAD_1";
			case __OperandIndex__.FLOAD_2: return "FLOAD_2";
			case __OperandIndex__.FLOAD_3: return "FLOAD_3";
			case __OperandIndex__.DLOAD_0: return "DLOAD_0";
			case __OperandIndex__.DLOAD_1: return "DLOAD_1";
			case __OperandIndex__.DLOAD_2: return "DLOAD_2";
			case __OperandIndex__.DLOAD_3: return "DLOAD_3";
			case __OperandIndex__.ALOAD_0: return "ALOAD_0";
			case __OperandIndex__.ALOAD_1: return "ALOAD_1";
			case __OperandIndex__.ALOAD_2: return "ALOAD_2";
			case __OperandIndex__.ALOAD_3: return "ALOAD_3";
			case __OperandIndex__.IALOAD: return "IALOAD";
			case __OperandIndex__.LALOAD: return "LALOAD";
			case __OperandIndex__.FALOAD: return "FALOAD";
			case __OperandIndex__.DALOAD: return "DALOAD";
			case __OperandIndex__.AALOAD: return "AALOAD";
			case __OperandIndex__.BALOAD: return "BALOAD";
			case __OperandIndex__.CALOAD: return "CALOAD";
			case __OperandIndex__.SALOAD: return "SALOAD";
			case __OperandIndex__.ISTORE: return "ISTORE";
			case __OperandIndex__.LSTORE: return "LSTORE";
			case __OperandIndex__.FSTORE: return "FSTORE";
			case __OperandIndex__.DSTORE: return "DSTORE";
			case __OperandIndex__.ASTORE: return "ASTORE";
			case __OperandIndex__.ISTORE_0: return "ISTORE_0";
			case __OperandIndex__.ISTORE_1: return "ISTORE_1";
			case __OperandIndex__.ISTORE_2: return "ISTORE_2";
			case __OperandIndex__.ISTORE_3: return "ISTORE_3";
			case __OperandIndex__.LSTORE_0: return "LSTORE_0";
			case __OperandIndex__.LSTORE_1: return "LSTORE_1";
			case __OperandIndex__.LSTORE_2: return "LSTORE_2";
			case __OperandIndex__.LSTORE_3: return "LSTORE_3";
			case __OperandIndex__.FSTORE_0: return "FSTORE_0";
			case __OperandIndex__.FSTORE_1: return "FSTORE_1";
			case __OperandIndex__.FSTORE_2: return "FSTORE_2";
			case __OperandIndex__.FSTORE_3: return "FSTORE_3";
			case __OperandIndex__.DSTORE_0: return "DSTORE_0";
			case __OperandIndex__.DSTORE_1: return "DSTORE_1";
			case __OperandIndex__.DSTORE_2: return "DSTORE_2";
			case __OperandIndex__.DSTORE_3: return "DSTORE_3";
			case __OperandIndex__.ASTORE_0: return "ASTORE_0";
			case __OperandIndex__.ASTORE_1: return "ASTORE_1";
			case __OperandIndex__.ASTORE_2: return "ASTORE_2";
			case __OperandIndex__.ASTORE_3: return "ASTORE_3";
			case __OperandIndex__.IASTORE: return "IASTORE";
			case __OperandIndex__.LASTORE: return "LASTORE";
			case __OperandIndex__.FASTORE: return "FASTORE";
			case __OperandIndex__.DASTORE: return "DASTORE";
			case __OperandIndex__.AASTORE: return "AASTORE";
			case __OperandIndex__.BASTORE: return "BASTORE";
			case __OperandIndex__.CASTORE: return "CASTORE";
			case __OperandIndex__.SASTORE: return "SASTORE";
			case __OperandIndex__.POP: return "POP";
			case __OperandIndex__.POP2: return "POP2";
			case __OperandIndex__.DUP: return "DUP";
			case __OperandIndex__.DUP_X1: return "DUP_X1";
			case __OperandIndex__.DUP_X2: return "DUP_X2";
			case __OperandIndex__.DUP2: return "DUP2";
			case __OperandIndex__.DUP2_X1: return "DUP2_X1";
			case __OperandIndex__.DUP2_X2: return "DUP2_X2";
			case __OperandIndex__.SWAP: return "SWAP";
			case __OperandIndex__.IADD: return "IADD";
			case __OperandIndex__.LADD: return "LADD";
			case __OperandIndex__.FADD: return "FADD";
			case __OperandIndex__.DADD: return "DADD";
			case __OperandIndex__.ISUB: return "ISUB";
			case __OperandIndex__.LSUB: return "LSUB";
			case __OperandIndex__.FSUB: return "FSUB";
			case __OperandIndex__.DSUB: return "DSUB";
			case __OperandIndex__.IMUL: return "IMUL";
			case __OperandIndex__.LMUL: return "LMUL";
			case __OperandIndex__.FMUL: return "FMUL";
			case __OperandIndex__.DMUL: return "DMUL";
			case __OperandIndex__.IDIV: return "IDIV";
			case __OperandIndex__.LDIV: return "LDIV";
			case __OperandIndex__.FDIV: return "FDIV";
			case __OperandIndex__.DDIV: return "DDIV";
			case __OperandIndex__.IREM: return "IREM";
			case __OperandIndex__.LREM: return "LREM";
			case __OperandIndex__.FREM: return "FREM";
			case __OperandIndex__.DREM: return "DREM";
			case __OperandIndex__.INEG: return "INEG";
			case __OperandIndex__.LNEG: return "LNEG";
			case __OperandIndex__.FNEG: return "FNEG";
			case __OperandIndex__.DNEG: return "DNEG";
			case __OperandIndex__.ISHL: return "ISHL";
			case __OperandIndex__.LSHL: return "LSHL";
			case __OperandIndex__.ISHR: return "ISHR";
			case __OperandIndex__.LSHR: return "LSHR";
			case __OperandIndex__.IUSHR: return "IUSHR";
			case __OperandIndex__.LUSHR: return "LUSHR";
			case __OperandIndex__.IAND: return "IAND";
			case __OperandIndex__.LAND: return "LAND";
			case __OperandIndex__.IOR: return "IOR";
			case __OperandIndex__.LOR: return "LOR";
			case __OperandIndex__.IXOR: return "IXOR";
			case __OperandIndex__.LXOR: return "LXOR";
			case __OperandIndex__.IINC: return "IINC";
			case __OperandIndex__.I2L: return "I2L";
			case __OperandIndex__.I2F: return "I2F";
			case __OperandIndex__.I2D: return "I2D";
			case __OperandIndex__.L2I: return "L2I";
			case __OperandIndex__.L2F: return "L2F";
			case __OperandIndex__.L2D: return "L2D";
			case __OperandIndex__.F2I: return "F2I";
			case __OperandIndex__.F2L: return "F2L";
			case __OperandIndex__.F2D: return "F2D";
			case __OperandIndex__.D2I: return "D2I";
			case __OperandIndex__.D2L: return "D2L";
			case __OperandIndex__.D2F: return "D2F";
			case __OperandIndex__.I2B: return "I2B";
			case __OperandIndex__.I2C: return "I2C";
			case __OperandIndex__.I2S: return "I2S";
			case __OperandIndex__.LCMP: return "LCMP";
			case __OperandIndex__.FCMPL: return "FCMPL";
			case __OperandIndex__.FCMPG: return "FCMPG";
			case __OperandIndex__.DCMPL: return "DCMPL";
			case __OperandIndex__.DCMPG: return "DCMPG";
			case __OperandIndex__.IFEQ: return "IFEQ";
			case __OperandIndex__.IFNE: return "IFNE";
			case __OperandIndex__.IFLT: return "IFLT";
			case __OperandIndex__.IFGE: return "IFGE";
			case __OperandIndex__.IFGT: return "IFGT";
			case __OperandIndex__.IFLE: return "IFLE";
			case __OperandIndex__.IF_ICMPEQ: return "IF_ICMPEQ";
			case __OperandIndex__.IF_ICMPNE: return "IF_ICMPNE";
			case __OperandIndex__.IF_ICMPLT: return "IF_ICMPLT";
			case __OperandIndex__.IF_ICMPGE: return "IF_ICMPGE";
			case __OperandIndex__.IF_ICMPGT: return "IF_ICMPGT";
			case __OperandIndex__.IF_ICMPLE: return "IF_ICMPLE";
			case __OperandIndex__.IF_ACMPEQ: return "IF_ACMPEQ";
			case __OperandIndex__.IF_ACMPNE: return "IF_ACMPNE";
			case __OperandIndex__.GOTO: return "GOTO";
			case __OperandIndex__.JSR: return "JSR";
			case __OperandIndex__.RET: return "RET";
			case __OperandIndex__.TABLESWITCH: return "TABLESWITCH";
			case __OperandIndex__.LOOKUPSWITCH: return "LOOKUPSWITCH";
			case __OperandIndex__.IRETURN: return "IRETURN";
			case __OperandIndex__.LRETURN: return "LRETURN";
			case __OperandIndex__.FRETURN: return "FRETURN";
			case __OperandIndex__.DRETURN: return "DRETURN";
			case __OperandIndex__.ARETURN: return "ARETURN";
			case __OperandIndex__.RETURN: return "RETURN";
			case __OperandIndex__.GETSTATIC: return "GETSTATIC";
			case __OperandIndex__.PUTSTATIC: return "PUTSTATIC";
			case __OperandIndex__.GETFIELD: return "GETFIELD";
			case __OperandIndex__.PUTFIELD: return "PUTFIELD";
			case __OperandIndex__.INVOKEVIRTUAL: return "INVOKEVIRTUAL";
			case __OperandIndex__.INVOKESPECIAL: return "INVOKESPECIAL";
			case __OperandIndex__.INVOKESTATIC: return "INVOKESTATIC";
			case __OperandIndex__.INVOKEINTERFACE: return "INVOKEINTERFACE";
			case __OperandIndex__.INVOKEDYNAMIC: return "INVOKEDYNAMIC";
			case __OperandIndex__.NEW: return "NEW";
			case __OperandIndex__.NEWARRAY: return "NEWARRAY";
			case __OperandIndex__.ANEWARRAY: return "ANEWARRAY";
			case __OperandIndex__.ARRAYLENGTH: return "ARRAYLENGTH";
			case __OperandIndex__.ATHROW: return "ATHROW";
			case __OperandIndex__.CHECKCAST: return "CHECKCAST";
			case __OperandIndex__.INSTANCEOF: return "INSTANCEOF";
			case __OperandIndex__.MONITORENTER: return "MONITORENTER";
			case __OperandIndex__.MONITOREXIT: return "MONITOREXIT";
			case __OperandIndex__.WIDE: return "WIDE";
			case __OperandIndex__.MULTIANEWARRAY: return "MULTIANEWARRAY";
			case __OperandIndex__.IFNULL: return "IFNULL";
			case __OperandIndex__.IFNONNULL: return "IFNONNULL";
			case __OperandIndex__.GOTO_W: return "GOTO_W";
			case __OperandIndex__.JSR_W: return "JSR_W";
			case __OperandIndex__.BREAKPOINT: return "BREAKPOINT";
			case __OperandIndex__.IMPDEP1: return "IMPDEP1";
			case __OperandIndex__.IMPDEP2: return "IMPDEP2";
			case __OperandIndex__.WIDE_ALOAD: return "WIDE_ALOAD";
			case __OperandIndex__.WIDE_ILOAD: return "WIDE_ILOAD";
			case __OperandIndex__.WIDE_LLOAD: return "WIDE_LLOAD";
			case __OperandIndex__.WIDE_FLOAD: return "WIDE_FLOAD";
			case __OperandIndex__.WIDE_DLOAD: return "WIDE_DLOAD";
			case __OperandIndex__.WIDE_ASTORE: return "WIDE_ASTORE";
			case __OperandIndex__.WIDE_ISTORE: return "WIDE_ISTORE";
			case __OperandIndex__.WIDE_LSTORE: return "WIDE_LSTORE";
			case __OperandIndex__.WIDE_FSTORE: return "WIDE_FSTORE";
			case __OperandIndex__.WIDE_DSTORE: return "WIDE_DSTORE";
			case __OperandIndex__.WIDE_IINC: return "WIDE_IINC";
			
				// Unknown
			default:
				return String.format("UNKNOWN_%04X", __op);
		}
	}
}

