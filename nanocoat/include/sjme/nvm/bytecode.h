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

#include "sjme/config.h"
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
typedef sjme_jint sjme_nvm_byteCode_instruction;

/** NOP. */
#define SJME_NVM_BYTECODE_JAVA_NOP \
	((sjme_nvm_byteCode_instruction)INT32_C(0))

/** ACONST_NULL. */
#define SJME_NVM_BYTECODE_JAVA_ACONST_NULL \
	((sjme_nvm_byteCode_instruction)INT32_C(1))

/** ICONST_M1. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_M1 \
	((sjme_nvm_byteCode_instruction)INT32_C(2))

/** ICONST_0. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(3))

/** ICONST_1. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(4))

/** ICONST_2. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(5))

/** ICONST_3. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(6))

/** ICONST_4. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_4 \
	((sjme_nvm_byteCode_instruction)INT32_C(7))

/** ICONST_5. */
#define SJME_NVM_BYTECODE_JAVA_ICONST_5 \
	((sjme_nvm_byteCode_instruction)INT32_C(8))

/** LCONST_0. */
#define SJME_NVM_BYTECODE_JAVA_LCONST_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(9))

/** LCONST_1. */
#define SJME_NVM_BYTECODE_JAVA_LCONST_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(10))

/** FCONST_0. */
#define SJME_NVM_BYTECODE_JAVA_FCONST_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(11))

/** FCONST_1. */
#define SJME_NVM_BYTECODE_JAVA_FCONST_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(12))

/** FCONST_2. */
#define SJME_NVM_BYTECODE_JAVA_FCONST_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(13))

/** DCONST_0. */
#define SJME_NVM_BYTECODE_JAVA_DCONST_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(14))

/** DCONST_1. */
#define SJME_NVM_BYTECODE_JAVA_DCONST_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(15))

/** BIPUSH. */
#define SJME_NVM_BYTECODE_JAVA_BIPUSH \
	((sjme_nvm_byteCode_instruction)INT32_C(16))

/** SIPUSH. */
#define SJME_NVM_BYTECODE_JAVA_SIPUSH \
	((sjme_nvm_byteCode_instruction)INT32_C(17))

/** LDC. */
#define SJME_NVM_BYTECODE_JAVA_LDC \
	((sjme_nvm_byteCode_instruction)INT32_C(18))

/** LDC_W. */
#define SJME_NVM_BYTECODE_JAVA_LDC_W \
	((sjme_nvm_byteCode_instruction)INT32_C(19))

/** LDC2_W. */
#define SJME_NVM_BYTECODE_JAVA_LDC2_W \
	((sjme_nvm_byteCode_instruction)INT32_C(20))

/** ILOAD. */
#define SJME_NVM_BYTECODE_JAVA_ILOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(21))

/** LLOAD. */
#define SJME_NVM_BYTECODE_JAVA_LLOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(22))

/** FLOAD. */
#define SJME_NVM_BYTECODE_JAVA_FLOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(23))

/** DLOAD. */
#define SJME_NVM_BYTECODE_JAVA_DLOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(24))

/** ALOAD. */
#define SJME_NVM_BYTECODE_JAVA_ALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(25))

/** ILOAD_0. */
#define SJME_NVM_BYTECODE_JAVA_ILOAD_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(26))

/** ILOAD_1. */
#define SJME_NVM_BYTECODE_JAVA_ILOAD_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(27))

/** ILOAD_2. */
#define SJME_NVM_BYTECODE_JAVA_ILOAD_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(28))

/** ILOAD_3. */
#define SJME_NVM_BYTECODE_JAVA_ILOAD_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(29))

/** LLOAD_0. */
#define SJME_NVM_BYTECODE_JAVA_LLOAD_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(30))

/** LLOAD_1. */
#define SJME_NVM_BYTECODE_JAVA_LLOAD_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(31))

/** LLOAD_2. */
#define SJME_NVM_BYTECODE_JAVA_LLOAD_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(32))

/** LLOAD_3. */
#define SJME_NVM_BYTECODE_JAVA_LLOAD_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(33))

