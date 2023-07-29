/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
 * Boolean type.
 * 
 * @since 2023/07/25
 */
typedef uint8_t jboolean;

/**
 * Byte type.
 * 
 * @since 2023/07/25
 */
typedef int8_t jbyte;

/**
 * Short type.
 * 
 * @since 2023/07/25
 */
typedef int16_t jshort;

/**
 * Character type.
 * 
 * @since 2023/07/25
 */
typedef uint16_t jchar;

/**
 * Integer type.
 * 
 * @since 2023/07/25
 */
typedef int32_t jint;

/**
 * Long value.
 * 
 * @since 2023/07/25
 */
typedef struct jlong
{
	/** High value. */
	jint hi;
	
	/** Low value. */
	jint lo;
} jlong;

/**
 * Float value.
 * 
 * @sinc 2023/07/25
 */
typedef struct jfloat
{
	jint value;
} jfloat;

/**
 * Double value.
 * 
 * @sinc 2023/07/25
 */
typedef struct jdouble
{
	/** High value. */
	jint hi;
	
	/** Low value. */
	jint lo;
} jdouble;

/**
 * Temporary index.
 * 
 * @since 2023/07/25
 */
typedef jint sjme_tempIndex;

/**
 * Basic data type identifier.
 * 
 * @since 2023/07/25
 */
typedef jint sjme_basicTypeId;

/**
 * Program counter address.
 * 
 * @since 2023/07/25
 */
typedef jint sjme_pcAddr;

/**
 * Static linkage type.
 * 
 * @since 2023/07/25
 */
typedef jint sjme_staticLinkageType;

/**
 * Base object information.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_jobjectBase
{
	int todo;
} sjme_jobjectBase;

/**
 * Object type.
 * 
 * @since 2023/07/25
 */
typedef sjme_jobjectBase* jobject;

/**
 * Class type.
 * 
 * @since 2023/07/25
 */
typedef jobject jclass;

/**
 * Throwable type.
 * 
 * @since 2023/07/25
 */
typedef jobject jthrowable;

