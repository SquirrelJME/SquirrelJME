/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME defined JNI interface.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_SJMEJNI_H
#define SQUIRRELJME_SJMEJNI_H

#include "ccfeatures.h"

#if defined(SJME_HAS_STDINT_H)
	#include <stdint.h>
#endif

/* Standard Includes. */
#include <stdlib.h>
#include <stddef.h>
#include <limits.h>
#include <string.h>
#include <stdio.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SJME_JNI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_HAS_STDINT_H)
	/** Byte. */
	typedef int8_t sjme_jbyte;

	/** Character. */
	typedef uint16_t sjme_jchar;

	/** Short. */
	typedef int16_t sjme_jshort;

	/** Integer. */
	typedef int32_t sjme_jint;

	/** Long. */
	typedef int64_t sjme_jlong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

#elif defined(SJME_FEATURE_MSVC)
	/** Byte. */
	typedef signed __int8 sjme_jbyte;

	/** Character. */
	typedef unsigned __int16 sjme_jchar;

	/** Short. */
	typedef signed __int16 sjme_jshort;

	/** Integer. */
	typedef signed __int32 sjme_jint;

	/** Long. */
	typedef signed __int64 sjme_jlong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

	/** Signed 8-bit constant. */
	#define INT8_C(x) x

	/** Signed 16-bit constant. */
	#define INT16_C(x) x

	/** Signed 32-bit constant. */
	#define INT32_C(x) x

	/** Unsigned 8-bit constant. */
	#define UINT8_C(x) x##U

	/** Unsigned 16-bit constant. */
	#define UINT16_C(x) x##U

	/** Unsigned 32-bit constant. */
	#define UINT32_C(x) x##U
#else
	#error No standard types are known.
#endif

#if defined(SJME_FEATURE_GCC)
	typedef enum __attribute__((__packed__)) sjme_jboolean
	{
		/** False. */
		sjme_false = INT8_C(0),

		/** True. */
		sjme_true = INT8_C(1)
	} sjme_jboolean;
#else
	/** False. */
	#define sjme_false INT8_C(0)

	/** True. */
	#define sjme_true INT8_C(1)

	#if defined(SJME_HAS_STDINT_H)
		/** Boolean. */
		typedef uint8_t sjme_jboolean;
	#elif defined(SJME_FEATURE_MSVC)
		/** Boolean. */
		typedef unsigned __int8 sjme_jboolean;
	#else
		#error Unknown boolean type.
	#endif
#endif

/** Interface version 1.1. */
#define SJME_INTERFACE_VERSION_1_1 INT32_C(0x00010001)

/** Interface version 1.2. */
#define SJME_INTERFACE_VERSION_1_2 INT32_C(0x00010002)

/** Interface version 1.4. */
#define SJME_INTERFACE_VERSION_1_4 INT32_C(0x00010004)

/** Interface version 1.6. */
#define SJME_INTERFACE_VERSION_1_6 INT32_C(0x00010006)

/** No interface error. */
#define SJME_INTERFACE_ERROR_NONE INT32_C(0)

/** The virtual machine is detached. */
#define SJME_INTERFACE_ERROR_DETACHED INT32_C(-2)

/** The virtual machine version is incorrect. */
#define SJME_INTERFACE_ERROR_INVALID_VERSION INT32_C(-3)

/** Size type. */
typedef sjme_jint sjme_jsize;

/**
 * The reference type of an object.
 *
 * @since 2022/12/17
 */
typedef enum sjme_jobjectReferenceType
{
	/** Invalid reference type. */
	SJME_JOBJECT_REF_TYPE_INVALID = 0,

	/** Local reference type. */
	SJME_JOBJECT_REF_TYPE_LOCAL = 1,

	/** Global reference type. */
	SJME_JOBJECT_REF_TYPE_GLOBAL = 2,

	/** Weak global reference type. */
	SJME_JOBJECT_REF_TYPE_WEAK_GLOBAL = 3
} sjme_jobjectReferenceType;

/** Object type. */
typedef struct sjme_jobject* sjme_jobject;