/** FLOAD_0. */
#define SJME_NVM_BYTECODE_JAVA_FLOAD_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(34))

/** FLOAD_1. */
#define SJME_NVM_BYTECODE_JAVA_FLOAD_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(35))

/** FLOAD_2. */
#define SJME_NVM_BYTECODE_JAVA_FLOAD_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(36))

/** FLOAD_3. */
#define SJME_NVM_BYTECODE_JAVA_FLOAD_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(37))

/** DLOAD_0. */
#define SJME_NVM_BYTECODE_JAVA_DLOAD_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(38))

/** DLOAD_1. */
#define SJME_NVM_BYTECODE_JAVA_DLOAD_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(39))

/** DLOAD_2. */
#define SJME_NVM_BYTECODE_JAVA_DLOAD_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(40))

/** DLOAD_3. */
#define SJME_NVM_BYTECODE_JAVA_DLOAD_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(41))

/** ALOAD_0. */
#define SJME_NVM_BYTECODE_JAVA_ALOAD_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(42))

/** ALOAD_1. */
#define SJME_NVM_BYTECODE_JAVA_ALOAD_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(43))

/** ALOAD_2. */
#define SJME_NVM_BYTECODE_JAVA_ALOAD_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(44))

/** ALOAD_3. */
#define SJME_NVM_BYTECODE_JAVA_ALOAD_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(45))

/** IALOAD. */
#define SJME_NVM_BYTECODE_JAVA_IALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(46))

/** LALOAD. */
#define SJME_NVM_BYTECODE_JAVA_LALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(47))

/** FALOAD. */
#define SJME_NVM_BYTECODE_JAVA_FALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(48))

/** DALOAD. */
#define SJME_NVM_BYTECODE_JAVA_DALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(49))

/** AALOAD. */
#define SJME_NVM_BYTECODE_JAVA_AALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(50))

/** BALOAD. */
#define SJME_NVM_BYTECODE_JAVA_BALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(51))

/** CALOAD. */
#define SJME_NVM_BYTECODE_JAVA_CALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(52))

/** SALOAD. */
#define SJME_NVM_BYTECODE_JAVA_SALOAD \
	((sjme_nvm_byteCode_instruction)INT32_C(53))

/** ISTORE. */
#define SJME_NVM_BYTECODE_JAVA_ISTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(54))

/** LSTORE. */
#define SJME_NVM_BYTECODE_JAVA_LSTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(55))

/** FSTORE. */
#define SJME_NVM_BYTECODE_JAVA_FSTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(56))

/** DSTORE. */
#define SJME_NVM_BYTECODE_JAVA_DSTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(57))

/** ASTORE. */
#define SJME_NVM_BYTECODE_JAVA_ASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(58))

/** ISTORE_0. */
#define SJME_NVM_BYTECODE_JAVA_ISTORE_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(59))

/** ISTORE_1. */
#define SJME_NVM_BYTECODE_JAVA_ISTORE_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(60))

/** ISTORE_2. */
#define SJME_NVM_BYTECODE_JAVA_ISTORE_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(61))

/** ISTORE_3. */
#define SJME_NVM_BYTECODE_JAVA_ISTORE_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(62))

/** LSTORE_0. */
#define SJME_NVM_BYTECODE_JAVA_LSTORE_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(63))

/** LSTORE_1. */
#define SJME_NVM_BYTECODE_JAVA_LSTORE_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(64))

/** LSTORE_2. */
#define SJME_NVM_BYTECODE_JAVA_LSTORE_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(65))

/** LSTORE_3. */
#define SJME_NVM_BYTECODE_JAVA_LSTORE_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(66))

/** FSTORE_0. */
#define SJME_NVM_BYTECODE_JAVA_FSTORE_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(67))

/** FSTORE_1. */
#define SJME_NVM_BYTECODE_JAVA_FSTORE_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(68))

/** FSTORE_2. */
#define SJME_NVM_BYTECODE_JAVA_FSTORE_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(69))

/** FSTORE_3. */
#define SJME_NVM_BYTECODE_JAVA_FSTORE_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(70))

