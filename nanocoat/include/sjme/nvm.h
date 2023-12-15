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

#include <stdlib.h>
#include <stdint.h>
#include <setjmp.h>

#include "sjme/config.h"

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
 * Posts two tokens together.
 * 
 * @param a The first token.
 * @param b The second token.
 * @since 2023/11/15
 */
#define SJME_TOKEN_PASTE(a, b) a##b

/**
 * Pasting two tokens but with preprocessing.
 * 
 * @param a The first token.
 * @param b The second token.
 * @since 2023/11/16
 */
#define SJME_TOKEN_PASTE_PP(a, b) SJME_TOKEN_PASTE(a, b)

/**
 * Stringifies the given token.
 * 
 * @param s The token to stringify.
 * @since 2023/11/24
 */
#define SJME_TOKEN_STRING(s) #s

/**
 * Stringifies the given token.
 * 
 * @param s The token to stringify.
 * @since 2023/11/24
 */
#define SJME_TOKEN_STRING_PP(s) SJME_TOKEN_STRING(s)

/** SquirrelJME version string. */
#define SQUIRRELJME_VERSION SJME_TOKEN_STRING_PP(SQUIRRELJME_VERSION_TRIM)

/**
 * Calculates the size of a struct member.
 * 
 * @param type The type of the struct.
 * @param member The member to check.
 * @return The size of the given member.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_STRUCT_MEMBER(type, member) \
	(sizeof((*((type*)0)).member))

/**
 * Boolean type.
 * 
 * @since 2023/07/25
 */
typedef enum sjme_jboolean
{
	SJME_JNI_FALSE = 0,

	SJME_JNI_TRUE = 1
} sjme_jboolean;

/**
 * Byte type.
 * 
 * @since 2023/07/25
 */
typedef int8_t sjme_jbyte;

/**
 * Unsigned byte type.
 * 
 * @since 2023/08/09
 */
typedef uint8_t sjme_jubyte;

/**
 * Short type.
 * 
 * @since 2023/07/25
 */
typedef int16_t sjme_jshort;

/**
 * Character type.
 * 
 * @since 2023/07/25
 */
typedef uint16_t sjme_jchar;

/**
 * Integer type.
 * 
 * @since 2023/07/25
 */
typedef int32_t sjme_jint;

/**
 * Unsigned integer type.
 * 
 * @since 2023/11/20
 */
typedef uint32_t sjme_juint;

/**
 * Long value.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_jlong
{
	/** High value. */
	sjme_jint hi;
	
	/** Low value. */
	sjme_juint lo;
} sjme_jlong;

/**
 * Float value.
 * 
 * @sinc 2023/07/25
 */
typedef struct sjme_jfloat
{
	sjme_jint value;
} sjme_jfloat;

/**
 * Double value.
 * 
 * @sinc 2023/07/25
 */
typedef struct sjme_jdouble
{
	/** High value. */
	sjme_juint hi;
	
	/** Low value. */
	sjme_juint lo;
} sjme_jdouble;

/**
 * Temporary index.
 * 
 * @since 2023/07/25
 */
typedef sjme_jint sjme_tempIndex;

/**
 * A wrapper used by front ends, which is reserved for use, which stores a
 * natively bound object accordingly as needed.
 * 
 * @since 2023/12/06
 */
typedef void* sjme_frontEndWrapper;

/**
 * Any data that is needed by the front end, which is reserved for use.
 *
 * @since 2023/12/14
 */
typedef void* sjme_frontEndData;

/**
 * This structure stores any front end data as needed.
 *
 * @since 2023/12/14
 */
typedef struct sjme_frontEnd
{
	/** Any wrapper as needed. */
	sjme_frontEndWrapper wrapper;

	/** Any data as needed. */
	sjme_frontEndData data;
} sjme_frontEnd;

/**
 * Wraps the given front end pointer.
 *
 * @param p The pointer to wrap.
 * @since 2023/12/08
 */
