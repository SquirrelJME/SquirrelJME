/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Bytecode Execution Support.
 * 
 * @file
 * @since 2023/11/18
 */

#ifndef SJME_C_BYTECODE_H
#define SJME_C_BYTECODE_H

#include "sjme/error.h"
#include "sjme/nvm/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BYTECODE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Enumeration for byte code instructions.
 *
 * @since 2023/11/18
 */
typedef enum sjme_nvm_byteCode_instruction
{
	/** NOP. */
	SJME_NVM_BYTECODE_JAVA_NOP = 0,
	
	/** ACONST_NULL. */
	SJME_NVM_BYTECODE_JAVA_ACONST_NULL = 1,

	/** ICONST_M1. */
	SJME_NVM_BYTECODE_JAVA_ICONST_M1 = 2,

	/** ICONST_0. */
	SJME_NVM_BYTECODE_JAVA_ICONST_0 = 3,

	/** ICONST_1. */
	SJME_NVM_BYTECODE_JAVA_ICONST_1 = 4,

	/** ICONST_2. */
	SJME_NVM_BYTECODE_JAVA_ICONST_2 = 5,

	/** ICONST_3. */
	SJME_NVM_BYTECODE_JAVA_ICONST_3 = 6,

	/** ICONST_4. */
	SJME_NVM_BYTECODE_JAVA_ICONST_4 = 7,

	/** ICONST_5. */
	SJME_NVM_BYTECODE_JAVA_ICONST_5 = 8,

	/** LCONST_0. */
	SJME_NVM_BYTECODE_JAVA_LCONST_0 = 9,

	/** LCONST_1. */
	SJME_NVM_BYTECODE_JAVA_LCONST_1 = 10,

	/** FCONST_0. */
	SJME_NVM_BYTECODE_JAVA_FCONST_0 = 11,

	/** FCONST_1. */
	SJME_NVM_BYTECODE_JAVA_FCONST_1 = 12,

	/** FCONST_2. */
	SJME_NVM_BYTECODE_JAVA_FCONST_2 = 13,

	/** DCONST_0. */
	SJME_NVM_BYTECODE_JAVA_DCONST_0 = 14,

	/** DCONST_1. */
	SJME_NVM_BYTECODE_JAVA_DCONST_1 = 15,

	/** BIPUSH. */
	SJME_NVM_BYTECODE_JAVA_BIPUSH = 16,

	/** SIPUSH. */
	SJME_NVM_BYTECODE_JAVA_SIPUSH = 17,

	/** LDC. */
	SJME_NVM_BYTECODE_JAVA_LDC = 18,

	/** LDC_W. */
	SJME_NVM_BYTECODE_JAVA_LDC_W = 19,

	/** LDC2_W. */
	SJME_NVM_BYTECODE_JAVA_LDC2_W = 20,

	/** ILOAD. */
	SJME_NVM_BYTECODE_JAVA_ILOAD = 21,

	/** LLOAD. */
	SJME_NVM_BYTECODE_JAVA_LLOAD = 22,

	/** FLOAD. */
	SJME_NVM_BYTECODE_JAVA_FLOAD = 23,

	/** DLOAD. */
	SJME_NVM_BYTECODE_JAVA_DLOAD = 24,

	/** ALOAD. */
	SJME_NVM_BYTECODE_JAVA_ALOAD = 25,

	/** ILOAD_0. */
	SJME_NVM_BYTECODE_JAVA_ILOAD_0 = 26,

	/** ILOAD_1. */
	SJME_NVM_BYTECODE_JAVA_ILOAD_1 = 27,

	/** ILOAD_2. */
	SJME_NVM_BYTECODE_JAVA_ILOAD_2 = 28,

	/** ILOAD_3. */
	SJME_NVM_BYTECODE_JAVA_ILOAD_3 = 29,

	/** LLOAD_0. */
	SJME_NVM_BYTECODE_JAVA_LLOAD_0 = 30,

	/** LLOAD_1. */
	SJME_NVM_BYTECODE_JAVA_LLOAD_1 = 31,

	/** LLOAD_2. */
	SJME_NVM_BYTECODE_JAVA_LLOAD_2 = 32,

	/** LLOAD_3. */
	SJME_NVM_BYTECODE_JAVA_LLOAD_3 = 33,

	/** FLOAD_0. */
	SJME_NVM_BYTECODE_JAVA_FLOAD_0 = 34,

	/** FLOAD_1. */
	SJME_NVM_BYTECODE_JAVA_FLOAD_1 = 35,

	/** FLOAD_2. */
	SJME_NVM_BYTECODE_JAVA_FLOAD_2 = 36,

	/** FLOAD_3. */
	SJME_NVM_BYTECODE_JAVA_FLOAD_3 = 37,

	/** DLOAD_0. */
	SJME_NVM_BYTECODE_JAVA_DLOAD_0 = 38,

	/** DLOAD_1. */
	SJME_NVM_BYTECODE_JAVA_DLOAD_1 = 39,

	/** DLOAD_2. */
	SJME_NVM_BYTECODE_JAVA_DLOAD_2 = 40,

	/** DLOAD_3. */
	SJME_NVM_BYTECODE_JAVA_DLOAD_3 = 41,

	/** ALOAD_0. */
	SJME_NVM_BYTECODE_JAVA_ALOAD_0 = 42,

	/** ALOAD_1. */
	SJME_NVM_BYTECODE_JAVA_ALOAD_1 = 43,

	/** ALOAD_2. */
	SJME_NVM_BYTECODE_JAVA_ALOAD_2 = 44,

	/** ALOAD_3. */
	SJME_NVM_BYTECODE_JAVA_ALOAD_3 = 45,

	/** IALOAD. */
	SJME_NVM_BYTECODE_JAVA_IALOAD = 46,

	/** LALOAD. */
	SJME_NVM_BYTECODE_JAVA_LALOAD = 47,

	/** FALOAD. */
	SJME_NVM_BYTECODE_JAVA_FALOAD = 48,

	/** DALOAD. */
	SJME_NVM_BYTECODE_JAVA_DALOAD = 49,

	/** AALOAD. */
	SJME_NVM_BYTECODE_JAVA_AALOAD = 50,

	/** BALOAD. */
	SJME_NVM_BYTECODE_JAVA_BALOAD = 51,

	/** CALOAD. */
	SJME_NVM_BYTECODE_JAVA_CALOAD = 52,

	/** SALOAD. */
	SJME_NVM_BYTECODE_JAVA_SALOAD = 53,

	/** ISTORE. */
	SJME_NVM_BYTECODE_JAVA_ISTORE = 54,

	/** LSTORE. */
	SJME_NVM_BYTECODE_JAVA_LSTORE = 55,

	/** FSTORE. */
	SJME_NVM_BYTECODE_JAVA_FSTORE = 56,

	/** DSTORE. */
	SJME_NVM_BYTECODE_JAVA_DSTORE = 57,

	/** ASTORE. */
	SJME_NVM_BYTECODE_JAVA_ASTORE = 58,

	/** ISTORE_0. */
	SJME_NVM_BYTECODE_JAVA_ISTORE_0 = 59,

	/** ISTORE_1. */
	SJME_NVM_BYTECODE_JAVA_ISTORE_1 = 60,

	/** ISTORE_2. */
	SJME_NVM_BYTECODE_JAVA_ISTORE_2 = 61,

	/** ISTORE_3. */
	SJME_NVM_BYTECODE_JAVA_ISTORE_3 = 62,

	/** LSTORE_0. */
	SJME_NVM_BYTECODE_JAVA_LSTORE_0 = 63,

	/** LSTORE_1. */
	SJME_NVM_BYTECODE_JAVA_LSTORE_1 = 64,

	/** LSTORE_2. */
	SJME_NVM_BYTECODE_JAVA_LSTORE_2 = 65,

	/** LSTORE_3. */
	SJME_NVM_BYTECODE_JAVA_LSTORE_3 = 66,

	/** FSTORE_0. */
	SJME_NVM_BYTECODE_JAVA_FSTORE_0 = 67,

	/** FSTORE_1. */
	SJME_NVM_BYTECODE_JAVA_FSTORE_1 = 68,

	/** FSTORE_2. */
	SJME_NVM_BYTECODE_JAVA_FSTORE_2 = 69,

	/** FSTORE_3. */
	SJME_NVM_BYTECODE_JAVA_FSTORE_3 = 70,

	/** DSTORE_0. */
	SJME_NVM_BYTECODE_JAVA_DSTORE_0 = 71,

	/** DSTORE_1. */
	SJME_NVM_BYTECODE_JAVA_DSTORE_1 = 72,

	/** DSTORE_2. */
	SJME_NVM_BYTECODE_JAVA_DSTORE_2 = 73,

	/** DSTORE_3. */
	SJME_NVM_BYTECODE_JAVA_DSTORE_3 = 74,

	/** ASTORE_0. */
	SJME_NVM_BYTECODE_JAVA_ASTORE_0 = 75,

	/** ASTORE_1. */
	SJME_NVM_BYTECODE_JAVA_ASTORE_1 = 76,

	/** ASTORE_2. */
	SJME_NVM_BYTECODE_JAVA_ASTORE_2 = 77,

	/** ASTORE_3. */
	SJME_NVM_BYTECODE_JAVA_ASTORE_3 = 78,

	/** IASTORE. */
	SJME_NVM_BYTECODE_JAVA_IASTORE = 79,

	/** LASTORE. */
	SJME_NVM_BYTECODE_JAVA_LASTORE = 80,

	/** FASTORE. */
	SJME_NVM_BYTECODE_JAVA_FASTORE = 81,

	/** DASTORE. */
	SJME_NVM_BYTECODE_JAVA_DASTORE = 82,

	/** AASTORE. */
	SJME_NVM_BYTECODE_JAVA_AASTORE = 83,

	/** BASTORE. */
	SJME_NVM_BYTECODE_JAVA_BASTORE = 84,

	/** CASTORE. */
	SJME_NVM_BYTECODE_JAVA_CASTORE = 85,

	/** SASTORE. */
	SJME_NVM_BYTECODE_JAVA_SASTORE = 86,

	/** POP. */
	SJME_NVM_BYTECODE_JAVA_POP = 87,

	/** POP2. */
	SJME_NVM_BYTECODE_JAVA_POP2 = 88,

	/** DUP. */
	SJME_NVM_BYTECODE_JAVA_DUP = 89,

	/** DUP_X1. */
	SJME_NVM_BYTECODE_JAVA_DUP_X1 = 90,

	/** DUP_X2. */
	SJME_NVM_BYTECODE_JAVA_DUP_X2 = 91,

	/** DUP2. */
	SJME_NVM_BYTECODE_JAVA_DUP2 = 92,

	/** DUP2_X1. */
	SJME_NVM_BYTECODE_JAVA_DUP2_X1 = 93,

	/** DUP2_X2. */
	SJME_NVM_BYTECODE_JAVA_DUP2_X2 = 94,

	/** SWAP. */
	SJME_NVM_BYTECODE_JAVA_SWAP = 95,

	/** IADD. */
	SJME_NVM_BYTECODE_JAVA_IADD = 96,

	/** LADD. */
	SJME_NVM_BYTECODE_JAVA_LADD = 97,

	/** FADD. */
	SJME_NVM_BYTECODE_JAVA_FADD = 98,

	/** DADD. */
	SJME_NVM_BYTECODE_JAVA_DADD = 99,

	/** ISUB. */
	SJME_NVM_BYTECODE_JAVA_ISUB = 100,

	/** LSUB. */
	SJME_NVM_BYTECODE_JAVA_LSUB = 101,

	/** FSUB. */
	SJME_NVM_BYTECODE_JAVA_FSUB = 102,

	/** DSUB. */
	SJME_NVM_BYTECODE_JAVA_DSUB = 103,

	/** IMUL. */
	SJME_NVM_BYTECODE_JAVA_IMUL = 104,

	/** LMUL. */
	SJME_NVM_BYTECODE_JAVA_LMUL = 105,

	/** FMUL. */
	SJME_NVM_BYTECODE_JAVA_FMUL = 106,

	/** DMUL. */
	SJME_NVM_BYTECODE_JAVA_DMUL = 107,

	/** IDIV. */
	SJME_NVM_BYTECODE_JAVA_IDIV = 108,

	/** LDIV. */
	SJME_NVM_BYTECODE_JAVA_LDIV = 109,

	/** FDIV. */
	SJME_NVM_BYTECODE_JAVA_FDIV = 110,

	/** DDIV. */
	SJME_NVM_BYTECODE_JAVA_DDIV = 111,

	/** IREM. */
	SJME_NVM_BYTECODE_JAVA_IREM = 112,

	/** LREM. */
	SJME_NVM_BYTECODE_JAVA_LREM = 113,

	/** FREM. */
	SJME_NVM_BYTECODE_JAVA_FREM = 114,

	/** DREM. */
	SJME_NVM_BYTECODE_JAVA_DREM = 115,

	/** INEG. */
	SJME_NVM_BYTECODE_JAVA_INEG = 116,

	/** LNEG. */
	SJME_NVM_BYTECODE_JAVA_LNEG = 117,

	/** FNEG. */
	SJME_NVM_BYTECODE_JAVA_FNEG = 118,

	/** DNEG. */
	SJME_NVM_BYTECODE_JAVA_DNEG = 119,

	/** ISHL. */
	SJME_NVM_BYTECODE_JAVA_ISHL = 120,

	/** LSHL. */
	SJME_NVM_BYTECODE_JAVA_LSHL = 121,

	/** ISHR. */
	SJME_NVM_BYTECODE_JAVA_ISHR = 122,

	/** LSHR. */
	SJME_NVM_BYTECODE_JAVA_LSHR = 123,

	/** IUSHR. */
	SJME_NVM_BYTECODE_JAVA_IUSHR = 124,

	/** LUSHR. */
	SJME_NVM_BYTECODE_JAVA_LUSHR = 125,

	/** IAND. */
	SJME_NVM_BYTECODE_JAVA_IAND = 126,

	/** LAND. */
	SJME_NVM_BYTECODE_JAVA_LAND = 127,

	/** IOR. */
	SJME_NVM_BYTECODE_JAVA_IOR = 128,

	/** LOR. */
	SJME_NVM_BYTECODE_JAVA_LOR = 129,

	/** IXOR. */
	SJME_NVM_BYTECODE_JAVA_IXOR = 130,

	/** LXOR. */
	SJME_NVM_BYTECODE_JAVA_LXOR = 131,

	/** IINC. */
	SJME_NVM_BYTECODE_JAVA_IINC = 132,

	/** I2L. */
	SJME_NVM_BYTECODE_JAVA_I2L = 133,

	/** I2F. */
	SJME_NVM_BYTECODE_JAVA_I2F = 134,

	/** I2D. */
	SJME_NVM_BYTECODE_JAVA_I2D = 135,

	/** L2I. */
	SJME_NVM_BYTECODE_JAVA_L2I = 136,

	/** L2F. */
	SJME_NVM_BYTECODE_JAVA_L2F = 137,

	/** L2D. */
	SJME_NVM_BYTECODE_JAVA_L2D = 138,

	/** F2I. */
	SJME_NVM_BYTECODE_JAVA_F2I = 139,

	/** F2L. */
	SJME_NVM_BYTECODE_JAVA_F2L = 140,

	/** F2D. */
	SJME_NVM_BYTECODE_JAVA_F2D = 141,

	/** D2I. */
	SJME_NVM_BYTECODE_JAVA_D2I = 142,

	/** D2L. */
	SJME_NVM_BYTECODE_JAVA_D2L = 143,

	/** D2F. */
	SJME_NVM_BYTECODE_JAVA_D2F = 144,

	/** I2B. */
	SJME_NVM_BYTECODE_JAVA_I2B = 145,

	/** I2C. */
	SJME_NVM_BYTECODE_JAVA_I2C = 146,

	/** I2S. */
	SJME_NVM_BYTECODE_JAVA_I2S = 147,

	/** LCMP. */
	SJME_NVM_BYTECODE_JAVA_LCMP = 148,

	/** FCMPL. */
	SJME_NVM_BYTECODE_JAVA_FCMPL = 149,

	/** FCMPG. */
	SJME_NVM_BYTECODE_JAVA_FCMPG = 150,

	/** DCMPL. */
	SJME_NVM_BYTECODE_JAVA_DCMPL = 151,

	/** DCMPG. */
	SJME_NVM_BYTECODE_JAVA_DCMPG = 152,

	/** IFEQ. */
	SJME_NVM_BYTECODE_JAVA_IFEQ = 153,

	/** IFNE. */
	SJME_NVM_BYTECODE_JAVA_IFNE = 154,

	/** IFLT. */
	SJME_NVM_BYTECODE_JAVA_IFLT = 155,

	/** IFGE. */
	SJME_NVM_BYTECODE_JAVA_IFGE = 156,

	/** IFGT. */
	SJME_NVM_BYTECODE_JAVA_IFGT = 157,

	/** IFLE. */
	SJME_NVM_BYTECODE_JAVA_IFLE = 158,

	/** IF_ICMPEQ. */
	SJME_NVM_BYTECODE_JAVA_IF_ICMPEQ = 159,

	/** IF_ICMPNE. */
	SJME_NVM_BYTECODE_JAVA_IF_ICMPNE = 160,

	/** IF_ICMPLT. */
	SJME_NVM_BYTECODE_JAVA_IF_ICMPLT = 161,

	/** IF_ICMPGE. */
	SJME_NVM_BYTECODE_JAVA_IF_ICMPGE = 162,

	/** IF_ICMPGT. */
	SJME_NVM_BYTECODE_JAVA_IF_ICMPGT = 163,

	/** IF_ICMPLE. */
	SJME_NVM_BYTECODE_JAVA_IF_ICMPLE = 164,

	/** IF_ACMPEQ. */
	SJME_NVM_BYTECODE_JAVA_IF_ACMPEQ = 165,

	/** IF_ACMPNE. */
	SJME_NVM_BYTECODE_JAVA_IF_ACMPNE = 166,

	/** GOTO. */
	SJME_NVM_BYTECODE_JAVA_GOTO = 167,

	/** JSR. */
	SJME_NVM_BYTECODE_JAVA_JSR = 168,

	/** RET. */
	SJME_NVM_BYTECODE_JAVA_RET = 169,

	/** TABLESWITCH. */
	SJME_NVM_BYTECODE_JAVA_TABLESWITCH = 170,

	/** LOOKUPSWITCH. */
	SJME_NVM_BYTECODE_JAVA_LOOKUPSWITCH = 171,

	/** IRETURN. */
	SJME_NVM_BYTECODE_JAVA_IRETURN = 172,

	/** LRETURN. */
	SJME_NVM_BYTECODE_JAVA_LRETURN = 173,

	/** FRETURN. */
	SJME_NVM_BYTECODE_JAVA_FRETURN = 174,

	/** DRETURN. */
	SJME_NVM_BYTECODE_JAVA_DRETURN = 175,

	/** ARETURN. */
	SJME_NVM_BYTECODE_JAVA_ARETURN = 176,

	/** RETURN. */
	SJME_NVM_BYTECODE_JAVA_RETURN = 177,

	/** GETSTATIC. */
	SJME_NVM_BYTECODE_JAVA_GETSTATIC = 178,

	/** PUTSTATIC. */
	SJME_NVM_BYTECODE_JAVA_PUTSTATIC = 179,

	/** GETFIELD. */
	SJME_NVM_BYTECODE_JAVA_GETFIELD = 180,

	/** PUTFIELD. */
	SJME_NVM_BYTECODE_JAVA_PUTFIELD = 181,

	/** INVOKEVIRTUAL. */
	SJME_NVM_BYTECODE_JAVA_INVOKEVIRTUAL = 182,

	/** INVOKESPECIAL. */
	SJME_NVM_BYTECODE_JAVA_INVOKESPECIAL = 183,

	/** INVOKESTATIC. */
	SJME_NVM_BYTECODE_JAVA_INVOKESTATIC = 184,

	/** INVOKEINTERFACE. */
	SJME_NVM_BYTECODE_JAVA_INVOKEINTERFACE = 185,

	/** INVOKEDYNAMIC. */
	SJME_NVM_BYTECODE_JAVA_INVOKEDYNAMIC = 186,

	/** NEW. */
	SJME_NVM_BYTECODE_JAVA_NEW = 187,

	/** NEWARRAY. */
	SJME_NVM_BYTECODE_JAVA_NEWARRAY = 188,

	/** ANEWARRAY. */
	SJME_NVM_BYTECODE_JAVA_ANEWARRAY = 189,

	/** ARRAYLENGTH. */
	SJME_NVM_BYTECODE_JAVA_ARRAYLENGTH = 190,

	/** ATHROW. */
	SJME_NVM_BYTECODE_JAVA_ATHROW = 191,

	/** CHECKCAST. */
	SJME_NVM_BYTECODE_JAVA_CHECKCAST = 192,

	/** INSTANCEOF. */
	SJME_NVM_BYTECODE_JAVA_INSTANCEOF = 193,

	/** MONITORENTER. */
	SJME_NVM_BYTECODE_JAVA_MONITORENTER = 194,

	/** MONITOREXIT. */
	SJME_NVM_BYTECODE_JAVA_MONITOREXIT = 195,

	/** WIDE. */
	SJME_NVM_BYTECODE_JAVA_WIDE = 196,

	/** MULTIANEWARRAY. */
	SJME_NVM_BYTECODE_JAVA_MULTIANEWARRAY = 197,

	/** IFNULL. */
	SJME_NVM_BYTECODE_JAVA_IFNULL = 198,

	/** IFNONNULL. */
	SJME_NVM_BYTECODE_JAVA_IFNONNULL = 199,

	/** GOTO_W. */
	SJME_NVM_BYTECODE_JAVA_GOTO_W = 200,

	/** JSR_W. */
	SJME_NVM_BYTECODE_JAVA_JSR_W = 201,

	/** BREAKPOINT. */
	SJME_NVM_BYTECODE_JAVA_BREAKPOINT = 202,

	/** IMPDEP1. */
	SJME_NVM_BYTECODE_JAVA_IMPDEP1 = 254,

	/** IMPDEP2. */
	SJME_NVM_BYTECODE_JAVA_IMPDEP2 = 255,

	/** The number of base Java instructions. */
	SJME_NVM_NUM_JAVA_BYTECODES = 256,

	/** Wide ALOAD. */
	SJME_NVM_BYTECODE_JAVA_WIDE_ALOAD =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_ALOAD,

	/** Wide ILOAD. */
	SJME_NVM_BYTECODE_JAVA_WIDE_ILOAD =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_ILOAD,

	/** Wide LLOAD. */
	SJME_NVM_BYTECODE_JAVA_WIDE_LLOAD =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_LLOAD,

	/** Wide FLOAD. */
	SJME_NVM_BYTECODE_JAVA_WIDE_FLOAD =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_FLOAD,

	/** Wide DLOAD. */
	SJME_NVM_BYTECODE_JAVA_WIDE_DLOAD =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_DLOAD,

	/** Wide ASTORE. */
	SJME_NVM_BYTECODE_JAVA_WIDE_ASTORE =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_ASTORE,

	/** Wide ISTORE. */
	SJME_NVM_BYTECODE_JAVA_WIDE_ISTORE =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_ISTORE,

	/** Wide LSTORE. */
	SJME_NVM_BYTECODE_JAVA_WIDE_LSTORE =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_LSTORE,

	/** Wide FSTORE. */
	SJME_NVM_BYTECODE_JAVA_WIDE_FSTORE =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_FSTORE,

	/** Wide DSTORE. */
	SJME_NVM_BYTECODE_JAVA_WIDE_DSTORE =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_DSTORE,
	
	/** Wide IINC. */
	SJME_NVM_BYTECODE_JAVA_WIDE_IINC =
		(SJME_NVM_BYTECODE_JAVA_WIDE << 8) |
		SJME_NVM_BYTECODE_JAVA_IINC,
} sjme_nvm_byteCode_instruction;