/** DSTORE_0. */
#define SJME_NVM_BYTECODE_JAVA_DSTORE_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(71))

/** DSTORE_1. */
#define SJME_NVM_BYTECODE_JAVA_DSTORE_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(72))

/** DSTORE_2. */
#define SJME_NVM_BYTECODE_JAVA_DSTORE_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(73))

/** DSTORE_3. */
#define SJME_NVM_BYTECODE_JAVA_DSTORE_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(74))

/** ASTORE_0. */
#define SJME_NVM_BYTECODE_JAVA_ASTORE_0 \
	((sjme_nvm_byteCode_instruction)INT32_C(75))

/** ASTORE_1. */
#define SJME_NVM_BYTECODE_JAVA_ASTORE_1 \
	((sjme_nvm_byteCode_instruction)INT32_C(76))

/** ASTORE_2. */
#define SJME_NVM_BYTECODE_JAVA_ASTORE_2 \
	((sjme_nvm_byteCode_instruction)INT32_C(77))

/** ASTORE_3. */
#define SJME_NVM_BYTECODE_JAVA_ASTORE_3 \
	((sjme_nvm_byteCode_instruction)INT32_C(78))

/** IASTORE. */
#define SJME_NVM_BYTECODE_JAVA_IASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(79))

/** LASTORE. */
#define SJME_NVM_BYTECODE_JAVA_LASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(80))

/** FASTORE. */
#define SJME_NVM_BYTECODE_JAVA_FASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(81))

/** DASTORE. */
#define SJME_NVM_BYTECODE_JAVA_DASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(82))

/** AASTORE. */
#define SJME_NVM_BYTECODE_JAVA_AASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(83))

/** BASTORE. */
#define SJME_NVM_BYTECODE_JAVA_BASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(84))

/** CASTORE. */
#define SJME_NVM_BYTECODE_JAVA_CASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(85))

/** SASTORE. */
#define SJME_NVM_BYTECODE_JAVA_SASTORE \
	((sjme_nvm_byteCode_instruction)INT32_C(86))

/** POP. */
#define SJME_NVM_BYTECODE_JAVA_POP \
	((sjme_nvm_byteCode_instruction)INT32_C(87))

/** POP2. */
#define SJME_NVM_BYTECODE_JAVA_POP2 \
	((sjme_nvm_byteCode_instruction)INT32_C(88))

/** DUP. */
#define SJME_NVM_BYTECODE_JAVA_DUP \
	((sjme_nvm_byteCode_instruction)INT32_C(89))

/** DUP_X1. */
#define SJME_NVM_BYTECODE_JAVA_DUP_X1 \
	((sjme_nvm_byteCode_instruction)INT32_C(90))

/** DUP_X2. */
#define SJME_NVM_BYTECODE_JAVA_DUP_X2 \
	((sjme_nvm_byteCode_instruction)INT32_C(91))

/** DUP2. */
#define SJME_NVM_BYTECODE_JAVA_DUP2 \
	((sjme_nvm_byteCode_instruction)INT32_C(92))

/** DUP2_X1. */
#define SJME_NVM_BYTECODE_JAVA_DUP2_X1 \
	((sjme_nvm_byteCode_instruction)INT32_C(93))

/** DUP2_X2. */
#define SJME_NVM_BYTECODE_JAVA_DUP2_X2 \
	((sjme_nvm_byteCode_instruction)INT32_C(94))

/** SWAP. */
#define SJME_NVM_BYTECODE_JAVA_SWAP \
	((sjme_nvm_byteCode_instruction)INT32_C(95))

/** IADD. */
#define SJME_NVM_BYTECODE_JAVA_IADD \
	((sjme_nvm_byteCode_instruction)INT32_C(96))

/** LADD. */
#define SJME_NVM_BYTECODE_JAVA_LADD \
	((sjme_nvm_byteCode_instruction)INT32_C(97))

/** FADD. */
#define SJME_NVM_BYTECODE_JAVA_FADD \
	((sjme_nvm_byteCode_instruction)INT32_C(98))

