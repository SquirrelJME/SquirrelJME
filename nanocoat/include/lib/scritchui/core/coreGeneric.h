/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Generic core functions.
 * 
 * @since 2024/07/22
 */

#ifndef SQUIRRELJME_COREGENERIC_H
#define SQUIRRELJME_COREGENERIC_H

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COREGENERIC_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Base function for common initialization logic.
 * 
 * @param inState The input state. 
 * @param inCommon The common item to be initialized.
 * @param inData Any data to use for initialization.
 * @return Any resultant error, if any.
 * @since 2024/07/22
 */
typedef sjme_errorCode (*sjme_scritchui_coreGeneric_commonNewImplFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inCommon,
	sjme_attrInNullable sjme_pointer inData);

/**
 * Basic core common initialization logic.
 * 
 * @param inState The input state. 
 * @param outCommon The resultant common.
 * @param outCommonSize The size of the resultant common.
 * @param uiType The UI type to initialize.
 * @param implNew The implementation new for this type.
 * @param inData Any data to pass to @c implNew .
 * @return Any resultant error, if any.
 * @since 2024/07/22
 */
sjme_errorCode sjme_scritchui_coreGeneric_commonNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiCommon* outCommon,
	sjme_attrInPositiveNonZero sjme_jint outCommonSize,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_UI_TYPES)
		sjme_scritchui_uiType uiType,
	sjme_attrInNotNull sjme_scritchui_coreGeneric_commonNewImplFunc implNew,
	sjme_attrInNullable sjme_pointer inData);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COREGENERIC_H
}
		#undef SJME_CXX_SQUIRRELJME_COREGENERIC_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COREGENERIC_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COREGENERIC_H */
