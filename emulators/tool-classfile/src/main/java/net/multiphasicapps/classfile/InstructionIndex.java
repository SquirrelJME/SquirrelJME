// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This interface contains instruction identification numbers which identify
 * opcodes based on their opcode ID.
 *
 * @since 2016/08/24
 */
public interface InstructionIndex
{
	/** NOP. */
	int NOP =
		0;

	/** ACONST_NULL. */
	int ACONST_NULL =
		1;

	/** ICONST_M1. */
	int ICONST_M1 =
		2;

	/** ICONST_0. */
	int ICONST_0 =
		3;

	/** ICONST_1. */
	int ICONST_1 =
		4;

	/** ICONST_2. */
	int ICONST_2 =
		5;

	/** ICONST_3. */
	int ICONST_3 =
		6;

	/** ICONST_4. */
	int ICONST_4 =
		7;

	/** ICONST_5. */
	int ICONST_5 =
		8;

	/** LCONST_0. */
	int LCONST_0 =
		9;

	/** LCONST_1. */
	int LCONST_1 =
		10;

	/** FCONST_0. */
	int FCONST_0 =
		11;

	/** FCONST_1. */
	int FCONST_1 =
		12;

	/** FCONST_2. */
	int FCONST_2 =
		13;

	/** DCONST_0. */
	int DCONST_0 =
		14;

	/** DCONST_1. */
	int DCONST_1 =
		15;

	/** BIPUSH. */
	int BIPUSH =
		16;

	/** SIPUSH. */
	int SIPUSH =
		17;

	/** LDC. */
	int LDC =
		18;

	/** LDC_W. */
	int LDC_W =
		19;

	/** LDC2_W. */
	int LDC2_W =
		20;

	/** ILOAD. */
	int ILOAD =
		21;

	/** LLOAD. */
	int LLOAD =
		22;

	/** FLOAD. */
	int FLOAD =
		23;

	/** DLOAD. */
	int DLOAD =
		24;

	/** ALOAD. */
	int ALOAD =
		25;

	/** ILOAD_0. */
	int ILOAD_0 =
		26;

	/** ILOAD_1. */
	int ILOAD_1 =
		27;

	/** ILOAD_2. */
	int ILOAD_2 =
		28;

	/** ILOAD_3. */
	int ILOAD_3 =
		29;

	/** LLOAD_0. */
	int LLOAD_0 =
		30;

	/** LLOAD_1. */
	int LLOAD_1 =
		31;

	/** LLOAD_2. */
	int LLOAD_2 =
		32;

	/** LLOAD_3. */
	int LLOAD_3 =
		33;

	/** FLOAD_0. */
	int FLOAD_0 =
		34;

	/** FLOAD_1. */
	int FLOAD_1 =
		35;

	/** FLOAD_2. */
	int FLOAD_2 =
		36;

	/** FLOAD_3. */
	int FLOAD_3 =
		37;

	/** DLOAD_0. */
	int DLOAD_0 =
		38;

	/** DLOAD_1. */
	int DLOAD_1 =
		39;

	/** DLOAD_2. */
	int DLOAD_2 =
		40;

	/** DLOAD_3. */
	int DLOAD_3 =
		41;

	/** ALOAD_0. */
	int ALOAD_0 =
		42;

	/** ALOAD_1. */
	int ALOAD_1 =
		43;

	/** ALOAD_2. */
	int ALOAD_2 =
		44;

	/** ALOAD_3. */
	int ALOAD_3 =
		45;

	/** IALOAD. */
	int IALOAD =
		46;

	/** LALOAD. */
	int LALOAD =
		47;

	/** FALOAD. */
	int FALOAD =
		48;

	/** DALOAD. */
	int DALOAD =
		49;

	/** AALOAD. */
	int AALOAD =
		50;

	/** BALOAD. */
	int BALOAD =
		51;

	/** CALOAD. */
	int CALOAD =
		52;

	/** SALOAD. */
	int SALOAD =
		53;

	/** ISTORE. */
	int ISTORE =
		54;

	/** LSTORE. */
	int LSTORE =
		55;

	/** FSTORE. */
	int FSTORE =
		56;

	/** DSTORE. */
	int DSTORE =
		57;

	/** ASTORE. */
	int ASTORE =
		58;

	/** ISTORE_0. */
	int ISTORE_0 =
		59;

	/** ISTORE_1. */
	int ISTORE_1 =
		60;

	/** ISTORE_2. */
	int ISTORE_2 =
		61;

	/** ISTORE_3. */
	int ISTORE_3 =
		62;

	/** LSTORE_0. */
	int LSTORE_0 =
		63;

	/** LSTORE_1. */
	int LSTORE_1 =
		64;

	/** LSTORE_2. */
	int LSTORE_2 =
		65;

	/** LSTORE_3. */
	int LSTORE_3 =
		66;

	/** FSTORE_0. */
	int FSTORE_0 =
		67;

	/** FSTORE_1. */
	int FSTORE_1 =
		68;

	/** FSTORE_2. */
	int FSTORE_2 =
		69;