/** DADD. */
#define SJME_NVM_BYTECODE_JAVA_DADD \
	((sjme_nvm_byteCode_instruction)INT32_C(99))

/** ISUB. */
#define SJME_NVM_BYTECODE_JAVA_ISUB \
	((sjme_nvm_byteCode_instruction)INT32_C(100))

/** LSUB. */
#define SJME_NVM_BYTECODE_JAVA_LSUB \
	((sjme_nvm_byteCode_instruction)INT32_C(101))

/** FSUB. */
#define SJME_NVM_BYTECODE_JAVA_FSUB \
	((sjme_nvm_byteCode_instruction)INT32_C(102))

/** DSUB. */
#define SJME_NVM_BYTECODE_JAVA_DSUB \
	((sjme_nvm_byteCode_instruction)INT32_C(103))

/** IMUL. */
#define SJME_NVM_BYTECODE_JAVA_IMUL \
	((sjme_nvm_byteCode_instruction)INT32_C(104))

/** LMUL. */
#define SJME_NVM_BYTECODE_JAVA_LMUL \
	((sjme_nvm_byteCode_instruction)INT32_C(105))

/** FMUL. */
#define SJME_NVM_BYTECODE_JAVA_FMUL \
	((sjme_nvm_byteCode_instruction)INT32_C(106))

/** DMUL. */
#define SJME_NVM_BYTECODE_JAVA_DMUL \
	((sjme_nvm_byteCode_instruction)INT32_C(107))

/** IDIV. */
#define SJME_NVM_BYTECODE_JAVA_IDIV \
	((sjme_nvm_byteCode_instruction)INT32_C(108))

/** LDIV. */
#define SJME_NVM_BYTECODE_JAVA_LDIV \
	((sjme_nvm_byteCode_instruction)INT32_C(109))

/** FDIV. */
#define SJME_NVM_BYTECODE_JAVA_FDIV \
	((sjme_nvm_byteCode_instruction)INT32_C(110))

/** DDIV. */
#define SJME_NVM_BYTECODE_JAVA_DDIV \
	((sjme_nvm_byteCode_instruction)INT32_C(111))

/** IREM. */
#define SJME_NVM_BYTECODE_JAVA_IREM \
	((sjme_nvm_byteCode_instruction)INT32_C(112))

/** LREM. */
#define SJME_NVM_BYTECODE_JAVA_LREM \
	((sjme_nvm_byteCode_instruction)INT32_C(113))

/** FREM. */
#define SJME_NVM_BYTECODE_JAVA_FREM \
	((sjme_nvm_byteCode_instruction)INT32_C(114))

/** DREM. */
#define SJME_NVM_BYTECODE_JAVA_DREM \
	((sjme_nvm_byteCode_instruction)INT32_C(115))

/** INEG. */
#define SJME_NVM_BYTECODE_JAVA_INEG \
	((sjme_nvm_byteCode_instruction)INT32_C(116))

/** LNEG. */
#define SJME_NVM_BYTECODE_JAVA_LNEG \
	((sjme_nvm_byteCode_instruction)INT32_C(117))

/** FNEG. */
#define SJME_NVM_BYTECODE_JAVA_FNEG \
	((sjme_nvm_byteCode_instruction)INT32_C(118))

/** DNEG. */
#define SJME_NVM_BYTECODE_JAVA_DNEG \
	((sjme_nvm_byteCode_instruction)INT32_C(119))

/** ISHL. */
#define SJME_NVM_BYTECODE_JAVA_ISHL \
	((sjme_nvm_byteCode_instruction)INT32_C(120))

/** LSHL. */
#define SJME_NVM_BYTECODE_JAVA_LSHL \
	((sjme_nvm_byteCode_instruction)INT32_C(121))

/** ISHR. */
#define SJME_NVM_BYTECODE_JAVA_ISHR \
	((sjme_nvm_byteCode_instruction)INT32_C(122))

/** LSHR. */
#define SJME_NVM_BYTECODE_JAVA_LSHR \
	((sjme_nvm_byteCode_instruction)INT32_C(123))

