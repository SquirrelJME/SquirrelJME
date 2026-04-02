/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#ifndef LIBEMULATORBASE_SQUIRRELJMEMLE_H
#define LIBEMULATORBASE_SQUIRRELJMEMLE_H

#include "squirreljme.h"

#if !defined(mleGroupId)
	#error mleGroupId is not defined
#endif

#if !defined(mleShelfClass)
	#error mleShelfClass is not defined
#endif

#pragma region(names)

/** The name of the MLE group. */
#define MLE_GROUP_NAME \
	SJME_TOKEN_PASTE_PP(mle, mleGroupId)

/** The init function name. */
#define MLE_INIT_FUNC_NAME \
	SJME_TOKEN_PASTE_PP(MLE_GROUP_NAME, Init)

/** The init methods name. */
#define MLE_INIT_METHODS_NAME \
	SJME_TOKEN_PASTE_PP(MLE_GROUP_NAME, Methods)

/** Name of a single MLE function. */
#define MLE_FUNC_NAME(methodName) \
	FORWARD_FUNC_NAME(MLE_GROUP_NAME, \
		SJME_TOKEN_PASTE_PP(__, methodName))

/** Alternative implementation name of a single MLE function. */
#define MLE_FUNC_NAME_ALT(methodName, alt) \
	FORWARD_FUNC_NAME_ALT(MLE_GROUP_NAME, \
	SJME_TOKEN_PASTE_PP(__, methodName), alt)

#define MLE_BOX_void Void
#define MLE_BOX_jboolean Boolean
#define MLE_BOX_jint Int
#define MLE_BOX_jlong Long
#define MLE_BOX_jfloat Float
#define MLE_BOX_jdouble Double
#define MLE_BOX_jobject Object

#define MLE_RETURN_void(tokens)
#define MLE_RETURN_jboolean(tokens) tokens
#define MLE_RETURN_jint(tokens) tokens
#define MLE_RETURN_jlong(tokens) tokens
#define MLE_RETURN_jfloat(tokens) tokens
#define MLE_RETURN_jdouble(tokens) tokens
#define MLE_RETURN_jobject(tokens) tokens

/** The boxed type of the given C type. */
#define MLE_BOX(type) \
	SJME_TOKEN_PASTE_PP(MLE_BOX_, type)

/** Emit the given tokens if there is a return type. */
#define MLE_RETURN(type, tokens) \
	SJME_TOKEN(SJME_TOKEN_PASTE_PP(MLE_RETURN_, type))(tokens)

/** The JNI method to call. */
#define MLE_CALL_STATIC_METHOD(type) \
	SJME_TOKEN_PASTE3_PP(CallStatic, MLE_BOX(type), MethodV)

#pragma endregion(names)
#pragma region(functions)

/** MLE Function prototype. */
#define MLE_FUNC_PROTO(returnType, methodName, ...) \
	JNIEXPORT returnType JNICALL MLE_FUNC_NAME(methodName) \
		(JNIEnv* env, jclass classy, __VA_ARGS__)

/** MLE Function prototype (alternate). */
#define MLE_FUNC_PROTO_ALT(returnType, methodName, alt, ...) \
	JNIEXPORT returnType JNICALL MLE_FUNC_NAME_ALT(methodName, alt) \
		(JNIEnv* env, jclass classy, __VA_ARGS__)

/** Proxied call to a JNI implementation. */
#define MLE_FUNC_PROXY_STATIC(returnType, methodName) \
	MLE_FUNC_PROTO(returnType, methodName, ...) \
	{ \
		va_list args; \
		forwardMethod forwardedTo; \
		MLE_RETURN(returnType, returnType rv;) \
		\
		/* Load in arguments. */ \
		va_start(args, classy); \
		\
		/* Find the forward method. */ \
		forwardedTo = findForwardMethod(env, \
			SJME_TOKEN(mleProxyTarget), \
			SJME_TOKEN_STRING(methodName), \
			SJME_TOKEN(SJME_TOKEN_PASTE_PP(MLE_DESC_, methodName))); \
		\
		/* Forward to JNI dispatch. */ \
		MLE_RETURN(returnType, rv =) \
			(*env)->MLE_CALL_STATIC_METHOD(returnType)(env, \
				forwardedTo.xclass, \
				forwardedTo.xmeth, args); \
		\
		/* Cleanup. */ \
		va_end(args); \
		\
		MLE_RETURN(returnType, return rv;) \
	}

#pragma endregion(functions)
#pragma region(lists)

/** Begin MLE function list. */
#define MLE_LIST_BEGIN() \
	static const JNINativeMethod MLE_INIT_METHODS_NAME[] = {

/** Single item in the MLE list. */
#define MLE_LIST_ITEM(methodName) \
	{ \
		SJME_TOKEN_STRING(methodName), \
		SJME_TOKEN(SJME_TOKEN_PASTE_PP(MLE_DESC_, methodName)), \
		MLE_FUNC_NAME(methodName) \
	}

/** End MLE function list. */
#define MLE_LIST_END() \
	}; \
	jint JNICALL MLE_INIT_FUNC_NAME(JNIEnv* env, jclass classy) \
	{ \
		return (*env)->RegisterNatives(env, \
			(*env)->FindClass(env, mleShelfClass), \
			MLE_INIT_METHODS_NAME, \
			sizeof(MLE_INIT_METHODS_NAME) / sizeof(JNINativeMethod)); \
	}

#pragma endregion(lists)

#endif //LIBEMULATORBASE_SQUIRRELJMEMLE_H
