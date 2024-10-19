/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME NanoCoat Virtual Machine Header Definitions.
 * 
 * @since 2023/07/25
 */

#ifndef SQUIRRELJME_NVM_H
#define SQUIRRELJME_NVM_H

#include <setjmp.h>

#include "sjme/closeable.h"
#include "sjme/config.h"
#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"
#include "sjme/alloc.h"
#include "sjme/list.h"
#include "sjme/atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_NVM_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Program counter address.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_pcAddr;

/**
 * Static linkage type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_staticLinkageType;

struct sjme_jobjectBase
{
	/** The reference count of this object, zero it becomes GCed. */
	sjme_jint refCount;
};

/**
 * Class type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobject sjme_jclass;

/**
 * Throwable type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobject sjme_jthrowable;

typedef union sjme_anyData
{
	/** Integer. */
	sjme_jint jint;
	
	/** Object. */
	sjme_jobject jobject;
	
	/** Temporary index. */
	sjme_tempIndex tempIndex;
} sjme_anyData;

typedef struct sjme_any
{
	/** Data type used. */
	sjme_basicTypeId type;

	/** Data stored within. */
	sjme_anyData data;
} sjme_any;

/**
 * Represents an identifier of a method.
 * 
 * @since 2024/10/19
 */
typedef struct sjme_jmethodIDBase* sjme_jmethodID;

/**
 * Represents an identifier of a field.
 * 
 * @since 2024/10/19
 */
typedef struct sjme_jfieldIDBase* sjme_jfieldID;

/**
 * The type of structure a type is.
 * 
 * @since 2024/08/09
 */
typedef enum sjme_nvm_structType
{
	/** Unknown. */
	SJME_NVM_STRUCT_UNKNOWN,
	
	/** Class information. */
	SJME_NVM_STRUCT_CLASS_INFO,
	
	/** Method code. */
	SJME_NVM_STRUCT_CODE,
	
	/** Field information. */
	SJME_NVM_STRUCT_FIELD_INFO,
	
	/** Thread frame. */
	SJME_NVM_STRUCT_FRAME,
	
	/** Identifier. */
	SJME_NVM_STRUCT_IDENTIFIER,
	
	/** Method information. */
	SJME_NVM_STRUCT_METHOD_INFO,
	
	/** Rom Library. */
	SJME_NVM_STRUCT_ROM_LIBRARY,
	
	/** Rom Suite. */
	SJME_NVM_STRUCT_ROM_SUITE,
	
	/** Constant pool. */
	SJME_NVM_STRUCT_POOL,
	
	/** NanoCoat state. */
	SJME_NVM_STRUCT_STATE,
	
	/** A string pool. */
	SJME_NVM_STRUCT_STRING_POOL,
	
	/** A string in the string pool. */
	SJME_NVM_STRUCT_STRING_POOL_STRING,
	
	/** A single task. */
	SJME_NVM_STRUCT_TASK,
	
	/** A single thread. */
	SJME_NVM_STRUCT_THREAD,
	
	/** The number of structure types. */
	SJME_NVM_NUM_STRUCT
} sjme_nvm_structType;

/**
 * Common data structure between all NanoCoat types.
 * 
 * @since 2024/08/09
 */
typedef struct sjme_nvm_commonBase sjme_nvm_commonBase;

/**
 * Common data structure pointer.
 * 
 * @since 2024/08/10
 */
typedef sjme_nvm_commonBase* sjme_nvm_common;

/** Cast to common type. */
#define SJME_AS_NVM_COMMON(x) ((sjme_nvm_common)(x))

/** Cast to common pointer type. */
#define SJME_AS_NVM_COMMONP(x) ((sjme_nvm_common*)(x))

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/08/08
 */
typedef struct sjme_nvm_stateBase sjme_nvm_stateBase;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
typedef sjme_nvm_stateBase* sjme_nvm;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/08/08
 */
typedef struct sjme_nvm_frameBase sjme_nvm_frameBase;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/07/25
 */
typedef sjme_nvm_frameBase* sjme_nvm_frame;

/**
 * Base structure for virtual machine threads.
 * 
 * @since 2024/08/08
 */