/**
 * Specifies the type of PC address change occurs.
 *
 * @since 2025/01/11
 */
typedef enum sjme_nvm_byteCode_pcNewType
{
	/** Default forward. */
	SJME_NVM_BYTECODE_PC_DEFAULT = 0,
	
	/** Relative address. */
	SJME_NVM_BYTECODE_PC_RELATIVE = 1,

	/** Absolute address. */
	SJME_NVM_BYTECODE_PC_ABSOLUTE = 2,

	/** Recycle the current operation, do nothing yet! */
	SJME_NVM_BYTECODE_PC_RECYCLE = 3,

	/** The number of types. */
	SJME_NVM_BYTECODE_NUM_PC_NEW_TYPE = 4,
} sjme_nvm_byteCode_pcNewType;

struct sjme_nvm_byteCode_pcNew
{
	/** The type of adjustment to make. */
	sjme_nvm_byteCode_pcNewType type;
	
	/** The PC adjustment. */
	sjme_jint adjust;

	/** Should the current frame be popped? */
	sjme_jboolean popFrame;
};

/**
 * Function type for byte code execution.
 * 
 * @param inFrame The frame to execute under.
 * @param id The instruction ID.
 * @param relRawCode The relative raw code at the PC address.
 * @param pcNew New PC address.
 * @return Any resultant error.
 * @since 2023/11/18
 */
