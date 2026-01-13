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
	
/**
 * Looks up a method ID from an interface call.
 * 
 * @param contextThread The current context thread.
 * @param required Is this required to be found?
 * @param outID The resultant method ID.
 * @param forObject The object this is for.
 * @param forMember The interface this is invoking.
 * @return Any resultant error, if any.
 * @since 2025/04/01
 */
sjme_errorCode sjme_nvm_vmMethod_idByInterface(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrOutNotNull sjme_jmethodID* outID,
	sjme_attrInNotNull sjme_jobject forObject,
	sjme_attrInNotNull sjme_nvm_class_poolEntryMember* forMember);
	
/**
 * Locates a method by the given name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The thread this request is under.
 * @param instanceType The instance type of the method.
 * @param required Is this method required?
 * @param inName The name of the method to find.
 * @param inType The type of the method to find.
 * @param outID The resultant method ID.
 * @return Any resultant error, if any.
 * @since 2024/11/13 
 */
sjme_errorCode sjme_nvm_vmMethod_idByNameType(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_charSeq inName,
	sjme_attrInPositive sjme_charSeq inType,
	sjme_attrOutNotNull sjme_jmethodID* outID);
	
/**
 * Locates a method by the given name and type.
 * 
 * @param inClass The class to look within.
 * @param contextThread The thread this request is under.
 * @param instanceType The instance type of the method.
 * @param required Is this method required?
 * @param inName The name of the method to find.
 * @param inType The type of the method to find.
 * @param outID The resultant method ID.
 * @return Any resultant error, if any.
 * @since 2025/03/16 
 */
sjme_errorCode sjme_nvm_vmMethod_idByNameTypeU(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_lpcstr inName,
	sjme_attrInPositive sjme_lpcstr inType,
	sjme_attrOutNotNull sjme_jmethodID* outID);

/**
 * Locates the source method in the given class chain for the given static
 * or instance method ID, which would be the source target method for the given
 * method slot. This does not take into consideration overridden methods
 * or otherwise.
 * 
 * @param inClass The class tree to look within. 
 * @param instanceType The type of instance this is.
 * @param required Is this method required?
 * @param methodId The method identifier.
 * @param outInfo The output info.
 * @return Any resultant error.
 * @since 2024/11/03
 */
sjme_errorCode sjme_nvm_vmMethod_sourceByIndex(
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInValue sjme_jboolean required,
	sjme_attrInPositive sjme_jint methodId,
	sjme_attrOutNotNull sjme_nvm_class_methodInfo* outInfo);

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
