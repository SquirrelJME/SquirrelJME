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
 * @since 2023/11/18
 */

#ifndef SQUIRRELJME_BYTECODE_H
#define SQUIRRELJME_BYTECODE_H

#include "sjme/nvm.h"

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
typedef enum sjme_byteCode_instruction
{
	/** NOP. */
	SJME_BYTECODE_INSTRUCTION_NOP = 0,
	
	/** ACONST_NULL. */
	SJME_BYTECODE_INSTRUCTION_ACONST_NULL = 1,

	/** ICONST_M1. */
	SJME_BYTECODE_INSTRUCTION_ICONST_M1 = 2,

	/** ICONST_0. */
	SJME_BYTECODE_INSTRUCTION_ICONST_0 = 3,

	/** ICONST_1. */
	SJME_BYTECODE_INSTRUCTION_ICONST_1 = 4,

	/** ICONST_2. */
	SJME_BYTECODE_INSTRUCTION_ICONST_2 = 5,

	/** ICONST_3. */
	SJME_BYTECODE_INSTRUCTION_ICONST_3 = 6,

	/** ICONST_4. */
	SJME_BYTECODE_INSTRUCTION_ICONST_4 = 7,

	/** ICONST_5. */
	SJME_BYTECODE_INSTRUCTION_ICONST_5 = 8,

	/** LCONST_0. */
	SJME_BYTECODE_INSTRUCTION_LCONST_0 = 9,

	/** LCONST_1. */
	SJME_BYTECODE_INSTRUCTION_LCONST_1 = 10,

	/** FCONST_0. */
	SJME_BYTECODE_INSTRUCTION_FCONST_0 = 11,

	/** FCONST_1. */
	SJME_BYTECODE_INSTRUCTION_FCONST_1 = 12,

	/** FCONST_2. */
	SJME_BYTECODE_INSTRUCTION_FCONST_2 = 13,

	/** DCONST_0. */
	SJME_BYTECODE_INSTRUCTION_DCONST_0 = 14,

	/** DCONST_1. */
	SJME_BYTECODE_INSTRUCTION_DCONST_1 = 15,

	/** BIPUSH. */
	SJME_BYTECODE_INSTRUCTION_BIPUSH = 16,

	/** SIPUSH. */
	SJME_BYTECODE_INSTRUCTION_SIPUSH = 17,

	/** LDC. */
	SJME_BYTECODE_INSTRUCTION_LDC = 18,

	/** LDC_W. */
	SJME_BYTECODE_INSTRUCTION_LDC_W = 19,

	/** LDC2_W. */
	SJME_BYTECODE_INSTRUCTION_LDC2_W = 20,

	/** ILOAD. */
	SJME_BYTECODE_INSTRUCTION_ILOAD = 21,

	/** LLOAD. */
	SJME_BYTECODE_INSTRUCTION_LLOAD = 22,

	/** FLOAD. */
	SJME_BYTECODE_INSTRUCTION_FLOAD = 23,

	/** DLOAD. */
	SJME_BYTECODE_INSTRUCTION_DLOAD = 24,

	/** ALOAD. */
	SJME_BYTECODE_INSTRUCTION_ALOAD = 25,

	/** ILOAD_0. */
	SJME_BYTECODE_INSTRUCTION_ILOAD_0 = 26,

	/** ILOAD_1. */
	SJME_BYTECODE_INSTRUCTION_ILOAD_1 = 27,

	/** ILOAD_2. */
	SJME_BYTECODE_INSTRUCTION_ILOAD_2 = 28,

	/** ILOAD_3. */
	SJME_BYTECODE_INSTRUCTION_ILOAD_3 = 29,

	/** LLOAD_0. */
	SJME_BYTECODE_INSTRUCTION_LLOAD_0 = 30,

	/** LLOAD_1. */
	SJME_BYTECODE_INSTRUCTION_LLOAD_1 = 31,

	/** LLOAD_2. */
	SJME_BYTECODE_INSTRUCTION_LLOAD_2 = 32,

	/** LLOAD_3. */
	SJME_BYTECODE_INSTRUCTION_LLOAD_3 = 33,

	/** FLOAD_0. */
	SJME_BYTECODE_INSTRUCTION_FLOAD_0 = 34,

	/** FLOAD_1. */
	SJME_BYTECODE_INSTRUCTION_FLOAD_1 = 35,

	/** FLOAD_2. */
	SJME_BYTECODE_INSTRUCTION_FLOAD_2 = 36,

	/** FLOAD_3. */
	SJME_BYTECODE_INSTRUCTION_FLOAD_3 = 37,

	/** DLOAD_0. */
	SJME_BYTECODE_INSTRUCTION_DLOAD_0 = 38,

	/** DLOAD_1. */
	SJME_BYTECODE_INSTRUCTION_DLOAD_1 = 39,

	/** DLOAD_2. */
	SJME_BYTECODE_INSTRUCTION_DLOAD_2 = 40,

	/** DLOAD_3. */
	SJME_BYTECODE_INSTRUCTION_DLOAD_3 = 41,

	/** ALOAD_0. */
	SJME_BYTECODE_INSTRUCTION_ALOAD_0 = 42,

	/** ALOAD_1. */
	SJME_BYTECODE_INSTRUCTION_ALOAD_1 = 43,

	/** ALOAD_2. */
	SJME_BYTECODE_INSTRUCTION_ALOAD_2 = 44,

	/** ALOAD_3. */
	SJME_BYTECODE_INSTRUCTION_ALOAD_3 = 45,

	/** IALOAD. */
	SJME_BYTECODE_INSTRUCTION_IALOAD = 46,

	/** LALOAD. */
	SJME_BYTECODE_INSTRUCTION_LALOAD = 47,

	/** FALOAD. */
	SJME_BYTECODE_INSTRUCTION_FALOAD = 48,

	/** DALOAD. */
	SJME_BYTECODE_INSTRUCTION_DALOAD = 49,

	/** AALOAD. */
	SJME_BYTECODE_INSTRUCTION_AALOAD = 50,

	/** BALOAD. */
	SJME_BYTECODE_INSTRUCTION_BALOAD = 51,

	/** CALOAD. */
	SJME_BYTECODE_INSTRUCTION_CALOAD = 52,

	/** SALOAD. */
	SJME_BYTECODE_INSTRUCTION_SALOAD = 53,

	/** ISTORE. */
	SJME_BYTECODE_INSTRUCTION_ISTORE = 54,

	/** LSTORE. */
	SJME_BYTECODE_INSTRUCTION_LSTORE = 55,

	/** FSTORE. */
	SJME_BYTECODE_INSTRUCTION_FSTORE = 56,

	/** DSTORE. */
	SJME_BYTECODE_INSTRUCTION_DSTORE = 57,

	/** ASTORE. */
	SJME_BYTECODE_INSTRUCTION_ASTORE = 58,

	/** ISTORE_0. */
	SJME_BYTECODE_INSTRUCTION_ISTORE_0 = 59,

	/** ISTORE_1. */
	SJME_BYTECODE_INSTRUCTION_ISTORE_1 = 60,

	/** ISTORE_2. */
	SJME_BYTECODE_INSTRUCTION_ISTORE_2 = 61,

	/** ISTORE_3. */
	SJME_BYTECODE_INSTRUCTION_ISTORE_3 = 62,

	/** LSTORE_0. */
	SJME_BYTECODE_INSTRUCTION_LSTORE_0 = 63,

	/** LSTORE_1. */
	SJME_BYTECODE_INSTRUCTION_LSTORE_1 = 64,

	/** LSTORE_2. */
	SJME_BYTECODE_INSTRUCTION_LSTORE_2 = 65,

	/** LSTORE_3. */
	SJME_BYTECODE_INSTRUCTION_LSTORE_3 = 66,

	/** FSTORE_0. */
	SJME_BYTECODE_INSTRUCTION_FSTORE_0 = 67,

	/** FSTORE_1. */
	SJME_BYTECODE_INSTRUCTION_FSTORE_1 = 68,

	/** FSTORE_2. */
	SJME_BYTECODE_INSTRUCTION_FSTORE_2 = 69,

	/** FSTORE_3. */
	SJME_BYTECODE_INSTRUCTION_FSTORE_3 = 70,

	/** DSTORE_0. */
	SJME_BYTECODE_INSTRUCTION_DSTORE_0 = 71,

	/** DSTORE_1. */
	SJME_BYTECODE_INSTRUCTION_DSTORE_1 = 72,

	/** DSTORE_2. */
	SJME_BYTECODE_INSTRUCTION_DSTORE_2 = 73,

	/** DSTORE_3. */
	SJME_BYTECODE_INSTRUCTION_DSTORE_3 = 74,

	/** ASTORE_0. */
	SJME_BYTECODE_INSTRUCTION_ASTORE_0 = 75,

	/** ASTORE_1. */
	SJME_BYTECODE_INSTRUCTION_ASTORE_1 = 76,

	/** ASTORE_2. */
	SJME_BYTECODE_INSTRUCTION_ASTORE_2 = 77,

	/** ASTORE_3. */
	SJME_BYTECODE_INSTRUCTION_ASTORE_3 = 78,

	/** IASTORE. */
	SJME_BYTECODE_INSTRUCTION_IASTORE = 79,

	/** LASTORE. */
	SJME_BYTECODE_INSTRUCTION_LASTORE = 80,

	/** FASTORE. */
	SJME_BYTECODE_INSTRUCTION_FASTORE = 81,

	/** DASTORE. */
	SJME_BYTECODE_INSTRUCTION_DASTORE = 82,

	/** AASTORE. */
	SJME_BYTECODE_INSTRUCTION_AASTORE = 83,

	/** BASTORE. */
	SJME_BYTECODE_INSTRUCTION_BASTORE = 84,

	/** CASTORE. */
	SJME_BYTECODE_INSTRUCTION_CASTORE = 85,

	/** SASTORE. */
	SJME_BYTECODE_INSTRUCTION_SASTORE = 86,

	/** POP. */
	SJME_BYTECODE_INSTRUCTION_POP = 87,

	/** POP2. */
	SJME_BYTECODE_INSTRUCTION_POP2 = 88,

	/** DUP. */
	SJME_BYTECODE_INSTRUCTION_DUP = 89,

	/** DUP_X1. */
	SJME_BYTECODE_INSTRUCTION_DUP_X1 = 90,

	/** DUP_X2. */
	SJME_BYTECODE_INSTRUCTION_DUP_X2 = 91,

	/** DUP2. */
	SJME_BYTECODE_INSTRUCTION_DUP2 = 92,

	/** DUP2_X1. */
	SJME_BYTECODE_INSTRUCTION_DUP2_X1 = 93,

	/** DUP2_X2. */
	SJME_BYTECODE_INSTRUCTION_DUP2_X2 = 94,

	/** SWAP. */
	SJME_BYTECODE_INSTRUCTION_SWAP = 95,

	/** IADD. */
	SJME_BYTECODE_INSTRUCTION_IADD = 96,

	/** LADD. */
	SJME_BYTECODE_INSTRUCTION_LADD = 97,

	/** FADD. */
	SJME_BYTECODE_INSTRUCTION_FADD = 98,

	/** DADD. */
	SJME_BYTECODE_INSTRUCTION_DADD = 99,

	/** ISUB. */
	SJME_BYTECODE_INSTRUCTION_ISUB = 100,

	/** LSUB. */
	SJME_BYTECODE_INSTRUCTION_LSUB = 101,

	/** FSUB. */
	SJME_BYTECODE_INSTRUCTION_FSUB = 102,

	/** DSUB. */
	SJME_BYTECODE_INSTRUCTION_DSUB = 103,

	/** IMUL. */
	SJME_BYTECODE_INSTRUCTION_IMUL = 104,

	/** LMUL. */
	SJME_BYTECODE_INSTRUCTION_LMUL = 105,

	/** FMUL. */
	SJME_BYTECODE_INSTRUCTION_FMUL = 106,

	/** DMUL. */
	SJME_BYTECODE_INSTRUCTION_DMUL = 107,

	/** IDIV. */
	SJME_BYTECODE_INSTRUCTION_IDIV = 108,

	/** LDIV. */
	SJME_BYTECODE_INSTRUCTION_LDIV = 109,

	/** FDIV. */
	SJME_BYTECODE_INSTRUCTION_FDIV = 110,

	/** DDIV. */
	SJME_BYTECODE_INSTRUCTION_DDIV = 111,

	/** IREM. */
	SJME_BYTECODE_INSTRUCTION_IREM = 112,

	/** LREM. */
	SJME_BYTECODE_INSTRUCTION_LREM = 113,

	/** FREM. */
	SJME_BYTECODE_INSTRUCTION_FREM = 114,

	/** DREM. */
	SJME_BYTECODE_INSTRUCTION_DREM = 115,

	/** INEG. */
	SJME_BYTECODE_INSTRUCTION_INEG = 116,

	/** LNEG. */
	SJME_BYTECODE_INSTRUCTION_LNEG = 117,

	/** FNEG. */
	SJME_BYTECODE_INSTRUCTION_FNEG = 118,

	/** DNEG. */
	SJME_BYTECODE_INSTRUCTION_DNEG = 119,

	/** ISHL. */
	SJME_BYTECODE_INSTRUCTION_ISHL = 120,

	/** LSHL. */
	SJME_BYTECODE_INSTRUCTION_LSHL = 121,

	/** ISHR. */
	SJME_BYTECODE_INSTRUCTION_ISHR = 122,

	/** LSHR. */
	SJME_BYTECODE_INSTRUCTION_LSHR = 123,

	/** IUSHR. */
	SJME_BYTECODE_INSTRUCTION_IUSHR = 124,

	/** LUSHR. */
	SJME_BYTECODE_INSTRUCTION_LUSHR = 125,

	/** IAND. */
	SJME_BYTECODE_INSTRUCTION_IAND = 126,

	/** LAND. */
	SJME_BYTECODE_INSTRUCTION_LAND = 127,

	/** IOR. */
	SJME_BYTECODE_INSTRUCTION_IOR = 128,

	/** LOR. */
	SJME_BYTECODE_INSTRUCTION_LOR = 129,

	/** IXOR. */
	SJME_BYTECODE_INSTRUCTION_IXOR = 130,

	/** LXOR. */
	SJME_BYTECODE_INSTRUCTION_LXOR = 131,

	/** IINC. */
	SJME_BYTECODE_INSTRUCTION_IINC = 132,

	/** I2L. */
	SJME_BYTECODE_INSTRUCTION_I2L = 133,

	/** I2F. */
	SJME_BYTECODE_INSTRUCTION_I2F = 134,

	/** I2D. */
	SJME_BYTECODE_INSTRUCTION_I2D = 135,

	/** L2I. */
	SJME_BYTECODE_INSTRUCTION_L2I = 136,

	/** L2F. */
	SJME_BYTECODE_INSTRUCTION_L2F = 137,

	/** L2D. */
	SJME_BYTECODE_INSTRUCTION_L2D = 138,

	/** F2I. */
	SJME_BYTECODE_INSTRUCTION_F2I = 139,

	/** F2L. */
	SJME_BYTECODE_INSTRUCTION_F2L = 140,

	/** F2D. */
	SJME_BYTECODE_INSTRUCTION_F2D = 141,

	/** D2I. */
	SJME_BYTECODE_INSTRUCTION_D2I = 142,

	/** D2L. */
	SJME_BYTECODE_INSTRUCTION_D2L = 143,

	/** D2F. */
	SJME_BYTECODE_INSTRUCTION_D2F = 144,

	/** I2B. */
	SJME_BYTECODE_INSTRUCTION_I2B = 145,

	/** I2C. */
	SJME_BYTECODE_INSTRUCTION_I2C = 146,

	/** I2S. */
	SJME_BYTECODE_INSTRUCTION_I2S = 147,

	/** LCMP. */
	SJME_BYTECODE_INSTRUCTION_LCMP = 148,

	/** FCMPL. */
	SJME_BYTECODE_INSTRUCTION_FCMPL = 149,

	/** FCMPG. */
	SJME_BYTECODE_INSTRUCTION_FCMPG = 150,

	/** DCMPL. */
	SJME_BYTECODE_INSTRUCTION_DCMPL = 151,

	/** DCMPG. */
	SJME_BYTECODE_INSTRUCTION_DCMPG = 152,

	/** IFEQ. */
	SJME_BYTECODE_INSTRUCTION_IFEQ = 153,

	/** IFNE. */
	SJME_BYTECODE_INSTRUCTION_IFNE = 154,

	/** IFLT. */
	SJME_BYTECODE_INSTRUCTION_IFLT = 155,

	/** IFGE. */
	SJME_BYTECODE_INSTRUCTION_IFGE = 156,

	/** IFGT. */
	SJME_BYTECODE_INSTRUCTION_IFGT = 157,

	/** IFLE. */
	SJME_BYTECODE_INSTRUCTION_IFLE = 158,

	/** IF_ICMPEQ. */
	SJME_BYTECODE_INSTRUCTION_IF_ICMPEQ = 159,

	/** IF_ICMPNE. */
	SJME_BYTECODE_INSTRUCTION_IF_ICMPNE = 160,

	/** IF_ICMPLT. */
	SJME_BYTECODE_INSTRUCTION_IF_ICMPLT = 161,

	/** IF_ICMPGE. */
	SJME_BYTECODE_INSTRUCTION_IF_ICMPGE = 162,

	/** IF_ICMPGT. */
	SJME_BYTECODE_INSTRUCTION_IF_ICMPGT = 163,

	/** IF_ICMPLE. */
	SJME_BYTECODE_INSTRUCTION_IF_ICMPLE = 164,

	/** IF_ACMPEQ. */
	SJME_BYTECODE_INSTRUCTION_IF_ACMPEQ = 165,

	/** IF_ACMPNE. */
	SJME_BYTECODE_INSTRUCTION_IF_ACMPNE = 166,

	/** GOTO. */
	SJME_BYTECODE_INSTRUCTION_GOTO = 167,

	/** JSR. */
	SJME_BYTECODE_INSTRUCTION_JSR = 168,

	/** RET. */
	SJME_BYTECODE_INSTRUCTION_RET = 169,

	/** TABLESWITCH. */
	SJME_BYTECODE_INSTRUCTION_TABLESWITCH = 170,

	/** LOOKUPSWITCH. */
	SJME_BYTECODE_INSTRUCTION_LOOKUPSWITCH = 171,

	/** IRETURN. */
	SJME_BYTECODE_INSTRUCTION_IRETURN = 172,

	/** LRETURN. */
	SJME_BYTECODE_INSTRUCTION_LRETURN = 173,

	/** FRETURN. */
	SJME_BYTECODE_INSTRUCTION_FRETURN = 174,

	/** DRETURN. */
	SJME_BYTECODE_INSTRUCTION_DRETURN = 175,

	/** ARETURN. */
	SJME_BYTECODE_INSTRUCTION_ARETURN = 176,

	/** RETURN. */
	SJME_BYTECODE_INSTRUCTION_RETURN = 177,

	/** GETSTATIC. */
	SJME_BYTECODE_INSTRUCTION_GETSTATIC = 178,

	/** PUTSTATIC. */
	SJME_BYTECODE_INSTRUCTION_PUTSTATIC = 179,

	/** GETFIELD. */
	SJME_BYTECODE_INSTRUCTION_GETFIELD = 180,

	/** PUTFIELD. */
	SJME_BYTECODE_INSTRUCTION_PUTFIELD = 181,

	/** INVOKEVIRTUAL. */
	SJME_BYTECODE_INSTRUCTION_INVOKEVIRTUAL = 182,

	/** INVOKESPECIAL. */
	SJME_BYTECODE_INSTRUCTION_INVOKESPECIAL = 183,

	/** INVOKESTATIC. */
	SJME_BYTECODE_INSTRUCTION_INVOKESTATIC = 184,

	/** INVOKEINTERFACE. */
	SJME_BYTECODE_INSTRUCTION_INVOKEINTERFACE = 185,

	/** INVOKEDYNAMIC. */
	SJME_BYTECODE_INSTRUCTION_INVOKEDYNAMIC = 186,

	/** NEW. */
	SJME_BYTECODE_INSTRUCTION_NEW = 187,

	/** NEWARRAY. */
	SJME_BYTECODE_INSTRUCTION_NEWARRAY = 188,

	/** ANEWARRAY. */
	SJME_BYTECODE_INSTRUCTION_ANEWARRAY = 189,

	/** ARRAYLENGTH. */
	SJME_BYTECODE_INSTRUCTION_ARRAYLENGTH = 190,

	/** ATHROW. */
	SJME_BYTECODE_INSTRUCTION_ATHROW = 191,

	/** CHECKCAST. */
	SJME_BYTECODE_INSTRUCTION_CHECKCAST = 192,

	/** INSTANCEOF. */
	SJME_BYTECODE_INSTRUCTION_INSTANCEOF = 193,

	/** MONITORENTER. */
	SJME_BYTECODE_INSTRUCTION_MONITORENTER = 194,

	/** MONITOREXIT. */
	SJME_BYTECODE_INSTRUCTION_MONITOREXIT = 195,

	/** WIDE. */
	SJME_BYTECODE_INSTRUCTION_WIDE = 196,

	/** MULTIANEWARRAY. */
	SJME_BYTECODE_INSTRUCTION_MULTIANEWARRAY = 197,

	/** IFNULL. */
	SJME_BYTECODE_INSTRUCTION_IFNULL = 198,

	/** IFNONNULL. */
	SJME_BYTECODE_INSTRUCTION_IFNONNULL = 199,

	/** GOTO_W. */
	SJME_BYTECODE_INSTRUCTION_GOTO_W = 200,

	/** JSR_W. */
	SJME_BYTECODE_INSTRUCTION_JSR_W = 201,

	/** BREAKPOINT. */
	SJME_BYTECODE_INSTRUCTION_BREAKPOINT = 202,

	/** IMPDEP1. */
	SJME_BYTECODE_INSTRUCTION_IMPDEP1 = 254,

	/** IMPDEP2. */
	SJME_BYTECODE_INSTRUCTION_IMPDEP2 = 255,

	/** Wide ALOAD. */
	SJME_BYTECODE_INSTRUCTION_WIDE_ALOAD =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_ALOAD,

	/** Wide ILOAD. */
	SJME_BYTECODE_INSTRUCTION_WIDE_ILOAD =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_ILOAD,

	/** Wide LLOAD. */
	SJME_BYTECODE_INSTRUCTION_WIDE_LLOAD =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_LLOAD,

	/** Wide FLOAD. */
	SJME_BYTECODE_INSTRUCTION_WIDE_FLOAD =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_FLOAD,

	/** Wide DLOAD. */
	SJME_BYTECODE_INSTRUCTION_WIDE_DLOAD =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_DLOAD,

	/** Wide ASTORE. */
	SJME_BYTECODE_INSTRUCTION_WIDE_ASTORE =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_ASTORE,

	/** Wide ISTORE. */
	SJME_BYTECODE_INSTRUCTION_WIDE_ISTORE =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_ISTORE,

	/** Wide LSTORE. */
	SJME_BYTECODE_INSTRUCTION_WIDE_LSTORE =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_LSTORE,

	/** Wide FSTORE. */
	SJME_BYTECODE_INSTRUCTION_WIDE_FSTORE =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_FSTORE,

	/** Wide DSTORE. */
	SJME_BYTECODE_INSTRUCTION_WIDE_DSTORE =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_DSTORE,
	
	/** Wide IINC. */
	SJME_BYTECODE_INSTRUCTION_WIDE_IINC =
		(SJME_BYTECODE_INSTRUCTION_WIDE << 8) |
		SJME_BYTECODE_INSTRUCTION_IINC,
} sjme_byteCode_instruction;

/**
 * Function type for byte code execution.
 * 
 * @param frame The frame to execute under.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/18
 */
typedef sjme_jboolean (*sjme_byteCode_function)(sjme_nvm_frame* frame);

/**
 * Contains a mapping of a byte code instruction to an actual operation.
 * 
 * @since 2023/11/18
 */
typedef struct sjme_byteCode_functionMap
{
	/** The instruction this maps. */
	sjme_byteCode_instruction instruction;
	
	/** The function to execute. */
	sjme_byteCode_function function;
} sjme_byteCode_functionMap;

/**
 * Maps a specific bytecode to the mapping for later execution.
 * 
 * @param inInstruction The instruction to map.
 * @param outMapping The output mapping.
 * @return Returns @c SJME_JNI_TRUE if the instruction is valid and mappable.
 * @since 2023/11/18
 */
sjme_jboolean sjme_byteCode_map(
	sjme_attrInValue sjme_byteCode_instruction inInstruction,
	sjme_attrOutNotNull const sjme_byteCode_functionMap** outMapping);

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
