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

#ifndef SJME_C_COREGENERIC_H
#define SJME_C_COREGENERIC_H

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
 * Base function for component initialization logic.
 * 
 * @param inState The input state. 
 * @param inComponent The component item to be initialized.
 * @param inData Any data to use for initialization.
 * @return Any resultant error, if any.
 * @since 2024/07/24
 */
typedef sjme_errorCode (*sjme_scritchui_coreGeneric_componentNewImplFunc)(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_pointer inData);

/**
 * Basic core component initialization logic.
 * 
 * @param inState The input state. 
 * @param outComponent The resultant component.
 * @param outComponentSize The size of the resultant component.
 * @param uiType The UI type to initialize.
 * @param implNew The implementation new for this type.
 * @param inData Any data to pass to @c implNew .
 * @return Any resultant error, if any.
 * @since 2024/07/24
 */
sjme_errorCode sjme_scritchui_coreGeneric_componentNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiComponent* outComponent,
	sjme_attrInPositiveNonZero sjme_jint outComponentSize,
	sjme_attrInRange(0, SJME_SCRITCHUI_NUM_UI_TYPES)
		sjme_scritchui_uiType uiType,
	sjme_attrInNotNull sjme_scritchui_coreGeneric_componentNewImplFunc implNew,
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
