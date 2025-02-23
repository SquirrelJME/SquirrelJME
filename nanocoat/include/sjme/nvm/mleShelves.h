/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Contains references to the MLE shelves.
 * 
 * @since 2025/02/22
 */

#ifndef SQUIRRELJME_MLESHELVES_H
#define SQUIRRELJME_MLESHELVES_H

#include "sjme/nvm/mle.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_MLESHELVES_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/** Integer type. */
#define SJME_MI SJME_JAVA_TYPE_ID_INTEGER

/** Object type. */
#define SJME_ML SJME_JAVA_TYPE_ID_OBJECT

/** Long type. */
#define SJME_MJ SJME_JAVA_TYPE_ID_LONG
	
/** Void type. */
#define SJME_MV SJME_JAVA_TYPE_ID_VOID

/** MLE Function name. */
#define SJME_NVM_MLE_FUNCTION_NAME(name, alt) \
	SJME_TOKEN_PASTE4(sjme_nvm_mleFunc_, name, _, alt)

/** MLE Function definition. */
#define SJME_NVM_MLE_FUNCTION_DECL_ALT(name, alt) \
	sjme_errorCode SJME_NVM_MLE_FUNCTION_NAME(name, alt)( \
		sjme_attrInNotNull sjme_nvm_frame inFrame, \
		sjme_attrInNotNull sjme_jvalueTyped* argR, \
		sjme_attrInPositive sjme_jint argC, \
		sjme_attrInNullable sjme_jvalueTyped* argV)

/** MLE Function definition. */
#define SJME_NVM_MLE_FUNCTION_DECL(name) \
	SJME_NVM_MLE_FUNCTION_DECL_ALT(name, none)

/** Defines an MLE function. */
#define SJME_NVM_MLE_DEFINE_ALT(name, alt, type, argX) \
	{ \
		#name, type, \
		argX, \
		SJME_NVM_MLE_FUNCTION_NAME(name, alt) \
	}

/** Defines an MLE function. */
#define SJME_NVM_MLE_DEFINE(name, type, argX) \
	SJME_NVM_MLE_DEFINE_ALT(name, none, type, argX)

/** Stop MLE definitions. */
#define SJME_NVM_MLE_STOP() \
	{NULL, NULL, NULL, NULL}
	
/** Runtime shelf. */
extern const sjme_nvm_mleShelf sjme_nvm_mleRuntimeShelf[];
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_MLESHELVES_H
}
#undef SJME_CXX_MLESHELVES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_MLESHELVES_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MLESHELVES_H */