/** IUSHR. */
#define SJME_NVM_BYTECODE_JAVA_IUSHR \
	((sjme_nvm_byteCode_instruction)INT32_C(124))

/** LUSHR. */
#define SJME_NVM_BYTECODE_JAVA_LUSHR \
	((sjme_nvm_byteCode_instruction)INT32_C(125))

/** IAND. */
#define SJME_NVM_BYTECODE_JAVA_IAND \
	((sjme_nvm_byteCode_instruction)INT32_C(126))

/** LAND. */
#define SJME_NVM_BYTECODE_JAVA_LAND \
	((sjme_nvm_byteCode_instruction)INT32_C(127))

/** IOR. */
#define SJME_NVM_BYTECODE_JAVA_IOR \
	((sjme_nvm_byteCode_instruction)INT32_C(128))

/** LOR. */
#define SJME_NVM_BYTECODE_JAVA_LOR \
	((sjme_nvm_byteCode_instruction)INT32_C(129))

/** IXOR. */
#define SJME_NVM_BYTECODE_JAVA_IXOR \
	((sjme_nvm_byteCode_instruction)INT32_C(130))

/** LXOR. */
#define SJME_NVM_BYTECODE_JAVA_LXOR \
	((sjme_nvm_byteCode_instruction)INT32_C(131))

/** IINC. */
#define SJME_NVM_BYTECODE_JAVA_IINC \
	((sjme_nvm_byteCode_instruction)INT32_C(132))

/** I2L. */
#define SJME_NVM_BYTECODE_JAVA_I2L \
	((sjme_nvm_byteCode_instruction)INT32_C(133))

/** I2F. */
#define SJME_NVM_BYTECODE_JAVA_I2F \
	((sjme_nvm_byteCode_instruction)INT32_C(134))

/** I2D. */
#define SJME_NVM_BYTECODE_JAVA_I2D \
	((sjme_nvm_byteCode_instruction)INT32_C(135))

/** L2I. */
#define SJME_NVM_BYTECODE_JAVA_L2I \
	((sjme_nvm_byteCode_instruction)INT32_C(136))

/** L2F. */
#define SJME_NVM_BYTECODE_JAVA_L2F \
	((sjme_nvm_byteCode_instruction)INT32_C(137))

/** L2D. */
#define SJME_NVM_BYTECODE_JAVA_L2D \
	((sjme_nvm_byteCode_instruction)INT32_C(138))

/** F2I. */
#define SJME_NVM_BYTECODE_JAVA_F2I \
	((sjme_nvm_byteCode_instruction)INT32_C(139))

/** F2L. */
#define SJME_NVM_BYTECODE_JAVA_F2L \
	((sjme_nvm_byteCode_instruction)INT32_C(140))

/** F2D. */
#define SJME_NVM_BYTECODE_JAVA_F2D \
	((sjme_nvm_byteCode_instruction)INT32_C(141))

/** D2I. */
#define SJME_NVM_BYTECODE_JAVA_D2I \
	((sjme_nvm_byteCode_instruction)INT32_C(142))

/** D2L. */
#define SJME_NVM_BYTECODE_JAVA_D2L \
	((sjme_nvm_byteCode_instruction)INT32_C(143))

/** D2F. */
#define SJME_NVM_BYTECODE_JAVA_D2F \
	((sjme_nvm_byteCode_instruction)INT32_C(144))

/** I2B. */
#define SJME_NVM_BYTECODE_JAVA_I2B \
	((sjme_nvm_byteCode_instruction)INT32_C(145))

/** I2C. */
#define SJME_NVM_BYTECODE_JAVA_I2C \
	((sjme_nvm_byteCode_instruction)INT32_C(146))

/** I2S. */
#define SJME_NVM_BYTECODE_JAVA_I2S \
	((sjme_nvm_byteCode_instruction)INT32_C(147))

/** LCMP. */
#define SJME_NVM_BYTECODE_JAVA_LCMP \
	((sjme_nvm_byteCode_instruction)INT32_C(148))

