/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Proxy instance objects which are virtually mapped to native code
 * similarly to MLE shelves, however their action is fully determined based on
 * the interfaces which are implemented. These are in effect, virtual proxy
 * classes that are created by the virtual machine and as such only interfaces
 * are valid targets.
 *
 * @file
 * @since 2026/09/19
 */

#ifndef SJME_C_SQUIRRELJME_INSTANCEPROXY_H
#define SJME_C_SQUIRRELJME_INSTANCEPROXY_H

#include "sjme/nvm/instance.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_INSTANCEPROXY_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Any data for the proxied method call, this is for convenience to add more
 * potential fields in the future without requiring major function changes.
 *
 * @since 2026/09/20
 */
typedef struct sjme_nvm_instance_proxyMethod
{
	/** The ID of the method that was called. */
	sjme_jmethodID id;

	/** The name of the method being called. */
	sjme_lpcstr methodName;

	/** The type of the method being called. */
	sjme_lpcstr methodType;
} sjme_nvm_instance_proxyMethod;

/**
 * Handles a call to a proxy method.
 *
 * @param inFrame The frame this is being called under.
 * @param proxyInstance The instance of the proxy class.
 * @param proxyMethod The proxied method which is being called.
 * @param argR Return value for the proxy call.
 * @param argC The number of passed arguments.
 * @param argV The arguments passed to the method.
 * @return Any resultant error, if any.
 * @since 2026/09/19
 */
typedef sjme_errorCode (*sjme_nvm_instance_proxyHandlerFunc)(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jobject proxyInstance,
	sjme_attrInNotNull const sjme_nvm_instance_proxyMethod* proxyMethod,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInNotNull sjme_jint argC,
	sjme_attrInNotNull sjme_jvalueTyped* argV);

/**
 * Initialize a proxy class which can then be used to initialize new object
 * instances which call @link sjme_nvm_instance_proxyHandlerFunc @endlink to
 * handle method calls. This cannot be used to create a proxy class of an
 * already existing proxy class. Only instance methods are proxied.
 *
 * @param contextThread The thread this is initializing under.
 * @param outClass The resultant class.
 * @param handler The handler for proxied calls.
 * @param inInterfaces The interfaces to be proxied, these must only be
 * interfaces.
 * @return Any resultant error, if any.
 * @since 2026/09/19
 */
sjme_errorCode sjme_nvm_instance_proxyClassL(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInNotNull sjme_list(sjme_jclass)* inInterfaces);

/**
 * Initialize a proxy class which can then be used to initialize new object
 * instances which call @link sjme_nvm_instance_proxyHandlerFunc @endlink to
 * handle method calls. This cannot be used to create a proxy class of an
 * already existing proxy class. Only instance methods are proxied.
 *
 * @param contextThread The thread this is initializing under.
 * @param outClass The resultant class.
 * @param handler The handler for proxied calls.
 * @param inInterfaces The interfaces to be proxied, these must only be
 * interfaces.
 * @param ... Continued interfaces to be proxied, terminated
 * by @code NULL @endcode.
 * @return Any resultant error, if any.
 * @since 2026/09/19
 */
sjme_errorCode sjme_nvm_instance_proxyClassV(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInNotNull sjme_nvm_instance_proxyHandlerFunc handler,
	sjme_attrInNotNull sjme_jclass inInterfaces,
	sjme_attrInNotNull ...);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_INSTANCEPROXY_H
}
#undef SJME_CXX_SQUIRRELJME_INSTANCEPROXY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_INSTANCEPROXY_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_INSTANCEPROXY_H */