#define SJME_FRONT_END_WRAP(p) ((sjme_frontEndWrapper)(p))

/**
 * Basic data type identifier.
 * 
 * @since 2023/07/25
 */
typedef enum sjme_basicTypeId
{
	/** Integer. */
	SJME_BASIC_TYPE_ID_INTEGER = 0,
	
	/** Integer. */
	SJME_JAVA_TYPE_ID_INTEGER = SJME_BASIC_TYPE_ID_INTEGER,
	
	/** Long. */
	SJME_BASIC_TYPE_ID_LONG = 1,
	
	/** Long. */
	SJME_JAVA_TYPE_ID_LONG = SJME_BASIC_TYPE_ID_LONG,
	
	/** Float. */
	SJME_BASIC_TYPE_ID_FLOAT = 2,
	
	/** Float. */
	SJME_JAVA_TYPE_ID_FLOAT = SJME_BASIC_TYPE_ID_FLOAT,
	
	/** Double. */
	SJME_BASIC_TYPE_ID_DOUBLE = 3,
	
	/** Double. */
	SJME_JAVA_TYPE_ID_DOUBLE = SJME_BASIC_TYPE_ID_DOUBLE,
	
	/** Object. */
	SJME_BASIC_TYPE_ID_OBJECT = 4,
	
	/** Object. */
	SJME_JAVA_TYPE_ID_OBJECT = SJME_BASIC_TYPE_ID_OBJECT,
	
	/** Boolean or byte. */
	SJME_BASIC_TYPE_ID_BOOLEAN_OR_BYTE = 5,
	
	/** The number of Java type IDs. */
	SJME_NUM_JAVA_TYPE_IDS = SJME_BASIC_TYPE_ID_BOOLEAN_OR_BYTE,
	
	/** Short. */
	SJME_BASIC_TYPE_ID_SHORT = 6,
	
	/** Character. */
	SJME_BASIC_TYPE_ID_CHARACTER = 7,
	
	/** Number of basic type IDs. */
	SJME_NUM_BASIC_TYPE_IDS = 8
} sjme_basicTypeId;

/** The Java type ID. */
typedef sjme_basicTypeId sjme_javaTypeId;

/**
 * Represents multiple type IDs.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_basicTypeIds
{
	/** The number of IDs. */
	sjme_jint count;
	
	/** The IDs. */
	const sjme_javaTypeId ids[sjme_flexibleArrayCount];
} sjme_basicTypeIds;

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

/**
 * Base object information.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_jobjectBase
{
	/** The reference count of this object, zero it becomes GCed. */
	sjme_jint refCount;
} sjme_jobjectBase;

/**
 * Object type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobjectBase* sjme_jobject;

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
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
typedef struct sjme_nvm_state sjme_nvm_state;

/**
 * Frame of execution within a thread.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_nvm_frame sjme_nvm_frame;

/**
 * Exception stack trace mechanism storage.
 *
 * @since 2023/12/08
 */
typedef volatile struct sjme_exceptTrace sjme_exceptTrace;

typedef struct sjme_nvm_thread
{
	/** The VM state this thread is in. */
	sjme_nvm_state* inState;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The thread ID. */
	sjme_jint threadId;
	
	/** The top of the stack. */
	sjme_nvm_frame* top;
	
	/** The number of frames. */
	sjme_jint numFrames;

	/** Current exception handler go back. */
	volatile sjme_exceptTrace* except;
} sjme_nvm_thread;

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
	const char* jstring;
	
	/** Class name. */
	const char* jclass;
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
	const char* descriptor;
	
	/** The basic type. */
	sjme_basicTypeId basicType;
} sjme_static_fieldType;

