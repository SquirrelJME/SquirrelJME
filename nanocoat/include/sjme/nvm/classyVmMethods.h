/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Class Methods.
 * 
 * @file
 * @since 2026/01/12
 */

#ifndef SJME_C_SQUIRRELJME_CLASSYVMMETHODS_H
#define SJME_C_SQUIRRELJME_CLASSYVMMETHODS_H

#include "sjme/nvm/classyVmMembers.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/classy.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_CLASSYVMMETHODS_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The basic type of call for a method.
 * 
 * @since 2024/11/07
 */
typedef enum sjme_nvm_methodCallType
{
	/** Non-virtual, special, call. */
	SJME_NVM_CALL_NON_VIRTUAL,
	
	/** Virtual call. */
	SJME_NVM_CALL_VIRTUAL,
	
	/** Virtual super call. */
	SJME_NVM_CALL_SUPER,
	
	/** The number of call types. */
	SJME_NVM_NUM_METHOD_CALL_TYPE,
} sjme_nvm_methodCallType;

struct sjme_jinterfaceIDBase
{
	/** Common virtual machine info. */
	sjme_nvm_commonBase common;

	/** The class this interface is. */
	sjme_jclass isInterface;

	/** The hash of the descriptor of the interface being implemented. */
	sjme_jint descriptorHash;

	/** The methods which are bound to this interface instance. */
	sjme_list(sjme_jmethodID)* methods;
};
	
struct sjme_jmethodIDBase
{
	/** Member information. */
	sjme_jmemberIDBase member;

	/** The method flags. */
	sjme_nvm_class_methodFlags flags;
	
	/** The info this is bound to, for virtual and non-virtual calls. */
	sjme_nvm_class_methodInfo info[SJME_NVM_NUM_METHOD_CALL_TYPE];
	
	/** Bits to assist in quicker method determinations. */
	sjme_nvm_class_methodInfoBits bits;
};
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_CLASSYVMMETHODS_H
}
#undef SJME_CXX_SQUIRRELJME_CLASSYVMMETHODS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_CLASSYVMMETHODS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_CLASSYVMMETHODS_H */