/** FCMPL. */
#define SJME_NVM_BYTECODE_JAVA_FCMPL \
	((sjme_nvm_byteCode_instruction)INT32_C(149))

/** FCMPG. */
#define SJME_NVM_BYTECODE_JAVA_FCMPG \
	((sjme_nvm_byteCode_instruction)INT32_C(150))

/** DCMPL. */
#define SJME_NVM_BYTECODE_JAVA_DCMPL \
	((sjme_nvm_byteCode_instruction)INT32_C(151))

/** DCMPG. */
#define SJME_NVM_BYTECODE_JAVA_DCMPG \
	((sjme_nvm_byteCode_instruction)INT32_C(152))

/** IFEQ. */
#define SJME_NVM_BYTECODE_JAVA_IFEQ \
	((sjme_nvm_byteCode_instruction)INT32_C(153))

/** IFNE. */
#define SJME_NVM_BYTECODE_JAVA_IFNE \
	((sjme_nvm_byteCode_instruction)INT32_C(154))

/** IFLT. */
#define SJME_NVM_BYTECODE_JAVA_IFLT \
	((sjme_nvm_byteCode_instruction)INT32_C(155))

/** IFGE. */
#define SJME_NVM_BYTECODE_JAVA_IFGE \
	((sjme_nvm_byteCode_instruction)INT32_C(156))

/** IFGT. */
#define SJME_NVM_BYTECODE_JAVA_IFGT \
	((sjme_nvm_byteCode_instruction)INT32_C(157))

/** IFLE. */
#define SJME_NVM_BYTECODE_JAVA_IFLE \
	((sjme_nvm_byteCode_instruction)INT32_C(158))

/** IF_ICMPEQ. */
#define SJME_NVM_BYTECODE_JAVA_IF_ICMPEQ \
	((sjme_nvm_byteCode_instruction)INT32_C(159))

/** IF_ICMPNE. */
#define SJME_NVM_BYTECODE_JAVA_IF_ICMPNE \
	((sjme_nvm_byteCode_instruction)INT32_C(160))

/** IF_ICMPLT. */
#define SJME_NVM_BYTECODE_JAVA_IF_ICMPLT \
	((sjme_nvm_byteCode_instruction)INT32_C(161))

/** IF_ICMPGE. */
#define SJME_NVM_BYTECODE_JAVA_IF_ICMPGE \
	((sjme_nvm_byteCode_instruction)INT32_C(162))

/** IF_ICMPGT. */
#define SJME_NVM_BYTECODE_JAVA_IF_ICMPGT \
	((sjme_nvm_byteCode_instruction)INT32_C(163))

/** IF_ICMPLE. */
#define SJME_NVM_BYTECODE_JAVA_IF_ICMPLE \
	((sjme_nvm_byteCode_instruction)INT32_C(164))

/** IF_ACMPEQ. */
#define SJME_NVM_BYTECODE_JAVA_IF_ACMPEQ \
	((sjme_nvm_byteCode_instruction)INT32_C(165))

/** IF_ACMPNE. */
#define SJME_NVM_BYTECODE_JAVA_IF_ACMPNE \
	((sjme_nvm_byteCode_instruction)INT32_C(166))

/** GOTO. */
#define SJME_NVM_BYTECODE_JAVA_GOTO \
	((sjme_nvm_byteCode_instruction)INT32_C(167))

/** JSR. */
#define SJME_NVM_BYTECODE_JAVA_JSR \
	((sjme_nvm_byteCode_instruction)INT32_C(168))

/** RET. */
#define SJME_NVM_BYTECODE_JAVA_RET \
	((sjme_nvm_byteCode_instruction)INT32_C(169))

/** TABLESWITCH. */
#define SJME_NVM_BYTECODE_JAVA_TABLESWITCH \
	((sjme_nvm_byteCode_instruction)INT32_C(170))

/** LOOKUPSWITCH. */
#define SJME_NVM_BYTECODE_JAVA_LOOKUPSWITCH \
	((sjme_nvm_byteCode_instruction)INT32_C(171))