typedef struct sjme_static_classField
{
	/** Field name. */
	const char* name;
	
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
 * @return Will return @c true if execution completed without throwing
 * a @c Throwable object.
 * @since 2023/07/25
 */
typedef sjme_jboolean (*sjme_methodCodeFunction)(
	struct sjme_nvm_state* currentState,
	struct sjme_nvm_thread* currentThread);

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
	const char* descriptor;
	
	/** The return type. */
	const sjme_static_fieldType* returnType;
	
	/** The number of arguments. */
	sjme_jint argCount;
	
	/** The arguments to the method. */
	const sjme_static_fieldType* argTypes[0];
} sjme_static_methodType;

typedef struct sjme_static_classMethod
{
	/** Method name. */
	const char* name;
	
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
	const char* interfaceName;
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
	const char* path;
	
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
	const char* className;
} sjme_static_linkage_data_classObject;

typedef struct sjme_static_linkage_data_fieldAccess
{
	/** Is this static? */
	sjme_jboolean isStatic;
	
	/** Is this a store? */
	sjme_jboolean isStore;
	
	/** The source method name. */
	const char* sourceMethodName;
	
	/** The source method type. */
	const char* sourceMethodType;
	
	/** The target class. */
	const char* targetClass;
	
	/** The target field name. */
	const char* targetFieldName;
	
	/** The target field type. */
	const char* targetFieldType;
} sjme_static_linkage_data_fieldAccess;

typedef struct sjme_static_linkage_data_invokeSpecial
{
	/** The source method name. */
	const char* sourceMethodName;
	
	/** The source method type. */
	const char* sourceMethodType;
	
	/** The target class. */
	const char* targetClass;
	
	/** The target method name. */
	const char* targetMethodName;
	
	/** The target method type. */
	const char* targetMethodType;
} sjme_static_linkage_data_invokeSpecial;

typedef struct sjme_static_linkage_data_invokeNormal
{
	/** Is this a static invocation? */
	sjme_jboolean isStatic;

	/** The source method name. */
	const char* sourceMethodName;
	
	/** The source method type. */
	const char* sourceMethodType;
	
	/** The target class. */
	const char* targetClass;
	
	/** The target method name. */
	const char* targetMethodName;
	
	/** The target method type. */
	const char* targetMethodType;
} sjme_static_linkage_data_invokeNormal;

typedef struct sjme_static_linkage_data_stringObject
{
	/** The string value. */
	const char* string;
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
 * Represents the frame of a stack tread.
 * 
 * @since 2023/11/15
 */
typedef struct sjme_nvm_frameTread
{
	/** The number of items in this tread. */
	sjme_jint count;
	
	/** The base index for the stack index. */
	sjme_jint stackBaseIndex;
	
	/** The maximum size this tread can be. */
	sjme_jint max;
	
	/** Values within the tread. */
	union
	{
		/** Integer values. */
		sjme_jint jints[sjme_flexibleArrayCountUnion];
		
		/** Long values. */
		sjme_jlong jlongs[sjme_flexibleArrayCountUnion];
		
		/** Float values. */
		sjme_jfloat jfloats[sjme_flexibleArrayCountUnion];
		
		/** Double values. */
		sjme_jdouble jdoubles[sjme_flexibleArrayCountUnion];
		
		/** Object references. */
		sjme_jobject jobjects[sjme_flexibleArrayCountUnion];
	} values;
} sjme_nvm_frameTread;

/**
 * Calculates the size of a frame tread for a given type.
 * 
 * @param type The type to get the size for.
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/15
 */
#define SJME_SIZEOF_FRAME_TREAD(type, count, baseType) \
	(sizeof(sjme_nvm_frameTread) + \
	/* Need to handle cases where values could be aligned up... */ \
	(offsetof(sjme_nvm_frameTread, values.SJME_TOKEN_PASTE(baseType,s)[0]) - \
		offsetof(sjme_nvm_frameTread, values)) + \
	(sizeof(type) * (size_t)(count)))

/**
 * Calculates the size of a frame tread for a given type via variable.
 * 
 * @param typeId The type to get the size for.
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/15
 */
static inline sjme_attrArtificial size_t SJME_SIZEOF_FRAME_TREAD_VAR(
	sjme_javaTypeId typeId, sjme_jint count)
{
	switch (typeId)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jint, count, jint);
		
		case SJME_JAVA_TYPE_ID_LONG:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jlong, count, jlong);
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jfloat, count, jfloat);
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jdouble, count, jdouble);
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			return SJME_SIZEOF_FRAME_TREAD(sjme_jobject, count, jobject);
	}
	
	/* Invalid. */
	return 0;
}