typedef union sjme_anyData
{
	/** Integer. */
	jint jint;
	
	/** Object. */
	jobject jobject;
	
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

typedef struct sjme_nvm_thread
{
	/** The top of the stack. */
	sjme_nvm_frame* top;
} sjme_nvm_thread;

typedef struct sjme_static_constValue
{
	/** Integer value. */
	jint jint;
	
	/** Long value. */
	jlong jlong;
	
	/** Float value. */
	jfloat jfloat;
	
	/** Double value. */
	jdouble jdouble;
	
	/** String value. */
	const char* jstring;
	
	/** Class name. */
	const char* jclass;
} sjme_static_constValue;

typedef struct sjme_static_classField
{
	/** Field name. */
	const char* name;
	
	/** Field type. */
	const char* type;
	
	/** Flags. */
	jint flags;
	
	/** The constant value type. */
	sjme_basicTypeId valueType;
	
	/** The value. */
	sjme_static_constValue value;
} sjme_static_classField;

typedef struct sjme_static_classFields
{
	/** The number of fields. */
	jint count;
	
	/** Fields. */
	sjme_static_classField fields[];
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
typedef jboolean (*sjme_methodCodeFunction)(
	struct sjme_nvm_state* currentState,
	struct sjme_nvm_thread* currentThread);

typedef struct sjme_static_classMethod
{
	/** Method name. */
	const char* name;
	
	/** Method type. */
	const char* type;
	
	/** Flags. */
	jint flags;
	
	/** Argument slots, how many it takes up for calls. */
	jint argSlots;
	
	/** The number of slots the return value takes up. */
	jint rValSlots;
	
	/** Method code. */
	sjme_methodCodeFunction code;
} sjme_static_classMethod;

typedef struct sjme_static_classMethods
{
	/** The number of methods. */
	jint count;
	
	/** Methods. */
	sjme_static_classMethod methods[];
} sjme_static_classMethods;

typedef struct sjme_static_classInterface
{
	const char* interfaceName;
} sjme_static_classInterface;

typedef struct sjme_static_classInterfaces
{
	/** The number of interfaces. */
	jint count;
	
	/** Interfaces. */
	sjme_static_classInterface interfaces[];
} sjme_static_classInterfaces;

typedef struct sjme_static_resource
{
	/** The resource path. */
	const char* path;
	
	/** The hash for the path. */
	jint pathHash;
	
	/** The size of the resource. */
	jint size;
	
	/** The resource data. */
	const jbyte data[];
} sjme_static_resource;

typedef struct sjme_static_linkage_data_classObject
{
	/** The class name. */
	const char* className;
} sjme_static_linkage_data_classObject;

typedef struct sjme_static_linkage_data_fieldAccess
{
	/** Is this static? */
	jboolean isStatic;
	
	/** Is this a store? */
	jboolean isStore;
	
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
	jboolean isStatic;

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
	jint count;
	
	/** The define linkages. */
	sjme_static_linkage linkages[];
} sjme_static_linkages;

typedef struct sjme_static_classInfo
{
	/** This class name. */
	const char* thisName;
	
	/** Hash of the current class name. */
	int thisNameHash;
	
	/** The super name. */
	const char* superName;
	
	/** Interfaces. */
	const sjme_static_classInterfaces* interfaceNames;
	
	/** Flags. */
	jint flags;
	
	/** Fields. */
	const sjme_static_classFields* fields;
	
	/** Methods. */
	const sjme_static_classMethods* methods;
	
	/** Linkages. */
	const sjme_static_linkages* linkages;
} sjme_static_classInfo;

typedef struct sjme_static_library_classes
{
	/** The number of classes. */
	jint count;
	
	/** Class set. */
	const struct sjme_static_classInfo* classes[];
} sjme_static_library_classes;

typedef struct sjme_static_library_resources
{
	/** The number of resources. */
	jint count;
	
	/** Resource set. */
	const struct sjme_static_resource* resources[];
} sjme_static_library_resources;

typedef struct sjme_static_library
{
	/** Library name. */
	const char* name;
	
	/** Hashcode for the name. */
	jint nameHash;
	
	/** The hash of the original library, to detect changes. */
	const char* originalLibHash;
	
	/** Resources. */
	const sjme_static_library_resources* resources;
	
	/** Classes. */
	const sjme_static_library_classes* classes;
} sjme_static_library;

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

struct sjme_nvm_frame
{
	/** The current program counter. */
	sjme_pcAddr pc;
	
	/** Object which is waiting to be thrown for exception handling. */
	jobject waitingThrown;
	
	/** Frame linkage. */
	sjme_dynamic_linkage* linkage;
	
	/** Temporary stack. */
	sjme_any* tempStack;
	
	/** Reference to this. */
	jobject thisRef;
	
	/** Class reference. */
	jclass classObjectRef;
};

typedef struct sjme_static_libraries
{
	/** The number of libraries. */
	jint count;
	
	/** The libraries. */
	const sjme_static_library* libraries[];
} sjme_static_libraries;

/**
 * ROM file.
 * 
 * @since 2023/07/25
 */
typedef struct sjme_static_rom
{
	/** The ROM source set. */
	const char* sourceSet;
	
	/** The ROM clutter level. */
	const char* clutterLevel;
	
	/** The ROM libraries, is always last. */
	const sjme_static_libraries* libraries;
} sjme_static_rom;

/**
 * Contains the payload information.
 * 
 * @since 2023/07/27
 */
typedef struct sjme_static_payload sjme_static_payload;

/**
 * Boot configuration for NanoCoat.
 * 
 * @since 2023/07/27
 */
typedef struct sjme_nvm_bootConfig
{
	/** The payload to use for booting the virtual machine. */
	const sjme_static_payload* payload;
} sjme_nvm_bootConfig;

/**
 * Represents the virtual machine state.
 * 
 * @since 2023/07/28
 */
struct sjme_nvm_state
{
	/** The copy of the boot config. */
	sjme_nvm_bootConfig bootConfig;
	
	/** Combined library set. */
	sjme_static_libraries* libraries;
};

/**
 * True value.
 * 
 * @since 2023/07/25
 */
#define JNI_TRUE ((jboolean)1)

/**
 * False value.
 * 
 * @since 2023/07/25
 */
#define JNI_FALSE ((jboolean)0)

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

jint sjme_nvm_arrayLength(sjme_nvm_frame* frame,
	jobject arrayInstance);

sjme_tempIndex sjme_nvm_arrayLoadIntoTemp(sjme_nvm_frame* frame,
	sjme_basicTypeId primitiveType,
	jobject arrayInstance,
	jint index);
	
jboolean sjme_nvm_arrayStore(sjme_nvm_frame* frame,
	sjme_basicTypeId primitiveType,
	jobject arrayInstance,
	jint index,
	sjme_any* value);
	
/**
 * Boots the virtual machine.
 * 
 * @param config The configuration to use.
 * @param outState The output state of the virtual machine.
 * @param argc The number of arguments passed to the executable.
 * @param argv The command line arguments passed to the executable.
 * @return The booted virtual machine.
 * @since 2023/07/27
 */
jboolean sjme_nvm_boot(const sjme_nvm_bootConfig* config,
	sjme_nvm_state** outState, int argc, char** argv);
	
jboolean sjme_nvm_checkCast(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_classObject* type);
	
jboolean sjme_nvm_countReferenceDown(sjme_nvm_frame* frame,
	jobject instance);

/**
 * Destroys the virtual machine.
 * 
 * @param state The state to destroy.
 * @return If destruction was successful.
 * @since 2023/07/27
 */
jboolean sjme_nvm_destroy(sjme_nvm_state* state, jint* exitCode);
	
sjme_tempIndex sjme_nvm_fieldGetToTemp(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_fieldAccess* field);

jboolean sjme_nvm_fieldPut(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_fieldAccess* field,
	sjme_any* value);

jboolean sjme_nvm_invokeNormal(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_invokeNormal* method);

jboolean sjme_nvm_invokeSpecial(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_invokeSpecial* method);
	
jint sjme_nvm_localLoadInteger(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPopDouble(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPopFloat(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPopInteger(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPopLong(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPopReference(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPushDouble(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPushFloat(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPushInteger(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPushLong(sjme_nvm_frame* frame,
	jint index);

jboolean sjme_nvm_localPushReference(sjme_nvm_frame* frame,
	jint index);
	
jboolean sjme_nvm_localStoreInteger(sjme_nvm_frame* frame,
	jint index,
	jint value);

sjme_tempIndex sjme_nvm_lookupClassObjectIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_classObject* classObjectLinkage);
	
sjme_tempIndex sjme_nvm_lookupStringIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_stringObject* stringObjectLinkage);

jboolean sjme_nvm_monitor(sjme_nvm_frame* frame,
	jobject instance,
	jboolean isEnter);

sjme_tempIndex sjme_nvm_newArrayIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_classObject* componentType,
	jint length);

sjme_tempIndex sjme_nvm_newInstanceIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_classObject* linkage);
	
jboolean sjme_nvm_returnFromMethod(sjme_nvm_frame* frame,
	sjme_any* value);

jboolean sjme_nvm_stackPopAny(sjme_nvm_frame* frame,
	sjme_any* output);

sjme_tempIndex sjme_nvm_stackPopAnyToTemp(sjme_nvm_frame* frame);

jint sjme_nvm_stackPopInteger(sjme_nvm_frame* frame);

jobject sjme_nvm_stackPopReference(sjme_nvm_frame* frame);

jboolean sjme_nvm_stackPopReferenceThenThrow(sjme_nvm_frame* frame);

sjme_tempIndex sjme_nvm_stackPopReferenceToTemp(sjme_nvm_frame* frame);

jboolean sjme_nvm_stackPushAny(sjme_nvm_frame* frame,
	sjme_any* input);

jboolean sjme_nvm_stackPushAnyFromTemp(sjme_nvm_frame* frame,
	sjme_tempIndex input);

jboolean sjme_nvm_stackPushDoubleParts(sjme_nvm_frame* frame,
	jint hi,
	jint lo);

jboolean sjme_nvm_stackPushFloatRaw(sjme_nvm_frame* frame,
	jint rawValue);

jboolean sjme_nvm_stackPushInteger(sjme_nvm_frame* frame,
	jint value);

jboolean sjme_nvm_stackPushIntegerIsInstanceOf(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_classObject* type);

jboolean sjme_nvm_stackPushLongParts(sjme_nvm_frame* frame,
	jint hi,
	jint lo);
	
jboolean sjme_nvm_stackPushReference(sjme_nvm_frame* frame,
	jobject instance);

jboolean sjme_nvm_stackPushReferenceFromTemp(sjme_nvm_frame* frame,
	sjme_tempIndex tempIndex);

jboolean sjme_nvm_tempDiscard(sjme_nvm_frame* frame);

jboolean sjme_nvm_throwExecute(sjme_nvm_frame* frame);

/**
 * Ticks the virtual machine.
 * 
 * @param state The state to tick, @c -1 means to tick forever.
 * @param maxTics The number of ticks to execute before returning.
 * @return Returns @c JNI_TRUE if the machine is still running.
 * @since 2023/07/27
 */
jboolean sjme_nvm_tick(sjme_nvm_state* state, jint maxTics);

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
