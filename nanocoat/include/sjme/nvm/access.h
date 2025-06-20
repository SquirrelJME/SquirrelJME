/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Access checking.
 * 
 * @since 2025/06/19
 */

#ifndef SJME_C_ACCESS_H
#define SJME_C_ACCESS_H

#include "sjme/config.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/classyVm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_ACCESS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Determines whether the current frame has access to the given field.
 * 
 * @param fromFrame The source frame.
 * @param toField The destination field.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_access_checkFToF(
	sjme_attrInNotNull sjme_nvm_frame fromFrame,
	sjme_attrInNotNull sjme_jfieldID toField);

/**
 * Determines whether the current frame has access to the given method.
 * 
 * @param fromFrame The source frame.
 * @param toMethod The destination method.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_access_checkFToM(
	sjme_attrInNotNull sjme_nvm_frame fromFrame,
	sjme_attrInNotNull sjme_jmethodID toMethod);

/**
 * Determines whether the current method has access to the given method.
 * 
 * @param fromMethod The source method.
 * @param toMethod The destination method.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_access_checkMToM(
	sjme_attrInNotNull sjme_jmethodID fromMethod,
	sjme_attrInNotNull sjme_jmethodID toMethod);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_ACCESS_H
}
#undef SJME_CXX_ACCESS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_ACCESS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_ACCESS_H */
