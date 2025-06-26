/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Fast bytecodes.
 * 
 * @since 2025/06/26
 */

#ifndef SJME_C_BYTECODEFAST_H
#define SJME_C_BYTECODEFAST_H

#include "sjme/nvm/bytecode.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_BYTECODEFAST_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The name of a fast byte code. */
#define SJME_NVM_BYTECODE_FAST_NAME(which) \
	SJME_NVM_BYTECODE_NAME(fast, which)
	
/** Declares a fast bytecode. */
#define SJME_NVM_BYTECODE_FAST(which) \
	SJME_NVM_BYTECODE(fast, which)

/**
 * Fast bytecode instructions.
 *
 * @since 2025/06/26
 */
typedef enum sjme_nvm_byteCode_fastInstruction
{
	/** Pop two narrows. */
	SJME_NVM_BYTECODE_FAST_POP_TWO_NARROW = 254,
	
	/** Pop a single wide. */
	SJME_NVM_BYTECODE_FAST_POP_WIDE = 255,
} sjme_nvm_byteCode_fastInstruction;

/* clang-format off */ /* @formatter:off */
/*--------------------------------------------------------------------------*/

/* Constant */

/* Flow */

/* Local */

/* Math */

/* Other */

/* Reference */

/* Stack */
SJME_NVM_BYTECODE_FAST(PopTwoNarrow);
SJME_NVM_BYTECODE_FAST(PopWide);

/*--------------------------------------------------------------------------*/
/* clang-format on */ /* @formatter:on */

/** Fast bytecode instructions. */
extern const sjme_nvm_byteCode_func sjme_nvm_byteCode_fastFunctions
	[SJME_NVM_NUM_JAVA_BYTECODES];

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_BYTECODEFAST_H
}
#undef SJME_CXX_BYTECODEFAST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_BYTECODEFAST_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_BYTECODEFAST_H */
