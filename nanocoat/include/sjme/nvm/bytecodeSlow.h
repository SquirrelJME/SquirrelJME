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
	SJME_NVM_BYTECODE_NAME(slow, which)
	
/**
 * Declares a slow bytecode.
 *
 * @param which Which byte code is declared?
 * @since 2025/01/08
 */
#define SJME_NVM_BYTECODE_SLOW(which) \
	SJME_NVM_BYTECODE(slow, which)

/* clang-format off */ /* @formatter:off */
/*--------------------------------------------------------------------------*/
	
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
SJME_NVM_BYTECODE_SLOW(LookupSwitch);
SJME_NVM_BYTECODE_SLOW(Goto);
SJME_NVM_BYTECODE_SLOW(GotoWide);
SJME_NVM_BYTECODE_SLOW(NoOp);
SJME_NVM_BYTECODE_SLOW(ReturnX);
SJME_NVM_BYTECODE_SLOW(TableSwitch);

/* Local */
SJME_NVM_BYTECODE_SLOW(IInc);
SJME_NVM_BYTECODE_SLOW(IIncWide);
SJME_NVM_BYTECODE_SLOW(XLoad);
SJME_NVM_BYTECODE_SLOW(XLoadZ);
SJME_NVM_BYTECODE_SLOW(XStore);
SJME_NVM_BYTECODE_SLOW(XStoreZ);

/* Math */
SJME_NVM_BYTECODE_SLOW(CastDoubleToX);
SJME_NVM_BYTECODE_SLOW(CastFloatToX);
SJME_NVM_BYTECODE_SLOW(CastIntToX);
SJME_NVM_BYTECODE_SLOW(CastLongToX);
SJME_NVM_BYTECODE_SLOW(CompareDouble);
SJME_NVM_BYTECODE_SLOW(CompareFloat);
SJME_NVM_BYTECODE_SLOW(CompareLong);
SJME_NVM_BYTECODE_SLOW(MathBinaryInt);
SJME_NVM_BYTECODE_SLOW(MathBinaryLong);
SJME_NVM_BYTECODE_SLOW(MathDouble);
SJME_NVM_BYTECODE_SLOW(MathFloat);
SJME_NVM_BYTECODE_SLOW(MathInt);
SJME_NVM_BYTECODE_SLOW(MathLong);
SJME_NVM_BYTECODE_SLOW(MathNegateDouble);
SJME_NVM_BYTECODE_SLOW(MathNegateFloat);
SJME_NVM_BYTECODE_SLOW(MathNegateInt);
SJME_NVM_BYTECODE_SLOW(MathNegateLong);

/* Other */
SJME_NVM_BYTECODE_SLOW(Wide);

/* Reference */
SJME_NVM_BYTECODE_SLOW(ArrayLength);
SJME_NVM_BYTECODE_SLOW(CheckCast);
SJME_NVM_BYTECODE_SLOW(InstanceAccess);
SJME_NVM_BYTECODE_SLOW(InstanceOf);
SJME_NVM_BYTECODE_SLOW(InvokeInterface);
SJME_NVM_BYTECODE_SLOW(InvokeSpecial);
SJME_NVM_BYTECODE_SLOW(InvokeStatic);
SJME_NVM_BYTECODE_SLOW(InvokeVirtual);
SJME_NVM_BYTECODE_SLOW(Monitor);
SJME_NVM_BYTECODE_SLOW(New);
SJME_NVM_BYTECODE_SLOW(NewArray);
SJME_NVM_BYTECODE_SLOW(NewArrayA);
SJME_NVM_BYTECODE_SLOW(NewArrayMulti);
SJME_NVM_BYTECODE_SLOW(StaticAccess);
SJME_NVM_BYTECODE_SLOW(Throw);
SJME_NVM_BYTECODE_SLOW(XALoad);
SJME_NVM_BYTECODE_SLOW(XAStore);

/* Stack */
SJME_NVM_BYTECODE_SLOW(Dup);
SJME_NVM_BYTECODE_SLOW(DupX1);
SJME_NVM_BYTECODE_SLOW(DupX2);
SJME_NVM_BYTECODE_SLOW(DupTwo);
SJME_NVM_BYTECODE_SLOW(DupTwoX1);
SJME_NVM_BYTECODE_SLOW(DupTwoX2);
SJME_NVM_BYTECODE_SLOW(Pop);
SJME_NVM_BYTECODE_SLOW(PopTwo);
SJME_NVM_BYTECODE_SLOW(Swap);

/*--------------------------------------------------------------------------*/
/* clang-format on */ /* @formatter:on */

/**
 * Which math binary function is being executed?
 *
 * @since 2025/06/19
 */
typedef enum sjme_nvm_byteCode_mathBinaryFunc
{
	/** Shift left. */
	SJME_NVM_BYTECODE_MATH_SHL,

	/** Shift right. */
	SJME_NVM_BYTECODE_MATH_SHR,

	/** Unsigned shift right. */
	SJME_NVM_BYTECODE_MATH_USHR,

	/** AND. */
	SJME_NVM_BYTECODE_MATH_AND,

	/** OR. */
	SJME_NVM_BYTECODE_MATH_OR,

	/** XOR. */
	SJME_NVM_BYTECODE_MATH_XOR,

	/** The number of binary math functions. */
	SJME_NVM_BYTECODE_NUM_MATH_BINARY_FUNC,
} sjme_nvm_byteCode_mathBinaryFunc;

/**
 * Which math function is being executed?
 *
 * @since 2025/06/19
 */
typedef enum sjme_nvm_byteCode_mathFunc
{
	/** Add. */
	SJME_NVM_BYTECODE_MATH_ADD,

	/** Subtract. */
	SJME_NVM_BYTECODE_MATH_SUB,

	/** Multiply. */
	SJME_NVM_BYTECODE_MATH_MUL,

	/** Divide. */
	SJME_NVM_BYTECODE_MATH_DIV,

	/** Remainder. */
	SJME_NVM_BYTECODE_MATH_REM,

	/** The number of math functions. */
	SJME_NVM_BYTECODE_NUM_MATH_FUNC,
} sjme_nvm_byteCode_mathFunc;
	
/** Narrow slow bytecode handlers. */
extern const sjme_nvm_byteCode_func sjme_nvm_byteCode_slowNarrowFunctions
	[SJME_NVM_NUM_JAVA_BYTECODES];

/** Wide slow bytecode handlers. */
extern const sjme_nvm_byteCode_func sjme_nvm_byteCode_slowWideFunctions
	[SJME_NVM_NUM_JAVA_BYTECODES];

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
