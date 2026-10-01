/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Class helpers.
 *
 * @file
 * @since 2026/09/22
 */

#ifndef SJME_C_SQUIRRELJME_CLASSYVMCLASS_H
#define SJME_C_SQUIRRELJME_CLASSYVMCLASS_H

#include "sjme/nvm/classyVm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CLASSYVMCLASS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Maps @link sjme_jclass @endlink names to a list
 * of @link sjme_nvm_stringPool_string @endlink.
 *
 * @param inState The input NVM state.
 * @param countUp Should names be counted up? This should generally be true
 * except for cases where this is only used for debugging information.
 * @param outList The resultant list.
 * @param numClasses The number of classes to map.
 * @param classes The classes to be mapped.
 * @return Any resultant error, if any.
 * @since 2026/09/22
 */
sjme_errorCode sjme_nvm_vmClass_poolNamesLFromClassesA(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInValue sjme_jboolean countUp,
	sjme_attrOutNotNull sjme_list(sjme_nvm_stringPool_string)** outList,
	sjme_attrInPositive sjme_jint numClasses,
	sjme_attrInNotNull sjme_jclass* classes);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CLASSYVMCLASS_H
}
#undef SJME_CXX_SQUIRRELJME_CLASSYVMCLASS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSYVMCLASS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CLASSYVMCLASS_H */