/** Class type. */
typedef struct sjme_jobject* sjme_jclass;

/** String type. */
typedef struct sjme_jstring* sjme_jstring;

/** Throwable. */
typedef struct sjme_jthrowable* sjme_jthrowable;

/** Weak reference. */
typedef struct sjme_jweakReference* sjme_jweakReference;

/** Array type. */
typedef struct sjme_jarray* sjme_jarray;

/** Boolean array type. */
typedef struct sjme_jbooleanArray* sjme_jbooleanArray;

/** Byte array type. */
typedef struct sjme_jbyteArray* sjme_jbyteArray;

/** Character array type. */
typedef struct sjme_jcharArray* sjme_jcharArray;

/** Short array type. */
typedef struct sjme_jshortArray* sjme_jshortArray;

/** Integer array type. */
typedef struct sjme_jintArray* sjme_jintArray;

/** Long array type. */
typedef struct sjme_jlongArray* sjme_jlongArray;

/** Float array type. */
typedef struct sjme_jfloatArray* sjme_jfloatArray;

/** Double array type. */
typedef struct sjme_jdoubleArray* sjme_jdoubleArray;

/** Object array type. */
typedef struct sjme_jobjectArray* sjme_jobjectArray;

/**
 * Value type.
 *
 * @since 2022/12/11
 */
typedef union sjme_jvalue
{
	sjme_jboolean z;
	sjme_jbyte b;
	sjme_jchar c;
	sjme_jshort s;
	sjme_jint i;
	sjme_jlong j;
	sjme_jfloat f;
	sjme_jdouble d;
	sjme_jobject l;
} sjme_jvalue;

/** Field within the VM. */
typedef struct sjme_vmField* sjme_vmField;

/** Method within the VM. */
typedef struct sjme_vmMethod* sjme_vmMethod;

/**
 * Virtual machine initialization options.
 *
 * @since 2022/12/11
 */
typedef struct sjme_vmInitOption
{
	/** The option specified on the launch of the virtual machine. */
	char* optionString;

	/** Extra argument information to the option. */
	void* extraInfo;
} sjme_vmInitOption;

/**
 * Initialization arguments for the virtual machine.
 *
 * @since 2022/12/11
 */
typedef struct sjme_vmCmdLine
{
	/** The requesting virtual machine version. */
	sjme_jint version;

	/** The number of specified options. */
	sjme_jint nOptions;

	/** Virtual machine options. */
	sjme_vmInitOption* options;

	/** Ignore unrecognized options? */
	sjme_jboolean ignoreUnrecognized;
} sjme_vmCmdLine;

/** Virtual machine functions. */
typedef struct sjme_vmThread* sjme_vmThread;

/** Virtual machine state. */
typedef struct sjme_vmState* sjme_vmState;

/**
 * Native method registration interface.
 *
 * @since 2022/12/17
 */
typedef struct sjme_vmRegisterNative
{
	/** The name of the method. */
	char* name;

	/** The type signature of the method. */
	char* signature;

	/** The function pointer of the method. */
	void* fnPtr;
} sjme_vmRegisterNative;

/** Declare function pointer. */
#define SJME_FUNC_PTR__(x) (*x)

/**
 * Virtual machine state.
 *
 * @since 2022/12/11
 */
struct sjme_vmState
{
	#include "sjmejni/tables/interfaceStructFields.h"

	void* reservedEnd;
};

/**
 * Virtual machine functions.
 *
 * @since 2022/12/11
 */
struct sjme_vmThread
{
	#include "sjmejni/tables/functionStructFields.h"
};

/* Clear this. */
#undef SJME_FUNC_PTR__

/**
 * Contains the functions which are used to interact with the system to
 * implement the core virtual machine and all of the various shelf interfaces.
 *
 * @since 2022/12/16
 */
typedef struct sjme_vmSysApi
{
	/**
	 * Returns the line ending type of the system.
	 *
	 * @return One of @c cc.squirreljme.jvm.mle.constants.LineEndingType .
	 * @since 2022/12/16
	 */
	sjme_jint (*runtimeLineEnding)(void);
} sjme_vmSysApi;

