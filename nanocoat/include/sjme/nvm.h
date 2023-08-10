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
 * Unsigned byte type.
 * 
 * @since 2023/08/09
 */
typedef uint8_t jubyte;

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
typedef enum sjme_basicTypeId
{
	/** Integer. */
	BASIC_TYPE_ID_INTEGER = 0,
	
	/** Integer. */
	JAVA_TYPE_ID_INTEGER = BASIC_TYPE_ID_INTEGER,
	
	/** Long. */
	BASIC_TYPE_ID_LONG = 1,
	
	/** Long. */
	JAVA_TYPE_ID_LONG = BASIC_TYPE_ID_LONG,
	
	/** Float. */
	BASIC_TYPE_ID_FLOAT = 2,
	
	/** Float. */
	JAVA_TYPE_ID_FLOAT = BASIC_TYPE_ID_FLOAT,
	
	/** Double. */
	BASIC_TYPE_ID_DOUBLE = 3,
	
	/** Double. */
	JAVA_TYPE_ID_DOUBLE = BASIC_TYPE_ID_DOUBLE,
	
	/** Object. */
	BASIC_TYPE_ID_OBJECT = 4,
	
	/** Object. */
	JAVA_TYPE_ID_OBJECT = BASIC_TYPE_ID_OBJECT,
	
	/** Boolean or byte. */
	BASIC_TYPE_ID_BOOLEAN_OR_BYTE = 5,
	
	/** The number of Java type IDs. */
	SJME_NUM_JAVA_TYPE_IDS = BASIC_TYPE_ID_BOOLEAN_OR_BYTE,
	
	/** Short. */
	BASIC_TYPE_ID_SHORT = 6,
	
	/** Character. */
	BASIC_TYPE_ID_CHARACTER = 7,
	
	/** Number of basic type IDs. */
	SJME_NUM_BASIC_TYPE_IDS = 8
} sjme_basicTypeId;

/**
 * Represents multiple type IDs.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_basicTypeIds
{
	/** The number of IDs. */
	jint count;
	
	/** The IDs. */
	const sjme_basicTypeId ids[];
} sjme_basicTypeIds;

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

/**
 * Represents a field type.
 * 
 * @since 2023/08/10
 */
typedef struct sjme_static_fieldType
{
	/** The hash code for the field type. */
	jint hashCode;
	
	/** The field descriptor. */
	const char* descriptor;
} sjme_static_fieldType;