typedef struct sjme_nvm_threadBase sjme_nvm_threadBase;

/**
 * A thread within SquirrelJME.
 * 
 * @since 2024/08/08
 */
typedef sjme_nvm_threadBase* sjme_nvm_thread;

/** List of threads. */
SJME_LIST_DECLARE(sjme_nvm_thread, 0);

typedef struct sjme_static_constValue
{
	/** Integer value. */
	sjme_jint jint;
	
	/** Long value. */
	sjme_jlong jlong;
	
	/** Float value. */
	sjme_jfloat jfloat;
	
	/** Double value. */
	sjme_jdouble jdouble;
	
	/** String value. */
	sjme_lpcstr jstring;
	
	/** Class name. */
	sjme_lpcstr jclass;
} sjme_static_constValue;

/**
 * Represents a field type.
 * 
 * @since 2023/08/10
 */
typedef struct sjme_static_fieldType
{
	/** The hash code for the field type. */
	sjme_jint hashCode;
	
	/** The field descriptor. */
	sjme_lpcstr descriptor;
	
	/** The basic type. */
	sjme_basicTypeId basicType;
} sjme_static_fieldType;

typedef struct sjme_static_classField
{
	/** Field name. */
	sjme_lpstr name;
	
	/** The field type. */
	const sjme_static_fieldType* type;
	
	/** Flags. */
	sjme_jint flags;
	
	/** The constant value type. */
	sjme_basicTypeId valueType;
	
	/** The value. */
	sjme_static_constValue value;
} sjme_static_classField;

typedef struct sjme_static_classFields
{
	/** The number of fields. */
	sjme_jint count;
	
	/** Fields. */
	sjme_static_classField fields[sjme_flexibleArrayCount];
} sjme_static_classFields;

/**
 * Type used for method code functions.
 * 
 * @param currentState The current virtual machine state.
 * @param currentThread The current virtual machine thread.
 * @return Will return @c SJME_JNI_TRUE if execution completed without throwing
 * a @c Throwable object.
 * @since 2023/07/25
 */
typedef sjme_jboolean (*sjme_methodCodeFunction)(
	sjme_nvm currentState,
	sjme_nvm_thread currentThread);

/**
 * The variable mapping and setup for any given method.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_static_classCodeLimits
{
	/** The maximum number of @c sjme_basicTypeId local/stack variables. */
	const sjme_jubyte maxVariables[SJME_NUM_JAVA_TYPE_IDS];
} sjme_static_classCodeLimits;

/**
 * Contains information about method code and how variables should be placed
 * on execution and stack handling.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_static_classCode
{
	/** The variable count and thrown index count used. */
	const sjme_static_classCodeLimits* limits;
	
	/** The index where thrown objects are placed. */
	sjme_jshort thrownVarIndex;
	
	/** The method code. */
	sjme_methodCodeFunction code;
} sjme_static_classCode;

/**
 * Represents a standard Java method type, using field descriptors.
 * 
 * @since 2023/08/10
 */
typedef struct sjme_static_methodType
{
	/** The hash code for the method type. */
	sjme_jint hashCode;
	
	/** The descriptor for the method type. */
	sjme_lpcstr descriptor;
	
	/** The return type. */
	const sjme_static_fieldType* returnType;
	
	/** The number of arguments. */
	sjme_jint argCount;
	
	/** The arguments to the method. */
	const sjme_static_fieldType* argTypes[sjme_flexibleArrayCount];
} sjme_static_methodType;

typedef struct sjme_static_classMethod
{
	/** Method name. */
	sjme_lpcstr name;
	
	/** Flags. */
	sjme_jint flags;
	
	/** Name typed. */
	const sjme_static_methodType* type;
	
	/** Method code and any pertaining information. */
	const sjme_static_classCode* code;
} sjme_static_classMethod;

typedef struct sjme_static_classMethods
{
	/** The number of methods. */
	sjme_jint count;
	
	/** Methods. */
	sjme_static_classMethod methods[sjme_flexibleArrayCount];
} sjme_static_classMethods;

typedef struct sjme_static_classInterface
{
	sjme_lpcstr interfaceName;
} sjme_static_classInterface;

