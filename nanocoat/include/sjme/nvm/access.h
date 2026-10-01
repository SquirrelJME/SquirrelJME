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
 * @file
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
 * Checks if the given object is compatible with the given field.
 * 
 * @param contextThread The context thread. 
 * @param fieldId The field identifier.
 * @param checkValue The value to check.
 * @return On any resultant error, if any.
 * @since 2025/07/09
 */
sjme_errorCode sjme_nvm_access_checkCompatibleField(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jfieldID fieldId,
	sjme_attrInNotNull sjme_jvalueTyped* checkValue);
	
/**
 * Determines whether the current member has access to the given member.
 * 
 * @param from The source member.
 * @param to The destination member.
 * @param toFlags The flags of the destination member.
 * @return Any resultant error, if any.
 * @since 2025/06/21
 */
sjme_errorCode sjme_nvm_access_checkEToE(
	sjme_attrInNotNull sjme_jmemberID from,
	sjme_attrInNotNull sjme_jmemberID to,
	sjme_attrInNotNull sjme_nvm_class_memberFlags* toFlags);
	
/**
 * Determines whether the current frame has access to the given field.
 * 
 * @param from The source frame.
 * @param to The destination field.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_access_checkFToF(
	sjme_attrInNotNull sjme_nvm_frame from,
	sjme_attrInNotNull sjme_jfieldID to);

/**
 * Determines whether the current frame has access to the given method.
 * 
 * @param from The source frame.
 * @param to The destination method.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_access_checkFToM(
	sjme_attrInNotNull sjme_nvm_frame from,
	sjme_attrInNotNull sjme_jmethodID to);

/**
 * Determines whether the current method has access to the given method.
 * 
 * @param from The source method.
 * @param to The destination method.
 * @return Any resultant error, if any.
 * @since 2025/06/19
 */
sjme_errorCode sjme_nvm_access_checkMToM(
	sjme_attrInNotNull sjme_jmethodID from,
	sjme_attrInNotNull sjme_jmethodID to);

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