typedef struct sjme_static_classField
{
	/** Field name. */
	const char* name;
	
	/** The field type. */
	const sjme_static_fieldType* type;
	
	/** The basic field type. */
	sjme_basicTypeId basicType;
	
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

/**
 * The variable mapping and setup for any given method.
 * 
 * @since 2023/08/09
 */
typedef struct sjme_static_classCodeLimits
{
	/** The maximum number of @c sjme_basicTypeId local/stack variables. */
	const jubyte maxVariables[SJME_NUM_JAVA_TYPE_IDS];
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
	jshort thrownVarIndex;
	
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
	jint hashCode;
	
	/** The return type. */
	const sjme_static_fieldType* returnType;
	
	/** The number of arguments. */
	jint argCount;
	
	/** The arguments to the method. */
	const sjme_static_fieldType* argTypes[0];
} sjme_static_methodType;

/**
 * Represents basic method types, which is used by the code execution system
 * to determine how a method is called.
 * 
 * @since 2023/08/10
 */
typedef struct sjme_static_methodBasicType
{
	/** The hash code for the basic method type. */
	jint hashCode;
	
	/** The return type of the method. */
	sjme_basicTypeId returnType;
	
	/** The number of arguments. */
	jint argCount;
	
	/** The arguments to the method. */
	sjme_basicTypeId argTypes[0];
} sjme_static_methodBasicType;

typedef struct sjme_static_classMethod
{
	/** Method name. */
	const char* name;
	
	/** Flags. */
	jint flags;
	
	/** Name typed. */
	const sjme_static_methodType* type;
	
	/** Basic method type. */
	const sjme_static_methodBasicType* basicType;
	
	/** Method code and any pertaining information. */
	const sjme_static_classCode* code;
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
	
	/** Linkages, effectively the constant pool. */
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

jboolean sjme_nvm_arrayLength(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject arrayInstance,
	sjme_attrOutNotNull jint* outLen)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_arrayLoadIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_basicTypeId primitiveType,
	sjme_attrInNullable jobject arrayInstance,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;
	
jboolean sjme_nvm_arrayStore(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_basicTypeId primitiveType,
	sjme_attrInNullable jobject arrayInstance,
	sjme_attrInValue sjme_attrInPositive jint index,
	sjme_attrInNotNull sjme_any* value)
	sjme_attrCheckReturn;
	
jboolean sjme_nvm_checkCast(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* type)
	sjme_attrCheckReturn;
	
jboolean sjme_nvm_countReferenceDown(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance)
	sjme_attrCheckReturn;
	
sjme_tempIndex sjme_nvm_fieldGetToTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_fieldAccess* field)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

jboolean sjme_nvm_fieldPut(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_fieldAccess* field,
	sjme_attrInNotNull sjme_any* value)
	sjme_attrCheckReturn;

jboolean sjme_nvm_invokeNormal(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_invokeNormal* method)
	sjme_attrCheckReturn;

jboolean sjme_nvm_invokeSpecial(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_invokeSpecial* method)
	sjme_attrCheckReturn;
	
jint sjme_nvm_localLoadInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPopDouble(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPopFloat(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPopInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPopLong(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPopReference(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPushDouble(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPushFloat(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPushInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPushLong(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;

jboolean sjme_nvm_localPushReference(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index)
	sjme_attrCheckReturn;
	
jboolean sjme_nvm_localStoreInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index,
	sjme_attrInValue jint value)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_lookupClassObjectIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* classLinkage)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;
	
sjme_tempIndex sjme_nvm_lookupStringIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_stringObject* stringLinkage)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

jboolean sjme_nvm_monitor(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance,
	sjme_attrInValue jboolean isEnter)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_newArrayIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* componentType,
	sjme_attrInValue sjme_attrInPositive jint length)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_newInstanceIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* linkage)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;
	
jboolean sjme_nvm_returnFromMethod(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_any* value)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPopAny(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrOutNotNull sjme_any* output)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_stackPopAnyToTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

jint sjme_nvm_stackPopInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrOutNegativeOnePositive;

jobject sjme_nvm_stackPopReference(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrOutNullable;

jboolean sjme_nvm_stackPopReferenceThenThrow(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_stackPopReferenceToTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushAny(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_any* input)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushAnyFromTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInPositive sjme_tempIndex input)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushDoubleParts(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue jint hi,
	sjme_attrInValue jint lo)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushFloatRaw(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue jint rawValue)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue jint value)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushIntegerIsInstanceOf(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* type)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushLongParts(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue jint hi,
	sjme_attrInValue jint lo)
	sjme_attrCheckReturn;
	
jboolean sjme_nvm_stackPushReference(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance)
	sjme_attrCheckReturn;

jboolean sjme_nvm_stackPushReferenceFromTemp(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInPositive sjme_tempIndex tempIndex)
	sjme_attrCheckReturn;

jboolean sjme_nvm_tempDiscard(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrCheckReturn;

jboolean sjme_nvm_throwExecute(
	sjme_attrInNotNull sjme_nvm_frame* frame)
	sjme_attrCheckReturn;

/**
 * Ticks the virtual machine.
 * 
 * @param state The state to tick, @c -1 means to tick forever.
 * @param maxTics The number of ticks to execute before returning.
 * @return Returns @c JNI_TRUE if the machine is still running.
 * @since 2023/07/27
 */
jboolean sjme_nvm_tick(sjme_attrInNotNull sjme_nvm_state* state,
	sjme_attrInValue sjme_attrInPositive jint maxTics)
	sjme_attrCheckReturn;

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