typedef struct sjme_static_classInterfaces
{
	/** The number of interfaces. */
	sjme_jint count;
	
	/** Interfaces. */
	sjme_static_classInterface interfaces[sjme_flexibleArrayCount];
} sjme_static_classInterfaces;

typedef struct sjme_static_resource
{
	/** The resource path. */
	sjme_lpcstr path;
	
	/** The hash for the path. */
	sjme_jint pathHash;
	
	/** The size of the resource. */
	sjme_jint size;
	
	/** The resource data. */
	const sjme_jbyte data[sjme_flexibleArrayCount];
} sjme_static_resource;

typedef struct sjme_static_linkage_data_classObject
{
	/** The class name. */
	sjme_lpcstr className;
} sjme_static_linkage_data_classObject;

typedef struct sjme_static_linkage_data_fieldAccess
{
	/** Is this static? */
	sjme_jboolean isStatic;
	
	/** Is this a store? */
	sjme_jboolean isStore;
	
	/** The source method name. */
	sjme_lpcstr sourceMethodName;
	
	/** The source method type. */
	sjme_lpcstr sourceMethodType;
	
	/** The target class. */
	sjme_lpcstr targetClass;
	
	/** The target field name. */
	sjme_lpcstr targetFieldName;
	
	/** The target field type. */
	sjme_lpcstr targetFieldType;
} sjme_static_linkage_data_fieldAccess;

typedef struct sjme_static_linkage_data_invokeSpecial
{
	/** The source method name. */
	sjme_lpcstr sourceMethodName;
	
	/** The source method type. */
	sjme_lpcstr sourceMethodType;
	
	/** The target class. */
	sjme_lpcstr targetClass;
	
	/** The target method name. */
	sjme_lpcstr targetMethodName;
	
	/** The target method type. */
	sjme_lpcstr targetMethodType;
} sjme_static_linkage_data_invokeSpecial;

typedef struct sjme_static_linkage_data_invokeNormal
{
	/** Is this a static invocation? */
	sjme_jboolean isStatic;

	/** The source method name. */
	sjme_lpcstr sourceMethodName;
	
	/** The source method type. */
	sjme_lpcstr sourceMethodType;
	
	/** The target class. */
	sjme_lpcstr targetClass;
	
	/** The target method name. */
	sjme_lpcstr targetMethodName;
	
	/** The target method type. */
	sjme_lpcstr targetMethodType;
} sjme_static_linkage_data_invokeNormal;

typedef struct sjme_static_linkage_data_stringObject
{
	/** The string value. */
	sjme_lpcstr string;
} sjme_static_linkage_data_stringObject;

typedef union sjme_static_linkage_data
{
	/** Reference to class object. */
	sjme_static_linkage_data_classObject classObject;
	
	/** Field access. */
	sjme_static_linkage_data_fieldAccess fieldAccess;
	
	/** Special invocation. */
	sjme_static_linkage_data_invokeSpecial invokeSpecial;
	
	/** Normal invocation. */
	sjme_static_linkage_data_invokeNormal invokeNormal;
	
	/** String object. */
	sjme_static_linkage_data_stringObject stringObject;
} sjme_static_linkage_data;

/**
 * Static linkage.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_static_linkage
{
	/** The type of linkage this is. */
	sjme_staticLinkageType type;
	
	/** Linkage data. */
	sjme_static_linkage_data data;
} sjme_static_linkage;

typedef struct sjme_static_linkages
{
	/** The number of linkages. */
	sjme_jint count;
	
	/** The define linkages. */
	sjme_static_linkage linkages[sjme_flexibleArrayCount];
} sjme_static_linkages;

typedef struct sjme_dynamic_linkage_data_classObject
{
	int todo;
} sjme_dynamic_linkage_data_classObject;

typedef struct sjme_dynamic_linkage_data_fieldAccess
{
	int todo;
} sjme_dynamic_linkage_data_fieldAccess;

typedef struct sjme_dynamic_linkage_data_invokeSpecial
{
	int todo;
} sjme_dynamic_linkage_data_invokeSpecial;

typedef struct sjme_dynamic_linkage_data_invokeNormal
{
	int todo;
} sjme_dynamic_linkage_data_invokeNormal;