typedef sjme_errorCode (*sjme_nvm_byteCode_func)(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew);

/**
 * Bytecode length interpretation.
 *
 * @since 2025/06/14
 */
typedef enum sjme_nvm_byteCode_length
{
	/** Invalid instruction. */
	SJME_NVM_BYTECODE_LENGTH_INVALID = -1,

	/** No default flow, length 1. */
	SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1 = -2,

	/** No default flow, length 2. */
	SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_2 = -3,

	/** No default flow, length 3. */
	SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_3 = -4,

	/** No default flow, length 4. */
	SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_4 = -5,

	/** No default flow, length 5. */
	SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_5 = -6,

	/** @c lookupswitch . */
	SJME_NVM_BYTECODE_LENGTH_LOOKUPSWITCH = -7,

	/** @c tableswitch . */
	SJME_NVM_BYTECODE_LENGTH_TABLESWITCH = -8,

	/** @c wide . */
	SJME_NVM_BYTECODE_LENGTH_WIDE = -9,

	/** Fast bytecode, length 1. */
	SJME_NVM_BYTECODE_LENGTH_FAST_1 = -10,

	/** Fast bytecode, length 2. */
	SJME_NVM_BYTECODE_LENGTH_FAST_2 = -11,

	/** Fast bytecode, length 3. */
	SJME_NVM_BYTECODE_LENGTH_FAST_3 = -12,

	/** Fast bytecode, length 4. */
	SJME_NVM_BYTECODE_LENGTH_FAST_4 = -13,

	/** Fast bytecode, length 5. */
	SJME_NVM_BYTECODE_LENGTH_FAST_5 = -14,
} sjme_nvm_byteCode_length;