/**
 * Serialization function.
 *
 * @param buf The buffer containing bytes to write.
 * @param off The offset into the buffer.
 * @param len The number of bytes to write.
 * @return If the write was successful.
 * @since 2022/12/18
 */
typedef sjme_jboolean (*sjme_vmSerializeDataWriteFunc)(const sjme_jbyte* buf,
	sjme_jsize off, sjme_jsize len);

/**
 * Deserialization function.
 *
 * @param buf The buffer containing the buffer to read into.
 * @param off The offset into the buffer.
 * @param len The number of bytes to attempt read.
 * @return The number of bytes read or @c -1 on success, other negative values
 * are indicative of failure.
 * @since 2022/12/18
 */
typedef sjme_jint (*sjme_vmDeserializeDataReadFunc)(sjme_jbyte* buf,
	sjme_jsize off, sjme_jsize len);

/**
 * Called by the deserializer to attach to a given thread when the virtual
 * machine has been deserialized, this should setup any threads and restore
 * state accordingly.
 *
 * @param vm The virtual machine being utilized.
 * @param thread The thread that needs to be attached.
 * @return If thread attaching is successful.
 * @since 2022/12/18
 */
typedef sjme_jboolean (*sjme_vmDeserializeThreadAttach)(sjme_vmState* vm,
	sjme_vmThread* thread);

/**
 * Initializes the arguments with the defaults.
 *
 * @param vmArgs The arguments to fill.
 * @return One of the interface error codes.
 * @since 2022/12/11
 */
sjme_jint sjme_vmDefaultInitArgs(sjme_vmCmdLine* vmArgs);

/**
 * Returns all of the VMs which are active.
 *
 * @param vmBuf The output virtual machines.
 * @param bufLen The length of the buffer.
 * @param nVMs The number of written VMs.
 * @return One of the interface error codes.
 * @since 2022/12/11
 */
sjme_jint sjme_vmGetAllVms(sjme_vmState** vmBuf, sjme_jsize bufLen,
	sjme_jsize* nVMs);

/**
 * Creates a virtual machine along with the functions and state for it.
 *
 * @param outVm The virtual machine output.
 * @param outThread The output thread environment.
 * @param vmArgs The arguments to the virtual machine.
 * @param sysApi System API handles, interacts with the system for operations
 * such as file access and the other shelves.
 * @return One of the interface error codes.
 * @since 2022/12/11
 */
sjme_jint sjme_vmNew(sjme_vmState** outVm, sjme_vmThread** outThread,
	sjme_vmCmdLine* vmArgs, sjme_vmSysApi* sysApi);

/**
 * Ticks the virtual machine executing any functions as needed and otherwise
 * for execution.
 *
 * @param vm The virtual machine being ticked.
 * @param thread The thread to be ticked
 * @param count The number of times to tick before returning.
 * @return If ticking was successful.
 * @since 2022/12/18
 */
sjme_jboolean sjme_vmTick(sjme_vmState* vm, sjme_vmThread* thread,
	sjme_jsize count);

/**
 * Serializes the virtual machine state into the given pointers.
 *
 * @param vmState The input virtual machine state.
 * @param write The serialization function.
 * @return If serialization was successful.
 * @since 2022/128
 */
sjme_jboolean sjme_vmSerialize(sjme_vmState* vmState,
	sjme_vmSerializeDataWriteFunc write);

/**
 * Deserializes the virtual machine and sets up a virtual machine state.
 *
 * @param vmState The input virtual machine state.
 * @param read The serialization function.
 * @param threadAttach This is called after deserialization to attach all
 * of the threads accordingly into the virtual machine.
 * @return If serialization was successful.
 * @since 2022/128
 */
sjme_jboolean sjme_vmDeserialize(sjme_vmState** vmState,
	sjme_vmDeserializeDataReadFunc read,
	sjme_vmDeserializeThreadAttach threadAttach);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H
}
		#undef SJME_CXX_SQUIRRELJME_SJME_JNI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SJMEJNI_H */