/**
 * Represents information on a frame's stack storage.
 * 
 * @since 2023/11/16
 */
typedef struct sjme_nvm_frameStack
{
	/** The number of items in the stack. */
	sjme_jint count;
	
	/** The current limit of this structure. */
	sjme_jint limit;
	
	/** The stack order. */
	sjme_javaTypeId order[sjme_flexibleArrayCount];
} sjme_nvm_frameStack;

/**
 * Calculates the size of a frame stack.
 * 
 * @param count The number if items to store.
 * @return The size in bytes for the tread.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_FRAME_STACK(count) \
	(sizeof(sjme_nvm_frameStack) + \
	(sizeof(sjme_javaTypeId) * (size_t)(count)))

typedef struct sjme_nvm_frameLocalMap
{
	/** The maximum number of locals. */
	sjme_jint max;
	
	/** Mapping of a specific variable to a given type index. */
	union
	{
		sjme_jbyte to[SJME_NUM_JAVA_TYPE_IDS];
	} maps[sjme_flexibleArrayCount];
} sjme_nvm_frameLocalMap;

/**
 * Calculates the size of the frame local variable map.
 * 
 * @param count The number of items in the mapping.
 * @return The size in bytes of the local mapping.
 * @since 2023/11/26
 */
#define SJME_SIZEOF_FRAME_LOCAL_MAP(count) \
	(sizeof(sjme_nvm_frameLocalMap) + \
	(SJME_SIZEOF_STRUCT_MEMBER(sjme_nvm_frameLocalMap, maps[0]) * (count)))

struct sjme_nvm_frame
{
	/** The thread this frame is in. */
	sjme_nvm_thread* inThread;
	
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;
	
	/** The parent frame. */
	sjme_nvm_frame* parent;
	
	/** The frame index in the thread. */
	sjme_jint frameIndex;
	
	/** The current program counter. */
	sjme_pcAddr pc;
	
	/** Object which is waiting to be thrown for exception handling. */
	sjme_jobject waitingThrown;
	
	/** Frame linkage. */
	sjme_dynamic_linkage* linkage;
	
	/** Temporary stack. */
	sjme_any* tempStack;
	
	/** Reference to this. */
	sjme_jobject thisRef;
	
	/** Class reference. */
	sjme_jclass classObjectRef;
	
	/** The current stack information. */
	sjme_nvm_frameStack* stack;
	
	/** Treads for the stack and locals. */
	sjme_nvm_frameTread* treads[SJME_NUM_BASIC_TYPE_IDS];
	