/** The length of each instruction. */
extern const sjme_jbyte sjme_nvm_byteCode_lengths[SJME_NVM_NUM_JAVA_BYTECODES];

/** The name of each instruction. */
extern const sjme_lpcstr sjme_nvm_byteCode_names[SJME_NVM_NUM_JAVA_BYTECODES];

/** Which LUT to use. */
extern const sjme_nvm_byteCode_func (*sjme_nvm_byteCode_lutTable
	[SJME_NVM_NUM_JAVA_BYTECODES])[SJME_NVM_NUM_JAVA_BYTECODES];

/**
 * Calculates the instruction length.
 *
 * @param inFrame The thread frame.
 * @param id The instruction ID.
 * @param relRawCode The relative raw code at the PC address.
 * @param pcNew New PC address.
 * @return Any resultant error.
 * @since 2025/06/14
 */
sjme_errorCode sjme_nvm_byteCode_calcLength(
	sjme_attrInNullable sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew);

/**
 * Checks if a recycle eligible event has occurred, a recycle is when the
 * byte code should be re-executed and no progression is made. This would be
 * the case if a method frame should be entered but another method such
 * as a static initializer must complete first.
 * 
 * @param basisFrame The basis frame to check from.
 * @return The resultant value.
 * @since 2025/07/05
 */