	/** FSTORE_3. */
	int FSTORE_3 =
		70;

	/** DSTORE_0. */
	int DSTORE_0 =
		71;

	/** DSTORE_1. */
	int DSTORE_1 =
		72;

	/** DSTORE_2. */
	int DSTORE_2 =
		73;

	/** DSTORE_3. */
	int DSTORE_3 =
		74;

	/** ASTORE_0. */
	int ASTORE_0 =
		75;

	/** ASTORE_1. */
	int ASTORE_1 =
		76;

	/** ASTORE_2. */
	int ASTORE_2 =
		77;

	/** ASTORE_3. */
	int ASTORE_3 =
		78;

	/** IASTORE. */
	int IASTORE =
		79;

	/** LASTORE. */
	int LASTORE =
		80;

	/** FASTORE. */
	int FASTORE =
		81;

	/** DASTORE. */
	int DASTORE =
		82;

	/** AASTORE. */
	int AASTORE =
		83;

	/** BASTORE. */
	int BASTORE =
		84;

	/** CASTORE. */
	int CASTORE =
		85;

	/** SASTORE. */
	int SASTORE =
		86;

	/** POP. */
	int POP =
		87;

	/** POP2. */
	int POP2 =
		88;

	/** DUP. */
	int DUP =
		89;

	/** DUP_X1. */
	int DUP_X1 =
		90;

	/** DUP_X2. */
	int DUP_X2 =
		91;

	/** DUP2. */
	int DUP2 =
		92;

	/** DUP2_X1. */
	int DUP2_X1 =
		93;

	/** DUP2_X2. */
	int DUP2_X2 =
		94;

	/** SWAP. */
	int SWAP =
		95;

	/** IADD. */
	int IADD =
		96;

	/** LADD. */
	int LADD =
		97;

	/** FADD. */
	int FADD =
		98;

	/** DADD. */
	int DADD =
		99;

	/** ISUB. */
	int ISUB =
		100;

	/** LSUB. */
	int LSUB =
		101;

	/** FSUB. */
	int FSUB =
		102;

	/** DSUB. */
	int DSUB =
		103;

	/** IMUL. */
	int IMUL =
		104;

	/** LMUL. */
	int LMUL =
		105;

	/** FMUL. */
	int FMUL =
		106;

	/** DMUL. */
	int DMUL =
		107;

	/** IDIV. */
	int IDIV =
		108;

	/** LDIV. */
	int LDIV =
		109;

	/** FDIV. */
	int FDIV =
		110;

	/** DDIV. */
	int DDIV =
		111;

	/** IREM. */
	int IREM =
		112;

	/** LREM. */
	int LREM =
		113;

	/** FREM. */
	int FREM =
		114;

	/** DREM. */
	int DREM =
		115;

	/** INEG. */
	int INEG =
		116;

	/** LNEG. */
	int LNEG =
		117;

	/** FNEG. */
	int FNEG =
		118;

	/** DNEG. */
	int DNEG =
		119;

	/** ISHL. */
	int ISHL =
		120;

	/** LSHL. */
	int LSHL =
		121;

	/** ISHR. */
	int ISHR =
		122;

	/** LSHR. */
	int LSHR =
		123;

	/** IUSHR. */
	int IUSHR =
		124;

	/** LUSHR. */
	int LUSHR =
		125;

	/** IAND. */
	int IAND =
		126;

	/** LAND. */
	int LAND =
		127;

	/** IOR. */
	int IOR =
		128;

	/** LOR. */
	int LOR =
		129;

	/** IXOR. */
	int IXOR =
		130;

	/** LXOR. */
	int LXOR =
		131;

	/** IINC. */
	int IINC =
		132;

	/** I2L. */
	int I2L =
		133;

	/** I2F. */
	int I2F =
		134;

	/** I2D. */
	int I2D =
		135;

	/** L2I. */
	int L2I =
		136;

	/** L2F. */
	int L2F =
		137;

	/** L2D. */
	int L2D =
		138;

	/** F2I. */
	int F2I =
		139;

	/** F2L. */
	int F2L =
		140;

	/** F2D. */
	int F2D =
		141;

	/** D2I. */
	int D2I =
		142;

	/** D2L. */
	int D2L =
		143;

	/** D2F. */
	int D2F =
		144;

	/** I2B. */
	int I2B =
		145;

	/** I2C. */
	int I2C =
		146;

	/** I2S. */
	int I2S =
		147;

	/** LCMP. */
	int LCMP =
		148;

	/** FCMPL. */
	int FCMPL =
		149;

	/** FCMPG. */
	int FCMPG =
		150;

	/** DCMPL. */
	int DCMPL =
		151;

	/** DCMPG. */
	int DCMPG =
		152;

	/** IFEQ. */
	int IFEQ =
		153;

	/** IFNE. */
	int IFNE =
		154;

	/** IFLT. */
	int IFLT =
		155;

	/** IFGE. */
	int IFGE =
		156;

	/** IFGT. */
	int IFGT =
		157;

	/** IFLE. */
	int IFLE =
		158;

