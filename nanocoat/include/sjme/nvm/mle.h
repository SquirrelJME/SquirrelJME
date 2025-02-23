/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Native shelves.
 * 
 * @since 2025/02/22
 */

#ifndef SQUIRRELJME_MLE_H
#define SQUIRRELJME_MLE_H

#include "sjme/config.h"
#include "sjme/nvm/task.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLE_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Handles the native MLE function call.
 * 
 * @param inFrame The frame this is being called from.
 * @param argR The return value of the call.
 * @param argC The argument count.
 * @param argV The argument values.
 * @return Any resultant error, if any.
 * @since 2025/02/22
 */
typedef sjme_errorCode (*sjme_nvm_mleFunc)(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV);
	
/**
 * Contains the implementation pointers for the runtime shelf.
 *
 * @since 2025/02/22
 */
typedef struct sjme_nvm_mleShelf
{
	/** The method name. */
	sjme_lpcstr name;

	/** The method type. */
	sjme_lpcstr type;

	/** Return type. */
	sjme_javaTypeId argR;

	/** Argument count. */
	sjme_jint argC;

	/** Argument types. */
	const sjme_javaTypeId* argV;

	/** The function which handles the call. */
	sjme_nvm_mleFunc function;
} sjme_nvm_mleShelf;
	
/**
 * Contains the root class information and handler structure for the run-time
 * shelves.
 *
 * @since 2025/02/22
 */
typedef struct sjme_nvm_mle
{
	/** The name of the shelf class. */
	sjme_lpcstr className;

	/** The target shelf functions. */
	const sjme_nvm_mleShelf* shelf;
} sjme_nvm_mle;

/**
 * Performs a MLE native function call.
 * 
 * @param inFrame The frame this is being called from.
 * @param className The class to target.
 * @param methodName The method name.
 * @param methodType The method type.
 * @param argR The return value of the call.
 * @param argC The argument count.
 * @param argV The argument values.
 * @return Any resultant error, if any.
 * @since 2025/02/22
 */
sjme_errorCode sjme_mle_mleCall(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_lpcstr className,
	sjme_attrInNotNull sjme_lpcstr methodName,
	sjme_attrInNotNull sjme_lpcstr methodType,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLE_H
}
#undef SJME_CXX_MLE_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLE_H */
