/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Slow bytecodes.
 * 
 * @since 2025/01/08
 */

#ifndef SQUIRRELJME_BYTECODESLOW_H
#define SQUIRRELJME_BYTECODESLOW_H

#include "sjme/nvm/bytecode.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_BYTECODESLOW_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The name of a slow byte code.
 *
 * @param which The name of the byte code.
 * @since 2025/01/08
 */
#define SJME_NVM_BYTECODE_SLOW_NAME(which) \
	SJME_TOKEN_PASTE(sjme_nvm_byteCode_slow, which)
	
/**
 * Declares a slow bytecode.
 *
 * @param which Which byte code is declared?
 * @since 2025/01/08
 */
#define SJME_NVM_BYTECODE_SLOW(which) \
	sjme_errorCode SJME_NVM_BYTECODE_SLOW_NAME(which) ( \
		sjme_attrInNotNull sjme_nvm_frame inFrame, \
		sjme_attrInRange(0, 256) sjme_byteCode id, \
		sjme_attrInNotNull sjme_byteCode* relRawCode, \
		sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew)

/* clang-format off */ /* @formatter:off */
/*--------------------------------------------------------------------------*/

/** Common entry for slow byte code. */
#define SJME_NVM_BYTECODE_SLOW_ENTRY \
	sjme_errorCode error; \
	if (inFrame == NULL || relRawCode == NULL || pcNew == NULL) \
		return SJME_ERROR_NULL_ARGUMENTS

/** Common exit for slow byte code. */
#define SJME_NVM_BYTECODE_SLOW_EXIT \
	return SJME_ERROR_NONE;
	
/* Array */
	
/* Compare */

/* Constant */
SJME_NVM_BYTECODE_SLOW(AConstNull);
SJME_NVM_BYTECODE_SLOW(BIPush);
SJME_NVM_BYTECODE_SLOW(DConstZ);
SJME_NVM_BYTECODE_SLOW(FConstZ);
SJME_NVM_BYTECODE_SLOW(IConstM);
SJME_NVM_BYTECODE_SLOW(LConstZ);
SJME_NVM_BYTECODE_SLOW(Ldc);
SJME_NVM_BYTECODE_SLOW(LdcW);
SJME_NVM_BYTECODE_SLOW(LdcWTwo);
SJME_NVM_BYTECODE_SLOW(SIPush);

/* Flow */
SJME_NVM_BYTECODE_SLOW(IfAX);
SJME_NVM_BYTECODE_SLOW(IfX);
SJME_NVM_BYTECODE_SLOW(IfACmpX);
SJME_NVM_BYTECODE_SLOW(IfICmpX);
SJME_NVM_BYTECODE_SLOW(Goto);
SJME_NVM_BYTECODE_SLOW(NoOp);
SJME_NVM_BYTECODE_SLOW(ReturnX);
SJME_NVM_BYTECODE_SLOW(TableSwitch);

/* Local */
SJME_NVM_BYTECODE_SLOW(IInc);
SJME_NVM_BYTECODE_SLOW(XLoad);
SJME_NVM_BYTECODE_SLOW(XLoadZ);
SJME_NVM_BYTECODE_SLOW(XStore);
SJME_NVM_BYTECODE_SLOW(XStoreZ);

/* Math */

/* Reference */
SJME_NVM_BYTECODE_SLOW(CheckCast);
SJME_NVM_BYTECODE_SLOW(InvokeStatic);
SJME_NVM_BYTECODE_SLOW(InvokeVirtual);
SJME_NVM_BYTECODE_SLOW(New);

/* Stack */
SJME_NVM_BYTECODE_SLOW(Pop);

/*--------------------------------------------------------------------------*/
/* clang-format on */ /* @formatter:on */

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_BYTECODESLOW_H
}
#undef SJME_CXX_BYTECODESLOW_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_BYTECODESLOW_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BYTECODESLOW_H */