/** IRETURN. */
#define SJME_NVM_BYTECODE_JAVA_IRETURN \
	((sjme_nvm_byteCode_instruction)INT32_C(172))

/** LRETURN. */
#define SJME_NVM_BYTECODE_JAVA_LRETURN \
	((sjme_nvm_byteCode_instruction)INT32_C(173))

/** FRETURN. */
#define SJME_NVM_BYTECODE_JAVA_FRETURN \
	((sjme_nvm_byteCode_instruction)INT32_C(174))

/** DRETURN. */
#define SJME_NVM_BYTECODE_JAVA_DRETURN \
	((sjme_nvm_byteCode_instruction)INT32_C(175))

/** ARETURN. */
#define SJME_NVM_BYTECODE_JAVA_ARETURN \
	((sjme_nvm_byteCode_instruction)INT32_C(176))

/** RETURN. */
#define SJME_NVM_BYTECODE_JAVA_RETURN \
	((sjme_nvm_byteCode_instruction)INT32_C(177))

/** GETSTATIC. */
#define SJME_NVM_BYTECODE_JAVA_GETSTATIC \
	((sjme_nvm_byteCode_instruction)INT32_C(178))

/** PUTSTATIC. */
#define SJME_NVM_BYTECODE_JAVA_PUTSTATIC \
	((sjme_nvm_byteCode_instruction)INT32_C(179))

/** GETFIELD. */
#define SJME_NVM_BYTECODE_JAVA_GETFIELD \
	((sjme_nvm_byteCode_instruction)INT32_C(180))

/** PUTFIELD. */
#define SJME_NVM_BYTECODE_JAVA_PUTFIELD \
	((sjme_nvm_byteCode_instruction)INT32_C(181))

/** INVOKEVIRTUAL. */
#define SJME_NVM_BYTECODE_JAVA_INVOKEVIRTUAL \
	((sjme_nvm_byteCode_instruction)INT32_C(182))

/** INVOKESPECIAL. */
#define SJME_NVM_BYTECODE_JAVA_INVOKESPECIAL \
	((sjme_nvm_byteCode_instruction)INT32_C(183))

/** INVOKESTATIC. */
#define SJME_NVM_BYTECODE_JAVA_INVOKESTATIC \
	((sjme_nvm_byteCode_instruction)INT32_C(184))

/** INVOKEINTERFACE. */
#define SJME_NVM_BYTECODE_JAVA_INVOKEINTERFACE \
	((sjme_nvm_byteCode_instruction)INT32_C(185))

/** INVOKEDYNAMIC. */
#define SJME_NVM_BYTECODE_JAVA_INVOKEDYNAMIC \
	((sjme_nvm_byteCode_instruction)INT32_C(186))

/** NEW. */
#define SJME_NVM_BYTECODE_JAVA_NEW \
	((sjme_nvm_byteCode_instruction)INT32_C(187))

/** NEWARRAY. */
#define SJME_NVM_BYTECODE_JAVA_NEWARRAY \
	((sjme_nvm_byteCode_instruction)INT32_C(188))

/** ANEWARRAY. */
#define SJME_NVM_BYTECODE_JAVA_ANEWARRAY \
	((sjme_nvm_byteCode_instruction)INT32_C(189))

/** ARRAYLENGTH. */
#define SJME_NVM_BYTECODE_JAVA_ARRAYLENGTH \
	((sjme_nvm_byteCode_instruction)INT32_C(190))

/** ATHROW. */
#define SJME_NVM_BYTECODE_JAVA_ATHROW \
	((sjme_nvm_byteCode_instruction)INT32_C(191))

/** CHECKCAST. */
#define SJME_NVM_BYTECODE_JAVA_CHECKCAST \
	((sjme_nvm_byteCode_instruction)INT32_C(192))

/** INSTANCEOF. */
#define SJME_NVM_BYTECODE_JAVA_INSTANCEOF \
	((sjme_nvm_byteCode_instruction)INT32_C(193))

/** MONITORENTER. */
#define SJME_NVM_BYTECODE_JAVA_MONITORENTER \
	((sjme_nvm_byteCode_instruction)INT32_C(194))