typedef struct sjme_dynamic_linkage_data_stringObject
{
	int todo;
} sjme_dynamic_linkage_data_stringObject;

typedef union sjme_dynamic_linkage_data
{
	/** Reference to class object. */
	sjme_dynamic_linkage_data_classObject classObject;
	
	/** Field access. */
	sjme_dynamic_linkage_data_fieldAccess fieldAccess;
	
	/** Special invocation. */
	sjme_dynamic_linkage_data_invokeSpecial invokeSpecial;
	
	/** Normal invocation. */
	sjme_dynamic_linkage_data_invokeNormal invokeNormal;
	
	/** String object. */
	sjme_dynamic_linkage_data_stringObject stringObject;
} sjme_dynamic_linkage_data;

/**
 * Dynamic linkage.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_dynamic_linkage
{
	/** The type of linkage this is. */
	sjme_staticLinkageType type;
	
	/** Linkage data. */
	sjme_dynamic_linkage_data data;
} sjme_dynamic_linkage;

/**
 * Contains the payload information.
 * 
 * @since 2023/07/27
 */
typedef struct sjme_payload_config sjme_payload_config;

/**
 * Hook for garbage collection detection and/or cancel capability.
 * 
 * @param frame The frame this is garbage collecting in.
 * @param gcWhat what is being garbage collected?
 * @return Returns @c SJME_JNI_TRUE if garbage collection should continue.
 * @since 2023/11/17
 */
typedef sjme_jboolean (*sjme_nvm_stateHookGcFunc)(sjme_nvm_frame frame,
	sjme_jobject gcWhat);

/**
 * Hooks for alternative function.
 * 
 * @since 2023/11/17
 */
typedef struct sjme_nvm_stateHooks
{
	/** Garbage collection. */
	sjme_nvm_stateHookGcFunc gc;
} sjme_nvm_stateHooks;

/**
 * Boot parameters for NanoCoat.
 *
 * @since 2023/07/27
 */
typedef struct sjme_nvm_bootParam sjme_nvm_bootParam;

/**
 * Standard Suite structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_nvm_rom_suiteBase sjme_nvm_rom_suiteBase;

/**
 * Opaque suite structure type.
 *
 * @since 2023/12/22
 */
typedef struct sjme_nvm_rom_suiteBase* sjme_nvm_rom_suite;

/**
 * Structure for a single task.
 *
 * @since 2023/12/17
 */
typedef struct sjme_nvm_taskBase* sjme_nvm_task;

/** List of tasks. */
SJME_LIST_DECLARE(sjme_nvm_task, 0);

struct sjme_nvm_commonBase
{
	/** Closeable for this NanoCoat object. */
	sjme_closeableBase closeable;
	
	/** The type of item this is. */
	sjme_nvm_structType type;	
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The lock to access this common item. */
	sjme_thread_spinLock lock;
};

struct sjme_nvm_stateBase
{
	/** Common data. */
	sjme_nvm_commonBase common;
	
	/** The memory pool to use for allocations. */
	sjme_alloc_pool* allocPool;

	/** The reserved memory pool. */
	sjme_alloc_pool* reservedPool;

	/** The copy of the input boot parameters. */
	const sjme_nvm_bootParam* bootParamCopy;
	
	/** Hooks for the state. */
	const sjme_nvm_stateHooks* hooks;

	/** The suite containing all the libraries. */
	sjme_nvm_rom_suite suite;
	
	/** The tasks that are currently existing. */
	sjme_list_sjme_nvm_task* tasks;
	
	/** The next identifier for tasks. */
	sjme_atomic_sjme_jint nextTaskId;
	
	/* The next identifier for tasks. */
	sjme_atomic_sjme_jint nextThreadId;
};

/**
 * Method initialization start.
 *
 * @since 2023/07/25
 */
#define SJME_NANOCOAT_START_CALL ((sjme_pcAddr)-1)

/**
 * Method closing end.
 *
 * @since 2023/07/25
 */
#define SJME_NANOCOAT_END_CALL ((sjme_pcAddr)-2)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_NVM_H
}
		#undef SJME_CXX_SQUIRRELJME_NVM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_NVM_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_NVM_H */