sjme_jboolean sjme_nvm_byteCode_checkRecycleR(
	sjme_attrInNullable sjme_nvm_frame basisFrame);
	
/**
 * Represents an instruction that is not legal.
 *
 * @param inFrame The thread frame.
 * @param id The instruction ID.
 * @param relRawCode The relative raw code at the PC address.
 * @param pcNew New PC address.
 * @return Any resultant error.
 * @since 2025/01/08
 */
sjme_errorCode sjme_nvm_byteCode_illegalInstruction(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew);

/**
 * Represents an instruction that is not implemented.
 *
 * @param inFrame The thread frame.
 * @param id The instruction ID.
 * @param relRawCode The relative raw code at the PC address.
 * @param pcNew New PC address.
 * @return Any resultant error.
 * @since 2025/01/08
 */
sjme_errorCode sjme_nvm_byteCode_notImplemented(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew);
	
/**
 * The name of a byte code.
 *
 * @param category The category this is in.
 * @param which The name of the byte code.
 * @since 2025/06/26
 */
#define SJME_NVM_BYTECODE_NAME(category, which) \
	SJME_TOKEN_PASTE3(sjme_nvm_byteCode_, category, which)
	
/**
 * Declares a bytecode.
 *
 * @param category The category this is in.
 * @param which Which byte code is declared?
 * @since 2025/06/26
 */
#define SJME_NVM_BYTECODE(category, which) \
	sjme_errorCode SJME_NVM_BYTECODE_NAME(category, which) ( \
		sjme_attrInNotNull sjme_nvm_frame inFrame, \
		sjme_attrInRange(0, 256) sjme_byteCode id, \
		sjme_attrInNotNull sjme_byteCode* relRawCode, \
		sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew)

/** Common entry for byte code. */
#define SJME_NVM_BYTECODE_ENTRY \
	sjme_errorCode error; \
	if (inFrame == NULL || relRawCode == NULL || pcNew == NULL) \
		return SJME_ERROR_NULL_ARGUMENTS

/** Common exit for byte code. */
#define SJME_NVM_BYTECODE_EXIT \
	return SJME_ERROR_NONE;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_BYTECODE_H
}
		#undef SJME_CXX_SQUIRRELJME_BYTECODE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_BYTECODE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BYTECODE_H */
