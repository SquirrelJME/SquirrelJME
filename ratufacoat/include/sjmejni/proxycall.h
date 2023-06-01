/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Proxy call handling to allow JNI calls to jump elsewhere such as another
 * kind of CPU or otherwise.
 * 
 * @since 2023/01/06
 */

#ifndef SQUIRRELJME_PROXYCALL_H
#define SQUIRRELJME_PROXYCALL_H

#include "sjmejni/sjmejni.h"
#include "sjmejni/tables/functionIds.h"
#include "sjmejni/tables/interfaceIds.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PROXYCALL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Represents the type of argument used for a proxy call.
 *
 * @since 2023/01/06
 */
typedef enum sjme_vmProxyCallArgType
{
	/** The stop/end type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_END,

	/** Boolean type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_BOOLEAN,

	/** Byte type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_BYTE,

	/** Character type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_CHARACTER,

	/** Short type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_SHORT,

	/** Integer type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_INTEGER,

	/** Long type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_LONG,

	/** Float type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_FLOAT,

	/** Double type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_DOUBLE,

	/** Object type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_OBJECT,

	/** A tread of argument pointers. */
	SJME_VM_PROXY_CALL_ARG_TYPE_ARGS,

	/** Field. */
	SJME_VM_PROXY_CALL_ARG_TYPE_FIELD,

	/** Method. */
	SJME_VM_PROXY_CALL_ARG_TYPE_METHOD,

	/** Size type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_SIZE,

	/** Set of characters. */
	SJME_VM_PROXY_CALL_ARG_TYPE_CHAR_STAR,

	/** Virtual machine state star star. */
	SJME_VM_PROXY_CALL_ARG_TYPE_VM_STATE_STAR_STAR,

	/** Boolean star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_BOOLEAN_STAR,

	/** Byte star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_BYTE_STAR,

	/** Character star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_CHARACTER_STAR,

	/** Short star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_SHORT_STAR,

	/** Integer star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_INTEGER_STAR,

	/** Long star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_LONG_STAR,

	/** Float star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_FLOAT_STAR,

	/** Double star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_DOUBLE_STAR,

	/** Object star type. */
	SJME_VM_PROXY_CALL_ARG_TYPE_OBJECT_STAR,

	/** Number of call arguments. */
	NUM_SJME_VM_PROXY_CALL_ARG_TYPE
} sjme_vmProxyCallArgType;

/**
 * Contains an argument for a proxy call.
 *
 * @since 2023/01/06
 */
typedef struct sjme_vmProxyCallArg
{
	/** The type of this argument. */
	sjme_vmProxyCallArgType type;

	/** The actual value for the argument. */
	union
	{
		/** Standard JNI value. */
		sjme_jvalue jni;

		/** Arguments pointer. */
		sjme_jvalue* args;

		/** Field. */
		sjme_vmField* field;

		/** Method. */
		sjme_vmMethod* method;

		/** Size. */
		sjme_jsize size;

		/** Star types. */
		union
		{
			/** Char star. */
			char* cChar;

			/** Virtual machine state star star. */
			sjme_vmState** vmStateStar;

			/** Multiple booleans. */
			sjme_jboolean* jboolean;

			/** Multiple bytes. */
			sjme_jbyte* jbyte;

			/** Multiple characters. */
			sjme_jchar* jchar;

			/** Multiple shorts. */
			sjme_jshort* jshort;

			/** Multiple integers. */
			sjme_jint* jint;

			/** Multiple longs. */
			sjme_jlong* jlong;

			/** Multiple floats. */
			sjme_jfloat* jfloat;

			/** Multiple doubles. */
			sjme_jdouble* jdouble;

			/** Multiple objects. */
			sjme_jobject* jobject;
		} star;
	} value;
} sjme_vmProxyCallArg;

/**
 * Stores the proxy data information, which determines which call to perform.
 *
 * @since 2023/01/06
 */
typedef struct sjme_vmProxyCallData
{
	/** The target state or thread. */
	union
	{
		/** The target thread. */
		sjme_vmThread* vmThread;

		/** The target virtual machine. */
		sjme_vmState* vmState;
	} target;

	/** The ID of the native method being called. */
	union
	{
		/** The function ID. */
		sjme_vmJniFunctionId function;

		/** The interface ID. */
		sjme_vmJniInterfaceId interface;
	} id;

	/** Arguments to the proxy call. */
	sjme_vmProxyCallArg* args;
} sjme_vmProxyCallData;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PROXYCALL_H
}
		#undef SJME_CXX_SQUIRRELJME_PROXYCALL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PROXYCALL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PROXYCALL_H */