	/** IF_ICMPEQ. */
	int IF_ICMPEQ =
		159;

	/** IF_ICMPNE. */
	int IF_ICMPNE =
		160;

	/** IF_ICMPLT. */
	int IF_ICMPLT =
		161;

	/** IF_ICMPGE. */
	int IF_ICMPGE =
		162;

	/** IF_ICMPGT. */
	int IF_ICMPGT =
		163;

	/** IF_ICMPLE. */
	int IF_ICMPLE =
		164;

	/** IF_ACMPEQ. */
	int IF_ACMPEQ =
		165;

	/** IF_ACMPNE. */
	int IF_ACMPNE =
		166;

	/** GOTO. */
	int GOTO =
		167;

	/** JSR. */
	int JSR =
		168;

	/** RET. */
	int RET =
		169;

	/** TABLESWITCH. */
	int TABLESWITCH =
		170;

	/** LOOKUPSWITCH. */
	int LOOKUPSWITCH =
		171;

	/** IRETURN. */
	int IRETURN =
		172;

	/** LRETURN. */
	int LRETURN =
		173;

	/** FRETURN. */
	int FRETURN =
		174;

	/** DRETURN. */
	int DRETURN =
		175;

	/** ARETURN. */
	int ARETURN =
		176;

	/** RETURN. */
	int RETURN =
		177;

	/** GETSTATIC. */
	int GETSTATIC =
		178;

	/** PUTSTATIC. */
	int PUTSTATIC =
		179;

	/** GETFIELD. */
	int GETFIELD =
		180;

	/** PUTFIELD. */
	int PUTFIELD =
		181;

	/** INVOKEVIRTUAL. */
	int INVOKEVIRTUAL =
		182;

	/** INVOKESPECIAL. */
	int INVOKESPECIAL =
		183;

	/** INVOKESTATIC. */
	int INVOKESTATIC =
		184;

	/** INVOKEINTERFACE. */
	int INVOKEINTERFACE =
		185;

	/** INVOKEDYNAMIC. */
	int INVOKEDYNAMIC =
		186;

	/** NEW. */
	int NEW =
		187;

	/** NEWARRAY. */
	int NEWARRAY =
		188;

	/** ANEWARRAY. */
	int ANEWARRAY =
		189;

	/** ARRAYLENGTH. */
	int ARRAYLENGTH =
		190;

	/** ATHROW. */
	int ATHROW =
		191;

	/** CHECKCAST. */
	int CHECKCAST =
		192;

	/** INSTANCEOF. */
	int INSTANCEOF =
		193;

	/** MONITORENTER. */
	int MONITORENTER =
		194;

	/** MONITOREXIT. */
	int MONITOREXIT =
		195;

	/** WIDE. */
	int WIDE =
		196;

	/** MULTIANEWARRAY. */
	int MULTIANEWARRAY =
		197;

	/** IFNULL. */
	int IFNULL =
		198;

	/** IFNONNULL. */
	int IFNONNULL =
		199;

	/** GOTO_W. */
	int GOTO_W =
		200;

	/** JSR_W. */
	int JSR_W =
		201;

	/** BREAKPOINT. */
	int BREAKPOINT =
		202;

	/** IMPDEP1. */
	int IMPDEP1 =
		254;

	/** IMPDEP2. */
	int IMPDEP2 =
		255;

	/** Wide ALOAD. */
	int WIDE_ALOAD =
		(InstructionIndex.WIDE << 8) | InstructionIndex.ALOAD;

	/** Wide ILOAD. */
	int WIDE_ILOAD =
		(InstructionIndex.WIDE << 8) | InstructionIndex.ILOAD;

	/** Wide LLOAD. */
	int WIDE_LLOAD =
		(InstructionIndex.WIDE << 8) | InstructionIndex.LLOAD;

	/** Wide FLOAD. */
	int WIDE_FLOAD =
		(InstructionIndex.WIDE << 8) | InstructionIndex.FLOAD;

	/** Wide DLOAD. */
	int WIDE_DLOAD =
		(InstructionIndex.WIDE << 8) | InstructionIndex.DLOAD;

	/** Wide ASTORE. */
	int WIDE_ASTORE =
		(InstructionIndex.WIDE << 8) | InstructionIndex.ASTORE;

	/** Wide ISTORE. */
	int WIDE_ISTORE =
		(InstructionIndex.WIDE << 8) | InstructionIndex.ISTORE;

	/** Wide LSTORE. */
	int WIDE_LSTORE =
		(InstructionIndex.WIDE << 8) | InstructionIndex.LSTORE;

	/** Wide FSTORE. */
	int WIDE_FSTORE =
		(InstructionIndex.WIDE << 8) | InstructionIndex.FSTORE;

	/** Wide DSTORE. */
	int WIDE_DSTORE =
		(InstructionIndex.WIDE << 8) | InstructionIndex.DSTORE;
	
	/** Wide IINC. */
	int WIDE_IINC =
		(InstructionIndex.WIDE << 8) | InstructionIndex.IINC;
}