/** MONITOREXIT. */
#define SJME_NVM_BYTECODE_JAVA_MONITOREXIT \
	((sjme_nvm_byteCode_instruction)INT32_C(195))

/** WIDE. */
#define SJME_NVM_BYTECODE_JAVA_WIDE \
	((sjme_nvm_byteCode_instruction)INT32_C(196))

/** MULTIANEWARRAY. */
#define SJME_NVM_BYTECODE_JAVA_MULTIANEWARRAY \
	((sjme_nvm_byteCode_instruction)INT32_C(197))

/** IFNULL. */
#define SJME_NVM_BYTECODE_JAVA_IFNULL \
	((sjme_nvm_byteCode_instruction)INT32_C(198))

/** IFNONNULL. */
#define SJME_NVM_BYTECODE_JAVA_IFNONNULL \
	((sjme_nvm_byteCode_instruction)INT32_C(199))

/** GOTO_W. */
#define SJME_NVM_BYTECODE_JAVA_GOTO_W \
	((sjme_nvm_byteCode_instruction)INT32_C(200))

/** JSR_W. */
#define SJME_NVM_BYTECODE_JAVA_JSR_W \
	((sjme_nvm_byteCode_instruction)INT32_C(201))

/** BREAKPOINT. */
#define SJME_NVM_BYTECODE_JAVA_BREAKPOINT \
	((sjme_nvm_byteCode_instruction)INT32_C(202))

/** IMPDEP1. */
#define SJME_NVM_BYTECODE_JAVA_IMPDEP1 \
	((sjme_nvm_byteCode_instruction)INT32_C(254))

/** IMPDEP2. */
#define SJME_NVM_BYTECODE_JAVA_IMPDEP2 \
	((sjme_nvm_byteCode_instruction)INT32_C(255))

/** The number of base Java instructions. */
#define SJME_NVM_NUM_JAVA_BYTECODES \
	((sjme_nvm_byteCode_instruction)INT32_C(256))

/** Wide ALOAD. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_ALOAD \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_ALOAD)

/** Wide ILOAD. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_ILOAD \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_ILOAD)

/** Wide LLOAD. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_LLOAD \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_LLOAD)

/** Wide FLOAD. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_FLOAD \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_FLOAD)

/** Wide DLOAD. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_DLOAD \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_DLOAD)

/** Wide ASTORE. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_ASTORE \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_ASTORE)

/** Wide ISTORE. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_ISTORE \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_ISTORE)

/** Wide LSTORE. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_LSTORE \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_LSTORE)

/** Wide FSTORE. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_FSTORE \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_FSTORE)

/** Wide DSTORE. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_DSTORE \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_DSTORE)

/** Wide IINC. */
#define SJME_NVM_BYTECODE_JAVA_WIDE_IINC \
	((SJME_NVM_BYTECODE_JAVA_WIDE << 8) | \
	SJME_NVM_BYTECODE_JAVA_IINC)

/**
 * Specifies the type of PC address change occurs.
 *
 * @since 2025/01/11
 */
typedef enum sjme_nvm_byteCode_pcNewType
{
	/** This is an int based enum. */
	sjme_enumInt(sjme_nvm_byteCode_pcNewType),

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

/** The type used for LUT tables. */
typedef sjme_nvm_byteCode_func sjme_nvm_byteCode_lutTableType
	[SJME_NVM_NUM_JAVA_BYTECODES];

/** The length of each instruction. */
extern const sjme_jbyte sjme_nvm_byteCode_lengths[SJME_NVM_NUM_JAVA_BYTECODES];

/** The name of each instruction. */
extern const sjme_lpcstr sjme_nvm_byteCode_names[SJME_NVM_NUM_JAVA_BYTECODES];

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
 * Returns the LUT table to use for the given byte code.
 *
 * @param id The byte code identifier.
 * @return The LUT table to use.
 * @since 2026/10/09
 */
const sjme_nvm_byteCode_lutTableType* sjme_nvm_byteCodeLutTable(
	sjme_attrInRange(0, 256) sjme_byteCode id);

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