	/** Mapping of local variables to the tread indexes per type. */
	const sjme_nvm_frameLocalMap* localMap;
};

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
typedef sjme_jboolean (*sjme_nvm_stateHookGcFunc)(sjme_nvm_frame* frame,
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
 * Structure which stores the pooled memory allocator.
 *
 * @since 2023/11/18
 */
typedef struct sjme_alloc_pool sjme_alloc_pool;

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
typedef struct sjme_rom_suite sjme_rom_suite;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
struct sjme_nvm_state
{
	/** The wrapper in the front end. */
	sjme_frontEnd frontEnd;

	/** The memory pool to use for allocations. */
	sjme_alloc_pool* allocPool;

	/** The reserved memory pool. */
	sjme_alloc_pool* reservedPool;

	/** The copy of the input boot parameters. */
	const sjme_nvm_bootParam* bootParamCopy;
	
	/** Hooks for the state. */
	const sjme_nvm_stateHooks* hooks;

	/* The suite containing all of the libraries. */
	sjme_rom_suite* suite;
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

/**
 * Error codes.
 * 
 * @since 2023/11/14
 */
typedef enum sjme_errorCode
{
	/** No error. */
	SJME_ERROR_NONE = 1,

	/** Generic unknown error. */
	SJME_ERROR_UNKNOWN = 0,

	/** Generic unknown error. */
	SJME_ERROR_UNKNOWN_NEGATIVE = -1,
	
	/** Null arguments. */
	SJME_ERROR_NULL_ARGUMENTS = -2,
	
	/** Local variable out of bounds. */
	SJME_ERROR_LOCAL_INDEX_INVALID = -3,
	
	/** Stack variable out of bounds. */
	SJME_ERROR_STACK_INDEX_INVALID = -4,
	
	/** Stack underflow. */
	SJME_ERROR_STACK_UNDERFLOW = -5,
	
	/** Stack overflow. */
	SJME_ERROR_STACK_OVERFLOW = -6,
	
	/** Top is not an integer type. */
	SJME_ERROR_TOP_NOT_INTEGER = -7,
	
	/** Top is not a long type. */
	SJME_ERROR_TOP_NOT_LONG = -8,
	
	/** Top is not a float type. */
	SJME_ERROR_TOP_NOT_FLOAT = -9,
	
	/** Top is not a double type. */
	SJME_ERROR_TOP_NOT_DOUBLE = -10,
	
	/** Top is not a object type. */
	SJME_ERROR_TOP_NOT_OBJECT = -11,
	
	/** Frame is missing stack treads. */
	SJME_ERROR_FRAME_MISSING_STACK_TREADS = -12,
	
	/** Invalid read of stack. */
	SJME_ERROR_STACK_INVALID_READ = -13,
	
	/** Invalid write of stack. */
	SJME_ERROR_STACK_INVALID_WRITE = -14,
	
	/** Invalid read of stack. */
	SJME_ERROR_LOCAL_INVALID_READ = -15,
	
	/** Invalid write of stack. */
	SJME_ERROR_LOCAL_INVALID_WRITE = -16,
	
	/** Invalid reference pop. */
	SJME_ERROR_INVALID_REFERENCE_POP = -17,
	
	/** Invalid reference push. */
	SJME_ERROR_INVALID_REFERENCE_PUSH = -18,
	
	/** Failed to garbage collect object. */
	SJME_ERROR_COULD_NOT_GC_OBJECT = -19,
	
	/** Object reference count is not zero. */
	SJME_ERROR_OBJECT_REFCOUNT_NOT_ZERO = -20,
	
	/** Garbage collection of object cancelled. */
	SJME_ERROR_OBJECT_GC_CANCELLED = -21,

	/** Out of memory. */
	SJME_ERROR_OUT_OF_MEMORY = -22,

	/** Pool initialization failed. */
	SJME_ERROR_POOL_INIT_FAILED = -23,

	/** Invalid argument. */
	SJME_ERROR_INVALID_ARGUMENT = -24,

	/** Not implemented. */
	SJME_ERROR_NOT_IMPLEMENTED = -25,

	/** Invalid tread read. */
	SJME_ERROR_TREAD_INVALID_READ = -26,

	/** Invalid tread write. */
	SJME_ERROR_TREAD_INVALID_WRITE = -27,
	
	/** The number of error codes. */
	SJME_NUM_ERROR_CODES = -28
} sjme_errorCode;

/**
 * Is this expression considered an error?
 *
 * @param x The expression.
 * @since 2023/12/08
 */
#define SJME_IS_ERROR(x) (SJME_ERROR_NONE != (x))

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
